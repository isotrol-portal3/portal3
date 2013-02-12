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

package com.isotrol.impe3.core;


import com.isotrol.impe3.api.ActionNotFoundPortalException;
import com.isotrol.impe3.api.NotFoundPortalException;


/**
 * Web exceptions helper class.
 * @author Andres Rodriguez
 */
public final class WebExceptions {
	/** Not instantiable. */
	private WebExceptions() {
		throw new AssertionError();
	}

	public static NotFoundPortalException notFound(String message) {
		return new NotFoundPortalException(message);
	}

	public static ActionNotFoundPortalException actionNotFound(String message) {
		return new ActionNotFoundPortalException(message);
	}

	public static void found(boolean condition, String message) {
		if (!condition) {
			throw notFound(message);
		}
	}

	public static <T> T found(T object, String message) {
		found(object != null, message);
		return object;
	}

	public static void actionFound(boolean condition, String message) {
		if (!condition) {
			throw actionNotFound(message);
		}
	}

	public static <T> T actionFound(T object, String message) {
		actionFound(object != null, message);
		return object;
	}

}
