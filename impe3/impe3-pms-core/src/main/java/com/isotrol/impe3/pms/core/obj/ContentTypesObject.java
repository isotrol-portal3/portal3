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


import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.obj.ContentTypeObject.MAP2API;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.CONTENT_TYPE;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.core.impl.AbstractIdentifiableMap;
import com.isotrol.impe3.core.impl.ContentTypesFactory;
import com.isotrol.impe3.core.support.IdentifiableMaps;
import com.isotrol.impe3.pbuf.type.ContentTypeProtos.ContentTypesPB;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.core.support.EntityFunctions;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.ContentTypeDfn;
import com.isotrol.impe3.pms.model.ContentTypeEntity;


/**
 * Collection of content types domain object.
 * @author Andres Rodriguez
 */
public final class ContentTypesObject extends AbstractIdentifiableMap<ContentTypeObject> {
	private final ImmutableMap<UUID, ContentTypeObject> map;

	/**
	 * Builds a collection from a set of definitions.
	 * @param dfns Definitions.
	 * @param function State function to apply.
	 * @return The requested collection.
	 */
	public static ContentTypesObject definitions(Iterable<ContentTypeDfn> dfns) {
		return new ContentTypesObject(dfns);
	}

	/**
	 * Builds a collection from a set of current definitions.
	 * @param entities Entities.
	 * @param function State function to apply.
	 */
	public static ContentTypesObject current(Iterable<ContentTypeEntity> entities) {
		return definitions(transform(entities, EntityFunctions.CONTENT_TYPE2DFN));
	}

	/** API collection. */
	private volatile ContentTypes contentTypes = null;

	/**
	 * Constructor.
	 * @param dfns Definitions.
	 * @param function State function to apply.
	 */
	private ContentTypesObject(Iterable<ContentTypeDfn> dfns) {
		final Function<ContentTypeDfn, ContentTypeObject> f = new Function<ContentTypeDfn, ContentTypeObject>() {
			public ContentTypeObject apply(ContentTypeDfn from) {
				return new ContentTypeObject(from);
			}
		};
		map = IdentifiableMaps.immutableOf(transform(dfns, f));
	}

	@Override
	protected Map<UUID, ContentTypeObject> delegate() {
		return map;
	}

	public ContentTypeObject load(UUID id) throws EntityNotFoundException {
		return CONTENT_TYPE.checkNotNull(get(id), id);
	}

	public ContentTypeObject load(ContentTypeEntity e) {
		return get(e.getId());
	}

	public ContentTypeObject load(String id) throws EntityNotFoundException {
		return load(CONTENT_TYPE.toUUID(id));
	}

	public List<ContentTypeSelDTO> map2sel() {
		return Mappers.list(values(), ContentTypeObject.MAP2SEL);
		// TODO: order
	}

	public ContentTypeSelDTO map2sel(ContentTypeEntity e) {
		if (e == null) {
			return null;
		}
		return load(e).toSelDTO();
	}

	public ContentTypes map2api() {
		if (contentTypes == null) {
			contentTypes = ContentTypesFactory.of(transform(map.values(), MAP2API));
		}
		return contentTypes;
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @param f Filter to apply
	 * @return The PB message.
	 */
	public final ContentTypesPB toPB(Predicate<? super ContentTypeObject> f) {
		ContentTypesPB.Builder b = ContentTypesPB.newBuilder();
		b.addAllContentTypes(Iterables.transform(Iterables.filter(map.values(), f), ContentTypeObject.MAP2PB));
		return b.build();
	}

	/**
	 * Transforms the object to a protocol buffer message with no filter.
	 * @return The PB message.
	 */
	public final ContentTypesPB toPB() {
		return toPB(Predicates.alwaysTrue());
	}

}
