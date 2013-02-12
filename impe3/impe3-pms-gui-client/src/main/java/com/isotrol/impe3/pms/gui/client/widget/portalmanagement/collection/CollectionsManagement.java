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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.SetFilterModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalImportWindow;


/**
 * Widget that manages portal collections
 * @author Manuel Ruiz
 * 
 */
public class CollectionsManagement extends PmsContentPanel {

	/**
	 * <br/>
	 */
	private static final String BORDER_TOP_VALUE = "none";
	/**
	 * <br/>
	 */
	private static final String BORDER_TOP_KEY = "borderTop";

	/**
	 * Width in pixesls for column <b>name</b><br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 150;
	/**
	 * Width in pixesls for column <b>description</b><br/>
	 */
	private static final int COLUMN_DESCRIPTION_WIDTH = 300;
	/**
	 * Width in pixels for column <b>type</b><br/>
	 */
	private static final int COLUMN_TYPE_WIDTH = 150;

	/**
	 * The current portal id
	 */
	private String portalId = null;

	/**
	 * Delete "setFilter" button
	 */
	private Button bDelete = null;

	/**
	 * Edit "setFilter" button
	 */
	private Button bEdit = null;

	private Map<String, String> mapFilterTypes = null;

	/**
	 * Grid.<br/>
	 */
	private Grid<SetFilterModelData> grid = null;

	/**
	 * Proxy to Portals async service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

	/**
	 * Generic messages local service reference.<br/>
	 */
	private GuiCommonMessages messages = null;
	/**
	 * PMS specific messages local service reference.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper class.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * Common css styles
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS css styles
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * PMS util bundle
	 */
	private PmsUtil pmsUtil = null;

	@Override
	protected void beforeRender() {
		assert portalId != null : "a portalId must be set";

		initThis();
		initComponents();
		initController();
		tryGetSetFilters();
	}

	private void tryGetSetFilters() {
		AsyncCallback<List<SetFilterDTO>> callback = new AsyncCallback<List<SetFilterDTO>>() {

			public void onSuccess(List<SetFilterDTO> result) {
				List<SetFilterModelData> list = new ArrayList<SetFilterModelData>();
				for (SetFilterDTO dto : result) {
					list.add(new SetFilterModelData(dto));
				}
				grid.getStore().removeAll();
				grid.getStore().add(list);
			}

			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorGetSetFilters());
			}
		};
		portalsService.getSetFilters(portalId, callback);
	}

	private void initController() {
		PortalsController controller = (PortalsController) portalsService;
		controller.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent
					&& (e.getType() == PmsChangeEvent.UPDATE_SET_FILTERS || e.getType() == PmsChangeEvent.IMPORT)) {
					tryGetSetFilters();
				}
			}
		});
	}

	private void initThis() {
		setLayout(new FitLayout());

		String mapString = pmsMessages.mapFilterTypes();
		mapFilterTypes = new HashMap<String, String>();
		for (String s : mapString.split(",")) {
			String[] entry = s.trim().split("=");
			if (!Util.emptyString(entry[0])) {
				mapFilterTypes.put(entry[0], entry[1]);
			}
		}
	}

	private void initComponents() {
		addGrid();
		addToolBars();
	}

	private void addGrid() {

		// Grid bases:
		List<ColumnConfig> cc = new LinkedList<ColumnConfig>();

		ColumnConfig ccName = new ColumnConfig(SetFilterModelData.PROPERTY_NAME, pmsMessages.columnHeaderName(),
			COLUMN_NAME_WIDTH);
		cc.add(ccName);

		ColumnConfig ccDescription = new ColumnConfig(SetFilterModelData.PROPERTY_DESCRIPTION, pmsMessages
			.columnHeaderDescription(), COLUMN_DESCRIPTION_WIDTH);
		cc.add(ccDescription);

		ColumnConfig ccType = new ColumnConfig(SetFilterModelData.PROPERTY_TYPE, pmsMessages.columnHeaderType(),
			COLUMN_TYPE_WIDTH);
		ccType.setRenderer(new GridCellRenderer<SetFilterModelData>() {

			public Object render(SetFilterModelData model, String property, ColumnData config, int rowIndex,
				int colIndex, ListStore<SetFilterModelData> store, Grid<SetFilterModelData> grid) {
				if (model.getDTO().getType() != null) {
					return mapFilterTypes.get(model.getDTO().getType().toString());
				}
				return null;
			}
		});
		cc.add(ccType);

		ColumnModel cmOwn = new ColumnModel(cc);

		ListStore<SetFilterModelData> store = new ListStore<SetFilterModelData>();

		grid = new Grid<SetFilterModelData>(store, cmOwn);
		grid.setAutoExpandColumn(SetFilterModelData.PROPERTY_NAME);
		grid.setStyleAttribute(BORDER_TOP_KEY, BORDER_TOP_VALUE);
		GridView gView = grid.getView();
		gView.setForceFit(true);

		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<SetFilterModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SetFilterModelData> se) {
				boolean enabled = false;
				if (se.getSelectedItem() != null) {
					enabled = true;
				}
				bEdit.setEnabled(enabled);
				bDelete.setEnabled(enabled);
			}
		});
		add(grid);
	}

	private void addToolBars() {
		ToolBar toolbar = new ToolBar();
		ToolBar bottomToolbar = new ToolBar();

		SelectionListener<ButtonEvent> lNew = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				showcreationPanel();
			}
		};
		buttonsSupport.addAddButton(toolbar, lNew);
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lEdit = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				SetFilterModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					showEditionPanel(selected);
				}
			}
		};
		bEdit = buttonsSupport.addEditButton(toolbar, lEdit);
		bEdit.disable();
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDelete = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm1 = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
							SetFilterModelData selected = grid.getSelectionModel().getSelectedItem();
							if (selected != null) {
								tryDeleteSetFilter(selected);
							}
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteSetFilter(), lConfirm1)
					.setModal(true);
			}
		};
		bDelete = buttonsSupport.addDeleteButton(toolbar, lDelete);
		bDelete.disable();
		toolbar.add(new SeparatorToolItem());

		SelectionListener<ButtonEvent> lDeleteAll = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm1 = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
							tryDeleteAllSetFilter();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteAllSetFilter(),
					lConfirm1).setModal(true);
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelDeleteAll(), styles.iDelete(), toolbar, lDeleteAll);

		toolbar.add(new FillToolItem());
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetSetFilters();
			}
		};
		buttonsSupport.addRefreshButton(toolbar, lRefresh);
		
		SelectionListener<ButtonEvent> lExport = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				portalsService.exportSetFilters(portalId, new AsyncCallback<String>() {

					public void onSuccess(String result) {
						pmsUtil.exportPmsFile(result);
					}

					public void onFailure(Throwable caught) {
						util.error(pmsMessages.msgExportError());
					}
				});
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsStyles.exportIcon(), bottomToolbar, lExport);
		buttonsSupport.addSeparator(bottomToolbar);

		SelectionListener<ButtonEvent> lImport = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				PortalImportWindow w = PmsFactory.getInstance().getPortalImportWindow();
				w.setPortalId(portalId);
				w.setPortalImportType(EPortalImportExportType.COLLECTIONS);
				w.show();
			}
		};
		buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), bottomToolbar, lImport);
		
		setTopComponent(toolbar);
		setBottomComponent(bottomToolbar);
	}

	private void tryDeleteSetFilter(SetFilterModelData selected) {
		AsyncCallback<List<SetFilterDTO>> callback = new AsyncCallback<List<SetFilterDTO>>() {
			public void onSuccess(List<SetFilterDTO> result) {
				util.info(pmsMessages.msgSuccessDeleteSetFilter());
			}

			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorDeleteSetFilter());
			}
		};
		portalsService.removeSetFilter(portalId, selected.getDTO().getName(), callback);
	}

	private void tryDeleteAllSetFilter() {
		AsyncCallback<List<SetFilterDTO>> callback = new AsyncCallback<List<SetFilterDTO>>() {

			public void onSuccess(List<SetFilterDTO> result) {
				util.info(pmsMessages.msgSuccessDeleteAllSetFilter());
			}

			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorDeleteAllSetFilter());
			}
		};
		portalsService.clearSetFilters(portalId, callback);
	}

	private void showEditionPanel(SetFilterModelData selected) {
		CollectionEditionPanel detailEditor = PmsFactory.getInstance().getCollectionEditionPanel();
		detailEditor.init(portalId, selected.getDTO());
		detailEditor.show();
	}

	private void showcreationPanel() {
		CollectionCreationPanel detailEditor = PmsFactory.getInstance().getCollectionCreationPanel();
		detailEditor.init(portalId, new SetFilterDTO());
		detailEditor.show();
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
	 * @param buttonsSupport the buttonsSupport to set
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
