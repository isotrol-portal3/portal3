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
package com.isotrol.impe3.pms.gui.client.widget.externalservices;

import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceType;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.nr.NrViewport;

/**
 * Nodes Repository management widget.
 * 
 * @author Andrei Cojocaru
 *
 */
public class NodesRepositoryManagement extends AServicesManagement {


	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.externalservices.AServicesManagement#getManagedServiceType()
	 */
	/**
	 * Returns {@link ExternalServiceType#NODE_REPOSITORY}.<br/>
	 */
	@Override
	protected ExternalServiceType getManagedServiceType() {
		return ExternalServiceType.NODE_REPOSITORY;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.externalservices.AServicesManagement#getManageIconStyle()
	 */
	/**
	 * <br/>
	 */
	@Override
	protected String getManageIconStyle() {
		return getPmsStyles().iconManageNodesRepository();
	}

	@Override
	protected NrViewport getExternalServiceViewport(ExternalServiceDTO dto) {
		NrViewport viewport = (NrViewport) PmsFactory.getInstance().getNRViewport();
		viewport.setRepositoryId(dto.getId());
		viewport.init();
		return viewport;
	}

	@Override
	protected String getHistoryToken() {
		return "nodesRepository";
	}

	@Override
	protected String getServiceManagementName() {
		return getPmsMessages().menuItem2NodesRepository();	}

}
