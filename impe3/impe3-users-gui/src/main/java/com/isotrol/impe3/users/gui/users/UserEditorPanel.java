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


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.ToolBarEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.isotrol.impe3.pms.gui.common.util.Buttons;
import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.gui.data.UserPropertiesModel;
import com.isotrol.impe3.users.gui.service.IPortalUsersServiceAsync;
import com.isotrol.impe3.users.gui.util.UsersMessages;


/**
 * Popup window that manages the detailed information of a User.<br/>
 * 
 * <dl> <dt><b>Events:</b></dt>
 * 
 * <dd><b>Change</b> : BaseEvent(UserEditorPanel)<br> <div>Fires after an existing user information has been succesfully
 * updated on server.</div> <ul> <li>UserEditorPanel : this</li> </ul> </dd>
 * 
 * <dd><b>Add</b> : BaseEvent(UserEditorPanel)<br> <div>Fires after a new user information has been succesfully saved on
 * server.</div> <ul> <li>UserEditorPanel : this</li> </ul> </dd>
 * 
 * </dl>
 * 
 * @author Manuel Ruiz
 * 
 */
public class UserEditorPanel extends Window {

	/**
	 * Constant "Clave".<br/>
	 */
	private static final String DEFAULT_KEY = "Clave";

	/**
	 * Constant "Valor"<br/>
	 */
	private static final String DEFAULT_VALUE = "Valor";

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * Preferred width for "key" column.<br/>
	 */
	private static final int COLUMN_KEY_WIDTH = 100;
	/**
	 * Preferred width for "value" column.<br/>
	 */
	private static final int COLUMN_VALUE_WIDTH = 200;

	/*
	 * fields
	 */
	/**
	 * "name" field.<br/>
	 */
	private TextField<String> tfName = null;
	/**
	 * "display name" field.<br/>
	 */
	private TextField<String> tfDisplayName = null;
	/**
	 * "email" field.<br/>
	 */
	private TextField<String> tfEmail = null;
	/**
	 * "password" field.<br/>
	 */
	private TextField<String> tfPwd = null;
	/**
	 * "repeat password" field.<br/>
	 */
	private TextField<String> tfRPwd = null;
	/**
	 * "active" combo.<br/>
	 */
	private CheckBox cbActivate = null;

	/** user properties */
	private Map<String, String> userProperties = new HashMap<String, String>();
	/** user properties grid */
	private EditorGrid<UserPropertiesModel> gProp = null;
	/** user properties content panel */
	private ContentPanel cpProperties = null;

	/** current user DTO. */
	private PortalUserDTO dto = null;

	/*
	 * Injected deps.
	 */
	/** portal users service */
	private IPortalUsersServiceAsync service = null;

	/**
	 * Common module messages service.<br/>
	 */
	private Messages messagesService = null;

	/**
	 * Users messages service reference.<br/>
	 */
	private UsersMessages usersMessagesService = null;
	
	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Constructor provided with bound data.<br/>
	 * 
	 * @param rdDto
	 */
	public UserEditorPanel() {}
	
	/**
	 * Inits the widget. Must be explicitly called after properties injection
	 * @param dto
	 */
	public void init(PortalUserDTO dto) {
		this.dto = dto;

		configThis();
		initComponents();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {

		addFields();

		if (dto != null) {
			displayBoundDataValues();
		}

		addButtonBar();
	}

	/**
	 * Configures this panel properties.<br/>
	 */
	private void configThis() {
		setLayout(new FitLayout());
		setClosable(false);
		setCloseAction(CloseAction.CLOSE);
		setShadow(false);
		setSize(600, 500);
		setButtonAlign(HorizontalAlignment.LEFT);

		container = new LayoutContainer(new FormLayout());
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Adds the fields to the container.<br/>
	 */
	private void addFields() {
		// name
		tfName = new TextField<String>();
		tfName.setFieldLabel(usersMessagesService.labelUserNameField());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		container.add(tfName);

		// display name
		tfDisplayName = new TextField<String>();
		tfDisplayName.setFieldLabel(usersMessagesService.labelDisplayNameField());
		tfDisplayName.setAllowBlank(false);
		container.add(tfDisplayName);

		if (dto == null) {
			// password
			tfPwd = new TextField<String>();
			tfPwd.setFieldLabel(usersMessagesService.labelPasswordField());
			tfPwd.setAllowBlank(false);
			tfPwd.setAutoValidate(true);
			tfPwd.setPassword(true);
			container.add(tfPwd);

			// repeat pasword
			tfRPwd = new TextField<String>();
			tfRPwd.setFieldLabel(usersMessagesService.labelRepeatPasswordField());
			tfRPwd.setAllowBlank(false);
			tfRPwd.setAutoValidate(true);
			tfRPwd.setPassword(true);
			container.add(tfRPwd);
		}

		// email
		tfEmail = new TextField<String>();
		tfEmail.setFieldLabel(usersMessagesService.labelEmailField());
		container.add(tfEmail);

		cbActivate = new CheckBox();
		cbActivate.setFieldLabel(usersMessagesService.labelActivatedField());
		container.add(cbActivate);

		addPropertiesGrid();
	}

	/**
	 * Create and add a grid with to edit the user properties
	 */
	private void addPropertiesGrid() {

		cpProperties = new ContentPanel();
		cpProperties.setHeight("250");
		cpProperties.setHeading(messagesService.headerPropertiesPanel());
		cpProperties.setLayout(new FitLayout());

		addToolBar();

		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		// add a checkbox column to select properties
		CheckBoxSelectionModel<UserPropertiesModel> smProp = new CheckBoxSelectionModel<UserPropertiesModel>();
		cc.add(smProp.getColumn());

		ColumnConfig ccKey = new ColumnConfig(UserPropertiesModel.PROPERTY_KEY, messagesService.columnHeaderKey(),
			COLUMN_KEY_WIDTH);
		TextField<String> tfKeyEditor = new TextField<String>();
		tfKeyEditor.setAllowBlank(false);
		tfKeyEditor.setAutoValidate(false);
		ccKey.setEditor(new CellEditor(tfKeyEditor));
		cc.add(ccKey);

		ColumnConfig ccValue = new ColumnConfig(UserPropertiesModel.PROPERTY_VALUE,
			messagesService.columnHeaderValue(), COLUMN_VALUE_WIDTH);
		TextField<String> tfVAlueEditor = new TextField<String>();
		tfVAlueEditor.setAllowBlank(false);
		tfVAlueEditor.setAutoValidate(false);
		ccValue.setEditor(new CellEditor(tfVAlueEditor));
		cc.add(ccValue);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<UserPropertiesModel> storeProp = new ListStore<UserPropertiesModel>();

		gProp = new EditorGrid<UserPropertiesModel>(storeProp, cmOwn);
		gProp.setAutoExpandColumn(UserPropertiesModel.PROPERTY_VALUE);
		gProp.setSelectionModel(smProp);
		gProp.addPlugin(smProp);
		GridView gView = gProp.getView();
		gView.setForceFit(true);

		cpProperties.add(gProp);
		container.add(cpProperties);
	}

	/**
	 * add a toolbar on top of properties grid
	 */
	private void addToolBar() {
		ToolBar toolbar = new ToolBar();

		SelectionListener<ToolBarEvent> lAdd = new SelectionListener<ToolBarEvent>() {
			public void componentSelected(ToolBarEvent ce) {
				UserPropertiesModel newProperty = new UserPropertiesModel(DEFAULT_KEY, DEFAULT_VALUE);
				gProp.getStore().add(newProperty);
			}
		};
		buttonsSupport.addAddButton(toolbar, lAdd);
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ToolBarEvent> lDelete = new SelectionListener<ToolBarEvent>() {
			@Override
			public void componentSelected(ToolBarEvent ce) {
				List<UserPropertiesModel> selected = gProp.getSelectionModel().getSelectedItems();
				ListStore<UserPropertiesModel> store = gProp.getStore();
				for (UserPropertiesModel model : selected) {
					store.remove(model);
				}
			}
		};
		buttonsSupport.addDeleteButton(toolbar, lDelete);
		toolbar.add(new SeparatorToolItem());

		cpProperties.setTopComponent(toolbar);
	}

	/**
	 * Creates, configures and adds the button bar.<br/>
	 */
	private void addButtonBar() {
		// buttons bar
		buttonBar = new ButtonBar();

		Button bAccept = new Button(messagesService.labelAccept());
		bAccept.addSelectionListener(new SelectionListener<ComponentEvent>() {

			@Override
			public void componentSelected(ComponentEvent ce) {
				if (dto == null) {
					if (checkEqualPwds()) {
						tryCreateUser();
					} else {
						MessageBox.alert(messagesService.headerErrorWindow(),
							usersMessagesService.msgPasswordsDoNotMatch(), null).setModal(true);
					}
				} else {
					tryUpdateUser();
				}
			}

			private boolean checkEqualPwds() {

				boolean equals = false;
				if (tfPwd.getValue() != null && tfPwd.getValue().equals(tfRPwd.getValue())) {
					equals = true;
				}

				return equals;
			}
		});
		buttonBar.add(bAccept);

		Button bCancel = new Button(messagesService.labelCancel());
		bCancel.addSelectionListener(new SelectionListener<ComponentEvent>() {

			@Override
			public void componentSelected(ComponentEvent ce) {
				close();
			}

		});
		buttonBar.add(bCancel);

		setButtonBar(buttonBar);
	}

	/**
	 * Displays the bound DTO values.<br/>
	 */
	private void displayBoundDataValues() {

		tfName.setValue(dto.getUsername());
		tfDisplayName.setValue(dto.getDisplayName());
		tfEmail.setValue(dto.getEmail());
		cbActivate.setValue(Boolean.valueOf(dto.isActive()));

		userProperties = dto.getProperties();
		List<UserPropertiesModel> propList = new LinkedList<UserPropertiesModel>();

		Iterator<Entry<String, String>> it = userProperties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> e = it.next();
			propList.add(new UserPropertiesModel((String) e.getKey(), (String) e.getValue()));
		}
		gProp.getStore().add(propList);
	}

	/**
	 * Inserts GUI values into a new DTO and tries to save it using RPC.<br/>
	 */
	private void tryCreateUser() {

		PortalUserDTO user = getUserValues();

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox
					.alert(messagesService.headerErrorWindow(), usersMessagesService.msgErrorCreatingUser(), null)
					.setModal(true);
			}

			public void onSuccess(PortalUserDTO arg0) {
				close();
				MessageBox.info(messagesService.headerOkWindow(), usersMessagesService.msgUserCreatedSuccessfully(),
					null);

				// fire an Add event. UsersManagement listens to it and refreshes the grid.
				BaseEvent be = new BaseEvent(UserEditorPanel.this);
				fireEvent(Events.Add, be);
			}
		};

		service.create(user, tfPwd.getValue(), callback);
	}

	/**
	 * Saves GUI values into bound {@link #dto} and saves it using RPC.<br/>
	 */
	private void tryUpdateUser() {

		updateUserValues();

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(messagesService.headerErrorWindow(),
					usersMessagesService.msgErrorUpdatingUser() + " " + arg0.getLocalizedMessage(), null)
					.setModal(true);
			}

			public void onSuccess(PortalUserDTO arg0) {
				close();
				MessageBox.info(messagesService.headerOkWindow(), usersMessagesService.msgUserUpdatedSuccessfully(),
					null);
				// fire a "Change" event. UsersManagement listens to it and refreshes the grid.
				fireEvent(Events.Change, new BaseEvent(UserEditorPanel.this));
			}
		};

		service.update(dto, callback);
	}

	/**
	 * Returns a user DTO with values from the GUI.<br/>
	 * @return
	 */
	private PortalUserDTO getUserValues() {

		PortalUserDTO user = new PortalUserDTO();
		// name
		user.setUsername(tfName.getValue());
		// display name
		user.setDisplayName(tfDisplayName.getValue());
		// email
		user.setEmail(tfEmail.getValue());
		// active state
		user.setActive(cbActivate.getValue());
		// properties
		ListStore<UserPropertiesModel> store = gProp.getStore();
		List<UserPropertiesModel> propList = store.getModels();
		for (UserPropertiesModel prop : propList) {
			userProperties.put(prop.getKey(), prop.getValue());
		}
		user.setProperties(userProperties);

		return user;
	}

	/**
	 * Refreshes GUI with {@link #dto} values.<br/>
	 */
	private void updateUserValues() {

		dto.setUsername(tfName.getValue());
		// display name
		dto.setDisplayName(tfDisplayName.getValue());
		// email
		dto.setEmail(tfEmail.getValue());
		// active state
		dto.setActive(cbActivate.getValue());
		// properties
		ListStore<UserPropertiesModel> store = gProp.getStore();
		List<UserPropertiesModel> propList = store.getModels();
		for (UserPropertiesModel prop : propList) {
			userProperties.put(prop.getKey(), prop.getValue());
		}
		dto.setProperties(userProperties);

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
	 * Injects the messages bundle.
	 * @param messagesService
	 */
	@Inject
	public void setMessagesService(Messages messagesService) {
		this.messagesService = messagesService;
	}

	/**
	 * Injects the users specific messages bundle.
	 * @param usersMessagesService
	 */
	@Inject
	public void setUsersMessagesService(UsersMessages usersMessagesService) {
		this.usersMessagesService = usersMessagesService;
	}
	
	/**
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

}
