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


import java.util.Set;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;


/**
 * Entity that represents a category.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "CATEGORY")
@NamedQueries({
	@NamedQuery(name = CategoryEntity.OFFLINE, query = "from CategoryEntity as e inner join fetch e.current where e.deleted is false and e.environment.id = ?"),
	@NamedQuery(name = CategoryEntity.ROOT, query = "from CategoryEntity as e where e.environment.id = ? and e.current.parent is null"),
	@NamedQuery(name = CategoryEntity.PFM, query = "from CategoryEntity as e where e.everPublished is null")})
public class CategoryEntity extends PublishableEntity<CategoryEntity, CategoryDfn, CategoryEdition> {
	/** Query: Offline categories. */
	public static final String OFFLINE = "category.offline";
	/** Query: root category. */
	public static final String ROOT = "category.root";
	/** Query: Published flag migration. */
	public static final String PFM = "category.pfm";
	/** By order ordering. */
	public static final Ordering<CategoryEntity> BY_ORDER;

	static {
		final Ordering<Integer> oi = Ordering.natural();
		final Function<CategoryEntity, Integer> order = new Function<CategoryEntity, Integer>() {
			public Integer apply(CategoryEntity from) {
				return from.getCurrent().getOrder();
			}
		};
		BY_ORDER = oi.onResultOf(order);
	}

	/** Current definition. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CTGY_DFN_ID", nullable = true)
	private CategoryDfn current;
	/** Definitions. */
	@OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
	private Set<CategoryDfn> definitions;

	/** Default constructor. */
	public CategoryEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.PublishableEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return "Category";
	}

	/**
	 * Returns the current definition.
	 * @return The current definition.
	 */
	public CategoryDfn getCurrent() {
		return current;
	}

	/**
	 * Sets the current definition.
	 * @param current The current definition.
	 */
	public void setCurrent(CategoryDfn current) {
		this.current = current;
	}

	@Override
	public Set<CategoryDfn> getDefinitions() {
		return definitions;
	}

}
