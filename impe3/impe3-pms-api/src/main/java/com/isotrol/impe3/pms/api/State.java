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
 * Enumeration of the possible states of objects with a lifecycle.
 * @author Andres Rodriguez
 */
public enum State {
	/** New: there is no published version. */
	NEW,
	/** Published: the current version is published. */
	PUBLISHED,
	/** Modified: the current version is different from the published one. */
	MODIFIED,
	/** Unpublished: there is no current nor published version. */
	UNPUBLISHED
}
