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


import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * Mock for JAX-RS' HttpHeaders.
 * @author Andres Rodriguez
 */
public class MockHttpHeaders implements HttpHeaders {
	public MockHttpHeaders() {
		// TODO Auto-generated constructor stub
	}

	public List<Locale> getAcceptableLanguages() {
		return ImmutableList.of();
	}

	public List<MediaType> getAcceptableMediaTypes() {
		return ImmutableList.of();
	}

	public Map<String, Cookie> getCookies() {
		return ImmutableMap.of();
	}

	public Locale getLanguage() {
		return new Locale("es");
	}

	public MediaType getMediaType() {
		return MediaType.TEXT_HTML_TYPE;
	}

	public List<String> getRequestHeader(String name) {
		return ImmutableList.of();
	}

	public MultivaluedMap<String, String> getRequestHeaders() {
		return new MultivaluedMapImpl();
	}
}
