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
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Maps;


/**
 * Category definition.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "CATEGORY_DFN")
public class CategoryDfn extends AbstractIAEDfn<CategoryDfn, CategoryEntity, CategoryEdition> {
	/** Category. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CTGY_ID", nullable = false)
	private CategoryEntity entity;
	/** Localized names. */
	@ElementCollection
	@JoinTable(name = "CATEGORY_L7D", joinColumns = @JoinColumn(name = "COTP_ID", nullable = false))
	@MapKeyColumn(name = "LOCALE", length = Lengths.LOCALE)
	private Map<String, NameValue> localizedNames;
	/** If the category is visible. */
	@Column(name = "VISIBLE", nullable = false)
	private boolean visible;
	/** Parent category. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CTGY_PARENT_ID", nullable = true)
	private CategoryEntity parent;
	/** Order in its category level. */
	@Column(name = "LEVEL_ORDER", nullable = false)
	private int order;
	/** Editions. */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "published")
	private Set<CategoryEdition> editions;

	/** Default constructor. */
	public CategoryDfn() {
	}

	/**
	 * Returns the entity.
	 * @return The entity.
	 */
	public CategoryEntity getEntity() {
		return entity;
	}

	/**
	 * Sets the entity.
	 * @param entity The entity.
	 */
	public void setEntity(CategoryEntity entity) {
		this.entity = entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.Definition#getEditions()
	 */
	@Override
	public Set<CategoryEdition> getEditions() {
		return editions;
	}

	/**
	 * Returns the localized names.
	 * @return The localized names.
	 */
	public Map<String, NameValue> getLocalizedNames() {
		if (localizedNames == null) {
			localizedNames = Maps.newHashMap();
		}
		return localizedNames;
	}

	/**
	 * Returns whether the category is visible.
	 * @return True if the category is visible.
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Sets whether the category is visible.
	 * @param visible True if the category is visible.
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns the parent category.
	 * @return The parent category.
	 */
	public CategoryEntity getParent() {
		return parent;
	}

	/**
	 * Sets the parent category.
	 * @param parent The parent category.
	 */
	public void setParent(CategoryEntity parent) {
		this.parent = parent;
	}

	/**
	 * Returns the parent category id.
	 * @return The parent category id.
	 */
	public UUID getParentId() {
		final CategoryEntity p = getParent();
		if (p == null) {
			return null;
		}
		return p.getId();
	}

	/**
	 * Returns the order in its category level.
	 * @return The order in its category level.
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the order in its category level.
	 * @param order The order in its category level.
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}
