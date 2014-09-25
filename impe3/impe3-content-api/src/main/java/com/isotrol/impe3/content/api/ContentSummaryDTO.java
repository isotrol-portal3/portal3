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

package com.isotrol.impe3.content.api;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.isotrol.impe3.dto.Counted;


/**
 * Content service summary.
 * @author Andres Rodriguez
 */
public class ContentSummaryDTO implements Serializable {
	/** Serial UID */
	private static final long serialVersionUID = 1557230343312546300L;
	/** Total number of contents. */
	private int total;
	/** Content count per content type. */
	private List<Counted<ContentTypeRefDTO>> perContentType;
	/** Content count per category */
	private CountedCategoryRefTreeDTO perCategory;

	/**
	 * Returns the total number of contents.
	 * @return The total number of contents.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Sets the total number of contents.
	 * @param total The total number of contents.
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * Returns the content count per content type.
	 * @return The content count per content type.
	 */
	public List<Counted<ContentTypeRefDTO>> getPerContentType() {
		if (perContentType == null) {
			perContentType = new ArrayList<Counted<ContentTypeRefDTO>>(0);
		}
		return perContentType;
	}

	/**
	 * Sets the content count per content type.
	 * @param perContentType The content count per content type.
	 */
	public void setPerContentType(List<Counted<ContentTypeRefDTO>> perContentType) {
		this.perContentType = perContentType;
	}

	/**
	 * Returns the content count per category.
	 * @return The content count per category.
	 */
	public CountedCategoryRefTreeDTO getPerCategory() {
		return perCategory;
	}

	/**
	 * Sets the content count per category.
	 * @param perContentType The content count per category.
	 */
	public void setPerCategory(CountedCategoryRefTreeDTO perCategory) {
		this.perCategory = perCategory;
	}

}
