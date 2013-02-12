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


import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.isotrol.impe3.core.component.ComponentType;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.model.CIPValue;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.ComponentEntity;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Utility functions for a set (or subset) of a portal components.
 * @author Andres Rodriguez.
 */
public final class PortalComponents implements Iterable<ComponentDfn> {
	public static ComponentRef SPACE = new ComponentRef(null, null);

	private static final Function<ComponentDfn, UUID> DFN2ID = new Function<ComponentDfn, UUID>() {
		public UUID apply(ComponentDfn from) {
			return from.getInstanceId();
		}
	};

	public static PortalComponents of(Iterable<ComponentDfn> components) {
		return new PortalComponents(components);
	}

	public static PortalComponents editable(PortalEntity portal) {
		return of(portal.getCurrent().getComponents());
	}

	public static PortalComponents palette(PortalEntity portal) {
		return of(portal.getCurrent().getComponents());
		// TODO
	}

	private final Map<UUID, ComponentDfn> map;

	/**
	 * Constructor
	 * @param components Component definitions.
	 */
	private PortalComponents(Iterable<ComponentDfn> components) {
		map = Maps.uniqueIndex(components, DFN2ID);
	}

	public ComponentDfn load(UUID id) throws PMSException {
		final ComponentDfn dfn = map.get(id);
		if (dfn == null) {
			throw NotFoundProviders.COMPONENT.getNotFoundException(id);
		}
		return dfn;
	}

	public ComponentDfn load(String id) throws PMSException {
		return load(NotFoundProviders.COMPONENT.toUUID(id));
	}

	public Iterator<ComponentDfn> iterator() {
		return map.values().iterator();
	}

	public ComponentRef loadRef(ComponentDfn dfn, String bean) throws PMSException {
		final ComponentProvision p = dfn.getModuleDefinition().getComponentProvisions().get(bean);
		NotFoundProviders.COMPONENT.checkNotNull(p, dfn.getInstanceId());
		return new ComponentRef(dfn, p);
	}

	public ComponentRef loadRef(UUID id, String bean) throws PMSException {
		return loadRef(load(id), bean);
	}

	public ComponentRef loadRef(String id, String bean) throws PMSException {
		return loadRef(load(id), bean);
	}

	public ComponentRef loadRef(ComponentKey key) throws PMSException {
		if (key == null) {
			return SPACE;
		}
		return loadRef(key.getInstanceId(), key.getBean());
	}

	public ComponentRef loadRef(CIPValue v) throws PMSException {
		Preconditions.checkNotNull(v);
		if (v.isSpace()) {
			return SPACE;
		}
		return loadRef(load(v.getInstanceId()), v.getBean());
	}

	public static final class ComponentRef {
		private final ComponentDfn dfn;
		private final ComponentProvision provision;

		private ComponentRef(final ComponentDfn dfn, final ComponentProvision provision) {
			Preconditions.checkArgument((dfn == null && provision == null) || (dfn != null && provision != null));
			this.dfn = dfn;
			this.provision = provision;
		}

		public ComponentDfn getDfn() {
			return dfn;
		}

		public ComponentEntity getComponent() {
			return dfn != null ? dfn.getComponent() : null;
		}

		public UUID getInstanceId() {
			return dfn != null ? dfn.getInstanceId() : null;
		}

		public String getBean() {
			return provision != null ? provision.getBeanName() : null;
		}

		public ComponentProvision getProvision() {
			return provision;
		}

		public ComponentKey getKey() {
			if (isSpace()) {
				return null;
			}
			final ComponentKey key = new ComponentKey();
			key.setInstanceId(dfn.getInstanceId().toString().toLowerCase());
			key.setBean(provision.getBeanName());
			return key;
		}

		public ConfigurationDefinition<?> getConfiguration() {
			if (isSpace()) {
				return null;
			}
			return provision.getComponent().getConfiguration();
		}

		public boolean isSpace() {
			return dfn == null;
		}

		public boolean isVisual() {
			return isSpace() || (provision.getComponent().getComponentType() == ComponentType.VISUAL);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(getInstanceId(), getBean());
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ComponentRef) {
				final ComponentRef c = (ComponentRef) obj;
				return Objects.equal(getInstanceId(), c.getInstanceId()) && Objects.equal(getBean(), c.getBean());
			}
			return false;
		}
	}
}
