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

package com.isotrol.impe3.palette.cloud;


import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.derquinse.lucis.GroupResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.palette.content.AbstractVisualContentComponent;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public abstract class AbstractCloud extends AbstractVisualContentComponent {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/** Categories. */
	protected Categories categories;
	/** ContentTypes. */
	protected ContentTypes contentTypes;
	/** Content repository. */
	private ContentRepository contentRepository;
	/** URI Generator. */
	protected URIGenerator uriGenerator;
	protected NavigationKey navigationKey;
	/** Route. */
	protected Route route = null;
	/** Locale. */
	protected Locale locale = null;
	private CloudModuleConfig config;
	protected List<CloudItem> cloud = Collections.emptyList();
	private long backMillis = -1L;

	abstract int calculateTotalHits(GroupResult result);

	abstract List<CloudItem> generateCloud(GroupResult result, int totalHits);

	abstract List<String> fields();

	abstract NodeQuery and();

	public ComponentResponse execute() {
		if (logger.isTraceEnabled()) {
			logger.trace("Execute cloud component.");
		}
		long e = System.currentTimeMillis();
		long s = backMillis != -1 ? e - backMillis : -1L;

		if (logger.isTraceEnabled()) {
			logger.trace("BackMillis {}ms at now {}ms so {}ms.",
				new String[] {String.valueOf(backMillis), String.valueOf(e), String.valueOf(s)});
		}

		final Date end = new Date(e);
		final Date start = s != -1 ? new Date(s) : null;

		final GroupResult result = doQuery(start, end);

		cloud = generateCloud(result, calculateTotalHits(result));

		return ComponentResponse.OK;
	}

	private GroupResult doQuery(final Date start, final Date end) {
		if (logger.isTraceEnabled()) {
			logger.trace("Doing query...");
		}

		final NodeQuery and = and();
		final NodeQuery query;

		if (start == null) {
			query = and != null ? and : NodeQueries.matchAll();
		} else {
			final NodeQuery range = NodeQueries.range(Schema.DATE, start, end, true, true);
			query = and != null ? NodeQueries.all(range, and) : range;
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Query ", query.toString());
		}

		final List<String> fields = fields();

		if (logger.isTraceEnabled()) {
			logger.trace("Fields ", fields.toString());
		}

		final GroupResult result = this.contentRepository.getRepository().groupBy(query, null, fields);

		if (logger.isTraceEnabled() && result != null) {
			logger.trace("Recover {} results", result.getTotalHits());
		}

		return result;
	}

	public List<CloudItem> getCloud() {
		return cloud;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	public NavigationKey getNavigationKey() {
		return navigationKey;
	}

	public void setContentRepository(ContentRepository contentRepository) {
		this.contentRepository = contentRepository;
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
		this.locale = (route != null) ? route.getLocale() : null;
	}

	@Inject
	public void setConfig(CloudModuleConfig config) {
		if (config != null) {
			this.config = config;
			Integer days = config.backDays();
			this.backMillis = (days != null && days > 0) ? days * 24 * 60 * 60 * 1000 : -1L;
		}
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		SkeletalHTMLRenderer html = new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				final Map<String, Object> model = getFreeMarkerService().createModel(context);

				model.put("cloud", getCloud());

				return getFreeMarkerService().getFragment(config.templateFile(), context, model);
			}
		};
		return html;
	}

	public Categories getCategories() {
		return categories;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	public URIGenerator getUriGenerator() {
		return uriGenerator;
	}

	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	public ContentTypes getContentTypes() {
		return contentTypes;
	}

	public void setContentTypes(ContentTypes contentTypes) {
		this.contentTypes = contentTypes;
	}
}
