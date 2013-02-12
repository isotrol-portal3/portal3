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

import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.WithId;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO for page selection.
 * @author Andres Rodriguez
 */
public final class PageTemplateDTO extends AbstractPageDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -5481728879986203291L;
	/** Category. */
	private CategorySelDTO category;
	/** Content type. */
	private ContentTypeSelDTO contentType;
	/** Possible Content types. */
	private List<ContentTypeSelDTO> contentTypes;
	/** Possible Categories. */
	private CategoryTreeDTO categories;
	/** Template page. */
	private PageSelDTO template;
	/** Possible templates. */
	private List<Inherited<PageSelDTO>> templates;
	/** Components. */
	private List<ComponentInPageTemplateDTO> components;

	/** Default constructor. */
	public PageTemplateDTO() {
	}

	/**
	 * Returns the category.
	 * @return The category.
	 */
	public CategorySelDTO getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category The category.
	 */
	public void setCategory(CategorySelDTO category) {
		this.category = category;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeSelDTO getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(ContentTypeSelDTO contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the list of possible values for content types.
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
	 * Returns the possible values for categories.
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
	 * Returns the template.
	 * @return The template.
	 */
	public PageSelDTO getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * @param template The template.
	 */
	public void setTemplate(PageSelDTO template) {
		this.template = template;
	}

	/**
	 * Returns the possible templates.
	 * @return The possible templates.
	 */
	public List<Inherited<PageSelDTO>> getTemplates() {
		return templates;
	}

	/**
	 * Sets the possible templates.
	 * @param templates The possible templates.
	 */
	public void setTemplates(List<Inherited<PageSelDTO>> templates) {
		this.templates = templates;
	}

	/**
	 * Returns the components.
	 * @return The components.
	 */
	public List<ComponentInPageTemplateDTO> getComponents() {
		return components;
	}

	/**
	 * Sets the components.
	 * @param components The components.
	 */
	public void setComponents(List<ComponentInPageTemplateDTO> components) {
		this.components = components;
	}

	private String getId(WithId dto) {
		return (dto != null) ? dto.getId() : null;
	}

	public PageDTO toPageDTO() {
		final PageDTO dto = new PageDTO(this);
		dto.setCategory(getId(getCategory()));
		dto.setContentType(getId(getContentType()));
		dto.setTemplate(getId(getTemplate()));
		final List<ComponentInPageDTO> list;
		if (components == null || components.isEmpty()) {
			list = new ArrayList<ComponentInPageDTO>(0);
		} else {
			list = new ArrayList<ComponentInPageDTO>(components.size());
			for (ComponentInPageTemplateDTO t : components) {
				list.add(t.toComponentInPageDTO());
			}
		}
		dto.setComponents(list);
		return dto;
	}
}
