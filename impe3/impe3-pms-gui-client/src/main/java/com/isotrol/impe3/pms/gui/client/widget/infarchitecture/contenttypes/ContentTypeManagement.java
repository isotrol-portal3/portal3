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

package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.ContentTypesController;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ContentTypeErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.renderer.StateCellRenderer;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Gui for content types management
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentTypeManagement extends PmsContentPanel {

	/**
	 * Width for <b>state</b> column<br/>
	 */
	private static final int COLUMN_STATE_WIDTH = Constants.COLUMN_ICON_WIDTH;
	/**
	 * Width for <b>name</b> column<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 200;
	/**
	 * Width for <b>description</b> column<br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 400;

	/**
	 * Detail view container for Content Type edition/creation.<br/>
	 */
	private Window pDetail = null;

	/**
	 * Store for {@link #grid} and {@link #filter}.<br/>
	 */
	private ListStore<ContentTypeSelModelData> store = null;

	/**
	 * Master view Content Types grid.<br/>
	 */
	private Grid<ContentTypeSelModelData> grid = null;

	/** edit contentype button */
	private Button ttiEdit = null;
	/** delete contenttype button */
	private Button ttiDelete = null;

	/**
	 * Filters the {@link #grid} elements.<br/>
	 */
	private CustomizableStoreFilter<ContentTypeSelModelData> filter = null;

	/*
	 * Injected deps
	 */
	/**
	 * Service errors processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Error message resolver for {@link ContentTypesService#delete(String)}.<br/>
	 */
	private ContentTypeErrorMessageResolver errorMessageResolver = null;
	/**
	 * <b>State</b> cell renderer in the {@link #grid}.<br/>
	 */
	private StateCellRenderer stateCellRenderer = null;

	/**
	 * <b>ID</b> cell renderer in the {@link #grid}.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * proxy to Content Types async service.<br/>
	 */
	private IContentTypesServiceAsync contentTypesService = null;

	/**
	 * General messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * The sorter used in the grid store.<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Pms styles
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Constructor. Creates the sources mapping tab panel, inits the asynchronous service and add the listeners.
	 */
	public ContentTypeManagement() {
	}

	/**
	 * Inits the widget. Must be called after the dependencies are injected.
	 */
	public void init() {
		initComponent();
		initController();

		// async call
		tryGetContentTypes();
	}

	/**
	 * Provides the controller with a Listener.<br/>
	 */
	private void initController() {
		ContentTypesController controller = (ContentTypesController) contentTypesService;
		controller.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.ADD:
						case PmsChangeEvent.UPDATE:
							tryGetContentTypes();
							break;
						case PmsChangeEvent.DELETE:
							String id = event.getEventInfo();
							ContentTypeSelModelData ctsModelData = store.findModel(ContentTypeSelModelData.PROPERTY_ID,
								id);
							store.remove(ctsModelData);
							grid.getView().refresh(false);
							break;
						case PmsChangeEvent.REFRESH:
							disableButtons();
							tryGetContentTypes();
							break;
						default:
							// nothing to do
					}
				}
			}
		});
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		setLayoutOnChange(true);
		setLayout(new FitLayout());

		addGrid();
		addTopToolBar();
		addBottomToolBar();
	}

	/**
	 * Creates, configures and adds the grid.<br/>
	 */
	private void addGrid() {
		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig config = new ColumnConfig(ContentTypeSelModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		config.setSortable(false);
		config.setRenderer(idCellRenderer);
		configs.add(config);

		config = new ColumnConfig();
		config.setId(ContentTypeSelModelData.PROPERTY_STATE);
		config.setHeader(pmsMessages.columnHeaderState());
		config.setWidth(COLUMN_STATE_WIDTH);
		config.setSortable(false);
		config.setRenderer(stateCellRenderer);
		configs.add(config);

		config = new ColumnConfig();
		config.setId(ContentTypeSelModelData.PROPERTY_NAME);
		config.setWidth(COLUMN_NAME_WIDTH);
		config.setHeader(pmsMessages.columnHeaderName());
		/*config.setRenderer(new GridCellRenderer<ContentTypeSelModelData>() {

			public Object render(ContentTypeSelModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<ContentTypeSelModelData> store, Grid<ContentTypeSelModelData> grid) {
				return Format.htmlEncode((String) model.get(property));
			}
		});*/
		configs.add(config);

		config = new ColumnConfig();
		config.setId(ContentTypeSelModelData.PROPERTY_DESCRIPTION);
		config.setWidth(COLUMN_DESCRIPTION_WIDTH);
		config.setHeader(pmsMessages.columnHeaderDescription());
		configs.add(config);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<ContentTypeSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ContentTypeSelModelData.PROPERTY_NAME);

		grid = new Grid<ContentTypeSelModelData>((ListStore<ContentTypeSelModelData>) store, cm);
		grid.setSelectionModel(new GridSelectionModel<ContentTypeSelModelData>());
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ContentTypeSelModelData>>() {
			/**
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(GridEvent<ContentTypeSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ContentTypeSelModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<ContentTypeSelModelData> se) {
				if (se.getSelection() != null && !se.getSelection().isEmpty()) {
					enableButtons();
				} else {
					disableButtons();
				}
			}
		});

		grid.setAutoExpandColumn(ContentTypeSelModelData.PROPERTY_NAME);
		grid.setLoadMask(true);
		grid.getView().setForceFit(true);

		add(grid);

	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addTopToolBar() {
		ToolBar toolBar = new ToolBar();

		SelectionListener<ButtonEvent> lNew = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				showCreationPanel();
			}
		};
		buttonsSupport.addAddButton(toolBar, lNew, null);
		buttonsSupport.addSeparator(toolBar);

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				final ContentTypeSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) { // confirmed
								tryDelete(selected.getDTO().getId());
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteContentType(),
						listener).setModal(true);
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

		toolBar.add(new FillToolItem());

		// Filter:
		filter = new CustomizableStoreFilter<ContentTypeSelModelData>(Arrays.asList(new String[] {
			ContentTypeSelModelData.PROPERTY_NAME, ContentTypeSelModelData.PROPERTY_DESCRIPTION}));
		filter.bind(store);
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetContentTypes();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		// disable remove and edit buttons
		disableButtons();

		setTopComponent(toolBar);
	}
	
	/**
	 * Creates, configures and adds the bottom toolbar.<br/>
	 */
	private void addBottomToolBar() {
		ToolBar toolBar = new ToolBar();

		// Export button
		buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ContentTypesExportWindow ctWindow = PmsFactory.getInstance().getContentTypeExportWindow();
					ctWindow.show();
				}
			});
		buttonsSupport.addSeparator(toolBar);

		// Import button
		buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ContentTypesImportWindow ctWindow = PmsFactory.getInstance().getContentTypeImportWindow();
					ctWindow.show();
				}
			});
		
		toolBar.add(new FillToolItem());
		
		// "Help" button:
		buttonsSupport.addHelpButton(toolBar, settings.pmsContentTypesAdminPortalManualUrl());

		setBottomComponent(toolBar);
	}

	/**
	 * Retrieves from service the details of the selected Content Type, and then displays them on a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		ContentTypeSelModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryRetrieveAndDisplayDetails(selected);
		}
	}

	/**
	 * Disables "edit" and "delete" buttons.<br/>
	 */
	private void disableButtons() {
		ttiEdit.disable();
		ttiDelete.disable();
	}

	/**
	 * Enables "edit" and "delete" buttons.<br/>
	 */
	private void enableButtons() {
		ttiEdit.enable();
		ttiDelete.enable();
	}

	/**
	 * Retrieves the Content Types from service. On success, displays them on the grid.<br/>
	 */
	private void tryGetContentTypes() {

		util.mask(pmsMessages.mskContentTypes());

		AsyncCallback<List<ContentTypeSelDTO>> ctCallback = new AsyncCallback<List<ContentTypeSelDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveContentTypes());
			}

			public void onSuccess(List<ContentTypeSelDTO> contentTypes) {
				store.removeAll();
				/*store.clearFilters();
				filter.setValue(null);*/
				List<ContentTypeSelModelData> datas = new ArrayList<ContentTypeSelModelData>();
				for (ContentTypeSelDTO dto : contentTypes) {
					datas.add(new ContentTypeSelModelData(dto));
				}
				store.add(datas);
				util.unmask();
			}
		};

		contentTypesService.getContentTypes(ctCallback);
	}

	/**
	 * Calls Content Types remote service "delete" operation for the passed Content Type.<br/>
	 * 
	 * @param selected
	 */
	private void tryDelete(String id) {
		util.mask(pmsMessages.mskDeleteContentType());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorDeleteContentType());
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
			 */
			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteContentType());
			}
		};

		contentTypesService.delete(id, callback);
	}

	/**
	 * retrieves the passed Content Type detail data via RPC and displays it on the creation panel.<br/>
	 * 
	 * @param ctSel
	 */
	private void tryRetrieveAndDisplayDetails(final ContentTypeSelModelData ctSel) {
		util.mask(pmsMessages.mskContentType());

		AsyncCallback<ContentTypeDTO> callback = new AsyncCallback<ContentTypeDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorRetrieveContentType(ctSel
					.getDTO().getName()));
			}

			public void onSuccess(ContentTypeDTO ct) {
				showEditionPanel(ct);
				util.unmask();
			}
		};

		contentTypesService.get((String) ctSel.get(ContentTypeSelModelData.PROPERTY_ID), callback);
	}

	/**
	 * Closes the detail window, if attached to DOM.<br/>
	 */
	private void closeDetailWindow() {
		if (pDetail != null && pDetail.isAttached()) {
			pDetail.hide();
		}
	}

	/**
	 * Creates an edition panel and binds it to a new Content Type.<br/>
	 */
	private void showCreationPanel() {
		ContentTypeDTO ct = new ContentTypeDTO();
		ct.setState(State.NEW);
		NameDTO defaultName = new NameDTO();
		defaultName.setDisplayName(pmsMessages.defaultValueContentTypeDisplayName());
		defaultName.setPath(pmsMessages.defaultValueContentTypePath());
		ct.setDefaultName(defaultName);

		closeDetailWindow();
		ContentTypeCreation contentTypeCreation = PmsFactory.getInstance().getContentTypeCreation();
		contentTypeCreation.init(ct);
		pDetail = contentTypeCreation;
		pDetail.show();
	}

	/**
	 * If exists, closes the edition panel.<br/> Creates from scratch a new edition panel, for the passed Content Type
	 * DTO.
	 * @param ct
	 */
	private void showEditionPanel(ContentTypeDTO ct) {

		closeDetailWindow();
		ContentTypeEdition contentTypeEdition = PmsFactory.getInstance().getContentTypeEdition();
		contentTypeEdition.init(ct);
		pDetail = contentTypeEdition;
		pDetail.show();
	}

	/**
	 * Injets the Content Types async service proxy.
	 * @param contentTypesService
	 */
	@Inject
	public void setContentTypesService(IContentTypesServiceAsync contentTypesService) {
		this.contentTypesService = contentTypesService;
	}

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
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects container
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the alphabetical store sorter.<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
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
	 * Injects the error message resolver.
	 * @param emr the errorMessageResolver to set
	 */
	@Inject
	public void setErrorMessageResolver(ContentTypeErrorMessageResolver emr) {
		this.errorMessageResolver = emr;
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
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
	
	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
