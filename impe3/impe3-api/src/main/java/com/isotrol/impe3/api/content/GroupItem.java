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

package com.isotrol.impe3.api.content;


/**
 * A group item.
 * @author Andres Rodriguez
 * @param <E> Group element type.
 */
public abstract class GroupItem<E> {
	private GroupItem() {
	}

	public boolean isGroupItem() {
		return false;
	}

	public boolean isElementItem() {
		return false;
	}

	/**
	 * Throws every time IllegalStateException
	 * @return nothing
	 */
	public Group<E> getGroup() {
		throw new IllegalStateException();
	}

	/**
	 * Throws every time IllegalStateException
	 * @return nothing
	 */
	public E getElement() {
		throw new IllegalStateException();
	}

	/**
	 * Create a new group item from a group of same element
	 * @param <E> groups element type
	 * @param group group to copy
	 * @return the group item
	 */
	public static <E> GroupItem<E> of(final Group<E> group) {
		return new GroupItem<E>() {
			@Override
			public boolean isGroupItem() {
				return true;
			}

			@Override
			public Group<E> getGroup() {
				return group;
			}
		};
	}

	/**
	 * Create a new group item from an element
	 * @param <E> element type
	 * @param element element to include
	 * @return the group (element)
	 */
	public static <E> GroupItem<E> of(final E element) {
		return new GroupItem<E>() {
			@Override
			public boolean isElementItem() {
				return true;
			}

			@Override
			public E getElement() {
				return element;
			}
		};
	}
}
