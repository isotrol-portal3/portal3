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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.BaseModelData;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;


/**
 * Portal Bases management widget.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class BasesManagement extends APropertiesManagementPopup<BaseModelData> {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#getPropertyKey()
	 */
	@Override
	protected String getPropertyKey() {
		return BaseModelData.PROPERTY_KEY;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#getPropertyValue()
	 */
	@Override
	protected String getPropertyValue() {
		return BaseModelData.PROPERTY_URI;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#tryGetProperties()
	 */
	@Override
	protected void tryGetProperties() {
		getUtil().mask(getPmsMessages().mskBases());

		AsyncCallback<BasesDTO> callback = new AsyncCallback<BasesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPortals(), getPmsMessages().msgErrorRetrieveBases());
			}

			public void onSuccess(BasesDTO bases) {
				repopulateGrids(bases);
				getUtil().unmask();
			}
		};

		getPortalsService().getBases(getPortalNameDto().getId(), callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#trySaveCurrentChanges()
	 */
	@Override
	protected void trySaveCurrentChanges() {
		getUtil().mask(getPmsMessages().mskSaveBases());

		String id = getPortalNameDto().getId();

		List<BaseDTO> bases = new LinkedList<BaseDTO>();
		for (BaseModelData model : getOwnGrid().getStore().getModels()) {
			bases.add(model.getDTO());
		}

		AsyncCallback<BasesDTO> callback = new AsyncCallback<BasesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPortals(), getPmsMessages().msgErrorSaveBases());
			}

			public void onSuccess(BasesDTO list) {
				getButtonsSupport().closeActiveWindow();
				getUtil().unmask();
				getUtil().info(getPmsMessages().msgSuccessSaveBases());
			}
		};

		getPortalsService().setBases(id, bases, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getAddButtonListener()
	 */
	@Override
	protected SelectionListener<ButtonEvent> getAddButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				BaseDTO newBase = new BaseDTO();
				newBase.setKey(getPmsMessages().defaultBaseKey());
				newBase.setUri(getPmsMessages().defaultBaseUri());
				getOwnGrid().getStore().add(new BaseModelData(newBase));
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getDeleteButtonListener()
	 */
	@Override
	protected SelectionListener<ButtonEvent> getDeleteButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				List<BaseModelData> selected = getOwnGrid().getSelectionModel().getSelectedItems();
				ListStore<BaseModelData> store = getOwnGrid().getStore();
				for (BaseModelData model : selected) {
					store.remove(model);
				}
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#initController()
	 */
	@Override
	protected void initController() {
		PortalsController controller = (PortalsController) getPortalsService();
		controller.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent && e.getType() == PmsChangeEvent.UPDATE_BASES) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					Map<String, Object> info = event.getEventInfo();
					BasesDTO bases = (BasesDTO) info.get(PmsConstants.BASES_LIST);
					String portalId = (String) info.get(PmsConstants.PORTAL_ID);

					if (portalId.equals(getPortalNameDto().getId())) {
						repopulateGrids(bases);
					}
				}
			}
		});
	}

	/**
	 * Repopulates this container grids with the passed bases information.<br/>
	 * @param basesList
	 */
	protected final void repopulateGrids(BasesDTO basesList) {
		Grid<BaseModelData> ownGrid = getOwnGrid();
		// own grid has a listener on its store: must be removed
		Store<BaseModelData> store = ownGrid.getStore();
		// TODO check events system
		store.setFiresEvents(false);
		// store.removeStoreListener(lOwnStore);
		repopulateGrid(ownGrid, basesList.getBases());
		store.setFiresEvents(true);
		// store.addStoreListener(lOwnStore);

		ownGrid.getView().refresh(false);

		// inherited grid:
		repopulateGrid(getInheritedGrid(), basesList.getInherited());
	}

	/**
	 * Repopulates the passed grid with the passed bases list.<br/>
	 * @param grid
	 * @param basesList
	 */
	private void repopulateGrid(Grid<BaseModelData> grid, List<BaseDTO> basesList) {
		List<BaseModelData> lModelData = new LinkedList<BaseModelData>();
		for (BaseDTO base : basesList) {
			lModelData.add(new BaseModelData(base));
		}

		ListStore<BaseModelData> store = grid.getStore();
		store.removeAll();
		store.add(lModelData);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#getHeaderText()
	 */
	@Override
	protected String getHeaderText() {
		return getPmsMessages().headerBasesManagement();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getOwnTabHeaderText()
	 */
	@Override
	protected String getOwnTabHeaderText() {
		return getPmsMessages().headerOwnBasesPanel();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getInheritedTabHeaderText()
	 */
	@Override
	protected String getInheritedTabHeaderText() {
		return getPmsMessages().headerInheritedBasesPanel();
	}
}
