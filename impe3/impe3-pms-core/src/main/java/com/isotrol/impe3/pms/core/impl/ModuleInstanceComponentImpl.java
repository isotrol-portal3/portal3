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
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.pbuf.BaseProtos.DependencyPB;
import com.isotrol.impe3.pbuf.BaseProtos.ModulePB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.ModuleInstanceComponent;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.ModuleObject;
import com.isotrol.impe3.pms.core.obj.Provider;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.Lengths;
import com.isotrol.impe3.pms.model.RequiredDependencyValue;
import com.isotrol.impe3.pms.model.WithModuleDfn;


/**
 * Module instance helper component.
 * @author Andres Rodriguez.
 */
@Component
public class ModuleInstanceComponentImpl extends AbstractContextService implements ModuleInstanceComponent {
	/** Configuration manager. */
	private final ConfigurationManager configurationManager;
	/** Module registry. */
	private final ModuleRegistry registry;

	/**
	 * Default constructor.
	 * @param configurationManager Configuration Manager.
	 * @param graphProvider Graph provider.
	 */
	@Autowired
	public ModuleInstanceComponentImpl(final ConfigurationManager configurationManager, final ModuleRegistry registry) {
		this.configurationManager = configurationManager;
		this.registry = checkNotNull(registry);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleInstanceComponent#provider2value(java.util.Map)
	 */
	public Map<String, RequiredDependencyValue> provider2value(Map<String, Provider> providers) throws PMSException {
		final Map<String, RequiredDependencyValue> dvs = Maps.newHashMap();
		for (Entry<String, Provider> entry : providers.entrySet()) {
			final Provider p = entry.getValue();
			final RequiredDependencyValue v = new RequiredDependencyValue();
			v.setBean(p.getBean());
			v.setConnector(loadConnectorEntity(p.getConnectorId()));
			dvs.put(entry.getKey(), v);
		}
		return dvs;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleInstanceComponent#dfn2sel(com.isotrol.impe3.pms.model.WithModuleDfn,
	 * com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO)
	 */
	public void dfn2sel(WithModuleDfn dfn, ModuleInstanceSelDTO dto) {
		dto.setId(dfn.getInstanceId().toString());
		dto.setModule(registry.getModuleSel(dfn.getModuleDefinition(), getLocale()));
		dto.setName(dfn.getName());
		dto.setDescription(dfn.getDescription());
	}

	private void dto2dfn(ModuleInstanceDTO dto, WithModuleDfn dfn) {
		dfn.setModuleClass(dto.getKey());
		String name = dto.getName();
		if (name != null && name.length() > Lengths.NAME) {
			name = name.substring(0, Lengths.NAME);
		}
		dfn.setName(dto.getName());
		dfn.setDescription(dto.getDescription());
	}

	private void pb2dfn(ModulePB pb, WithModuleDfn dfn) {
		dfn.setModuleClass(pb.getModuleClass());
		String name = pb.getName();
		if (name != null && name.length() > Lengths.NAME) {
			name = name.substring(0, Lengths.NAME);
		}
		dfn.setName(pb.getName());
		dfn.setDescription(pb.getDescription());
	}

	private ModuleDefinition<?> getModuleDefinition(String className, ModuleType moduleType) throws PMSException {
		Preconditions.checkNotNull(className);
		final ModuleDefinition<?> md = ModuleDefinition.of(className);
		Preconditions.checkArgument(md.getModuleType() == moduleType);
		return md; // TODO: exceptions.
	}

	private void saveConfiguration(ModuleDefinition<?> md, ModuleInstanceDTO dto, WithModuleDfn dfn, boolean isNew)
		throws PMSException {
		final List<ConfigurationItemDTO> items = dto.getConfiguration();
		ConfigurationEntity config = dfn.getConfiguration();
		if (items == null) {
			if (md.isConfigurationRequired()) {
				throw new IllegalStateException(); // TODO
			} else if (config != null) {
				dfn.setConfiguration(null);
				configurationManager.delete(config);
			}
		} else {
			final ConfigurationDefinition<?> cd = md.getConfiguration();
			if (cd == null) {
				return;
			}
			if (isNew || config == null) {
				config = configurationManager.create(cd, items);
				dfn.setConfiguration(config);
			} else {
				configurationManager.update(cd, config, items);
			}
		}
	}

	private void saveConfiguration(ModuleDefinition<?> md, ModulePB pb, WithModuleDfn dfn, boolean isNew)
		throws PMSException {
		ConfigurationEntity config = dfn.getConfiguration();
		if (!pb.hasConfiguration() || pb.getConfiguration().getConfigurationValuesCount() == 0) {
			if (md.isConfigurationRequired()) {
				throw new IllegalStateException(); // TODO
			} else if (config != null) {
				dfn.setConfiguration(null);
				configurationManager.delete(config);
			}
		} else {
			final ConfigurationDefinition<?> cd = md.getConfiguration();
			if (cd == null) {
				return;
			}
			if (isNew || config == null) {
				config = configurationManager.create(cd, pb.getConfiguration());
				dfn.setConfiguration(config);
			} else {
				configurationManager.update(cd, config, pb.getConfiguration());
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleInstanceComponent#fill(com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO,
	 * com.isotrol.impe3.pms.model.WithModuleDfn, boolean)
	 */
	public void fill(ModuleInstanceDTO dto, WithModuleDfn dfn, boolean isNew) throws PMSException {
		checkNotNull(dto);
		checkNotNull(dto.getKey());
		checkNotNull(dfn);
		final ModuleType moduleType = (dfn instanceof ConnectorDfn) ? ModuleType.CONNECTOR : ModuleType.COMPONENT;
		// 1 - Load module.
		final ModuleDefinition<?> md = getModuleDefinition(dto.getKey(), moduleType);
		// 2 - Set scalar fields
		dto2dfn(dto, dfn);
		// 3 - Manage configuration
		saveConfiguration(md, dto, dfn, isNew);
		// 4 - Dependencies.
		final ContextGlobal ctx = loadContextGlobal();
		final ModuleObject mi;
		if (isNew || moduleType == ModuleType.COMPONENT) {
			mi = null;
		} else {
			mi = ctx.loadConnector(dfn.getInstanceId());
		}
		final Map<String, RequiredDependencyValue> dependencies = dfn.getDependencies();
		dependencies.clear();
		dependencies.putAll(provider2value(ctx.validateDependencies(md, mi, dto.getDependencies())));
		// 5 - Done!!
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleInstanceComponent#fill(com.isotrol.impe3.pbuf.BaseProtos.ModulePB,
	 * com.isotrol.impe3.pms.model.WithModuleDfn, boolean)
	 */
	public void fill(ModulePB pb, WithModuleDfn dfn, boolean isNew) throws PMSException {
		final ModuleType moduleType = (dfn instanceof ConnectorDfn) ? ModuleType.CONNECTOR : ModuleType.COMPONENT;
		// 1 - Load module.
		final ModuleDefinition<?> md = getModuleDefinition(pb.getModuleClass(), moduleType);
		// 2 - Set scalar fields
		pb2dfn(pb, dfn);
		// 3 - Manage configuration
		saveConfiguration(md, pb, dfn, isNew);
		// 4 - Dependencies.
		final ContextGlobal ctx = loadContextGlobal();
		final ModuleObject mi;
		if (isNew || moduleType == ModuleType.COMPONENT) {
			mi = null;
		} else {
			mi = ctx.loadConnector(dfn.getInstanceId());
		}
		final Map<String, RequiredDependencyValue> dependencies = dfn.getDependencies();
		dependencies.clear();
		List<DependencyDTO> deps = newArrayList(transform(pb.getDependenciesList(), PB2Dep.INSTANCE));
		dependencies.putAll(provider2value(ctx.validateDependencies(md, mi, deps)));
		// 5 - Done!!
	}

	private enum PB2Dep implements Function<DependencyPB, DependencyDTO> {
		INSTANCE;

		public DependencyDTO apply(DependencyPB from) {
			DependencyDTO dto = new DependencyDTO();
			dto.setName(from.getName());
			dto.setConnectorId(from.getProvision().getInstanceId());
			dto.setBean(from.getProvision().getBean());
			return dto;
		}
	}
}
