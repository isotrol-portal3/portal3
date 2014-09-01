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
