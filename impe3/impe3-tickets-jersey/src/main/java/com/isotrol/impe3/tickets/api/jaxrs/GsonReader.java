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

package com.isotrol.impe3.tickets.api.jaxrs;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;


/**
 * Gson-based JSON JAX-RS Reader.
 * @author Andres Rodriguez.
 */
@Component
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class GsonReader implements MessageBodyReader<Object> {
	public GsonReader() {
	}

	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
		MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
		WebApplicationException {

		final Writer w = new OutputStreamWriter(entityStream, Charsets.UTF_8);
		final JsonWriter jsw = new JsonWriter(w);
		new Gson().toJson(t, type, jsw);
	}

	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return true;
	}

	public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
		MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException,
		WebApplicationException {
		return new Gson().fromJson(new InputStreamReader(entityStream, Charsets.UTF_8), type);
	}

}
