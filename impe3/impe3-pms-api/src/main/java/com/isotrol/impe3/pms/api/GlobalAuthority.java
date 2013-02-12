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

package com.isotrol.impe3.pms.api;


/**
 * Global authorities enumeration.
 * @author Andres Rodriguez
 */
public enum GlobalAuthority {
	/** Content type management: GET. */
	TYPE_GET,
	/** Content type management: SET. */
	TYPE_SET,
	/** Category management: GET. */
	CATEGORY_GET,
	/** Category management: SET. */
	CATEGORY_SET,
	/** Source mapping management: GET. */
	SMAP_GET,
	/** Source mapping management: SET. */
	SMAP_SET,
	/** Connector management: GET. */
	CONNECTOR_GET,
	/** Connector management: SET. */
	CONNECTOR_SET,
	/** Portal Tree Query. */
	PORTAL_TREE,
	/** Portal management: GET. */
	PORTAL_GET,
	/** Portal management: SET. */
	PORTAL_SET,
	/** Portal management: Bases. */
	PORTAL_BASE,
	/** Portal management: Properties. */
	PORTAL_PROPERTY,
	/** Portal management: Components. */
	PORTAL_COMPONENT,
	/** Portal management: Pages. */
	PORTAL_PAGE,
	/** User management: GET. */
	USER_GET,
	/** User management: SET. */
	USER_SET,
	/** User management: Password management. */
	USER_PWD,
	/** Environment configuration: GET. */
	ENV_CONFIG_GET,
	/** Environment configuration: SET. */
	ENV_CONFIG_SET,
	/** Edition: GET. */
	EDITION_GET,
	/** Edition: publish. */
	EDITION_PUBLISH,
	/** Edition: republish old version. */
	EDITION_REPUBLISH,
	/** Routing domain management: GET. */
	RD_GET,
	/** Routing domain management: SET. */
	RD_SET,
	/** Device management: GET. */
	DEVICE_GET,
	/** Device management: SET. */
	DEVICE_SET
}
