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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PAGE;

import java.util.UUID;

import com.google.common.base.Predicate;
import com.isotrol.impe3.core.impl.AbstractIdentifiableMap;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.model.PageEntity;


/**
 * Abstract collection of pages domain objects.
 * @author Andres Rodriguez
 */
public abstract class PagesObject extends AbstractIdentifiableMap<PageObject> {
	/** Portal Id. */
	private final UUID portal;

	/**
	 * Constructor.
	 * @param portal Portal Id.
	 */
	PagesObject(UUID portal) {
		this.portal = checkNotNull(portal);
	}

	/**
	 * Returns the portal id.
	 * @return The portal id.
	 */
	public final UUID getPortal() {
		return portal;
	}

	public final PageObject load(UUID id) throws EntityNotFoundException {
		return PAGE.checkNotNull(get(id), id);
	}

	public final PageObject load(PageEntity e) {
		return get(e.getId());
	}

	public final PageObject load(String id) throws EntityNotFoundException {
		return load(PAGE.toUUID(id));
	}

	/**
	 * Returns whether a content type is used by this collection.
	 * @param id Content type id.
	 * @return True if the content type is used by this collection.
	 */
	public boolean isContentTypeUsed(final UUID id) {
		return any(values(), new Predicate<PageObject>() {
			public boolean apply(PageObject input) {
				return input.isContentTypeUsed(id);
			}
		});
	}

	/**
	 * Returns whether a category is used by this module.
	 * @param id Category id.
	 * @return True if the category is used by this module.
	 */
	public boolean isCategoryUsed(final UUID id) {
		return any(values(), new Predicate<PageObject>() {
			public boolean apply(PageObject input) {
				return input.isCategoryUsed(id);
			}
		});
	}

}
