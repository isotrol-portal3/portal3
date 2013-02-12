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

package com.isotrol.impe3.core;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchies;
import net.sf.derquinsej.collect.HierarchyVisitor;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.WithDeviceProperty;


/**
 * Value representing IMPE3 pages.
 * @author Andres Rodriguez
 */
public final class Page extends AbstractIdentifiable implements WithDeviceProperty {
	private final Device device;
	private final PageMapKey key;
	private final ImmutableHierarchy<UUID, CIP> components;
	private final ImmutableList<Frame> layout;
	/** Components in execution order. */
	private final ImmutableList<CIP> order;

	public Page(UUID id, Device device, PageMapKey key, ImmutableHierarchy<UUID, CIP> components, List<Frame> layout) {
		super(id);
		this.device = checkNotNull(device);
		this.key = key;
		this.components = checkNotNull(components);
		if (layout == null) {
			this.layout = ImmutableList.of();
		} else {
			this.layout = ImmutableList.copyOf(layout);
		}
		final ImmutableList.Builder<CIP> orderBuilder = ImmutableList.builder();
		final HierarchyVisitor<UUID, CIP> visitor = new HierarchyVisitor<UUID, CIP>() {
			public void visit(UUID key, CIP value, UUID parentKey, int position) {
				orderBuilder.add(value);
			}
		};
		Hierarchies.visitDepthFirst(this.components, visitor);
		this.order = orderBuilder.build();
	}

	public Device getDevice() {
		return device;
	}

	public PageMapKey getKey() {
		return key;
	}

	public ImmutableHierarchy<UUID, CIP> getComponents() {
		return components;
	}

	public ImmutableList<Frame> getLayout() {
		return layout;
	}

	/**
	 * Returns the execution order.
	 * @return The execution order.
	 */
	public ImmutableList<CIP> getOrder() {
		return order;
	}
}
