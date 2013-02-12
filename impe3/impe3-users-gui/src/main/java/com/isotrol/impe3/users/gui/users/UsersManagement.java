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

package com.isotrol.impe3.users.gui.users;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.isotrol.impe3.pms.gui.common.util.Buttons;
import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.pms.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.pms.gui.common.util.Styles;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserSelDTO;
import com.isotrol.impe3.users.gui.data.PortalUserSelModelData;
import com.isotrol.impe3.users.gui.ioc.UsersAppFactory;
import com.isotrol.impe3.users.gui.service.IPortalUsersServiceAsync;
import com.isotrol.impe3.users.gui.util.UsersStyles;
import com.isotrol.impe3.users.gui.util.UsersMessages;

/**
 * Users Master view.
 * 
 * @author Manuel Ruiz
 * 
 */
public class UsersManagement extends ContentPanel {

	/**
	 * Tooltip bound to "Refresh" button.<br/>
	 */
	private static final String TOOLTIP_REFRESH = "Refrescar listado";

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
	 * Header for "name" column.<br/>
	 */
	private static final String COLUMN_NAME_HEADER = "Nombre de usuario";
	/**
	 * Header for "display" column.<br/>
	 */
	private static final String COLUMN_DISPLAY_NAME_HEADER = "Nombre a mostrar";
	/**
	 * Header for "active" column.<br/>
	 */
	private static final String COLUMN_ACTIVE_HEADER = "Activo";

	/**
	 * Users grid instance.<br/>
	 */
	private Grid<PortalUserSelModelData> grid = null;
	/**
	 * Store for {@link #grid}.<br/>
	 */
	private ListStore<PortalUserSelModelData> store = null;
	/** user edit button */
	private TextToolItem ttiEdit = null;
	/** user delete button */
	private TextToolItem ttiDelete = null;
	/** user change password button */
	private TextToolItem ttiChangePwd = null;
	/**
	 * Filter bound to users grid.<br/>
	 */
	private CustomizableStoreFilter<PortalUserSelModelData> filter = null;

	/*
	 * Injected deps
	 */
	/**
	 * Reference to users remote service proxy.<br/>
	 */
	private IPortalUsersServiceAsync service = null;
	
	/**
	 * Reference to common messages service.<br/>
	 */
	private Messages commonMessagesBundle = null;
	
	/**
	 * Reference to users messages service.<br/>
	 */
	private UsersMessages usersMessagesBundle = null;
	
	/**
	 * Common stylesBundle service.<br/>
	 */
	private Styles stylesBundle = null;
	
	/**
	 * Users app specific styles bundle.<br/>
	 */
	private UsersStyles usersStylesBundle = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsHelper = null;
	
	/**
	 * Constructor for the editions management panel.<br/>
	 */
	public UsersManagement() {}
	
	/**
	 * Inits the widget. Must be called after the dependencies injection.
	 */
	public UsersManagement init() {

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
		SelectionListener<ToolBarEvent> lNew = new SelectionListener<ToolBarEvent>() {
			@Override
			public void componentSelected(ToolBarEvent tbe) {
				showDetailsPanel(null);
			}
		};
		buttonsHelper.addGenericButton(
				commonMessagesBundle.labelAdd(), 
				stylesBundle.iconAddUser(), 
				toolBar, 
				lNew);
		buttonsHelper.addSeparator(toolBar);

		// "delete" button
		SelectionListener<ToolBarEvent> lDelete = new SelectionListener<ToolBarEvent>() {
			@Override
			public void componentSelected(ToolBarEvent tbe) {
				final PortalUserSelModelData selected = grid
						.getSelectionModel().getSelectedItem();
				if (selected != null) {
					Listener<WindowEvent> listener = new Listener<WindowEvent>() {
						public void handleEvent(WindowEvent be) {
							if (be.buttonClicked.getItemId().equals(Dialog.YES)) { // confirmed
								tryRemoveUser(selected);
							}
						}
					};
					MessageBox.confirm(
								"Confirmar eliminación de Usuario",
								"¿Seguro que desea eliminar el Usuario seleccionado?",
								listener).setModal(true);
				}
			}
		};
		ttiDelete = buttonsHelper.addGenericButton(
				commonMessagesBundle.labelDelete(),
				stylesBundle.iconDeleteUser(),
				toolBar,
				lDelete);
		buttonsHelper.addSeparator(toolBar);

		// "edit" button
		SelectionListener<ToolBarEvent> lEdit = new SelectionListener<ToolBarEvent>() {
			@Override
			public void componentSelected(ToolBarEvent ce) {
				PortalUserSelModelData selected = grid.getSelectionModel()
						.getSelectedItem();
				if (selected != null) {
					tryRetrieveAndDisplayDetails(selected);
				}
			}
		};
		ttiEdit = buttonsHelper.addEditButton(toolBar, lEdit);
		buttonsHelper.addSeparator(toolBar);

		// change password button
		SelectionListener<ToolBarEvent> lChangePwd = new SelectionListener<ToolBarEvent>() {
			@Override
			public void componentSelected(ToolBarEvent ce) {
				PortalUserSelModelData selected = grid.getSelectionModel()
						.getSelectedItem();
				if (selected != null) {
					openChangePwdWindow(selected);
				}
			}
		};
		ttiChangePwd = buttonsHelper.addGenericButton(
				"Cambiar contraseña", 
				usersStylesBundle.iconChangePassword(), 
				toolBar, 
				lChangePwd);
		toolBar.add(ttiChangePwd);

		toolBar.add(new FillToolItem());

		filter = new CustomizableStoreFilter<PortalUserSelModelData>(Arrays
				.asList(new String[] {
							PortalUserSelModelData.PROPERTY_NAME, 
							PortalUserSelModelData.PROPERTY_DISPLAY_NAME
						}));
		filter.bind(store);
		filter.setHideLabel(false);
		toolBar.add(new AdapterToolItem(filter));

		IconButton bRefresh = buttonsHelper.addRefreshButton(toolBar,
				new SelectionListener<IconButtonEvent>() {
					@Override
					public void componentSelected(IconButtonEvent ce) {
						tryGetUsers();
					}
				});
		bRefresh.setToolTip(new ToolTipConfig(TOOLTIP_REFRESH));

		// disable remove and edit buttons
		enableButtons(false);

		setTopComponent(toolBar);
	}

	/**
	 * Requests the service for user details. On success displays them on the
	 * detail panel.<br/>
	 * 
	 * @param portalUser
	 */
	private void tryRetrieveAndDisplayDetails(PortalUserSelModelData portalUser) {

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(
					commonMessagesBundle.headerErrorWindow(),
					usersMessagesBundle.msgErrorGettingUserData(),
					null).setModal(true);
			}

			public void onSuccess(PortalUserDTO user) {
				showDetailsPanel(user);
			}
		};

		service.getById(portalUser.getDTO().getId(), callback);

	}

	/**
	 * Creates and shows a details panel for the passed user DTO.<br/>
	 * 
	 * @param user
	 */
	private void showDetailsPanel(PortalUserDTO user) {
		UserEditorPanel wDetails = UsersAppFactory.getInstance().getUserEditorPanel();
		wDetails.init(user);
		
		Listener<BaseEvent> lChange = new Listener<BaseEvent>() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(BaseEvent be) {
				tryGetUsers();
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
	private void tryRemoveUser(PortalUserSelModelData userModel) {

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(
					commonMessagesBundle.headerErrorWindow(),
					usersMessagesBundle.msgErrorGettingUserData(),
					null).setModal(true);
			}

			public void onSuccess(Void arg) {
				MessageBox.info("Usuarios", "Usuario eliminado correctamente",
						null);
				tryGetUsers();
			}
		};

		service.delete(userModel.getDTO().getId(), callback);
	}

	/**
	 * Creates and shows the "change password" window for the passed user model
	 * data.<br/>
	 * 
	 * @param portalUser
	 */
	private void openChangePwdWindow(PortalUserSelModelData portalUser) {
		ChangePwdWindow pwdWin = UsersAppFactory.getInstance().getChangePwdWindow()
			.init(portalUser.getDTO());
		pwdWin.show();
	}

	/**
	 * Enables or disables the buttons that need a selected user on grid,
	 * according to the passed param value.<br/>
	 * 
	 * @param enabled
	 *            if <code>true</code> enables, otherwise disables.
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

		// renderer for active column
		GridCellRenderer<PortalUserSelModelData> activeRenderer = new GridCellRenderer<PortalUserSelModelData>() {
			public String render(PortalUserSelModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<PortalUserSelModelData> s) {
				String checked = "";
				if (model.getDTO().isActive()) {
					checked = "checked='checked'";
				}
				return "<input type='checkbox' disabled='disabled' " + checked + " />";
			}
		};

		List<ColumnConfig> configs = new LinkedList<ColumnConfig>();

		ColumnConfig ccName = new ColumnConfig();
		ccName.setId(PortalUserSelModelData.PROPERTY_NAME);
		ccName.setWidth(COLUMN_NAME_WIDTH);
		ccName.setHeader(COLUMN_NAME_HEADER);
		configs.add(ccName);

		ColumnConfig ccDisplay = new ColumnConfig();
		ccDisplay.setId(PortalUserSelModelData.PROPERTY_DISPLAY_NAME);
		ccDisplay.setWidth(COLUMN_DISPLAY_NAME_WIDTH);
		ccDisplay.setHeader(COLUMN_DISPLAY_NAME_HEADER);
		configs.add(ccDisplay);

		ColumnConfig ccActive = new ColumnConfig();
		ccActive.setId(PortalUserSelModelData.PROPERTY_ACTIVE);
		ccActive.setWidth(COLUMN_ACTIVE_WIDTH);
		ccActive.setHeader(COLUMN_ACTIVE_HEADER);
		ccActive.setRenderer(activeRenderer);
		ccActive.setAlignment(HorizontalAlignment.CENTER);
		configs.add(ccActive);

		ColumnModel cm = new ColumnModel(configs);

		store = new ListStore<PortalUserSelModelData>();

		grid = new Grid<PortalUserSelModelData>(store, cm);
		grid.setAutoExpandColumn(PortalUserSelModelData.PROPERTY_NAME);
		grid
				.setSelectionModel(new GridSelectionModel<PortalUserSelModelData>());
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		grid.addListener(Events.RowClick, new Listener<GridEvent>() {
			public void handleEvent(GridEvent ge) {
				if (grid.getSelectionModel().getSelectedItem() != null) {
					enableButtons(true);
				}
			}
		});
		grid.getView().setForceFit(true);

		add(grid);
	}

	/**
	 * Requests the service for registered users.<br/>
	 * On success, adds entries to the grid.
	 */
	private void tryGetUsers() {

		AsyncCallback<List<PortalUserSelDTO>> callback = new AsyncCallback<List<PortalUserSelDTO>>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(
					commonMessagesBundle.headerErrorWindow(),
					"No se pudieron obtener los usuarios.", 
					null).setModal(true);
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
			}
		};

		service.getUsers(callback);
	}

	/**
	 * Injects the users service proxy.
	 * @param service
	 */
	@Inject
	public void setService(IPortalUsersServiceAsync service) {
		this.service = service;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param commonMessagesBundle
	 */
	@Inject
	public void setCommonMessagesService(Messages commonMessagesService) {
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
	public void setStyles(Styles styles) {
		this.stylesBundle = styles;
	}

	/**
	 * Injects the users app specific styles bundle.
	 * @param usersStylesBundle
	 */
	@Inject
	public void setUsersStylesBundle(UsersStyles usersStylesBundle) {
		this.usersStylesBundle = usersStylesBundle;
	}
	
	/**
	 * Injects the buttons helper
	 * @param buttonsHelper
	 */
	@Inject
	public void setButtonsHelper(Buttons buttonsHelper) {
		this.buttonsHelper = buttonsHelper;
	}
}
