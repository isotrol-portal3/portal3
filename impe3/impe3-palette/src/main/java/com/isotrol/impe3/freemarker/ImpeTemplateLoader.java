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

package com.isotrol.impe3.freemarker;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.isotrol.impe3.api.FileBundleData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;

import freemarker.cache.TemplateLoader;


/**
 * Template loader based on a zip file stores as a file.
 * @author Andres Rodriguez
 */
public class ImpeTemplateLoader implements TemplateLoader {
	private final FileLoader loader;
	private final FileId file;
	private final long time = System.currentTimeMillis();
	private volatile FileBundleData bundle = null;

	public ImpeTemplateLoader(final FileLoader loader, final FileId file) {
		checkNotNull(loader);
		checkNotNull(file);
		this.loader = loader;
		this.file = file;
	}

	public ImpeTemplateLoader(final FileLoader loader, final FileBundleLoaderConfiguration config) {
		this(loader, config.templateBundle());
	}

	private void load() throws IOException {
		if (bundle != null) {
			return;
		}
		synchronized (this) {
			try {
				this.bundle = loader.loadBundle(file);
			} catch (RuntimeException e) {
				throw new IOException();
			}
		}
	}

	/**
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	public void closeTemplateSource(Object templateSource) throws IOException {
		// Do nothing.
	}

	/**
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	public Object findTemplateSource(String templateSource) throws IOException {
		load();
		if (bundle.contains(templateSource)) {
			return templateSource;
		}
		return null;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	public long getLastModified(Object templateSource) {
		return time; // files are immutable.
	}

	/**
	 * (non-Javadoc)
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object, java.lang.String)
	 */
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		load();
		final InputStream is;
		final String name = templateSource.toString();
		if (bundle.contains(name)) {
			is = bundle.apply(name).getData();
		} else {
			is = new ByteArrayInputStream(new byte[0]);
		}
		return new InputStreamReader(is, encoding);
	}

}
