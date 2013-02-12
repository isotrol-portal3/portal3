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
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.portal.PropertiesDTO;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.PropertyModelData;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;


/**
 * Manages the Properties of the bound Portal.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class PropertiesManagement extends APropertiesManagementPopup<PropertyModelData> {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getAddButtonListener()
	 */
	@Override
	protected SelectionListener<ButtonEvent> getAddButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				PropertyDTO newProperty = new PropertyDTO();
				newProperty.setName(getPmsMessages().defaultPropertyName());
				newProperty.setValue(getPmsMessages().defaultPropertyValue());
				getOwnGrid().getStore().add(new PropertyModelData(newProperty));
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
				List<PropertyModelData> selected = getOwnGrid().getSelectionModel().getSelectedItems();
				ListStore<PropertyModelData> store = getOwnGrid().getStore();
				for (PropertyModelData model : selected) {
					store.remove(model);
				}
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#getPropertyKey()
	 */
	@Override
	protected String getPropertyKey() {
		return PropertyModelData.PROPERTY_NAME;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#getPropertyValue()
	 */
	@Override
	protected String getPropertyValue() {
		return PropertyModelData.PROPERTY_VALUE;
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
				if (e instanceof PmsChangeEvent && e.getType() == PmsChangeEvent.UPDATE_PROPERTIES) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					Map<String, Object> info = event.getEventInfo();
					PropertiesDTO bases = (PropertiesDTO) info.get(PmsConstants.PROPERTIES_LIST);
					String portalId = (String) info.get(PmsConstants.PORTAL_ID);

					if (portalId.equals(getPortalNameDto().getId())) {
						repopulateGrids(bases);
					}
				}
			}

		});
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagementPopup#tryGetProperties()
	 */
	@Override
	protected void tryGetProperties() {
		getUtil().mask(getPmsMessages().mskProperties());

		AsyncCallback<PropertiesDTO> callback = new AsyncCallback<PropertiesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPortals(), getPmsMessages().msgErrorRetrieveProperties());
			}

			public void onSuccess(PropertiesDTO properties) {
				repopulateGrids(properties);
				getUtil().unmask();
			}
		};

		getPortalsService().getProperties(getPortalNameDto().getId(), callback);
	}

	@Override
	protected void trySaveCurrentChanges() {
		getUtil().mask(getPmsMessages().mskSaveProperties());

		String id = getPortalNameDto().getId();

		List<PropertyDTO> properties = new LinkedList<PropertyDTO>();
		for (PropertyModelData model : getOwnGrid().getStore().getModels()) {
			properties.add(model.getDTO());
		}

		AsyncCallback<PropertiesDTO> callback = new AsyncCallback<PropertiesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPortals(), getPmsMessages().msgErrorSaveProperties());
			}

			public void onSuccess(PropertiesDTO list) {
				getButtonsSupport().closeActiveWindow();
				getUtil().unmask();
				getUtil().info(getPmsMessages().msgSuccessSaveProperties());
			}
		};

		getPortalsService().setProperties(id, properties, callback);
	}

	/**
	 * Repopulates the grids with the passed DTO data.
	 * @param properties
	 */
	private void repopulateGrids(PropertiesDTO properties) {
		Grid<PropertyModelData> ownGrid = getOwnGrid();
		// own grid has a listener on its store: must be removed
		Store<PropertyModelData> store = ownGrid.getStore();
		// TODO check events system
		store.setFiresEvents(false);
		// store.removeStoreListener(lOwnStore);
		repopulateGrid(ownGrid, properties.getProperties());
		store.setFiresEvents(true);
		// store.addStoreListener(lOwnStore);

		ownGrid.getView().refresh(false);

		// inherited grid:
		repopulateGrid(getInheritedGrid(), properties.getInherited());
	}

	/**
	 * Repopulates the passed grid with the passed Properties list.<br/>
	 * 
	 * @param inheritedGrid
	 * @param inherited
	 */
	private void repopulateGrid(Grid<PropertyModelData> grid, List<PropertyDTO> properties) {
		List<PropertyModelData> lModelData = new LinkedList<PropertyModelData>();
		for (PropertyDTO base : properties) {
			lModelData.add(new PropertyModelData(base));
		}

		ListStore<PropertyModelData> store = grid.getStore();
		store.removeAll();
		store.add(lModelData);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getHeaderText()
	 */
	@Override
	protected String getHeaderText() {
		return getPmsMessages().headerPropertiesManagement();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties
	 * .APropertiesManagementPopup#getOwnTabHeaderText()
	 */
	@Override
	protected String getOwnTabHeaderText() {
		return getPmsMessages().headerOwnPropertiesPanel();
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
