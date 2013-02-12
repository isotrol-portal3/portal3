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

import java.util.Locale;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.DependencyValue;


/**
 * A possible connector provider.
 * @author Andres Rodriguez.
 */
public final class Provider {
	static interface Mapper extends Function<Provider, ProviderDTO> {
	}

	static final Function<DependencyValue, Provider> FROM_DEPENDENCY_VALUE = new Function<DependencyValue, Provider>() {
		public Provider apply(DependencyValue from) {
			if (from == null) {
				return null;
			}
			return of(from.getConnector().getId(), from.getBean());
		}
	};

	static final Function<Provider, UUID> CONNECTOR_ID = new Function<Provider, UUID>() {
		public UUID apply(Provider from) {
			return from.connectorId;
		}
	};

	static final Function<Provider, String> BEAN = new Function<Provider, String>() {
		public String apply(Provider from) {
			return from.bean;
		}
	};

	static final Mapper mapper(final ConnectorsObject cnns, final ModuleRegistry registry, final Locale locale) {
		return new Mapper() {
			public ProviderDTO apply(Provider from) {
				return from.toSelDTO(cnns, registry, locale);
			}
		};
	}

	/**
	 * Builds a provider object.
	 * @param connectorId Connector Id.
	 * @param bean Exported bean name.
	 * @return The requested object or {@code null} if any of the arguments is null.
	 */
	public static Provider of(UUID connectorId, String bean) {
		if (connectorId == null || bean == null) {
			return null;
		}
		return new Provider(connectorId, bean);
	}

	/**
	 * Builds a provider object.
	 * @param connector Connector Entity.
	 * @param bean Exported bean name.
	 * @return The requested object or {@code null} if any of the arguments is null.
	 */
	public static Provider of(ConnectorEntity connector, String bean) {
		if (connector == null) {
			return null;
		}
		return of(connector.getId(), bean);
	}

	/**
	 * Builds a provider object.
	 * @param connector Connector Object.
	 * @param bean Exported bean name.
	 * @return The requested object or {@code null} if any of the arguments is null.
	 */
	public static Provider of(ConnectorObject connector, String bean) {
		if (connector == null) {
			return null;
		}
		return of(connector.getId(), bean);
	}

	private final UUID connectorId;
	private final String bean;

	/**
	 * Default constructor.
	 * @param connectorId Connector Id.
	 * @param bean Exported bean name.
	 */
	private Provider(final UUID connectorId, final String bean) {
		this.connectorId = checkNotNull(connectorId);
		this.bean = checkNotNull(bean);
	}

	public String getBean() {
		return bean;
	}

	public UUID getConnectorId() {
		return connectorId;
	}

	ProviderDTO toSelDTO(ConnectorsObject cnns, ModuleRegistry registry, Locale locale) {
		final ProviderDTO dto = new ProviderDTO();
		dto.setBean(bean);
		dto.setCurrent(cnns.get(connectorId).toSelDTO(registry, locale));
		dto.setDescription("");
		return dto;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Provider) {
			final Provider p = (Provider) obj;
			return Objects.equal(bean, p.bean) && Objects.equal(connectorId, p.connectorId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(bean, connectorId);
	}
}
