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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.springframework.util.StringUtils.hasText;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.modules.ConnectorProvision;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.esvc.ExternalService;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceNotFoundException;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;
import com.isotrol.impe3.pms.core.obj.ConnectorObject;
import com.isotrol.impe3.pms.core.obj.Provider;


/**
 * Abstract base class for external services.
 * @author Andres Rodriguez.
 * @param <T> External service type.
 */
public abstract class AbstractExternalService<T> extends AbstractContextService implements ExternalService {
	private final Class<T> type;
	private EngineModelLoader engineLoader;

	/**
	 * Default constructor.
	 * @param type External service type.
	 */
	public AbstractExternalService(Class<T> type) {
		this.type = checkNotNull(type);
	}

	@Autowired
	public final void setEngineLoader(EngineModelLoader engineLoader) {
		this.engineLoader = engineLoader;
	}

	private Provider getProvider(String id) {
		final String[] parts = id.split(":");
		checkArgument(parts.length == 2);
		checkArgument(hasText(parts[0]) && hasText(parts[1]));
		final UUID uuid = UUID.fromString(parts[0]);
		return Provider.of(uuid, parts[1]);
	}

	private Service get(String id) throws PMSException {
		try {
			final Provider p = getProvider(id);
			final ConnectorObject c = loadContextGlobal().loadConnector(p.getConnectorId());
			final ConnectorProvision cp = checkNotNull(c.getModule().getConnectorProvisions().get(p.getBean()));
			checkArgument(type.isAssignableFrom(cp.getType()));
			return new Service(c, p);
		} catch (NullPointerException e) {
			throw new ExternalServiceNotFoundException(id);
		} catch (IllegalArgumentException e) {
			throw new ExternalServiceNotFoundException(id);
		}
	}

	protected final T getExternalService(String id) throws PMSException {
		final Service s = get(id);
		final EngineModel model = engineLoader.getOffline(getEnvironment().getName());
		final StartedModule<?> module = model.getConnector(s.connector.getId());
		return type.cast(module.getProvision(s.bean));
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.api.esvc.ExternalService#getService(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public final ExternalServiceDTO getService(String id) throws PMSException {
		final ConnectorObject c = get(id).connector;
		final ExternalServiceDTO dto = new ExternalServiceDTO();
		dto.setId(id);
		c.fill(dto);
		return dto;
	}

	private static final class Service {
		private final ConnectorObject connector;
		private final String bean;

		Service(ConnectorObject connector, Provider p) {
			this.connector = connector;
			this.bean = p.getBean();
		}
	}
}
