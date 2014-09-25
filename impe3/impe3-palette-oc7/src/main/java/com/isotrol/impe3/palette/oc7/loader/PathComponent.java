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
package com.isotrol.impe3.palette.oc7.loader;


import net.sf.derquinse.lucis.Item;

import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.nr.api.ISO9075;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.idx.oc.api.OpenCmsSchema;


/**
 * Functional component that loads a content by OpenCMS 7 path.
 * @author Juan Manuel Valverde Ramírez
 */
public class PathComponent extends AbstractContentComponent { 
	
	/** Content OC7 path (component-level configuration). */
	private PathConfig config;

	/**
	 * Constructor.
	 * @param contentRepository repository provided by the engine.
	 */
	public PathComponent() {
	}

	/**
	 * Path to load.
	 * @param path Path to load.
	 */
	@Inject
	public void setPath(PathConfig path) {
		this.config = path;
	}
	
	@Override
	Content loadContent(ContentCriteria criteria) {
		if (config != null && config.path() != null) {
			final String path = config.path();
			final String pathEncode = ISO9075.encode(path);
			final NodeQuery query = NodeQueries.term(OpenCmsSchema.PATH, pathEncode);
			criteria.must(query);
			final Item<Content> item = criteria.getFirst();
			if (item != null) {
				return item.getItem();
			}
		}
		return null;
	}
}
