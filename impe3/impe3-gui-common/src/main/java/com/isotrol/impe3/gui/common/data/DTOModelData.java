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

package com.isotrol.impe3.gui.common.data;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.extjs.gxt.ui.client.data.ModelData;


/**
 * ModelData backed by a DTO.
 * @author Andres Rodriguez
 * @param <D> DTO type.
 */
public abstract class DTOModelData<D> implements ModelData, DTOBacked<D> {

	private final D dto;
	
	/**
	 * <br/>
	 * @param dto
	 */
	public DTOModelData(D dto) {
		this.dto = dto;
	}
	
	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.data.DTOBacked#applyValuesAndGetDTO()
	 */
	public D getDTO() {
		return dto;
	}
	
	private static Set<String> addAll(Set<String> set, String... names) {
		for (String name : names) {
			set.add(name);
		}
		return Collections.unmodifiableSet(set);
	}

	public static Set<String> propertySet(String... names) {
		final Set<String> set = new HashSet<String>();
		return addAll(set, names);
	}

	public static Set<String> propertySet(Set<String> base, String... names) {
		final Set<String> set = new HashSet<String>();
		set.addAll(base);
		return addAll(set, names);
	}
	
	private void check(String property) {
		if (!getPropertyNames().contains(property)) {
			throw new IllegalArgumentException(
					getClass().getName() + "#check(String): no such property: " + property);
		}
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public final <X> X get(String property) {
		check(property);
		return (X)doGet(property);
	}

	protected abstract Object doGet(String property);

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getProperties()
	 */
	public final Map<String, Object> getProperties() {
		final Map<String, Object> map = new HashMap<String, Object>();
		for (final String property : getPropertyNames()) {
			map.put(property, get(property));
		}
		return map;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#remove(java.lang.String)
	 */
	public final <X> X remove(String property) {
		throw new UnsupportedOperationException();
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#set(java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public final <X> X set(String property, X value) {
		check(property);
		return (X)doSet(property, value);
	}

	protected abstract Object doSet(String property, Object value);
}
