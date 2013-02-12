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
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.RoutingBase;
import com.isotrol.impe3.api.RoutingDomain;
import com.isotrol.impe3.api.RoutingDomains;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.impl.AbstractIdentifiableMap;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.rd.RoutingDomainSelDTO;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.RoutingDomainEntity;


/**
 * Source mappings collection.
 * @author Andres Rodriguez
 */
public final class RoutingDomainsObject extends AbstractIdentifiableMap<RoutingDomainObject> {
	/** Default routing domain name. */
	public static final String DEFAULT = "DEFAULT";
	/** Default base URL. */
	public static final String BASE = "http://localhost:8080/impe3";

	private static final Function<RoutingDomainEntity, String> ENTITY_NAME = new Function<RoutingDomainEntity, String>() {
		public String apply(RoutingDomainEntity from) {
			return from.getName();
		}
	};

	private static final Function<RoutingDomainObject, String> OBJ_NAME = new Function<RoutingDomainObject, String>() {
		public String apply(RoutingDomainObject from) {
			return from.getName();
		}
	};

	private static final Predicate<RoutingDomainEntity> ENTITY_DEFAULT = compose(equalTo(DEFAULT), ENTITY_NAME);

	private static final Predicate<RoutingDomainObject> OBJ_DEFAULT = compose(equalTo(DEFAULT), OBJ_NAME);

	private static final Function<RoutingDomainObject, RoutingDomain> RD2API = new Function<RoutingDomainObject, RoutingDomain>() {
		public RoutingDomain apply(RoutingDomainObject from) {
			return from.getDomain();
		}
	};

	private static final Function<RoutingDomainObject, RoutingDomainSelDTO> RD2SEL = new Function<RoutingDomainObject, RoutingDomainSelDTO>() {
		public RoutingDomainSelDTO apply(RoutingDomainObject from) {
			return from.toSel();
		}
	};

	/** By Id. */
	private final ImmutableMap<UUID, RoutingDomainObject> byId;
	/** By name. */
	private final ImmutableSortedMap<String, RoutingDomainObject> byName;

	/**
	 * Builds a collection from a set of entities.
	 * @param domains Routing domains.
	 * @return The requested collection.
	 */
	public static RoutingDomainsObject of(Iterable<RoutingDomainEntity> domains) {
		try {
			final RoutingDomainEntity drd = Iterables.find(domains, ENTITY_DEFAULT);
			final RoutingBase offline = RoutingBase.of(drd.getOfflineBase(), drd.getOfflineAbsBase());
			return new RoutingDomainsObject(offline, domains);
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("No default routing domain");
		}
	}

	/**
	 * Constructor.
	 * @param offline Offline base.
	 * @param domains Routing domains.
	 */
	private RoutingDomainsObject(RoutingBase offline, Iterable<RoutingDomainEntity> domains) {
		final Map<UUID, RoutingDomainObject> byId = Maps.newHashMap();
		final Map<String, RoutingDomainObject> byName = Maps.newHashMap();
		for (RoutingDomainEntity domain : domains) {
			final UUID id = domain.getId();
			if (byId.containsKey(id)) {
				Loggers.pms().error(String.format("Duplicate routing domain with id [%s]. Skipping...", id));
			}
			final String name = domain.getName();
			if (byName.containsKey(name)) {
				Loggers.pms().error(String.format("Duplicate routing domain with name [%s]. Skipping...", name));
			}
			final RoutingDomain rd = RoutingDomain.builder().setId(id).setName(name)
				.setDescription(domain.getDescription()).setOffline(offline)
				.setOnline(RoutingBase.of(domain.getOnlineBase(), domain.getOnlineAbsBase())).get();
			final RoutingDomainObject rdo = new RoutingDomainObject(rd);
			byId.put(id, rdo);
			byName.put(name, rdo);
		}
		this.byId = ImmutableMap.copyOf(byId);
		this.byName = ImmutableSortedMap.copyOf(byName);
		checkArgument(byName.containsKey(DEFAULT), "No default routing domain provided");
	}

	@Override
	protected Map<UUID, RoutingDomainObject> delegate() {
		return byId;
	}

	public RoutingDomainObject loadNotDefault(String id) throws EntityNotFoundException {
		final UUID uuid = NotFoundProviders.DEFAULT.toUUID(id);
		final RoutingDomainObject rdo = NotFoundProviders.DEFAULT.checkNotNull(get(uuid), id);
		NotFoundProviders.DEFAULT.checkCondition(!DEFAULT.equals(rdo.getName()), id);
		return rdo;
	}

	public RoutingDomainObject loadDefault() {
		return getByName(DEFAULT);
	}

	/**
	 * Returns a source mapping by name.
	 * @param name Requested name.
	 * @return The source mapping with the requested name or {@code null} if not found.
	 */
	public RoutingDomainObject getByName(String name) {
		return byName.get(name);
	}

	public List<RoutingDomainSelDTO> map2sel(boolean includeDefault) {
		final Predicate<RoutingDomainObject> p;
		if (includeDefault) {
			p = Predicates.alwaysTrue();
		} else {
			p = Predicates.not(OBJ_DEFAULT);
		}
		return newArrayList(transform(Iterables.filter(byName.values(), p), RD2SEL));
	}
	
	public RoutingDomains start() {
		return RoutingDomains.builder().add(Iterables.transform(values(), RD2API)).get();
	}
}
