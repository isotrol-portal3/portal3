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

package com.isotrol.impe3.freemarker.wrap;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.freemarker.wrap.ModelUtils.buildURIQueryParameters;
import static com.isotrol.impe3.freemarker.wrap.ModelUtils.notNullArgs;
import static com.isotrol.impe3.freemarker.wrap.ModelUtils.uri;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.freemarker.FreeMarkerFunction;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * FreeMarker model for a route URI builder.
 * @author Andres Rodriguez
 */
final class URIBuilderModel extends AbstractImpeModel {
	/** Destination route. */
	private final Route route;
	/** Current parameters. */
	private final Multimap<String, String> parameters = LinkedListMultimap.create();
	/** Current parameters. */
	private final Map<String, TemplateModel> map = ImmutableMap.<String, TemplateModel> builder().put("uri", new Uri())
		.put("absUri", new AbsUri()).put("clear", new Clear()).put("putCurrent", new PutCurrent())
		.put("replaceWithCurrent", new ReplaceWithCurrent()).build();

	/**
	 * Constructor.
	 * @param wrapper Object wrapper.
	 * @param route Destination route.
	 */
	URIBuilderModel(PortalObjectWrapper wrapper, Route route) {
		super(wrapper);
		this.route = checkNotNull(route);
	}

	@Override
	Map<String, TemplateModel> getModels() {
		return map;
	}

	private Multimap<String, String> merge(List<String> args) {
		Multimap<String, String> extra = buildURIQueryParameters(args, 0);
		if (extra.isEmpty()) {
			return parameters;
		}
		Multimap<String, String> total = LinkedListMultimap.create(parameters);
		total.putAll(extra);
		return total;
	}

	private class Uri extends FreeMarkerFunction {
		@Override
		public Object apply(List<String> args) throws TemplateModelException {
			return uri(getContext().getURI(route, merge(args)));
		}
	}

	private class AbsUri extends FreeMarkerFunction {
		@Override
		public Object apply(List<String> args) throws TemplateModelException {
			return uri(getContext().getAbsoluteURI(route, merge(args)));
		}
	}

	private class Clear extends FreeMarkerFunction {
		@Override
		public Object apply(List<String> args) throws TemplateModelException {
			parameters.clear();
			return URIBuilderModel.this;
		}
	}

	private class PutCurrent extends FreeMarkerFunction {
		@Override
		public Object apply(List<String> args) throws TemplateModelException {
			final RequestParams rp = getContext().getRequestParams();
			for (String name : notNullArgs(args)) {
				parameters.putAll(name, rp.get(name));
			}
			return URIBuilderModel.this;
		}
	}

	private class ReplaceWithCurrent extends FreeMarkerFunction {
		@Override
		public Object apply(List<String> args) throws TemplateModelException {
			final RequestParams rp = getContext().getRequestParams();
			for (String name : notNullArgs(args)) {
				parameters.replaceValues(name, rp.get(name));
			}
			return URIBuilderModel.this;
		}
	}

}
