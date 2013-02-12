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
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.nr.api.NRSortField;


/**
 * Component to apply sorts to transform the current criteria
 * @author Manuel Ruiz
 * 
 */
@Name("Componente de Ordenaciones")
@Description("Componente funcional que genera una ordenaci\u00f3n para aplic\u00e1rsela a un componente que use el ContentLoader")
public class SortComponent implements Component {

	/** Sort Component configuration */
	private SortConfig config;

	/** Content criteria transformer */
	private ContentCriteriaTransformer criteriaTransformer;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {

		if (config != null) {
			NRSortField sort = NRSortField.of(config.field(), config.type().equals(SortType.DESC));
			criteriaTransformer = ContentCriteriaTransformer.sort(sort);
		}

		return ComponentResponse.OK;
	}

	/**
	 * @param config the config to set
	 */
	@Inject
	public void setConfig(SortConfig config) {
		this.config = config;
	}

	/**
	 * @return the criteriaTransformer
	 */
	@Extract
	public ContentCriteriaTransformer getCriteriaTransformer() {
		return criteriaTransformer;
	}
}
