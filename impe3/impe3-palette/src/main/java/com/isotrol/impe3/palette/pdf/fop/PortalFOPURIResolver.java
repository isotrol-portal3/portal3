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

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import com.isotrol.impe3.api.FileBundleData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;


/**
 * URI Resolver for FOP Service.
 * @author Andres Rodriguez
 */
final class PortalFOPURIResolver implements URIResolver {
	private static final String SCHEME = "portalStream:";

	private final FileLoader fileLoader;
	private final FileId fileId;
	private FileBundleData bundle = null;
	private final URIResolver chain;

	/**
	 * Constructor.
	 * @param fileLoader File loader.
	 */
	public PortalFOPURIResolver(FileLoader fileLoader, FileId fileId, URIResolver chain) {
		this.fileLoader = checkNotNull(fileLoader);
		this.fileId = checkNotNull(fileId);
		this.chain = chain;
	}

	public Source resolve(String href, String base) throws TransformerException {
		if (href == null || href.length() <= SCHEME.length() || !href.startsWith(SCHEME)) {
			if (chain != null) {
				return chain.resolve(href, base);
			}
			return null;
		}
		if (bundle == null) {
			try {
				bundle = fileLoader.loadBundle(fileId);
			} catch (RuntimeException e) {
				return null;
			}
		}
		final String name = href.substring(SCHEME.length());
		if (bundle.contains(name)) {
			return new StreamSource(bundle.apply(name).getData());
		}
		return null;
	}
}
