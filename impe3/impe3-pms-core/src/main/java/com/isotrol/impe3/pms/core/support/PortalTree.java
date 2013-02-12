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

package com.isotrol.impe3.pms.core.support;


import static com.isotrol.impe3.pms.core.support.EntityFunctions.ID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.core.PortalSelector;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Internal representacion of a portal tree.
 * @author Andres Rodriguez
 */
public final class PortalTree {
	private static final Ordering<PortalEntity> BY_NAME;
	private static final Function<PortalEntity, UUID> PARENT_ID = new Function<PortalEntity, UUID>() {
		public UUID apply(PortalEntity from) {
			if (from == null) {
				return null;
			}
			final PortalDfn dfn = from.getCurrent();
			if (dfn == null) {
				return null;
			}
			final PortalEntity parent = dfn.getParent();
			if (parent == null) {
				return null;
			}
			return parent.getId();
		}
	};

	static {
		final Ordering<String> oi = Ordering.natural();
		final Function<PortalEntity, String> order = new Function<PortalEntity, String>() {
			public String apply(PortalEntity from) {
				return from.getCurrent().getName().getName();
			}
		};
		BY_NAME = oi.onResultOf(order);
	}

	public static ImmutableHierarchy<UUID, PortalEntity> hierarchy(final Iterable<PortalEntity> portals) {
		return ImmutableHierarchy.of(portals, BY_NAME, ID, PARENT_ID);
	}

	private final PortalTreeDTO root;
	private final Map<String, PortalTreeDTO> nodes;

	/**
	 * Constructor.
	 * @param portals Loaded portals.
	 * @param mapper Transformation function.
	 */
	public PortalTree(final Iterable<PortalEntity> portals, final PortalSelector mapper) {
		this.root = new PortalTreeDTO();
		this.root.setNode(null);
		final ImmutableHierarchy<UUID, PortalEntity> h = hierarchy(portals);
		if (h.isEmpty()) {
			this.nodes = ImmutableMap.of();
			List<PortalTreeDTO> empty = Lists.newArrayListWithCapacity(0);
			this.root.setChildren(empty);
			return;
		}
		this.nodes = Maps.newHashMap();
		final Function<PortalEntity, PortalTreeDTO> tree = new Function<PortalEntity, PortalTreeDTO>() {
			public PortalTreeDTO apply(PortalEntity from) {
				final PortalSelDTO sel = mapper.apply(from);
				final String id = sel.getId();
				final PortalTreeDTO dto = new PortalTreeDTO();
				dto.setNode(sel);
				dto.setChildren(Mappers.list(h.getChildren(from.getId()), this));
				nodes.put(id, dto);
				return dto;
			}
		};
		this.root.setChildren(Mappers.list(h.getFirstLevel(), tree));
	}

	/**
	 * Returns the root node.
	 * @return The root node.
	 */
	public PortalTreeDTO getRoot() {
		return root;
	}

	/**
	 * Returns a node by id.
	 * @param id Id
	 * @return The root node.
	 */
	public PortalTreeDTO getNode(UUID id) {
		return nodes.get(id.toString());
	}
}
