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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.ComputationException;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceType;
import com.isotrol.impe3.pms.api.esvc.ExternalServicesService;
import com.isotrol.impe3.pms.api.esvc.IndexersService;
import com.isotrol.impe3.pms.core.obj.ConnectorObject;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.Provider;
import com.isotrol.impe3.users.api.PortalUsersService;
import com.isotrol.impe3.web20.api.CommentsService;


/**
 * Implementation of ExternalServicesService.
 * @author Andres Rodriguez.
 */
@Service("externalServicesService")
public final class ExternalServicesServiceImpl extends AbstractContextService implements ExternalServicesService {
	private static final Map<ExternalServiceType, Class<?>> TYPES;

	private static final Ordering<ExternalServiceDTO> BY_NAME = Ordering.natural().onResultOf(
		new Function<ExternalServiceDTO, String>() {
			public String apply(ExternalServiceDTO from) {
				return from.getName();
			}
		});

	static {
		final Map<ExternalServiceType, Class<?>> m = Maps.newEnumMap(ExternalServiceType.class);
		m.put(ExternalServiceType.USERS, PortalUsersService.class);
		m.put(ExternalServiceType.NODE_REPOSITORY, NodeRepository.class);
		m.put(ExternalServiceType.COMMENTS, CommentsService.class);
		m.put(ExternalServiceType.INDEXER_REPOSITORY,IndexersService.class);
		// TODO ZINEB --> Poner la interfaz del nuevo servicio... EL DEL CONECTOR
		//m.put(ExternalServiceType.INDEXER_REPOSITORY, restinde.class); (RESTINDEXERSERVICE.CLASS,TAMBIEN PONER LA DEPENDENCIA DEL PROYECTO DEL OTRO(CONECTOR CREADO))
		TYPES = Collections.unmodifiableMap(m);
	}

	/** Default constructor. */
	public ExternalServicesServiceImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.api.esvc.ExternalServicesService#getServices(com.isotrol.impe3.pms.api.esvc.ExternalServiceType)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<ExternalServiceDTO> getServices(ExternalServiceType type) throws PMSException {
		checkNotNull(type);
		final Class<?> klass = checkNotNull(TYPES.get(type));
		final ContextGlobal ctx = loadContextGlobal();
		final Set<Provider> providers = ctx.getPossibleProviders(klass);
		final Function<Provider, ExternalServiceDTO> f = new Function<Provider, ExternalServiceDTO>() {
			public ExternalServiceDTO apply(Provider from) {
				try {
					final UUID cnnId = from.getConnectorId();
					final ConnectorObject cnn = ctx.loadConnector(cnnId);
					final ExternalServiceDTO dto = new ExternalServiceDTO();
					dto.setId(cnnId.toString().toLowerCase() + ":" + from.getBean());
					cnn.fill(dto);
					return dto;
				} catch (PMSException e) {
					throw new ComputationException(e);
				}
			}
		};
		return BY_NAME.sortedCopy(Iterables.transform(providers, f));
	}

}
