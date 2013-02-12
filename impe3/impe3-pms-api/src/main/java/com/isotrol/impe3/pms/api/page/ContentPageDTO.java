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

import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO representing a content node of the portal pages tree for a device.
 * @author Andres Rodriguez
 */
public final class ContentPageDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -6754747899950960215L;
	/** Content type. */
	private ContentTypeSelDTO contentType;
	/** Page. */
	private Inherited<PageLoc> page;
	/** Whether there are pages for this content type and a category. */
	private boolean withCategory = false;

	/** Default constructor. */
	public ContentPageDTO() {
	}

	/**
	 * Returns the content type.
	 * @return The content type.
	 */
	public ContentTypeSelDTO getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * @param contentType The content type.
	 */
	public void setContentType(ContentTypeSelDTO contentType) {
		this.contentType = contentType;
	}

	/**
	 * Returns the page.
	 * @return The page.
	 */
	public Inherited<PageLoc> getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 * @param page The page.
	 */
	public void setPage(Inherited<PageLoc> defaultPage) {
		this.page = defaultPage;
	}

	/**
	 * Returns whether there are pages for this content type and a category.
	 * @return True if the condition is met.
	 */
	public boolean isWithCategory() {
		return withCategory;
	}

	/**
	 * Sets whether there are pages for this content type and a category.
	 * @param withCategory True if the condition is met.
	 */
	public void setWithCategory(boolean withCategory) {
		this.withCategory = withCategory;
	}
}
