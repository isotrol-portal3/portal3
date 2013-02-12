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


import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Component to apply a filter by content type.
 * @author Andres Rodriguez
 * 
 */
@Name("Filtro por Tipo de Contenido")
public class ContentTypeFilterComponent implements Component {
	/** Configuration. */
	private ContentTypeFilterConfig config;
	/** Content criteria transformer */
	private ContentCriteriaTransformer criteriaTransformer = null;

	/** Constructor. */
	public ContentTypeFilterComponent() {
	}

	@Inject
	public void setConfig(ContentTypeFilterConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		criteriaTransformer = new ContentCriteriaTransformer() {
			@Override
			public void apply(ContentCriteria criteria) {
				criteria.contentTypes().apply(config.contentType(), config.filterType().getFilterType());
			}
		};
		return ComponentResponse.OK;
	}

	/**
	 * @return the criteriaTransformer
	 */
	@Extract
	public ContentCriteriaTransformer getCriteriaTransformer() {
		return criteriaTransformer;
	}
}
