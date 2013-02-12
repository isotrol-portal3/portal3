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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.util;


/**
 * Describes layout constants.
 * 
 * @author Andrei Cojocaru
 * 
 */
public final class PmsConstants {

	private PmsConstants() {}

	/**
	 * Popup window options used with {@link com.google.gwt.user.client.Window#open(String, String, String)}.<br/>
	 */
	public static final String NEW_WINDOW_FEATURES = 
		"menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes,toolbar=yes";
	
	/**
	 * If a Drop is performed when the mouse is on a TreeItem T but near of its
	 * top, GXT interprets it as an insert (as the previous sibling of T)
	 * instead of an append (as child of T). This constant defines this margin,
	 * in pixels.<br/>
	 */
	public static final int APPEND_DROP_ACCURACY_MARGIN = 4;

	/**
	 * Value used in ModelData objects that do not have ID but need a value for {@link #PROPERTY_ID} property.<br/>
	 */
	public static final String NULL_ID_VALUE = "-1"; 
	
	/**
	 * Key for bases list entry in the PmsChangeEvent info for UPDATE_BASES
	 * event type.<br/>
	 */
	public static final String BASES_LIST = "bases-list";
	
	/**
	 * Key for Properties list entry in the PmsChangeEvent info for UPDATE_PROPERTIES
	 * event type<br/>
	 */
	public static final String PROPERTIES_LIST = "properties-list";

	/**
	 * Key for portal ID entry in the PmsChangeEvent info for UPDATE_BASES event
	 * type.<br/>
	 */
	public static final String PORTAL_ID = "portal-id";

	/**
	 * height of the left-side vertical menus that can be found within tab
	 * items:<br/>
	 * pages & categories master views panels.
	 */
	public static final int LEFT_MENU_IN_TAB_HEIGHT = 700;

	/**
	 * Window width for detail windows: connectors, components, source mappings
	 * etc.<br/>
	 */
	public static final int DETAIL_WINDOW_WIDTH = 700;
	
	/**
	 * Fixed height for non-liquid detail windows.<br/>
	 */
	public static final int DETAIL_WINDOW_HEIGHT = 600;

	/**
	 * Width for source mappings detail window.<br/>
	 */
	public static final int SOURCE_MAPPING_DETAIL_WINDOW_WIDTH = 500;
	
	/**
	 * Init navigation history token
	 */
	public static final String INIT_TOKEN = "index";
	
	/**
	 * Template with a html link
	 */
	public static final String HTML_LINK = "<a href='${LINK_HREF}' target='_blank'>${LINK_HREF}</a>";
	
	/**
	 * Pattern with the href in a {@link PmsConstants#HTML_LINK}
	 */
	public static final String PATTERN_HREF = "\\$\\{LINK_HREF\\}";
	
	/**
	 * Pattern with the text in a {@link PmsConstants#HTML_LINK}
	 */
	public static final String PATTERN_TEXT = "\\$\\{LINK_TEXT\\}";
	
}
