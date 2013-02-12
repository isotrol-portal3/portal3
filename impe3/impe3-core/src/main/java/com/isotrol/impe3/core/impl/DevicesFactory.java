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


import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Devices;


/**
 * Factory class for different categories collection implementations.
 * @author Andres Rodriguez
 */
public final class DevicesFactory {
	/** Not instantiable. */
	private DevicesFactory() {
		throw new AssertionError();
	}

	private static final Empty EMPTY = new Empty();

	/**
	 * Returns an empty collection.
	 */
	public static Devices of() {
		return EMPTY;
	}

	/**
	 * Returns a collection of devices containing the provided hierarchy.
	 * @param devices Hierarchy of devices.
	 * @return The requested collection.
	 * @throws NullPointerException if the argument is null.
	 */
	public static Devices of(ImmutableHierarchy<UUID, Device> devices) {
		Preconditions.checkNotNull(devices);
		return new Immutable(devices);
	}

	/**
	 * Abstract collection.
	 * @author Andres Rodriguez
	 */
	private abstract static class Abstract extends AbstractIdentifiableHierarchy<Device> implements Devices {
		private Abstract() {
		}
	}

	/**
	 * Empty collection.
	 * @author Andres Rodriguez
	 */
	private static final class Empty extends Abstract {
		private Empty() {
		}

		@Override
		protected Hierarchy<UUID, Device> delegate() {
			return ImmutableHierarchy.of();
		}

		public Device getByName(String name) {
			return null;
		}

		public Set<String> getNames() {
			return ImmutableSet.of();
		}
	}

	/**
	 * Immutable collection.
	 * @author Andres Rodriguez
	 */
	private static final class Immutable extends Abstract {
		private ImmutableHierarchy<UUID, Device> hierarchy;
		private ImmutableMap<String, Device> byName;

		private Immutable(ImmutableHierarchy<UUID, Device> hierarchy) {
			this.hierarchy = hierarchy;
			this.byName = Maps.uniqueIndex(hierarchy.values(), Device.NAME);
		}

		@Override
		protected Hierarchy<UUID, Device> delegate() {
			return hierarchy;
		}

		public Device getByName(String name) {
			return byName.get(name);
		}

		public Set<String> getNames() {
			return byName.keySet();
		}
	}
}
