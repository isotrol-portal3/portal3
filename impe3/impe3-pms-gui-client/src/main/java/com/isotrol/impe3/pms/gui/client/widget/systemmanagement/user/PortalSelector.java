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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.store.PortalTreeStore;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;

/**
 * A portal selector that displays a tree with available portals.
 * 
 * @author Andrei Cojocaru
 *
 */
public class PortalSelector extends TypicalWindow implements IInitializableWidget {

	/**
	 * Listener configured from the class that instantiates this widget.<br/>
	 */
	private IPortalSelectionListener portalSelectionListener = null;
	
	/**
	 * Changes to <code>true</code> after calling {@link #init(IPortalSelectionListener)}<br/>
	 */
	private boolean initialized = false;

	/**
	 * "Accept" button.<br/>
	 */
	private Button bAccept = null;

	/**
	 * The portals tree.
	 */
	private TreePanel<PortalSelModelData> tree = null;
	
	/*
	 * Injected deps
	 */
	/**
	 * Styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;
	/**
	 * Error message resolver for exceptions thrown in portals service.<br/>
	 */
	private PortalsServiceErrorMessageResolver errorMessageResolver = null;
	/**
	 * Buttons helper.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Portals async service proxy.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;
	
	/**
	 * General messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init(java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	public PortalSelector init() {
		initThis();
		initComponents();
		
		tryGetPortals();
		
		return this;
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setHeadingText(pmsMessages.headerPortalSelector());
		setWidth(Constants.SMALL_WINDOW_WIDTH);
		setClosable(true);
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTOY);
	}

	/**
	 * Inits this panel inner components.<br/>
	 */
	private void initComponents() {
		addTree();
		addButtons();
	}

	/**
	 * Adds the portals tree.<br/>
	 */
	private void addTree() {
		tree  = new TreePanel<PortalSelModelData>(new PortalTreeStore());
		
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.getStyle().setLeafIcon(IconHelper.create(styles.iconTreeFolder()));
		tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<PortalSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<PortalSelModelData> se) {
				boolean enabled = true;
				if (se.getSelectedItem() == null) {
					enabled = false;
				}
				bAccept.setEnabled(enabled);
			}
		});
		
		tree.setDisplayProperty(PortalSelModelData.PROPERTY_NAME);
		
		add(tree);
	}


	/**
	 * Adds an "Accept" button to the button bar, and a "Refresh" button to the header.<br/>
	 */
	private void addButtons() {
		// "Accept" button:
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				hide();
				PortalSelModelData portalModelData = tree.getSelectionModel().getSelectedItem();
				portalSelectionListener.portalSelected(portalModelData.getDTO());
			}
		};
		bAccept = buttonsSupport.createAcceptButton(listener);
		bAccept.disable();
		addButton(bAccept);
		
		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPortals();
			}
		};
		buttonsSupport.addRefreshButton(this, lRefresh);
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
	
	/**
	 * Populates the tree store with the passed portal tree.<br/>
	 * Previously removes all elements in the store.
	 * @param rootDto portals tree.
	 */
	private void populateTree(PortalTreeDTO rootDto) {
		PortalTreeStore store = (PortalTreeStore) tree.getStore();
		store.removeAll();
		for (PortalTreeDTO dto : rootDto.getChildren()) {
			store.add(dto, true);
		}
	}

	/**
	 * Injects the error message resolver for the portals service.<br/>
	 * @param emr
	 */
	@Inject
	public void setErrorMessageResolver(PortalsServiceErrorMessageResolver emr) {
		this.errorMessageResolver = emr;
	}

	/**
	 * Injects the buttons helper object.<br/>
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the portals async service proxy.<br/>
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * Injects the messages bundle.<br/>
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the pms messages bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
	
	/**
	 * Injects the styles bundle.
	 * @param s the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(GuiCommonStyles s) {
		this.styles = s;
	}
	
	/**
	 * Inserts the portal selection callback.
	 * @param portalSelectionListener the portalSelectionListener to set
	 */
	public void setPortalSelectionListener(IPortalSelectionListener portalSelectionListener) {
		this.portalSelectionListener = portalSelectionListener;
	}
	
	/**
	 * Requests the server for the portals.<br/>
	 */
	private void tryGetPortals() {
		AsyncCallback<PortalTreeDTO> callback = new AsyncCallback<PortalTreeDTO>() {
			public void onSuccess(PortalTreeDTO arg0) {
				populateTree(arg0);
			}
			public void onFailure(Throwable arg0) {
				MessageBox.alert(
					messages.headerErrorWindow(),
					errorMessageResolver.getMessage(arg0, pmsMessages.msgErrorGetPortals()), 
					null).setModal(true);
			}
		};
		portalsService.getPortals(callback);
	}
	
	/**
	 * Callback interface for portal selection.
	 * 
	 * @author Andrei Cojocaru
	 */
	public interface IPortalSelectionListener {
		/**
		 * Fired when a portal is selected.<br/>
		 * @param psDto
		 */
		void portalSelected(PortalSelDTO psDto); 
	}

}
