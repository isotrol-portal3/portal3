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


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
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
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.gui.api.service.external.IPortalUsersExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.UserPropertyModel;
import com.isotrol.impe3.pms.gui.client.error.PortalUsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.UsersMessages;
import com.isotrol.impe3.pms.gui.client.i18n.UsersSettings;
import com.isotrol.impe3.users.api.PortalUserDTO;


/**
 * Popup window that manages the detailed information of a User.<br/>
 * 
 * <dl> <dt><b>Events:</b></dt>
 * 
 * <dd><b>Change</b> : AppEvent&lt;UserEditorPanel&gt;<br> <div>Fires after an existing 
 * user information has been succesfully updated on server.</div> 
 * <ul> <li>UserEditorPanel : this</li> </ul> 
 * </dd>
 * 
 * <dd><b>Add</b> : AppEvent&lt;UserEditorPanel&gt;<br> <div>Fires after 
 * a new user information has been succesfully saved on server.</div> 
 * 
 * <ul> <li>UserEditorPanel : this</li> </ul> </dd>
 * 
 * </dl>
 * 
 * @author Manuel Ruiz
 * 
 */
public class UserEditorPanel extends Window implements IDetailPanel {

	/**
	 * Service ID obtained from the request.<br/>
	 */
	private String serviceId = null;
	
	/**
	 * Becomes <code>true</code> when some property is added, removed or changed 
	 * in the custom properties grid.<br/>
	 */
	private boolean propertiesPanelDirty = false;
	
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
	
	/**
	 * Collection of components that fire Change events.<br/>
	 */
	private List<Component> changeSourceComponents = null;

	/** user properties */
	private Map<String, String> userProperties = new HashMap<String, String>();
	/** user properties grid */
	private EditorGrid<UserPropertyModel> gProp = null;
	/** user properties content panel */
	private ContentPanel cpProperties = null;

	/** current user DTO. */
	private PortalUserDTO dto = null;

	/*
	 * Injected deps.
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	
	/**
	 * PMS listening strategy.<br/>
	 */
	private IComponentListeningStrategy eventsListeningStrategy = null;
	
	/**
	 * Reference to users settings.<br/>
	 */
	private UsersSettings usersSettings = null;
	
	/**
	 * Window utilities.<br/>
	 */
	private Util util = null; 
	
	/** portal users service */
	private IPortalUsersExternalServiceAsync service = null;
	
	/**
	 * Error message resolver for the {@link #service}.<br/>
	 */
	private PortalUsersServiceErrorMessageResolver emr = null;

	/**
	 * Common module messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Users messages service reference.<br/>
	 */
	private UsersMessages usersMessages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms helper object.<br/>
	 */
	private FormSupport formSupport = null;
	
	/**
	 * Common styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Constructor provided with bound data.<br/>
	 * 
	 * @param rdDto
	 */
	public UserEditorPanel() {
		this.changeSourceComponents = new LinkedList<Component>();
	}

	/**
	 * Inits the widget. Must be explicitly called after properties injection
	 * @param puDto
	 */
	public void init(PortalUserDTO puDto) {
		this.dto = puDto;
		this.serviceId = com.google.gwt.user.client.Window.Location
			.getParameter(usersSettings.paramUsersPortalService());

		configThis();
		initComponents();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {

		addFields();

		if (isEdition()) {
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
		setShadow(false);
		setSize(600, 500);
		setButtonAlign(HorizontalAlignment.LEFT);

		container = new LayoutContainer(new FormLayout());
		container.addStyleName(styles.margin10px());
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Adds the fields to the container.<br/>
	 */
	private void addFields() {
		// name
		tfName = new TextField<String>();
		tfName.setFieldLabel(messages.labelUserNameField());
		formSupport.configRequired(tfName);
		tfName.setAutoValidate(true);
		container.add(tfName);
		changeSourceComponents.add(tfName);

		// display name
		tfDisplayName = new TextField<String>();
		tfDisplayName.setFieldLabel(messages.labelDisplayNameField());
		tfDisplayName.setAutoValidate(true);
		formSupport.configRequired(tfDisplayName);
		container.add(tfDisplayName);
		changeSourceComponents.add(tfDisplayName);

		if (!isEdition()) {
			// password
			tfPwd = new TextField<String>();
			tfPwd.setFieldLabel(messages.labelPasswordField());
			formSupport.configRequired(tfPwd);
			tfPwd.setAutoValidate(true);
			tfPwd.setPassword(true);
			container.add(tfPwd);
			changeSourceComponents.add(tfPwd);

			// repeat pasword
			tfRPwd = new TextField<String>();
			tfRPwd.setFieldLabel(messages.labelRepeatPasswordField());
			formSupport.configRequired(tfRPwd);
			tfRPwd.setAutoValidate(true);
			tfRPwd.setPassword(true);
			container.add(tfRPwd);
			changeSourceComponents.add(tfRPwd);
		}

		// email
		tfEmail = new TextField<String>();
		tfEmail.setFieldLabel(usersMessages.labelEmailField());
		container.add(tfEmail);
		changeSourceComponents.add(tfEmail);

		cbActivate = new CheckBox();
		cbActivate.addInputStyleName(styles.checkBoxAlignLeft());
		cbActivate.setFieldLabel(messages.labelActivatedField());
		container.add(cbActivate);
		changeSourceComponents.add(cbActivate);

		addPropertiesGrid();
	}

	/**
	 * Create and add a grid with to edit the user properties
	 */
	private void addPropertiesGrid() {

		cpProperties = new ContentPanel();
		cpProperties.setHeight("250");
		cpProperties.setHeadingText(messages.headerPropertiesPanel());
		cpProperties.setLayout(new FitLayout());

		addToolBar();

		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		// add a checkbox column to select properties
		CheckBoxSelectionModel<UserPropertyModel> smProp = new CheckBoxSelectionModel<UserPropertyModel>();
		cc.add(smProp.getColumn());

		ColumnConfig ccKey = new ColumnConfig(UserPropertyModel.PROPERTY_KEY, messages.columnHeaderKey(),
			COLUMN_KEY_WIDTH);
		TextField<String> tfKeyEditor = new TextField<String>();
		tfKeyEditor.setAllowBlank(false);
		tfKeyEditor.setAutoValidate(false);
		ccKey.setEditor(new CellEditor(tfKeyEditor));
		cc.add(ccKey);

		ColumnConfig ccValue = new ColumnConfig(UserPropertyModel.PROPERTY_VALUE,
			messages.columnHeaderValue(), COLUMN_VALUE_WIDTH);
		TextField<String> tfVAlueEditor = new TextField<String>();
		tfVAlueEditor.setAllowBlank(false);
		tfVAlueEditor.setAutoValidate(false);
		ccValue.setEditor(new CellEditor(tfVAlueEditor));
		cc.add(ccValue);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<UserPropertyModel> storeProp = new ListStore<UserPropertyModel>();
		storeProp.addStoreListener(new StoreListener<UserPropertyModel>() {
			@SuppressWarnings("unchecked")
			@Override
			public void storeAdd(StoreEvent se) {
				propertiesPanelDirty = true;
				Util.fireChangeEvent(gProp);
			}
			@SuppressWarnings("unchecked")
			@Override
			public void storeRemove(StoreEvent se) {
				propertiesPanelDirty = true;
				Util.fireChangeEvent(gProp);
			}
			@SuppressWarnings("unchecked")
			@Override
			public void storeUpdate(StoreEvent se) {
				propertiesPanelDirty = true;
				Util.fireChangeEvent(gProp);
			}
		});

		gProp = new EditorGrid<UserPropertyModel>(storeProp, cmOwn);
		gProp.setAutoExpandColumn(UserPropertyModel.PROPERTY_VALUE);
		gProp.setSelectionModel(smProp);
		gProp.addPlugin(smProp);
		
		changeSourceComponents.add(gProp);
		
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

		SelectionListener<ButtonEvent> lAdd = new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				UserPropertyModel newProperty = new UserPropertyModel(
					usersMessages.defaultValueKey(), usersMessages.defaultValueValue());
				gProp.getStore().add(newProperty);
			}
		};
		buttonsSupport.addAddButton(toolbar, lAdd, null);
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<UserPropertyModel> selected = gProp.getSelectionModel().getSelectedItems();
				ListStore<UserPropertyModel> store = gProp.getStore();
				for (UserPropertyModel model : selected) {
					store.remove(model);
				}
			}
		};
		buttonsSupport.addDeleteButton(toolbar, lDelete, null);
		toolbar.add(new SeparatorToolItem());

		cpProperties.setTopComponent(toolbar);
	}

	/**
	 * Creates, configures and adds the button bar.<br/>
	 */
	private void addButtonBar() {

		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lw = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySaveUser();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), 
						messages.msgConfirmSaveUser(),
						lw).setModal(true);
			}
		};
		addButton(buttonsSupport.createSaveButtonForDetailPanels(
			this,
			lAccept,
			changeSourceComponents,
			eventsListeningStrategy));
		
		addButton(buttonsSupport.createCancelButtonForDetailPanels(this));
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
		List<UserPropertyModel> propList = new LinkedList<UserPropertyModel>();

		Iterator<Entry<String, String>> it = userProperties.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> e = it.next();
			propList.add(new UserPropertyModel((String) e.getKey(), (String) e.getValue()));
		}
		ListStore<UserPropertyModel> store = gProp.getStore();
		store.setFiresEvents(false);
		store.add(propList);
		store.setFiresEvents(true);
	}

	/**
	 * Calls service method create or update, according to current user state 
	 * (wether already created or not).<br/>
	 */
	private void trySaveUser() {
		util.mask(messages.mskSaveUser());
		
		if (isEdition()) {
			tryUpdateUser();
		} else {
			tryCreateUser();			
		}
	}
	
	/**
	 * Inserts GUI values into a new DTO and tries to save it using RPC.<br/>
	 */
	private void tryCreateUser() {

		PortalUserDTO user = getUserValues();

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emr, usersMessages.msgErrorCreatingUser());
			}

			public void onSuccess(PortalUserDTO arg0) {
				hide();
				
				util.unmask();
				util.info(usersMessages.msgUserCreatedSuccessfully());
				
				// fire an Add event. UsersManagement listens to it and refreshes the grid.
				AppEvent be = new AppEvent(Events.Add, arg0);
				be.setSource(UserEditorPanel.this);
				fireEvent(Events.Add, be);
			}
		};

		service.create(serviceId, user,tfPwd.getValue(), callback);
	}

	/**
	 * Saves GUI values into bound {@link #dto} and saves it using RPC.<br/>
	 */
	private void tryUpdateUser() {
		updateUserValues();

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emr, usersMessages.msgErrorUpdatingUser());
			}

			public void onSuccess(PortalUserDTO arg0) {
				hide();
				
				util.unmask();
				util.info(usersMessages.msgUserUpdatedSuccessfully());
				
				// fire a "Change" event. UsersManagement listens to it and refreshes the grid.
				AppEvent event = new AppEvent(Events.Change, dto);
				event.setSource(UserEditorPanel.this);
				fireEvent(Events.Change, event);
			}
		};

		service.update(serviceId, dto, callback);
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
		ListStore<UserPropertyModel> store = gProp.getStore();
		List<UserPropertyModel> propList = store.getModels();
		for (UserPropertyModel prop : propList) {
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
		ListStore<UserPropertyModel> store = gProp.getStore();
		List<UserPropertyModel> propList = store.getModels();
		for (UserPropertyModel prop : propList) {
			userProperties.put(prop.getKey(), prop.getValue());
		}
		dto.setProperties(userProperties);

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
	 * Injects the messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessagesService(GuiCommonMessages messagesService) {
		this.messages = messagesService;
	}

	/**
	 * Injects the users specific messages bundle.
	 * @param usersMessages
	 */
	@Inject
	public void setUsersMessagesService(UsersMessages usersMessagesService) {
		this.usersMessages = usersMessagesService;
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
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
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
	 * Injects the form helper.
	 * @param formSupport the formSupport to set
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}
	
	/**
	 * Injects the events listening strategy.<br/>
	 * @param strategy
	 */
	@Inject
	public void setEventsListeningStrategy(IComponentListeningStrategy strategy) {
		this.eventsListeningStrategy = strategy;
	}
	
	/**
	 * Injects the Error Message Resover for the Portal Users service.
	 * @param resolver the emr to set
	 */
	@Inject
	public void setEmr(PortalUsersServiceErrorMessageResolver resolver) {
		this.emr = resolver;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	/**
	 * <br/>
	 */
	public boolean isDirty() {
		boolean dirty = propertiesPanelDirty;
		Iterator<Component> it = changeSourceComponents.iterator();
		while(!dirty && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				Field<?> field = (Field<?>) component;
				dirty = field.isDirty();
			}
		}
		return dirty;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	/**
	 * <br/>
	 */
	public boolean isEdition() {
		return dto != null;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	/**
	 * <br/>
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Component> it = changeSourceComponents.iterator();
		while (valid && it.hasNext()) {
			Component component = it.next();
			if (component instanceof Field<?>) {
				Field<?> field = (Field<?>) component;
				valid = field.isValid();				
			}
		}
		return valid;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
	
}
