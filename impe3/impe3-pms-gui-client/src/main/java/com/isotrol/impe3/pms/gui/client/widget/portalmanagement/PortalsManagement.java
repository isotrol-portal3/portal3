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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Popup;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeGridCellRenderer;
import com.isotrol.impe3.gui.common.renderer.HtmlEncodeTreeGridCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.edition.ModifiedParentPortalsException;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.gui.api.service.IEditionsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.renderer.StateCellRenderer;
import com.isotrol.impe3.pms.gui.client.store.PortalTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.NavigationPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsMainPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.PortalNameCreationPanel;


/**
 * Gui for portals management
 * @author Manuel Ruiz
 * 
 */
public class PortalsManagement extends PmsContentPanel {

	/**
	 * Width for <b>name</b> column<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 200;

	/**
	 * Width for <b>description</b> column<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 400;

	private static final String PORTAL_HISTORY_ITEM = "portal";

	/**
	 * Store for {@link #grid} and {@link #filter}.<br/>
	 */
	private PortalTreeStore store = null;

	/**
	 * Master view Content Types grid.<br/>
	 */
	private TreeGrid<PortalSelModelData> treeGrid = null;

	/** edit portal button */
	private Button ttiEdit = null;

	/** delete portal button */
	private Button ttiDelete = null;

	/** publish portal button */
	private Button ttiPublish = null;

	/** PortalNameDTO for the portal selected in the grid */
	private PortalNameDTO selectedPortal = null;

	private PortalViewport viewport = null;

	/* ********
	 * Injected deps:*******
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Portals async service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Editions async service.<br/>
	 */
	private IEditionsServiceAsync editionsService = null;

	/**
	 * Error Message Resolver for Portals.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	/**
	 * PMS specific message bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * PMS specific styles service.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	/**
	 * Gui common messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS main panel
	 */
	private PmsMainPanel pmsMainPanel = null;

	private NavigationPanel navigationPanel = null;

	/**
	 * Renderer for "state" grid cell.<br/>
	 */
	private StateCellRenderer stateCellRenderer = null;

	@Override
	protected void beforeRender() {

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if (PORTAL_HISTORY_ITEM.equals(event.getValue()) && viewport != null) {
					navigationPanel.initNavigation();
					addNavigationItems();
					viewport.init();
					pmsMainPanel.activateViewport(viewport);
				}
			}
		});

		initComponent();
		initController();

		// asynchronous access:
		tryGetPortals();
	}

	private void initComponent() {
		setLayoutOnChange(true);
		setLayout(new FitLayout());

		addTreeGrid();
		addTopToolBar();
	}

	private void initController() {
		PortalsController portalsController = (PortalsController) portalsService;

		portalsController.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
				switch (event.getType()) {
					case PmsChangeEvent.DELETE: // portal deleted
						// repopulate tree:
						PortalTreeDTO ptDto = pmsEvent.getEventInfo();
						populateTreeGrid(ptDto);
						break;
					case PmsChangeEvent.ADD: // new portal
					case PmsChangeEvent.UPDATE: // portal properties updated
						tryGetPortals();
						break;
					case PmsChangeEvent.UPDATE_PORTAL_NAME: // portal name updated
						PortalNameDTO info = (PortalNameDTO) pmsEvent.getEventInfo();
						selectedPortal = info;
						tryGetPortals();
						break;
					default:
						// nothing to do here.
				}
			}
		});
	}

	/**
	 * Retrieves portals data and populates the tree grid.<br/>
	 */
	private void tryGetPortals() {
		util.mask(pmsMessages.mskPortals());
		AsyncCallback<PortalTreeDTO> callback = new AsyncCallback<PortalTreeDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrPortals, pmsMessages.msgErrorGetPortals());
			}

			public void onSuccess(PortalTreeDTO arg0) {
				populateTreeGrid(arg0);
				util.unmask();
			}
		};

		portalsService.getPortals(callback);
	}

	/**
	 * @param rootDto
	 */
	private void populateTreeGrid(PortalTreeDTO rootDto) {
		store.removeAll();

		for (PortalTreeDTO child : rootDto.getChildren()) {
			store.add(child, true);
		}
	}

	/**
	 * Creates, configures and adds the grid.<br/>
	 */
	private void addTreeGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		GridCellRenderer<PortalSelModelData> urlsRenderer = new GridCellRenderer<PortalSelModelData>() {
			public Object render(final PortalSelModelData model, String property, ColumnData config,
				final int rowIndex, final int colIndex, ListStore<PortalSelModelData> store,
				Grid<PortalSelModelData> grid) {

				IconButton icon = new IconButton(pmsStyles.iconPortalManagement(),
					new SelectionListener<IconButtonEvent>() {
						@Override
						public void componentSelected(IconButtonEvent ce) {
							tryShowUrls(model, ce.getComponent());
						}
					});
				icon.setToolTip(pmsMessages.titlePortalUrls());
				return icon;
			}
		};

		ColumnConfig config = new ColumnConfig();
		config.setId(PortalSelModelData.PROPERTY_ID);
		config.setWidth(30);
		config.setRenderer(urlsRenderer);
		config.setHeaderText("Urls");
		configs.add(config);

		// state icon
		config = new ColumnConfig();
		config.setRenderer(stateCellRenderer);
		config.setId(ModuleInstanceSelModelData.PROPERTY_STATE);
		config.setHeaderText(pmsMessages.columnHeaderState());
		config.setWidth(50);
		configs.add(config);

		config = new ColumnConfig();
		config.setId(PortalSelModelData.PROPERTY_NAME);
		config.setWidth(COLUMN_NAME_WIDTH);
		config.setRenderer(new HtmlEncodeTreeGridCellRenderer());
		config.setHeaderText(pmsMessages.columnHeaderName());
		configs.add(config);

		config = new ColumnConfig();
		config.setId(PortalSelModelData.PROPERTY_DESCRIPTION);
		config.setWidth(COLUMN_DESCRIPTION_WIDTH);
		config.setHeaderText(pmsMessages.columnHeaderDescription());
		config.setRenderer(new HtmlEncodeGridCellRenderer());
		configs.add(config);

		ColumnModel cm = new ColumnModel(configs);

		store = new PortalTreeStore();

		treeGrid = new TreeGrid<PortalSelModelData>((PortalTreeStore) store, cm);
		treeGrid.setSelectionModel(new GridSelectionModel<PortalSelModelData>());
		treeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		treeGrid.addListener(Events.RowClick, new Listener<GridEvent<PortalSelModelData>>() {
			public void handleEvent(GridEvent<PortalSelModelData> ge) {
				if (treeGrid.getSelectionModel().getSelectedItem() != null) {
					enableDisableButtons(true);
				}
			}
		});

		treeGrid.addListener(Events.RowDoubleClick, new Listener<GridEvent<PortalSelModelData>>() {
			/**
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(GridEvent<PortalSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		treeGrid.setAutoExpandColumn(PortalSelModelData.PROPERTY_NAME);
		treeGrid.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));
		treeGrid.setLoadMask(true);
		treeGrid.getView().setForceFit(true);

		add(treeGrid);
	}

	private void tryShowUrls(final PortalSelModelData model, final Component component) {
		AsyncCallback<PortalURLsDTO> callback = new AsyncCallback<PortalURLsDTO>() {

			public void onSuccess(PortalURLsDTO result) {
				UrlsPopUp popup = new UrlsPopUp(model, result);
				popup.show(component);
			}

			public void onFailure(Throwable caught) {

			}
		};
		portalsService.getURLs(model.getDTO().getId(), callback);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addTopToolBar() {
		ToolBar toolBar = new ToolBar();

		SelectionListener<ButtonEvent> lNew = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				PortalSelModelData selected = treeGrid.getSelectionModel().getSelectedItem();
				PortalSelDTO parentDto = null;
				String parentId = null;
				if (selected != null) {
					parentDto = selected.getDTO();
					parentId = parentDto.getId();
				}

				PortalNameCreationPanel portalNameCreationPanel = PmsFactory.getInstance().getPortalNameCreationPanel();
				portalNameCreationPanel.init(null, parentId);
				portalNameCreationPanel.show();

				treeGrid.getSelectionModel().deselectAll();
			}
		};
		buttonsSupport.addAddButton(toolBar, lNew, null);
		buttonsSupport.addSeparator(toolBar);

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				final PortalSelModelData selected = treeGrid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final String portalDisplayName = selected.getDTO().getName();

					Listener<MessageBoxEvent> lConfirm1 = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
								Listener<MessageBoxEvent> lConfirm2 = new Listener<MessageBoxEvent>() {
									public void handleEvent(MessageBoxEvent be) {
										if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
											tryDeletePortal(selected);
										}
									}
								};
								MessageBox.confirm(pmsMessages.titleConfirmDeletePortal(portalDisplayName), pmsMessages
									.msgConfirm2DeletePortal(), lConfirm2);
							}
						}
					};
					MessageBox.confirm(pmsMessages.titleConfirmDeletePortal(portalDisplayName),
						pmsMessages.msgConfirm1DeletePortal(portalDisplayName), lConfirm1).setModal(true);
				}
			}
		};
		ttiDelete = buttonsSupport.addDeleteButton(toolBar, lDelete, null);
		buttonsSupport.addSeparator(toolBar);

		SelectionListener<ButtonEvent> lEdit = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}
		};
		ttiEdit = buttonsSupport.addEditButton(toolBar, lEdit);
		buttonsSupport.addSeparator(toolBar);

		SelectionListener<ButtonEvent> lPublish = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final PortalSelModelData selected = treeGrid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					final String portalDisplayName = selected.getDTO().getName();
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
								tryPublishPortal(selected, false);
							}
						}
					};
					MessageBox.confirm(pmsMessages.titleConfirmPublishPortal(portalDisplayName),
						pmsMessages.msgConfirmPublishPortal(portalDisplayName), lConfirm).setModal(true);
				}
			}
		};
		ttiPublish = buttonsSupport.addGenericButton(pmsMessages.labelPublish(), pmsStyles.iconPublished(), toolBar,
			lPublish);

		toolBar.add(new FillToolItem());

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPortals();
				enableDisableButtons(false);
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		// disable remove and edit buttons
		enableDisableButtons(false);

		setTopComponent(toolBar);
	}

	private void enableDisableButtons(boolean enabled) {
		ttiEdit.setEnabled(enabled);
		ttiDelete.setEnabled(enabled);
		ttiPublish.setEnabled(enabled);
	}

	private void getSelectedAndDisplayDetails() {

		PortalSelModelData itemSelected = treeGrid.getSelectionModel().getSelectedItem();
		if (itemSelected != null) {
			util.mask(pmsMessages.mskPortal());

			// retrieve the portal template:
			AsyncCallback<PortalNameDTO> portalCallback = new AsyncCallback<PortalNameDTO>() {
				public void onFailure(Throwable arg0) {
					util.unmask();
					MessageBox.alert(messages.headerErrorWindow(),
						emrPortals.getMessage(arg0, pmsMessages.msgErrorGetPortal()), null).setModal(true);
				}

				public void onSuccess(PortalNameDTO arg0) {
					util.unmask();
					selectedPortal = arg0;
					showPortalViewport();
				}
			};

			portalsService.getName(itemSelected.getDTO().getId(), portalCallback);
		}
	}

	/**
	 * Puts the portal viewport as active in the main panel and update the navigation history.
	 */
	private void showPortalViewport() {

		viewport = (PortalViewport) PmsFactory.getInstance().getPortalViewport();
		viewport.setPortalName(selectedPortal);
		viewport.init();

		if (!viewport.isAttached()) {
			pmsMainPanel.add(viewport);
			// History.addValueChangeHandler(new ValueChangeHandler<String>() {
			//
			// public void onValueChange(ValueChangeEvent<String> event) {
			// if (PORTAL_HISTORY_ITEM.equals(event.getValue())) {
			// navigationPanel.initNavigation();
			// addNavigationItems();
			// viewport.init();
			// pmsMainPanel.activateViewport(viewport);
			// }
			// }
			// });
		}

		addNavigationItems();
		History.newItem(PORTAL_HISTORY_ITEM);
		pmsMainPanel.activateViewport(viewport);
	}

	private void addNavigationItems() {
		navigationPanel.addNewNavigationItem(pmsMessages.menuItem2Portals(), false);
		navigationPanel.addNewNavigationItem(selectedPortal.getName().getDisplayName(), true);
	}

	/**
	 * Requests the portals service for deletion of bound portal.<br/>
	 * @param portal
	 */
	private void tryDeletePortal(PortalSelModelData portal) {
		util.mask(pmsMessages.mskDeletePortal());

		AsyncCallback<PortalTreeDTO> callback = new AsyncCallback<PortalTreeDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrPortals, pmsMessages.msgErrorDeletePortal());
			}

			public void onSuccess(PortalTreeDTO arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessDeletePortal());
			}
		};

		portalsService.delete(portal.getDTO().getId(), callback);
	}

	/**
	 * Try publish a portal.
	 * @param portal portal to publish
	 * @param tryParents if true, try publish portal parents too
	 */
	private void tryPublishPortal(final PortalSelModelData portal, final boolean tryParents) {

		mask(pmsMessages.mskPublishPortal());

		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

			public void onSuccess(Boolean result) {
				unmask();

				if (result) {
					util.info(pmsMessages.msgSuccessPublishPortal());
					tryGetPortals();
				} else {
					util.info(pmsMessages.msgNothingToPublish());
				}

			}

			public void onFailure(Throwable caught) {
				unmask();
				if (caught instanceof ModifiedParentPortalsException && !tryParents) {
					Listener<MessageBoxEvent> listenerPublishParent = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
								tryPublishPortal(portal, true);
							}
						}
					};
					util.confirm(pmsMessages.msgPublishParentPortal(), listenerPublishParent);

				} else {
					errorProcessor.processError(caught, emrPortals, pmsMessages.msgErrorPublishPortal());
				}
			}
		};
		editionsService.publishPortal(portal.getDTO().getId(), tryParents, callback);
	}

	private class UrlsPopUp extends Popup {

		private PortalSelModelData portalModel;
		private PortalURLsDTO urlsDto;

		public UrlsPopUp(PortalSelModelData model, PortalURLsDTO urls) {
			this.portalModel = model;
			this.urlsDto = urls;

			setScrollMode(Scroll.AUTO);
			addPanel();
		}

		private void addPanel() {
			ContentPanel panel = new ContentPanel();
			panel.setFrame(true);
			panel.setHeadingText(portalModel.getDTO().getName());
			panel.getHeader().addTool(new ToolButton("x-tool-close", new SelectionListener<IconButtonEvent>() {
				@Override
				public void componentSelected(IconButtonEvent ce) {
					UrlsPopUp.this.hide();
				}
			}));

			HorizontalPanel offlinePanel = new HorizontalPanel();
			offlinePanel.add(new Html("<b>Offline:</b>"));

			String urlOffline = urlsDto.getOffline();
			offlinePanel.setSpacing(3);
			if (urlOffline != null) {
				offlinePanel.add(new Html(PmsConstants.HTML_LINK.replaceAll(PmsConstants.PATTERN_HREF, urlOffline)
					.replaceAll(PmsConstants.PATTERN_TEXT, urlOffline)));
			}
			panel.add(offlinePanel);

			HorizontalPanel onlinePanel = new HorizontalPanel();
			onlinePanel.setSpacing(3);
			onlinePanel.add(new Html("<b>Online:</b>"));

			String urlOnline = urlsDto.getOnline();
			if (urlOnline != null) {
				onlinePanel.add(new Html(PmsConstants.HTML_LINK.replaceAll(PmsConstants.PATTERN_HREF, urlOnline)
					.replaceAll(PmsConstants.PATTERN_TEXT, urlOnline)));
			}
			panel.add(onlinePanel);

			add(panel);
		}
	}

	/**
	 * Injects the portals async service proxy.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * Injects the Error Message Resolver for Portals service.
	 * @param emrPortals the emrPortals to set
	 */
	@Inject
	public void setEmrPortals(PortalsServiceErrorMessageResolver emrPortals) {
		this.emrPortals = emrPortals;
	}

	/**
	 * Injects the PMS specific message bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the utilities object
	 * @param u the util to set
	 */
	@Inject
	public void setUtil(Util u) {
		this.util = u;
	}

	/**
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the PMS specific style bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the pms main panel
	 * @param pmsMainPanel the pmsMainPanel to set
	 */
	@Inject
	public void setPmsMainPanel(PmsMainPanel pmsMainPanel) {
		this.pmsMainPanel = pmsMainPanel;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param navigationPanel the navigationPanel to set
	 */
	@Inject
	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}

	/**
	 * Injects the state cell renderer.<br/>
	 * @param stateCellRenderer
	 */
	@Inject
	public void setStateCellRenderer(StateCellRenderer stateCellRenderer) {
		this.stateCellRenderer = stateCellRenderer;
	}

	/**
	 * @param editionsService the editionsService to set
	 */
	@Inject
	public void setEditionsService(IEditionsServiceAsync editionsService) {
		this.editionsService = editionsService;
	}
}
