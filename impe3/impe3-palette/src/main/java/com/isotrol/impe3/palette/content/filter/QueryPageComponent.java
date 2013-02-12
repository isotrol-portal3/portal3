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

package com.isotrol.impe3.palette.content.filter;


import net.sf.derquinse.lucis.Page;

import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.support.listing.ContentListingPage;


/**
 * Component to get a page of results from the current criteria.
 * @author Manuel Ruiz
 * 
 */
@Name("Componente de Consulta - P\u00E1gina")
@Description("Componente funcional que devulve una p\u00E1gina de una consulta")
public class QueryPageComponent implements Component {

	/** Content loader. */
	private ContentLoader contentLoader;
	/** Pagination */
	private Pagination pagination;
	/** Contents */
	private Listing<Content> contents;
	/** Configuration. */
	private QueryBytesConfig config;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		final ContentCriteria criteria = contentLoader.newCriteria();
		criteria.setBytes(config != null && config.bytes());
		Page<Content> page = criteria.getPage(pagination);
		contents = new ContentListingPage(page.getTotalHits(), pagination, page.getItems());
		return ComponentResponse.OK;
	}
	
	@Inject
	public void setConfig(QueryBytesConfig config) {
		this.config = config;
	}

	@Inject
	public void setContentLoader(ContentLoader contentLoader) {
		this.contentLoader = contentLoader;
	}

	/**
	 * @param pagination the pagination to set
	 */
	@Inject
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	/**
	 * Content extractor.
	 * @return Loaded content.
	 */
	@Extract
	public Listing<?> getContents() {
		return this.contents;
	}
}
