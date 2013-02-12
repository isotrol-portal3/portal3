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

package com.isotrol.impe3.pms.core;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.google.protobuf.MessageLite;


/**
 * Value that represents an export job result.
 * @author Andres Rodriguez.
 */
public final class ExportResult {
	/** File name. */
	private final String name;
	/** Message. */
	private final MessageLite message;

	public static ExportResult create(String name, MessageLite message) {
		return new ExportResult(name, message);
	}

	private ExportResult(String name, MessageLite message) {
		this.name = checkNotNull(name);
		this.message = checkNotNull(message);
	}

	public Response getResponse() {
		final String disp = "attachment; filename=" + name + ".impe";
		final StreamingOutput output = new StreamingOutput() {
			public void write(OutputStream output) throws IOException, WebApplicationException {
				message.writeTo(output);
			}
		};
		return Response.ok(output).header("Content-Disposition", disp).type("application/x-protobuf").build();
	}

}
