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


import static com.google.common.collect.Iterables.any;
import static com.isotrol.impe3.pms.core.obj.ModuleObject.BY_NAME;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.isotrol.impe3.core.impl.AbstractIdentifiableMap;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.support.Mappers;


/**
 * Collection of modules domain object.
 * @author Andres Rodriguez
 */
public abstract class ModulesObject<T extends ModuleObject> extends AbstractIdentifiableMap<T> {
	static <T extends ModuleObject> List<ModuleInstanceSelDTO> map2sel(final ModuleRegistry registry,
		final Locale locale, Iterable<T> modules) {
		final List<T> ordered = BY_NAME.sortedCopy(modules);
		final Function<T, ModuleInstanceSelDTO> f = new Function<T, ModuleInstanceSelDTO>() {
			public ModuleInstanceSelDTO apply(T from) {
				return from.toSelDTO(registry, locale);
			};
		};
		return Mappers.list(ordered, f);
	}

	/** Constructor. */
	ModulesObject() {
	}

	/**
	 * Returns whether a content type is used by this collection.
	 * @param id Content type id.
	 * @return True if the content type is used by this collection.
	 */
	public boolean isContentTypeUsed(final UUID id) {
		return any(values(), new Predicate<ModuleObject>() {
			public boolean apply(ModuleObject input) {
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
		return any(values(), new Predicate<ModuleObject>() {
			public boolean apply(ModuleObject input) {
				return input.isCategoryUsed(id);
			}
		});
	}

	/**
	 * Returns whether a connector is used by any module in this collection.
	 * @param id Connector id.
	 * @return True if the connector is used by any module in this collection.
	 */
	public boolean isConnectorUsed(UUID id) {
		for (T module : values()) {
			if (module.isConnectorUsed(id)) {
				return true;
			}
		}
		return false;
	}
}
