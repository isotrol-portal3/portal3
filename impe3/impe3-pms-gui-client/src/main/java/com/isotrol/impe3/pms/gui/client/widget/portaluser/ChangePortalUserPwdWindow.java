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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
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
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.gui.api.service.external.IPortalUsersExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PortalUsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.UsersSettings;
import com.isotrol.impe3.users.api.PortalUserSelDTO;


/**
 * Window to change the user password
 * 
 * @author Manuel Ruiz
 * 
 */
public class ChangePortalUserPwdWindow extends Window implements IDetailPanel {

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
	private PortalUserSelDTO user = null;

	/*
	 * Injected fields
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	
	/**
	 * Form support object<br/>
	 */
	private FormSupport formSupport = null;
	
	/**
	 * Events listening strategy.<br/>
	 */
	private IComponentListeningStrategy eventsListeningStrategy = null;
	
	/**
	 * Field Listener that validates when processing the event.<br/>
	 */
	private ValidatorListener validatorListener = null;
	
	/** portal users usersService */
	private IPortalUsersExternalServiceAsync usersService = null;

	/**
	 * Error message resolver for the Users service.<br/>
	 */
	private PortalUsersServiceErrorMessageResolver emrPortalUsers = null;
	
	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messagesBundle = null;

	/**
	 * Reference to users settings.<br/>
	 */
	private UsersSettings usersSettings = null;
	
	/**
	 * Window utilities.<br/>
	 */
	private Util util = null;
	
	/**
	 * External service ID<br/>
	 */
	private String serviceId = null;
	
	/**
	 * Buttons support.<br/>
	 */
	private Buttons buttons = null;
	
	/**
	 * Common styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Constructor
	 * 
	 */
	public ChangePortalUserPwdWindow() {}

	/**
	 * Inits the widget. Must be explicitly called after deps injection.
	 * @param user
	 * @return
	 */
	public ChangePortalUserPwdWindow init(PortalUserSelDTO portaluser) {
		this.user = portaluser;
		this.serviceId = com.google.gwt.user.client.Window.Location
			.getParameter(usersSettings.paramUsersPortalService());

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
		setWidth(Constants.SMALL_WINDOW_WIDTH);
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
		tfPwd.setFieldLabel(messagesBundle.labelPasswordField());
		tfPwd.setAutoValidate(true);
		formSupport.configRequired(tfPwd);
		tfPwd.setPassword(true);
		container.add(tfPwd);

		// repeat pasword
		tfRPwd = new TextField<String>();
		tfRPwd.setFieldLabel(messagesBundle.labelRepeatPasswordField());
		tfRPwd.setAllowBlank(false);
		tfRPwd.setAutoValidate(true);
		tfRPwd.setPassword(true);
		tfRPwd.setValidator(new Validator() {
			public String validate(Field<?> field, String value) {
				String pwd = tfPwd.getValue();
				String rPwd = tfRPwd.getValue();
				if (!pwd.equals(rPwd)) {
					return messagesBundle.msgPasswordsDoNotMatch();
				}
				return null;
			}
		});
		tfRPwd.addListener(Events.Render, validatorListener);
		container.add(tfRPwd);
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
		addButton(buttons.createSaveButtonForDetailPanels(
			this, 
			lAccept, 
			Arrays.asList(new Component [] {tfPwd, tfRPwd}), 
			eventsListeningStrategy));

		Button bCancel = buttons.createCancelButtonForDetailPanels(this);
		addButton(bCancel);

	}

	/**
	 * Requests usersService for chaging password.<br/>
	 */
	private void tryChangePwd() {
		util.mask(messagesBundle.mskChangePassword());
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(
						arg0, 
						emrPortalUsers, 
						messagesBundle.msgErrorChangingUserPassword());
			}

			public void onSuccess(Void arg) {
				hide();
				util.unmask();
				util.info(messagesBundle.msgPasswordChangedSuccessfully());
			}
		};

		usersService.changePassword(serviceId, user.getId(), tfPwd.getValue(), callback);
	}

	/**
	 * Injects the users service.
	 * @param usersService
	 */
	@Inject
	public void setUsersService(IPortalUsersExternalServiceAsync usersService) {
		this.usersService = usersService;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messagesBundle
	 */
	@Inject
	public void setMessagesBundle(GuiCommonMessages messagesBundle) {
		this.messagesBundle = messagesBundle;
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
	 * Injects the utilities.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}
	
	/**
	 * Injects the buttons support
	 * @param b the buttons to set
	 */
	@Inject
	public void setButtons(Buttons b) {
		this.buttons = b;
	}
	
	/**
	 * @param strategy the eventsListeningStrategy to set
	 */
	@Inject
	public void setEventsListeningStrategy(IComponentListeningStrategy strategy) {
		this.eventsListeningStrategy = strategy;
	}
	
	/**
	 * @param formSupport the formSupport to set
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}
	
	/**
	 * Injects the validator listener.
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
	}
	
	/**
	 * Injects the Portal Users service error message resolver.
	 * @param emr the emrPortalUsers to set
	 */
	@Inject
	public void setEmrPortalUsers(PortalUsersServiceErrorMessageResolver emr) {
		this.emrPortalUsers = emr;
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
		return false;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	/**
	 * <br/>
	 */
	public boolean isValid() {
		return tfPwd.isValid() && tfRPwd.isValid();
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
