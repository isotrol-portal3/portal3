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

package com.isotrol.impe3.pms.api;


import java.util.HashMap;
import java.util.Map;

import com.isotrol.impe3.dto.AbstractTreeDTO;


/**
 * DTO for a tree of selection DTOs.
 * @author Andres Rodriguez
 * @param <T> Tree type.
 * @param <E> Node type.
 */
public abstract class AbstractTreeIndex<E extends WithId, T extends AbstractTreeDTO<E, T>> {
	/** Node map. */
	private final Map<String, T> map;

	private static <E extends WithId, T extends AbstractTreeDTO<E, T>> void load(Map<String, T> map, T tree) {
		if (tree == null) {
			return;
		}
		final E node = tree.getNode();
		if (node == null) {
			return;
		}
		final String id = node.getId();
		if (id != null) {
			map.put(id, tree);
			Iterable<T> children = tree.getChildren();
			if (children != null) {
				for (T child : children) {
					load(map, child);
				}
			}
		}

	}

	/**
	 * Default constructor.
	 * @param tree Original tree.
	 */
	public AbstractTreeIndex(T tree) {
		this.map = new HashMap<String, T>();
		load(map, tree);
	}

	/**
	 * Returns the tree node with the specified id.
	 * @param id Requested id.
	 * @return The requested tree node or {@code null} if not found.
	 */
	public final T getTreeNode(String id) {
		return map.get(id);
	}
}
