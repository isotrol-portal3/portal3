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


import java.util.ArrayList;
import java.util.List;

import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;


/**
 * Value that represents a component in a page.
 * @author Andres Rodriguez
 */
public class ComponentInPageTemplateDTO extends AbstractComponentInPageDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -4722696419875947302L;
	/** Palette item. */
	private PaletteDTO component;
	/** Component configuration. */
	private ConfigurationTemplateDTO configuration;
	/** Children component. */
	private List<ComponentInPageTemplateDTO> children;

	/** Default constructor. */
	public ComponentInPageTemplateDTO() {
	}

	/**
	 * Returns the palette item.
	 * @return The palette item.
	 */
	public PaletteDTO getComponent() {
		return component;
	}

	/**
	 * Sets the palette item.
	 * @param component The palette item.
	 */
	public void setComponent(PaletteDTO component) {
		this.component = component;
	}

	/**
	 * Returns the configuration.
	 * @return The configuration.
	 */
	public ConfigurationTemplateDTO getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 * @param configuration The configuration.
	 */
	public void setConfiguration(ConfigurationTemplateDTO configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Returns the children components.
	 * @return The children components.
	 */
	public List<ComponentInPageTemplateDTO> getChildren() {
		return children;
	}

	/**
	 * Sets the children components.
	 * @param children The children components.
	 */
	public void setChildren(List<ComponentInPageTemplateDTO> children) {
		this.children = children;
	}
	
	/**
	 * Trasformation method for the update DTO.
	 * @return Update DTO.
	 */
	public ComponentInPageDTO toComponentInPageDTO() {
		final ComponentInPageDTO dto = new ComponentInPageDTO();
		dto.setId(getId());
		dto.setName(getName());
		dto.setComponent(component != null ? component.getKey() : null);
		if (configuration != null) {
			dto.setConfiguration(configuration.toConfiguationItemDTO());
		}
		if (children != null) {
			final List<ComponentInPageDTO> list = new ArrayList<ComponentInPageDTO>(children.size());
			for (ComponentInPageTemplateDTO t : children) {
				list.add(t.toComponentInPageDTO());
			}
			dto.setChildren(list);
		}
		return dto;
	}

	@Override
	public boolean isSpace() {
		return (component == null || component.getKey() == null);
	}
	
}
