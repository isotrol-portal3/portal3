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

package com.isotrol.impe3.api.content;


import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;


/**
 * Simple result for a one-level grouped query. The total number of results represents the number of nodes found in the
 * query and may not be equal to the sum of the counts. Besides, if a transformation function in used and it throws an
 * exception or return {@code null} for an element, it is discarded.
 * @author Andres Rodriguez
 * @param <T> Grouped field type.
 */
public final class OneLevelGroupResult<T> implements Function<T, Integer> {
	/** Total number of nodes. */
	private final int total;
	/** Backing map. */
	private final Map<T, Integer> map;

	public static OneLevelGroupResult<String> of(GroupResult result) {
		return new OneLevelGroupResult<String>(result, Functions.<String> identity());
	}

	public static <T> OneLevelGroupResult<T> of(GroupResult result, Function<String, ? extends T> transformer) {
		return new OneLevelGroupResult<T>(result, transformer);
	}

	private OneLevelGroupResult(GroupResult result, Function<String, ? extends T> f) {
		checkNotNull(f, "Transformation function");
		this.map = Maps.newHashMap();
		if (result != null) {
			this.total = result.getTotalHits();
			final Group rootGroup = result.getGroup();
			if (rootGroup != null) {
				for (String groupName : rootGroup.getGroupNames()) {
					final Group group = rootGroup.getGroup(groupName);
					if (group != null) {
						boolean ok = false;
						T key = null;
						try {
							key = f.apply(groupName);
							ok = true;
						} catch (RuntimeException e) {}
						if (ok && key != null) {
							map.put(key, group.getHits());
						}
					}
				}
			}
		} else {
			this.total = 0;
		}
	}

	/**
	 * Returns the total number of nodes.
	 * @return The total number of nodes.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Returns the number of hits for the provided value (never {@code null}).
	 */
	public Integer apply(T input) {
		if (input == null) {
			return 0;
		}
		return firstNonNull(map.get(input), 0);
	}
}
