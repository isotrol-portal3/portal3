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


import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.isotrol.impe3.pms.gui.common.util.Messages;
import com.isotrol.impe3.users.api.PortalUserSelDTO;
import com.isotrol.impe3.users.gui.service.IPortalUsersServiceAsync;
import com.isotrol.impe3.users.gui.util.UsersMessages;


/**
 * Window to change the user password
 * 
 * @author Manuel Ruiz
 * 
 */
public class ChangePwdWindow extends Window {

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
	/** portal users usersService */
	private IPortalUsersServiceAsync usersService = null;
	
	private Messages messagesBundle = null;
	
	/**
	 * Reference to users messages usersService.<br/>
	 */
	private UsersMessages usersMessagesBundle = null;

	/**
	 * Constructor
	 * 
	 */
	public ChangePwdWindow() {}
	
	/**
	 * Inits the widget. Must be explicitly called after deps injection.
	 * @param user
	 * @return
	 */
	public ChangePwdWindow init(PortalUserSelDTO user) {
		this.user = user;
		
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
		setCloseAction(CloseAction.CLOSE);
		setWidth(400);
		setAutoHeight(true);
		setButtonAlign(HorizontalAlignment.LEFT);

		container = new LayoutContainer(new FormLayout());
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Creates and adds fields properties.<br/>
	 */
	private void addFields() {

		// password
		tfPwd = new TextField<String>();
		tfPwd.setFieldLabel(usersMessagesBundle.labelPasswordField());
		tfPwd.setAllowBlank(false);
		tfPwd.setAutoValidate(true);
		tfPwd.setPassword(true);
		container.add(tfPwd);

		// repeat pasword
		tfRPwd = new TextField<String>();
		tfRPwd.setFieldLabel(usersMessagesBundle.labelRepeatPasswordField());
		tfRPwd.setAllowBlank(false);
		tfRPwd.setAutoValidate(true);
		tfRPwd.setPassword(true);
		container.add(tfRPwd);
	}
	
	/**
	 * Creates, configures and adds the button bar.<br/>
	 */
	private void addButtonBar() {
		// buttons bar
		buttonBar = new ButtonBar();

		Button bAccept = new Button(messagesBundle.labelAccept());
		bAccept.addSelectionListener(new SelectionListener<ComponentEvent>() {

			@Override
			public void componentSelected(ComponentEvent ce) {
				if (checkEqualPwds()) {
					tryChangePwd();
				} else {
					MessageBox.alert(
						messagesBundle.headerErrorWindow(), 
						usersMessagesBundle.msgPasswordsDoNotMatch(), 
						null);
				}
			}

			private boolean checkEqualPwds() {

				boolean equals = false;
				if (tfPwd.getValue().equals(tfRPwd.getValue())) {
					equals = true;
				}

				return equals;
			}
		});
		buttonBar.add(bAccept);

		Button bCancel = new Button(messagesBundle.labelCancel());
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
	 * Requests usersService for chaging password.<br/>
	 */
	private void tryChangePwd() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(messagesBundle.headerErrorWindow(),
					usersMessagesBundle.msgErrorChangingUserPassword(), 
					null).setModal(true);
			}

			public void onSuccess(Void arg) {
				close();
				MessageBox.info(messagesBundle.headerOkWindow(), 
						usersMessagesBundle.msgPasswordChangedSuccessfully(),
						null);
			}
		};

		usersService.changePassword(user.getId(), "", callback);
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
	 * Injects the generic messages bundle.
	 * @param messagesBundle
	 */
	@Inject
	public void setMessagesBundle(Messages messagesBundle) {
		this.messagesBundle = messagesBundle;
	}

	/**
	 * Injects the users app specific messages bundle.
	 * @param usersMessagesBundle
	 */
	@Inject
	public void setUsersMessagesBundle(UsersMessages usersMessagesBundle) {
		this.usersMessagesBundle = usersMessagesBundle;
	}
	
}
