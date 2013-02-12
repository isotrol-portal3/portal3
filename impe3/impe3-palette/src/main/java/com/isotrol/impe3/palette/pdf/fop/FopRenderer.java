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

import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.apache.fop.apps.FOPException;

import com.isotrol.impe3.api.PortalException;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.pdf.PDFRenderer;
import com.isotrol.impe3.palette.binary.AbstractBinaryRenderer;


/**
 * Base class for excel renderers based on Apache POI. This class IS NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class FopRenderer extends AbstractBinaryRenderer implements PDFRenderer {
	private static final MediaType PDF_MIME = MediaType.valueOf("application/pdf");

	/** FOP Service. */
	private final FopService fopService;

	protected FopRenderer(RenderContext context, FopService fopService, FopModuleConfig moduleConfig,
		FopComponentConfig componentConfig) {
		super(context, moduleConfig, componentConfig);
		this.fopService = checkNotNull(fopService);
	}

	public MediaType getMediaType() {
		return PDF_MIME;
	}

	/**
	 * Performs actual writing.
	 * @param os Output stream.
	 * @throws IOException If an error occurs.
	 */
	protected final void doWrite(OutputStream os) throws IOException {
		try {
			final Source source = getSource();
			if (source != null) {
				fopService.generatePDF(getSource(), getTransformer(), null, os);
			}
		} catch (FOPException e) {
			throw new PortalException(e, "FOP Exception");
		} catch (TransformerException e) {
			throw new PortalException(e, "Transformer Exception");
		}
	}

	/**
	 * Returns the source data to use.
	 * @return The source data.
	 */
	protected abstract Source getSource();

	/**
	 * Returns the transformer to use.
	 * @return The transformer to use.
	 */
	protected abstract Transformer getTransformer() throws TransformerException;
}
