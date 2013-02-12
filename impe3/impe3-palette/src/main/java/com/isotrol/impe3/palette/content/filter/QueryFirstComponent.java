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


import net.sf.derquinse.lucis.Item;

import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Component to get the first result from the current criteria.
 * @author Manuel Ruiz
 * 
 */
@Name("Componente de Consulta - Primer Resultado")
@Description("Componente funcional que devulve el primer resultado de una consulta")
public class QueryFirstComponent implements Component {

	/** Content loader. */
	private ContentLoader contentLoader;
	/** Content. */
	private Content content = null;
	/** Configuration. */
	private QueryBytesConfig config;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		final ContentCriteria criteria = contentLoader.newCriteria();
		criteria.setBytes(config != null && config.bytes());
		Item<Content> item = criteria.getFirst();
		if (item != null) {
			content = item.getItem();
		}

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

	@Extract
	public Content getContent() {
		return content;
	}

	@Extract
	public ContentKey getContentKey() {
		if (content != null) {
			return content.getContentKey();
		}
		return null;
	}
}
