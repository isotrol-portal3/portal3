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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckNodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.user.GlobalAuthorityDTO;
import com.isotrol.impe3.pms.api.user.GlobalRoleDTO;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserTemplateDTO;
import com.isotrol.impe3.pms.api.user.UsersService;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.GrantedAuthModelData;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.GrantedAuthoritiesTreeStore;
import com.isotrol.impe3.pms.gui.client.store.GrantedRolesTreeStore;
import com.isotrol.impe3.pms.gui.client.util.AuthoritiesTreeIconProvider;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * The user details window, with 3 tabs:<ul> <li>general info tab;</li> <li>Roles management tab;</li> <li>Authorities
 * management tab;</li> </ul>
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class UserDetails extends TypicalWindow implements IDetailPanel {

	/**
	 * <code>true</code> when check changes on the roles tree.<br/>
	 */
	private boolean rolesTabDirty = false;

	/**
	 * <code>true</code> when check changes on the authorities tree.<br/>
	 */
	private boolean authoritiesTabDirty = false;

	/**
	 * Bound user data<br/>
	 */
	private UserTemplateDTO userTemplate = null;

	/**
	 * Main tab panel.<br/>
	 */
	private TabPanel tpMain = null;

	/**
	 * <b>Username</b> field.<br/>
	 */
	private TextField<String> tfUserName = null;

	/**
	 * <b>Display name</b> field.<br/>
	 */
	private TextField<String> tfDisplayName = null;

	/**
	 * CheckBox for <b>root</b> property<br/>
	 */
	private CheckBox cbRoot = null;

	/**
	 * CheckBox for <b>active</b> property<br/>
	 */
	private CheckBox cbActive = null;

	/**
	 * CheckBox for <b>locked</b> property<br/>
	 */
	private CheckBox cbLocked = null;

	/**
	 * Change source components<br/>
	 */
	private List<Component> changeSourceComponents = null;

	/*
	 * Injected deps
	 */
	/**
	 * form layout helper.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Util bundle<br/>
	 */
	private Util util = null;

	/**
	 * Error message resolver for users service.<br/>
	 */
	private UsersServiceErrorMessageResolver emrUsers = null;

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages guiCommonMessages = null;

	/**
	 * The change events listening strategy.<br/>
	 */
	private PmsListeningStrategy eventsListeningStrategy = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStylesBundle = null;

	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles stylesBundle = null;

	/**
	 * Users service proxy.<br/>
	 */
	private IUsersServiceAsync usersService = null;

	/**
	 * Icon provider for the authorities tree.<br/>
	 */
	private AuthoritiesTreeIconProvider authoritiesTreeIconProvider = null;

	/**
	 * The Roles tree
	 */
	private TreePanel<DTOModelData<?>> tRoles = null;

	/**
	 * The Authorities tree
	 */
	private TreePanel<DTOModelData<?>> tAuthorities = null;

	/**
	 * Pms Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Sole constructor
	 */
	@Override
	protected void beforeRender() {
		
		assert userTemplate!=null : "userTemplate can be null";
		changeSourceComponents = new LinkedList<Component>();
		initThis();
		initComponents();
	}

	/**
	 * Inits the widget. Must be specifically called after properties injection.<br/>
	 */
	public void setUsertemplate(UserTemplateDTO dto) {
		this.userTemplate = dto;
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setClosable(false);
		setBodyBorder(false);
		setWidth(PmsConstants.DETAIL_WINDOW_WIDTH);
		setLayout(new FitLayout());

		String heading = null;
		if (userTemplate.getId() == null) {
			heading = pmsMessages.headerUserCreationPanel();
		} else {
			heading = pmsMessages.headerUserEditionPanel() + ": " + userTemplate.getName();
		}
		setHeading(heading);

		getHeader().addTool(buttonsSupport.createHelpToolButton(settings.pmsUsersAdminPortalManualUrl()));
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		tpMain = new TabPanel();
		tpMain.setBorders(false);
		tpMain.setBodyBorder(false);

		addGeneralInfoTab();
		addRolesTab();
		addAuthoritiesTab();
		addButtonBar();

		add(tpMain);
	}

	/**
	 * Adds the user general information tab (name, display name, root and active)<br/>
	 */
	private void addGeneralInfoTab() {
		LayoutContainer container = new LayoutContainer(formSupport.getStandardLayout());
		container.setBorders(false);
		container.setHeight(Constants.HUNDRED_PERCENT);
		container.addStyleName(stylesBundle.padding10px());
		// container.setScrollMode(Scroll.AUTOY);

		// 'Username' field:
		tfUserName = new TextField<String>();
		tfUserName.setFieldLabel(guiCommonMessages.labelUserNameField());
		formSupport.configRequired(tfUserName);
		tfUserName.setOriginalValue(userTemplate.getName());
		tfUserName.setValue(userTemplate.getName());

		container.add(tfUserName);
		changeSourceComponents.add(tfUserName);

		// 'Display name' field:
		tfDisplayName = new TextField<String>();
		tfDisplayName.setFieldLabel(guiCommonMessages.labelDisplayNameField());
		formSupport.configRequired(tfDisplayName);
		tfDisplayName.setOriginalValue(userTemplate.getDisplayName());
		tfDisplayName.setValue(userTemplate.getDisplayName());

		container.add(tfDisplayName);
		changeSourceComponents.add(tfDisplayName);

		// 'Is root' checkbox:
		cbRoot = new CheckBox();
		cbRoot.setFieldLabel(guiCommonMessages.labelRootField());
		cbRoot.addInputStyleName(stylesBundle.checkBoxAlignLeft());
		cbRoot.setValue(userTemplate.isRoot());

		container.add(cbRoot);
		changeSourceComponents.add(cbRoot);

		// 'Is active' checkbox:
		cbActive = new CheckBox();
		cbActive.setFieldLabel(guiCommonMessages.labelActivatedField());
		cbActive.addInputStyleName(stylesBundle.checkBoxAlignLeft());
		cbActive.setValue(userTemplate.isActive());

		container.add(cbActive);
		changeSourceComponents.add(cbActive);

		// 'Is locked' checkbox:
		cbLocked = new CheckBox();
		cbLocked.setFieldLabel(pmsMessages.labelLockedField());
		cbLocked.addInputStyleName(stylesBundle.checkBoxAlignLeft());
		cbLocked.setValue(userTemplate.isLocked());

		container.add(cbLocked);
		changeSourceComponents.add(cbLocked);

		addTab(pmsMessages.titleGeneralUserInfo(), container);
	}

	/**
	 * Adds the Roles tab item.
	 */
	private void addRolesTab() {
		LayoutContainer container = new LayoutContainer(new FitLayout());
		container.setScrollMode(Scroll.AUTOY);
		container.setBorders(false);
		container.setHeight(Constants.HUNDRED_PERCENT);

		tRoles = new TreePanel<DTOModelData<?>>(new GrantedRolesTreeStore(userTemplate.getRoles()));
		tRoles.setAutoLoad(true);
		tRoles.setCheckable(true);
		tRoles.setCheckNodes(CheckNodes.PARENT);
		tRoles.setDisplayProperty(GrantedAuthModelData.PROPERTY_DISPLAY_NAME);

		tRoles.setIconProvider(new ModelIconProvider<DTOModelData<?>>() {
			public AbstractImagePrototype getIcon(DTOModelData<?> model) {
				String iconStyle = null;
				if (model.getDTO() instanceof Granted<?>) {
					iconStyle = pmsStylesBundle.iconRole();
				} else {
					iconStyle = pmsStylesBundle.iconAuthorityGranted() + Constants.SPACE
						+ stylesBundle.marginLeft17px();
				}
				return IconHelper.create(iconStyle);
			}
		});

		tRoles.addListener(Events.Render, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent be) {
				applyRolesToGui();
			};
		});
		tRoles.addCheckListener(new CheckChangedListener<DTOModelData<?>>() {
			@Override
			public void checkChanged(CheckChangedEvent<DTOModelData<?>> event) {
				rolesTabDirty = true;
				Util.fireChangeEvent(tRoles);
			}
		});

		container.add(tRoles);
		changeSourceComponents.add(tRoles);

		addTab(pmsMessages.titleUserRoles(), container);
	}

	/**
	 * Applies the granted roles from the bound DTO to GUI.<br/>
	 */
	private void applyRolesToGui() {
		GrantedRolesTreeStore store = (GrantedRolesTreeStore) tRoles.getStore();
		List<DTOModelData<?>> checked = new LinkedList<DTOModelData<?>>();
		for (DTOModelData<?> model : store.getAllItems()) {
			if (model instanceof GrantedAuthModelData<?>) {
				GrantedAuthModelData<?> grantedModel = (GrantedAuthModelData<?>) model;
				Granted<?> granted = (Granted<?>) model.getDTO();
				if (granted.isGranted()) {
					checked.add(grantedModel);
				}
			}
		}

		tRoles.enableEvents(false);
		tRoles.setCheckedSelection(checked);
		tRoles.enableEvents(true);
	}

	/**
	 * Adds the authorities management tab.<br/>
	 */
	private void addAuthoritiesTab() {
		LayoutContainer container = new LayoutContainer(new FitLayout());
		container.setScrollMode(Scroll.AUTOY);
		container.setBorders(false);
		container.setHeight(Constants.HUNDRED_PERCENT);

		tAuthorities = new TreePanel<DTOModelData<?>>(new GrantedAuthoritiesTreeStore(userTemplate.getAuthorities()));
		tAuthorities.setAutoLoad(true);
		tAuthorities.setCheckable(true);
		tAuthorities.setCheckNodes(CheckNodes.PARENT);
		// tAuthorities.setCheckStyle(CheckCascade.CHILDREN);
		tAuthorities.setDisplayProperty(GrantedAuthModelData.PROPERTY_DISPLAY_NAME);
		tAuthorities.setIconProvider(authoritiesTreeIconProvider);

		tAuthorities.addListener(Events.Render, new Listener<TreePanelEvent<DTOModelData<?>>>() {
			public void handleEvent(TreePanelEvent<DTOModelData<?>> be) {
				applyAuthoritiesToGui();
			}
		});
		tAuthorities.addCheckListener(new CheckChangedListener<DTOModelData<?>>() {
			@Override
			public void checkChanged(CheckChangedEvent<DTOModelData<?>> event) {
				authoritiesTabDirty = true;
				Util.fireChangeEvent(tAuthorities);
			}
		});

		container.add(tAuthorities);
		changeSourceComponents.add(tAuthorities);

		addTab(pmsMessages.titleUserAuthorizations(), container);
	}

	/**
	 * Applies the granted authorizations from the bound DTO to GUI.<br/>
	 */
	private void applyAuthoritiesToGui() {

		// FIXME
		List<DTOModelData<?>> rootItems = tAuthorities.getStore().getRootItems();
		for (DTOModelData<?> model : rootItems) {
			tAuthorities.setLeaf(model, false);
			tAuthorities.setChecked(model, true);
		}

		GrantedAuthoritiesTreeStore store = (GrantedAuthoritiesTreeStore) tAuthorities.getStore();
		List<DTOModelData<?>> checked = new LinkedList<DTOModelData<?>>();
		for (DTOModelData<?> model : store.getAllItems()) {
			if (model instanceof GrantedAuthModelData<?>) {
				GrantedAuthModelData<?> grantedModel = (GrantedAuthModelData<?>) model;
				Granted<?> granted = (Granted<?>) model.getDTO();
				if (granted.isGranted()) {
					checked.add(grantedModel);
					tAuthorities.setChecked(model, true);
				} else {
					tAuthorities.setChecked(model, false);
				}
			}
		}

		/*
		 * tAuthorities.enableEvents(false); tAuthorities.setCheckedSelection(checked); tAuthorities.enableEvents(true);
		 */
	}

	/**
	 * Adds a new tab to the {@link #tpMain tab panel}, with the content (title and content widget) passed as args.<br/>
	 * @param title
	 * @param widget
	 */
	private void addTab(String title, Widget widget) {
		TabItem ti = new TabItem(title);
		ti.setLayout(new FitLayout());
		ti.setScrollMode(Scroll.AUTOY);
		ti.add(widget);
		tpMain.add(ti);
	}

	/**
	 * Adds the button bar to the bottom of the window.<br/>
	 */
	private void addButtonBar() {
		SelectionListener<ButtonEvent> lSave = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySave();
						}
					}
				};
				MessageBox.confirm(guiCommonMessages.headerConfirmWindow(), guiCommonMessages.msgConfirmSaveUser(),
					listener);
			}
		};
		Button bSave = buttonsSupport.createSaveButtonForDetailPanels(this, lSave, changeSourceComponents,
			eventsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Calls {@link UsersService#save(com.isotrol.impe3.pms.api.user.UserDTO)}.<br/> On success, shows an info message.
	 * On failure shows another info message.
	 */
	private void trySave() {

		util.mask(guiCommonMessages.mskSaveUser());

		applyGuiData();

		AsyncCallback<UserDTO> callback = new AsyncCallback<UserDTO>() {
			public void onSuccess(UserDTO arg0) {
				// buttonsSupport.closeActiveWindow();
				UserDetails.this.hide();
				util.unmask();
				util.info(guiCommonMessages.msgOkSaveUser());
			}

			public void onFailure(Throwable arg0) {
				util.unmask();
				MessageBox.alert(guiCommonMessages.headerErrorWindow(),
					emrUsers.getMessage(arg0, guiCommonMessages.msgErrorSaveUser()), null).setModal(true);
			}
		};
		usersService.save(userTemplate.toDTO(), callback);
	}

	/**
	 * Passes data from GUI to bound DTO.<br/>
	 */
	@SuppressWarnings("unchecked")
	private void applyGuiData() {
		// simple fields:
		userTemplate.setName(tfUserName.getValue());
		userTemplate.setDisplayName(tfDisplayName.getValue());
		userTemplate.setActive(cbActive.getValue());
		userTemplate.setRoot(cbRoot.getValue());
		userTemplate.setLocked(cbLocked.getValue());

		// roles:
		List<GlobalRole> lGrantedRoles = new LinkedList<GlobalRole>();
		for (DTOModelData<?> model : tRoles.getCheckedSelection()) {
			if (model.getDTO() instanceof Granted<?>) {
				Granted<GlobalRoleDTO> grantedRole = (Granted<GlobalRoleDTO>) model.getDTO();
				lGrantedRoles.add(grantedRole.get().getRole());
			}
		}
		for (Granted<GlobalRoleDTO> granted : userTemplate.getRoles()) {
			boolean isGranted = false;
			if (lGrantedRoles.contains(granted.get().getRole())) {
				isGranted = true;
			}
			granted.setGranted(isGranted);
		}

		// authorities:
		List<GlobalAuthority> lGrantedAuthorities = new LinkedList<GlobalAuthority>();
		for (DTOModelData<?> model : tAuthorities.getCheckedSelection()) {
			if (model.getDTO() instanceof Granted<?>) {
				Granted<GlobalAuthorityDTO> grantedAuthority = (Granted<GlobalAuthorityDTO>) model.getDTO();
				lGrantedAuthorities.add(grantedAuthority.get().getAuthority());
			}
		}
		for (Granted<GlobalAuthorityDTO> granted : userTemplate.getAuthorities()) {
			boolean isGranted = false;
			if (lGrantedAuthorities.contains(granted.get().getAuthority())) {
				isGranted = true;
			}
			granted.setGranted(isGranted);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return isGeneralInfoTabDirty() || isRolesTabDirty() || isAuthorizationsTabDirty();
	}

	/**
	 * @return <code>true</code>, if the general info tab is dirty;<code>false</code>, otherwise.
	 */
	private boolean isGeneralInfoTabDirty() {
		return tfUserName.isDirty() || tfDisplayName.isDirty() || cbRoot.isDirty() || cbActive.isDirty()
			|| cbLocked.isDirty();
	}

	/**
	 * @return <code>true</code>, if roles tree check changed; <code>false</code>otherwise.
	 */
	private boolean isRolesTabDirty() {
		if (!tRoles.isRendered()) {
			return false;
		}
		return rolesTabDirty;
	}

	/**
	 * <br/>
	 * @return <code>true</code>, if authorities tree check changed; <code>false</code>otherwise.
	 */
	private boolean isAuthorizationsTabDirty() {
		if (!tAuthorities.isRendered()) {
			return false;
		}
		return authoritiesTabDirty;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return userTemplate.getId() != null;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		return isGeneralInfoTabValid() && isRolesTabValid() && isAuthorizationTabValid();
	}

	/**
	 * @return
	 */
	private boolean isGeneralInfoTabValid() {
		return tfUserName.isValid() && tfDisplayName.isValid() && cbRoot.isValid() && cbActive.isValid()
			&& cbLocked.isValid();
	}

	/**
	 * @return always <code>true</code>
	 */
	private boolean isRolesTabValid() {
		return true;
	}

	/**
	 * @return always <code>true</code>
	 */
	private boolean isAuthorizationTabValid() {
		return true;
	}

	/**
	 * Injects the form layout helper.<br/>
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the PMS specific message bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the generic messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.guiCommonMessages = messages;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setEventsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.eventsListeningStrategy = pmsListeningStrategy;
	}

	/**
	 * Injects the PMS specific styles bundle.<br/>
	 * @param pmsStylesBundle
	 */
	@Inject
	public void setPmsStylesBundle(PmsStyles pmsStylesBundle) {
		this.pmsStylesBundle = pmsStylesBundle;
	}

	/**
	 * @param stylesBundle the stylesBundle to set
	 */
	@Inject
	public void setStylesBundle(GuiCommonStyles stylesBundle) {
		this.stylesBundle = stylesBundle;
	}

	/**
	 * @param utilities the util to set
	 */
	@Inject
	public void setUtils(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the error message resolver
	 * @param emrUsers the emrUsers to set
	 */
	@Inject
	public void setEmrUsers(UsersServiceErrorMessageResolver emrUsers) {
		this.emrUsers = emrUsers;
	}

	/**
	 * Injects the users service.
	 * @param usersService the usersService to set
	 */
	@Inject
	public void setUsersService(IUsersServiceAsync usersService) {
		this.usersService = usersService;
	}

	/**
	 * Injects the icon provider for the authorities tree.
	 * @param authoritiesTreeIconProvider the authoritiesTreeIconProvider to set
	 */
	@Inject
	public void setAuthoritiesTreeIconProvider(AuthoritiesTreeIconProvider authoritiesTreeIconProvider) {
		this.authoritiesTreeIconProvider = authoritiesTreeIconProvider;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
