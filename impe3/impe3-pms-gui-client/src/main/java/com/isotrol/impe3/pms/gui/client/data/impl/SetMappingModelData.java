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

package com.isotrol.impe3.pms.gui.client.data.impl;


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.pms.api.smap.SetMappingDTO;

/**
 * Model for set mappings
 * @author Manuel Ruiz
 * 
 */
public class SetMappingModelData extends AbstractMappingModelData<SetMappingDTO> {

	/** <b>Set</b> property descriptor.
	 * Should NOT be used as a display property (use renderers instead) */
	public static final String PROPERTY_SET = "set";
	
	/** Properties. */
	public static final Set<String> PROPERTIES = propertySet(AbstractMappingModelData.PROPERTIES, PROPERTY_SET);

	/**
	 * Default constructor
	 */
	public SetMappingModelData(SetMappingDTO dto) {
		super(dto);
	}

	/**
	 * Default constructor
	 */
	public SetMappingModelData() {
		this(new SetMappingDTO());
	}

	@Override
	protected Object doGet(String property) {
		if (PROPERTY_SET.equals(property)) {
			return getDTO().getSet();
		}
		return super.doGet(property);
	}

	@Override
	protected Object doSet(String property, Object value) {
		if (PROPERTY_SET.equals(property)) {
			
			SetMappingDTO setMDto = getDTO();
			String old = setMDto.getSet();
			setMDto.setSet((String) value);

			return old;
		}
		return super.doSet(property, value);
	}

	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

}
