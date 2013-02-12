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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.parent;


import java.util.Arrays;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.portal.PortalParentDTO;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.PortalSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.PortalTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;


/**
 * Shows the portals tree and enables change the current portal's parent
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class ParentPortalManagement extends PmsContentPanel {

	private static final String ID_FICTITIOUS_NODE = "root";

	/**
	 * The Categories tree.<br/>
	 */
	private TreePanel<PortalSelModelData> tree = null;
	/**
	 * Store for {@link #tree}. It is also bound to the {@link #filter}.<br/>
	 */
	private PortalTreeStore store = null;

	// Tool items we should care enabling/disabling
	/**
	 * "Set parent" toolbar button.<br/>
	 */
	private Button bSetParent = null;

	/**
	 * Fitler for the tree elements.<br/>
	 */
	private CustomizableStoreFilter<PortalSelModelData> filter = null;

	/**
	 * Current portal's id
	 */
	private String portalId = null;

	/**
	 * Current portal's parent
	 */
	private PortalSelDTO parent = null;

	/*
	 * Injected deps
	 */
	/**
	 * Portals async service proxy.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Generic messages service.
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS specific styles service.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Static objects container<br/>
	 */
	private Util util = null;

	/**
	 * Error Message Resolver for Portals service.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Constructor
	 */
	public ParentPortalManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are set.
	 */
	public void init(String portalId) {
		this.portalId = portalId;

		configThis();
		initController();

		addToolBar();

		tryGetPortals();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the portals controller. All change events fired by the
	 * controller will refresh the {@link #tree}.<br/>
	 */
	private void initController() {
		ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.UPDATE:
							if (isVisible()) {
								tryGetPortals();
							}
							break;
						default: // shouldn't happen..
							// Logger.getInstance().log(
							// "Unexpected event descriptor for a ChangeEventSource instance :" + event.type);
					}
				}
			}
		};
		PortalsController portalsController = (PortalsController) portalsService;
		portalsController.addChangeListener(controllerListener);
	}

	/**
	 * Configures this component properties.<br/>
	 */
	private void configThis() {
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTO);
		setLayoutOnChange(true);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		bSetParent = buttonsSupport.addGenericButton(pmsMessages.labelSetParent(), pmsStyles.iconPortalParent(),
			toolBar, new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent tbe) {
					final PortalSelModelData item = tree.getSelectionModel().getSelectedItem();
					if (item != null) {
						Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
							public void handleEvent(MessageBoxEvent be) {
								if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
									trySetParent(item.getDTO().getId());
								}
							}
						};
						MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmSetParent(), lConfirm);
					}
				}
			});
		bSetParent.disable();
		toolBar.add(new FillToolItem());

		// Filter:
		filter = new CustomizableStoreFilter<PortalSelModelData>(Arrays
			.asList(new String[] {PortalSelModelData.PROPERTY_NAME}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPortals();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		setTopComponent(toolBar);
	}

	/**
	 * If exists, removes the category tree and recreates it from scratch.<br/>
	 * @param portalDto
	 */
	private void addTreePanel(PortalTreeDTO portalDto) {

		if (store != null) {
			filter.unbind(store);
		}

		// create a fictitious node
		PortalSelDTO root = new PortalSelDTO();
		root.setId(ID_FICTITIOUS_NODE);
		root.setName(pmsMessages.nodeParentPortalRoot());
		portalDto.setNode(root);
		store = new PortalTreeStore(portalDto);
		// for (PortalTreeDTO child : portalDto.getChildren()) {
		// store.add(child, true);
		// }

		filter.setValue(null);
		filter.bind(store);
		tree = new TreePanel<PortalSelModelData>(store);
		tree.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));

		tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<PortalSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<PortalSelModelData> se) {
				PortalSelModelData selected = tree.getSelectionModel().getSelectedItem();
				boolean enabled = false;
				String idSelected = selected.getDTO().getId();
				if (selected != null
					&& ((parent == null && !idSelected.equals(ID_FICTITIOUS_NODE)) || (parent != null && !parent
						.getId().equals(idSelected)))) {
					enabled = true;
				}
				bSetParent.setEnabled(enabled);
			}
		});

		tree.setDisplayProperty(PortalSelModelData.PROPERTY_NAME);

		add(tree);
	}

	/**
	 * Repopulates the tree with the passed portal tree.<br/>
	 * 
	 * @param portal
	 */
	private void repopulateTree(PortalParentDTO portal) {

		// destroy & create the tree:
		if (tree != null && tree.isAttached()) {
			tree.removeFromParent();
		}
		addTreePanel(portal.getPortalTree());
		// select the portal parent
		PortalSelModelData item = null;
		if (parent != null) {
			item = tree.getStore().findModel(PortalSelModelData.PROPERTY_ID, parent.getId());
			tree.getSelectionModel().select(false, item);
		} else {
			item = tree.getStore().getRootItems().get(0);
			tree.getSelectionModel().select(false, item);
		}
		// expand the portal parent
		tree.setExpanded(item, true);
	}

	/**
	 * Retrieves portals from service and displays them in the portals tree.<br/>
	 */
	private void tryGetPortals() {

		util.mask(pmsMessages.mskPortals());

		AsyncCallback<PortalParentDTO> callback = new AsyncCallback<PortalParentDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrPortals, pmsMessages.msgErrorGetPortals());
			}

			public void onSuccess(PortalParentDTO portal) {
				parent = portal.getParent();
				repopulateTree(portal);
				util.unmask();
			}
		};

		portalsService.getParent(portalId, callback);
	}

	/**
	 * Try change parent for current portal
	 * @param id new parent id
	 */
	private void trySetParent(String id) {
		String parentId = id;
		// is root node is selected, delete parent for current portal
		if (id.equals(ID_FICTITIOUS_NODE)) {
			parentId = null;
		}

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				util.info(pmsMessages.msgSuccessSetParentPortal());
			}

			public void onFailure(Throwable caught) {
				errorProcessor.processError(caught, emrPortals, pmsMessages.msgErrorSetParentPortal());
			}
		};
		portalsService.setParent(portalId, parentId, callback);
	}

	/**
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the PMS specific style bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the buttons helper object.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the static objects container
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * @param portalId the portalId to set
	 */
	public void setPortalId(String portalId) {
		this.portalId = portalId;
	}

	/**
	 * @param portalsService the portalsService to set
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * Injects the Error Message Resolver for Portals service.
	 * @param emrPortals the emrPortals to set
	 */
	@Inject
	public void setEmrPortals(PortalsServiceErrorMessageResolver emrPortals) {
		this.emrPortals = emrPortals;
	}

	/**
	 * @param errorProcessor the error processor.
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
}
