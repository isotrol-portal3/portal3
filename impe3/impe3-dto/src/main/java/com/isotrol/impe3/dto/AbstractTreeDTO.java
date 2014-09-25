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

package com.isotrol.impe3.dto;


import java.io.Serializable;
import java.util.List;


/**
 * DTO for a tree of selection DTOs.
 * @author Andres Rodriguez
 * @param <T> Tree type.
 * @param <E> Node type.
 */
public abstract class AbstractTreeDTO<E, T extends AbstractTreeDTO<E,T>> implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -6103542893333124402L;
	/** Node. */
	private E node;
	/** Children list. */
	private List<T> children;

	/** Default constructor. */
	public AbstractTreeDTO() {
	}

	/**
	 * Returns the node.
	 * @return The node.
	 */
	public E getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 * @param node The node.
	 */
	public void setNode(E node) {
		this.node = node;
	}

	/**
	 * Returns the children of the current node.
	 * @return The children of the current node.
	 */
	public List<T> getChildren() {
		return children;
	}

	/**
	 * Sets the children of the current node.
	 * @param children The children of the current node.
	 */
	public void setChildren(List<T> children) {
		this.children = children;
	}
}
