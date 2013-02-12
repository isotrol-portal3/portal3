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

package com.isotrol.impe3.pms.api.mreg;


import java.util.List;



/**
 * DTO for a valid connector module description.
 * @author Andres Rodriguez
 */
public final class ConnectorModuleDTO extends AbstractValidModuleDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 8812926213081191074L;
	/** Provided connectors. */
	private List<ProvidedConnectorDTO> connectors;

	/** Default constructor. */
	public ConnectorModuleDTO() {
	}

	/**
	 * @return the connectors
	 */
	public List<ProvidedConnectorDTO> getConnectors() {
		return connectors;
	}

	/**
	 * @param connectors the connectors to set
	 */
	public void setConnectors(List<ProvidedConnectorDTO> connectors) {
		this.connectors = connectors;
	}

}
