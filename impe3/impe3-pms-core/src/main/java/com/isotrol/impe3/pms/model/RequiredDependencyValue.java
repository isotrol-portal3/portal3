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


import static com.google.common.base.Objects.equal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;


/**
 * Value that represents a satisfied dependency.
 * @author Andres Rodriguez
 */
@Embeddable
public class RequiredDependencyValue implements DependencyValue {
	/** Connector. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CNNT_ID", nullable = false)
	private ConnectorEntity connector;
	/** Exported bean. */
	@Column(name = "CNNT_BEAN", nullable = false, length = Lengths.NAME)
	private String bean;

	/** Default constructor. */
	public RequiredDependencyValue() {
	}

	/**
	 * Constructor.
	 * @param connector Connector.
	 * @param bean Exported bean.
	 */
	public RequiredDependencyValue(ConnectorEntity connector, String bean) {
		this.connector = connector;
		this.bean = bean;
	}
	
	/**
	 * Clones this value.
	 */
	public RequiredDependencyValue clone() {
		return new RequiredDependencyValue(connector, bean);
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

	@Override
	public int hashCode() {
		return Objects.hashCode(connector, bean);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RequiredDependencyValue) {
			final RequiredDependencyValue v = (RequiredDependencyValue) obj;
			return equal(connector, v.connector) && equal(bean, v.bean);
		}
		return false;
	}
}
