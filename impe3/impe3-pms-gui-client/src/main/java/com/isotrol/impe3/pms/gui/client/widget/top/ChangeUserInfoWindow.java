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

package com.isotrol.impe3.pms.gui.client.widget.top;

import java.util.Arrays;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.api.session.CurrentUserDTO;
import com.isotrol.impe3.pms.api.session.UserNameDTO;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;


/**
 * Window to change the user info
 * 
 */
public class ChangeUserInfoWindow extends Window implements IDetailPanel {

	/*
	 * fields
	 */
	/**
	 * "display name" field.<br/>
	 */
	private TextField<String> tfDisplayName = null;
	
	/**
	 * "username" field.<br/>
	 */
	private TextField<String> tfUsername = null;
	
	/**
	 * "Root" combo.<br/>
	 */
	private CheckBox cbRoot = null;
	
	/**
	 * Change password button
	 */
	private Button bChangePwd = null;
	
	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	
	/**
	 * Bound user data.<br/>
	 */
	private CurrentUserDTO currentUser = null;

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
	 * Error message resolver for the Users service.<br/>
	 */
	private SimpleErrorMessageResolver emr = null;
	
	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messagesBundle = null;
	
	/**
	 * Window utilities.<br/>
	 */
	private Util util = null;
	
	/**
	 * Buttons support.<br/>
	 */
	private Buttons buttons = null;
	
	/**
	 * Common styles bundle
	 */
	private GuiCommonStyles styles = null;
	
	/**
	 * Sessions Service
	 */
	private ISessionsServiceAsync sessionsService = null;
	
	/**
	 * User Info Panel in top
	 */
	private UserInfoPanel userInfoPanel = null;

	/**
	 * Constructor
	 * 
	 */
	public ChangeUserInfoWindow() {}

	/**
	 * Inits the widget. Must be explicitly called after deps injection.
	 * @param user
	 * @return
	 */
	public ChangeUserInfoWindow init(CurrentUserDTO currentUser) {
		this.currentUser = currentUser;
		
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
		setWidth(Constants.FORM_WINDOW_WIDTH);
		setAutoHeight(true);
		setButtonAlign(HorizontalAlignment.LEFT);

		container = new LayoutContainer(formSupport.getStandardLayout());
		container.addStyleName(styles.margin10px());
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Creates and adds fields properties.<br/>
	 */
	private void addFields() {
		
		// Username (login)
		tfUsername = new TextField<String>();
		tfUsername.setFieldLabel(messagesBundle.labelUserNameField());
		tfUsername.setOriginalValue(currentUser.getName().getName());
		tfUsername.setValue(currentUser.getName().getName());
		tfUsername.setEnabled(currentUser.isChangeLogin());
		tfUsername.setAutoValidate(true);
		formSupport.configRequired(tfUsername);
		container.add(tfUsername);
				
		// Display Name
		tfDisplayName = new TextField<String>();
		tfDisplayName.setFieldLabel(messagesBundle.labelDisplayNameField());
		tfDisplayName.setOriginalValue(currentUser.getName().getDisplayName());
		tfDisplayName.setValue(currentUser.getName().getDisplayName());
		tfDisplayName.setEnabled(currentUser.isChangeDisplayName());
		tfDisplayName.setAutoValidate(true);
		formSupport.configRequired(tfDisplayName);
		container.add(tfDisplayName);
		
		// is Root
		cbRoot = new CheckBox();
		cbRoot.addInputStyleName(styles.checkBoxAlignLeft());
		cbRoot.setFieldLabel(messagesBundle.labelRootField());
		cbRoot.setValue(currentUser.isRoot());
		cbRoot.setEnabled(false);
		container.add(cbRoot);
	}

	/**
	 * Creates, configures and adds the button bar.<br/>
	 */
	private void addButtonBar() {

		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryChangeUserInfo();
			}
		};
		Button bAccept = buttons.createSaveButtonForDetailPanels(
			this, 
			lAccept, 
			Arrays.asList(new Component [] {tfUsername, tfDisplayName}), 
			eventsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttons.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
		
		// change password button
		SelectionListener<ButtonEvent> lChangePwd = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				openChangeUserPwdWindow();
			}
		};
		bChangePwd = buttons.createGenericButton(messagesBundle.labelChangePassword(),
				styles.iChangePassword(), lChangePwd);
		bChangePwd.setEnabled(currentUser.isChangePassword());
		addButton(bChangePwd);
	}
	
	/**
	 * Opens the window to change user password
	 */
	private void openChangeUserPwdWindow() {
		
		if(currentUser.isChangePassword()) {
			ChangeUserPwdWindow userPwdWin = PmsFactory.getInstance().getChangeUserPwdWindow().init();
			userPwdWin.show();
		}
	}

	/**
	 * Requests usersService for changing user info.<br/>
	 */
	private void tryChangeUserInfo() {
		
		util.mask(messagesBundle.mskSaveUser());
		
		UserNameDTO userNameDTO = currentUser.getName();
		if(currentUser.isChangeLogin()) {
			userNameDTO.setName(tfUsername.getValue());
		}
		if(currentUser.isChangeDisplayName()) {
			userNameDTO.setDisplayName(tfDisplayName.getValue());
		}
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(
						arg0, 
						emr, 
						messagesBundle.msgErrorSaveUser());
			}

			public void onSuccess(Void result) {
				userInfoPanel.getLabelUsername().setText(tfDisplayName.getValue());
				
				hide();
				util.unmask();
				util.info(messagesBundle.msgOkSaveUser());
			}
		};

		sessionsService.setUserName(userNameDTO, callback);
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
	 * Injects the service error message resolver.
	 * @param emr the emr to set
	 */
	@Inject
	public void setEmr(SimpleErrorMessageResolver emr) {
		this.emr = emr;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	/**
	 * <br/>
	 */
	public boolean isDirty() {
		return tfDisplayName.isDirty() || tfUsername.isDirty();
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
		return tfDisplayName.isValid() && tfUsername.isValid();
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
	
	/**
	 * @param sessionsService the sessionsService to set
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}
	
	/**
	 * @param userInfoPanel the userInfoPanel to set
	 */
	@Inject
	public void setUserInfoPanel(UserInfoPanel userInfoPanel) {
		this.userInfoPanel = userInfoPanel;
	}
}
