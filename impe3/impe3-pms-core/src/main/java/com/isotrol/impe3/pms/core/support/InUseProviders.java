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

package com.isotrol.impe3.pms.core.support;


import com.isotrol.impe3.pms.api.EntityInUseException;
import com.isotrol.impe3.pms.api.category.CategoryInUseException;
import com.isotrol.impe3.pms.api.component.ComponentInUseException;
import com.isotrol.impe3.pms.api.connector.ConnectorInUseException;
import com.isotrol.impe3.pms.api.device.DeviceInUseException;
import com.isotrol.impe3.pms.api.page.PageInUseException;
import com.isotrol.impe3.pms.api.portal.PortalInUseException;
import com.isotrol.impe3.pms.api.type.ContentTypeInUseException;


/**
 * EntityInUseException Providers.
 * @author Andres Rodriguez.
 */
public final class InUseProviders {
	private InUseProviders() {
		throw new AssertionError();
	}

	/** Default. */
	public static final InUseProvider DEFAULT = new DefaultInUseProvider();
	/** Content type. */
	public static final InUseProvider CONTENT_TYPE = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new ContentTypeInUseException(id);
		}
	};
	/** Category. */
	public static final InUseProvider CATEGORY = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new CategoryInUseException(id);
		}
	};
	/** Connector. */
	public static final InUseProvider CONNECTOR = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new ConnectorInUseException(id);
		}
	};
	/** File. */
	public static final InUseProvider FILE = DEFAULT;
	/** Portal. */
	public static final InUseProvider PORTAL = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new PortalInUseException(id);
		}
	};
	/** Component. */
	public static final InUseProvider COMPONENT = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new ComponentInUseException(id);
		}
	};
	/** Device. */
	public static final InUseProvider DEVICE = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new DeviceInUseException(id);
		}
	};
	/** Page. */
	public static final InUseProvider PAGE = new DefaultInUseProvider() {
		@Override
		public EntityInUseException getInUseException(String id) {
			return new PageInUseException(id);
		}
	};
	/** User. */
	public static final InUseProvider USER = DEFAULT;
	/** Routing domain. */
	public static final InUseProvider ROUTING_DOMAIN = DEFAULT;
}
