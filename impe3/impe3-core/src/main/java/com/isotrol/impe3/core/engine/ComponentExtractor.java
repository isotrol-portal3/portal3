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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.transform.Source;

import com.google.common.collect.Maps;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.Action;
import com.isotrol.impe3.api.component.CacheMode;
import com.isotrol.impe3.api.component.CacheScope;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.Expires;
import com.isotrol.impe3.api.component.LastModified;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteriaTransformer;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.PageResult;
import com.isotrol.impe3.core.component.ActionExtractors;
import com.isotrol.impe3.core.component.ComponentDefinition;
import com.isotrol.impe3.core.component.CookieExtractors;
import com.isotrol.impe3.core.component.DirectExtractors;
import com.isotrol.impe3.core.component.HeaderExtractors;
import com.isotrol.impe3.core.component.LocalExtractors;
import com.isotrol.impe3.core.component.QueryExtractors;
import com.isotrol.impe3.core.component.SessionExtractors;


/**
 * Component extractor. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
final class ComponentExtractor {
	private final CIP cip;
	private final ComponentDefinition<?> dfn;
	private final Component component;
	private final PageResult.Builder b;
	private final ImmutableComponentRequestContext context;
	private final ImmutableComponentRequestContext.Builder builder;

	static ComponentExtractor create(CIP cip, Component component, PageResult.Builder b,
		ImmutableComponentRequestContext context) {
		return new ComponentExtractor(cip, component, b, context);
	}

	private ComponentExtractor(CIP cip, Component component, PageResult.Builder b,
		ImmutableComponentRequestContext context) {
		this.cip = checkNotNull(cip);
		this.dfn = cip.getDefinition();
		this.component = checkNotNull(component);
		this.b = checkNotNull(b);
		this.context = checkNotNull(context);
		this.builder = context.builder();
	}

	private void log(String msg, Object... args) {
		final Object[] p;
		if (args != null && args.length > 0) {
			p = new Object[2 + args.length];
			System.arraycopy(args, 0, p, 2, args.length);
		} else {
			p = new Object[2];
		}
		p[0] = context.getComponentId();
		p[1] = dfn.getTypeName();
		Loggers.core().warn("CIP [{}] Class [{}]: " + msg, p);
	}

	private <T> T direct(Class<T> type, String what, T fallback) {
		final DirectExtractors<?> extractors = dfn.getExtractors();
		try {
			if (extractors.contains(type)) {
				return extractors.extract(component, type);
			}
		} catch (Exception e) {
			log("Unable to extract " + what);
		}
		return fallback;
	}

	void extractSession() {
		// Extract session params
		try {
			final SessionExtractors<?> sessionExtractors = dfn.getSessionExtractors();
			b.session(sessionExtractors.extract(component));
		} catch (Exception e) {
			log("Unable to extract session parameters");
		}
	}

	ImmutableComponentRequestContext.Builder extract() {
		// NavigationKey
		builder.setNavigationKey(direct(NavigationKey.class, "navigation key", context.getNavigationKey()));
		// ContentKey
		builder.setContentKey(direct(ContentKey.class, "content key", context.getContentKey()));
		// Content
		builder.setContent(direct(Content.class, "content", context.getContent()));
		// Listing
		builder.setListing(direct(Listing.class, "listing", context.getListing()));
		// Pagination
		builder.setPagination(direct(Pagination.class, "pagination", context.getPagination()));
		// Template key
		builder.setTemplateKey(direct(TemplateKey.class, "template key", context.getTemplateKey()));
		// Template model
		builder.setTemplateModel(direct(TemplateModel.class, "template model", context.getTemplateModel()));
		// XML Source
		builder.setSource(direct(Source.class, "XML source", context.getSource()));
		// Criteria transformer
		builder.setCriteriaTransformer(direct(ContentCriteriaTransformer.class, "Content criteria transformer", null));
		// Local params
		try {
			final LocalExtractors<?> localExtractors = dfn.getLocalExtractors();
			final Map<String, Object> map = localExtractors.extract(component);
			if (!map.isEmpty()) {
				builder.setLocalParams(context.getLocalParams().apply(map));
			}
		} catch (Exception e) {
			log("Unable to extract local parameters");
		}
		// Query params
		try {
			final QueryExtractors<?> queryExtractors = dfn.getQueryExtractors();
			b.query(queryExtractors.extract(component));
		} catch (Exception e) {
			log("Unable to extract query parameters. Error [{}]", e.getMessage());
		}
		// Headers
		try {
			final HeaderExtractors<?> headerExtractors = dfn.getHeaderExtractors();
			b.headers(headerExtractors.extract(component));
		} catch (Exception e) {
			log("Unable to extract headers");
		}
		// Actions
		Map<String, UUID> ractions = null;
		try {
			final ActionExtractors<?> actionExtractors = dfn.getActionExtractors();
			final List<Action> extracted = actionExtractors.extract(component);
			if (extracted != null && !extracted.isEmpty()) {
				for (Action a : extracted) {
					final String actionName = a.getActionName();
					if (!cip.getModuleDefinition().getActions().contains(actionName)) {
						log("Exported non-existing action [{}].", actionName);
					} else {
						if (ractions == null) {
							ractions = Maps.newHashMap(context.getRegisteredActions());
						}
						ractions.put(a.getRegistrationName(), cip.getId());
					}
				}
			}
		} catch (Exception e) {
			log("Unable to extract actions");
		}
		if (ractions != null) {
			builder.setRegisteredActions(ractions);
		}
		// Cookies
		try {
			final CookieExtractors<?> cookieExtractors = dfn.getCookieExtractors();
			b.cookies(cookieExtractors.extract(component));
		} catch (Exception e) {
			log("Unable to extract cookies");
		}
		extractCache();
		return builder;
	}

	private void extractCache() {
		if (!b.isCacheOn()) {
			return;
		}
		// Cache mode
		b.setCacheMode(direct(CacheMode.class, "cache mode", dfn.getCacheMode()));
		if (!b.isCacheOn()) {
			return;
		}
		// Cache scope
		b.setCacheScope(direct(CacheScope.class, "cache scope", dfn.getCacheScope()));
		// Modification timestamp
		b.setModification(direct(LastModified.class, "modification timestamp", null));
		// Expiration timestamp
		b.setExpiration(direct(Expires.class, "expiration timestamp", null));
		// ETag
		final ETag tag;
		final ComponentETagMode cm = dfn.getETagMode();
		if (cm == ComponentETagMode.DEFAULT) {
			tag = direct(ETag.class, "partial ETag", null);
		} else {
			tag = null;
		}
		b.putETag(cip.getId(), cm, tag);
	}

}
