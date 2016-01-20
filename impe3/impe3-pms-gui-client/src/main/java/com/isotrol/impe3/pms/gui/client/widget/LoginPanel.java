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

package com.isotrol.impe3.pms.gui.client.widget;


import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.SessionErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.top.UserInfoPanel;


/**
 * Gui for Login
 * 
 * @author Manuel Ruiz
 * 
 */
public class LoginPanel extends TypicalWindow implements IDetailPanel {

	/**
	 * Widget width.<br/>
	 */
	private static final int WIDTH = 530;

	// html form ids
	private static final String FORM_ID = "loginForm";
	private static final String USER_LABEL_ID = "loginLabel";
	private static final String PASSWORD_LABEL_ID = "pwdLabel";
	private static final String USER_FIELD_ID = "loginUsername";
	private static final String PASSWORD_FIELD_ID = "loginPassword";
	private static final String SUBMIT_BUTTON_ID = "loginSubmit";

	/*
	 * GUI fields.
	 */
	/**
	 * "Username" field<br/>
	 */
	private InputElement tfUsername = null;
	/**
	 * "Password" field.<br/>
	 */
	private InputElement tfPassword = null;

	/**
	 * Contains the login error message.<br/>
	 */
	private LabelField tfErrorMessage = null;

	/**
	 * PMS login Form
	 */
	private FormPanel pmsLoginForm = null;

	/**
	 * Panel that shows the "Authenticating" state.<br/>
	 */
	private LayoutContainer lcAuthenticating = null;

	/*
	 * Injected deps
	 */
	/**
	 * The UserInfoPanel menu.<br/>
	 */
	private UserInfoPanel userInfoPanel = null;
	/**
	 * Proxy to users remote service.<br/>
	 */
	private ISessionsServiceAsync sessionsService = null;

	/**
	 * Application specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles service<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Form helper.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * The Pms main panel
	 */
	private PmsMainPanel pmsMainPanel = null;

	private NavigationPanel navigationPanel = null;

	/** PMS styles */
	private PmsStyles pmsStyles = null;

	/** Common messages bundle */
	private GuiCommonMessages messages = null;

	/** PMS utilities bundle */
	private PmsUtil pmsUtil = null;

	private SessionErrorMessageResolver sessionsError = null;

	/**
	 * Constructor
	 */
	public LoginPanel() {
	}

	/**
	 * Inits the widget. Must be called after the properties injection<br/>
	 */
	@Override
	protected void beforeRender() {
		initThis();
		if (!pmsUtil.isDisableLogin()) {
			initComponent();
		}
	}

	@Override
	protected void afterRender() {
		super.afterRender();
		getBody().setWidth(Constants.HUNDRED_PERCENT);
		if (pmsUtil.isDisableLogin()) {
			hide();
			setStyleAttribute("display", "none");
		}
	}

	/**
	 * Configures the panel.
	 */
	private void initThis() {
		setClosable(false);
		setWidth(WIDTH);
		setAutoHeight(true);
		String heading = pmsMessages.headerLoginPanel();
		setHeadingText(heading);
		setDraggable(false);
		setPlain(true);
		setShim(false);
		setShadow(false);
		setHeaderVisible(!pmsUtil.isDisableLogin());

		setButtonAlign(HorizontalAlignment.LEFT);
		setIconStyle(styles.iUser());
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		LayoutContainer lc = new LayoutContainer(formSupport.getStandardLayout(false));
		lc.addStyleName(pmsStyles.loginPanel());
		lc.setAutoWidth(true);

		tfErrorMessage = new LabelField();
		tfErrorMessage.addStyleName(styles.labelInfoMessage());
		tfErrorMessage.addStyleName(styles.redMessage());
		tfErrorMessage.setVisible(false);
		lc.add(tfErrorMessage);

		add(lc);

		lcAuthenticating = new LayoutContainer(new ColumnLayout());
		lcAuthenticating.setVisible(false);
		lcAuthenticating.setStyleName(styles.marginBottom10px());
		// add "loading" icon:
		lcAuthenticating.add(new Html("<div class='loading-icon' style='float: right; margin-right: 5px;'></div>"),
			new ColumnData(165));
		// add "authenticating" label:
		Label tfAuthenticating = new Label(pmsMessages.msgAuthenticating());
		tfAuthenticating.addStyleName(styles.labelInfoMessage());
		lcAuthenticating.add(tfAuthenticating);

		add(lcAuthenticating);

		KeyPressHandler keyEnterPressHandler = new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				if (event.getUnicodeCharCode() == KeyCodes.KEY_ENTER) {
					pmsLoginForm.submit();
				}
			}
		};

		tfUsername = (InputElement) Document.get().getElementById(USER_FIELD_ID);
		// wrap the input element in a gwt textbox to listen 'enter' key press
		TextBox tbUserName = TextBox.wrap(tfUsername);
		if (tbUserName != null) {
			tbUserName.addKeyPressHandler(keyEnterPressHandler);
		}

		tfPassword = (InputElement) Document.get().getElementById(PASSWORD_FIELD_ID);
		PasswordTextBox tbPassword = PasswordTextBox.wrap(tfPassword);
		if (tbPassword != null) {
			tbPassword.addKeyPressHandler(keyEnterPressHandler);
		}

		LabelElement userLabel = (LabelElement) Document.get().getElementById(USER_LABEL_ID);
		userLabel.setInnerText(pmsMessages.labelUsername());
		LabelElement pwdLabel = (LabelElement) Document.get().getElementById(PASSWORD_LABEL_ID);
		pwdLabel.setInnerText(pmsMessages.labelPassword());

		// Get a handle to the form and set its action. The Wraping for form must be after the input wrapings
		pmsLoginForm = FormPanel.wrap(Document.get().getElementById(FORM_ID), false);
		// form.setAction("javascript:__gwt_login()");
		// form.setAction("javascript:''");
		pmsLoginForm.addSubmitHandler(new SubmitHandler() {
			/**
			 * Add login form validations (user and password are required fields)
			 * @see com.google.gwt.user.client.ui.FormPanel.SubmitHandler#onSubmit(com.google.gwt.user.client.ui.FormPanel.SubmitEvent)
			 */
			public void onSubmit(SubmitEvent event) {
				if (tfUsername.getValue() == null || tfUsername.getValue().equals("")) {
					tfErrorMessage.setValue(pmsMessages.msgErrorUserRequired());
					tfErrorMessage.show();
					event.cancel();
				} else if (tfPassword.getValue() == null || tfPassword.getValue().equals("")) {
					tfErrorMessage.setValue(pmsMessages.msgErrorPasswordRequired());
					tfErrorMessage.show();
					event.cancel();
				} else {
					initAuthentication();
				}
			}
		});

		// Get the submit button for text localization
		final ButtonElement submit = (ButtonElement) Document.get().getElementById(SUBMIT_BUTTON_ID);
		submit.setInnerText(messages.labelAccept());

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				submit.click();
			}
		};
		Button bAccept = buttonsSupport.createAcceptButton(listener);
		addButton(bAccept);
		// Add the form to the panel
		lc.add(pmsLoginForm);
	}

	/**
	 * Shows the "Authenticating..." message, and calls {@link #tryLogin()}.<br/>
	 */
	private void initAuthentication() {
		tfErrorMessage.hide();
		lcAuthenticating.show();
		tryLogin();
	}

	/**
	 * Retrieves the input username and password and checks them against the users remote service.<br/>
	 */
	private void tryLogin() {

		AsyncCallback<SessionDTO> callback = new AsyncCallback<SessionDTO>() {
			public void onSuccess(SessionDTO sessionDto) {
				if (sessionDto == null) { // bad credentials
					lcAuthenticating.hide();
					onAuthenticationFailure(null);
				} else { // OK
					showPmsViewport(sessionDto);
					hide();
				}
			}

			/**
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				lcAuthenticating.hide();
				onAuthenticationFailure(arg0);
			}
		};

		sessionsService.login(new Credentials.BasicCredentials().setLogin(tfUsername.getValue()).setPassword(
			tfPassword.getValue()), callback);
	}

	/**
	 * Called when authentication failed<br/>
	 * @param error
	 */
	private void onAuthenticationFailure(Throwable error) {
		tfErrorMessage.setValue(sessionsError.getErrorLoginMessage(error, pmsMessages.msgErrorAuthentication()));
		tfErrorMessage.show();
	}

	/**
	 * Shows the PMS viewport.<br/>
	 * @param sessionDto the current session information
	 */
	public void showPmsViewport(SessionDTO sessionDto) {

		// leftMenu.setSessionDto(sessionDto);
		userInfoPanel.setSessionDto(sessionDto);

		final PmsViewport viewport = PmsFactory.getInstance().getPmsViewport();
		viewport.init();
		pmsMainPanel.add(viewport);

		RootPanel.get().add(pmsMainPanel);

		History.newItem(PmsConstants.INIT_TOKEN);

		// activate the current layout if the history token tells you
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				if (PmsConstants.INIT_TOKEN.equals(event.getValue())) {
					// initializes the navigation
					navigationPanel.initNavigation();
					viewport.init();
					pmsMainPanel.activateViewport(viewport);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		return true;
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
	 * @return the sessionsService
	 */
	public ISessionsServiceAsync getSessionsService() {
		return sessionsService;
	}

	/**
	 * Injects the Sessions service proxy.<br/>
	 * @param sessionsService
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
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
	 * Injects the buttons helper.<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the form support object.<br/>
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
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

	/**
	 * @param userInfoPanel the userInfoPanel to set
	 */
	@Inject
	public void setUserInfoPanel(UserInfoPanel userInfoPanel) {
		this.userInfoPanel = userInfoPanel;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @return the pmsUtil
	 */
	public PmsUtil getPmsUtil() {
		return pmsUtil;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

	/**
	 * @param sessionsError the sessionsError to set
	 */
	@Inject
	public void setSessionsError(SessionErrorMessageResolver sessionsError) {
		this.sessionsError = sessionsError;
	}
}
