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

package com.isotrol.impe3.nr.api;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.sf.derquinsej.i18n.Locales;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;


/**
 * Regular node filter.
 * @author Andres Rodriguez
 */
final class RegularNodeFilter extends NodeFilter {
	/** Serial UID. */
	private static final long serialVersionUID = 437919965102840677L;
	/** Only due nodes. */
	private final boolean due;
	/** Sets. */
	private final ImmutableMap<String, FilterType> sets;
	/** Categories. */
	private final ImmutableMap<Optional<UUID>, FilterType> categories;
	/** Content types. */
	private final ImmutableMap<UUID, FilterType> contentTypes;
	/** Locales. */
	private final ImmutableMap<Locale, FilterType> locales;
	/** Tags. */
	private final ImmutableMap<String, FilterType> tags;
	/** Node keys. */
	private final ImmutableMap<NodeKey, FilterType> keys;

	private static <K, V> ImmutableMap<K, V> map(Map<K, V> map) {
		if (map == null) {
			return null;
		}
		checkArgument(!map.isEmpty(), "Empty map in a regular node filter");
		return ImmutableMap.copyOf(map);
	}

	RegularNodeFilter(boolean due, Map<String, FilterType> sets, Map<Optional<UUID>, FilterType> categories,
		Map<UUID, FilterType> contentTypes, Map<Locale, FilterType> locales, Map<String, FilterType> tags,
		Map<NodeKey, FilterType> keys) {
		this.due = due;
		this.sets = map(sets);
		this.categories = map(categories);
		this.contentTypes = map(contentTypes);
		this.locales = map(locales);
		this.tags = map(tags);
		this.keys = map(keys);
		checkArgument(due || sets != null || categories != null || contentTypes != null || locales != null
			|| tags != null || keys != null, "Null criteria in a regular node filter");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isEmpty()
	 */
	public boolean isEmpty() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isNull()
	 */
	public boolean isNull() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.nr.api.NodeFilter#isDue()
	 */
	public boolean isDue() {
		return due;
	}

	@Override
	public Map<String, FilterType> sets() {
		return sets;
	}

	@Override
	public Map<Optional<UUID>, FilterType> categories() {
		return categories;
	}

	@Override
	public Map<UUID, FilterType> contentTypes() {
		return contentTypes;
	}

	@Override
	public Map<Locale, FilterType> locales() {
		return locales;
	}

	@Override
	public Map<String, FilterType> tags() {
		return tags;
	}

	@Override
	public Map<NodeKey, FilterType> keys() {
		return keys;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(due, sets, categories, contentTypes, locales, tags, keys);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof RegularNodeFilter) {
			RegularNodeFilter f = (RegularNodeFilter) obj;
			return due == f.due && equal(sets, f.sets) && equal(categories, f.categories)
				&& equal(contentTypes, f.contentTypes) && equal(locales, f.locales) && equal(tags, f.tags)
				&& equal(keys, f.keys);
		}
		return false;
	}

	// ////////////////////////////////////////////////////////////////////////////////

	// =================================================================
	// Serialization proxy

	private static <K> Map<K, FilterType> c2map(Map<K, FilterType> map) {
		if (map == null) {
			return null;
		}
		return Maps.newHashMap(map);
	}

	private static <T, K> Map<K, FilterType> c2map(Map<T, FilterType> map, Function<? super T, K> f) {
		if (map == null) {
			return null;
		}
		Map<K, FilterType> result = Maps.newHashMap();
		for (Entry<T, FilterType> e : map.entrySet()) {
			result.put(f.apply(e.getKey()), e.getValue());
		}
		return result;
	}

	private static final Function<Optional<UUID>, String> OC2STR = new Function<Optional<UUID>, String>() {
		public String apply(Optional<UUID> input) {
			if (input.isPresent()) {
				return input.get().toString();
			}
			return null;
		}
	};

	private static final Function<String, Optional<UUID>> STR2OC = new Function<String, Optional<UUID>>() {
		public Optional<UUID> apply(String input) {
			if (input == null) {
				return Optional.absent();
			}
			return Optional.of(UUID.fromString(input));
		}
	};

	private static final Function<String, UUID> STR2UUID = new Function<String, UUID>() {
		public UUID apply(String input) {
			return UUID.fromString(input);
		}
	};

	private static class SerializationProxy implements Serializable {
		/** Serial UID. */
		private static final long serialVersionUID = -7318935200526361074L;
		private final boolean due;
		private final Map<String, FilterType> sets;
		private final Map<String, FilterType> categories;
		private final Map<String, FilterType> contentTypes;
		private final Map<String, FilterType> locales;
		private final Map<String, FilterType> tags;
		private final Map<NodeKey, FilterType> keys;

		SerializationProxy(RegularNodeFilter s) {
			this.due = s.due;
			this.sets = c2map(s.sets);
			this.categories = c2map(s.categories, OC2STR);
			this.contentTypes = c2map(s.contentTypes, Functions.toStringFunction());
			this.locales = c2map(s.locales, Functions.toStringFunction());
			this.tags = c2map(s.tags);
			this.keys = c2map(s.keys);
		}

		private Object readResolve() {
			Builder b = builder();
			b.due(due);
			if (sets != null) {
				b.sets().apply(sets);
			}
			if (categories != null) {
				b.categories().apply(c2map(categories, STR2OC));
			}
			if (contentTypes != null) {
				b.contentTypes().apply(c2map(contentTypes, STR2UUID));
			}
			if (locales != null) {
				b.locales().apply(c2map(locales, Locales.FROM_STRING));
			}
			if (tags != null) {
				b.tags().apply(tags);
			}
			if (keys != null) {
				b.keys().apply(keys);
			}
			return b.build();
		}
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

}
