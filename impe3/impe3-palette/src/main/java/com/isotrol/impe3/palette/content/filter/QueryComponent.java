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


import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;


/**
 * Component to apply queries to transform the current criteria
 * @author Manuel Ruiz
 * 
 */
@Name("Componente de consultas")
@Description("Componente funcional que genera una consulta para aplic\u00e1rsela a un componente que use el ContentLoader")
public class QueryComponent implements Component {

	/** Query Component configuration */
	private QueryConfig config;

	/** Content criteria transformer */
	private ContentCriteriaTransformer criteriaTransformer;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {

		if (config != null && StringUtils.hasText(config.value())) {
			String field = config.field();
			String value = config.value();
			QueryType type = config.type();
			ConditionalClause occurr = config.condition();
			NodeQuery query = null;

			if (type.equals(QueryType.TERM_QUERY)) {
				query = NodeQueries.term(field, value);
			} else if (type.equals(QueryType.WILDCARD_QUERY)) {
				query = NodeQueries.wildcard(field, value);
			} else if (type.equals(QueryType.PARSE_QUERY)) {
				query = NodeQueries.string(field, value);
			}

			criteriaTransformer = ContentCriteriaTransformer.addQuery(query, occurr.getOccur());
		}

		return ComponentResponse.OK;
	}

	/**
	 * @param config the config to set
	 */
	@Inject
	public void setConfig(QueryConfig config) {
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
