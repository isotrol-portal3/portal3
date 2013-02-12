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


import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.category.CategoryNotFoundException;
import com.isotrol.impe3.pms.api.component.ComponentNotFoundException;
import com.isotrol.impe3.pms.api.connector.ConnectorNotFoundException;
import com.isotrol.impe3.pms.api.device.DeviceNotFoundException;
import com.isotrol.impe3.pms.api.edition.EditionNotFoundException;
import com.isotrol.impe3.pms.api.page.PageNotFoundException;
import com.isotrol.impe3.pms.api.portal.PortalNotFoundException;
import com.isotrol.impe3.pms.api.rd.RoutingDomainNotFoundException;
import com.isotrol.impe3.pms.api.smap.SourceMappingNotFoundException;
import com.isotrol.impe3.pms.api.type.ContentTypeNotFoundException;
import com.isotrol.impe3.pms.api.user.UserNotFoundException;


/**
 * EntityNotFoundException Providers.
 * @author Andres Rodriguez.
 */
public final class NotFoundProviders {
	private NotFoundProviders() {
		throw new AssertionError();
	}

	/** Default. */
	public static final NotFoundProvider DEFAULT = new DefaultNotFoundProvider();
	/** Content type. */
	public static final NotFoundProvider CONTENT_TYPE = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new ContentTypeNotFoundException(id);
		}
	};
	/** Category. */
	public static final NotFoundProvider CATEGORY = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new CategoryNotFoundException(id);
		}
	};
	/** Connector. */
	public static final NotFoundProvider CONNECTOR = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new ConnectorNotFoundException(id);
		}
	};
	/** File. */
	public static final NotFoundProvider FILE = DEFAULT;
	/** Portal. */
	public static final NotFoundProvider PORTAL = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new PortalNotFoundException(id);
		}
	};
	/** Component. */
	public static final NotFoundProvider COMPONENT = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new ComponentNotFoundException(id);
		}
	};
	/** Device. */
	public static final NotFoundProvider DEVICE = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new DeviceNotFoundException(id);
		}
	};
	/** Page. */
	public static final NotFoundProvider PAGE = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new PageNotFoundException(id);
		}
	};
	/** User. */
	public static final NotFoundProvider USER = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new UserNotFoundException(id);
		}
	};
	/** Source mapping. */
	public static final NotFoundProvider MAPPING = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new SourceMappingNotFoundException(id);
		}
	};
	/** Routing domain. */
	public static final NotFoundProvider ROUTING_DOMAIN = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new RoutingDomainNotFoundException(id);
		}
	};
	/** Edition. */
	public static final NotFoundProvider EDITION = new DefaultNotFoundProvider() {
		@Override
		public EntityNotFoundException getNotFoundException(String id) {
			return new EditionNotFoundException(id);
		}
	};
}
