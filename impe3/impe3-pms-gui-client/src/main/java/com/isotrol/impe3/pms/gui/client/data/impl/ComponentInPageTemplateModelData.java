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

import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.util.Labels;


/**
 * ModelData that represents a component in a page
 * @author Manuel Ruiz
 * 
 */
public class ComponentInPageTemplateModelData extends DTOModelData<ComponentInPageTemplateDTO> {

	/** Descriptor for component's bean property */
	public static final String PROPERTY_NAME = "name";
	
	/**
	 * reference to its parent
	 */
	private ComponentInPageTemplateModelData parent;

	/**
	 * Constructor
	 * @param dto
	 */
	public ComponentInPageTemplateModelData(ComponentInPageTemplateDTO dto) {
		super(dto);
	}

	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(PROPERTY_NAME);

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if(PROPERTY_NAME.equals(property)) {
			return getDTO().getName();
		}
		throw new IllegalArgumentException(Labels.PROPERTY_NOT_READABLE + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException(Labels.PROPERTY_NOT_WRITABLE + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/**
	 * @return the parent
	 */
	public ComponentInPageTemplateModelData getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ComponentInPageTemplateModelData parent) {
		this.parent = parent;
	}

}
