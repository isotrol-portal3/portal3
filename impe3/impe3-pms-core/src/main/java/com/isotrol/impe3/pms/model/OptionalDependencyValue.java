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

package com.isotrol.impe3.pms.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * Value that represents an optional satisfied dependency.
 * @author Andres Rodriguez
 */
@Embeddable
public class OptionalDependencyValue implements DependencyValue {
	/** Connector. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CNNT_ID", nullable = true)
	private ConnectorEntity connector;
	/** Exported bean. */
	@Column(name = "CNNT_BEAN", nullable = true, length = Lengths.NAME)
	private String bean;

	/** Default constructor. */
	public OptionalDependencyValue() {
	}

	/**
	 * Returns the connector.
	 * @return The connector.
	 */
	public ConnectorEntity getConnector() {
		return connector;
	}

	/**
	 * Sets the connector.
	 * @param connector The connector.
	 */
	public void setConnector(ConnectorEntity connector) {
		this.connector = connector;
	}

	/**
	 * Returns the exported bean.
	 * @return The exported bean.
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the exported bean.
	 * @param bean The exported bean.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}
}
