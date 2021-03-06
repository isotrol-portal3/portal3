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

package com.isotrol.impe3.connectors.nr;


import com.isotrol.impe3.nr.api.CachingNodeRepository;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * Node Repository microcache implementation.
 * @author Andres Rodriguez
 */
public final class NRMicrocache extends CachingNodeRepository {
	/** Expiration calculation. */
	private static int expiration(NRMicrocacheConfig config) {
		return Math.min(Math.max(800, config.microcache()), 80000);
	}

	/**
	 * Constructor.
	 * @param repository Repository to cache.
	 * @param config Module configuration.
	 */
	public NRMicrocache(NodeRepository repository, NRMicrocacheConfig config) {
		super(repository, expiration(config));
	}

}
