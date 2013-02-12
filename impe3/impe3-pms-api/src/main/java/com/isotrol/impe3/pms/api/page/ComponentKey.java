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

package com.isotrol.impe3.pms.api.page;


import java.io.Serializable;


/**
 * DTO representing the key of a component in a page.
 * @author Andres Rodriguez
 */
public final class ComponentKey implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 62965745308754082L;
	/** Component package instance. */
	private String instanceId;
	/** Provided component. */
	private String bean;

	/** Default constructor. */
	public ComponentKey() {
	}

	/**
	 * Returns the component package instance.
	 * @return The component package instance.
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * Sets the component package instance.
	 * @param instanceId The component package instance.
	 */
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/**
	 * Returns the provided component bean.
	 * @return The provided component bean.
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * Sets the provided component bean.
	 * @param bean The provided component bean.
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	private static boolean eq(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null || o2 == null) {
			return false;
		}
		return o1.equals(o2);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComponentKey) {
			final ComponentKey key = (ComponentKey) obj;
			return eq(instanceId, key.instanceId) && eq(bean, key.bean);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (instanceId == null) {
			return 0;
		}
		return instanceId.hashCode();
	}
}
