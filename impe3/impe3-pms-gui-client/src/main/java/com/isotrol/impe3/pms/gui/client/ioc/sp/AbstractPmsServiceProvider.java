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
package com.isotrol.impe3.pms.gui.client.ioc.sp;

import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;

/**
 * All async services instantiated in PMS entry point must use a PmsSettings instance
 * in order to retrieve the service URL mapping. This class encloses that shared instance,
 * to avoid repeated code.
 * 
 * @author Andrei Cojocaru
 *
 * @param <T> provided async service type
 */
public abstract class AbstractPmsServiceProvider<T> extends
		AbstractServiceProvider<T> {

	/**
	 * PMS specific pmsSettings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;
	
	/**
	 * @return the pmsSettings bundle
	 */
	protected final PmsSettings getSettings() {
		return pmsSettings;
	}
	
	/**
	 * @param pmsSettings the pmsSettings to set
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}
	
}
