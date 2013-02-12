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

import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.Request;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.ModelInfo;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageKey.ContentPage;
import com.isotrol.impe3.api.PageKey.WithNavigation;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.core.impl.LocalParamsFactory;


/**
 * Request Contexts factory methods.
 * @author Andres Rodriguez
 */
public final class RequestContexts {
	/** Not instantiable. */
	private RequestContexts() {
		throw new AssertionError();
	}

	/**
	 * Creates a new client context.
	 * @param device Device.
	 * @param deviceCapabilities Device capabilities.
	 * @param locale Locale.
	 * @return The created context (immutable).
	 */
	public static ClientRequestContext client(Device device, DeviceCapabilities deviceCapabilities, Locale locale) {
		return new ImmutableClientRequestContext(device, deviceCapabilities, locale);
	}

	static ImmutableClientRequestContext copyOf(ClientRequestContext context) {
		checkNotNull(context);
		if (context instanceof ImmutableClientRequestContext) {
			return (ImmutableClientRequestContext) context;
		}
		return new ImmutableClientRequestContext(context.getDevice(), context.getDeviceCapabilities(),
			context.getLocale());
	}

	public static HttpRequestContext http() {
		return ImmutableHttpRequestContext.EMPTY;
	}

	public static ImmutableHttpRequestContext http(Request request, boolean secure, Headers headers, Cookies cookies,
		RequestParams requestParams, SessionParams sessionParams) {
		return new ImmutableHttpRequestContext(request, secure, headers, cookies, requestParams, sessionParams);
	}

	static ImmutableHttpRequestContext copyOf(HttpRequestContext context) {
		checkNotNull(context);
		if (context instanceof ImmutableHttpRequestContext) {
			return (ImmutableHttpRequestContext) context;
		}
		return new ImmutableHttpRequestContext(context.getJAXRSRequest(), context.isSecure(), context.getHeaders(),
			context.getCookies(), context.getRequestParams(), context.getSessionParams());
	}

	public static RequestContext request(ClientRequestContext clientContext, HttpRequestContext httpContext) {
		return new ImmutableRequestContext(clientContext, httpContext);
	}

	public static PortalRequestContext portal(ModelInfo modelInfo, RequestContext requestContext,
		URIGenerator uriGenerator, PrincipalContext principalContext, ContentLoader contentLoader) {
		return new ImmutablePortalRequestContext(modelInfo, requestContext, uriGenerator, principalContext,
			contentLoader);
	}

	public static PageRequestContext firstRequestedPage(PortalRequestContext context, PathSegments path,
		PageKey pageKey, LocalParams localParams) {
		return new ImmutablePageRequestContext(context, path, pageKey, localParams, null);
	}

	public static PageRequestContext actionExceptionPage(PortalRequestContext context, Route route, PageKey pageKey,
		Exception exception) {
		return new ImmutablePageRequestContext(context, PathSegments.of(), pageKey, LocalParamsFactory.of(), exception);
	}

	static PageRequestContext exceptionPage(PageRequestContext context, PageKey pageKey, Exception exception) {
		return new ImmutablePageRequestContext(context, pageKey, null, exception);
	}

	static PageRequestContext redirectedPage(PageRequestContext context, PageKey pageKey) {
		return new ImmutablePageRequestContext(context, pageKey, null, null);
	}

	public static ComponentRequestContext component(PageRequestContext context, UUID componentId) {
		return firstLevelComponent(context, componentId);
	}

	static ImmutableComponentRequestContext firstLevelComponent(PageRequestContext context, UUID componentId) {
		final PageKey pageKey = context.getPageKey();
		NavigationKey nk = null;
		ContentKey ck = null;
		if (pageKey instanceof WithNavigation) {
			nk = ((WithNavigation) pageKey).getNavigationKey();
		}
		if (pageKey instanceof ContentPage) {
			ck = ((ContentPage) pageKey).getContentKey();
		}
		return new ImmutableComponentRequestContext(context, componentId, ImmutableMap.<String, UUID> of(), null, null,
			ck, nk, null, null, null, null, null, null, null, null);
	}

	/**
	 * Contructor.
	 * @param context Portal contexts.
	 * @param name Action name.
	 * @param id Calling CIP Id.
	 * @param route Calling route (if known).
	 * @return The requested action.
	 */
	public static ActionContext action(PortalRequestContext context, String name, UUID id, Route route) {
		return new ImmutableActionContext(context, name, id, route);
	}

	public static RenderContext render(ComponentRequestContext delegate, Integer width, Multimap<String, String> query) {
		return new ImmutableRenderContext(delegate, width, query);
	}

}
