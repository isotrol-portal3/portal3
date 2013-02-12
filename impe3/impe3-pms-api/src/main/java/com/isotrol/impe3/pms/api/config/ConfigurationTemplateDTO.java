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

package com.isotrol.impe3.pms.api.config;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.isotrol.impe3.pms.api.AbstractDescribed;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO for a configuration template.
 * @author Andres Rodriguez
 */
public final class ConfigurationTemplateDTO extends AbstractDescribed {
	/** Serial UID. */
	private static final long serialVersionUID = -7635420266764459478L;
	/** Configuration key (configuration interface name). */
	private String key;
	/** Content types. */
	private List<ContentTypeSelDTO> contentTypes;
	/** Categories. */
	private CategoryTreeDTO categories;
	/** Configuration items. */
	private List<ConfigurationTemplateItemDTO> items;

	/** Default constructor. */
	public ConfigurationTemplateDTO() {
	}

	/**
	 * Returns the configuration key (configuration interface name).
	 * @return The configuration key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the configuration key.
	 * @param key The configuration key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the list of possible values for content types. This list will only constain values if there is at least
	 * one configuration item that is a content type.
	 * @return The possible content type values.
	 */
	public List<ContentTypeSelDTO> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Sets the possible content type values.
	 * @param contentTypes The possible content type values.
	 */
	public void setContentTypes(List<ContentTypeSelDTO> contentTypes) {
		this.contentTypes = contentTypes;
	}

	/**
	 * Returns the first level of the possible values for categories. This list will only constain values if there is at
	 * least one configuration item is a category.
	 * @return The possible values for categories.
	 */
	public CategoryTreeDTO getCategories() {
		return categories;
	}

	/**
	 * Sets the possible values for categories.
	 * @param categories The possible values for categories.
	 */
	public void setCategories(CategoryTreeDTO categories) {
		this.categories = categories;
	}

	/**
	 * Returns the configuration items.
	 * @return The configuration items.
	 */
	public List<ConfigurationTemplateItemDTO> getItems() {
		return items;
	}

	/**
	 * Sets the configuration items.
	 * @param items The configuration items.
	 */
	public void setItems(List<ConfigurationTemplateItemDTO> items) {
		this.items = items;
	}

	/**
	 * Returns the list of configuration items with the values of the current template.
	 * @return The list of configuration items.
	 */
	public List<ConfigurationItemDTO> toConfiguationItemDTO() {
		if (items == null || items.isEmpty()) {
			return Collections.emptyList();
		}
		final List<ConfigurationItemDTO> dtos = new ArrayList<ConfigurationItemDTO>(items.size());
		for (ConfigurationTemplateItemDTO item : items) {
			if (item != null) {
				dtos.add(item.toConfigurationItemDTO());
			}
		}
		return dtos;
	}

}
