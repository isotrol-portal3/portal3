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

package com.isotrol.impe3.core;


import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.api.PortalRequestContext;


/**
 * Portal cache model.
 * @author Andres Rodriguez
 */
public abstract class PortalCacheModel {
	/** Disabled cache. */
	private static final Off OFF = new Off();

	public static PortalCacheModel off() {
		return OFF;
	}

	public static PortalCacheModel on(boolean publicCache, Integer modification, Integer expiration, ETagMode eTagMode) {
		return new On(publicCache, modification, expiration, eTagMode);
	}

	/** Constructor. */
	private PortalCacheModel() {
	}

	public abstract PortalCacheControl getControl(PortalRequestContext context);

	private static final class Off extends PortalCacheModel {
		private Off() {
		}

		@Override
		public PortalCacheControl getControl(PortalRequestContext context) {
			return PortalCacheControl.off();
		}
	}

	private static class On extends PortalCacheModel {
		/** Whether the cache is public. */
		private boolean publicCache;
		/** Seconds to modification date. */
		private Integer modification;
		/** Seconds to expiration date. */
		private Integer expiration;
		/** ETag mode. */
		private ETagMode eTagMode;

		/** Default constructor. */
		private On(boolean publicCache, Integer modification, Integer expiration, ETagMode eTagMode) {
			this.publicCache = publicCache;
			this.modification = modification;
			this.expiration = expiration;
			this.eTagMode = eTagMode != null ? eTagMode : ETagMode.OFF;
		}

		@Override
		public PortalCacheControl getControl(PortalRequestContext context) {
			return PortalCacheControl.on(context, publicCache, modification, expiration, eTagMode);
		}
	}

}
