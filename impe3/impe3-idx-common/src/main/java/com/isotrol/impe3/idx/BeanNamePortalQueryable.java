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

package com.isotrol.impe3.idx;


import static com.google.common.base.MoreObjects.firstNonNull;

import org.springframework.beans.factory.BeanNameAware;


/**
 * Abstract Port@l queryable that uses the bean name as its name.
 * @author Andres Rodriguez Chamorro
 */
public abstract class BeanNamePortalQueryable implements PortalQueryable, BeanNameAware {
	/** Bean name. */
	private String name;
	/** Indexer description. */
	private String description;

	public BeanNamePortalQueryable() {
	}

	public void setBeanName(String beanName) {
		this.name = beanName;
	}

	/**
	 * @see com.isotrol.impe3.idx.PortalIndexer#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the indexer description.
	 * @param description The indexer description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see com.isotrol.impe3.idx.PortalIndexer#getDescription()
	 */
	public String getDescription() {
		return firstNonNull(description, name);
	}
}
