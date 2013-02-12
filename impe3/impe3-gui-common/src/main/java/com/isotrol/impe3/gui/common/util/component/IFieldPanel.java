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
 * Represents panels that act like fields, e.g. that display some bound data that can
 * be changed by user. Just like fields, these panels manage 
 * the <code>dirty</code> and <code>valid</code> states.
 * 
 * @author Andrei Cojocaru
 *
 */
public interface IFieldPanel {
	/**
	 * Checks if input values are changed. Same contract as {@link Field#isDirty()}.<br/>
	 * @return <code>true</code>, if the value of a field representing some property 
	 * of the bound data has changed;<code>false</code>,otherwise
	 */
	boolean isDirty();
	
	/**
	 * Checks if input values are valid.<br/>
	 * @return <code>true</code>, if the contained fields values are valid;<code>false</code> otherwise. 
	 */
	boolean isValid();
}
