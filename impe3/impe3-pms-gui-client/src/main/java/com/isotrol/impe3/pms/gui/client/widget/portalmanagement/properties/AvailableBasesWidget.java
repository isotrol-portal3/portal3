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
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.data.impl.BaseModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;


/**
 * Grid that shows the available bases of a portal
 * @author Manuel Ruiz
 * 
 */
public class AvailableBasesWidget extends APropertiesManagement<BaseModelData> {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getHeaderText()
	 */
	@Override
	protected String getHeaderText() {
		return getPmsMessages().headerBasesManagement();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getPropertyKey()
	 */
	@Override
	protected String getPropertyKey() {
		return BaseModelData.PROPERTY_KEY;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getPropertyValue()
	 */
	@Override
	protected String getPropertyValue() {
		return BaseModelData.PROPERTY_URI;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#tryGetProperties()
	 */
	@Override
	protected void tryGetProperties(String portalId) {
		getUtil().mask(getPmsMessages().mskBases());

		AsyncCallback<List<BaseDTO>> callback = new AsyncCallback<List<BaseDTO>>() {

			public void onSuccess(List<BaseDTO> result) {
				getUtil().unmask();
				List<BaseModelData> bases = new ArrayList<BaseModelData>();
				for (BaseDTO base : result) {
					bases.add(new BaseModelData(base));
				}
				storeProperties(bases);
			}

			public void onFailure(Throwable caught) {
				getUtil().unmask();
				getErrorProcessor().processError(caught, getEmrPortals(), getPmsMessages().msgErrorRetrieveBases());
			}
		};
		getPortalsService().getAvailableBases(portalId, callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#openAvailableWindowProperties()
	 */
	@Override
	protected void openAvailableWindowProperties() {
		BasesManagement widget = PmsFactory.getInstance().getBaseManagement();
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
						case PmsChangeEvent.UPDATE_BASES:
						case PmsChangeEvent.IMPORT:
							Object object = event.getEventInfo();
							if(object instanceof Map<?, ?>) {
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
	 * Export portal bases
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#exportProperties()
	 */
	@Override
	protected void exportProperties() {
		getPortalsService().exportBases(getPortalNameDto().getId(), new AsyncCallback<String>() {

			public void onSuccess(String result) {
				getPmsUtil().exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				getUtil().error(getPmsMessages().msgExportError());
			}
		});
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.APropertiesManagement#getImportType()
	 */
	@Override
	protected EPortalImportExportType getImportType() {
		return EPortalImportExportType.BASES;
	}

}
