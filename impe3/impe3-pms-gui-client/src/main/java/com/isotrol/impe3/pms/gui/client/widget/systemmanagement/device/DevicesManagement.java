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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceSelDTO;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.DevicesController;
import com.isotrol.impe3.pms.gui.client.data.impl.DeviceSelModelData;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.DeviceTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;


/**
 * Shows the devices management tree panel
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class DevicesManagement extends PmsContentPanel {

	/**
	 * Key used in GXT for mapping models that don't implement TreeModel (DeviceSelModelData) into a BaseTreeModel.<br/>
	 */
	private static final String KEY_MODEL = "model";

	/**
	 * Detail view as popup window.<br/>
	 */
	private Window wDetail = null;

	/**
	 * The Devices tree.<br/>
	 */
	private TreePanel<DeviceSelModelData> tree = null;
	/**
	 * Store for {@link #tree}. It is also bound to the {@link #filter}.<br/>
	 */
	private DeviceTreeStore store = null;

	// Tool items we should care enabling/disabling
	/**
	 * "Edit" toolbar button.<br/>
	 */
	private Button bEdit = null;
	/**
	 * "Delete" toolbar button.<br/>
	 */
	private Button bDelete = null;

	/**
	 * Fitler for the tree elements.<br/>
	 */
	private CustomizableStoreFilter<DeviceSelModelData> filter = null;

	/*
	 * Injected deps
	 */
	/**
	 * Devices async service proxy.<br/>
	 */
	private IDevicesServiceAsync devicesService = null;

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
	 * Error Message Resolver service.<br/>
	 */
	private SimpleErrorMessageResolver emrSimple = null;

	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Constructor
	 */
	public DevicesManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are set.
	 */
	public void init() {
		configThis();
		initController();

		addToolBar();

		tryGetDevices();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the devices controller. All change events fired by the
	 * controller will refresh the {@link #tree}.<br/>
	 */
	private void initController() {
		ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.ADD: // ask for all devices:
							tryGetDevices(); // internally calls configDND()
							break;
						case PmsChangeEvent.DELETE:
							repopulateTree((List<DeviceTreeDTO>) event.getEventInfo());
							break;
						case PmsChangeEvent.UPDATE:
							DeviceDTO cDto = event.getEventInfo();
							List<DeviceSelModelData> models = store.findModels(DeviceSelModelData.PROPERTY_ID, cDto
								.getId());
							boolean found = !models.isEmpty();
							if (found) { // update writable properties on the ModelData,
								// and update view
								DeviceSelModelData model = models.get(0);
								DeviceSelDTO csDto = model.getDTO();
								csDto.setName(cDto.getName());

								store.update(model);
								// item.setText((String) csModelData.get(treeBinder.getDisplayProperty()));
							}
							break;
						default: // shouldn't happen..
							// Logger.getInstance().log(
							// "Unexpected event descriptor for a ChangeEventSource instance :" + event.type);
					}
				}
			}
		};
		DevicesController devController = (DevicesController) devicesService;
		devController.addChangeListener(controllerListener);
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

		buttonsSupport.addAddButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				DeviceSelModelData parent = tree.getSelectionModel().getSelectedItem();
				if (parent == null) {
					parent = tree.getStore().getRootItems().get(0);
				}
				if (parent != null) {
					showDeviceCreationPanel(parent.getDTO(), tree.getStore().getChildCount(parent));
				}
			}
		}, null);

		toolBar.add(new SeparatorToolItem());

		bDelete = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				DeviceSelModelData item = tree.getSelectionModel().getSelectedItem();
				if (item != null) {
					final DeviceSelDTO csDto = item.getDTO();
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
								tryDeleteDevice(csDto);
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteDevice(csDto
						.getName()), lConfirm);
				}
			}
		}, null);
		bDelete.disable();

		toolBar.add(new SeparatorToolItem());

		bEdit = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				getSelectedAndDisplayDetails();
			}
		});
		bEdit.disable();

		toolBar.add(new FillToolItem());

		// Filter:
		filter = new CustomizableStoreFilter<DeviceSelModelData>(Arrays
			.asList(new String[] {DeviceSelModelData.PROPERTY_NAME}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				bDelete.disable();
				bEdit.disable();
				tryGetDevices();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);

		setTopComponent(toolBar);
	}

	/**
	 * Retrieves from service the selected Device details data, and displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		DeviceSelModelData selected = tree.getSelectionModel().getSelectedItem();
		if (selected != null) {
			tryGetDeviceDetails(selected.getDTO());
		}
	}

	/**
	 * Requests the remote service for the passed device full info.<br/> On success, opens the device edition panel with
	 * the retrieved info.
	 * @param dto
	 */
	private void tryGetDeviceDetails(final DeviceSelDTO dto) {
		util.mask(pmsMessages.mskDevice());

		AsyncCallback<DeviceDTO> callback = new AsyncCallback<DeviceDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				util.error(pmsMessages.msgErrorRetrieveDevice());
			}

			public void onSuccess(DeviceDTO arg0) {
				showDeviceEditionPanel(arg0);
				util.unmask();
			}
		};

		devicesService.get(dto.getId(), callback);
	}

	/**
	 * If exists, removes the device tree and recreates it from scratch.<br/>
	 * @param devices
	 */
	private void addTreePanel(List<DeviceTreeDTO> devices) {

		if (store != null) {
			filter.unbind(store);
		}
		store = new DeviceTreeStore(devices);
		filter.setValue(null);
		filter.bind(store);

		tree = new TreePanel<DeviceSelModelData>(store) {
			protected void onDoubleClick(TreePanelEvent tpe) {
				super.onDoubleClick(tpe);
				getSelectedAndDisplayDetails();
			}
		};
		tree.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));

		tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<DeviceSelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<DeviceSelModelData> se) {
				DeviceSelModelData selected = tree.getSelectionModel().getSelectedItem();

				boolean enabled = false;
				if (selected != null) {
					enabled = true;
				}

				bDelete.setEnabled(enabled);
				bEdit.setEnabled(enabled);
			}
		});

		tree.setDisplayProperty(DeviceSelModelData.PROPERTY_NAME);

		add(tree);
	}

	/**
	 * Closes the device details window.<br/>
	 */
	private void closeDetailWindow() {
		if (wDetail != null && wDetail.isAttached()) {
			wDetail.hide();
		}
	}

	/**
	 * If exists, closes the device edition panel and re-creates it from scratch with the passed DTO for data
	 * binding.<br/>
	 * @param model
	 */
	private void showDeviceEditionPanel(DeviceDTO model) {
		// close creation panel:
		closeDetailWindow();
		DeviceEditionPanel deviceEdition = PmsFactory.getInstance().getDeviceEdition();
		deviceEdition.init(model);
		wDetail = deviceEdition;
		wDetail.show();
	}

	/**
	 * Creates and shows a device edition panel and binds it to a new device instance.<br/>
	 * @param parent
	 * @param order
	 */
	private void showDeviceCreationPanel(DeviceSelDTO parent, int order) {

		DeviceDTO dSel = new DeviceDTO();
		dSel.setName(pmsMessages.defaultValueDeviceName());
		String parentId = parent.getId();

		closeDetailWindow();
		DeviceCreationPanel deviceCreation = PmsFactory.getInstance().getDeviceCreationPanel();
		deviceCreation.init(dSel, parentId, order);
		wDetail = deviceCreation;
		wDetail.show();
	}

	/**
	 * Repopulates the tree with the passed device trees, and remembers the previously expanded nodes.<br/>
	 * 
	 * @param devices
	 */
	private void repopulateTree(List<DeviceTreeDTO> devices) {
		// store expanded items IDs:
		List<String> expanded = new LinkedList<String>();
		if (tree != null) {
			List<DeviceSelModelData> rootModels = store.getRootItems();
			for (DeviceSelModelData model : rootModels) {
				computeExpanded(model, expanded);
			}
		}
		// destroy & create the tree:
		if (tree != null && tree.isAttached()) {
			tree.removeFromParent();
		}
		addTreePanel(devices);
		// restore expanded:
		List<DeviceSelModelData> rootModels = store.getRootItems();
		for (DeviceSelModelData model : rootModels) {
			tree.setExpanded(model, true);
			// children nodes:
			for (DeviceSelModelData child : store.getChildren(model)) {
				maybeExpand(child, expanded);
			}
		}

		configDND();
	}

	/**
	 * If the passed model is expanded, adds it to the passed list and recursively computes the child expanded nodes.<br
	 * /> When current call is finished, expanded contains its old nodes AND all expanded nodes from passed model
	 * branch.
	 * 
	 * @param model
	 * @param expanded
	 */
	private void computeExpanded(DeviceSelModelData model, List<String> expanded) {
		if (tree.isExpanded(model)) {
			expanded.add(model.getDTO().getId());
			List<DeviceSelModelData> children = store.getChildren(model);
			for (DeviceSelModelData child : children) {
				computeExpanded(child, expanded);
			}
		}
	}

	/**
	 * Expands the passed model branches whose IDs are contained in the expanded list.
	 * 
	 * @param model
	 * @param expanded
	 */
	private void maybeExpand(DeviceSelModelData model, List<String> expanded) {
		if (expanded.contains(model.getDTO().getId())) {
			tree.setExpanded(model, true);
			for (DeviceSelModelData child : store.getChildren(model)) {
				maybeExpand(child, expanded);
			}
		}
	}

	/**
	 * Calls the devices remote service "move" method.<br/>
	 * @param source
	 * @param parent
	 * @param order
	 */
	private void tryMoveDevice(final DeviceSelDTO source, DeviceSelDTO parent, int order) {

		AsyncCallback<List<DeviceTreeDTO>> callback = new AsyncCallback<List<DeviceTreeDTO>>() {
			public void onFailure(Throwable arg0) {
				util.error(pmsMessages.msgErrorMoveDevice());
			}

			public void onSuccess(List<DeviceTreeDTO> arg0) {
				util.info(pmsMessages.msgSuccessMoveDevice());
			}
		};

		devicesService.move(source.getId(), parent.getId(), order, callback);
	}

	/**
	 * Retrieves all the devices from service and displays them in the devices tree.<br/>
	 */
	private void tryGetDevices() {

		util.mask(pmsMessages.mskDevices());

		AsyncCallback<List<DeviceTreeDTO>> catCallback = new AsyncCallback<List<DeviceTreeDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrSimple, pmsMessages.msgErrorRetrieveDevices());
			}

			public void onSuccess(List<DeviceTreeDTO> device) {
				repopulateTree(device);
				util.unmask();
			}
		};

		devicesService.getDevices(catCallback);
	}

	/**
	 * Calls the "delete" operation on devices remote service, from the passed device. <br/>
	 * @param csDto
	 */
	private void tryDeleteDevice(final DeviceSelDTO csDto) {
		util.mask(pmsMessages.mskDeleteDevice());

		AsyncCallback<List<DeviceTreeDTO>> callback = new AsyncCallback<List<DeviceTreeDTO>>() {
			/**
			 * Just shows an alert.<br/>
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable arg0) {
				util.unmask();
				util.error(pmsMessages.msgErrorDeleteDevice(csDto.getName()));
			}

			/**
			 * Shows an animated info popup and populates the tree with the results.<br/>
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
			 */
			public void onSuccess(List<DeviceTreeDTO> arg0) {
				repopulateTree(arg0);
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteDevice());
			}
		};

		devicesService.delete(csDto.getId(), callback);
	}

	/**
	 * Configs the tree binder with DND capabilities. Should be called after structural changes in the Tree:
	 * move/insert/remove.<br/>
	 */
	private void configDND() {

		TreePanelDragSource dragSource = new TreePanelDragSource(tree);
		dragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent e) {
				DeviceSelModelData selected = tree.getSelectionModel().getSelectedItem();
				if (selected.equals(store.getRootItems().get(0))) {
					e.setCancelled(true);
					e.getStatus().setStatus(false);
				}
				super.dragStart(e);
			}
		});

		TreePanelDropTarget dropTarget = new TreePanelDropTarget(tree);
		dropTarget.setAllowSelfAsSource(true);
		dropTarget.setAllowDropOnLeaf(true);
		dropTarget.setFeedback(Feedback.BOTH);
		dropTarget.setAutoExpand(true);

		dropTarget.setAllowSelfAsSource(true);
		dropTarget.setFeedback(Feedback.BOTH);

		dropTarget.addDNDListener(new DNDListener() {
			/**
			 * Ask for confirmation on drop.
			 * @param e drop event.
			 */
			@Override
			public void dragDrop(final DNDEvent e) {

				Listener<MessageBoxEvent> wListener = new Listener<MessageBoxEvent>() {
					@SuppressWarnings("unchecked")
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked == null) {
							return;
						}

						if (clicked.getItemId().equals(Dialog.YES)) { // confirmado
							// DND params: dragged item, new parent & order
							DeviceSelModelData draggedItem = null;
							List<BaseTreeModel> draggedItems = (List<BaseTreeModel>) e.getData();
							if (!draggedItems.isEmpty()) {
								draggedItem = draggedItems.get(0).get(KEY_MODEL);
							}

							// parent:
							DeviceSelModelData parent = DevicesManagement.this.store.getParent(draggedItem);

							int dndOrder = DevicesManagement.this.store.getChildren(parent).indexOf(draggedItem);

							tryMoveDevice(draggedItem.getDTO(), parent.getDTO(), dndOrder);
						} else { // no ha confirmado: devolver el Tree al estado anterior es un marron,
							// asi que volvemos a pedirlo todo:
							tryGetDevices();
						}
					}
				};

				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmMoveDevice(), wListener)
					.setModal(true);
			}
		});
	}

	/**
	 * Injects the Devices async service.
	 * @param devicesService
	 */
	@Inject
	public void setDevicesService(IDevicesServiceAsync devicesService) {
		this.devicesService = devicesService;
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
	 * @param emrSimple the emrSimple to set
	 */
	@Inject
	public void setEmrSimple(SimpleErrorMessageResolver emrSimple) {
		this.emrSimple = emrSimple;
	}

	/**
	 * @param errorProcessor the errorProcessor to set
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
}
