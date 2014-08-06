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


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Supplier;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.collect.Multiset.Entry;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Map containing commented resources.
 * @author Andres Rodriguez
 */
public final class CommentMap {
	/** Count. */
	private static final Function<Entry<String>, Integer> COUNT = new Function<Entry<String>, Integer>() {
		public Integer apply(Entry<String> from) {
			return from.getCount();
		}
	};
	/** Ordering. */
	private static final Ordering<Entry<String>> ORDER = Ordering.natural().onResultOf(COUNT).reverse();
	/** Mapper. */
	private static final Function<Entry<String>, ResourceCounterDTO> DTO = new Function<Entry<String>, ResourceCounterDTO>() {
		public ResourceCounterDTO apply(Entry<String> from) {
			return new ResourceCounterDTO(from.getElement(), from.getCount());
		}
	};

	/** Map. */
	private final ImmutableListMultimap<Key, Entry<String>> map;

	public static Builder builder() {
		return new Builder();
	}

	CommentMap(Builder builder) {
		final ImmutableListMultimap.Builder<Key, Entry<String>> b = ImmutableListMultimap.builder();
		for (Map.Entry<Key, Multiset<String>> e : builder.map.entrySet()) {
			b.putAll(e.getKey(), ORDER.sortedCopy(e.getValue().entrySet()));
		}
		this.map = b.build();
	}

	/**
	 * Returns the most commented resources.
	 * @param communityId Community.
	 * @param valid Validity filter.
	 * @param moderated Moderation filter.
	 * @param max Maximum number of results.
	 * @return The requested resources.
	 */
	public List<ResourceCounterDTO> get(UUID communityId, Boolean valid, Boolean moderated, int max) {
		if (max <= 0) {
			return Lists.newArrayListWithCapacity(0);
		}
		if (communityId == null) {
			communityId = CommunityManager.GLOBAL_ID;
		}
		List<Entry<String>> list = map.get(new Key(communityId, valid, moderated));
		if (list.isEmpty()) {
			return Lists.newArrayListWithCapacity(0);
		}
		if (list.size() > max) {
			list = list.subList(0, max);
		}
		return Lists.newArrayList(Iterables.transform(list, DTO));
	}

	/**
	 * Returns the total number of hits in the map.
	 * @return
	 */
	public long size() {
		long size = 0;
		for (Entry<String> e : map.values()) {
			size += e.getCount();
		}
		return size;
	}

	private static final class Key {
		private final UUID communityId;
		private final Boolean valid;
		private final Boolean moderated;

		public Key(UUID communityId, Boolean valid, Boolean moderated) {
			this.communityId = communityId;
			this.valid = valid;
			this.moderated = moderated;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(communityId, valid, moderated);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				final Key k = (Key) obj;
				return Objects.equal(communityId, k.communityId) && Objects.equal(valid, k.valid)
					&& Objects.equal(moderated, k.moderated);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("[communityId:%s/valid:%s/moderated:%s]", communityId, valid, moderated);
		}

		Iterable<Key> others() {
			if (valid == null && moderated == null) {
				return ImmutableSet.of();
			}
			final Set<Key> set = Sets.newHashSet();
			if (valid != null && moderated != null) {
				set.add(new Key(communityId, valid, null));
				set.add(new Key(communityId, null, moderated));
			}
			set.add(new Key(communityId, null, null));
			return set;
		}
	}

	public static final class Builder implements Supplier<CommentMap> {
		private final Map<Key, Multiset<String>> map = Maps.newHashMap();

		Builder() {
		}

		private void add(Key key, String resource) {
			Multiset<String> ms = map.get(key);
			if (ms == null) {
				ms = HashMultiset.create();
				map.put(key, ms);
			}
			ms.add(resource);
		}

		/**
		 * Adds a commment.
		 * @param params Parameters.
		 */
		public void add(Object[] params) {
			final String resource = (String) params[0];
			final UUID communityId = (UUID) params[1];
			final Boolean valid = (Boolean) params[2];
			final boolean moderated = (params[3] != null);
			final Key k = new Key(communityId, valid, moderated);
			add(k, resource);
			for (Key k2 : k.others()) {
				add(k2, resource);
			}
		}

		public CommentMap get() {
			return new CommentMap(this);
		}

	}

}
