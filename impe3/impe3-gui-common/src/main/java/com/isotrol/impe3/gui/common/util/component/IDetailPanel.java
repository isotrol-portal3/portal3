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

package com.isotrol.impe3.gui.common.util.component;


/**
 * Represents panels that can be checked for validation and dirt (a Component is dirty if some value
 * is different from the original value).<br/>
 * 
 * @author Andrei Cojocaru
 *
 */
public interface IDetailPanel extends IFieldPanel {
	
	/**
	 * <br/>
	 * @return <code>true</code> if detail panel of an existing element in the system, <code>false</code>
	 * if element is being created.
	 */
	boolean isEdition();
}
