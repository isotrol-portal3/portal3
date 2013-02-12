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


import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.api.session.CurrentUserDTO;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * panel with the current user information and the application log-out button
 * @author Manuel Ruiz
 * 
 */
public class UserInfoPanel extends HorizontalPanel {
	
	/** Display name */
	private Label labelUsername = null;

	/** Session dto */
	private SessionDTO sessionDto = null;

	/** Pms styles */
	private PmsStyles pmsStyles = null;

	/** Gui common styles */
	private GuiCommonStyles guiCommonStyles = null;

	/** Common messages bundle */
	private GuiCommonMessages messages = null;

	/** Pms messages bundle */
	private PmsMessages pmsMessages = null;
	
	/** Session service */
	private ISessionsServiceAsync sessionsService = null;

	/**
	 * Error message resolver for the session service.<br/>
	 */
	private SimpleErrorMessageResolver emr = null;
	
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	
	/** PMS utilities bundle */
	private PmsUtil pmsUtil = null;
	

	@Override
	protected void beforeRender() {
		initThis();
		addComponents();
	}

	private void initThis() {
		setAutoWidth(true);
		setVerticalAlign(VerticalAlignment.MIDDLE);
		setHorizontalAlign(HorizontalAlignment.RIGHT);
	}

	private void addComponents() {

		assert sessionDto != null : "A SessionDTO object must be provided before initializing this";

		labelUsername = new Label(sessionDto.getName());
		labelUsername.addStyleName(pmsStyles.infoUser());
		
		labelUsername.addListener(Events.OnClick, new Listener<BaseEvent>() {
			
			public void handleEvent(BaseEvent be) {
				tryGetUserInfo();
			}
		});

		Image bLogout = new Image("img/logout.jpg");
		bLogout.addStyleName(guiCommonStyles.cursorPointer());
		bLogout.setTitle(messages.tooltipDisconnect());
		bLogout.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							tryLogout();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmLogout(), listener).setModal(
					true);
			}
		});

		TableData td = new TableData("23px", "100%");
		add(labelUsername);
		add(bLogout, td);
	}
	
	/**
	 * Requests usersService for chaging password.<br/>
	 */
	private void tryGetUserInfo() {
		
		AsyncCallback<CurrentUserDTO> callback = new AsyncCallback<CurrentUserDTO>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(
						arg0, 
						emr, 
						messages.msgError());
			}

			public void onSuccess(CurrentUserDTO currentUser) {
				openChangeUserInfoWindow(currentUser);
			}
		};

		sessionsService.getCurrentUser(callback);
	}
	
	/**
	 * Opens the user info Window
	 */
	private void openChangeUserInfoWindow(CurrentUserDTO currentUser) {
		ChangeUserInfoWindow userInfoWin = PmsFactory.getInstance().getChangeUserInfoWindow().init(currentUser);
		userInfoWin.show();
	}

	/**
	 * Logs out of the app.<br/>
	 */
	private void tryLogout() {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onSuccess(Void arg0) {
				//Window.Location.reload();
				// reload, but remove hash if exists
				setLogoutUri();
			}

			public void onFailure(Throwable arg0) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						// Window.Location.reload();
						setLogoutUri();
					}
				};
				MessageBox.alert(messages.headerErrorWindow(), emr.getMessage(arg0, pmsMessages.msgErrorLogout()),
					listener).setModal(true);
			}
		};
		sessionsService.logout(callback);
	}
	
	private void setLogoutUri() {
		UrlBuilder url = Window.Location.createUrlBuilder().setHash(null);
		if(pmsUtil.isDisableLogin()){
			int index = url.buildString().lastIndexOf("/");
			Window.Location.replace(url.buildString().substring(0, index));
		} else {
			Window.Location.replace(url.buildString());
		}
	}

	/**
	 * @param sessionDto the sessionDto to set
	 */
	public void setSessionDto(SessionDTO sessionDto) {
		this.sessionDto = sessionDto;
	}
	
	public Label getLabelUsername() {
		return labelUsername;
	}
	
	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the sessions service proxy.<br/>
	 * @param sessionsService
	 */
	@Inject
	public void setSessionsService(ISessionsServiceAsync sessionsService) {
		this.sessionsService = sessionsService;
	}

	/**
	 * Injects the error message resolver for the sessions service.<br/>
	 * @param errorMessageResolver
	 */
	@Inject
	public void setErrorMessageResolver(SimpleErrorMessageResolver errorMessageResolver) {
		this.emr = errorMessageResolver;
	}
	
	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
	
	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
