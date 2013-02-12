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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PagesController;
import com.isotrol.impe3.pms.gui.client.data.impl.CategoryPageModelData;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.CategoryPageTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.design.Design;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.CategoryContentPageDetailPanel;


/**
 * Base class for the Master views of the pages associated to Categories: Content Type and Content Type listing pages
 * management widgets.
 * 
 * @author Andrei Cojocaru
 * 
 */
public abstract class AbstractWithCategoryContentPageManagement extends ContentPanel {

	/** the current portal pages loc */
	private PortalPagesLoc portalPagesLoc = null;
	/** contains the page detail view */
	private Window wDetail = null;

	/** categories pages tree */
	private TreeGrid<CategoryPageModelData> catPageTree = null;

	/**
	 * Pages tree store.<br/>
	 */
	private CategoryPageTreeStore store = null;
	/**
	 * Filter for Pages store.<br/>
	 */
	private CustomizableStoreFilter<CategoryPageModelData> filter = null;

	/*
	 * toolbar items:
	 */
	/**
	 * "New Page" menu item.<br/>
	 */
	private Button miNewPage = null;
	/**
	 * "Edit Page" menu item.<br/>
	 */
	private Button miEditPage = null;
	/**
	 * "Design Page" menu item.<br/>
	 */
	private Button miDesign = null;
	/**
	 * "Delete Page" menu item.<br/>
	 */
	private Button miDeletePage = null;
	/**
	 * "Preview Page" menu item.<br/>
	 */
	private Button tiPreviewPage = null;

	/**
	 * The pages DTO.<br/>
	 */
	private CategoryPagesDTO pages = null;
	/**
	 * Current Page model.<br/>
	 */
	private ContentPageDTO content = null;

	/*
	 * Injected deps.
	 */
	/**
	 * The service errors processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles.<br/>
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	/**
	 * PMS specific styles service.
	 */
	private PmsStyles pmsStyles = null;

	/** pages service key */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Error message resolver for the Pages service<br/>
	 */
	private PageErrorMessageResolver emrPages = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Async callback for Page deletion.<br/>
	 */
	private DeletePageCallback deletePageCallback = null;
	
	/**
	 * PMS Utilities object.<br/>
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Constructor provided with the portal location for the managed pages.<br/>
	 * @param pages
	 * @param content2
	 * @param portalPagesLoc2
	 */
	public AbstractWithCategoryContentPageManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.<br/>
	 * @param contentDto
	 * @param plPortal
	 * @param pagesDto
	 */
	public void init(ContentPageDTO contentDto, PortalPagesLoc plPortal, CategoryPagesDTO pagesDto) {
		this.portalPagesLoc = plPortal;
		this.pages = pagesDto;
		this.content = contentDto;

		configThis();
		initComponent();
		initController();
	}

	/**
	 * Configures this container properties.<br/>
	 */
	private void configThis() {

		setHeaderVisible(false);
		setBodyBorder(false);
		setLayout(new FitLayout());
		setWidth(Constants.HUNDRED_PERCENT);
		setScrollMode(Scroll.AUTO);
		setLayoutOnChange(true);
	}

	/**
	 * Creates and add the required widgets
	 */
	private void initComponent() {

		wDetail = new Window();
		wDetail.setModal(true);
		wDetail.setScrollMode(Scroll.AUTO);
		wDetail.setHeading(messages.headerDetailPanel());
		// wDetail.setClosable(false);
		wDetail.setMaximizable(true);

		addPagesToolbar();
		repopulateCategoryPagesTree(pages);
		filter.bind(store);
	}

	/**
	 * Creates a new tree for the passed CategoryPagesDTO. If tree already exists, deletes it prior to creation.<br/>
	 * 
	 * @param p
	 */
	private void repopulateCategoryPagesTree(CategoryPagesDTO p) {

		if (catPageTree != null) {
			catPageTree.removeFromParent();
		}
		createCatPagesTree(p);
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the categories controller. All change events fired by the
	 * controller will refresh the {@link #tree}.<br/>
	 */
	private void initController() {

		final ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.ADD: // ask for all pages:
							PageTemplateDTO ptDto = event.getEventInfo();
							onPageAdd(ptDto);
							break;
						case PmsChangeEvent.DELETE:
							PageLoc loc = event.getEventInfo();
							onPageRemove(loc);
							break;
						case PmsChangeEvent.UPDATE:
							break;
						default: // shouldn't happen..
							// GWT.log("Unexpected event descriptor for a ChangeEventSource instance :" + event.type,
							// null);
					}
				}
			}
		};
		final PagesController pagesController = (PagesController) pagesService;
		pagesController.addChangeListener(controllerListener);
		
		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				pagesController.removeChangeListener(controllerListener);
			}
		});
	}

	/**
	 * Called when a page has been saved by user.<br/>
	 * 
	 * @param ptDto added pge
	 */
	private void onPageAdd(PageTemplateDTO ptDto) {
		if (ptDto.getPageClass().equals(getManagedPageClass())) {
			tryGetPages();
		}
	}
	
	/**
	 * Recursively computes the passed model<br/>
	 * @param t
	 * @param m
	 * @param lExpanded
	 */
	private void computeExpanded(TreeGrid<CategoryPageModelData> t, CategoryPageModelData m, List<String> lExpanded) {
		if (t.isExpanded(m)) {
			lExpanded.add((String) m.getDTO().getCategoryId());
			List<CategoryPageModelData> lChildren = t.getTreeStore().getChildren(m);
			for (CategoryPageModelData child : lChildren) {
				computeExpanded(t, child, lExpanded);
			}
		}
	}

	/**
	 * Expands the passed model branches whose IDs are contained in the expanded list.
	 * 
	 * @param model
	 * @param lExpanded
	 */
	private void maybeExpand(CategoryPageModelData model, List<String> lExpanded) {
		if (lExpanded.contains(model.getDTO().getCategoryId())) {
			catPageTree.setExpanded(model, true);
			for (CategoryPageModelData child : catPageTree.getTreeStore().getChildren(model)) {
				maybeExpand(child, lExpanded);
			}
		}
	}
	
	/**
	 * Called when a page has been removed by user.<br/>
	 * @param ptDto
	 */
	private void onPageRemove(PageLoc pageLoc) {
		CategoryPageModelData model = store.findModel(CategoryPageModelData.PROPERTY_ID, pageLoc.getId());
		if (model != null) {
			tryGetPages();
		}
	}

	/**
	 * Adds a toolbar on top of this component.<br/>
	 */
	private void addPagesToolbar() {

		ToolBar toolBar = new ToolBar();
		setTopComponent(toolBar);

		// new page item
		miNewPage = buttonsSupport.addAddButton(toolBar, new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				CategoryPageModelData selected = catPageTree.getSelectionModel().getSelectedItem();
				if (selected != null) {
					newPageTemplate(getManagedPageClass(), selected);
				}
			}
		});
		miNewPage.disable();
		buttonsSupport.addSeparator(toolBar);

		// edit page item de
		miEditPage = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails(false);
			}
		});
		miEditPage.disable();
		buttonsSupport.addSeparator(toolBar);

		// page design item
		miDesign = buttonsSupport.addGenericButton(pmsMessages.labelDesign(), pmsStyles.iconDesign(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					getSelectedAndDisplayDetails(true);
				}
			});
		miDesign.disable();
		buttonsSupport.addSeparator(toolBar);

		// page preview item
		tiPreviewPage = buttonsSupport.addGenericButton(pmsMessages.labelPreview(), guiCommonStyles.iPreview(),
			toolBar, new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					CategoryPageModelData selected = catPageTree.getSelectionModel().getSelectedItem();
					if (selected != null) {
						PageLoc pageLoc = getPageLoc(selected);
						openPreviewWindow(pageLoc);
					}
				}
			});
		tiPreviewPage.disable();
		buttonsSupport.addSeparator(toolBar);

		// delete page item
		miDeletePage = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				CategoryPageModelData selected = catPageTree.getSelectionModel().getSelectedItem();
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
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeletePage(), lConfirm);
				}
			}
		});
		miDeletePage.disable();

		toolBar.add(new FillToolItem());

		// pages filter:
		filter = new CustomizableStoreFilter<CategoryPageModelData>(Arrays
			.asList(new String[] {CategoryPageModelData.PROPERTY_NAME}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPages();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

	}

	/**
	 * Delete a page
	 * 
	 * @param item the modeldata to delete
	 */
	protected void tryDeletePage(PageLoc pageLoc) {
		util.mask(pmsMessages.mskDeletePage());
		pagesService.delete(pageLoc, deletePageCallback);
	}

	/**
	 * Requests the pages service for the preview URL of the passed Page.<br/> On success, opens a popup window with the
	 * retrieved URL.
	 * 
	 * @param pageLoc
	 */
	protected void openPreviewWindow(PageLoc pageLoc) {
		pmsUtil.openPagePreview(pageLoc);
	}

	/**
	 * Creates the tree, its store and binder and attaches the tree to this component.<br/>
	 * @param p
	 */
	private void createCatPagesTree(CategoryPagesDTO p) {

		store = PmsFactory.getInstance().getCategoryPageTreeStore();
		store.initPageStore(p);
		store.setRootName(content.getContentType().getName());
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		// page defined?
		ColumnConfig config = new ColumnConfig();
		config.setId(CategoryPageModelData.PROPERTY_OWNPAGES);
		config.setFixed(true);
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setRenderer(new GridCellRenderer<CategoryPageModelData>() {
			public Object render(CategoryPageModelData model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<CategoryPageModelData> store, Grid<CategoryPageModelData> grid) {
				if ((Boolean) model.get(property) && !model.isPage()) {
					String title = "Esta categoría o sus hijos tienen una página definida en el propio portal";
					return "<img src='img/folder_page.gif' title='" + title + "'>";
				} else {
					return "";
				}
			}
		});
		columns.add(config);

		// inherited page defined?
		config = new ColumnConfig();
		config.setId(CategoryPageModelData.PROPERTY_INHPAGES);
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setFixed(true);
		config.setRenderer(new GridCellRenderer<CategoryPageModelData>() {
			public Object render(CategoryPageModelData model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<CategoryPageModelData> store, Grid<CategoryPageModelData> grid) {
				if ((Boolean) model.get(property) && !model.isPage()) {
					String title = "Esta categoría o sus hijos tienen una página heredada definida";
					return "<img src='img/folder_page_h.gif' title='" + title + "'>";
				} else {
					return "";
				}
			}
		});
		columns.add(config);

		// page name
		config = new ColumnConfig();
		config.setId(CategoryPageModelData.PROPERTY_TO_DISPLAY);
		config.setWidth(200);
		config.setHeader(pmsMessages.columnHeaderName());
		config.setRenderer(new TreePageCellRenderer());
		columns.add(config);
		
		ColumnModel cm = new ColumnModel(columns);
		catPageTree = new TreeGrid<CategoryPageModelData>(store, cm);
		catPageTree.setAutoHeight(true);
		catPageTree.setAutoWidth(true);
		catPageTree.getStyle().setLeafIcon(IconHelper.create(pmsStyles.iconTreeLeaf()));

		catPageTree.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<CategoryPageModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<CategoryPageModelData> se) {
					CategoryPageModelData selected = catPageTree.getSelectionModel().getSelectedItem();
					if (selected != null) {
						if (mustDisableEditButtons(selected)) {
							enableEditButtons(false);
						} else if (mustEnableEditButtons(selected)) {
							enableEditButtons(true);
						} else {
							disableAllButtons();
						}
					}
				}
			});

		catPageTree.setAutoExpandColumn(columns.get(columns.size() - 1).getId());
		catPageTree.setLoadMask(true);
		catPageTree.getView().setForceFit(true);
		add(catPageTree);

		// expand the tree root node
		catPageTree.addListener(Events.ViewReady, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				catPageTree.setExpanded(store.getRootItems().get(0), true, false);
			}
		});

	}

	/**
	 * @param pageModel
	 * @return <code>true</code>, if the UI element corresponding to the passed model shold be displayed as disabled (in
	 * gray);<code>false</code> otherwise.
	 */
	private boolean shouldBeDisabled(CategoryPageModelData pageModel) {
		return pageModel != null
			&& pageModel.isPage()
			&& ((pageModel.isOnlyThis() && pageModel.getDTO().getOnlyThis() == null) || (pageModel.isThisAndChildren() && pageModel
				.getDTO().getThisAndChildren() == null));
	}

	private boolean isInheritedPage(CategoryPageModelData model) {
		if (model != null && model.getDTO().getOnlyThis() != null) {
			return model.getDTO().getOnlyThis().isInherited();
		} else if (model != null && model.getDTO().getThisAndChildren() != null) {
			return model.getDTO().getThisAndChildren().isInherited();
		}
		return false;
	}

	/**
	 * Returns the PageLoc structure for the passed model.<br/>
	 * @param model
	 * @return
	 */
	protected PageLoc getPageLoc(CategoryPageModelData model) {

		PageLoc pageLoc = null;
		if (model.isOnlyThis()) {
			pageLoc = model.getDTO().getOnlyThis().getValue();
		} else if (model.isThisAndChildren()) {
			pageLoc = model.getDTO().getThisAndChildren().getValue();
		}

		return pageLoc;
	}

	/**
	 * <br/>
	 * @param model
	 * @return <code>true</code>, if the edition buttons should be disabled; <code>false</code>, otherwise.
	 */
	private boolean mustDisableEditButtons(CategoryPageModelData model) {
		return (model.isOnlyThis() && model.getDTO().getOnlyThis() == null)
			|| (model.isThisAndChildren() && model.getDTO().getThisAndChildren() == null);
	}

	/**
	 * <br/>
	 * @param model
	 * @return <code>true</code>, if the edition buttons should be enabled; <code>false</code>, otherwise.
	 */
	private boolean mustEnableEditButtons(CategoryPageModelData model) {
		return (model.isOnlyThis() && model.getDTO().getOnlyThis() != null)
			|| (model.isThisAndChildren() && model.getDTO().getThisAndChildren() != null);
	}

	/**
	 * <br/>
	 * @param enabled if <code>true</code>, enables the toolbar buttons which need a Category context and disables the
	 * buttons which need no Category selected. If <code>false</code>, disables the buttons that need a Category context
	 * and enables the buttons which need no Category selected.
	 */
	private void enableEditButtons(boolean enabled) {
		miNewPage.setEnabled(!enabled);
		miDeletePage.setEnabled(enabled);
		miDesign.setEnabled(enabled);
		miEditPage.setEnabled(enabled);
		tiPreviewPage.setEnabled(enabled);
	}

	/**
	 * Disables all toolbar buttons.<br/>
	 */
	private void disableAllButtons() {
		miNewPage.disable();
		miDeletePage.disable();
		miDesign.disable();
		miEditPage.disable();
		tiPreviewPage.disable();
	}

	/**
	 * <br/>
	 * @param design if <code>true</code>, will display the page design window. If <code>false</code>, will display the
	 * properties window.
	 */
	protected void getSelectedAndDisplayDetails(boolean design) {
		CategoryPageModelData selected = catPageTree.getSelectionModel().getSelectedItem();
		if (selected != null && !mustDisableEditButtons((CategoryPageModelData) selected)) {
			PageLoc pageLoc = getPageLoc(selected);
			tryGetPage(pageLoc, selected, design);
		}
	}

	/**
	 * Retrieves the details of the passed page; on success displays them on a details window.<br/>
	 * 
	 * @param pageLoc
	 * @param model
	 * @param design if <code>true</code>, the result will be bound to design panel; if <code>false</code>, the result
	 * will be bound to page detail panel
	 */
	protected void tryGetPage(PageLoc pageLoc, final CategoryPageModelData model, final boolean design) {
		util.mask(pmsMessages.mskPage());

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				getErrorProcessor().processError(arg0, emrPages, pmsMessages.msgErrorRetrievePage());
			}

			public void onSuccess(PageTemplateDTO page) {
				if (design) { // show design
					tryGetPageLayoutAndDisplayDesign(page);
				} else { // show detail
					showDetailPanel(page, model);
					util.unmask();
				}
			}
		};

		pagesService.get(pageLoc, callback);
	}

	/**
	 * Requests service for the Pages of the bound Portal.<br/> On success, populates the tree with the obtained Pages.
	 */
	private void tryGetPages() {

		String contentId = content.getContentType().getId();

		AsyncCallback<CategoryPagesDTO> callback = new AsyncCallback<CategoryPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getErrorProcessor().processError(arg0, emrPages, pmsMessages.msgErrorRetrieveCategoryPages());
			}

			public void onSuccess(CategoryPagesDTO p) {
				// store the current expanded nodes 
				List<String> lExpanded = new LinkedList<String>();
				if (store != null) {
					for (CategoryPageModelData model : store.getRootItems()) {
						computeExpanded(catPageTree, model, lExpanded);
					}
				}
				
				final List<String> expendedIds = lExpanded;
				
				repopulateCategoryPagesTree(p);
				filter.bind(store);
				
				// restore expanded. Expand at least depth 1 elements:
				catPageTree.addListener(Events.ViewReady, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						List<CategoryPageModelData> rootModels = store.getRootItems();
						for (CategoryPageModelData model : rootModels) {
							catPageTree.setExpanded(model, true);
							// children nodes:
							for (CategoryPageModelData child : store.getChildren(model)) {
								maybeExpand(child, expendedIds);
							}
						}
					}
				});
			}

		};

		doCallGetPages(contentId, callback);
	}

	protected abstract void doCallGetPages(String contentId, AsyncCallback<CategoryPagesDTO> callback);

	/**
	 * Requests the service for a new page template of the managed kind.<br/> On success, shows the detail panel with
	 * the retrieved data.
	 * @param pageClass
	 * @param model
	 */
	private void newPageTemplate(PageClass pageClass, final CategoryPageModelData model) {

		AsyncCallback<PageTemplateDTO> callback = new AsyncCallback<PageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrPages, pmsMessages.msgErrorCreateTemplate());
			}

			public void onSuccess(PageTemplateDTO template) {
				showDetailPanel(template, model);
			}
		};

		pagesService.newTemplate(portalPagesLoc, pageClass, callback);
	}

	/**
	 * Retrieves the Layout of the passed Page from service.<br/> On success, displays the retrieved Layout in the
	 * design window.
	 * 
	 * @param page
	 */
	private void tryGetPageLayoutAndDisplayDesign(final PageTemplateDTO page) {

		AsyncCallback<LayoutDTO> callback = new GetLayoutCallback(page);

		PageLoc pageLoc = new PageLoc();
		pageLoc.setDeviceId(page.getDeviceId());
		pageLoc.setId(page.getId());
		pageLoc.setPortalId(page.getPortalId());

		pagesService.getLayout(pageLoc, callback);
	}

	/**
	 * Creates<br/>
	 * @param ptDto
	 * @param model
	 */
	private void showDetailPanel(PageTemplateDTO ptDto, CategoryPageModelData model) {

		CategoryContentPageDetailPanel ccpDetailPanel = PmsFactory.getInstance().getCategoryContentPageDetailPanel();
		ccpDetailPanel.setLayoutButton(true);
		ccpDetailPanel.init(ptDto, content, model);
		wDetail = ccpDetailPanel;
		wDetail.show();
		// wDetail.maximize();
	}

	/**
	 * Returns the page class managed by the current implementation.<br/>
	 * 
	 * @return
	 */
	protected abstract PageClass getManagedPageClass();

	/**
	 * Async Callback for <code>getLayout()</code> RPC. Shows the Design window for the bound page.
	 * 
	 * @author Andrei Cojocaru
	 */
	private class GetLayoutCallback implements AsyncCallback<LayoutDTO> {

		/**
		 * The page.
		 */
		private PageTemplateDTO page = null;

		/**
		 * Unique constructor provided with the page to display.<br/>
		 * 
		 * @param page
		 */
		public GetLayoutCallback(PageTemplateDTO page) {
			this.page = page;
		}

		public void onFailure(Throwable arg0) {
			util.unmask();
			errorProcessor.processError(arg0, emrPages, pmsMessages.msgErrorRetrieveLayout());
		}

		public void onSuccess(LayoutDTO layout) {
			Design design = Design.getInstance();
			design.config(page, layout);
			design.show();
			design.maximize();

			util.unmask();
		}
	}
	
	class TreePageCellRenderer extends TreeGridCellRenderer<CategoryPageModelData> {
		@Override
		protected String getText(TreeGrid<CategoryPageModelData> grid, CategoryPageModelData model, String property, int rowIndex, int colIndex) {
			String value = String.valueOf(model.get(property));
			String name = "";
			String res = "";
			if (value != null) {
				name = Format.htmlEncode(value);
			}

			if (shouldBeDisabled(model)) {
				res = "<span class='item-disabled'>" + name + "</span>";
			} else if (isInheritedPage(model) && !store.getRootItems().contains(model)) {
				String title = pmsMessages.titleInheritedPage();
				res = "<span class='inherited-page' title='" + title + "'>" + name + "</span>";
			} else {
				res = name;
			}

			return res;
		}
	}

	/**
	 * @return the pagesService
	 */
	protected final IPagesServiceAsync getPagesService() {
		return pagesService;
	}

	/**
	 * @return the portalPagesLoc
	 */
	protected final PortalPagesLoc getPortalPagesLoc() {
		return portalPagesLoc;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the PMS specific styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the async Pages service proxy.
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
	}

	/**
	 * Injects the buttons helper object
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the async callback for Page deletion.
	 * @param deletePageCallback
	 */
	@Inject
	public void setDeletePageCallback(DeletePageCallback deletePageCallback) {
		this.deletePageCallback = deletePageCallback;
	}

	/**
	 * Injects the utilities.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * Injects the error message resolver for the pages service.
	 * @param emrPages the emrPages to set
	 */
	@Inject
	public void setEmrPages(PageErrorMessageResolver emrPages) {
		this.emrPages = emrPages;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor.
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * Injects GuiCommon styles bundle.
	 * @param guiCommonStyles
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @return GuiCommon styles bundle.
	 */
	protected GuiCommonStyles getGuiCommonStyles() {
		return guiCommonStyles;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
