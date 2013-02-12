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

package com.isotrol.impe3.pms.api.page;


import java.io.Serializable;
import java.util.List;


/**
 * Base DTO por pages collection with category children.
 * @author Andres Rodriguez
 */
public abstract class WithCategoryPageChildren implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -798585395359683077L;
	/** Children pages. */
	private List<CategoryPageDTO> children;
	/** Whether there is any page defined in this portal. */
	private boolean ownPages;
	/** Whether there is any inherited page in this portal. */
	private boolean inheritedPages;

	/** Default constructor. */
	public WithCategoryPageChildren() {
	}

	/**
	 * Return the children categories.
	 * @return The children categories.
	 */
	public List<CategoryPageDTO> getChildren() {
		return children;
	}

	/**
	 * Sets the children categories.
	 * @param children The children categories.
	 */
	public void setChildren(List<CategoryPageDTO> children) {
		this.children = children;
	}
	
	/**
	 * Returns whether there is any page defined in this portal.
	 * @return True if the condition is met.
	 */
	public boolean hasOwnPages() {
		return ownPages;
	}
	
	/**
	 * Returns whether there is any inherited page in this portal.
	 * @return True if the condition is met.
	 */
	public boolean hasInheritedPages() {
		return inheritedPages;
	}

	/**
	 * Returns whether there is any page defined in this branch.
	 * @return True if the condition is met.
	 */
	public boolean hasPages() {
		return ownPages || inheritedPages;
	}
	
	/**
	 * Calculates the page definition flags.
	 */
	public final void calc() {
		final HasPages has = doCalc();
		if (has != null) {
			ownPages = has.hasOwnPages();
			inheritedPages = has.hasInheritedPages();
		} else {
			ownPages = false;
			inheritedPages = false;
		}
	}
	
	HasPages doCalc() {
		boolean o = false;
		boolean i = false;
		if (children != null || !children.isEmpty()) {
			for (CategoryPageDTO dto : children) {
				if (dto != null) {
					dto.calc();
					o = o || dto.hasOwnPages();
					i = i || dto.hasInheritedPages();
				}
			}
		}
		return new HasPages(o, i);
	}
	
	static final class HasPages {
		/** Whether there is any page defined in this portal. */
		private final boolean ownPages;
		/** Whether there is any inherited page in this portal. */
		private final boolean inheritedPages;
		
		public HasPages(boolean ownPages, boolean inheritedPages) {
			this.ownPages = ownPages;
			this.inheritedPages = inheritedPages;
		}
		
		/**
		 * Returns whether there is any page defined in this portal.
		 * @return True if the condition is met.
		 */
		boolean hasOwnPages() {
			return ownPages;
		}
		
		/**
		 * Returns whether there is any inherited page in this portal.
		 * @return True if the condition is met.
		 */
		boolean hasInheritedPages() {
			return inheritedPages;
		}
	}
	
}
