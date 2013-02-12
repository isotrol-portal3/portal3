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


import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;
import org.hibernate.annotations.Type;

import com.google.common.collect.Maps;


/**
 * Entity that represents a page definition.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PAGE_DFN")
public class PageDfn extends WithUpdatedEntity {
	/** Page entity. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "PAGE_ID", nullable = false)
	private PageEntity page;
	/** Name. */
	@Column(name = "NAME", nullable = true, length = Lengths.NAME)
	private String name;
	/** Page description. */
	@Column(name = "DESCRIPTION", length = Lengths.DESCRIPTION, nullable = true)
	private String description;
	/** Category. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CTGY_ID", nullable = true)
	private CategoryEntity category;
	/** Content type. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "COTP_ID", nullable = true)
	private ContentTypeEntity contentType;
	/** Whether the page is an umbrella page. */
	@Column(name = "UMBRELLA", nullable = false)
	private boolean umbrella = false;
	/** Tag. */
	@Column(name = "TAG_NAME", nullable = true, length = Lengths.NAME)
	private String tag;
	/** Template page. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "TMPL_ID", nullable = true)
	private PageEntity template;
	/** Components in page. */
	@CollectionOfElements
	@JoinTable(name = "PAGE_CIP", joinColumns = @JoinColumn(name = "PAGE_ID", nullable = false))
	@MapKey(columns = @Column(name = "CIP_ID", length = Lengths.UUID), type=@Type(type="impeId"))
	private Map<UUID, CIPValue> components;
	/** Page layout. */
	@CollectionOfElements
	@JoinTable(name = "PAGE_LAYOUT", joinColumns = @JoinColumn(name = "PAGE_ID", nullable = false))
	@MapKey(columns = @Column(name = "LYT_ID"))
	private Map<Integer, LayoutValue> layout;

	/** Default constructor. */
	public PageDfn() {
	}

	/**
	 * Returns the page.
	 * @return The page.
	 */
	public PageEntity getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 * @param device The page.
	 */
	public void setPage(PageEntity page) {
		this.page = page;
	}

	/**
	 * Returns the name.
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the page description.
	 * @return The page description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the page description.
	 * @param description The page description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the category.
	 * @return The category.
	 */
	public CategoryEntity getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category The category.
	 */
	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeEntity getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(ContentTypeEntity contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns whether the page is an umbrella page.
	 * @return True if the page is an umbrella page.
	 */
	public boolean isUmbrella() {
		return umbrella;
	}

	/**
	 * Sets whether the page is an umbrella page.
	 * @param umbrella True if the page is an umbrella page.
	 */
	public void setUmbrella(boolean umbrella) {
		this.umbrella = umbrella;
	}

	/**
	 * Returns the name or tag.
	 * @return The name or tag.
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Sets the name or tag.
	 * @param name The name or tag.
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Returns the template.
	 * @return The template.
	 */
	public PageEntity getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * @param template The template.
	 */
	public void setTemplate(PageEntity template) {
		this.template = template;
	}

	/**
	 * Returns the components in the page.
	 * @return The components in the page.
	 */
	public Map<UUID, CIPValue> getComponents() {
		if (components == null) {
			components = Maps.newHashMap();
		}
		return components;
	}

	/**
	 * Returns the page layout.
	 * @return The page layout.
	 */
	public Map<Integer, LayoutValue> getLayout() {
		if (layout == null) {
			layout = Maps.newHashMap();
		}
		return layout;
	}
}
