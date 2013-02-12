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


import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.MAPPING;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.impl.AbstractIdentifiableMap;
import com.isotrol.impe3.pbuf.mappings.MappingProtos.MappingsPB;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.model.SourceMappingEntity;


/**
 * Source mappings collection.
 * @author Andres Rodriguez
 */
public final class SourceMappingsObject extends AbstractIdentifiableMap<SourceMappingObject> {
	private static final Function<SourceMappingObject, SourceMappingSelDTO> MAP2SEL = new Function<SourceMappingObject, SourceMappingSelDTO>() {
		public SourceMappingSelDTO apply(SourceMappingObject from) {
			return from.toSel();
		};
	};

	/** By Id. */
	private final ImmutableMap<UUID, SourceMappingObject> byId;
	/** By name. */
	private final ImmutableSortedMap<String, SourceMappingObject> byName;

	/**
	 * Builds a collection from a set of entities.
	 * @param mappings Mapping entities.
	 * @return The requested collection.
	 */
	public static SourceMappingsObject of(Iterable<SourceMappingEntity> mappings) {
		return new SourceMappingsObject(mappings);
	}

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	private SourceMappingsObject(Iterable<SourceMappingEntity> mappings) {
		final Map<UUID, SourceMappingObject> byId = Maps.newHashMap();
		final Map<String, SourceMappingObject> byName = Maps.newHashMap();
		for (SourceMappingEntity mapping : mappings) {
			final UUID id = mapping.getId();
			if (byId.containsKey(id)) {
				Loggers.pms().error(String.format("Duplicate source mapping with id [%s]. Skipping...", id));
			}
			final String name = mapping.getName();
			if (byName.containsKey(name)) {
				Loggers.pms().error(String.format("Duplicate source mapping with name [%s]. Skipping...", name));
			}
			final SourceMappingObject smo = new SourceMappingObject(mapping);
			byId.put(id, smo);
			byName.put(name, smo);
		}
		this.byId = ImmutableMap.copyOf(byId);
		this.byName = ImmutableSortedMap.copyOf(byName);
	}

	@Override
	protected Map<UUID, SourceMappingObject> delegate() {
		return byId;
	}

	public SourceMappingObject load(UUID id) throws EntityNotFoundException {
		return MAPPING.checkNotNull(get(id), id);
	}

	public SourceMappingObject load(String id) throws EntityNotFoundException {
		return load(MAPPING.toUUID(id));
	}

	/**
	 * Returns a source mapping by name.
	 * @param name Requested name.
	 * @return The source mapping with the requested name or {@code null} if not found.
	 */
	public SourceMappingObject getByName(String name) {
		return byName.get(name);
	}

	public List<SourceMappingSelDTO> map2sel() {
		return newArrayList(transform(byName.values(), MAP2SEL));
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @param f Filter to apply
	 * @return The PB message.
	 */
	public final MappingsPB toPB(Predicate<? super SourceMappingObject> f) {
		MappingsPB.Builder b = MappingsPB.newBuilder();
		b.addAllMappings(Iterables.transform(Iterables.filter(byName.values(), f), SourceMappingObject.MAP2PB));
		return b.build();
	}

	/**
	 * Transforms the object to a protocol buffer message with no filter.
	 * @return The PB message.
	 */
	public final MappingsPB toPB() {
		return toPB(Predicates.alwaysTrue());
	}

	/**
	 * Returns whether a content type is used by this mapping.
	 * @param id Content type id.
	 * @return True if the content type is used by this mapping.
	 */
	public boolean isContentTypeUsed(final UUID id) {
		return any(values(), new Predicate<SourceMappingObject>() {
			public boolean apply(SourceMappingObject input) {
				return input.isContentTypeUsed(id);
			}
		});
	}

	/**
	 * Returns whether a category is used by this mapping.
	 * @param id Category id.
	 * @return True if the category is used by this mapping.
	 */
	public boolean isCategoryUsed(final UUID id) {
		return any(values(), new Predicate<SourceMappingObject>() {
			public boolean apply(SourceMappingObject input) {
				return input.isCategoryUsed(id);
			}
		});
	}

}
