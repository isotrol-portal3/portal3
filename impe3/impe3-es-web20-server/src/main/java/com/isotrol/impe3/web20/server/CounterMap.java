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
package com.isotrol.impe3.web20.server;


import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Ordering;


/**
 * Map containing a counters hits.
 * @author Andres Rodriguez
 */
public final class CounterMap {
	/** Count. */
	private static final Function<Entry, Long> COUNT = new Function<Entry, Long>() {
		public Long apply(Entry from) {
			return from.getCount();
		}
	};
	/** Ordering. */
	private static final Ordering<Entry> ORDER = Ordering.natural().onResultOf(COUNT).reverse();

	/** Map. */
	private final ImmutableListMultimap<Key, Entry> map;
	/** Resource map. */
	private final ImmutableMap<RKey, Long> resourceMap;

	public static Builder builder() {
		return new Builder();
	}

	CounterMap(Builder builder) {
		for (Key k : builder.builder.keySet()) {
			Collections.sort(builder.builder.get(k), ORDER);
		}
		this.map = ImmutableListMultimap.copyOf(builder.builder);
		this.resourceMap = builder.resources.build();
	}

	/**
	 * Returns the resources with most hits.
	 * @param typeId Counter type id.
	 * @param groupId Counter group id.
	 * @param max Maximum number of results.
	 * @return The requested resources.
	 */
	public List<Entry> get(long typeId, Long groupId, int max) {
		return get(new Key(typeId, groupId), max);
	}

	/**
	 * Returns the resources with most hits.
	 * @param key Counter map key.
	 * @param max Maximum number of results.
	 * @return The requested resources.
	 */
	public List<Entry> get(Key key, int max) {
		if (max <= 0) {
			return ImmutableList.of();
		}
		List<Entry> entries = map.get(key);
		if (entries.size() > max) {
			entries = entries.subList(0, max);
		}
		return entries;
	}

	/**
	 * Returns the number of hits of a resource.
	 * @param typeId Counter type id.
	 * @param groupId Counter group id.
	 * @param resourceId Resource id.
	 * @return The requested hit count.
	 */
	public long getResource(long typeId, Long groupId, long resourceId) {
		return getResource(new Key(typeId, groupId), resourceId);
	}

	/**
	 * Returns the number of hits of a resource.
	 * @param key Counter map key.
	 * @param resourceId Resource id.
	 * @return The requested hit count.
	 */
	public long getResource(Key key, long resourceId) {
		final Long hits = resourceMap.get(new RKey(resourceId, key));
		return hits == null ? 0L : hits.longValue();
	}

	/**
	 * Returns the total number of hits in the map.
	 * @return
	 */
	public long size() {
		long size = 0;
		for (Entry e : map.values()) {
			size += e.count;
		}
		return size;
	}

	public static final class Key {
		private final long typeId;
		private final Long groupId;

		public Key(long typeId, Long groupId) {
			this.typeId = typeId;
			this.groupId = groupId;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(typeId, groupId);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return typeId == k.typeId && Objects.equal(groupId, k.groupId);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("[type:%d/group:%d]", typeId, groupId);
		}

	}

	public static final class Entry {
		private final long resourceId;
		private long count;

		Entry(long resourceId, long count) {
			this.resourceId = resourceId;
			this.count = count;
		}

		public long getResourceId() {
			return resourceId;
		}

		public long getCount() {
			return count;
		}

		@Override
		public String toString() {
			return String.format("[resourceId:%d/count:%d]", resourceId, count);
		}
	}

	private static final class RKey {
		private final long resourceId;
		private final Key key;

		RKey(long resourceId, Key key) {
			this.resourceId = resourceId;
			this.key = key;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(resourceId, key);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof RKey) {
				final RKey k = (RKey) obj;
				return resourceId == k.resourceId && Objects.equal(key, k.key);
			}
			return false;
		}

	}

	public static final class Builder implements Supplier<CounterMap> {
		private final ListMultimap<Key, Entry> builder = ArrayListMultimap.create();
		private final ImmutableMap.Builder<RKey, Long> resources = ImmutableMap.builder();

		Builder() {
		}

		/**
		 * Adds a counter.
		 * @param counter Counter parameters.
		 */
		public void add(Object[] counter) {
			final long typeId = ((Long) counter[0]).longValue();
			final Long groupId = (Long) counter[1];
			final Key k = new Key(typeId, groupId);
			final long resourceId = ((Long) counter[2]).longValue();
			final long count = ((Long) counter[3]).longValue();
			final Entry e = new Entry(resourceId, count);
			builder.put(k, e);
			resources.put(new RKey(resourceId, k), count);
		}

		public CounterMap get() {
			return new CounterMap(this);
		}

	}

}
