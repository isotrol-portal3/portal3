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

package com.isotrol.impe3.pms.gui.client.widget.portaluser;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.BooleanCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.gui.api.service.external.IPortalUsersExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalUserSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalUsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.UsersMessages;
import com.isotrol.impe3.pms.gui.client.i18n.UsersSettings;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserSelDTO;


/**
 * Users Master view. Displays a grid with the registered users, and a toolbar for operations on users.
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalUsersList extends ContentPanel implements IInitializableWidget {

	/**
	 * Width for "name" column<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 100;
	/**
	 * Width for "display" column.<br/>
	 */
	private static final int COLUMN_DISPLAY_NAME_WIDTH = 200;
	/**
	 * Width for "active" column.<br/>
	 */
	private static final int COLUMN_ACTIVE_WIDTH = 50;

	/**
	 * Users grid instance.<br/>
	 */
	private Grid<PortalUserSelModelData> grid = null;
	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<PortalUserSelModelData> store = null;
	/** user edit button */
	private Button ttiEdit = null;
	/** user delete button */
	private Button ttiDelete = null;
	/** user change password button */
	private Button ttiChangePwd = null;
	/**
	 * Filter bound to users grid.<br/>
	 */
	private CustomizableStoreFilter<PortalUserSelModelData> filter = null;
	
	/**
	 * support for {@link IInitializableWidget#init()}<br/>
	 */
	private boolean initialized = false;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Sorter used in the {@link #grid}.
	 */
	private AlphabeticalStoreSorter storeSorter = null;
	/**
	 * Cell renderer for <b>ID</b> cell.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	/**
	 * Cell renderer for <b>active</b> grid cell.<br/>
	 */
	private BooleanCellRenderer activeCellRenderer = null;
	/**
	 * Reference to users remote service proxy.<br/>
	 */
	private IPortalUsersExternalServiceAsync service = null;

	/**
	 * Error Message Resolver<br/>
	 */
	private PortalUsersServiceErrorMessageResolver emr = null;

	/**
	 * Reference to common messages service.<br/>
	 */
	private GuiCommonMessages commonMessagesBundle = null;

	/**
	 * Reference to users messages service.<br/>
	 */
	private UsersMessages usersMessagesBundle = null;

	/**
	 * Common stylesBundle service.<br/>
	 */
	private GuiCommonStyles stylesBundle = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsHelper = null;

	/**
	 * Reference to users settings.<br/>
	 */
	private UsersSettings usersSettings = null;

	/**
	 * Utilities.<br/>
	 */
	private Util util = null;

	/**
	 * Constructor for the editions management panel.<br/>
	 */
	public PortalUsersList() {
	}

	/**
	 * Inits the widget. Must be called after the dependencies injection.
	 */
	public PortalUsersList init() {
		
		initialized = true;
		
		configThis();
		initComponents();

		tryGetUsers();

		return this;
	}

	/**
	 * Configures this panel properties.<br/>
	 */
	private void configThis() {
		setHeaderVisible(false);
		setLayout(new FitLayout());
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {
		addGrid();
		addToolBar();
	}

	/**
	 * Creates and configures the toolbar.<br/>
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		// "new" button
		SelectionListener<ButtonEvent> lNew = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				showDetailsPanel(null);
			}
		};
		buttonsHelper.addGenericButton(commonMessagesBundle.labelAdd(), stylesBundle.iAddUser(), toolBar, lNew);
		buttonsHelper.addSeparator(toolBar);

		// "delete" button
		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				final PortalUserSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) { // confirmed
								tryRemoveUser(selected);
							}
						}
					};
					MessageBox.confirm("Confirmar eliminación de Usuario",
						"¿Seguro que desea eliminar el Usuario seleccionado?", listener).setModal(true);
				}
			}
		};
		ttiDelete = buttonsHelper.addGenericButton(commonMessagesBundle.labelDelete(), stylesBundle.iDeleteUser(),
			toolBar, lDelete);
		buttonsHelper.addSeparator(toolBar);

		// "edit" button
		SelectionListener<ButtonEvent> lEdit = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndDisplayDetails();
			}
		};
		ttiEdit = buttonsHelper.addEditButton(toolBar, lEdit);
		buttonsHelper.addSeparator(toolBar);

		// change password button
		SelectionListener<ButtonEvent> lChangePwd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				PortalUserSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					openChangePwdWindow(selected);
				}
			}
		};
		ttiChangePwd = buttonsHelper.addGenericButton(commonMessagesBundle.labelChangePassword(), stylesBundle
			.iChangePassword(), toolBar, lChangePwd);
		toolBar.add(ttiChangePwd);

		toolBar.add(new FillToolItem());

		filter = new CustomizableStoreFilter<PortalUserSelModelData>(Arrays.asList(new String[] {
			PortalUserSelModelData.PROPERTY_NAME, PortalUserSelModelData.PROPERTY_DISPLAY_NAME}));
		filter.bind(store);
		filter.setHideLabel(false);
		toolBar.add(filter);

		buttonsHelper.addRefreshButton(toolBar, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetUsers();
			}
		});

		// disable remove and edit buttons
		enableButtons(false);

		setTopComponent(toolBar);
	}

	/**
	 * Retrieves from service the details data for the selected User, and displays that data in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		PortalUserSelModelData selected = grid.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryRetrieveAndDisplayDetails(selected);
		}
	}

	/**
	 * Requests the service for user details. On success displays them on the detail panel.<br/>
	 * 
	 * @param portalUser
	 */
	private void tryRetrieveAndDisplayDetails(PortalUserSelModelData portalUser) {
		util.mask(commonMessagesBundle.mskUser());

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emr, usersMessagesBundle.msgErrorGettingUserData());
			}

			public void onSuccess(PortalUserDTO user) {
				showDetailsPanel(user);
				util.unmask();
			}
		};

		service.getById(Window.Location.getParameter(usersSettings.paramUsersPortalService()), portalUser.getDTO()
			.getId(), callback);

	}

	/**
	 * Creates and shows a details panel for the passed user DTO.<br/>
	 * 
	 * @param user
	 */
	private void showDetailsPanel(PortalUserDTO user) {
		UserEditorPanel wDetails = PmsFactory.getInstance().getUserEditorPanel();
		wDetails.init(user);

		Listener<AppEvent> lChange = new Listener<AppEvent>() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(AppEvent event) {
				GWT.log(PortalUsersList.this.getClass().getName() + ": catching an Add/Change event", null);
				PortalUserDTO userDto = event.getData();
				PortalUserSelModelData newModel = new PortalUserSelModelData(userDto);
				PortalUserSelModelData oldModel = store.findModel(PortalUserSelModelData.PROPERTY_ID, userDto.getId());
				if (oldModel != null) {
					store.remove(oldModel);
				}
				store.add(newModel);
				// tryGetUsers();
			}
		};
		wDetails.addListener(Events.Add, lChange);
		wDetails.addListener(Events.Change, lChange);
		wDetails.show();
	}

	/**
	 * Requests the service for passed user deletion.<br/>
	 * 
	 * @param userModel
	 */
	private void tryRemoveUser(final PortalUserSelModelData userModel) {
		util.mask(commonMessagesBundle.mskDeleteUser());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emr, usersMessagesBundle.msgErrorGettingUserData());
			}

			public void onSuccess(Void arg) {
				store.remove(userModel);
				util.unmask();
				util.info(commonMessagesBundle.msgOkDeleteUser());
				// tryGetUsers();
			}
		};

		service.delete(Window.Location.getParameter(usersSettings.paramUsersPortalService()), userModel.getDTO()
			.getId(), callback);
	}

	/**
	 * Creates and shows the "change password" window for the passed user model data.<br/>
	 * 
	 * @param portalUser
	 */
	private void openChangePwdWindow(PortalUserSelModelData portalUser) {
		ChangePortalUserPwdWindow pwdWin = PmsFactory.getInstance().getChangePortalUserPwdWindow().init(portalUser.getDTO());
		pwdWin.show();
	}

	/**
	 * Enables or disables the buttons that need a selected user on grid, according to the passed param value.<br/>
	 * 
	 * @param enabled if <code>true</code> enables, otherwise disables.
	 */
	private void enableButtons(boolean enabled) {
		ttiEdit.setEnabled(enabled);
		ttiDelete.setEnabled(enabled);
		ttiChangePwd.setEnabled(enabled);
	}

	/**
	 * Creates and configures the users grid.<br/>
	 */
	private void addGrid() {

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig ccId = new ColumnConfig(PortalUserSelModelData.PROPERTY_ID, commonMessagesBundle.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setSortable(false);
		ccId.setRenderer(idCellRenderer);
		configs.add(ccId);

		ColumnConfig ccName = new ColumnConfig();
		ccName.setId(PortalUserSelModelData.PROPERTY_NAME);
		ccName.setWidth(COLUMN_NAME_WIDTH);
		ccName.setHeader(commonMessagesBundle.columnHeaderUsername());
		configs.add(ccName);

		ColumnConfig ccDisplay = new ColumnConfig();
		ccDisplay.setId(PortalUserSelModelData.PROPERTY_DISPLAY_NAME);
		ccDisplay.setWidth(COLUMN_DISPLAY_NAME_WIDTH);
		ccDisplay.setHeader(commonMessagesBundle.columnHeaderDisplayName());
		configs.add(ccDisplay);

		ColumnConfig ccActive = new ColumnConfig();
		ccActive.setId(PortalUserSelModelData.PROPERTY_ACTIVE);
		ccActive.setWidth(COLUMN_ACTIVE_WIDTH);
		ccActive.setHeader(commonMessagesBundle.columnHeaderActive());
		ccActive.setSortable(false);
		ccActive.setRenderer(activeCellRenderer);
		ccActive.setAlignment(HorizontalAlignment.CENTER);
		configs.add(ccActive);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<PortalUserSelModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(PortalUserSelModelData.PROPERTY_NAME);

		grid = new Grid<PortalUserSelModelData>(store, cm);
		grid.setAutoExpandColumn(PortalUserSelModelData.PROPERTY_NAME);
		grid.setSelectionModel(new GridSelectionModel<PortalUserSelModelData>());
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		grid.addListener(Events.RowClick, new Listener<GridEvent<PortalUserSelModelData>>() {
			public void handleEvent(GridEvent<PortalUserSelModelData> ge) {
				if (grid.getSelectionModel().getSelectedItem() != null) {
					enableButtons(true);
				}
			}
		});

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<PortalUserSelModelData>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<PortalUserSelModelData> be) {
				getSelectedAndDisplayDetails();
			}
		});

		grid.getView().setForceFit(true);

		add(grid);
	}

	/**
	 * Requests the service for registered users.<br/> On success, adds entries to the grid.
	 */
	private void tryGetUsers() {
		util.mask(commonMessagesBundle.mskUsers());

		AsyncCallback<List<PortalUserSelDTO>> callback = new AsyncCallback<List<PortalUserSelDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emr, commonMessagesBundle.msgErrorGetUsers());
			}

			public void onSuccess(List<PortalUserSelDTO> contentTypes) {
				store.removeAll();
				store.clearFilters();
				filter.setValue(null);

				List<PortalUserSelModelData> lModels = new LinkedList<PortalUserSelModelData>();
				for (PortalUserSelDTO dto : contentTypes) {
					lModels.add(new PortalUserSelModelData(dto));
				}
				store.add(lModels);

				util.unmask();
			}
		};

		service.getUsers(Window.Location.getParameter(usersSettings.paramUsersPortalService()), callback);
	}
	
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Injects the users service proxy.
	 * @param service
	 */
	@Inject
	public void setService(IPortalUsersExternalServiceAsync service) {
		this.service = service;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param commonMessagesBundle
	 */
	@Inject
	public void setCommonMessagesService(GuiCommonMessages commonMessagesService) {
		this.commonMessagesBundle = commonMessagesService;
	}

	/**
	 * Injects the users messages bundle
	 * @param usersMessagesBundle
	 */
	@Inject
	public void setUsersMessagesService(UsersMessages usersMessagesService) {
		this.usersMessagesBundle = usersMessagesService;
	}

	/**
	 * Injects the generic stylesBundle bundle.
	 * @param stylesBundle
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.stylesBundle = styles;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsHelper
	 */
	@Inject
	public void setButtonsHelper(Buttons buttonsHelper) {
		this.buttonsHelper = buttonsHelper;
	}

	/**
	 * Injects the users settings.
	 * @param usersSettings
	 */
	@Inject
	public void setUsersSettings(UsersSettings usersSettings) {
		this.usersSettings = usersSettings;
	}

	/**
	 * Injects the cell renderer for the <b>active</b> property.
	 * @param activeCellRenderer the activeCellRenderer to set
	 */
	@Inject
	public void setActiveCellRenderer(BooleanCellRenderer activeCellRenderer) {
		this.activeCellRenderer = activeCellRenderer;
	}

	/**
	 * Injects the Window utilities
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * Injects the alphabetical store sorter.
	 * @param storeSorter the storeSorter to set
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * Injects the Portal Users service error message resover.
	 * @param emr the emr to set
	 */
	@Inject
	public void setEmr(PortalUsersServiceErrorMessageResolver emr) {
		this.emr = emr;
	}

	/**
	 * Injects the renderer for cell <b>ID</b>.
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
}
