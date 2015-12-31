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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckCascade;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.CheckNodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.api.user.PortalAuthoritiesTemplateDTO;
import com.isotrol.impe3.pms.api.user.PortalAuthorityDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.AuthModelData;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.store.PortalAuthoritiesTreeStore;
import com.isotrol.impe3.pms.gui.client.util.AuthoritiesTreeIconProvider;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;

/**
 * Manages the User Authorities per portal.
 * 
 * @author Andrei Cojocaru
 *
 */
public class PortalAuthoritiesWindow extends TypicalWindow implements IDetailPanel, IInitializableWidget {

	/**
	 * Represents the <code>dirty</code> state. Once changed, 
	 * it cannot get back to its initial <code>false</code> value.<br/>
	 */
	private boolean dirty = false;
	
	/**
	 * Defaults to <code>false</code>. Changes to <code>true</code> once {@link #init(String)} is called.<br/>
	 */
	private boolean initialized = false;

	/**
	 * Bound user data.<br/>
	 */
	private UserSelDTO userSelDto = null;
	
	/**
	 * Bound portal data.<br/>
	 */
	private PortalSelDTO portalSelDto = null;
	
	/**
	 * Authorities names map.<br/>
	 */
	private Map<PortalAuthority, String> authoritiesNames = null;
	
	/**
	 * Portal authorities DTO built from the {@link #authoritiesNames authorities names map}
	 * and the granted authorities retrieved from server.<br/>
	 */
	private PortalAuthoritiesTemplateDTO authoritiesTemplateDto = null;

	/**
	 * the Authorities tree
	 */
	private TreePanel<DTOModelData<?>> tree = null;
	
	/*
	 * Injected deps.
	 */
	/**
	 * Util helper<br/>
	 */
	private Util util = null;
	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;
	/**
	 * General messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	/**
	 * Pms specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	/**
	 * Proxy for the users service async service.<br/>
	 */
	private IUsersServiceAsync usersService = null;
	/**
	 * Error message resolver for exceptions thrown by users service.<br/>
	 */
	private UsersServiceErrorMessageResolver emr = null;
	/**
	 * Icon provider for portal authorities tree.<br/>
	 */
	private AuthoritiesTreeIconProvider authoritiesTreeIconProvider = null;
	/**
	 * Pms events listening strategy.<br/>
	 */
	private PmsListeningStrategy eventsListeningStrategy = null;
	
	/**
	 * Default constructor.
	 */
	public PortalAuthoritiesWindow() {}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init(java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	public PortalAuthoritiesWindow init() {
		assert userSelDto != null : getClass().getName() + " must be initialized with user data";
		assert portalSelDto != null : getClass().getName() + " must be initialized with portal data";
		assert authoritiesNames != null : getClass().getName() + " must be initialized with authorities names data";
		
		initialized = true;
		
		initThis();
		initComponents();
		
		tryGetPortalAuthorities();
		
		return this;
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setHeadingText(pmsMessages.headerPortalAuthorities(userSelDto.getDisplayName(), portalSelDto.getName()));
		setWidth(Constants.SMALL_WINDOW_WIDTH);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);
		setClosable(false);
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		addTree();
		addButtons();
	}

	/**
	 * Adds the buttons to the button bar.<br/>
	 */
	private void addButtons() {
		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySetPortalAuthorities();
						}
					}
				};
				MessageBox.confirm(
					messages.headerConfirmWindow(), 
					pmsMessages.msgConfirmSetPortalAuthorities(), 
					listener).setModal(true);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(
			this,
			lAccept,
			Arrays.asList(new Component [] {tree}),
			eventsListeningStrategy);
		addButton(bAccept);
		
		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds the authorities tree.<br/>
	 */
	private void addTree() {
		tree = new TreePanel<DTOModelData<?>>(new PortalAuthoritiesTreeStore()) {
			@Override
			protected boolean hasChildren(DTOModelData<?> model) {
				if(model.getDTO() instanceof Granted<?>) {
					return true;
				}
				return false;
			}
		};
		
		tree.setCheckable(true);
		tree.setCheckStyle(CheckCascade.NONE);
		tree.setCheckNodes(CheckNodes.PARENT);
		
		tree.addCheckListener(new CheckChangedListener<DTOModelData<?>>() {
			@Override
			public void checkChanged(CheckChangedEvent<DTOModelData<?>> event) {
				dirty = true;
				Util.fireChangeEvent(tree);
			}
		});
		
		tree.setDisplayProperty(AuthModelData.PROPERTY_DISPLAY_NAME);
		tree.setIconProvider(authoritiesTreeIconProvider);
		
		add(tree);
	}

	/**
	 * @param userSelDto the userSelDto to set
	 */
	public void setUserSelDto(UserSelDTO userSelDto) {
		this.userSelDto = userSelDto;
	}
	
	/**
	 * @param portalSelDto the portalSelDto to set
	 */
	public void setPortalSelDto(PortalSelDTO portalSelDto) {
		this.portalSelDto = portalSelDto;
	}
	
	/**
	 * @param authoritiesNames the authoritiesNames to set
	 */
	public void setAuthoritiesNames(Map<PortalAuthority, String> authoritiesNames) {
		this.authoritiesNames = authoritiesNames;
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	/**
	 * <br/>
	 */
	public boolean isDirty() {
		return dirty;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	/**
	 * Always edition.<br/>
	 */
	public boolean isEdition() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	/**
	 * Always valid.<br/>
	 */
	public boolean isValid() {
		return true;
	}

	/**
	 * Applies the values from GUI to bound template DTO.<br/>
	 */
	@SuppressWarnings("unchecked")
	private void applyGuiValues() {
		List<DTOModelData<?>> checkedModels = tree.getCheckedSelection();
		List<PortalAuthority> checkedAuthorities = new LinkedList<PortalAuthority>();
		for (DTOModelData<?> model : checkedModels) {
			if (model.getDTO() instanceof Granted<?>) {
				Granted<PortalAuthorityDTO> grantedDto = (Granted<PortalAuthorityDTO>) model.getDTO();
				checkedAuthorities.add(grantedDto.get().getAuthority());
			}
		}
		
		for (Granted<PortalAuthorityDTO> granted : authoritiesTemplateDto.getAuthorities()) {
			boolean isGranted = false;
			if (checkedAuthorities.contains(granted.get().getAuthority())) {
				isGranted = true;
			}
			granted.setGranted(isGranted);
		}
	}

	/**
	 * Builds the set of granted portal authorities, and saves it in the remote service.<br/>
	 */
	private void trySetPortalAuthorities() {
		util.mask(pmsMessages.mskAuthorities());
		applyGuiValues();
		
		Set<PortalAuthority> authorities = authoritiesTemplateDto.toSet();
		
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onSuccess(Void arg0) {
				hide();
				util.unmask();
				util.info(pmsMessages
					.msgOkSetPortalAuthorities(userSelDto.getName(), portalSelDto.getName()));
			}
			public void onFailure(Throwable arg0) {
				util.unmask();
				MessageBox.alert(
					messages.headerErrorWindow(), 
					emr.getMessage(arg0, pmsMessages.msgErrorSetPortalAuthorities()),
					null).setModal(true);
			}
		};
		
		usersService.setPortalAuthorities(userSelDto.getId(), portalSelDto.getId(), authorities, callback);
	}
	
	/**
	 * Retrieves from service the Portal Authorities set, and creates the corresponding template DTO.<br/>
	 * Then, populates the Portal Authorities tree with the built DTO.
	 */
	private void tryGetPortalAuthorities() {
		
		AsyncCallback<Set<PortalAuthority>> callback = new AsyncCallback<Set<PortalAuthority>>() {
			public void onSuccess(Set<PortalAuthority> arg0) {
				authoritiesTemplateDto = new PortalAuthoritiesTemplateDTO(arg0, authoritiesNames);
				populateTree();
			}
			public void onFailure(Throwable arg0) {
				MessageBox.alert(
					messages.headerErrorWindow(),
					emr.getMessage(arg0, pmsMessages.msgErrorGetPortalAuthorities()), 
					null).setModal(true);
			}
		};
		usersService.getPortalAuthorities(userSelDto.getId(), portalSelDto.getId(), callback);
	}
	
	/**
	 * Builds a template DTO with the passed authorities set, and populates the tree with it.<br/>
	 */
	private void populateTree() {
		PortalAuthoritiesTreeStore store = (PortalAuthoritiesTreeStore) tree.getStore();
		store.load(authoritiesTemplateDto.getAuthorities());
		
		// checked nodes:
		List<DTOModelData<?>> checked = new LinkedList<DTOModelData<?>>();
		for (DTOModelData<?> model : store.getAllItems()) {
			if (model.getDTO() instanceof Granted<?>) {
				Granted<?> grantedDto = (Granted<?>) model.getDTO();
				if (grantedDto.isGranted()) {
					checked.add(model);
				}
			}
		}
		
		tree.enableEvents(false);
		tree.setCheckedSelection(checked);
		tree.enableEvents(true);
	}
	
	/**
	 * Injects the icon provider for the authorities tree.
	 * @param iconProvider the authoritiesTreeIconProvider to set
	 */
	@Inject
	public void setAuthoritiesTreeIconProvider(AuthoritiesTreeIconProvider iconProvider) {
		this.authoritiesTreeIconProvider = iconProvider;
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
	 * Injects the PMS specific messages.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the users service proxy.<br/>
	 * @param usersService
	 */
	@Inject
	public void setUsersService(IUsersServiceAsync usersService) {
		this.usersService = usersService;
	}

	/**
	 * Injects the error message resolver for users service exceptions.<br/>
	 * @param e
	 */
	@Inject
	public void setErrorMessageResolver(UsersServiceErrorMessageResolver e) {
		this.emr = e;
	}
	
	/**
	 * Injects the buttons helper object.
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}
	
	/**
	 * Injects the PMS events listening strategy.
	 * @param strat the eventsListeningStrategy to set
	 */
	@Inject
	public void setEventsListeningStrategy(PmsListeningStrategy strat) {
		this.eventsListeningStrategy = strat;
	}
	
	/**
	 * Injects the statics
	 * @param utilities the util to set
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}
}
