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
package com.isotrol.impe3.pms.gui.client.ioc;

import com.google.gwt.core.client.GWT;

/**
 * Singleton for the {@link IPmsFactory} interface.
 * This way it can be accessed everywhere.<br/>
 * The Singleton is eagerly created.
 * 
 * @author Andrei Cojocaru
 *
 */
public final class PmsFactory {
	/**
	 * private constructor - not instantiable class.
	 */
	private PmsFactory() {}
	
	/**
	 * static Singleton instance.<br/>
	 */
	private static final IPmsFactory INSTANCE = GWT.create(IPmsFactory.class);
	
	/**
	 * @return Singleton instance.
	 */
	public static final IPmsFactory getInstance() {
		return INSTANCE;
	}

}
