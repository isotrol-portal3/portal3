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

package com.isotrol.impe3.pms.gui.client.util;


/**
 * Constantes con los mensajes usadas. 
 * TODO en el futuro, reemplazar con i18n de GWT
 * 
 * @author Andrei Cojocaru
 * 
 */
public final class Labels {

	private Labels() {
	}

	/**
	 * Default style for tree items (leaves and non-leaves).<br/>
	 */
	public static final String TREE_ITEM_DEFAULT_STYLE = "tree-folder";

	/**
	 * Message within an exception thrown when GUI is trying to write a property that is not writable.<br/>
	 */
	public static final String PROPERTY_NOT_WRITABLE = "Property not writable: ";

	/**
	 * Message within an exception thrown when GUI is trying to access a property that is not readable.<br/>
	 */
	public static final String PROPERTY_NOT_READABLE = "Property not readable: ";

}
