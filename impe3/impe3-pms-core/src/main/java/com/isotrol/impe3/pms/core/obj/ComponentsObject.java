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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.obj.ComponentObject.map2ipb;
import static com.isotrol.impe3.pms.core.obj.ComponentObject.map2pb;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.COMPONENT;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.core.support.IdentifiableMaps;
import com.isotrol.impe3.pbuf.portal.PortalProtos.ComponentsPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.InheritedComponentsPB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.obj.ComponentObject.Inherited;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.ComponentEntity;
import com.isotrol.impe3.pms.model.OverridenComponentValue;
import com.isotrol.impe3.pms.model.PortalDfn;


/**
 * Collection of components domain object.
 * @author Andres Rodriguez
 */
public final class ComponentsObject extends ModulesObject<ComponentObject> {
	private static final Function<ComponentDfn, ComponentObject> DFN2OBJ = new Function<ComponentDfn, ComponentObject>() {
		public ComponentObject apply(ComponentDfn from) {
			return ComponentObject.of(from);
		}
	};

	/** Portal Id. */
	private final UUID portalId;
	/** All components map. */
	private final ImmutableMap<UUID, ComponentObject> all;
	/** Owned components map. */
	private final ImmutableMap<UUID, ComponentObject> owned;
	/** Inherited components map. */
	private final ImmutableMap<UUID, Inherited> inherited;
	/** Palette. */
	private final ImmutableMap<PaletteKey, PaletteItem> palette;

	/**
	 * Builds a collection from a portal definition.
	 * @param dfn Portal definition.
	 * @param parent Parent components.
	 * @return The requested collection.
	 */
	public static ComponentsObject of(PortalDfn dfn, ComponentsObject parent) {
		return new ComponentsObject(checkNotNull(dfn), parent);
	}

	/**
	 * Constructor.
	 * @param dfn Portal definition.
	 * @param parent Parent components.
	 */
	private ComponentsObject(PortalDfn dfn, ComponentsObject parent) {
		this.portalId = dfn.getEntity().getId();
		this.owned = IdentifiableMaps.immutableOf(transform(dfn.getComponents(), DFN2OBJ));
		if (parent == null || parent.isEmpty()) {
			this.all = owned;
			this.inherited = ImmutableMap.of();
		} else {
			Map<UUID, OverridenComponentValue> map = Maps.newHashMap();
			for (Entry<ComponentEntity, OverridenComponentValue> e : dfn.getOverridenComponents().entrySet()) {
				map.put(e.getKey().getId(), e.getValue());
			}
			ImmutableMap.Builder<UUID, Inherited> ib = ImmutableMap.builder();
			for (Entry<UUID, ComponentObject> e : parent.entrySet()) {
				final UUID id = e.getKey();
				
				Inherited inh = e.getValue().override(map.get(id), dfn);
				//inh.getPortalConfiguration()
				ib.put(id, inh);
			}
			this.inherited = ib.build();
			Map<UUID, ComponentObject> ab = Maps.newHashMap();
			ab.putAll(this.inherited);
			ab.putAll(this.owned);
			this.all = ImmutableMap.copyOf(ab);
		}
		ImmutableMap.Builder<PaletteKey, PaletteItem> builder = ImmutableMap.builder();
		for (ComponentObject c : all.values()) {
			for (PaletteItem p : c.getPalette()) {
				builder.put(p.getKey(), p);
			}
		}
		this.palette = builder.build();
	}

	@Override
	protected Map<UUID, ComponentObject> delegate() {
		return all;
	}

	public UUID getPortalId() {
		return portalId;
	}

	List<ModuleInstanceSelDTO> getOwnedSel(Context2 context) {
		return map2sel(context.getRegistry(), context.getLocale(), owned.values());
	}

	List<ModuleInstanceSelDTO> getOwnedSel(Context2 context, Correctness correctness) {
		return map2sel(context.getRegistry(), context.getLocale(),
			filter(owned.values(), compose(equalTo(correctness), ComponentObject.CORRECTNESS)));
	}

	List<InheritedComponentInstanceSelDTO> getInheritedSel(final Context2 context) {
		final Function<Inherited, InheritedComponentInstanceSelDTO> f = new Function<Inherited, InheritedComponentInstanceSelDTO>() {
			public InheritedComponentInstanceSelDTO apply(Inherited from) {
				return from.toInheritedDTO(context);
			};
		};
		return Mappers.list(inherited.values(), f);
	}

	Iterable<PaletteItem> getPalette() {
		return palette.values();
	}

	final PaletteItem getPaletteItem(PaletteKey key) throws PMSException {
		COMPONENT.checkNotNull(key, (UUID) null);
		COMPONENT.checkCondition(palette.containsKey(key), (UUID) null);
		return palette.get(key);
	}

	public boolean isOwned(UUID id) {
		return owned.containsKey(id);
	}

	public boolean isOwned(String id) throws PMSException {
		return isOwned(COMPONENT.toUUID(id));
	}

	public boolean isInherited(UUID id) {
		return inherited.containsKey(id);
	}

	public boolean isInherited(String id) throws PMSException {
		return isInherited(COMPONENT.toUUID(id));
	}

	public ComponentObject loadOwned(UUID id) throws EntityNotFoundException {
		return COMPONENT.checkNotNull(owned.get(id), id);
	}

	public ComponentObject loadOwned(String id) throws EntityNotFoundException {
		return loadOwned(COMPONENT.toUUID(id));
	}

	/**
	 * Exports the owned components.
	 * @param f Filter to apply
	 * @return The PB message.
	 */
	public final ComponentsPB exportOwned(FileManager fileManager, Predicate<? super ComponentObject> f) {
		ComponentsPB.Builder b = ComponentsPB.newBuilder();
		b.addAllComponents(transform(filter(owned.values(), f), map2pb(fileManager)));
		return b.build();
	}

	/**
	 * Exports the owned components.
	 * @return The PB message.
	 */
	public final ComponentsPB exportOwned(FileManager fileManager) {
		return exportOwned(fileManager, alwaysTrue());
	}

	/**
	 * Exports the overriden components.
	 * @param f Filter to apply.
	 * @return The PB message.
	 */
	public final InheritedComponentsPB exportOverriden(FileManager fileManager, Predicate<? super ComponentObject> f) {
		return InheritedComponentsPB.newBuilder()
			.addAllComponents(filter(transform(filter(inherited.values(), f), map2ipb(fileManager)), notNull()))
			.build();
	}

	/**
	 * Exports the overriden components.
	 * @return The PB message.
	 */
	public final InheritedComponentsPB exportOverriden(FileManager fileManager) {
		return exportOverriden(fileManager, alwaysTrue());
	}

	public Inherited loadInherited(UUID id) throws EntityNotFoundException {
		return COMPONENT.checkNotNull(inherited.get(id), id);
	}

	public Inherited loadInherited(String id) throws EntityNotFoundException {
		return loadInherited(COMPONENT.toUUID(id));
	}

	/**
	 * Starts the components collection.
	 * @param model Model to apply.
	 * @param connectors Connectors to use.
	 * @urig URI Generator to use.
	 * @return The started connectors.
	 */
	public StartedComponents start(BaseModel model, Function<UUID, StartedModule<?>> connectors, URIGenerator urig) {
		Map<UUID, StartedModule<?>> started = Maps.newHashMap();
		List<StartedModule<?>> connectorStop = Lists.newLinkedList();
		for (ComponentObject c : values()) {
			final StartedModule<?> sm = c.starter(model, connectors).set(URIGenerator.class, urig).start(null);
			started.put(c.getId(), sm);
			connectorStop.add(0, sm);
		}
		return new StartedComponents(started);
	}

}
