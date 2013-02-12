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

package com.isotrol.impe3.core.impl;


import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.api.MultiStringParameters;


/**
 * Skeletal implementation for empty parameters implementations.
 * @author Andres Rodriguez
 */
abstract class EmptyMultiStringParameters implements MultiStringParameters {
	EmptyMultiStringParameters() {
	}

	public List<String> get(String name) {
		return ImmutableList.of();
	}

	public String getFirst(String name) {
		return null;
	}

	public boolean contains(String name) {
		return false;
	}

	public Set<String> getNames() {
		return ImmutableSet.of();
	}
}
