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


import java.util.List;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Identifiable;


/**
 * A frame is an element in a Layout. It can be an instantiated component or a set of columns.
 * @author Andres Rodriguez
 */
public abstract class Frame {
	private static int sum(Iterable<Column> columns) {
		int sum = 0;
		for (Column c : columns) {
			sum += c.getWidth();
		}
		return sum;
	}

	/** Frame name. */
	private final String name;

	private Frame(String name) {
		this.name = name;
	}

	/**
	 * Returns the frame name.
	 * @return The frame name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a component frame.
	 * @param id ID of the component instantiation.
	 * @return The request frame.
	 */
	public static Component component(final String name, final UUID id) {
		Preconditions.checkNotNull(id, "The component instantiation id must be provided");
		return new Component(name, id);
	}

	/**
	 * Returns a columns frame.
	 * @param columns Columns.
	 * @return The request frame.
	 */
	public static Columns columns(final String name, final Iterable<Column> columns) {
		Preconditions.checkNotNull(columns, "The columns must be provided");
		return new Columns(name, columns);
	}

	/**
	 * Frame representing the instantiacion of a component.
	 * @author Andres Rodriguez
	 */
	public static final class Component extends Frame implements Identifiable {
		private UUID id;

		private Component(final String name, final UUID id) {
			super(name);
			this.id = id;
		}

		public UUID getId() {
			return id;
		}
	}

	/**
	 * Frame representing a set of columns.
	 * @author Andres Rodriguez
	 */
	public static final class Columns extends Frame {
		private ImmutableList<Column> columns;

		private Columns(final String name, final Iterable<Column> columns) {
			super(name);
			this.columns = ImmutableList.copyOf(columns);
			Preconditions.checkArgument(sum(columns) == Column.MAX_WIDTH, "The columns widths do not sum 100");
		}

		public List<Column> getColumns() {
			return columns;
		}
	}
}
