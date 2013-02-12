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

package com.isotrol.impe3.api.support;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.component.RenderContext;


/**
 * Render context support.
 * @author Andres Rodriguez.
 */
public final class RenderContexts {
	/** Not instantiable. */
	private RenderContexts() {
		throw new AssertionError();
	}

	public static RenderContext changeBase(final RenderContext context, final String base, final FileId bundle,
		final boolean modeDependent) {
		checkNotNull(context);
		checkNotNull(base);
		checkNotNull(bundle);
		return new ForwardingRenderContext() {
			@Override
			protected RenderContext delegate() {
				return context;
			}

			@Override
			public URI getURIByBase(String requestedBase, String requestedPath) {
				if (base.equals(requestedBase)) {
					return getURI(bundle, requestedPath);
				}
				return super.getURIByBase(requestedBase, requestedPath);
			}

			@Override
			public URI getURIByMDBase(String requestedBase, String requestedPath) {
				if (modeDependent && base.equals(requestedBase)) {
					return getURI(bundle, requestedPath);
				}
				return super.getURIByMDBase(requestedBase, requestedPath);
			}

		};
	}
}
