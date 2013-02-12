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
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;


/**
 * Component to apply a range query to transform the current criteria
 * @author Manuel Ruiz
 * 
 */
@Name("Componente de Consulta por Rango")
@Description("Componente funcional que genera una consulta por rango para aplic\u00e1rsela a un componente que use el ContentLoader")
public class RangeComponent implements Component {

	/** Range Component configuration */
	private RangeConfig config;

	/** Content criteria transformer */
	private ContentCriteriaTransformer criteriaTransformer;

	/** Context. */
	private ComponentRequestContext context;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {

		if (config != null && (config.initValue() != null || config.endValue() != null)) {
			String begin = config.initValue();
			if (begin != null && config.paramInitValue()) {
				begin = context.getRequestParams().getFirst(begin);
			}
			String end = config.endValue();
			if (end != null && config.paramEndValue()) {
				end = context.getRequestParams().getFirst(end);
			}
			NodeQuery query = NodeQueries.range(config.field(), begin, end, begin != null && config.includeInitValue(),
				end != null && config.includeEndValue());
			criteriaTransformer = ContentCriteriaTransformer.must(query);
		}

		return ComponentResponse.OK;
	}

	/**
	 * @param config the config to set
	 */
	@Inject
	public void setConfig(RangeConfig config) {
		this.config = config;
	}

	/** Context. */
	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

	/**
	 * @return the criteriaTransformer
	 */
	@Extract
	public ContentCriteriaTransformer getCriteriaTransformer() {
		return criteriaTransformer;
	}
}
