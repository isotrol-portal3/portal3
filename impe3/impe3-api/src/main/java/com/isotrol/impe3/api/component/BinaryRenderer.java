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

package com.isotrol.impe3.api.component;


import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;


/**
 * Interface defining a binary renderer.
 * @author Andres Rodriguez
 * 
 */
public interface BinaryRenderer extends ComponentRenderer {
	/**
	 * First method called by the rendering engine. Use to prepare the return arguments for the other methods or to
	 * precompute the response. If the method throws any exception further processing will be interrupted and server
	 * error will be sent to the client.
	 */
	void prepare();

	/**
	 * Returns the media type of the response. It MUST be non-null.
	 * @return The media type of the response.
	 */
	MediaType getMediaType();

	/**
	 * Returns the content length, if known.
	 * @return The content length or null if not known.
	 */
	Integer getLength();

	/**
	 * Returns the suggested file name, if the response is an attachment.
	 * @return The file name for an attachment, null for an inline disposition.
	 */
	String getFileName();

	/**
	 * Writes the response.
	 * @param os Output stream.
	 * @throws IOException If an error occurs.
	 */
	void write(OutputStream os) throws IOException;
}
