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

package com.isotrol.impe3.test;


import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.content.ContentLoader;


/**
 * IMPE3 Test Model.
 * @author Andres Rodriguez
 */
public interface TestModel extends IAModel {
	/** Returns the engine mode. */
	EngineMode getMode();

	/** Returns the default portal. */
	Portal getPortal();

	/** Returns the file loader. */
	FileLoader getFileLoader();

	/** Returns the URI generator. */
	URIGenerator getURIGenerator();

	/** Returns the test portal content loader. */
	ContentLoader getContentLoader();

	/** Returns the component context. */
	ComponentRequestContext getContext();
	
	/** Returns the render context. */
	RenderContext getRenderContext();
}
