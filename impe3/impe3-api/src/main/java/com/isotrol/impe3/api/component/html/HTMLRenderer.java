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


import com.isotrol.impe3.api.component.ComponentRenderer;


/**
 * Interface defining an HTML renderer.
 * @author Andres Rodriguez
 * 
 */
public interface HTMLRenderer extends ComponentRenderer {
	/**
	 * Returns the external stylesheets provided by this renderer. The stylesheets referenced by this method MUST NOT be
	 * included in the header fragment.
	 * @return The list of external stylesheets provided by this renderer.
	 */
	Iterable<CSS> getCSS();

	/**
	 * Returns the additional external stylesheets provided by this renderer for IE6. The stylesheets referenced by this
	 * method MUST NOT be included in the header fragment.
	 * @return The list of external stylesheets provided by this renderer.
	 */
	Iterable<CSS> getIE6CSS();

	/**
	 * Returns the additional external stylesheets provided by this renderer for IE7. The stylesheets referenced by this
	 * method MUST NOT be included in the header fragment.
	 * @return The list of external stylesheets provided by this renderer.
	 */
	Iterable<CSS> getIE7CSS();

	/**
	 * Returns the additional external stylesheets provided by this renderer for IE8. The stylesheets referenced by this
	 * method MUST NOT be included in the header fragment.
	 * @return The list of external stylesheets provided by this renderer.
	 */
	Iterable<CSS> getIE8CSS();
	
	/**
	 * Return the scripts to be included in the header.
	 * @return The scripts to be included in the header.
	 */
	Iterable<Script> getHeaderScripts();

	/**
	 * Returns a HTML fragment representing de header part of the component.
	 */
	HTMLFragment getHeader();

	/**
	 * Returns a HTML fragment representing de body part of the component.
	 */
	HTMLFragment getBody();

	/**
	 * Returns a HTML fragment representing de footer part of the component.
	 */
	HTMLFragment getFooter();
}
