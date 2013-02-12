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


import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.model.CategoryDfn;
import com.isotrol.impe3.pms.model.CategoryEdition;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.ConnectorEdition;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeDfn;
import com.isotrol.impe3.pms.model.ContentTypeEdition;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.Definition;
import com.isotrol.impe3.pms.model.Entity;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.PublishableEntity;
import com.isotrol.impe3.pms.model.Published;
import com.isotrol.impe3.pms.model.PublishedEntity;


/**
 * Several commonly used functions.
 * @author Andres Rodriguez.
 */
public final class EntityFunctions {
	/** Not instantiable. */
	private EntityFunctions() {
		throw new AssertionError();
	}

	/** Entity to id. */
	public static final Function<Entity, UUID> ID = new Function<Entity, UUID>() {
		public UUID apply(Entity from) {
			return from.getId();
		}
	};

	/** Not deleted. */
	public static final Predicate<PublishableEntity<?, ?, ?>> NOT_DELETED = new Predicate<PublishableEntity<?, ?, ?>>() {
		public boolean apply(PublishableEntity<?, ?, ?> input) {
			return !input.isDeleted();
		}
	};

	/** Filter not deleted. */
	public static <T extends PublishableEntity<?, ?, ?>> Iterable<T> notDeleted(Iterable<T> entities) {
		return Iterables.filter(entities, NOT_DELETED);
	}

	/** Current definition ID. */
	public static final Function<PublishableEntity<?, ?, ?>, UUID> CURRENT_DFN_ID = new Function<PublishableEntity<?, ?, ?>, UUID>() {
		public UUID apply(PublishableEntity<?, ?, ?> from) {
			return from.getCurrentId();
		}
	};

	/** Category to its current definition. */
	public static final Function<CategoryEntity, CategoryDfn> CATEGORY2DFN = new Entity2Dfn<CategoryEntity, CategoryDfn, CategoryEdition>();

	/** Content type to its current definition. */
	public static final Function<ContentTypeEntity, ContentTypeDfn> CONTENT_TYPE2DFN = new Entity2Dfn<ContentTypeEntity, ContentTypeDfn, ContentTypeEdition>();

	/** Connector to its current definition. */
	public static final Function<ConnectorEntity, ConnectorDfn> CONNECTOR2DFN = new Entity2Dfn<ConnectorEntity, ConnectorDfn, ConnectorEdition>();

	/** Portal to its current definition. */
	public static final Function<PortalEntity, PortalDfn> PORTAL2DFN = new Entity2Dfn<PortalEntity, PortalDfn, PortalEdition>();

	/** Category to its current definition. */
	public static final Function<CategoryDfn, CategoryEntity> DFN2CATEGORY = new Dfn2Entity<CategoryDfn, CategoryEntity, CategoryEdition>();

	/** Content type to its current definition. */
	public static final Function<ContentTypeDfn, ContentTypeEntity> DFN2CONTENT_TYPE = new Dfn2Entity<ContentTypeDfn, ContentTypeEntity, ContentTypeEdition>();

	/** Connector to its current definition. */
	public static final Function<ConnectorDfn, ConnectorEntity> DFN2CONNECTOR = new Dfn2Entity<ConnectorDfn, ConnectorEntity, ConnectorEdition>();

	/** Portal to its current definition. */
	public static final Function<PortalDfn, PortalEntity> DFN2PORTAL = new Dfn2Entity<PortalDfn, PortalEntity, PortalEdition>();

	/** Published entity. */
	public static final Function<Published, UUID> PUBLISHED_ENTITY = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getEntityId();
		}
	};

	/** Published definition. */
	public static final Function<Published, UUID> PUBLISHED_DFN = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getDefinitionId();
		}
	};

	/** Published edition. */
	public static final Function<Published, UUID> PUBLISHED_EDITION = new Function<Published, UUID>() {
		public UUID apply(Published from) {
			return from.getEditionId();
		}
	};

	public static String stringId(UUID id) {
		if (id == null) {
			return null;
		}
		return id.toString().toLowerCase();
	}

	public static String stringId(Entity entity) {
		if (entity == null) {
			return null;
		}
		return stringId(entity.getId());
	}

	/**
	 * Returns a constant state mapper.
	 * @param state State to map to.
	 * @return The requested function.
	 */
	public static final Function<PublishableEntity<?, ?, ?>, State> constantState(final State state) {
		return new Function<PublishableEntity<?, ?, ?>, State>() {
			public State apply(PublishableEntity<?, ?, ?> from) {
				return state;
			}
		};
	}

	private static final class Entity2Dfn<T extends PublishableEntity<T, D, P>, D extends Definition<D, T, P>, P extends PublishedEntity>
		implements Function<T, D> {
		public D apply(T from) {
			return from.getCurrent();
		};
	}

	private static final class Dfn2Entity<D extends Definition<D, T, P>, T extends PublishableEntity<T, D, P>, P extends PublishedEntity>
		implements Function<D, T> {
		public T apply(D from) {
			return from.getEntity();
		};
	}

}
