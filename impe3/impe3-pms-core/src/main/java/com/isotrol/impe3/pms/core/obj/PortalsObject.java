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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.PORTAL;

import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.core.impl.AbstractIdentifiableHierarchy;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.portal.PortalParentDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.core.CacheKey;
import com.isotrol.impe3.pms.core.PortalTxProvider;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Collection of content types domain object.
 * @author Andres Rodriguez
 */
public final class PortalsObject extends AbstractIdentifiableHierarchy<PortalObject> {
	private static final Ordering<PortalObject> BY_NAME = Ordering.natural().onResultOf(
		new Function<PortalObject, String>() {
			public String apply(PortalObject from) {
				return from.getName().getDisplayName();
			}
		});

	public static CacheKey key(EnvironmentEntity e) {
		return CacheKey.create(e.getId(), e.getPortalVersion());
	}

	/**
	 * Builds a collection from a set of editions.
	 * @param e Edition.
	 * @return The requested collection.
	 */
	public static PortalsObject edition(final PortalTxProvider tx, EditionEntity e) {
		Function<PortalEdition, PortalObject> f = new Function<PortalEdition, PortalObject>() {
			public PortalObject apply(PortalEdition from) {
				return new PortalObject(tx, null, from.getId(), from.getPublished());
			}
		};
		return new PortalsObject(transform(e.getPortals(), f));
	}

	/**
	 * Builds a collection from a set of current definitions.
	 * @param e Environment.
	 */
	public static PortalsObject current(final PortalTxProvider tx, EnvironmentEntity e, Iterable<PortalEntity> portals) {
		final CacheKey key = key(e);
		Function<PortalEntity, PortalObject> f = new Function<PortalEntity, PortalObject>() {
			public PortalObject apply(PortalEntity from) {
				return new PortalObject(tx, key, null, from.getCurrent());
			}
		};
		return new PortalsObject(transform(portals, f));
	}

	/** Portal hierarchy. */
	private final ImmutableHierarchy<UUID, PortalObject> hierarchy;

	/**
	 * Constructor.
	 * @param portals Portals.
	 */
	private PortalsObject(Iterable<PortalObject> portals) {
		final Iterable<PortalObject> ordered = BY_NAME.sortedCopy(portals);
		ImmutableHierarchy.Builder<UUID, PortalObject> builder = ImmutableHierarchy.builder();
		for (PortalObject p : ordered) {
			builder.add(p.getId(), p, p.getParentId());
		}
		hierarchy = builder.get();
	}

	@Override
	protected Hierarchy<UUID, PortalObject> delegate() {
		return hierarchy;
	}

	public PortalObject load(UUID id) throws EntityNotFoundException {
		return PORTAL.checkNotNull(get(id), id);
	}

	public PortalObject load(PortalEntity e) {
		if (e == null) {
			return null;
		}
		return get(e.getId());
	}

	public PortalObject load(String id) throws EntityNotFoundException {
		return load(PORTAL.toUUID(id));
	}

	/**
	 * Returns the tree of portals, excluding the specified node.
	 * @param exclude Portal to exlude. Optional.
	 * @return The requested tree.
	 */
	private PortalTreeDTO map2tree(final PortalObject exclude) {
		final PortalTreeDTO root = new PortalTreeDTO();
		root.setNode(null);
		if (isEmpty()) {
			root.setChildren(Lists.<PortalTreeDTO> newArrayListWithCapacity(0));
			return root;
		}
		final Predicate<PortalObject> filter;
		if (exclude == null) {
			filter = Predicates.alwaysTrue();
		} else {
			filter = new Predicate<PortalObject>() {
				public boolean apply(PortalObject input) {
					return !exclude.getId().equals(input.getId());
				}
			};
		}
		final Function<PortalObject, PortalTreeDTO> tree = new Function<PortalObject, PortalTreeDTO>() {
			public PortalTreeDTO apply(PortalObject from) {
				final PortalTreeDTO dto = new PortalTreeDTO();
				dto.setNode(from.toSelDTO());
				dto.setChildren(Mappers.list(filter(hierarchy.getChildren(from.getId()), filter), this));
				return dto;
			}
		};
		root.setChildren(Mappers.list(filter(hierarchy.getFirstLevel(), filter), tree));
		return root;
	}

	public PortalTreeDTO map2tree() {
		return map2tree(null);
	}

	public PortalParentDTO getParent(PortalObject portal) {
		checkArgument(containsValue(portal));
		final PortalParentDTO dto = new PortalParentDTO();
		dto.setId(portal.getStringId());
		dto.setState(portal.getState());
		final PortalObject parent = getParent(portal.getId());
		dto.setParent(parent != null ? parent.toSelDTO() : null);
		dto.setPortalTree(map2tree(portal));
		return dto;
	}
}
