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

package com.isotrol.impe3.users.gui.login;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;

import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.pms.gui.common.util.Styles;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.gui.UsersViewport;
import com.isotrol.impe3.users.gui.service.IPortalUsersServiceAsync;
import com.isotrol.impe3.users.gui.util.UsersMessages;

/**
 * Gui for Login
 * 
 * @author Manuel Ruiz
 * 
 */
public class LoginPanel extends ContentPanel {

	/**
	 * "Username" field<br/>
	 */
	private TextField<String> user = null;
	/**
	 * "Password" field.<br/>
	 */
	private TextField<String> pwd = null;

	/*
	 * Injected deps
	 */
	/**
	 * Proxy to users remote service.<br/>
	 */
	private IPortalUsersServiceAsync usersService = null;

	/**
	 * Application specific messages service.<br/>
	 */
	private UsersMessages usersMessagesService = null;

	/**
	 * General messages service.<br/>
	 */
	private Messages messagesService = null;
	
	/**
	 * Common styles service<br/>
	 */
	private Styles styles = null;

	/**
	 * Constructor
	 */
	public LoginPanel() {}
	
	/**
	 * Inits the widget. Must be called after the properties injection
	 */
	public void init() {
		configComponent();
		initThis();
	}

	/**
	 * Configures the panel
	 */
	private void configComponent() {

		setWidth(400);
		setHeading(usersMessagesService.appName());
		setIconStyle(styles.iconUser());
		setFrame(true);
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initThis() {

		LayoutContainer lc = new LayoutContainer(new FormLayout());
		lc.setAutoWidth(true);

		user = new TextField<String>();
		user.setFieldLabel(usersMessagesService.labelUserNameField());
		user.setAllowBlank(false);
		lc.add(user);

		pwd = new TextField<String>();
		pwd.setPassword(true);
		pwd.setFieldLabel(usersMessagesService.labelPasswordField());
		pwd.setAllowBlank(false);
		lc.add(pwd);

		ButtonBar bb = new ButtonBar();
		bb.addStyleName(styles.marginTop10px());
		bb.setButtonAlign(HorizontalAlignment.CENTER);
		lc.add(bb);

		Button accept = new Button(messagesService.labelAccept());
		accept.addSelectionListener(new SelectionListener<ComponentEvent>() {

			@Override
			public void componentSelected(ComponentEvent ce) {
				tryLoginUser();
			}

		});
		bb.add(accept);

		Button reset = new Button(messagesService.labelDelete());
		reset.addSelectionListener(new SelectionListener<ComponentEvent>() {

			@Override
			public void componentSelected(ComponentEvent ce) {
				resetLoginFields();
			}

		});
		bb.add(reset);

		add(lc);
	}

	/**
	 * Resets "username" and "password" fields.<br/>
	 */
	private void resetLoginFields() {
		user.reset();
		pwd.reset();
	}

	/**
	 * Retrieves the input username and password and checks them against the
	 * users remote service.<br/>
	 */
	private void tryLoginUser() {

		AsyncCallback<PortalUserDTO> callback = new AsyncCallback<PortalUserDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert("Error", "No pudo logarse.", null).setModal(
						true);
				openUserApplication();
			}

			public void onSuccess(PortalUserDTO arg0) {
				MessageBox.alert("Logado", "Logado!!!!", null).setModal(true);
				openUserApplication();
			}
		};

		usersService.checkPassword(user.getValue(), pwd.getValue(), callback);
	}

	/**
	 * Creates and shows the user application panel.<br/>
	 */
	private void openUserApplication() {
		final RootPanel rootPanel = RootPanel.get();
		// remove login panel
		rootPanel.remove(0);

		UsersViewport mainPanel = new UsersViewport();
		rootPanel.add(mainPanel);
	}

	/**
	 * Injects the users service.
	 * @param usersService
	 */
	@Inject
	public void setUsersService(IPortalUsersServiceAsync usersService) {
		this.usersService = usersService;
	}

	/**
	 * Injects the users messages bundle
	 * @param usersMessagesService
	 */
	 @Inject
	public void setUsersMessagesService(UsersMessages usersMessagesService) {
		this.usersMessagesService = usersMessagesService;
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messagesService
	 */
	 @Inject
	public void setMessagesService(Messages messagesService) {
		this.messagesService = messagesService;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(Styles styles) {
		this.styles = styles;
	}
	
	
}
