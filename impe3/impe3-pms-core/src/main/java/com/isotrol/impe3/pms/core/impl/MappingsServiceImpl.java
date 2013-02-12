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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.isotrol.impe3.mappings.MappingDTO;
import com.isotrol.impe3.mappings.MappingsDTO;
import com.isotrol.impe3.mappings.MappingsService;
import com.isotrol.impe3.pms.core.SourceMappingManager;
import com.isotrol.impe3.pms.core.dao.EnvironmentDAO;
import com.isotrol.impe3.pms.core.obj.MappingObject;
import com.isotrol.impe3.pms.core.obj.SourceMappingObject;
import com.isotrol.impe3.pms.core.obj.SourceMappingsObject;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Implementation of MappingsService.
 * @author Andres Rodriguez.
 */
@Service("mappingsService")
public final class MappingsServiceImpl extends AbstractService implements MappingsService {
	private EnvironmentDAO environmentDAO;
	private SourceMappingManager sourceMappingManager;

	private static final Function<MappingObject, MappingDTO> TO_DTO = new Function<MappingObject, MappingDTO>() {
		public MappingDTO apply(MappingObject from) {
			final MappingDTO dto = new MappingDTO();
			dto.setId(from.getId().toString());
			dto.setMapping(from.getMapping());
			return dto;
		}
	};

	/** Default constructor. */
	public MappingsServiceImpl() {
	}

	@Autowired
	public void setEnvironmentDAO(EnvironmentDAO environmentDAO) {
		this.environmentDAO = environmentDAO;
	}

	@Autowired
	public void setSourceMappingManager(SourceMappingManager sourceMappingManager) {
		this.sourceMappingManager = sourceMappingManager;
	}

	private SourceMappingObject getMapping(String environment, String source) {
		if (environment == null || source == null) {
			return null;
		}
		final EnvironmentEntity e = environmentDAO.getByName(environment);
		if (e == null) {
			return null;
		}
		final SourceMappingsObject smo = sourceMappingManager.loadOffline(e.getId());
		return smo.getByName(source);
	}

	/**
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappings(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public MappingsDTO getMappings(String environment, String source) {
		final SourceMappingObject sm = getMapping(environment, source);
		if (sm == null) {
			return null;
		}
		final MappingsDTO dto = new MappingsDTO();
		dto.setVersion(sm.getVersion());
		dto.setContentTypes(list(sm.getContentTypes(), TO_DTO));
		dto.setCategories(list(sm.getCategories(), TO_DTO));
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappingsIfModified(java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public MappingsDTO getMappingsIfModified(String environment, String source, int version) {
		final SourceMappingObject sm = getMapping(environment, source);
		if (sm == null) {
			return null;
		}
		final int v = sm.getVersion();
		final MappingsDTO dto = new MappingsDTO();
		dto.setVersion(v);
		if (v != version) {
			dto.setContentTypes(list(sm.getContentTypes(), TO_DTO));
			dto.setCategories(list(sm.getCategories(), TO_DTO));
		}
		return dto;
	}
}
