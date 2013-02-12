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

package com.isotrol.impe3.pms.core;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;


/**
 * Object representing the coordinates of a published object.
 * @author Andres Rodriguez
 */
public final class PublishedObject {
	public static final Function<PublishedObject, UUID> ENTITY_ID = new Function<PublishedObject, UUID>() {
		public UUID apply(PublishedObject from) {
			return from.getEntityId();
		}
	};

	public static final Function<PublishedObject, UUID> DFN_ID = new Function<PublishedObject, UUID>() {
		public UUID apply(PublishedObject from) {
			return from.getDefinitionId();
		}
	};

	public static final Function<PublishedObject, UUID> EDITION_ID = new Function<PublishedObject, UUID>() {
		public UUID apply(PublishedObject from) {
			return from.getEditionId();
		}
	};

	/** Factory method. */
	public static PublishedObject of(UUID entityId, UUID definitionId, UUID editionId) {
		return new PublishedObject(entityId, definitionId, editionId);
	}

	/** Published entity id. */
	private final UUID entityId;
	/** Published definition id. */
	private final UUID definitionId;
	/** Published edition id. */
	private final UUID editionId;
	/** Hash code. */
	private final int hash;

	/** Constructor. */
	private PublishedObject(UUID entityId, UUID definitionId, UUID editionId) {
		this.entityId = checkNotNull(entityId);
		this.definitionId = checkNotNull(definitionId);
		this.editionId = checkNotNull(editionId);
		this.hash = Objects.hashCode(entityId, definitionId, editionId);
	}

	/**
	 * Returns the id of the published entity.
	 * @return The id of the published entity.
	 */
	public UUID getEntityId() {
		return entityId;
	}

	/**
	 * Returns the id of the published definition.
	 * @return The id of the published definition.
	 */
	public UUID getDefinitionId() {
		return definitionId;
	}

	/**
	 * Returns the id of the edition.
	 * @return The id of the edition.
	 */
	public UUID getEditionId() {
		return editionId;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PublishedObject) {
			PublishedObject p = (PublishedObject) obj;
			return hash == p.hash && entityId.equals(p.entityId) && definitionId.equals(p.definitionId)
				&& editionId.equals(p.editionId);
		}
		return false;
	}

}
