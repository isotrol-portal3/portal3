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


import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.model.Entity;


/**
 * Several commonly used functions.
 * @author Andres Rodriguez.
 */
final class ObjectFunctions {
	/** Not instantiable. */
	private ObjectFunctions() {
		throw new AssertionError();
	}

	static UUID id(Entity e) {
		return e == null ? null : e.getId();
	}

	/** Name to NameDTO. */
	static final Function<Name, NameDTO> NAME2DTO = new Function<Name, NameDTO>() {
		public NameDTO apply(Name from) {
			if (from == null) {
				return null;
			}
			final NameDTO dto = new NameDTO();
			dto.setDisplayName(from.getDisplayName());
			dto.setPath(from.getPath());
			return dto;
		}
	};

	static final <T> Function<T, Inherited<T>> toInherited(final Predicate<? super T> predicate) {
		return new Function<T, Inherited<T>>() {
			public Inherited<T> apply(T from) {
				return new Inherited<T>(predicate.apply(from), from);

			};
		};
	}
}
