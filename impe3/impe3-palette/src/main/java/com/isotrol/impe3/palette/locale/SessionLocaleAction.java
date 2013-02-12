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

package com.isotrol.impe3.palette.locale;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import net.sf.derquinsej.i18n.Locales;

import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.palette.Loggers;
import com.isotrol.impe3.support.action.RedirectAction;


/**
 * Session Locale Action.
 * @author Andres Rodriguez Chamorro
 */
public class SessionLocaleAction extends RedirectAction {
	/** Configuration. */
	private SessionLocaleActionConfig config;

	public SessionLocaleAction() {
	}
	
	public void setConfig(SessionLocaleActionConfig config) {
		this.config = config;
	}

	@POST
	public Response setLocale(@Context HttpServletRequest request, @QueryParam("l") String localeString,
		@HeaderParam("Referer") String referer) {
		if (!StringUtils.hasText(localeString) || config == null) {
			return getResponse(referer);
		}
		final String att = config.sessionAttribute();
		if (!StringUtils.hasText(att)) {
			return getResponse(referer);
		}
		try {
			final Locale locale = Locales.fromString(localeString);
			request.getSession(true).setAttribute(att, locale);
		} catch (RuntimeException e) {
			Loggers.palette().warn(String.format("Unable to parse locale [%s]", localeString));
		}
		return getResponse(referer);
	}

	private Response getResponse(String referer) {
		if (config == null) {
			return getBack(null);
		}
		if (config.main()) {
			return getResponse(PageKey.main(), null);
		}
		final String special = config.special();
		if (StringUtils.hasText(special)) {
			return getResponse(PageKey.special(special), null);
		}
		if (config.referrer() && StringUtils.hasText(referer)) {
			try {
				final URI uri = new URI(referer);
				if (uri.isAbsolute()) {
					return Response.seeOther(uri).build();
				}
			} catch (URISyntaxException e) {}
		}
		return getBack(null);
	}
}
