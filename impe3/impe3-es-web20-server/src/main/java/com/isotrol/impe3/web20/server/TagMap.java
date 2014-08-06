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


import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newArrayListWithCapacity;

import java.util.List;

import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.web20.api.UsedTagDTO;
import com.isotrol.impe3.web20.model.ResourceEntity;
import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.model.TagNameEntity;


/**
 * Map containing applied tags.
 * @author Andres Rodriguez
 */
public final class TagMap {
	public static final Ordering<UsedTagDTO> BY_COUNT = Ordering.natural().onResultOf(new Function<UsedTagDTO, Long>() {
		public Long apply(UsedTagDTO from) {
			return from.getCount();
		}
	}).reverse();
	public static final Ordering<UsedTagDTO> BY_NAME = Ordering.natural().onResultOf(
		new Function<UsedTagDTO, String>() {
			public String apply(UsedTagDTO from) {
				return from.getName();
			}
		});

	public static final Ordering<UsedTagDTO> COMPOUND = BY_COUNT.compound(BY_NAME);

	/** Use Map. */
	private final ImmutableListMultimap<Long, UsedTagDTO> map;
	/** Tag map. */
	private final ImmutableMap<TagKey, Long> tagMap;

	public static Builder builder() {
		return new Builder();
	}

	TagMap(Builder builder) {
		final ImmutableListMultimap.Builder<Long, UsedTagDTO> b = ImmutableListMultimap.builder();
		for (Long setId : builder.used.keySet()) {
			b.putAll(setId, COMPOUND.sortedCopy(builder.used.get(setId)));
		}
		this.map = b.build();
		this.tagMap = builder.countMap.build();
	}

	/**
	 * Returns the most used tags.
	 * @param setId Tag set id.
	 * @param max Maximum number of results.
	 * @return The requested tags.
	 */
	public List<UsedTagDTO> get(long setId, int max) {
		if (max <= 0) {
			return ImmutableList.of();
		}
		List<UsedTagDTO> entries = map.get(setId);
		if (entries.size() > max) {
			entries = entries.subList(0, max);
		}
		return entries;
	}

	/**
	 * Returns tag suggestions.
	 * @param setId Tag set id.
	 * @param prefix Tag prefix.
	 * @param max Maximum number of results.
	 * @return The requested tags suggestions.
	 */
	public List<UsedTagDTO> suggest(final long setId, final String prefix, final int max) {
		if (max <= 0) {
			return newArrayListWithCapacity(0);
		}
		if (!StringUtils.hasText(prefix)) {
			return newArrayList(get(setId, max));
		}
		final List<UsedTagDTO> entries = map.get(setId);
		final List<UsedTagDTO> list = newArrayListWithCapacity(Math.min(entries.size(), max));
		final Predicate<UsedTagDTO> predicate = new Predicate<UsedTagDTO>() {
			public boolean apply(UsedTagDTO input) {
				if (input != null) {
					String name = input.getName();
					return name != null && name.startsWith(prefix);
				}
				return false;
			}
		};
		int n = 0;
		for (UsedTagDTO dto : Iterables.filter(entries, predicate)) {
			list.add(dto);
			n++;
			if (n >= max) {
				break;
			}
		}
		return list;
	}

	/**
	 * Returns the total number of tags in the map.
	 * @return The total number of applied tags in the map.
	 */
	public long size() {
		long size = 0;
		for (Long c : tagMap.values()) {
			size += c;
		}
		return size;
	}

	public static final class Builder implements Supplier<TagMap> {
		private final ListMultimap<Long, UsedTagDTO> used = ArrayListMultimap.create();
		private final ImmutableMap.Builder<TagKey, Long> countMap = ImmutableMap.builder();

		Builder() {
		}

		/**
		 * Adds a tag entry.
		 * @param tag Tag.
		 */
		public Builder add(TagEntity tag) {
			final Long setId = tag.getSet().getId();
			final TagNameEntity tne = tag.getName();
			final Long nameId = tne.getId();
			final String name = tne.getName();
			final TagKey key = new TagKey(setId, nameId);
			final long count = Iterables.size(Iterables.filter(tag.getResources(), ResourceEntity.NOT_DELETED));
			used.put(setId, new UsedTagDTO(name, count));
			countMap.put(key, count);
			return this;
		}

		/**
		 * Adds some tags entries.
		 * @param tags Tags.
		 */
		public Builder addAll(Iterable<TagEntity> tags) {
			if (tags != null) {
				for (TagEntity tag : tags) {
					add(tag);
				}
			}
			return this;
		}

		public TagMap get() {
			return new TagMap(this);
		}

	}

}
