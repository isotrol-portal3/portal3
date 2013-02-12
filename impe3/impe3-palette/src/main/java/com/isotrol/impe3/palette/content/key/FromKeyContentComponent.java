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

package com.isotrol.impe3.palette.content.key;


import net.sf.derquinse.lucis.Item;

import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * Reads a Content from ContentRepository id by contentkey
 * @author Emilio Escobar Reyero
 */
@RequiresLink(ContentKey.class)
public class FromKeyContentComponent implements Component {

	private final ContentRepository contentRepository;

	private Content content;

	private ContentKey contentKey;

	/**
	 * Need content repository.
	 * @param contentRepository repository provides by engine.
	 */
	public FromKeyContentComponent(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	/**
	 * Reads content from content repository id by contentkey.
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		if (contentKey == null) {
			throw new IllegalStateException();
		}

		final NodeQuery query = NodeQueries.nodeKey(contentKey.getContentId(), contentKey.getContentType().getId());

		final Item<Content> item = contentRepository.getFirst(query, null, true);

		content = item.getItem();

		return ComponentResponse.OK;
	}

	@Inject
	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	/**
	 * Provides content to other components.
	 * @return the content.
	 */
	@Extract
	public Content getContent() {
		return content;
	}

}
