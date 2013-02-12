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

package com.isotrol.impe3.connectors.uri;


import com.google.common.base.Preconditions;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.connectors.Loggers;


/**
 * File bundle based URI builder implementation.
 * @author Andres Rodriguez
 */
public class FileBundleURIBuilderImpl implements URIBuilderService {
	private final FileBundleURIBuilderConfig configuration;

	public FileBundleURIBuilderImpl(final FileBundleURIBuilderConfig configuration) {
		this.configuration = Preconditions.checkNotNull(configuration);
	}

	/**
	 * @see com.isotrol.impe3.connectors.uri.URIBuilderService#getURI(com.isotrol.impe3.api.component.ComponentRequestContext,
	 * java.lang.String)
	 */
	public String getURI(ComponentRequestContext context, String key) {
		if (configuration == null || configuration.bundle() == null) {
			Loggers.connectors().error("Invalid configuration for FileBundleURIBuilderImpl");
			return null;
		}
		final FileId bundle = configuration.bundle();
		try {
			return context.getURI(bundle, key).toASCIIString();
		} catch (RuntimeException e) {
			Loggers.connectors().error("Error obtaining fitem URL for bundle [{}] [{}] key [{}]",
				new Object[] {bundle.getId(), bundle.getName(), key});
			Loggers.connectors().error("Error: ", e);
			return null;
		}
	}
}
