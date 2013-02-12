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


import java.util.List;

import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.nr.api.NRBooleanClause;
import com.isotrol.impe3.nr.api.NRBooleanClause.Occur;
import com.isotrol.impe3.nr.api.NRBooleanQuery;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;


/**
 * Component to apply queries based on a run-time parameter to transform the current criteria.
 * @author Manuel Ruiz
 * @author Andres Rodriguez
 */
@Name("Componente de consultas con Par\u00e1metro en tiempo de ejecuci\u00e1n")
@Description("Componente funcional que genera una consulta para aplic\u00e1rsela a un componente que use el ContentLoader")
public class ParametrizedQueryComponent implements Component {
	/** Component request context. */
	private ComponentRequestContext context;
	/** Query Component configuration */
	private ParametrizedQueryConfig config;
	/** Content criteria transformer */
	private ContentCriteriaTransformer criteriaTransformer;

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {

		if (config != null) {
			if (config.allowMultiple()) {
				multi();
			} else {
				simple();
			}
		}

		return ComponentResponse.OK;
	}

	private NodeQuery q(String value) {
		final String field = config.field();
		final QueryType type = config.type();
		if (type.equals(QueryType.TERM_QUERY)) {
			return NodeQueries.term(field, value);
		} else if (type.equals(QueryType.WILDCARD_QUERY)) {
			return NodeQueries.wildcard(field, value);
		} else if (type.equals(QueryType.PARSE_QUERY)) {
			return NodeQueries.string(field, value);
		}
		return null;
	}

	private void add(NodeQuery q) {
		ContentCriteriaTransformer t = ContentCriteriaTransformer.addQuery(q, config.condition().getOccur());
		if (criteriaTransformer == null) {
			criteriaTransformer = t;
		} else {
			criteriaTransformer = criteriaTransformer.append(t);
		}
	}

	private void add(String value) {
		criteriaTransformer = ContentCriteriaTransformer.addQuery(q(value), config.condition().getOccur());
	}

	private void simple() {
		String value = getValue();
		if (StringUtils.hasText(value)) {
			add(value);
		}
	}

	private void multi() {
		List<String> values = getValues();
		if (values == null || values.isEmpty()) {
			return;
		}
		if (values.size() == 1) {
			add(values.get(0));
		} else if (config.consolidate() == null) {
			for (String value : values) {
				add(value);
			}
		} else {
			Occur o = config.consolidate().getOccur();
			NRBooleanQuery q = NodeQueries.bool();
			for (String value : values) {
				q.add(NRBooleanClause.create(q(value), o));
			}
			add(q);
		}
	}

	private String getValue() {
		String param = config.parameter();
		switch (config.parameterSource()) {
			case LOCAL:
				if (context.getLocalParams().contains(param)) {
					return getObjectValue(context.getLocalParams().get(param));
				}
				return null;
			case REQUEST:
				if (context.getRequestParams().contains(param)) {
					return context.getRequestParams().getFirst(param);
				}
				return null;
			case SESSION:
				if (context.getSessionParams().contains(param)) {
					return getObjectValue(context.getSessionParams().get(param));
				}
				return null;
			default:
				return null;
		}
	}

	private List<String> getValues() {
		String param = config.parameter();
		switch (config.parameterSource()) {
			case LOCAL:
				if (context.getLocalParams().contains(param)) {
					return getObjectValues(context.getLocalParams().get(param));
				}
				break;
			case REQUEST:
				if (context.getRequestParams().contains(param)) {
					return context.getRequestParams().get(param);
				}
				break;
			case SESSION:
				if (context.getSessionParams().contains(param)) {
					return getObjectValues(context.getSessionParams().get(param));
				}
				break;
			default:
				break;
		}
		return ImmutableList.of();
	}

	private String getObjectValue(Object o) {
		if (o != null) {
			return o.toString();
		}
		return null;
	}

	private List<String> getObjectValues(Object o) {
		if (o instanceof Iterable) {
			List<String> list = Lists.newLinkedList();
			Iterable<?> iterable = (Iterable<?>) o;
			for (Object item : iterable) {
				String s = getObjectValue(item);
				if (s != null) {
					list.add(s);
				}
			}
			return list;
		} else if (o != null) {
			return ImmutableList.of(o.toString());
		}
		return ImmutableList.of();
	}

	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

	/**
	 * @param config the config to set
	 */
	@Inject
	public void setConfig(ParametrizedQueryConfig config) {
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
