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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.not;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;


/**
 * Devices in portal collection.
 * @author Andres Rodriguez
 */
public abstract class DevicesInPortal {
	private static final Empty EMPTY = new Empty();

	/**
	 * Creates the collection.
	 * @param devices Registered devices.
	 * @return The requested collection.
	 */
	public static DevicesInPortal of(Set<DeviceInPortal> devices) {
		checkNotNull(devices, "Devices");
		if (devices.isEmpty()) {
			return EMPTY;
		}
		return new Regular(checkNotNull(devices, "Devices"));
	}

	/** Constructor. */
	private DevicesInPortal() {
	}

	/**
	 * @return The by device map.
	 */
	abstract Map<Device, DeviceInPortal> deviceMap();

	/**
	 * @return The by device id map.
	 */
	abstract Map<UUID, DeviceInPortal> idMap();

	/**
	 * @return The by device name in portal map.
	 */
	abstract Map<String, DeviceInPortal> nameMap();

	/**
	 * Returns whether the collection is empty.
	 * @return True if the collection is empty.
	 */
	public final boolean isEmpty() {
		return deviceMap().isEmpty();
	}

	/**
	 * Returns the number of devices definitions in the collection.
	 * @return The collection size.
	 */
	public final int size() {
		return deviceMap().size();
	}

	/**
	 * Returns the devices definitions in the portal.
	 * @return The registered devices definitions.
	 */
	public final Collection<DeviceInPortal> values() {
		return deviceMap().values();
	}

	/**
	 * Returns the devices registered in the portal.
	 * @return The devices registered in the portal.
	 */
	public final Set<Device> getDevices() {
		return deviceMap().keySet();
	}

	/**
	 * Returns whether the a device is registered.
	 * @param device Device to check.
	 * @return True if the device is registered.
	 */
	public final boolean containsDevice(Device device) {
		return deviceMap().containsKey(device);
	}

	/**
	 * Returns the device definition for a given device.
	 * @param device Device to get.
	 * @return The device definition.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the device is not registered.
	 */
	public final DeviceInPortal getByDevice(Device device) {
		final DeviceInPortal dip = deviceMap().get(checkNotNull(device, "Null devices not allowed"));
		checkArgument(dip != null, "Device not registered");
		return dip;
	}

	/**
	 * Returns the ids of the devices registered in the portal.
	 * @return The ids of the devices registered in the portal.
	 */
	public final Set<UUID> getDeviceIds() {
		return idMap().keySet();
	}

	/**
	 * Returns whether the a device is registered.
	 * @param deviceId Device id to check.
	 * @return True if the device is registered.
	 */
	public final boolean containsDeviceId(UUID deviceId) {
		return idMap().containsKey(deviceId);
	}

	/**
	 * Returns the device definition for a given device.
	 * @param deviceId Device id to get.
	 * @return The device definition.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the device is not registered.
	 */
	public final DeviceInPortal getByDeviceId(Device deviceId) {
		final DeviceInPortal dip = idMap().get(checkNotNull(deviceId, "Null devices not allowed"));
		checkArgument(dip != null, "Device not registered");
		return dip;
	}

	/**
	 * Returns the names of the devices registered in the portal.
	 * @return The names of the devices registered in the portal.
	 */
	public final Set<String> getNames() {
		return nameMap().keySet();
	}

	/**
	 * Returns whether the a device with a certain name is registered.
	 * @param name Name to check.
	 * @return True if the device is registered.
	 */
	public final boolean containsName(String name) {
		return nameMap().containsKey(name);
	}

	/**
	 * Returns the device definition registered with a certain name.
	 * @param name Name to get.
	 * @return The device definition.
	 * @throws NullPointerException if the argument is null.
	 * @throws IllegalArgumentException if the device is not registered.
	 */
	public final DeviceInPortal getByName(String name) {
		final DeviceInPortal dip = nameMap().get(checkNotNull(name, "Null device names not allowed"));
		checkArgument(dip != null, "Device name not registered");
		return dip;
	}

	/**
	 * Filters the collection by device name use.
	 * @param use Use to filter.
	 * @return The filtered collection.
	 */
	public final DevicesInPortal filterByUse(DeviceNameUse use) {
		checkNotNull(use, "Device name use");
		Predicate<DeviceInPortal> f = Predicates.compose(equalTo(use), DeviceInPortal.USE);
		if (Iterables.any(values(), f)) {
			return new Filter(this, f);
		} else {
			return EMPTY;
		}
	}

	/**
	 * Excludes one device from the collection.
	 * @param device Device to exclude.
	 * @return The filtered collection.
	 */
	public final DevicesInPortal excludeDevice(Device device) {
		checkNotNull(device, "Null devices not allowed");
		if (!containsDevice(device)) {
			return this;
		}
		if (size() == 1) {
			return EMPTY;
		}
		return new Filter(this, Predicates.compose(not(equalTo(device)), DeviceInPortal.DEVICE));
	}

	/**
	 * Excludes one device from the collection.
	 * @param deviceId Device id to exclude.
	 * @return The filtered collection.
	 */
	public final DevicesInPortal excludeDeviceId(UUID deviceId) {
		checkNotNull(deviceId, "Null devices not allowed");
		if (!containsDeviceId(deviceId)) {
			return this;
		}
		if (size() == 1) {
			return EMPTY;
		}
		return new Filter(this, Predicates.compose(not(equalTo(deviceId)),
			Functions.compose(Device.ID, DeviceInPortal.DEVICE)));
	}

	private static String checkSegment(String segment) {
		return checkNotNull(segment, "Null segment");
	}

	private Device find(DeviceNameUse use, String name) {
		final DevicesInPortal dips = filterByUse(use);
		if (dips.containsName(name)) {
			return dips.getByName(name).getDevice();
		}
		return null;
	}

	/**
	 * Returns the device represented by a first segment.
	 * @param segment Segment to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByFirstSegment(String segment) {
		return find(DeviceNameUse.FIRST_SEGMENT, checkSegment(segment));
	}

	/**
	 * Returns the device represented by a first segment.
	 * @param path Path to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByFirstSegment(PathSegments path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		return findDeviceByFirstSegment(path.get(0));
	}
	
	/**
	 * Returns the device represented by a last segment.
	 * @param segment Segment to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByLastSegment(String segment) {
		return find(DeviceNameUse.LAST_SEGMENT, checkSegment(segment));
	}
	
	/**
	 * Returns the device represented by a last segment.
	 * @param path Path to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByLastSegment(PathSegments path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		return findDeviceByLastSegment(path.last());
	}

	/**
	 * Returns the device represented by an extension.
	 * @param extension Extension to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByExtension(String extension) {
		return find(DeviceNameUse.EXTENSION, checkNotNull(extension, "Extension"));
	}

	/**
	 * Returns the device represented by a last segment extension.
	 * @param segment Segment to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByLastSegmentExtension(String segment) {
		final String extension = PathSegments.getExtension(checkSegment(segment));
		if (extension != null) {
			return findDeviceByExtension(extension);
		}
		return null;
	}
	
	/**
	 * Returns the device represented by a last segment extension.
	 * @param path Path to check.
	 * @return The resolved device or {@code null} if no device is found.
	 */
	public final Device findDeviceByLastSegmentExtension(PathSegments path) {
		if (path == null || path.isEmpty()) {
			return null;
		}
		return findDeviceByLastSegmentExtension(path.last());
	}

	private static final class Empty extends DevicesInPortal {
		private Empty() {
		}

		@Override
		Map<Device, DeviceInPortal> deviceMap() {
			return ImmutableMap.of();
		}

		@Override
		Map<UUID, DeviceInPortal> idMap() {
			return ImmutableMap.of();
		}

		@Override
		Map<String, DeviceInPortal> nameMap() {
			return ImmutableMap.of();
		}
	}

	private static final class Regular extends DevicesInPortal {
		private final ImmutableMap<Device, DeviceInPortal> deviceMap;
		private final ImmutableMap<UUID, DeviceInPortal> idMap;
		private final ImmutableMap<String, DeviceInPortal> nameMap;

		private Regular(Set<DeviceInPortal> devices) {
			try {
				deviceMap = Maps.uniqueIndex(devices, DeviceInPortal.DEVICE);
			} catch (RuntimeException e) {
				throw new IllegalArgumentException("Unable to index devices in portal by device");
			}
			try {
				idMap = Maps.uniqueIndex(devices, Functions.compose(Device.ID, DeviceInPortal.DEVICE));
			} catch (RuntimeException e) {
				throw new IllegalArgumentException("Unable to index devices in portal by device id");
			}
			try {
				nameMap = Maps.uniqueIndex(devices, DeviceInPortal.NAME);
			} catch (RuntimeException e) {
				throw new IllegalStateException("Unable to index devices in portal by name");
			}
		}

		@Override
		Map<Device, DeviceInPortal> deviceMap() {
			return deviceMap;
		}

		@Override
		Map<UUID, DeviceInPortal> idMap() {
			return idMap;
		}

		@Override
		Map<String, DeviceInPortal> nameMap() {
			return nameMap;
		}
	}

	private static final class Filter extends DevicesInPortal {
		private final Map<Device, DeviceInPortal> deviceMap;
		private final Map<UUID, DeviceInPortal> idMap;
		private final Map<String, DeviceInPortal> nameMap;

		private Filter(DevicesInPortal devices, Predicate<? super DeviceInPortal> filter) {
			this.deviceMap = Maps.filterValues(devices.deviceMap(), filter);
			this.idMap = Maps.filterValues(devices.idMap(), filter);
			this.nameMap = Maps.filterValues(devices.nameMap(), filter);
		}

		@Override
		Map<Device, DeviceInPortal> deviceMap() {
			return deviceMap;
		}

		@Override
		Map<UUID, DeviceInPortal> idMap() {
			return idMap;
		}

		@Override
		Map<String, DeviceInPortal> nameMap() {
			return nameMap;
		}
	}

}
