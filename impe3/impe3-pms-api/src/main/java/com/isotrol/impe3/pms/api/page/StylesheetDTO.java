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


/**
 * DTO for a stylesheet reference in an layout.
 * @author Andres Rodriguez
 */
public final class StylesheetDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -434050410952312981L;
	/** Stylesheet URL. */
	private String url;
	/** Stylesheet media parameter. */
	private String media;

	/** Default constructor. */
	public StylesheetDTO() {
	}

	/**
	 * Returns the URL.
	 * @return The URL.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the media parameter.
	 * @return The media parameter.
	 */
	public String getMedia() {
		return media;
	}

	/**
	 * Sets the URL.
	 * @param url The URL.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Sets the media parameter.
	 * @param media The media parameter.
	 */
	public void setMedia(String media) {
		this.media = media;
	}
}
