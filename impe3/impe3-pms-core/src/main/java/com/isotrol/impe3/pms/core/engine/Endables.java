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

package com.isotrol.impe3.pms.core.engine;


import java.util.List;

import com.google.common.collect.Lists;


/**
 * Companion object for endable objects.
 * @author Andres Rodriguez.
 */
public abstract class Endables {
	/** Not instantiable. */
	private Endables() {
		throw new AssertionError();
	}

	private static final ThreadLocal<List<Endable>> OBJECTS = new ThreadLocal<List<Endable>>();

	public static void end() {
		final List<Endable> list = OBJECTS.get();
		if (list != null) {
			try {
				for (Endable e : list) {
					e.endRequest();
				}
			}
			finally {
				list.clear();
			}
		}
	}

	static void add(Endable e) {
		List<Endable> list = OBJECTS.get();
		if (list == null) {
			list = Lists.newArrayList();
			OBJECTS.set(list);
		}
		list.add(e);
	}
}
