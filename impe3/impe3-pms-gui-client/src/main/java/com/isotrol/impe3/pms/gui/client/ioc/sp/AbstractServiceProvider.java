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

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.gui.common.util.Settings;

/**
 * Base class for all async service providers
 * @author Andrei Cojocaru
 *
 * @param <T> Async service interface type
 */
public abstract class AbstractServiceProvider<T> implements Provider<T> {
	/**
	 * Base URL for RPC calls.<br/>
	 */
	private static String rpcBaseUrl = null;
	
	/**
	 * GuiCommon settings bundle.<br/>
	 */
	private Settings settings = null;
	
	/**
	 * base URL for RPC. The result must be prepended to each service mapping
	 * (defined server-side in the service exporters.).
	 * 
	 * @return base URL for RPC
	 */
	protected final String getRpcBaseUrl() {
		if (rpcBaseUrl == null) {
			if (GWT.isScript()) {
				rpcBaseUrl = Util.getBaseApplicationContext() + settings.rpcServletMapping();
			} else {
				rpcBaseUrl = settings.rpcBaseUrlHostedMode();
			}
		}
		return rpcBaseUrl;
	}
	
	/**
	 * Injects the common settings bundle.
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
