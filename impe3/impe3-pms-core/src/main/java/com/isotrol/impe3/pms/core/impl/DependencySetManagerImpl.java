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


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.core.DependencySetManager;
import com.isotrol.impe3.pms.core.ModuleInstanceComponent;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.ModuleObject;
import com.isotrol.impe3.pms.model.DependencySetEntity;
import com.isotrol.impe3.pms.model.RequiredDependencyValue;


/**
 * Implementation of DependencySetManager.
 * @author Andres Rodriguez.
 */
@Service("dependencySetManager")
public final class DependencySetManagerImpl extends AbstractEntityService<DependencySetEntity> implements
	DependencySetManager {

	@Autowired
	private ModuleInstanceComponent moduleInstanceComponent;

	/** Default constructor. */
	public DependencySetManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.DependencySetManager#save(java.util.UUID,
	 * com.isotrol.impe3.core.modules.ModuleDefinition, java.util.List, com.isotrol.impe3.pms.model.DependencySetEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public DependencySetEntity save(UUID instanceId, ModuleDefinition<?> md, List<DependencyDTO> dependencies,
		DependencySetEntity entity) throws PMSException {
		if (entity == null) {
			entity = new DependencySetEntity();
			saveNew(entity);
		}
		final ContextGlobal ctx = loadContextGlobal();
		final ModuleObject mi = instanceId == null ? null : ctx.loadConnector(instanceId);
		final Map<String, RequiredDependencyValue> dvs = entity.getDependencies();
		dvs.clear();
		dvs.putAll(moduleInstanceComponent.provider2value(ctx.validateDependencies(md, mi, dependencies)));
		return entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.impl.AbstractEntityService#delete(com.isotrol.impe3.pms.model.Entity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(DependencySetEntity entity) {
		super.delete(entity);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.core.DependencySetManager#duplicate(com.isotrol.impe3.pms.model.DependencySetEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public DependencySetEntity duplicate(DependencySetEntity entity) throws PMSException {
		if (entity == null) {
			return null;
		}
		final DependencySetEntity dup = new DependencySetEntity();
		final Map<String, RequiredDependencyValue> dvs = dup.getDependencies();
		for (Entry<String, RequiredDependencyValue> entry : entity.getDependencies().entrySet()) {
			dvs.put(entry.getKey(), entry.getValue().clone());
		}
		saveNew(dup);
		return dup;
	}
}
