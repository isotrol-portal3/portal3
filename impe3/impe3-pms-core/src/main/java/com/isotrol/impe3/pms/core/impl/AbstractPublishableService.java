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

package com.isotrol.impe3.pms.core.impl;


import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.support.EntityFunctions;
import com.isotrol.impe3.pms.core.support.StateSupport;
import com.isotrol.impe3.pms.model.Definition;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.PublishableEntity;
import com.isotrol.impe3.pms.model.PublishedEntity;


/**
 * Abstract implementation of servicies related to publishable entities.
 * @author Andres Rodriguez.
 * @param <D> Definition type.
 * @param <T> Publishable Entity type.
 */
public abstract class AbstractPublishableService<T extends PublishableEntity<T, D, P>, D extends Definition<D, T, P>, P extends PublishedEntity>
	extends AbstractOfEnvironmentService<T> {

	static void validate(NameDTO dto) {
		Preconditions.checkNotNull(dto);
		Preconditions.checkNotNull(dto.getDisplayName());
		Preconditions.checkNotNull(dto.getPath());
	}

	static void validate(Map<String, NameDTO> localizedNames) {
		Preconditions.checkNotNull(localizedNames);
		Preconditions.checkNotNull(localizedNames.keySet());
		for (NameDTO name : localizedNames.values()) {
			validate(name);
		}
	}

	static <U extends PublishableEntity<?, ?, ?>> Iterable<U> filterNotDeleted(Iterable<U> entities) {
		return Iterables.filter(entities, EntityFunctions.NOT_DELETED);
	}

	/** Default constructor. */
	public AbstractPublishableService() {
	}

	/**
	 * Saves a new entity with a new definition, setting the latter as the current definition.
	 * @param environment Environment.
	 * @param entity Entity to save.
	 * @param definition Definition to save.
	 * @return The entity Id.
	 */
	protected final UUID saveNewEntity(EnvironmentEntity environment, T entity, D definition) throws PMSException {
		saveNew(environment, entity);
		flush();
		saveNewDfn(entity, definition);
		return entity.getId();
	}

	/**
	 * Saves a new entity with a new definition, setting the latter as the current definition.
	 * @param entity Entity to save.
	 * @param definition Definition to save.
	 * @return The entity Id.
	 */
	protected final UUID saveNewEntity(T entity, D definition) throws PMSException {
		return saveNewEntity(getEnvironment(), entity, definition);
	}

	/**
	 * Saves a new definition for an existing entity, setting it as the current definition.
	 * @param entity Entity.
	 * @param definition Definition to save.
	 * @return The definition Id.
	 */
	protected final UUID saveNewDfn(T entity, D definition) throws PMSException {
		definition.setEntity(entity);
		saveNewEntity(definition);
		entity.setCurrent(definition);
		return definition.getId();
	}

	/**
	 * Checks whether a new definition must be created.
	 * @param definition Current definition.
	 * @return True if a new definition must be created.
	 */
	protected final boolean isNewDfnNeeded(D definition) {
		return Boolean.TRUE.equals(definition.getEverPublished());
	}

	/**
	 * Checks whether the entity is used by any current definition.
	 * @param entity Entity.
	 * @return If the entity is used by any current definition.
	 */
	protected boolean isUsed(T entity) {
		return true;
		// TODO make it abstract
	}

	/**
	 * Deletes a publishable entity.
	 * @param entity Entity.
	 */
	protected final void delete(T entity) {
		if (entity.isDeleted()) {
			return;
		}
		if (isUsed(entity)) {
			return;
			// TODO throw an error.
		}
		StateSupport.check(entity);
		if (Boolean.TRUE.equals(entity.getEverPublished())) {
			entity.setDeleted(true);
			getDao().update(entity);
		} else {
			entity.setCurrent(null);
			getDao().update(entity);
			getDao().flush();
			for (D dfn : entity.getDefinitions()) {
				getDao().delete(dfn);
			}
			getDao().delete(entity);
		}
	}

	protected final EnvironmentEntity touchOffline() throws PMSException {
		final EnvironmentEntity e = getEnvironment();
		e.touchOfflineVersion(loadUser());
		return e;
	}

	protected final EnvironmentEntity touchContentType() throws PMSException {
		final EnvironmentEntity e = getEnvironment();
		e.touchContentTypeVersion(loadUser());
		return e;
	}

	protected final EnvironmentEntity touchCategory() throws PMSException {
		final EnvironmentEntity e = getEnvironment();
		e.touchCategoryVersion(loadUser());
		return e;
	}

	protected final EnvironmentEntity touchConnector() throws PMSException {
		final EnvironmentEntity e = getEnvironment();
		e.touchConnectorVersion(loadUser());
		return e;
	}

	protected final Function<T, D> currentDfn() {
		return new Function<T, D>() {
			public D apply(T from) {
				return from.getCurrent();
			};
		};
	}
}
