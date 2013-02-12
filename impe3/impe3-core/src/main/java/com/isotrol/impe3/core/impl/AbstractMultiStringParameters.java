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


import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.derquinsej.CaseIgnoringString;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Iterators;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.MultiStringParameters;


/**
 * Skeletal implementation for map-based parameters.
 * @author Andres Rodriguez
 * @author Emilio Escobar
 */
abstract class AbstractMultiStringParameters extends AbstractParameters<List<String>> implements MultiStringParameters {
	AbstractMultiStringParameters() {
	}

	abstract ListMultimap<CaseIgnoringString, String> map();

	final ListMultimap<CaseIgnoringString, String> submap(Set<String> included) {
		if (included == null || included.isEmpty()) {
			return ImmutableListMultimap.of();
		}
		final ImmutableListMultimap.Builder<CaseIgnoringString, String> builder = ImmutableListMultimap.builder();
		for (Map.Entry<CaseIgnoringString, Collection<String>> entry : map().asMap().entrySet()) {
			if (included.contains(entry.getKey())) {
				builder.putAll(entry.getKey(), entry.getValue());
			}
		}
		return builder.build();
	}

	public Set<String> getNames() {
		final Set<CaseIgnoringString> keys = map().keySet();
		return new AbstractSet<String>() {

			public boolean add(String e) {
				throw new UnsupportedOperationException();
			}

			public void clear() {
				throw new UnsupportedOperationException();
			}

			public boolean contains(Object o) {
				Preconditions.checkNotNull(o);
				return keys.contains(CaseIgnoringString.valueOf((String) o));
			}

			public boolean isEmpty() {
				return keys.isEmpty();
			}

			public Iterator<String> iterator() {
				return Iterators.transform(keys.iterator(), Functions.toStringFunction());
			}

			public boolean remove(Object o) {
				throw new UnsupportedOperationException();
			}

			public boolean removeAll(Collection<?> c) {
				throw new UnsupportedOperationException();
			}

			public boolean retainAll(Collection<?> c) {
				throw new UnsupportedOperationException();
			}

			public int size() {
				return keys.size();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.api.Parameters#get(java.lang.String)
	 */
	public List<String> get(String name) {
		return map().get(CaseIgnoringString.valueOf(name));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.api.MultiStringParameters#getFirst(java.lang.String)
	 */
	public String getFirst(String name) {
		final List<String> values = get(name);
		if (values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	@Override
	public boolean contains(String name) {
		return map().containsKey(CaseIgnoringString.valueOf(name));
	}

	/**
	 * Skeletal Immutable implementation.
	 * @author Andres Rodriguez
	 */
	abstract static class AbstractImmutable extends AbstractMultiStringParameters {
		private final ImmutableListMultimap<CaseIgnoringString, String> map;

		AbstractImmutable(Multimap<CaseIgnoringString, String> map) {
			this.map = ImmutableListMultimap.copyOf(map);
		}

		@Override
		ListMultimap<CaseIgnoringString, String> map() {
			return map;
		}
	}

}
