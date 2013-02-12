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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;

/**
 * Window to change the user password
 * 
 * @author Manuel Ruiz
 * 
 */
public class ChangePwdWindow extends Window implements IDetailPanel {

	/**
	 * Window width in pixels.<br/>
	 */
	private static final int WINDOW_WIDTH = 400;
	
	/*
	 * fields
	 */
	/**
	 * "password" field.<br/>
	 */
	private TextField<String> tfPwd = null;
	/**
	 * "repeat password" field.<br/>
	 */
	private TextField<String> tfRPwd = null;

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/**
	 * Bound user data.<br/>
	 */
	private UserSelDTO user = null;

	/**
	 * Change event sources.<br/>
	 */
	private List<Component> changeSourceComponents = null;
	
	/*
	 * Injected fields
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;
	/**
	 * PMS events listening strategy<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;
	
	/**
	 * Users async service proxy<br/>
	 */
	private IUsersServiceAsync usersService = null;
	
	/**
	 * Error Message Resolver for Users service.<br/>
	 */
	private UsersServiceErrorMessageResolver emrUsers = null;

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;
	
	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Constructor
	 */
	public ChangePwdWindow() {
		changeSourceComponents = new LinkedList<Component>();
	}

	/**
	 * Inits the widget. Must be explicitly called after deps injection.
	 * @param user
	 * @return
	 */
	public ChangePwdWindow init(UserSelDTO userDto) {
		this.user = userDto;

		configThis();
		initComponents();

		return this;
	}

	/**
	 * Inits this window inner components.<br/>
	 */
	private void initComponents() {
		addFields();
		addButtonBar();
	}

	/**
	 * Configures this window properties and main container.<br/>
	 */
	private void configThis() {
		setLayout(new FitLayout());
		setClosable(false);
		setWidth(WINDOW_WIDTH);
		setAutoHeight(true);
		setButtonAlign(HorizontalAlignment.LEFT);

		container = new LayoutContainer(new FormLayout());
		container.addStyleName(styles.margin10px());
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Creates and adds fields properties.<br/>
	 */
	private void addFields() {

		// password
		tfPwd = new TextField<String>();
		tfPwd.setFieldLabel(messages.labelPasswordField());
		tfPwd.setAllowBlank(false);
		tfPwd.setAutoValidate(true);
		tfPwd.addListener(Events.Render, validatorListener);
		tfPwd.addListener(Events.KeyUp, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				tfRPwd.validate();
			}
		});
		tfPwd.setPassword(true);

		container.add(tfPwd);
		changeSourceComponents.add(tfPwd);

		// repeat pasword
		tfRPwd = new TextField<String>();
		tfRPwd.setFieldLabel(messages.labelRepeatPasswordField());
		tfRPwd.setAllowBlank(false);
		tfRPwd.setAutoValidate(true);
		tfRPwd.setValidator(new Validator() {
			public String validate(Field<?> field, String value) {
				String pwd = tfPwd.getValue();
				String rPwd = tfRPwd.getValue();
				if (!pwd.equals(rPwd)) {
					return messages.msgPasswordsDoNotMatch();
				}
				return null;
			}
		});
		tfRPwd.addListener(Events.Render, validatorListener);
		tfRPwd.setPassword(true);
		
		container.add(tfRPwd);
		changeSourceComponents.add(tfRPwd);
	}

	/**
	 * Creates, configures and adds the button bar.<br/>
	 */
	private void addButtonBar() {
		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryChangePwd();
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(
				this, 
				lAccept, 
				changeSourceComponents, 
				pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Requests usersService for chaging password.<br/>
	 */
	private void tryChangePwd() {
		util.mask(messages.mskChangePassword());
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrUsers, messages.msgErrorChangingUserPassword());
			}

			public void onSuccess(Void arg) {
				hide();
				util.unmask();
				util.info(messages.msgPasswordChangedSuccessfully());
			}
		};

		usersService.setPassword(user.getId(), tfPwd.getValue(), callback);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	/**
	 * <br/>
	 */
	public boolean isDirty() {
		return tfPwd.isDirty() || tfRPwd.isDirty();
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	/**
	 * <br/>
	 */
	public boolean isEdition() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	/**
	 * <br/>
	 */
	public boolean isValid() {
		return tfPwd.isValid() && tfRPwd.isValid() 
			&& tfPwd.getValue().equals(tfRPwd.getValue());
	}

	/**
	 * Injects the generic messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the users aynchronous service.
	 * @param usersService the usersService to set
	 */
	@Inject
	public void setUsersService(IUsersServiceAsync usersService) {
		this.usersService = usersService;
	}
	
	/**
	 * Injects the buttons helper object.
	 * @param helper the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons helper) {
		this.buttonsSupport = helper;
	}

	/**
	 * Inject the events listening strategy.<br/>
	 * @param pmsListeningStrategy
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = pmsListeningStrategy;
	}
	
	/**
	 * Injects the field validator listener.
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener  = validatorListener;
	}
	
	/**
	 * @param utilities the util to set
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}
	
	/**
	 * Injects the Error Message Resolver for Users service.
	 * @param emr the emrUsers to set
	 */
	@Inject
	public void setEmrUsers(UsersServiceErrorMessageResolver emr) {
		this.emrUsers = emr;
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
