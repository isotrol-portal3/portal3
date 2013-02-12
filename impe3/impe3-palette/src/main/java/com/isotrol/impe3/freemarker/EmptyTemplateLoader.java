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

package com.isotrol.impe3.freemarker;


import java.io.IOException;
import java.io.Reader;

import freemarker.cache.TemplateLoader;


/**
 * Template loader based on a zip file stores as a file.
 * @author Andres Rodriguez
 */
public class EmptyTemplateLoader implements TemplateLoader {

	public EmptyTemplateLoader() {
	}

	/**
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	public void closeTemplateSource(Object templateSource) throws IOException {
	}

	/**
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	public Object findTemplateSource(String templateSource) throws IOException {
		return null;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	public long getLastModified(Object templateSource) {
		return -1L;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object, java.lang.String)
	 */
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return null;
	}

}
