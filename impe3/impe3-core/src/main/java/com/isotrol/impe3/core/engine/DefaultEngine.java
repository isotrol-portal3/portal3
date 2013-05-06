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
import static com.isotrol.impe3.core.WebExceptions.actionFound;
import static com.isotrol.impe3.core.WebExceptions.found;
import static com.isotrol.impe3.core.engine.RequestContexts.exceptionPage;
import static com.isotrol.impe3.core.engine.RequestContexts.redirectedPage;

import java.util.EnumMap;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.NotFoundPortalException;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PageResolutionParams;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.PortalException;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.api.RequestContext;
import com.isotrol.impe3.api.ResolvedDevice;
import com.isotrol.impe3.api.ResolvedLocale;
import com.isotrol.impe3.api.ResolvedPage;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.ActionContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ComponentResponse.External;
import com.isotrol.impe3.api.component.ComponentResponse.Internal;
import com.isotrol.impe3.api.support.DefaultDeviceCapabilities;
import com.isotrol.impe3.core.CIP;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.Page;
import com.isotrol.impe3.core.PageContext;
import com.isotrol.impe3.core.PageResponse;
import com.isotrol.impe3.core.PageResult;
import com.isotrol.impe3.core.PageResult.Early;
import com.isotrol.impe3.core.PageResult.Ok;
import com.isotrol.impe3.core.Pages;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.RenderingEngine;
import com.isotrol.impe3.core.WebExceptions;
import com.isotrol.impe3.core.support.RouteParams;


/**
 * Default engine implementation. This engine provides a per-request implementation so IT IS NOT THREAD-SAFE.
 */
public abstract class DefaultEngine implements Engine {
	private static final EnumMap<DeviceType, Supplier<RenderingEngine<?>>> RENDERING_ENGINES;
	private static final Function<UUID, PrincipalContext> DEFAULT_PRINCIPAL_CONTEXT = new Function<UUID, PrincipalContext>() {
		public PrincipalContext apply(UUID from) {
			return new SimplePrincipalContext();
		}
	};

	static {
		RENDERING_ENGINES = Maps.newEnumMap(DeviceType.class);
		RENDERING_ENGINES.put(DeviceType.HTML, HTMLRenderingEngine.SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.XHTML, XHTMLRenderingEngine.SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.ATOM, ATOMRenderingEngine.SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.SITEMAP, SitemapRenderingEngine.SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.XLS, BinaryRenderingEngine.EXCEL_SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.XLSX, BinaryRenderingEngine.EXCEL_SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.PDF, BinaryRenderingEngine.PDF_SUPPLIER);
		RENDERING_ENGINES.put(DeviceType.ICMS, ICMSFragmentRenderingEngine.SUPPLIER);
	}

	private final EngineModel model;
	private final Function<UUID, PrincipalContext> principalContextBuilder;
	private final Logger logger = Loggers.page();

	DefaultEngine(final EngineModel model, Function<UUID, PrincipalContext> principalContextBuilder) {
		this.model = checkNotNull(model);
		if (principalContextBuilder == null) {
			this.principalContextBuilder = DEFAULT_PRINCIPAL_CONTEXT;
		} else {
			this.principalContextBuilder = principalContextBuilder;
		}
	}

	DefaultEngine(final EngineModel model) {
		this(model, null);
	}

	EngineModel getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.core.Engine#process(com.isotrol.impe3.api.PathSegments, javax.ws.rs.core.HttpHeaders,
	 * com.isotrol.impe3.api.HttpRequestContext)
	 */
	public final PageResponse process(PathSegments path, HttpHeaders headers, HttpRequestContext request) {
		if (logger.isTraceEnabled()) {
			logger.trace("Processing page request " + path.toString());
		}
		// 1 - Portal resolution
		final ResolvedPortal resolvedPortal = getPortal(path, headers, request);
		final PortalModel portalModel = resolvedPortal.getPortalModel();
		// 2 - Device resolution
		final DeviceCapabilities caps = getDeviceCapabilities(portalModel, headers);
		final DeviceResolutionParams deviceParams = new DeviceResolutionParams(headers, request,
			resolvedPortal.getPortal(), resolvedPortal.getPath(), resolvedPortal.getPath(),
			resolvedPortal.getParameters(), model.getDevices(), caps);
		final ResolvedDevice resolvedDevice = getDevice(portalModel, deviceParams);
		if (!resolvedDevice.isNormal()) {
			return new PageResponse(resolvedDevice.getResponse(), ImmutableMap.<String, Object> of());
		}
		// 3 - Locale resolution
		final LocaleResolutionParams localeParams = new LocaleResolutionParams(deviceParams, resolvedDevice);
		final ResolvedLocale resolvedLocale = getLocale(portalModel, localeParams);
		if (!resolvedLocale.isNormal()) {
			return new PageResponse(resolvedLocale.getResponse(), ImmutableMap.<String, Object> of());
		}
		// 4 - Page resolution
		// 4.1 - Context building
		final Portal portal = portalModel.getPortal();
		final UUID portalId = portal.getId();
		final PrincipalContext pc = principalContextBuilder.apply(portalId);
		final Device device = resolvedDevice.getDevice();
		final Locale locale = resolvedLocale.getLocale();
		final ClientRequestContext crc = RequestContexts.client(device, resolvedDevice.getCapabilities(), locale);
		final RequestContext rc = RequestContexts.request(crc, request);
		final PortalRequestContext portalRequestContext = RequestContexts.portal(portalModel.getModelInfo(), rc,
			portalModel.getURIGenerator(), pc, portalModel.getContentLoader(crc));
		final PageResolutionParams pageParams = new PageResolutionParams(localeParams, resolvedLocale,
			portalRequestContext);
		final ResolvedPage resolvedPage = getPage(portalModel, pageParams);
		if (!resolvedPage.isNormal()) {
			return new PageResponse(resolvedPage.getResponse(), ImmutableMap.<String, Object> of());
		}
		final PageContext pageContext = getPageContext(portalModel, portalRequestContext, resolvedPage);
		if (pageContext == null) {
			PageKey pk = resolvedPage.getPage();
			if (pk == null) {
				pk = PageKey.error(NotFoundPortalException.class);
			}
			PageRequestContext prc = RequestContexts.firstRequestedPage(portalRequestContext, resolvedPage.getPath(),
				pk, resolvedPage.getParameters());
			return runErrorPage(portalModel, prc, WebExceptions.notFound("No suitable page found"), 0);
		}
		return runPage(pageContext, 0, Status.OK);
	}

	private DeviceCapabilities getDeviceCapabilities(PortalModel portalModel, HttpHeaders headers) {
		final DeviceCapabilitiesProvider dcp = portalModel.getDeviceCapabilitiesProvider();
		if (dcp != null) {
			try {
				return dcp.getDeviceCapabilities(headers);
			} catch (RuntimeException e) {
				Loggers.core().error("Unable to detect device capabilities", e);
			}
		}
		return null;
	}

	private ResolvedPortal getPortal(PathSegments path, HttpHeaders headers, HttpRequestContext request) {
		final ResolvedPortal resolvedPortal = resolvePortal(path, headers, request);
		if (resolvedPortal == null) {
			throw WebExceptions.notFound("No portal found");
		}
		return resolvedPortal;
	}

	private ResolvedDevice getDevice(PortalModel portalModel, DeviceResolutionParams params) {
		ResolvedDevice resolvedDevice = tryDevice(null, portalModel.getDeviceRouter(), params);
		resolvedDevice = tryDevice(resolvedDevice, model.getDeviceRouter(), params);
		if (resolvedDevice == null) {
			resolvedDevice = params.resolve(portalModel.getPortal().getDevice());
		}
		return resolvedDevice;
	}

	private ResolvedDevice tryDevice(ResolvedDevice resolved, DeviceRouter router, DeviceResolutionParams params) {
		if (resolved != null) {
			return resolved;
		}
		if (router != null) {
			try {
				return router.resolveDevice(params);
			} catch (RuntimeException e) {
				Loggers.core().error("Error detecting device", e);
			}
		}
		return null;
	}

	private ResolvedLocale getLocale(PortalModel portalModel, LocaleResolutionParams params) {
		final ResolvedLocale resolvedLocale = portalModel.getLocaleRouter().resolveLocale(params);
		if (resolvedLocale == null) {
			return new ResolvedLocale(params.getPath(), new Locale("es", "ES"), params.getParameters());
		}
		return resolvedLocale;
	}

	private ResolvedPage getPage(PortalModel portal, PageResolutionParams params) {
		if (logger.isTraceEnabled()) {
			logger.trace("Resolving page " + params.getPath().toString());
		}
		final PageResolver resolver = portal.getPageResolver();
		if (resolver != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Trying custom router...");
			}
			final ResolvedPage resolved = resolver.getRoute(params);
			if (resolved != null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Custom router returned path [{}], key [{}], params [{}]",
						new Object[] {resolved.getPath(), resolved.getPage(), resolved.getParameters().getNames()});
				}
				return resolved;
			}
		}
		return new ResolvedPage(params.getPath(), null, params.getParameters());
	}

	private PageContext getPageContext(PortalModel portal, PortalRequestContext context, ResolvedPage resolvedPage) {
		final Pages pages = portal.getPages();
		return pages.getPage(portal, context, resolvedPage.getPath(), resolvedPage.getParameters(),
			resolvedPage.getPage());
	}

	protected abstract ResolvedPortal resolvePortal(PathSegments path, HttpHeaders headers, HttpRequestContext request);

	private Status getStatus(Exception e) {
		if (e instanceof WebApplicationException) {
			Status s = Status.fromStatusCode(((WebApplicationException) e).getResponse().getStatus());
			return (s != null) ? s : Status.INTERNAL_SERVER_ERROR;
		} else {
			return Status.INTERNAL_SERVER_ERROR;
		}
	}

	private PageResponse runErrorPage(PortalModel portalModel, PageRequestContext context, Exception e, int redirections) {
		// Find error page.
		final PageKey errorKey = PageKey.error(e.getClass());
		final Route errorRoute = context.getRoute().toPage(errorKey);
		final Page destination = portalModel.getPages().getPage(errorRoute);
		if (destination == null) {
			logger.error("No suitable error page found. Rethrowing exception.");
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new PortalException(e);
			}
		}
		final PageContext newPageContext = new PageContext(portalModel, exceptionPage(context, errorKey, e),
			destination);
		return runPage(newPageContext, redirections, getStatus(e));
	}

	/**
	 * Runs a page.
	 * @param pageContext Page context to run.
	 * @param redirections Internal redirections performed so far.
	 * @param status Default status.
	 * @return The page response.
	 */
	private PageResponse runPage(final PageContext pageContext, int redirections, Status status) {
		if (redirections >= 100) {
			throw new RuntimeException("Too many redirections");
		}
		final PageRequestContext context = pageContext.getContext();
		final PortalModel model = pageContext.getPortalModel();
		try {
			final PageInstance pi = new PageInstance(pageContext);
			// Run the page
			final PageResult pr;
			try {
				pr = pi.run();
			} catch (Exception e) {
				logger.error("Exception thrown while processing page.", e);
				if (context.getException() != null) {
					logger.error("The exception was thrown while processing an error page. Rethrowing.", e);
					if (e instanceof RuntimeException) {
						throw (RuntimeException) e;
					}
					throw new RuntimeException(e); // TODO
				}
				return runErrorPage(model, context, e, redirections);
			}
			// Early response
			if (pr instanceof Early) {
				if (logger.isTraceEnabled()) {
					logger.trace("Early response requested");
				}
				return ((Early) pr).getPageResponse();
			}
			// Ok
			if (pr instanceof Ok) {
				return render(model, pi, (Ok) pr, Response.status(status));
			}
			// Redirection
			if (logger.isTraceEnabled()) {
				logger.trace("Redirection requested");
			}
			final ComponentResponse.Redirection cr = ((PageResult.Redirection) pr).getRedirection();
			// External
			if (cr instanceof External) {
				if (logger.isTraceEnabled()) {
					logger.trace("External redirection requested");
				}
				return new PageResponse(((External) cr).getResponse(), pr.getSession());
			}
			// Internal redirection
			if (logger.isTraceEnabled()) {
				logger.trace("Internal redirection requested");
			}
			final Internal internal = (Internal) cr;
			final Route nextRoute = internal.getRoute();
			final PageKey nextKey = nextRoute.getPage();
			final Page destination = model.getPages().getPage(nextRoute);
			if (destination == null) {
				if (logger.isTraceEnabled()) {
					logger.trace("Destination route not found [{}]", new Object[] {internal.getRoute()});
				}
				throw new RuntimeException("Internal redirection without valid destination");
				// TODO
			}
			final PageContext newPageContext = new PageContext(model, redirectedPage(context, nextKey), destination);
			return runPage(newPageContext, redirections + 1, internal.getStatus());
		} catch (RuntimeException e) {
			// TODO error processing
			throw e;
		}
	}

	final RenderingEngine<?> getRenderingEngine(PageInstance pi) {
		Supplier<RenderingEngine<?>> s = RENDERING_ENGINES.get(pi.getPage().getDevice().getType());
		if (s == null) {
			s = HTMLRenderingEngine.SUPPLIER;
		}
		return s.get();
	}

	private PageResponse render(final PortalModel model, PageInstance pi, Ok ok, ResponseBuilder rb) {
		ok.apply(rb);
		// Render response
		final Response response = getRenderingEngine(pi).render(pi, ok, rb);
		return new PageResponse(response, ok.getSession());
	}

	/**
	 * @see com.isotrol.impe3.core.Engine#getAction(java.util.UUID, java.util.UUID, java.util.Locale, java.util.UUID,
	 * java.lang.String, javax.ws.rs.core.HttpHeaders, com.isotrol.impe3.api.HttpRequestContext,
	 * javax.ws.rs.core.MultivaluedMap)
	 */
	public Object getAction(UUID portalId, UUID deviceId, Locale locale, UUID cipId, String name, HttpHeaders headers,
		HttpRequestContext request, MultivaluedMap<String, String> query) {
		final PortalModel portalModel = actionFound(model.getPortal(portalId), "Portal not found");
		final Device device = actionFound(model.getDevices().get(deviceId), "Device not found");
		final CIP cip = actionFound(portalModel.getPages().getCIP(cipId), "CIP not found");
		final Route route = actionFound(RouteParams.fromParams(portalModel.getPortal(), model.getDevices(), query),
			"Invalid source route");
		final PrincipalContext principalContext = principalContextBuilder.apply(portalId);
		// TODO
		final ClientRequestContext crc = RequestContexts.client(device, DefaultDeviceCapabilities.get(device), locale);
		final RequestContext rc = RequestContexts.request(crc, request);
		final PortalRequestContext portalRequestContext = RequestContexts.portal(portalModel.getModelInfo(), rc,
			portalModel.getURIGenerator(), principalContext, portalModel.getContentLoader(crc));
		final ActionContext context = RequestContexts.action(portalRequestContext, name, cipId, route);
		return actionFound(cip.getAction(context), "Action not found");
	}

	public final PageResponse processActionException(UUID portalId, UUID deviceId, Locale locale, HttpHeaders headers,
		HttpRequestContext request, MultivaluedMap<String, String> query, Exception exception) {
		final PortalModel portalModel = found(model.getPortal(portalId), "Portal not found");
		final Device device = found(model.getDevices().get(deviceId), "Device not found");
		final PageKey pageKey = exception == null ? PageKey.error() : PageKey.error(exception.getClass());
		final PrincipalContext principalContext = principalContextBuilder.apply(portalId);
		// TODO
		final ClientRequestContext crc = RequestContexts.client(device, DefaultDeviceCapabilities.get(device), locale);
		final RequestContext rc = RequestContexts.request(crc, request);
		// START: Route selection
		Route route = RouteParams.fromParams(portalModel.getPortal(), model.getDevices(), query);
		if (route == null) {
			route = Route.of(rc, PageKey.main());
		}
		// END: Route selection
		final PortalRequestContext portalRequestContext = RequestContexts.portal(portalModel.getModelInfo(), rc,
			portalModel.getURIGenerator(), principalContext, portalModel.getContentLoader(crc));
		final PageRequestContext pageRequestContext = RequestContexts.actionExceptionPage(portalRequestContext, route,
			pageKey, exception);
		final ResolvedPage resolvedPage = new ResolvedPage(pageRequestContext.getPath(), pageKey,
			pageRequestContext.getLocalParams());
		final PageContext pageContext = getPageContext(portalModel, portalRequestContext, resolvedPage);
		if (pageContext == null) {
			throw WebExceptions.notFound("No suitable page found");
		}
		return runPage(pageContext, 0, getStatus(exception));
	}
}
