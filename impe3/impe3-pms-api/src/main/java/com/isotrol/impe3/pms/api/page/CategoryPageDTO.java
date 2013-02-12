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


import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;


/**
 * DTO representing a category node of the portal pages tree for a device.
 * @author Andres Rodriguez
 */
public final class CategoryPageDTO extends WithCategoryPageChildren {
	/** Serial UID. */
	private static final long serialVersionUID = -6673141597080134734L;
	/** Category. */
	private CategorySelDTO category;
	/** This category and children page. */
	private Inherited<PageLoc> thisAndChildren;
	/** This category page. */
	private Inherited<PageLoc> onlyThis;
	
	private static boolean isOwn(Inherited<PageLoc> p ) {
		return p != null && !p.isInherited();
	}

	private static boolean isInherited(Inherited<PageLoc> p ) {
		return p != null && p.isInherited();
	}
	
	/** Default constructor. */
	public CategoryPageDTO() {
	}

	/**
	 * Returns the category.
	 * @return The category.
	 */
	public CategorySelDTO getCategory() {
		return category;
	}
	
	/**
	 * Returns the category id.
	 * @return The category id.
	 */
	public String getCategoryId() {
		if (category == null) {
			return null;
		}
		return category.getId();
	}
	

	/**
	 * Sets the category.
	 * @param category The category.
	 */
	public void setCategory(CategorySelDTO category) {
		this.category = category;
	}

	/**
	 * Returns the this and children category page.
	 * @return The this and children category page.
	 */
	public Inherited<PageLoc> getThisAndChildren() {
		return thisAndChildren;
	}

	/**
	 * Sets the this and children category page.
	 * @param thisAndChildren The this and children category page.
	 */
	public void setThisAndChildren(Inherited<PageLoc> thisAndChildren) {
		this.thisAndChildren = thisAndChildren;
	}

	/**
	 * Returns the only this category page.
	 * @return The only this category page.
	 */
	public Inherited<PageLoc> getOnlyThis() {
		return onlyThis;
	}

	/**
	 * Sets the only this category page.
	 * @param onlyThis The only this category page.
	 */
	public void setOnlyThis(Inherited<PageLoc> onlyThis) {
		this.onlyThis = onlyThis;
	}
	
	@Override
	HasPages doCalc() {
		final HasPages has = super.doCalc();
		final boolean o = isOwn(onlyThis) || isOwn(thisAndChildren);
		final boolean i = isInherited(onlyThis) || isInherited(thisAndChildren);
		if (o || i) {
			return new HasPages(o || has.hasOwnPages(), i || hasInheritedPages());
		}
		return has;
	}
	
}
