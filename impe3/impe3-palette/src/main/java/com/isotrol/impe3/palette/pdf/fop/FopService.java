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

package com.isotrol.impe3.palette.pdf.fop;


import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;


/**
 * Interface for a FOP Service.
 * @author Andres Rodriguez
 */
public interface FopService {
	/**
	 * Creates a new user agent instance.
	 * @return A new user agent instance.
	 */
	FOUserAgent newUserAgent();

	/**
	 * Returns the identity transformer.
	 * @return The requested transformer.
	 */
	Transformer newTransformer() throws TransformerException;

	/**
	 * Returns a transformer based on the provided source.
	 * @param source Source to use.
	 * @return The requested transformer.
	 */
	Transformer newTransformer(Source source) throws TransformerException;

	/**
	 * Generates a PDF
	 * @param source Source data.
	 * @param transformer Transformer to use (optional).
	 * @param userAgent User agent to apply (optional).
	 * @param os Destination output stream.
	 * @throws TransformerException if an error occurs.
	 * @throws FOPException if an error occurs.
	 * @throws IOException if an error occurs.
	 */
	void generatePDF(Source source, Transformer transformer, FOUserAgent userAgent, OutputStream os) throws TransformerException, FOPException, IOException;
}
