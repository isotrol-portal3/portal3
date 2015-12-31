/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.InheritedPageSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PageSelModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ToolbarSupport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.PageDetailPanel;


/**
 * Superclass for management of pages shown in grids: Special, Template or Default pages
 * 
 * @author Manuel Ruiz
 * 
 */
public abstract class AbstractGridPagesManagement extends AbstractPagesManagement {

	/**
	 * Width in pixels for column <b>Name</b>.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 100;
	/**
	 * Width in pixels for column <b>Description</b>.<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 200;

	/** pages store */
	private ListStore<InheritedPageSelModelData> store = null;

	/** pages grid */
	private Grid<InheritedPageSelModelData> grid = null;

	/*
	 * Injected deps
	 */
	/**
	 * Cell renderer for ID field.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * The grid store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Returns the class of pages managed by this class implementation.<br/>
	 * 
	 * @return {@link PageClass#SPECIAL} | {@link PageClass#TEMPLATE} | {@link PageClass#ERROR} |
	 * {@link PageClass#DEFAULT}. (<code>DEFAULT</code> represents DEFAULT <b>AND</b> MAIN, since both types are managed
	 * in the same widget).<br/> Used for discriminatory grid refreshes.
	 */
	protected abstract PageClass getManagedPageClass();

	/**
	 * Constructor provided with all injectable dependencies.
	 * 
	 * @param pagesService
	 * @param messages
	 * @param pmsMessages
	 * @param pmsStyles
	 */
	@Inject
	public AbstractGridPagesManagement() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement#addListeners()
	 */
	@Override
	protected void addListeners() {
		// add "Refresh" button listener:
		super.addListeners();

		ToolbarSupport tbSupport = getToolBarSupport();
		// New
		tbSupport.getMiNewPage().addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				newPageTemplate(getPageClass());
			}
		});
		// Edit
		tbSupport.getMiEditPage().addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}
		});
		// Design
		tbSupport.getMiDesign().addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				InheritedPageSelModelData item = grid.getSelectionModel().getSelectedItem();
				if (item != null) {
					// creates the page locator
					tryGetPage(getPageLoc(item), true);
				}
			}
		});
		// Preview
		tbSupport.getMiPreviewPage().addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				InheritedPageSelModelData item = grid.getSelectionModel().getSelectedItem();
				if (item != null) {
					// creates the page locator
					openPreviewWindow(getPageLoc(item));
				}
			}
		});

		tbSupport.getMiDeletePage().addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				final InheritedPageSelModelData item = grid.getSelectionModel().getSelectedItem();

				if (item != null) {
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent we) {
							Button clicked = we.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryDeletePage(new PageLoc(getPortalPagesLoc(), item.getDTO().getValue().getId()));
							}
						}
					};
					MessageBox.confirm(getMessages().headerConfirmWindow(), getPmsMessages().msgConfirmDeletePage(),
						lConfirm).setModal(true);
				}
			}
		});
	}

	/**
	 * Retrieves from service the detailed data of the selected Page, and displays that data in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		InheritedPageSelModelData item = grid.getSelectionModel().getSelectedItem();
		if (item != null && item.getDTO().getValue().getId() != null) {
			// creates the page locator
			tryGetPage(getPageLoc(item), false);
		} else {
			// open a creation page detail panel
			newPageTemplate(getPageClass());
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractPagesManagement#addSpecificToolItems()
	 */
	@Override
	protected void addSpecificToolItems() {
		// nothing to do
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement
	 * #addSpecificComponents()
	 */
	@Override
	protected final void addSpecificComponents() {
		addGrid();
	}

	/**
	 * Returns the PageLoc that corresponds to the passed page.<br/>
	 * 
	 * @param psModelData
	 * @return the PageLoc that corresponds to the passed page
	 */
	protected final PageLoc getPageLoc(InheritedPageSelModelData psModelData) {
		return new PageLoc(getPortalPagesLoc(), psModelData.getDTO().getValue().getId());
	}

	/**
	 * Creates, configures and adds the pages grid to the panel.<br/>
	 */
	@SuppressWarnings("unchecked")
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		// id
		ColumnConfig column = new ColumnConfig(PageSelModelData.PROPERTY_ID, getMessages().columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		column.setSortable(false);
		column.setRenderer(idCellRenderer);
		configs.add(column);

		// name
		column = new ColumnConfig();
		column.setId(PageSelModelData.PROPERTY_NAME);
		column.setHeaderText(getPmsMessages().columnHeaderName());
		column.setWidth(COLUMN_NAME_WIDTH);
		GridCellRenderer<InheritedPageSelModelData> renderer = getNameCellRenderer();
		if (renderer != null) {
			column.setRenderer(renderer);
		}
		configs.add(column);

		// description:
		column = new ColumnConfig();
		column.setId(PageSelModelData.PROPERTY_DESCRIPTION);
		column.setWidth(COLUMN_DESCRIPTION_WIDTH);
		column.setHeaderText(getPmsMessages().columnHeaderDescription());
		configs.add(column);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<InheritedPageSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(PageSelModelData.PROPERTY_NAME);

		getToolBarSupport().maybeBindFilter((Store) store);

		grid = new Grid<InheritedPageSelModelData>((ListStore<InheritedPageSelModelData>) store, cm);

		GridSelectionModel<InheritedPageSelModelData> sm = new GridSelectionModel<InheritedPageSelModelData>();

		sm.setSelectionMode(SelectionMode.SINGLE);
		sm.addSelectionChangedListener(new SelectionChangedListener<InheritedPageSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<InheritedPageSelModelData> se) {
				controlEnabledButtons(se.getSelectedItem());
			}
		});
		grid.setSelectionModel(sm);

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<?>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<?> be) {
				getSelectedAndDisplayDetails();
			}
		});

		grid.setAutoExpandColumn(PageSelModelData.PROPERTY_NAME);
		grid.setLoadMask(true);
		grid.getView().setForceFit(true);

		add(grid);
	}

	/**
	 * Returns the implementation-specific <b>name</b> property cell renderer.<br/> Returns <code>null</code> if the
	 * implementation does not need any special cell rendering.
	 * 
	 * @return the implementation-specific <b>name</b> property cell renderer.
	 */
	protected GridCellRenderer<InheritedPageSelModelData> getNameCellRenderer() {
		return new NameCellRenderer();
	}

	/**
	 * Enables or disables buttons according to the passed SelectionChangedEvent.<br/>
	 * 
	 * @param pageSelModelData
	 */
	protected void controlEnabledButtons(InheritedPageSelModelData pageSelModelData) {
		boolean enabled = false;
		boolean inherited = false;
		boolean designed = false;
		if (pageSelModelData != null) {
			enabled = true;
			inherited = pageSelModelData.getDTO().isInherited();
		}
		if (pageSelModelData != null && getCurrentDevice().getDTO().isLayout()) {
			designed = true;
		}

		getToolBarSupport().enableContextCommonToolItems(enabled, inherited, designed);
	}

	/**
	 * Store the pages in the grid
	 * 
	 * @param pages
	 */
	protected void storePages(List<Inherited<PageSelDTO>> pages) {

		List<InheritedPageSelModelData> pagesModel = dtoToModel(pages);
		store.removeAll();
		store.add(pagesModel);
	}

	/**
	 * Returns a List of PageSelModelData objects which wrap the passed PageSelDTO objects.<br/>
	 * 
	 * @param pages
	 * @return a List of PageSelModelData objects which wrap the passed PageSelDTO objects.
	 */
	private List<InheritedPageSelModelData> dtoToModel(List<Inherited<PageSelDTO>> pages) {

		List<InheritedPageSelModelData> models = new LinkedList<InheritedPageSelModelData>();
		for (Inherited<PageSelDTO> p : pages) {
			InheritedPageSelModelData model = new InheritedPageSelModelData(p);
			models.add(model);
		}

		return models;
	}

	/**
	 * Requests the service for a new page template. On success shows the detail panel for the retrieved template.<br/>
	 * 
	 * @param pageClass
	 */
	protected final void newPageTemplate(PageClass pageClass) {

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorCreateTemplate());
			}

			public void onSuccess(PageTemplateDTO template) {
				showDetailPanel(template);
			}
		};

		getPagesService().newTemplate(getPortalPagesLoc(), pageClass, callback);
	}

	/**
	 * Shows the detail panel for the passed Page template DTO.<br/>
	 * @param ptDto
	 */
	private void showDetailPanel(PageTemplateDTO ptDto) {

		PageDetailPanel wDetail = createDetailPanel();
		wDetail.setLayoutButton(getCurrentDevice().getDTO().isLayout());
		wDetail.init(ptDto);
		wDetail.show();
	}

	/**
	 * @return the page detail panel
	 */
	protected PageDetailPanel createDetailPanel() {
		return PmsFactory.getInstance().getPageDetailPanel();
	}

	/**
	 * Retrieves the details of the passed page.<br/>
	 * 
	 * @param pageLoc
	 * @param design if <code>true</code>, the result will be bound to design panel; if <code>false</code>, the result
	 * will be bound to page detail panel
	 */
	protected final void tryGetPage(PageLoc pageLoc, final boolean design) {
		getUtil().mask(getPmsMessages().mskLoadPageLayout());

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorRetrievePage());
			}

			public void onSuccess(PageTemplateDTO page) {
				if (design) { // show design
					tryGetPageLayoutAndDisplayDesign(page);
				} else { // show detail
					showDetailPanel(page);
					getUtil().unmask();
				}
			}
		};

		getPagesService().get(pageLoc, callback);
	}

	/**
	 * Logic of this method is executed when controller fired a ADD event for the passed page.<br/> The grid store is
	 * changed only if the passed page is managed by this widget (does nothing otherwise).
	 * 
	 * @param ptDto page which has been added.
	 */
	@Override
	protected void onPageAdd(PageSelDTO ptDto) {
		// ask for all pages only if the new page is of class managed by this
		// widget:
		if (isPageManagedInThisWidget(ptDto)) {
			tryGetPages();
		}
	}

	/**
	 * Logic of this method is executed when controller fired a DELETE event for the passed page.<br/>
	 * 
	 * @param pageId
	 */
	protected void onPageRemove(String pageId) {
		InheritedPageSelModelData model = store.findModel(InheritedPageSelModelData.PROPERTY_ID, pageId);
		if (model != null) {
			store.remove(model);
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement
	 * #isPageManagedInThisWidget(com.isotrol.impe3.pms.api.page.PageSelDTO)
	 */
	@Override
	protected boolean isPageManagedInThisWidget(PageSelDTO psDto) {
		PageClass managedPageClass = getManagedPageClass();
		PageClass addedPageClass = psDto.getPageClass();

		return managedPageClass.equals(addedPageClass)
			|| (managedPageClass.equals(PageClass.DEFAULT) && addedPageClass.equals(PageClass.MAIN) || (managedPageClass
				.equals(PageClass.DEFAULT) && addedPageClass.equals(PageClass.ERROR)));
	}

	/**
	 * @return the store
	 */
	public ListStore<InheritedPageSelModelData> getStore() {
		return store;
	}

	/**
	 * @return the grid
	 */
	protected final Grid<InheritedPageSelModelData> getGrid() {
		return grid;
	}

	/**
	 * Injects the store sorter.<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * Injects the ID cell renderer.
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
	}

	/**
	 * Cell renderer for <b>Page name</b> property.
	 * 
	 * @author Andrei Cojocaru
	 * 
	 */
	private class NameCellRenderer implements GridCellRenderer<InheritedPageSelModelData> {

		/**
		 * Template for rendered HTML.<br/>
		 */
		private static final String TEMPLATE = "<span class='${CLASS}' title='${TITLE}'>${TEXT}</span>";
		/**
		 * CSS class pattern to replace in template.<br/>
		 */
		private static final String PATTERN_CLASS = "${CLASS}";
		/**
		 * Text pattern to replace in template.<br/>
		 */
		private static final String PATTERN_TEXT = "${TEXT}";
		/**
		 * Title pattern to replace in template.<br/>
		 */
		private static final String PATTERN_TITLE = "${TITLE}";

		/**
		 * (non-Javadoc)
		 * @see com.extjs.gxt.ui.client.widget.grid .GridCellRenderer#render(com.extjs.gxt.ui.client.data.ModelData,
		 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int,
		 * com.extjs.gxt.ui.client.store.ListStore, com.extjs.gxt.ui.client.widget.grid.Grid)
		 */
		public Object render(InheritedPageSelModelData model, String property, ColumnData config, int rowIndex,
			int colIndex, ListStore<InheritedPageSelModelData> store, Grid<InheritedPageSelModelData> grid) {
			String spanClass = "";
			String title = "";
			if (model.getDTO().getValue().getId() == null) {
				spanClass = getPmsStyles().gridCellDisabled();
			} else if (model.getDTO().isInherited()) {
				spanClass = getPmsStyles().inheritedPage();
				title = getPmsMessages().titleInheritedPage();
			}
			String text = model.get(property);
			if (text == null) {
				// is the default error page
				text = getPmsMessages().nodeDefaultErrorPage();
			}

			return TEMPLATE.replace(PATTERN_CLASS, spanClass).replace(PATTERN_TEXT, text).replace(PATTERN_TITLE, title);
		}
	}
}
