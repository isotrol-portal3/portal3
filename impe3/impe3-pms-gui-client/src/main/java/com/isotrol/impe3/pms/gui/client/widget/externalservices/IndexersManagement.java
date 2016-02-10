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

package com.isotrol.impe3.pms.gui.client.widget.externalservices;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO.EstadoHerencia;
import com.isotrol.impe3.pms.gui.api.service.IIndexerServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ExternalServiceModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.IndexerModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalConfigurationInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.NavigationPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsMainPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;

public class IndexersManagement extends PmsContentPanel{

	private static final int COLUMN_NAME_WIDTH = 150;
	private static final int COLUMN_STATE_WIDTH = 70;
	

	/**
	 * "Description" column width in pixels.<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 230;
	private static final int COLUMN_TYPE_WIDTH = 150;
	private static final int COLUMN_NODE_WIDTH = 150;

	/**
	 * External Service grid.<br/>
	 */
	private Grid<IndexerModelData> grid = null;

	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<IndexerModelData> store = null;
	
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * "Manage service" tool item.<br/>
	 */
	//private Button ttiManage = null;
	
	private Button ttiStopIndexing= null;
	private Button ttiStartIndexing = null;
	private Button ttiReindex = null;
	private Button ttiForceIndexing = null;

	private IndexerDTO selectedServiceDto = null;

	private PmsViewport viewport = null;

	/*
	 * Injected deps
	 */
	/**
	 * The services errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Cell renderer for ID column.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;
	/**
	 * external services service.<br/>
	 */
	private IIndexerServiceAsync externalServicesService = null;

	/**
	 * Error message resolver for the external services service.<br/>
	 */
	private IErrorMessageResolver emrServicesService = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * PMS specific settings.<br/>
	 */
	private PmsSettings pmsSettings = null;
	
	private static final String TEMPLATE_ICON_CELL = "<div style='text-align: center;'><img src='img/${ICON}' title='${TITLE}' /></div>";
	/**
	 * Pattern "icon" to replace in the template.<br/>
	 */
	private static final String PATTERN_ICON = "\\$\\{ICON\\}";
	/**
	 * Pattern "title" to be replaced in the template.<br/>
	 */
	private static final String PATTERN_TITLE = "\\$\\{TITLE\\}";

	/**
	 * PMS main panel
	 */
	private PmsMainPanel pmsMainPanel = null;

	private NavigationPanel navigationPanel = null;

	public IndexersManagement() {
		/*History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if (getHistoryToken() != null && getHistoryToken().equals(event.getValue()) && viewport != null) {
					navigationPanel.initNavigation();
					addNavigationServiceItems();
					viewport.init();
					pmsMainPanel.activateViewport(viewport);
				}
			}
		});*/
	}

	/**
	 * Inits the widget. Must be explicitly called once dependencies are injected.
	 */
	public void init() {

		setHeaderVisible(false);
		setLayout(new FitLayout());

		initComponent();

		tryGetIndexers();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetIndexers();
			}
		};
		buttonsSupport.addRefreshButton(this, lRefresh);

		addGrid();

		addToolBars();
		addListeners();

	}

	/**
	 * Creates, configures and adds the Services grid.<br/>
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		/*ColumnConfig ccId = new ColumnConfig(ExternalServiceModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setSortable(false);
		ccId.setRenderer(idCellRenderer);
		configs.add(ccId);*/
		
		
		ColumnConfig ccState = new ColumnConfig();
		ccState.setId(IndexerModelData.PROPERTY_STATE);
		ccState.setWidth(COLUMN_STATE_WIDTH);
		ccState.setHeaderText(pmsMessages.columnHeaderState());
		ccState.setRenderer(new GridCellRenderer<IndexerModelData>() {

			public Object render(IndexerModelData model, String property, ColumnData config,
				int rowIndex, int colIndex, ListStore<IndexerModelData> store,
				Grid<IndexerModelData> grid) {

				String icon = null;
				String title = null;
				String configuration = model.getDTO().getState();
				if (configuration == null) {
					return "";
				} else if (configuration.equals("ON")) {
					icon = Constants.OK_IMAGE;
					title = pmsMessages.titleIndexadorArrancado();
				} else {
					icon = Constants.ERROR_IMAGE;
					title = pmsMessages.titleIndexadorParado();
				}
				return TEMPLATE_ICON_CELL.replaceAll(PATTERN_ICON, icon).replaceAll(PATTERN_TITLE, title);
			}
		});
		configs.add(ccState);

		ColumnConfig ccName = new ColumnConfig();
		ccName.setId(IndexerModelData.PROPERTY_NAME);
		ccName.setWidth(COLUMN_NAME_WIDTH);
		ccName.setHeaderText(pmsMessages.columnHeaderName());
		configs.add(ccName);

		ColumnConfig ccDescription = new ColumnConfig();
		ccDescription.setId(IndexerModelData.PROPERTY_DESCRIPTION);
		ccDescription.setWidth(COLUMN_DESCRIPTION_WIDTH);
		ccDescription.setHeaderText(pmsMessages.columnHeaderDescription());
		configs.add(ccDescription);
		
		ColumnConfig ccType = new ColumnConfig();
		ccType.setId(IndexerModelData.PROPERTY_TYPE);
		ccType.setWidth(COLUMN_TYPE_WIDTH);
		ccType.setHeaderText(pmsMessages.columnHeaderType());
		configs.add(ccType);
		
		ColumnConfig ccNode = new ColumnConfig();
		ccNode.setId(IndexerModelData.PROPERTY_NODE);
		ccNode.setWidth(COLUMN_NODE_WIDTH);
		ccNode.setHeaderText(pmsMessages.columnHeaderNode());
		configs.add(ccNode);
		
		

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<IndexerModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(IndexerModelData.PROPERTY_NAME);
		store.setSortField(IndexerModelData.PROPERTY_NODE);


		grid = new Grid<IndexerModelData>(store, cm);
		grid.setAutoExpandColumn(IndexerModelData.PROPERTY_DESCRIPTION);
		grid.getView().setForceFit(true);

		/*grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<IndexerModelData>>() {
			
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 
			*//**
			 * <br/>
			 *//*
			public void handleEvent(GridEvent<IndexerModelData> be) {
				//getSelectedAndDisplayDetails();
			}
		});*/

		/*Listener<SelectionChangedEvent<ExternalServiceModelData>> listener = new Listener<SelectionChangedEvent<ExternalServiceModelData>>() {
			public void handleEvent(SelectionChangedEvent<ExternalServiceModelData> sce) {
				boolean enabled = true;
				if (sce.getSelection().isEmpty()) {
					enabled = false;
				}
				ttiManage.setEnabled(enabled);
			}
		};
		grid.getSelectionModel().addListener(Events.SelectionChange, listener);*/

		add(grid);
	}

	public Grid<IndexerModelData> getGrid() {
		return grid;
	}
	@Inject
	public void setGrid(Grid<IndexerModelData> grid) {
		this.grid = grid;
	}

	public ListStore<IndexerModelData> getStore() {
		return store;
	}
	@Inject
	public void setStore(ListStore<IndexerModelData> store) {
		this.store = store;
	}

	public IndexerDTO getSelectedServiceDto() {
		return selectedServiceDto;
	}
	@Inject
	public void setSelectedServiceDto(IndexerDTO selectedServiceDto) {
		this.selectedServiceDto = selectedServiceDto;
	}

	public InformationCellRenderer getIdCellRenderer() {
		return idCellRenderer;
	}

	public Util getUtil() {
		return util;
	}

	public IErrorMessageResolver getEmrServicesService() {
		return emrServicesService;
	}

	public GuiCommonMessages getMessages() {
		return messages;
	}

	public Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	public PmsMainPanel getPmsMainPanel() {
		return pmsMainPanel;
	}

	public NavigationPanel getNavigationPanel() {
		return navigationPanel;
	}

	/**
	 * Retrieves the available services from the server, and populates the grid with the result.<br/>
	 */
	private void tryGetIndexers() {
		util.mask(pmsMessages.mskServices());

		AsyncCallback<IndexersDTO> callback = new AsyncCallback<IndexersDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
			}

			public void onSuccess(IndexersDTO arg0) {

				List<IndexerModelData> mdList = new LinkedList<IndexerModelData>();
				for (IndexerDTO dto : arg0.getList()) {
					mdList.add(new IndexerModelData(dto));
				}

				store.removeAll();
				store.add(mdList);

				util.unmask();
			}
		};
		externalServicesService.getIndexers(callback);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */

		
		private void addToolBars() {
			ToolBar toolBar = new ToolBar();
			ToolBar bottomToolBar = new ToolBar();
			
			ttiStartIndexing = buttonsSupport.addGenericButton(pmsMessages.labelStartIndexing(),
					pmsMessages.ttStartIndexing(), pmsStyles.iconCheck(), toolBar,
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							final IndexerModelData selected = grid.getSelectionModel().getSelectedItem();
							if (selected != null) {
								Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
									public void handleEvent(MessageBoxEvent be) {
										Button clicked = be.getButtonClicked();
										if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
											tryStartIndexing(selected);
											enableDisableButtons(null);
											
										}
									}
								};
								MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmStartIndexation(),
									lConfirm).setModal(true);
							}
						}
					});

				ttiStartIndexing.setEnabled(false);
				toolBar.add(new SeparatorToolItem());
				
				
				ttiStopIndexing = buttonsSupport.addGenericButton(pmsMessages.labelStopIndexing(),
						pmsMessages.ttStopIndexing(), pmsStyles.iconNotCheck(), toolBar,
						new SelectionListener<ButtonEvent>() {
							@Override
							public void componentSelected(ButtonEvent ce) {
								final IndexerModelData selected = grid.getSelectionModel().getSelectedItem();
								if (selected != null) {
									Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
										public void handleEvent(MessageBoxEvent be) {
											Button clicked = be.getButtonClicked();
											if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
												tryStopIndexing(selected);
												enableDisableButtons(null);
												
											}
										}
									};
									MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmStopIndexation(),
										lConfirm).setModal(true);
								}
							}
						});

					ttiStopIndexing.setEnabled(false);
					toolBar.add(new SeparatorToolItem());
					
					
					
			ttiReindex = buttonsSupport.addGenericButton(pmsMessages.labelReindexing(),
						pmsMessages.ttReindexing(), pmsStyles.iconReindex(), toolBar,
						new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							final IndexerModelData selected = grid.getSelectionModel().getSelectedItem();
							if (selected != null) {
								Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
									public void handleEvent(MessageBoxEvent be) {
										Button clicked = be.getButtonClicked();
										if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
											tryForceIndexing(selected,true);
											enableDisableButtons(null);
										}else if(clicked != null && clicked.getItemId().equals(Dialog.NO)){
											tryForceIndexing(selected,false);
											enableDisableButtons(null);
										}
							
									}
					
								};
								confirm(messages.headerConfirmWindow(), pmsMessages.msgmsgConfirmSaveCopyOfIndexers(),
										lConfirm, MessageBox.YESNOCANCEL).setModal(true);
							}
	
						}
			});
	

				ttiReindex.setEnabled(false);
				toolBar.add(new SeparatorToolItem());
					
				
				
				ttiForceIndexing = buttonsSupport.addGenericButton(pmsMessages.labelForceIndexing(),
						pmsMessages.ttForceIndexing(), pmsStyles.iconForceReindex(), toolBar,
						new SelectionListener<ButtonEvent>() {
							
					@Override
					public void componentSelected(ButtonEvent ce) {
					final IndexerModelData selected = grid.getSelectionModel().getSelectedItem();
					if (selected != null) {
						Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent be) {
								Button clicked = be.getButtonClicked();
								if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
									tryReindexing(selected);
									enableDisableButtons(null);
									
								}
							}
						};
						MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmReindexation(),
							lConfirm).setModal(true);
					}
				}
			});
		
				ttiForceIndexing.setEnabled(false);
				//toolBar.add(new SeparatorToolItem());



		


		setTopComponent(toolBar);
	}
		
		
	  /**
	   * Displays a confirmation message box with Yes and No buttons (comparable to
	   * JavaScript's confirm).
	   * 
	   * @param title the title bar text
	   * @param msg the message box body text
	   * @param callback the listener invoked after the message box is closed
	   * @return the new message box instance
	   */
	  public static MessageBox confirm(String title, String msg, Listener<MessageBoxEvent> callback, String buttons) {
	    MessageBox box = new MessageBox();
	    box.setTitleHtml(title);
	    box.setMessage(msg);
	    box.addCallback(callback);
	    box.setIcon(MessageBox.QUESTION);
	    box.setButtons(buttons);
	    box.show();
	    return box;
	  }
		
		public void tryStopIndexing(final IndexerModelData selected){
			util.mask(pmsMessages.mskServices());

			AsyncCallback<IndexerDTO> callback = new AsyncCallback<IndexerDTO>() {
				public void onFailure(Throwable arg0) {
					util.unmask();
					errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
				}

				public void onSuccess(IndexerDTO arg0) {

					/*List<IndexerModelData> mdList = new LinkedList<IndexerModelData>();
					for (IndexerDTO dto : arg0.getList()) {
						mdList.add(new IndexerModelData(dto));
					}*/
					IndexerModelData nuevo= new IndexerModelData(arg0);
					store.remove(selected);
					store.add(nuevo);

					util.unmask();
				}
			};
			externalServicesService.stopIndexer(selected.getDTO().getId(),selected.getDTO().getNode() ,callback);
		}
		
		public void tryStartIndexing(final IndexerModelData selected){
			util.mask(pmsMessages.mskServices());

			AsyncCallback<IndexerDTO> callback = new AsyncCallback<IndexerDTO>() {
				public void onFailure(Throwable arg0) {
					util.unmask();
					errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
				}

				public void onSuccess(IndexerDTO arg0) {

					/*List<IndexerModelData> mdList = new LinkedList<IndexerModelData>();
					for (IndexerDTO dto : arg0.getList()) {
						mdList.add(new IndexerModelData(dto));
					}*/
					IndexerModelData nuevo= new IndexerModelData(arg0);
					store.remove(selected);
					store.add(nuevo);

					util.unmask();
				}
			};
			externalServicesService.startIndexer(selected.getDTO().getId(),selected.getDTO().getNode() ,callback);
		}
		
		
		
		public void tryReindexing(final IndexerModelData selected){
			util.mask(pmsMessages.mskServices());

			AsyncCallback<IndexerDTO> callback = new AsyncCallback<IndexerDTO>() {
				public void onFailure(Throwable arg0) {
					util.unmask();
					errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
				}

				public void onSuccess(IndexerDTO arg0) {

					/*List<IndexerModelData> mdList = new LinkedList<IndexerModelData>();
					for (IndexerDTO dto : arg0.getList()) {
						mdList.add(new IndexerModelData(dto));
					}*/
					IndexerModelData nuevo= new IndexerModelData(arg0);
					store.remove(selected);
					store.add(nuevo);

					util.unmask();
				}
			};
			externalServicesService.reindex(selected.getDTO().getId(),selected.getDTO().getNode() ,callback);
		}
		
		public void tryForceIndexing(final IndexerModelData selected,boolean copia){
			util.mask(pmsMessages.mskServices());

			AsyncCallback<IndexerDTO> callback = new AsyncCallback<IndexerDTO>() {
				public void onFailure(Throwable arg0) {
					util.unmask();
					errorProcessor.processError(arg0, emrServicesService, pmsMessages.msgErrorRetrieveExternalServices());
				}

				public void onSuccess(IndexerDTO arg0) {

					/*List<IndexerModelData> mdList = new LinkedList<IndexerModelData>();
					for (IndexerDTO dto : arg0.getList()) {
						mdList.add(new IndexerModelData(dto));
					}*/
					IndexerModelData nuevo= new IndexerModelData(arg0);
					store.remove(selected);
					store.add(nuevo);

					util.unmask();
				}
			};
			externalServicesService.reindexAll(selected.getDTO().getId(),selected.getDTO().getNode(),copia ,callback);
		}
		
		
		
		private void addListeners(){
			grid.addListener(Events.RowClick, new Listener<GridEvent<IndexerModelData>>() {
				public void handleEvent(GridEvent<IndexerModelData> ge) {
					IndexerModelData model = grid.getSelectionModel().getSelectedItem();
					if (model != null) {
						enableDisableButtons(model);
					}
				}
			});
		}
		

	/**
	 * 
	 * @param model
	 */
	private void enableDisableButtons(IndexerModelData model) {
		String estado = null;
		if (model != null) {
			estado = model.getDTO().getState();
		}
	
		if (estado == null) {
			ttiStopIndexing.disable();
			ttiStartIndexing.disable();
			ttiReindex.disable();
			ttiForceIndexing.disable();
		} else if(estado=="ON") {
			ttiStopIndexing.enable();
			ttiStartIndexing.disable();
			ttiReindex.enable();
			ttiForceIndexing.enable();
		} else {
			ttiStopIndexing.disable();
			ttiStartIndexing.enable();
			ttiReindex.enable();
			ttiForceIndexing.enable();
		}
	}

			
		

	/**
	 * Retrieves from service the detailed data of the selected element, and then displays it in an edition panel.<br/>
	 */
	/*private void getSelectedAndDisplayDetails() {
		IndexerModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			selectedServiceDto = selected.getDTO();
			tryDisplayDetails();
		}
	}*/

	/**
	 * Opens the External Service visualizer entry point in a new window, for the passed DTO.<br/>
	 * @param serviceDto the selected External Service
	 */
	/*private void tryDisplayDetails() {

		viewport = getExternalServiceViewport(selectedServiceDto);
		if (!viewport.isAttached()) {
			pmsMainPanel.add(viewport);
		}
		// update the navigation
		addNavigationServiceItems();
		if (getHistoryToken() != null) {
			History.newItem(getHistoryToken());
		}
		pmsMainPanel.activateViewport(viewport);
	}

	//private void addNavigationServiceItems() {
		navigationPanel.addNewNavigationItem(getServiceManagementName(), false);
		navigationPanel.addNewNavigationItem(selectedServiceDto.getName(), true);
	}*/

	//protected abstract String getServiceManagementName();

	/**
	 * Returns the pms viewport.<br/>
	 * @param serviceDto
	 * @return the pms viewport.<br/>
	 */
	//protected abstract PmsViewport getExternalServiceViewport(ExternalServiceDTO serviceDto);

	/**
	 * Returns the type of service managed by this widget.<br/>
	 * @return the type of service managed by this widget.<br/>
	 */
	//protected abstract ExternalServiceType getManagedServiceType();

	/**
	 * Return an icon style for "Manage" button.<br/>
	 * @return
	 */
	//protected abstract String getManageIconStyle();

	/**
	 * Returns the history token
	 * @return the history token
	 */
	//protected abstract String getHistoryToken();

	/**
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * Injects the External Services async service.
	 * @param service
	 */
	@Inject
	public void setExternalServicesService(IIndexerServiceAsync service) {
		this.externalServicesService = service;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the PMS specific settings.<br/>
	 * @param pmsSettings
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}

	
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}
	/**
	 * Returns the PMS specific settings bundle<br/>
	 * @return the PMS specific settings bundle
	 */
	protected PmsSettings getPmsSettings() {
		return pmsSettings;
	}

	/**
	 * Returns the generic styles bundle.
	 * @return the generic styles bundle.
	 */
	public GuiCommonStyles getStyles() {
		return styles;
	}

	/**
	 * Returns the PMS specific styles bundle.<br/>
	 * @return the PMS specific styles bundle.<br/>
	 */
	protected PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * Injects the PMS specific styles bundle.<br/>
	 * @param pmsStyles the PMS specific styles bundle.<br/>
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
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
	 * Injects the Error Message Resolver for the External Services service.
	 * @param emr the emrServicesService to set
	 */
	@Inject
	public void setEmrServicesService(IErrorMessageResolver emr) {
		this.emrServicesService = emr;
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
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service errors processor
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
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
	 * @param navigationPanel the navigationPanel to set
	 */
	@Inject
	public void setNavigationPanel(NavigationPanel navigationPanel) {
		this.navigationPanel = navigationPanel;
	}
}

