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


import static com.google.common.collect.Iterables.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.pms.core.ConnectorManager;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.obj.ConnectorsObject;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.ConnectorEdition;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Connector manager implementation.
 * @author Andres Rodriguez.
 */
@Component
public final class ConnectorManagerImpl extends AbstractStateLoaderComponent<ConnectorsObject> implements
	ConnectorManager {
	private static final Function<ConnectorEdition, ConnectorDfn> CONNECTOR = new Function<ConnectorEdition, ConnectorDfn>() {
		public ConnectorDfn apply(ConnectorEdition from) {
			return from.getPublished();
		};
	};

	/** Loader. */
	private final ConnectorsTypesLoader loader;

	/** Default constructor. */
	@Autowired
	public ConnectorManagerImpl(final ModuleRegistry registry) {
		this.loader = new ConnectorsTypesLoader(registry);
	}

	@Override
	ConnectorsTypesLoader getLoader() {
		return loader;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getConnectorVersion();
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.CONNECTOR;
	}

	private class ConnectorsTypesLoader implements Loader<ConnectorsObject> {
		private final ModuleRegistry registry;

		ConnectorsTypesLoader(ModuleRegistry registry) {
			this.registry = registry;
		}

		public ConnectorsObject load(EnvironmentEntity e) {
			return ConnectorsObject.current(registry, getDao().getOfflineConnectors(e.getId()));
		}

		public ConnectorsObject load(EditionEntity e) {
			return ConnectorsObject.definitions(registry, transform(e.getConnectors(), CONNECTOR));
		}

		@Override
		public String toString() {
			return "Connectors";
		}
	}

}
