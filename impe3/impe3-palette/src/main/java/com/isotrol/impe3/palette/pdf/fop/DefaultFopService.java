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


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOURIResolver;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;


/**
 * Default implementation for a FOP Service.
 * @author Andres Rodriguez
 */
public final class DefaultFopService implements FopService {
	private final FopFactory fopFactory;
	private final TransformerFactory transformerFactory;

	/**
	 * Constructor.
	 * @param config Module configuration.
	 * @param fopFactory FOP Factory.
	 * @param transformerFactory Transformer factory.
	 * @param fileLoader File loader.
	 */
	public DefaultFopService(FopModuleConfig config, FopFactory fopFactory, TransformerFactory transformerFactory,
		FileLoader fileLoader) {
		this.fopFactory = checkNotNull(fopFactory);
		this.transformerFactory = checkNotNull(transformerFactory);
		final FileId bundle = config.fopResources();
		if (bundle != null) {
			final FOURIResolver fur = fopFactory.getFOURIResolver();
			final URIResolver resolver = new PortalFOPURIResolver(fileLoader, bundle, fur.getCustomURIResolver());
			fur.setCustomURIResolver(resolver);
		}
	}

	/**
	 * @see com.isotrol.impe3.palette.pdf.fop.FopService#newUserAgent()
	 */
	public FOUserAgent newUserAgent() {
		return fopFactory.newFOUserAgent();
	}

	/**
	 * @see com.isotrol.impe3.palette.pdf.fop.FopService#newTransformer()
	 */
	public Transformer newTransformer() throws TransformerException {
		return transformerFactory.newTransformer();
	}

	/**
	 * @see com.isotrol.impe3.palette.pdf.fop.FopService#newTransformer(javax.xml.transform.Source)
	 */
	public Transformer newTransformer(Source source) throws TransformerException {
		return transformerFactory.newTransformer(source);
	}

	/**
	 * @see com.isotrol.impe3.palette.pdf.fop.FopService#generatePDF(javax.xml.transform.Source,
	 * javax.xml.transform.Transformer, org.apache.fop.apps.FOUserAgent, java.io.OutputStream)
	 */
	public void generatePDF(Source source, Transformer transformer, FOUserAgent userAgent, OutputStream os)
		throws TransformerException, FOPException, IOException {
		checkNotNull(source, "Source data");
		checkNotNull(os, "Output stream");
		// Wrap the stream in an BufferedOutputStream.
		final OutputStream out = new BufferedOutputStream(os, 8192);
		try {
			// Construct fop with PDF output format
			final Fop fop;
			if (userAgent != null) {
				fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, out);
			} else {
				fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
			}
			// Setup transformer
			if (transformer == null) {
				transformer = newTransformer();
			}
			// Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());
			// Start XSLT transformation and FOP processing
			transformer.transform(source, res);
		}
		finally {
			// Clean-up
			out.close();
		}
	}
}
