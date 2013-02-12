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
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.BooleanCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.api.user.UserTemplateDTO;
import com.isotrol.impe3.pms.api.user.UsersService;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.UsersController;
import com.isotrol.impe3.pms.gui.client.data.impl.UserSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Implements the PMS tool users management widget.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class UsersManagement extends ContentPanel implements IInitializableWidget {

	/**
	 * Gives support to {@link IInitializableWidget}.<br/>
	 */
	private boolean initialized = false;

	/**
	 * Global roles map.<br/>
	 */
	private Map<GlobalRole, String> globalRoles = null;

	/**
	 * Global authorities map.<br/>
	 */
	private Map<GlobalAuthority, String> globalAuthorities = null;

	/**
	 * Portal authorities map.<br/>
	 */
	private Map<PortalAuthority, String> portalAuthorities = null;

	/**
	 * Width in pixels for <b>username</b> column.<br/>
	 */
	private static final int COLUMN_USERNAME_WIDTH = 150;

	/**
	 * Width in pixels for <b>display name</b> column.<br/>
	 */
	private static final int COLUMN_DISPLAY_NAME_WIDTH = 200;

	/**
	 * Width in pixels for <b>booleans</b> column.<br/>
	 */
	private static final int COLUMN_BOOLEAN_WIDTH = 50;

	/**
	 * the users grid.<br/>
	 */
	private Grid<UserSelModelData> grid = null;

	/**
	 * ToolItem "Edit user".<br/>
	 */
	private Button ttiEdit = null;

	/**
	 * ToolItem "Change password".<br/>
	 */
	private Button ttiChangePwd = null;

	/**
	 * ToolItem "Effective rights".<br/>
	 */
	private Button ttiEffectiveAuthorities = null;

	/**
	 * ToolItem "Portal Authorities".<br/>
	 */
	private Button ttiPortalAuthorities = null;

	/**
	 * ToolItem "Delete user".<br/>
	 */
	private Button ttiDelete = null;

	/**
	 * Callback code fired when a portal has been selected.<br/> Basically shows the selected Portal Authorities window
	 * for the currently selected User in the grid.
	 */
	private PortalSelector.IPortalSelectionListener portalSelectionListener = new PortalSelector.IPortalSelectionListener() {
		public void portalSelected(PortalSelDTO portalSelDto) {
			UserSelDTO userSelDto = grid.getSelectionModel().getSelectedItem().getDTO();
			showPortalAuthoritiesWindow(userSelDto, portalSelDto);
		}
	};

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStylesBundle = null;

	/**
	 * Error message resolver used for the sessions service.<br/>
	 */
	private SimpleErrorMessageResolver emrSimple = null;

	/**
	 * Error message resolver for the users service.<br/>
	 */
	private UsersServiceErrorMessageResolver emrUsersService = null;

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Users service proxy.<br/>
	 */
	private IUsersServiceAsync usersService = null;

	/**
	 * Sessions service proxy.<br/>
	 */
	private ISessionsServiceAsync sessionsService = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Cell renderer for "Admin" and "Active" properties.<br/>
	 */
	private BooleanCellRenderer booleanCellRenderer = null;

	/**
	 * GuiCommon styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Util support object.<br/>
	 */
	private Util util = null;

	/**
	 * The gear cell renderer.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;

	/**
	 * Pms Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Default and sole constructor.
	 */
	public UsersManagement() {
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init(java.lang.Object)
	 */
	public Widget init() {
		initialized = true;

		tryGetGlobalRoles();
		initThis();

		tryGetGlobalAuthorities();
		addGrid();

		tryGetPortalAuthorities();
		addToolbar();
		addBottomToolbar();

		initController();

		tryGetUsers();

		return this;
	}

	/**
	 * Inits the users service controller.<br/>
	 */
	private void initController() {
		UsersController controller = (UsersController) usersService;
		ChangeListener listener = new ChangeListener() {
			public void modelChanged(ChangeEvent changeEvent) {
				PmsChangeEvent event = (PmsChangeEvent) changeEvent;
				switch (event.getType()) {
					case PmsChangeEvent.ADD:
						tryGetUsers();
					case PmsChangeEvent.UPDATE:
						tryGetUsers();
						break;
					case PmsChangeEvent.DELETE:
						removeFromGrid((String) event.getEventInfo());
						break;
					default: // nothing to do - unexpected event type
				}
			}
		};
		controller.addChangeListener(listener);
	}

	/**
	 * Inserts a new row in the grid, with the passed User data.<br/>
	 * @param dto
	 */
	@SuppressWarnings("unused")
	private void addToGrid(UserDTO dto) {
		grid.getStore().add(new UserSelModelData(dto));
	}

	/**
	 * Removes from grid the row identified by its User ID.<br/>
	 * @param id
	 */
	private void removeFromGrid(String id) {
		ListStore<UserSelModelData> store = grid.getStore();
		UserSelModelData deleted = store.findModel(UserSelModelData.PROPERTY_ID, id);
		if (deleted != null) {
			store.remove(deleted);
		}
	}

	/**
	 * Inits the widget properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setHeaderVisible(false);
	}

	/**
	 * Retrieves from service the selected User data, and displays it in an edition window.<br/>
	 */
	private void getSelectedAndShowDetails() {
		UserSelModelData userModel = grid.getSelectionModel().getSelectedItem();
		if (userModel != null) {
			tryGetUser(userModel.getDTO().getId());
		}
	}

	/**
	 * Adds the users grid.<br/>
	 */
	private void addGrid() {

		List<ColumnConfig> columns = new LinkedList<ColumnConfig>();

		ColumnConfig ccId = new ColumnConfig(UserSelModelData.PROPERTY_ID, messages.columnHeaderId(),
			Constants.COLUMN_ICON_WIDTH);
		ccId.setRenderer(idCellRenderer);
		columns.add(ccId);

		ColumnConfig ccName = new ColumnConfig(UserSelModelData.PROPERTY_USERNAME, messages.columnHeaderUsername(),
			COLUMN_USERNAME_WIDTH);
		columns.add(ccName);

		ColumnConfig ccDisplayName = new ColumnConfig(UserSelModelData.PROPERTY_DISPLAY_NAME, messages
			.columnHeaderDisplayName(), COLUMN_DISPLAY_NAME_WIDTH);
		columns.add(ccDisplayName);

		ColumnConfig ccAdmin = new ColumnConfig(UserSelModelData.PROPERTY_ADMIN, pmsMessages.columnHeaderAdmin(),
			COLUMN_BOOLEAN_WIDTH);
		ccAdmin.setRenderer(booleanCellRenderer);
		columns.add(ccAdmin);

		ColumnConfig ccActive = new ColumnConfig(UserSelModelData.PROPERTY_ACTIVE, messages.columnHeaderActive(),
			COLUMN_BOOLEAN_WIDTH);
		ccActive.setRenderer(booleanCellRenderer);
		columns.add(ccActive);

		ColumnConfig ccLock = new ColumnConfig(UserSelModelData.PROPERTY_LOCKED, pmsMessages.columnHeaderLocked(),
			COLUMN_BOOLEAN_WIDTH);
		ccLock.setRenderer(booleanCellRenderer);
		columns.add(ccLock);

		ColumnModel cm = new ColumnModel(columns);
		ListStore<UserSelModelData> store = new ListStore<UserSelModelData>();
		grid = new Grid<UserSelModelData>(store, cm);

		GridView gridView = grid.getView();
		gridView.setAutoFill(true);
		gridView.setForceFit(true);

		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<UserSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<UserSelModelData> se) {
				enableDisableToolItems();
			}
		});

		// edit on row double click:
		grid.addListener(Events.CellDoubleClick, new Listener<GridEvent<UserSelModelData>>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			/**
			 * <br/>
			 */
			public void handleEvent(GridEvent<UserSelModelData> be) {
				getSelectedAndShowDetails();
			}
		});

		add(grid);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 * @param container
	 */
	private void addToolbar() {
		ToolBar toolBar = new ToolBar();

		// Add user
		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				showDetailsPanel(new UserTemplateDTO(globalRoles, globalAuthorities));
			}
		};
		buttonsSupport.addGenericButton(messages.labelAdd(), styles.iAddUser(), toolBar, lAdd);
		buttonsSupport.addSeparator(toolBar);

		// Edit user
		ttiEdit = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				getSelectedAndShowDetails();
			}
		});
		buttonsSupport.addSeparator(toolBar);

		// Change password
		SelectionListener<ButtonEvent> lChangePassword = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				UserSelModelData userModel = grid.getSelectionModel().getSelectedItem();
				if (userModel != null) {
					PmsFactory.getInstance().getChangePwdWindow().init(userModel.getDTO()).show();
				}
			}
		};
		ttiChangePwd = buttonsSupport.addGenericButton(messages.labelChangePassword(), styles.iChangePassword(),
			toolBar, lChangePassword);
		buttonsSupport.addSeparator(toolBar);

		// Effective Authorities
		SelectionListener<ButtonEvent> lEffectiveRights = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				UserSelModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					PmsFactory.getInstance().getGrantedAuthoritiesWindow().init(selected.getDTO(), globalAuthorities)
						.show();
				}
			}
		};
		ttiEffectiveAuthorities = buttonsSupport.addGenericButton(pmsMessages.labelEffectiveAuthorities(),
			pmsStylesBundle.iconEffectiveAuthorities(), toolBar, lEffectiveRights);
		buttonsSupport.addSeparator(toolBar);

		// Portal authorities
		SelectionListener<ButtonEvent> lPortalAuthorities = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				showPortalSelector();
			}
		};
		ttiPortalAuthorities = buttonsSupport.addGenericButton(pmsMessages.labelPortalAuthorities(), pmsStylesBundle
			.iconPortalAuthorities(), toolBar, lPortalAuthorities);
		buttonsSupport.addSeparator(toolBar);

		// Delete user
		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				final UserSelModelData selected = grid.getSelectionModel().getSelectedItem();
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES) && selected != null) {
							tryDeleteUser(selected.getDTO().getId());
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(),
					messages.msgConfirmDeleteUser(selected.getDTO().getDisplayName()), listener).setModal(true);
			}
		};
		ttiDelete = buttonsSupport.addGenericButton(messages.labelDelete(), styles.iDeleteUser(), toolBar, lDelete);
		buttonsSupport.addSeparator(toolBar);

		toolBar.add(new FillToolItem());

		// a "Refresh" button
		buttonsSupport.addRefreshButton(toolBar, new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetUsers();
			}
		});

		enableDisableToolItems();

		setTopComponent(toolBar);
	}

	/**
	 * Creates, configures and adds the bottom toolbar.<br/>
	 * @param container
	 */
	private void addBottomToolbar() {
		ToolBar bottomToolBar = new ToolBar();

		bottomToolBar.add(new FillToolItem());

		// "Help" button:
		buttonsSupport.addHelpButton(bottomToolBar, settings.pmsUsersAdminPortalManualUrl());

		setBottomComponent(bottomToolBar);
	}

	/**
	 * Shows the portal selector
	 */
	private void showPortalSelector() {
		PortalSelector portalSelector = PmsFactory.getInstance().getPortalSelector();
		portalSelector.setPortalSelectionListener(portalSelectionListener);
		portalSelector.init().show();
	}

	/**
	 * Shows the portal authorities window.
	 */
	private void showPortalAuthoritiesWindow(UserSelDTO userSelDto, PortalSelDTO portalSelDto) {
		PortalAuthoritiesWindow wAuthorities = PmsFactory.getInstance().getPortalAuthoritiesWindow();
		wAuthorities.setUserSelDto(userSelDto);
		wAuthorities.setPortalSelDto(portalSelDto);
		wAuthorities.setAuthoritiesNames(portalAuthorities);
		wAuthorities.init().show();
	}

	/**
	 * Tries to remove the passed user by calling remote operation {@link UsersService#delete(String)}.<br/>
	 * @param id user ID.
	 */
	private void tryDeleteUser(String id) {
		util.mask(messages.mskDeleteUser());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onSuccess(Void arg0) {
				util.unmask();
				util.info(messages.msgOkDeleteUser());
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrUsersService, messages.msgErrorDeleteUser());
			}
		};

		usersService.delete(id, callback);
	}

	/**
	 * Retrieves the user data from service.<br/> On success, constructs the corresponding template DTO and disaplays a
	 * user details panel with the created DTO.<br/> On failure, displays an error window.
	 * @param id the user ID.
	 */
	private void tryGetUser(String id) {
		util.mask(messages.mskUser());

		AsyncCallback<UserDTO> callback = new AsyncCallback<UserDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrUsersService, messages.msgErrorDeleteUser());
			}

			public void onSuccess(UserDTO arg0) {
				UserTemplateDTO templateDto = arg0.toTemplate(globalRoles, globalAuthorities);
				showDetailsPanel(templateDto);
				util.unmask();
			}
		};

		usersService.get(id, callback);
	}

	/**
	 * Displays the details panel for the passed template DTO.<br/>
	 * @param templateDto
	 */
	private void showDetailsPanel(UserTemplateDTO templateDto) {
		UserDetails detail = PmsFactory.getInstance().getUserDetailsPanel();
		detail.setUsertemplate(templateDto);
		detail.show();
	}

	/**
	 * If no grid item is selected, disables the context sensitive items. Otherwise enables the context sensitive
	 * items.<br/> The affected items are: Edit, Change Pwd, Effective Rights, Delete.
	 */
	private void enableDisableToolItems() {
		boolean enabled = false;
		if (!grid.getSelectionModel().getSelectedItems().isEmpty()) {
			enabled = true;
		}

		ttiEdit.setEnabled(enabled);
		ttiChangePwd.setEnabled(enabled);
		ttiEffectiveAuthorities.setEnabled(enabled);
		ttiPortalAuthorities.setEnabled(enabled);
		ttiDelete.setEnabled(enabled);
	}

	/**
	 * Retrieves the users from service.<br/>
	 */
	private void tryGetUsers() {

		util.mask(messages.mskUsers());

		AsyncCallback<List<UserSelDTO>> callback = new AsyncCallback<List<UserSelDTO>>() {
			public void onSuccess(List<UserSelDTO> arg0) {
				populateGrid(arg0);
				util.unmask();
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrUsersService, pmsMessages.msgErrorGetUsers());
			}
		};

		usersService.getUsers(callback);
	}

	/**
	 * Retrieves the global roles from the sessions service.<br/>
	 */
	private void tryGetGlobalRoles() {
		AsyncCallback<Map<GlobalRole, String>> callback = new AsyncCallback<Map<GlobalRole, String>>() {
			public void onSuccess(Map<GlobalRole, String> arg0) {
				globalRoles = arg0;
			}

			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrSimple, pmsMessages.msgErrorGetGlobalRoles());
			}
		};

		sessionsService.getGlobalRoles(callback);
	}

	/**
	 * Retrieves the global authorities from the sessions service.<br/>
	 */
	private void tryGetGlobalAuthorities() {
		AsyncCallback<Map<GlobalAuthority, String>> callback = new AsyncCallback<Map<GlobalAuthority, String>>() {
			public void onSuccess(Map<GlobalAuthority, String> arg0) {
				globalAuthorities = arg0;
			}

			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrSimple, pmsMessages.msgErrorGetGlobalAuthorities());
			}
		};

		sessionsService.getGlobalAuthorities(callback);
	}

	/**
	 * Retrieves the portal authorities from the sessions service.<br/>
	 */
	private void tryGetPortalAuthorities() {
		AsyncCallback<Map<PortalAuthority, String>> callback = new AsyncCallback<Map<PortalAuthority, String>>() {
			public void onSuccess(Map<PortalAuthority, String> arg0) {
				portalAuthorities = arg0;
			}

			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrSimple, pmsMessages.msgErrorGetPortalAuthorities());
			}
		};
		sessionsService.getPortalAuthorities(callback);
	}

	/**
	 * Populates the grid with the passed users list.<br/>
	 * @param users the users list.
	 */
	private void populateGrid(List<UserSelDTO> users) {
		List<UserSelModelData> models = new LinkedList<UserSelModelData>();
		for (UserSelDTO dto : users) {
			models.add(new UserSelModelData(dto));
		}

		ListStore<UserSelModelData> store = grid.getStore();
		store.removeAll();
		store.add(models);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * Injects the PMS specific messages bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the generic messages service.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the users async service proxy.<br/>
	 * @param usersService
	 */
	@Inject
	public void setUsersService(IUsersServiceAsync usersService) {
		this.usersService = usersService;
	}

	/**
	 * Injects the button helper.<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the cell renderer for boolean values.
	 * @param booleanCellRenderer the booleanCellRenderer to set
	 */
	@Inject
	public void setBooleanCellRenderer(BooleanCellRenderer booleanCellRenderer) {
		this.booleanCellRenderer = booleanCellRenderer;
	}

	/**
	 * Injects the GuiCommon module styles bundle.<br/>
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the error message resolver for the sessions service.<br/>
	 * @param emrSimple
	 */
	@Inject
	public void setEmrSimple(SimpleErrorMessageResolver emrSimple) {
		this.emrSimple = emrSimple;
	}

	/**
	 * Injects the error message resolver for the users service.<br/>
	 * @param emrUsersService
	 */
	@Inject
	public void setEmrUsersService(UsersServiceErrorMessageResolver emrUsersService) {
		this.emrUsersService = emrUsersService;
	}

	/**
	 * <br/>
	 * @param sessionsService
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}

	/**
	 * @param utilities the util to set
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the gear cell rederer.
	 * @param cellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setGearCellRenderer(InformationCellRenderer cellRenderer) {
		this.idCellRenderer = cellRenderer;
	}

	/**
	 * Injects the PMS specific styles bundle.
	 * @param pmsStylesBundle the pmsStylesBundle to set
	 */
	@Inject
	public void setPmsStylesBundle(PmsStyles pmsStylesBundle) {
		this.pmsStylesBundle = pmsStylesBundle;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
