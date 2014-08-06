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
package com.isotrol.impe3.oi.server;


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
import com.isotrol.impe3.oi.api.UsedClassDTO;
import com.isotrol.impe3.oi.model.InterviewEntity;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.ClassNameEntity;


/**
 * Map containing applied classes.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero.
 */
public final class ClassMap {
	public static final Ordering<UsedClassDTO> BY_COUNT = Ordering.natural().onResultOf(new Function<UsedClassDTO, Long>() {
		public Long apply(UsedClassDTO from) {
			return from.getCount();
		}
	}).reverse();
	public static final Ordering<UsedClassDTO> BY_NAME = Ordering.natural().onResultOf(
		new Function<UsedClassDTO, String>() {
			public String apply(UsedClassDTO from) {
				return from.getName();
			}
		});

	public static final Ordering<UsedClassDTO> COMPOUND = BY_COUNT.compound(BY_NAME);

	/** Use Map. */
	private final ImmutableListMultimap<Long, UsedClassDTO> map;
	/** Class map. */
	private final ImmutableMap<ClassKey, Long> classMap;

	public static Builder builder() {
		return new Builder();
	}

	ClassMap(Builder builder) {
		final ImmutableListMultimap.Builder<Long, UsedClassDTO> b = ImmutableListMultimap.builder();
		for (Long setId : builder.used.keySet()) {
			b.putAll(setId, COMPOUND.sortedCopy(builder.used.get(setId)));
		}
		this.map = b.build();
		this.classMap = builder.countMap.build();
	}

	/**
	 * Returns the most used classes.
	 * @param setId Class set id.
	 * @param max Maximum number of results.
	 * @return The requested classes.
	 */
	public List<UsedClassDTO> get(long setId, int max) {
		if (max <= 0) {
			return ImmutableList.of();
		}
		List<UsedClassDTO> entries = map.get(setId);
		if (entries.size() > max) {
			entries = entries.subList(0, max);
		}
		return entries;
	}

	/**
	 * Returns class suggestions.
	 * @param setId Class set id.
	 * @param prefix Class prefix.
	 * @param max Maximum number of results.
	 * @return The requested classes suggestions.
	 */
	public List<UsedClassDTO> suggest(final long setId, final String prefix, final int max) {
		if (max <= 0) {
			return newArrayListWithCapacity(0);
		}
		if (!StringUtils.hasText(prefix)) {
			return newArrayList(get(setId, max));
		}
		final List<UsedClassDTO> entries = map.get(setId);
		final List<UsedClassDTO> list = newArrayListWithCapacity(Math.min(entries.size(), max));
		final Predicate<UsedClassDTO> predicate = new Predicate<UsedClassDTO>() {
			public boolean apply(UsedClassDTO input) {
				if (input != null) {
					String name = input.getName();
					return name != null && name.startsWith(prefix);
				}
				return false;
			}
		};
		int n = 0;
		for (UsedClassDTO dto : Iterables.filter(entries, predicate)) {
			list.add(dto);
			n++;
			if (n >= max) {
				break;
			}
		}
		return list;
	}

	/**
	 * Returns the total number of classes in the map.
	 * @return The total number of applied classes in the map.
	 */
	public long size() {
		long size = 0;
		for (Long c : classMap.values()) {
			size += c;
		}
		return size;
	}

	public static final class Builder implements Supplier<ClassMap> {
		private final ListMultimap<Long, UsedClassDTO> used = ArrayListMultimap.create();
		private final ImmutableMap.Builder<ClassKey, Long> countMap = ImmutableMap.builder();

		Builder() {
		}

		/**
		 * Adds a class entry.
		 * @param classification Class.
		 */
		public Builder add(ClassEntity classification) {
			final Long setId = classification.getSet().getId();
			final ClassNameEntity tne = classification.getName();
			final Long nameId = tne.getId();
			final String name = tne.getName();
			final ClassKey key = new ClassKey(setId, nameId);
			final long count = Iterables.size(Iterables.filter(classification.getInterviews(), InterviewEntity.NOT_DELETED));
			used.put(setId, new UsedClassDTO(name, count));
			countMap.put(key, count);
			return this;
		}

		/**
		 * Adds some classes entries.
		 * @param classes Classes.
		 */
		public Builder addAll(Iterable<ClassEntity> classes) {
			if (classes != null) {
				for (ClassEntity classify : classes) {
					add(classify);
				}
			}
			return this;
		}

		public ClassMap get() {
			return new ClassMap(this);
		}

	}

}
