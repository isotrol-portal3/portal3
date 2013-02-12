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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.MapMaker;

import freemarker.cache.TemplateLoader;


/**
 * Chaining FreeMarker Template Loader.
 * @author Andres Rodriguez
 */
public class ChainingTemplateLoader implements TemplateLoader {
	private final TemplateLoader loader1;
	private final TemplateLoader loader2;
	private final ConcurrentMap<Object, TemplateLoader> map = new MapMaker().makeMap();

	public ChainingTemplateLoader(final TemplateLoader loader1, final TemplateLoader loader2) {
		this.loader1 = checkNotNull(loader1);
		this.loader2 = checkNotNull(loader2);
	}

	private TemplateLoader get(Object source) {
		final TemplateLoader loader = map.get(source);
		checkArgument(loader != null, "Requested template source not found.");
		return loader;
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		get(templateSource).closeTemplateSource(templateSource);
	}

	public Object findTemplateSource(String templateSource) throws IOException {
		Object source = loader1.findTemplateSource(templateSource);
		if (source != null) {
			map.put(source, loader1);
			return source;
		}
		source = loader2.findTemplateSource(templateSource);
		if (source != null) {
			map.put(source, loader2);
		}
		return source;
	}

	public long getLastModified(Object templateSource) {
		return get(templateSource).getLastModified(templateSource);
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return get(templateSource).getReader(templateSource, encoding);
	}

}
