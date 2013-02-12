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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.PropertyModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;


/**
 * Grid that shows the available properties of a portal
 * @author Manuel Ruiz
 * 
 */
public class AvailablePropertiesWidget extends APropertiesManagement<PropertyModelData> {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getHeaderText()
	 */
	@Override
	protected String getHeaderText() {
		return getPmsMessages().headerPropertiesManagement();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getPropertyKey()
	 */
	@Override
	protected String getPropertyKey() {
		return PropertyModelData.PROPERTY_NAME;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getPropertyValue()
	 */
	@Override
	protected String getPropertyValue() {
		return PropertyModelData.PROPERTY_VALUE;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#tryGetProperties()
	 */
	@Override
	protected void tryGetProperties(String portalId) {
		getUtil().mask(getPmsMessages().mskProperties());

		AsyncCallback<List<PropertyDTO>> callback = new AsyncCallback<List<PropertyDTO>>() {

			public void onSuccess(List<PropertyDTO> result) {
				getUtil().unmask();
				List<PropertyModelData> bases = new ArrayList<PropertyModelData>();
				for (PropertyDTO property : result) {
					bases.add(new PropertyModelData(property));
				}
				storeProperties(bases);
			}

			public void onFailure(Throwable caught) {
				getUtil().unmask();
				getErrorProcessor()
					.processError(caught, getEmrPortals(), getPmsMessages().msgErrorRetrieveProperties());
			}
		};
		getPortalsService().getAvailableProperties(portalId, callback);
	}

	@Override
	protected void openAvailableWindowProperties() {
		PropertiesManagement widget = PmsFactory.getInstance().getPropertiesManagement();
		widget.init(getPortalNameDto());
		widget.show();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#initController()
	 */
	@Override
	protected void initController() {
		PortalsController controller = (PortalsController) getPortalsService();
		controller.addChangeListener(new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.UPDATE_PROPERTIES:
						case PmsChangeEvent.IMPORT:
							Object object = event.getEventInfo();
							if (object instanceof Map<?, ?>) {
								Map<String, Object> info = event.getEventInfo();
								String portalId = (String) info.get(PmsConstants.PORTAL_ID);

								if (portalId.equals(getPortalNameDto().getId())) {
									tryGetProperties(portalId);
								}
							}
							break;
						default:
							break;
					}

				}
			}
		});
	}

	/**
	 * Export portal properties
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#exportProperties()
	 */
	@Override
	protected void exportProperties() {
		getPortalsService().exportProperties(getPortalNameDto().getId(), new AsyncCallback<String>() {

			public void onSuccess(String result) {
				getPmsUtil().exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				getUtil().error(getPmsMessages().msgExportError());
			}
		});
	}

	@Override
	protected EPortalImportExportType getImportType() {
		return EPortalImportExportType.PROPERTIES;
	}

}
