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


import static com.isotrol.impe3.pms.core.support.Mappers.list;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.EDITION;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.edition.EditionDTO;
import com.isotrol.impe3.pms.api.edition.EditionsService;
import com.isotrol.impe3.pms.api.edition.ModifiedGlobalsException;
import com.isotrol.impe3.pms.api.edition.ModifiedParentPortalsException;
import com.isotrol.impe3.pms.api.user.DoneDTO;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.obj.PortalsObject;
import com.isotrol.impe3.pms.core.obj.PublishableObject;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.CategoryEdition;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConnectorEdition;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ContentTypeEdition;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.Definition;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEdition;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.PublishableEntity;


/**
 * Implementation of EditionsService.
 * @author Andres Rodriguez.
 */
@Service("editionsService")
public final class EditionsServiceImpl extends AbstractOfEnvironmentService<EditionEntity> implements EditionsService {
	private static Predicate<State> NEEDS_PUBLISHING = new Predicate<State>() {
		public boolean apply(State input) {
			return input == State.NEW || input == State.MODIFIED;
		}
	};

	private static Function<EditionDTO, Date> LAST_PUBLISHED = new Function<EditionDTO, Date>() {
		public Date apply(EditionDTO from) {
			return from.getLastPublished().getTimestamp();
		}
	};

	private static Ordering<EditionDTO> BY_LAST_PUBLISHED = Ordering.natural().onResultOf(LAST_PUBLISHED).reverse();

	/** Default constructor. */
	public EditionsServiceImpl() {
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.EDITION_GET)
	public List<EditionDTO> getLastEditions() throws PMSException {
		final EnvironmentEntity env = getEnvironment();
		final UUID current = env.getCurrentId();
		final Function<EditionEntity, EditionDTO> f = new Function<EditionEntity, EditionDTO>() {
			public EditionDTO apply(EditionEntity from) {
				final EditionDTO dto = new EditionDTO();
				dto.setId(from.getId().toString().toLowerCase());
				final DoneDTO created = Mappers.done2dto(from.getCreated());
				final DoneDTO published = Mappers.done2dto(from.getLastPublished());
				dto.setCreated(created);
				dto.setLastPublished(published != null ? published : created);
				dto.setCurrent(Objects.equal(current, from.getId()));
				return dto;
			}
		};
		List<EditionDTO> editions = list(getDao().getLastestEditions(env.getId(), 1000000), f);
		Collections.sort(editions, BY_LAST_PUBLISHED);
		return editions;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.EDITION_PUBLISH)
	public void publish() throws PMSException {
		final ContextGlobal ctx = loadContextGlobal();
		// Do we need a new edition?
		if (!needs(ctx.getContentTypes().values()) && !needs(ctx.getCategories().values())
			&& !needs(ctx.getConnectors().values()) && !needs(ctx.getPortals().values())) {
			return;
		}
		createEdition(getEnvironment(), null);
	}

	private void createEdition(EnvironmentEntity env, Predicate<UUID> portals) throws PMSException {
		final EditionEntity edition = new EditionEntity();
		edition.setLastPublished(loadUser());
		saveNew(edition);
		// 1 - Content types
		for (ContentTypeEntity e : env.getOfflineContentTypes()) {
			final ContentTypeEdition edt = new ContentTypeEdition();
			edt.setEdition(edition);
			edt.setPublished(e.getCurrent());
			setPublishedFlag(e.getCurrent());
			saveNewEntity(edt);
		}
		// 2 - Categories
		for (CategoryEntity e : env.getOfflineCategories()) {
			final CategoryEdition edt = new CategoryEdition();
			edt.setEdition(edition);
			edt.setPublished(e.getCurrent());
			setPublishedFlag(e.getCurrent());
			saveNewEntity(edt);
		}
		// 3 - Connectors
		for (ConnectorEntity e : env.getOfflineConnectors()) {
			final ConnectorEdition edt = new ConnectorEdition();
			edt.setEdition(edition);
			edt.setPublished(e.getCurrent());
			setPublishedFlag(e.getCurrent());
			saveNewEntity(edt);
		}
		// 4 - Portals
		if (portals == null) {
			for (PortalEntity e : env.getOfflinePortals()) {
				newPortalEdition(edition, e.getCurrent());
			}
		} else {
			Map<UUID, PortalEdition> map = Maps.uniqueIndex(env.getCurrent().getPortals(), PortalEdition.ENTITY_ID);
			for (PortalEntity e : env.getOfflinePortals()) {
				final UUID id = e.getId();
				final PortalDfn dfn;
				if (portals.apply(id)) {
					dfn = e.getCurrent();
				} else if (map.containsKey(id)) {
					dfn = map.get(id).getPublished();
				} else {
					dfn = null;
				}
				if (dfn != null) {
					newPortalEdition(edition, dfn);
				}
			}
		}
		// We set the current edition
		env.setCurrent(edition);
	}

	/** Sets the ever published flag for a definition and entity. */
	private void setPublishedFlag(Definition<?, ?, ?> dfn) {
		if (!Boolean.TRUE.equals(dfn.getEverPublished())) {
			dfn.setEverPublished(true);
			PublishableEntity<?, ?, ?> entity = dfn.getEntity();
			if (!Boolean.TRUE.equals(entity.getEverPublished())) {
				entity.setEverPublished(true);
			}
		}
	}

	private void newPortalEdition(EditionEntity edition, PortalDfn dfn) throws PMSException {
		final PortalEdition edt = new PortalEdition();
		edt.setEdition(edition);
		edt.setPublished(dfn);
		setPublishedFlag(dfn);
		saveNewEntity(edt);
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.EDITION_REPUBLISH)
	public void setOnlineEdition(String editionId) throws PMSException {
		final EditionEntity edition = getDao().getEditionById(getEnvironmentId(), EDITION.toUUID(editionId));
		EDITION.checkNotNull(edition, editionId);
		final EnvironmentEntity env = getEnvironment();
		if (edition != env.getCurrent()) {
			edition.setLastPublished(loadUser());
			env.setCurrent(edition);
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.EDITION_PUBLISH, portal = PortalAuthority.PUBLISH)
	public boolean publishPortal(String portalId, boolean tryParents) throws PMSException {
		final EnvironmentEntity env = getEnvironment();
		final EditionEntity current = env.getCurrent();
		final ContextGlobal ctx = loadContextGlobal();
		final PortalsObject portals = loadContextGlobal().getPortals();
		// Check global elements
		if (current == null || needs(ctx.getContentTypes().values()) || needs(ctx.getCategories().values())
			|| needs(ctx.getConnectors().values())) {
			throw new ModifiedGlobalsException();
		}
		final PortalObject portal = portals.load(portalId);
		// Check parents
		final LinkedList<PortalObject> parents = Lists.newLinkedList();
		PortalObject parent = portals.getParent(portal.getId());
		while (parent != null) {
			parents.addFirst(parent);
			parent = portals.getParent(parent.getId());
		}
		final boolean anyParentModified = needs(parents);
		final boolean portalModified = NEEDS_PUBLISHING.apply(portal.getState());
		if (!anyParentModified) {
			if (!portalModified) {
				return false;
			}
		} else if (!tryParents) {
			throw new ModifiedParentPortalsException();
		}
		// Prepare the set of portals to export
		final Set<UUID> ids;
		if (!anyParentModified) {
			ids = ImmutableSet.of(portal.getId());
		} else {
			ids = Sets.newHashSet();
			for (PortalObject p : parents) {
				if (!ids.isEmpty() || NEEDS_PUBLISHING.apply(p.getState())) {
					ids.add(p.getId());
				}
			}
			ids.add(portal.getId());
		}
		// Create edition.
		createEdition(env, Predicates.in(ids));
		return true;
	}

	private boolean needs(Iterable<? extends PublishableObject> elements) {
		return Iterables.any(Iterables.transform(elements, PublishableObject.STATE), NEEDS_PUBLISHING);
	}
}
