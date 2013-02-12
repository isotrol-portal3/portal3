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

package com.isotrol.impe3.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

import com.google.common.collect.ObjectArrays;
import com.isotrol.impe3.pms.core.AppContext;


/**
 * Context loading listener for IMPE servlet contexts.
 * @author Andres Rodriguez
 */
public final class ImpeContextLoaderListener extends ContextLoaderListener {
	/** Default constructor. */
	public ImpeContextLoaderListener() {
	}
	
	@Override
	protected ContextLoader createContextLoader() {
		return new ContextLoader() {
			@Override
			protected void customizeContext(ServletContext servletContext,
				ConfigurableWebApplicationContext applicationContext) {
				String[] locations = applicationContext.getConfigLocations();
				locations = ObjectArrays.concat(locations, AppContext.RES_PATH);
				applicationContext.setConfigLocations(locations);
			}
		};
	}
}
