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


import java.util.List;


/**
 * DTO for page modification.
 * @author Andres Rodriguez
 */
public final class PageDTO extends AbstractPageDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 6133010493671322269L;
	/** Category. */
	private String category;
	/** Content type. */
	private String contentType;
	/** Template page. */
	private String template;
	/** Components. */
	private List<ComponentInPageDTO> components;

	/** Default constructor. */
	public PageDTO() {
	}

	/**
	 * Partial copy constructor.
	 * @param dto Source dto.
	 */
	public PageDTO(AbstractPageDTO dto) {
		super(dto);
	}
	
	/**
	 * Returns the category.
	 * @return The category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category The category.
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the template.
	 * @return The template.
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * @param template The template.
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * Returns the components.
	 * @return The components.
	 */
	public List<ComponentInPageDTO> getComponents() {
		return components;
	}

	/**
	 * Sets the components.
	 * @param components The components.
	 */
	public void setComponents(List<ComponentInPageDTO> components) {
		this.components = components;
	}

}
