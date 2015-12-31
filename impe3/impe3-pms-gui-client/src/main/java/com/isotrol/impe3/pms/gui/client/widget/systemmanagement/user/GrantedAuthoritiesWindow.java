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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.common.util.Settings;

/**
 * Window that shows the granted authorities for the configured user,
 * and a checkbox for the user <b>root</b> state.
 * 
 * @author Andrei Cojocaru
 *
 */
public class GrantedAuthoritiesWindow extends TypicalWindow {
	
	/**
	 * User summary info.<br/>
	 */
	private UserSelDTO userDto = null;
	
	/**
	 * Authorities display names map.<br/>
	 */
	private Map<GlobalAuthority, String> authorities = null;

	/**
	 * "Is root" checkbox.<br/>
	 */
	private CheckBox cbRoot = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	/**
	 * Message resolver for errors thrown by users remote service.<br/>
	 */
	private UsersServiceErrorMessageResolver emrUsers = null;
	/**
	 * Users service proxy.<br/>
	 */
	private IUsersServiceAsync usersSservice = null;
	
	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;
	
	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * Generic settings bundle.
	 */
	private Settings settings = null;
	
	/**
	 * Pms specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;
	
	/**
	 * Default consturctor
	 */
	public GrantedAuthoritiesWindow() {}
	
	/**
	 * Initializes this widget. Must be called after properties injection.<br/>
	 * @param uDto user summary info
	 * @param authoritiesNames Map structure that associates authorities on their display names. 
	 * @return this widget initialized.
	 */
	public GrantedAuthoritiesWindow init(UserSelDTO uDto, Map<GlobalAuthority, String> authoritiesNames) {
		
		userDto = uDto;
		authorities = authoritiesNames;
		
		initThis();
		initComponents();
		tryGetGranted();
		
		return this;
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setHeadingText(pmsMessages.headerUserAuthorities(userDto.getDisplayName()));
		setClosable(true);
		setLayout(new RowLayout());
		setLayoutOnChange(true);
		setWidth(Constants.SMALL_WINDOW_WIDTH);
		setScrollMode(Scroll.AUTOY);
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		
		// "Root" flag:
		LayoutContainer container = new LayoutContainer(new FormLayout());
		container.addStyleName(styles.margin10px());
		container.setAutoHeight(true);
		add(container);
		
		cbRoot = new CheckBox();
		cbRoot.setReadOnly(true);
		cbRoot.setFieldLabel(messages.labelRootField());
		cbRoot.addInputStyleName(styles.checkBoxAlignLeft());
		container.add(cbRoot);
		
		// "Accept" button:
		addButton(buttonsSupport.createAcceptButton(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		}));
		
	}

	/**
	 * Requests the service for the granted authorities.<br/>
	 * On success, populates the authorities data list.
	 * On failure, shows an alert message according to the caught exception type.
	 */
	private void tryGetGranted() {
		AsyncCallback<AuthorizationDTO> callback = new AsyncCallback<AuthorizationDTO>() {
			public void onSuccess(AuthorizationDTO arg0) {
				showAuthoritiesInfo(arg0);
			}
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrUsers, pmsMessages.msgErrorGetGranted());
			}
		};
		usersSservice.getGranted(userDto.getId(), callback);
	}
	
	/**
	 * Shows retrieved authorities info:<br/>
	 * <ul>
	 * <li>if the authorities set is empty, a message is displayed</li>
	 * <li>otherwise, a list is displayed with the associated authorities.</li>
	 * </ul>
	 * @param dto
	 */
	private void showAuthoritiesInfo(AuthorizationDTO dto) {
		cbRoot.setValue(dto.isRoot());
		Widget widget = null;
		if (dto.getAuthorities().isEmpty()) {
			// create and show a label:
			Label fNoAuthorities = new Label(pmsMessages.msgUserHasNoAuthorities());
			fNoAuthorities.addStyleName(styles.labelInfoMessage());
			widget = fNoAuthorities;
		} else {
			// create, populate and show a data list:
			ListView<MenuItemModelData> lvAuthorities = new ListView<MenuItemModelData>();
			lvAuthorities.setSimpleTemplate(settings.tplListView());
			lvAuthorities.setStore(new ListStore<MenuItemModelData>());
			lvAuthorities.setBorders(false);
			lvAuthorities.setAutoHeight(true);
			lvAuthorities.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
			List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
			for (GlobalAuthority authority : dto.getAuthorities()) {
				MenuItemModelData model = new MenuItemModelData(
						authorities.get(authority), 
						pmsStyles.iconAuthorityGranted(), 
						null);
				lModels.add(model);
			}
			lvAuthorities.getStore().add(lModels);
			widget = lvAuthorities;
		}
		widget.addStyleName(styles.marginLeft10px());
		add(widget);
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
	 * Injects the error messages resolver for the users service.<br/>
	 * @param emrUsers
	 */
	@Inject
	public void setEmrUsers(UsersServiceErrorMessageResolver emrUsers) {
		this.emrUsers = emrUsers;
	}

	/**
	 * Injects the users service proxy.<br/>
	 * @param usersSservice
	 */
	@Inject
	public void setUsersSservice(IUsersServiceAsync usersSservice) {
		this.usersSservice = usersSservice;
	}

	/**
	 * Injects the buttons support.<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
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
	 * Injects the PMS specific styles bundle.<br/>
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
	
	/**
	 * Injects the generic styles bundle.
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
	
	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
	
	/**
	 * Injects the settings bundle.
	 * @param settings
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
