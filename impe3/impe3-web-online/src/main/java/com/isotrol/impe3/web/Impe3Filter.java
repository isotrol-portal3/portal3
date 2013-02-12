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

package com.isotrol.impe3.web;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.core.engine.ActionRequest;
import com.isotrol.impe3.pms.core.engine.Endables;
import com.isotrol.impe3.pms.core.impl.SecurityContext;


/**
 * IMPE3 Environment Filter.
 * @author Andres Rodriguez
 */
public class Impe3Filter implements Filter {
	/** Session attribute name. */
	private static final String SC_VAR = "__impe3__secctx__";

	/** Logger. */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	/** URI Logger. */
	private final Logger uriLogger = Loggers.uri();

	/** Default constructor. */
	public Impe3Filter() {
	}

	/** Nothing to do. */
	public void destroy() {
	}

	/**
	 * Fech and release security params at session.
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 * javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
		ServletException {
		logURI(request);
		fetchSC(request);
		try {
			chain.doFilter(request, response);
		}
		finally {
			ActionRequest.clear();
			Endables.end();
			releaseSC(request);
		}
	}

	/** Nothing to do. */
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	private final HttpSession getSession(ServletRequest request) {
		return ((HttpServletRequest) request).getSession();
	}

	private void logURI(ServletRequest request) {
		try {
			if (uriLogger != null && uriLogger.isDebugEnabled()) {
				final HttpServletRequest r = (HttpServletRequest) request;
				String m = String.format("Context Path[%s]; Servlet Path[%s]; Path Info[%s]; Query[%s]",
					r.getContextPath(), r.getServletPath(), r.getPathInfo(), r.getQueryString());
				uriLogger.debug(m);
			}
		} catch (Exception e) {
			// Nothing to do.
		}
	}

	private void fetchSC(ServletRequest request) {
		try {
			final HttpSession s = getSession(request);
			SecurityContext ctx = null;
			final Object o = s.getAttribute(SC_VAR);
			if (o instanceof SecurityContext) {
				ctx = (SecurityContext) o;
				if (logger.isTraceEnabled()) {
					logger.trace("There's session for user " + ctx.getStringId());
				}
			} else if (logger.isTraceEnabled()) {
				logger.trace("There's no security context in the session");
			}
			SecurityContext.set(ctx);
		} catch (Exception e) {
			SecurityContext.set(null);
		}
	}

	private void releaseSC(ServletRequest request) {
		try {
			final HttpSession s = getSession(request);
			final SecurityContext ctx = SecurityContext.get();
			if (ctx == null) {
				s.removeAttribute(SC_VAR);
				if (logger.isTraceEnabled()) {
					logger.trace("Removing security context from session");
				}
			} else {
				s.setAttribute(SC_VAR, ctx);
				if (logger.isTraceEnabled()) {
					logger.trace("Storing in session security context for user " + ctx.getStringId());
				}
			}
		} catch (Exception e) {
			if (logger.isTraceEnabled()) {
				logger.trace("Exception thrown during SC release: " + e);
			}
		}
		finally {
			SecurityContext.set(null);
		}
	}

}
