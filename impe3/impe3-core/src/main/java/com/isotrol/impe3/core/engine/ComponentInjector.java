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

import javax.xml.transform.Source;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Cookies;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Headers;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Principal;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.SessionParams;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.component.ComponentDefinition;


/**
 * Component injector. This class is NOT THREAD-SAFE.
 * @author Andres Rodriguez
 */
public final class ComponentInjector {
	private final ComponentDefinition<?> dfn;
	private final Component component;
	private final ComponentRequestContext context;

	public static <T extends Component> T inject(ComponentDefinition<?> dfn, T component, ComponentRequestContext context) {
		new ComponentInjector(dfn, component, context).inject();
		return component;
	}

	private ComponentInjector(ComponentDefinition<?> dfn, Component component, ComponentRequestContext context) {
		this.dfn = checkNotNull(dfn);
		this.component = checkNotNull(component);
		this.context = checkNotNull(context);
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

	private <T> void direct(Class<T> valueType, T value, String msg) {
		try {
			dfn.getDirectInjectors().inject(component, valueType, value);
		} catch (Exception e) {
			log(msg);
		}
	}

	private <T> void bei(Class<T> valueType, T value, String what) {
		try {
			dfn.getBindingErrorsInjectors().inject(component, valueType, value);
		} catch (Exception e) {
			log("Unable to inject " + what + "binding errors");
		}
	}

	private void inject() {
		// Direct Injection
		direct(UUID.class, context.getComponentId(), "Unable to inject id");
		final PrincipalContext principalContext = context.getPrincipalContext();
		direct(PrincipalContext.class, principalContext, "Unable to inject principal context");
		direct(Principal.class, principalContext.getPrincipal(), "Unable to inject principal");
		direct(Device.class, context.getDevice(), "Unable to inject device");
		direct(Locale.class, context.getLocale(), "Unable to inject locale");
		direct(PageKey.class, context.getPageKey(), "Unable to inject page key");
		direct(NavigationKey.class, context.getNavigationKey(), "Unable to inject navigation key");
		direct(ContentKey.class, context.getContentKey(), "Unable to inject content key");
		direct(Content.class, context.getContent(), "Unable to inject content");
		direct(Listing.class, context.getListing(), "Unable to inject listing");
		direct(Pagination.class, context.getPagination(), "Unable to inject pagination");
		direct(Route.class, context.getRoute(), "Unable to inject route");
		final Portal portal = context.getPortal();
		direct(Portal.class, portal, "Unable to inject portal");
		direct(ContentLoader.class, context.getContentLoader(), "Unable to inject content loader");
		direct(TemplateKey.class, context.getTemplateKey(), "Unable to inject template key");
		direct(TemplateModel.class, context.getTemplateModel(), "Unable to inject template model");
		direct(Source.class, context.getSource(), "Unable to inject XML Source");
		direct(ETag.class, context.getETag(), "Unable to inject parent partial ETag");
		direct(ContentTypes.class, context.getPortal().getContentTypes(), "Unable to inject content types");
		direct(Categories.class, context.getPortal().getCategories(), "Unable to inject caterories");
		direct(URIGenerator.class, context, "Unable to inject URI generator");
		direct(ComponentRequestContext.class, context, "Unable to inject component request context");
		// Parameters
		Headers headers = context.getHeaders();
		Cookies cookies = context.getCookies();
		RequestParams rp = context.getRequestParams();
		SessionParams sp = context.getSessionParams();
		LocalParams lp = context.getLocalParams();
		// Collection injection
		direct(Headers.class, headers, "Unable to inject headers collection");
		direct(Cookies.class, cookies, "Unable to inject cookie collection");
		direct(RequestParams.class, rp, "Unable to inject request parameters collection");
		direct(SessionParams.class, sp, "Unable to inject session parameters collection");
		direct(LocalParams.class, lp, "Unable to inject local parameters collection");
		// Parameters injection
		dfn.getHeaderInjectors().inject(component, headers);
		cookies = dfn.getCookieInjectors().inject(component, cookies);
		rp = dfn.getRequestInjectors().inject(component, rp);
		sp = dfn.getSessionInjectors().inject(component, sp);
		lp = dfn.getLocalInjectors().inject(component, lp);
		// Binding error injections
		bei(Cookies.class, cookies, "cookie");
		bei(RequestParams.class, rp, "request parameters");
		bei(SessionParams.class, sp, "session parameters");
		bei(LocalParams.class, lp, "local parameters");
		// Portal properties
		try {
			dfn.getPropertyInjectors().inject(component, portal);
		} catch (Exception e) {
			log("Unable to inject portal properties");
		}
		// Portal bases
		try {
			dfn.getBaseInjectors().inject(component, portal);
		} catch (Exception e) {
			log("Unable to inject portal bases");
		}
	}
}
