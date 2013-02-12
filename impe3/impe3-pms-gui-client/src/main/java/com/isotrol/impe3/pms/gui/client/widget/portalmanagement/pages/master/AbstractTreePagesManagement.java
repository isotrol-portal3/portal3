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


import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.model.AbstractDTOModelDataWithId;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ToolbarSupport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractSpecificPageDetailPanel;


/**
 * Management of Content, category and content-type pages
 * 
 * @author Manuel Ruiz
 * @param <D> model data type
 * @param <E> detailPanel type
 */
public abstract class AbstractTreePagesManagement<D extends DTOModelData<?>, E extends AbstractSpecificPageDetailPanel>
	extends AbstractPagesManagement {

	/** categories pages tree */
	private TreeGrid<D> pagesTree = null;

	/**
	 * The locator of the default page.<br/>
	 */
	private Inherited<PageLoc> defaultPage = null;

	/**
	 * Store for {@link #pagesTree}.<br/>
	 */
	private TreeStore<D> store = null;

	/**
	 * Default constructor.
	 */
	public AbstractTreePagesManagement() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement#lazyInit()
	 */
	@Override
	public void lazyInit() {
		super.lazyInit();

		// specific init params:
		getToolBarSupport().getMiNewPage().disable();
	}

	/**
	 * Create a apge detail panel
	 * 
	 * @param ptDto
	 * @param model
	 * @return the detail panel
	 */
	protected abstract E createDetailPanel(PageTemplateDTO ptDto, D model);

	/**
	 * Returns the page locator for a page
	 * 
	 * @param model the page's model data
	 * @return the page loc
	 */
	protected abstract PageLoc getPageLoc(D model);

	/**
	 * @return the model data property to display in the pages tree
	 */
	protected abstract String getPropertyToDisplay();

	/**
	 * Returns <code>true</code> if the common tool items which need a context (a selected item: edit, preview, design,
	 * delete) should be disabled, <code>false</code> otherwise.
	 * 
	 * @param model selected element ModelData
	 * @return <code>true</code> if the tool items which need a context (edit, preview, design, delete) should be
	 * disabled, <code>false</code> otherwise.
	 */
	protected abstract boolean mustDisableContextCommonButtons(D model);

	/**
	 * <br/>
	 * 
	 * @param model currently selected model in the tree.
	 * @return <code>true</code>, if "New Page" menu item must be disabled for the passed context (the model). Returns
	 * <code>false</code> otherwise.
	 */
	protected abstract boolean mustDisableMiNew(D model);

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractPagesManagement#addListeners()
	 */
	@Override
	protected void addListeners() {
		// add "Refresh" button listener:
		super.addListeners();

		ToolbarSupport tbSupport = getToolBarSupport();

		Button miNewPage = tbSupport.getMiNewPage();
		miNewPage.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				D selected = pagesTree.getSelectionModel().getSelectedItem();
				if (selected != null) {
					newPageTemplate(getPageClass(), selected);
				}
			}
		});

		Button miEditPage = tbSupport.getMiEditPage();
		miEditPage.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails(false);
			}
		});
		miEditPage.disable();

		Button miDesign = tbSupport.getMiDesign();
		miDesign.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails(true);
			}
		});
		miDesign.disable();

		Button miPreviewPage = tbSupport.getMiPreviewPage();
		miPreviewPage.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				D selected = pagesTree.getSelectionModel().getSelectedItem();
				if (selected != null) {
					PageLoc pageLoc = getPageLoc(selected);
					openPreviewWindow(pageLoc);
				}
			}
		});
		miPreviewPage.disable();

		Button miDeletePage = tbSupport.getMiDeletePage();
		miDeletePage.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				D selected = pagesTree.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final PageLoc pageLoc = getPageLoc(selected);

					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent we) {
							Button clicked = we.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								tryDeletePage(pageLoc);
							}
						}
					};
					MessageBox.confirm(getMessages().headerConfirmWindow(), getPmsMessages().msgConfirmDeletePage(),
						lConfirm);
				}
			}
		});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractPagesManagement#addSpecificComponents()
	 */
	@Override
	protected final void addSpecificComponents() {
		// nothing to do
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractPagesManagement#addSpecificToolItems()
	 */
	@Override
	protected void addSpecificToolItems() {
		// nothing to do
	}

	/**
	 * Creates a new pages tree and adds it to the panel.<br/>
	 */
	private void createPagesTree() {

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		// page name
		ColumnConfig config = new ColumnConfig();
		config.setId(getPropertyToDisplay());
		config.setWidth(200);
		// config.setFixed(true);
		config.setHeader(getPmsMessages().columnHeaderName());
		config.setRenderer(new TreePageCellRenderer());
		columns.add(config);

		addSpecificColumnConfigs(columns);
		ColumnModel cm = new ColumnModel(columns);

		pagesTree = new TreeGrid<D>(store, cm);
		pagesTree.setAutoHeight(true);
		pagesTree.setAutoWidth(true);
		// pagesTree.setDisplayProperty(getPropertyToDisplay());
		pagesTree.getStyle().setLeafIcon(IconHelper.create(getPmsStyles().iconTreeLeaf()));
		pagesTree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<D>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<D> se) {
				D selected = pagesTree.getSelectionModel().getSelectedItem();
				if (selected != null) {
					enableDisableToolItems();
				}
			}
		});

		pagesTree.setAutoExpandColumn(columns.get(columns.size() - 1).getId());
		pagesTree.setLoadMask(true);
		pagesTree.getView().setForceFit(true);
		add(pagesTree);

		pagesTree.addListener(Events.ViewReady, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				pagesTree.setExpanded(pagesTree.getStore().getModels().get(0), true);
			}
		});
	}

	protected abstract void addSpecificColumnConfigs(List<ColumnConfig> columns);

	/**
	 * @param model
	 * @return <code>true</code>, if the passed model does not have a bound Page (root items, or non existing Pages for
	 * the concrete model);<code>false</code> otherwise.
	 */
	protected abstract boolean shouldBeDisabled(D model);

	/**
	 * @param model
	 * @return <code>true</code>, if the passed model is a inherited page;<code>false</code> otherwise.
	 */
	protected abstract boolean isInheritedPage(D model);

	/**
	 * Enables or disables toolbar buttons according to the selected tree item state.<br/>
	 */
	protected final void enableDisableToolItems() {
		D tiSelected = pagesTree.getSelectionModel().getSelectedItem();

		ToolbarSupport tbSupport = getToolBarSupport();

		// contextual buttons present in all implementations:
		tbSupport.enableContextCommonToolItems(!mustDisableContextCommonButtons(tiSelected),
			isInheritedPage(tiSelected), getCurrentDevice().getDTO().isLayout());

		// implementation-specific items must be treated in subclasses:
		enableDisableSpecificToolItems(tiSelected);

		// non-contextual items: only "New" button
		tbSupport.getMiNewPage().setEnabled(!mustDisableMiNew(tiSelected));
	}

	/**
	 * Enables or disables the implementation-specific tool items according to passed param value.<br/> Does nothing by
	 * default.
	 * 
	 * @param model the selected item model
	 */
	protected void enableDisableSpecificToolItems(D model) {

	}

	/**
	 * Gets the page details for the selected item in order to edit them in a details panel.<br/>
	 * 
	 * @param design if <code>true</code>, displays the design window instead of the properties panel.
	 */
	protected void getSelectedAndDisplayDetails(boolean design) {
		D selected = pagesTree.getSelectionModel().getSelectedItem();
		if (!mustDisableContextCommonButtons(selected)) {
			PageLoc pageLoc = getPageLoc(selected);
			tryGetPage(pageLoc, selected, design);
		}
	}

	/**
	 * Removes the current pages tree and creates a new one from scratch.<br/> Binds the filter to the new store.
	 */
	@SuppressWarnings("unchecked")
	protected final void repopulatePagesTree() {

		if (pagesTree != null) {
			pagesTree.removeFromParent();
		}

		createPagesTree();

		StoreFilterField<ModelData> miFilter = getToolBarSupport().getMiFilter();
		if (miFilter != null) {
			miFilter.bind((Store) store);
		}
	}

	/**
	 * Requests the service for a new page template, and shows a detail panel with the resultant data.<br/>
	 * 
	 * @param pageClass
	 * @param model
	 */
	private void newPageTemplate(PageClass pageClass, final D model) {

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorCreateTemplate());
			}

			public void onSuccess(PageTemplateDTO template) {
				showDetailPanel(template, model);
			}
		};

		getPagesService().newTemplate(getPortalPagesLoc(), pageClass, callback);
	}

	/**
	 * Creates and shows the detail panel for the passed template DTO and<br/>
	 * 
	 * @param ptDto
	 * @param model
	 */
	private void showDetailPanel(PageTemplateDTO ptDto, D model) {
		AbstractPageDetailPanel wDetail = createDetailPanel(ptDto, model);
		wDetail.show();
	}

	/**
	 * Retrieves the details of the passed page<br/>
	 * 
	 * @param pageLoc
	 * @param model
	 * @param design if <code>true</code>, the result will be bound to design panel; if <code>false</code>, the result
	 * will be bound to page detail panel
	 */
	protected void tryGetPage(PageLoc pageLoc, final D model, final boolean design) {
		getUtil().mask(getPmsMessages().mskPage());

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorRetrievePage());
			}

			public void onSuccess(PageTemplateDTO page) {
				if (design) { // show design
					tryGetPageLayoutAndDisplayDesign(page);
				} else { // show detail
					showDetailPanel(page, model);
					getUtil().unmask();
				}
			}
		};

		getPagesService().get(pageLoc, callback);
	}

	/**
	 * Checks if this widget manages pages of the passed page class.<br/>
	 * 
	 * @param page
	 * @return <code>true</code>, if this widget manages the passed page; <code>false</code> otherwise.
	 */
	@Override
	protected final boolean isPageManagedInThisWidget(PageSelDTO page) {
		return page.getPageClass().equals(getPageClass());
	}

	/**
	 * Checks if this widget manages the passed page class. If so, retrieves the pages info through RPC.<br/>
	 * 
	 * @param page
	 */
	@Override
	protected final void onPageAdd(PageSelDTO page) {
		if (isPageManagedInThisWidget(page)) {
			tryGetPages();
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractPagesManagement
	 * #onPageRemove(java.lang.String)
	 */
	@Override
	protected final void onPageRemove(String pageId) {
		D model = store.findModel(AbstractDTOModelDataWithId.PROPERTY_ID, pageId);
		if (model != null) {
			tryGetPages();
		}
		enableDisableToolItems();
		pagesTree.getStore().update(model);
	}

	/**
	 * Sets the tree item with no associated page.<br/> Implementations should call super.clearItem(TreeItem) in order
	 * to change the TreeItem visually
	 * 
	 * @param item
	 */
	protected void clearItem(D item) {
		// item.addStyleName(getPmsStyles().itemDisabled());
	}

	/**
	 * @return the defaultPage
	 */
	protected final Inherited<PageLoc> getDefaultPage() {
		return defaultPage;
	}

	/**
	 * @param defaultPage the defaultPage to set
	 */
	protected final void setDefaultPage(Inherited<PageLoc> defaultPage) {
		this.defaultPage = defaultPage;
	}

	/**
	 * @return the store
	 */
	protected final TreeStore<D> getStore() {
		return store;
	}

	/**
	 * @return the pagesTree
	 */
	protected final TreeGrid<D> getPagesTree() {
		return pagesTree;
	}

	/**
	 * @param store the store to set
	 */
	protected final void setStore(TreeStore<D> store) {
		this.store = store;
	}

	class TreePageCellRenderer extends TreeGridCellRenderer<D> {
		@Override
		protected String getText(TreeGrid<D> grid, D model, String property, int rowIndex, int colIndex) {
			String value = String.valueOf(model.get(property));
			String name = "";
			String res = "";
			if (value != null) {
				name = Format.htmlEncode(value);
			}

			if (shouldBeDisabled(model)) {
				res = "<span class='item-disabled'>" + name + "</span>";
			} else if (isInheritedPage(model)) {
				String title = getPmsMessages().titleInheritedPage();
				res = "<span class='inherited-page' title='" + title + "'>" + name + "</span>";
			} else {
				res = name;
			}

			return res;
		}
	}

}
