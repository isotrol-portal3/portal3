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
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;


/**
 * Map containing ratings.
 * @author Andres Rodriguez
 */
public final class RatingMap {
	/** Count. */
	private static final Function<Entry, Double> MEAN = new Function<Entry, Double>() {
		public Double apply(Entry from) {
			return from.getMean();
		}
	};
	/** Ordering. */
	private static final Ordering<Entry> ORDER = Ordering.natural().onResultOf(MEAN).reverse();

	/** Global group. */
	private final ImmutableList<Entry> global;
	/** Map. */
	private final ImmutableListMultimap<Long, Entry> map;
	/** Resource map. */
	private final ImmutableMap<Key, Entry> resourceMap;

	public static Builder builder() {
		return new Builder();
	}

	RatingMap(Builder builder) {
		Collections.sort(builder.global, ORDER);
		for (Long k : builder.group.keySet()) {
			Collections.sort(builder.group.get(k), ORDER);
		}
		this.global = ImmutableList.copyOf(builder.global);
		this.map = ImmutableListMultimap.copyOf(builder.group);
		this.resourceMap = builder.resources.build();
	}

	/**
	 * Returns the best rated resources.
	 * @param groupId Group id.
	 * @param max Maximum number of results.
	 * @return The requested resources.
	 */
	public List<Entry> get(Long groupId, int max) {
		if (max <= 0) {
			return ImmutableList.of();
		}
		List<Entry> entries = groupId == null ? global : map.get(groupId);
		if (entries.size() > max) {
			entries = entries.subList(0, max);
		}
		return entries;
	}

	/**
	 * Returns the ratings of a resource.
	 * @param groupId Counter group id.
	 * @param resourceId Resource id.
	 * @return The requested hit count.
	 */
	public Entry getResource(Long groupId, long resourceId) {
		final Key key = new Key(resourceId, groupId);
		final Entry entry = resourceMap.get(key);
		return entry == null ? new Entry(resourceId) : entry;
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
		for (Entry e : global) {
			size += e.count;
		}
		return size;
	}

	private static final class Key {
		private final long resourceId;
		private final Long groupId;

		public Key(long resourceId, Long groupId) {
			this.resourceId = resourceId;
			this.groupId = groupId;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(resourceId, groupId);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return resourceId == k.resourceId && Objects.equal(groupId, k.groupId);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("[resource:%d/group:%d]", resourceId, groupId);
		}
	}

	public static final class Entry {
		private final long resourceId;
		private final long count;
		private final int min;
		private final int max;
		private final double mean;

		Entry(long resourceId, long count, int min, int max, double mean) {
			this.resourceId = resourceId;
			this.count = count;
			this.min = min;
			this.max = max;
			this.mean = mean;
		}

		Entry(long resourceId) {
			this(resourceId, 0, 0, 0, 0.0);
		}

		public long getResourceId() {
			return resourceId;
		}

		public long getCount() {
			return count;
		}

		public int getMin() {
			return min;
		}

		public int getMax() {
			return max;
		}

		public double getMean() {
			return mean;
		}

		@Override
		public String toString() {
			return String
				.format("RatingEntry[res:%d,hits:%d,min:%d,max:%d,mean:%f]", resourceId, count, min, max, mean);
		}
	}

	public static final class Builder implements Supplier<RatingMap> {
		private final List<Entry> global = Lists.newArrayList();
		private final ListMultimap<Long, Entry> group = ArrayListMultimap.create();
		private final ImmutableMap.Builder<Key, Entry> resources = ImmutableMap.builder();

		Builder() {
		}

		/**
		 * Adds a counter.
		 * @param counter Counter parameters.
		 */
		public void add(Object[] counter) {
			final Long groupId = (Long) counter[0];
			final long resourceId = ((Long) counter[1]).longValue();
			final int count = ((Number) counter[2]).intValue();
			final int min = ((Number) counter[3]).intValue();
			final int max = ((Number) counter[4]).intValue();
			final double mean = ((Number) counter[5]).doubleValue();
			final Key k = new Key(resourceId, groupId);
			final Entry e = new Entry(resourceId, count, min, max, mean);
			if (groupId == null) {
				global.add(e);
			} else {
				group.put(groupId, e);
			}
			resources.put(k, e);
		}

		public RatingMap get() {
			return new RatingMap(this);
		}

	}

}
