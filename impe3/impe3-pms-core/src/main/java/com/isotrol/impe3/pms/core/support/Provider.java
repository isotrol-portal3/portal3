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

package com.isotrol.impe3.pms.core.support;


import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.isotrol.impe3.pms.model.ConnectorDfn;


/**
 * A possible connector provider.
 * @author Andres Rodriguez.
 */
public final class Provider {
	private final ConnectorDfn connector;
	private final String bean;

	/**
	 * Default constructor.
	 * @param connector Connector.
	 * @param bean Exported bean name.
	 */
	public Provider(final ConnectorDfn connector, final String bean) {
		this.connector = Preconditions.checkNotNull(connector);
		this.bean = Preconditions.checkNotNull(bean);
	}

	public String getBean() {
		return bean;
	}

	public ConnectorDfn getConnector() {
		return connector;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Provider) {
			final Provider p = (Provider) obj;
			return Objects.equal(bean, p.bean) && Objects.equal(connector, p.connector);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(bean, connector);
	}
}
