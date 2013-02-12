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

package com.isotrol.impe3.api.component.html;


import com.google.common.collect.ImmutableList;


/**
 * Skeletal HTML renderer.
 * @author Andres Rodriguez
 */
public class SkeletalHTMLRenderer implements HTMLRenderer {
	/** Default constructor. */
	public SkeletalHTMLRenderer() {
	}

	public Iterable<CSS> getCSS() {
		return ImmutableList.of();
	}

	public Iterable<CSS> getIE6CSS() {
		return ImmutableList.of();
	}

	public Iterable<CSS> getIE7CSS() {
		return ImmutableList.of();
	}

	public Iterable<CSS> getIE8CSS() {
		return ImmutableList.of();
	}

	public Iterable<Script> getHeaderScripts() {
		return ImmutableList.of();
	}

	public HTMLFragment getHeader() {
		return null;
	}

	public HTMLFragment getBody() {
		return null;
	}

	public HTMLFragment getFooter() {
		return null;
	}
}
