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

package com.isotrol.impe3.palette.content.load;


import static com.isotrol.impe3.nr.api.NodeQueries.all;
import static com.isotrol.impe3.nr.api.NodeQueries.null2All;
import net.sf.derquinse.lucis.Item;

import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * Reads a page from a ContentRepository.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
@RequiresLink(ContentKey.class)
public class ContentComponent implements Component {
	/** Content repository. */
	private ContentRepository contentRepository;
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Content key. */
	private ContentKey contentKey;
	/** Component configuration. */
	private ContentConfig config;
	/** Content. */
	private Content content;

	/**
	 * Constructor.
	 */
	public ContentComponent() {
	}

	public void setContentRepository(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Inject
	public void setConfig(ContentConfig config) {
		this.config = config;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	/** Component execution. */
	public ComponentResponse execute() {
		// Content query
		if (contentKey == null) {
			content = null;
		} else {
			final NodeQuery cq = contentRepository.contentKey(contentKey);
			// Navigation query
			// TODO
			//final NodeQuery nq = contentRepository.navigationKey(navigationKey);
			final NodeQuery nq = null;
			// Filter query
			final NodeQuery f;
			if (config == null) {
				f = null;
			} else {
				f = all(contentRepository.category(config.category()), contentRepository.tag(config.tag()),
					contentRepository.contentType(config.contentType()));
			}
			// Final query
			final NodeQuery q = null2All(all(cq, nq, f));
			final Item<Content> item = contentRepository.getFirst(q, null, true);
			content = item.getItem();
		}
		return ComponentResponse.OK;
	}

	@Extract
	public Content getContent() {
		return content;
	}
}
