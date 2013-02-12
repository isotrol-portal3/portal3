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

package com.isotrol.impe3.pms.core.engine;


import java.util.Locale;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.HttpRequestContext;
import com.isotrol.impe3.api.PortalException;
import com.isotrol.impe3.api.PrincipalContext;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.OfflineEngine;
import com.isotrol.impe3.core.WebExceptions;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.core.engine.DefaultOnlineEngine;
import com.isotrol.impe3.core.engine.SessionPrincipalContext;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.sun.jersey.spi.inject.Inject;


/**
 * Base class for IMPE JAX-RS resources.
 */
public abstract class AbstractResource {
	@Context
	private ServletConfig servletConfig;
	@Context
	private HttpServletRequest servletRequest;
	@Autowired
	private FileLoader fileLoader;
	@Inject
	private EngineModelLoader loader;
	final Logger logger = Loggers.page();

	public AbstractResource() {
	}

	protected final HttpSession getSession() {
		return servletRequest.getSession();
	}

	protected final boolean isOnline() {
		return "ONLINE".equals(servletConfig.getInitParameter("impe.mode"));
	}

	protected final FileLoader getFileLoader() {
		return fileLoader;
	}

	protected UUID toUUID(String id, String what) {
		try {
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw WebExceptions.notFound(String.format("%s [%s] not found", what, id));
		}
	}

	protected UUID toPortalUUID(String id) {
		return toUUID(id, "Portal Id");
	}

	protected UUID toPageUUID(String id) {
		return toUUID(id, "Page Id");
	}

	private Function<UUID, PrincipalContext> getPCB() {
		return new Function<UUID, PrincipalContext>() {
			public PrincipalContext apply(UUID from) {
				return new SessionPrincipalContext(getSession(), from);
			}
		};
	}

	protected OfflineEngine getOfflineEngine() {
		if (isOnline()) {
			throw WebExceptions.notFound("Offline engine not found");
		}
		final EngineModel model = loader.getOffline(EnvironmentManager.NAME);
		final OfflineEngine engine = new DefaultOfflineEngine(model, getPCB());
		return engine;
	}

	protected Engine getOnlineEngine() {
		if (!isOnline()) {
			throw WebExceptions.notFound("Online engine not found");
		}
		try {
			final EngineModel model = loader.getOnline(EnvironmentManager.NAME);
			final Engine engine = new DefaultOnlineEngine(model, getPCB());
			return engine;
		} catch (PMSException e) {
			throw new PortalException(e);
		}
	}

	protected Engine getEngine() {
		return isOnline() ? getOnlineEngine() : getOfflineEngine();
	}

	protected void setActionRequest(Engine engine, UUID portalId, UUID deviceId, Locale locale, String action,
		HttpHeaders headers, HttpRequestContext request, MultivaluedMap<String, String> query, HttpSession session) {
		ActionRequest.set(new ActionRequest(engine, portalId, deviceId, locale, action, headers, request, query,
			session));
	}

}
