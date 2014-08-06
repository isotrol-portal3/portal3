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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


/**
 * Configuration values for time-based map caches.
 * @author Andres Rodriguez
 */
public abstract class TimeMapConfig {
	private static final TimeMapConfig DEFAULT = new Default();

	public static TimeMapConfig defaultConfig() {
		return DEFAULT;
	}

	public static TimeMapConfig delay(long seconds) {
		return new Delay(seconds);
	}

	public static TimeMapConfig create(long seconds, boolean global, List<Long> intervals) {
		if (intervals == null) {
			intervals = ImmutableList.of();
		}
		if (intervals.isEmpty()) {
			return delay(seconds);
		}
		return new Regular(seconds, global, intervals);
	}

	private TimeMapConfig() {
	}

	/**
	 * Returns the computation delay.
	 * @return The computation delay.
	 */
	public abstract long getDelay();

	/**
	 * Returns whether to include global map.
	 * @return True if all-time values are included.
	 */
	public abstract boolean isGlobal();

	/**
	 * Returns the intervals to include.
	 * @return Intervals represented as a list of "seconds from now".
	 */
	public abstract ImmutableList<Long> getIntervals();

	private static final class Default extends TimeMapConfig {
		private Default() {
		}

		@Override
		public long getDelay() {
			return 30L;
		}

		@Override
		public ImmutableList<Long> getIntervals() {
			return ImmutableList.of();
		}

		@Override
		public boolean isGlobal() {
			return true;
		}
	}

	private static class Delay extends TimeMapConfig {
		private final long delay;

		Delay(long delay) {
			checkArgument(delay > 0L, "The delay must be > 0");
			this.delay = delay;
		}

		@Override
		public final long getDelay() {
			return delay;
		}

		@Override
		public ImmutableList<Long> getIntervals() {
			return ImmutableList.of();
		}

		@Override
		public boolean isGlobal() {
			return true;
		}
	}

	private static class Regular extends Delay {
		private final boolean global;
		private final ImmutableList<Long> intervals;

		Regular(long delay, boolean global, List<Long> intervals) {
			super(delay);
			this.global = global;
			checkArgument(Iterables.all(checkNotNull(intervals), new Predicate<Long>() {
				public boolean apply(Long input) {
					return input != null && input.longValue() > 0;
				}
			}), "The intervals must be > 0");
			this.intervals = ImmutableList.copyOf(intervals);
		}

		@Override
		public ImmutableList<Long> getIntervals() {
			return intervals;
		}

		@Override
		public boolean isGlobal() {
			return global;
		}
	}

}
