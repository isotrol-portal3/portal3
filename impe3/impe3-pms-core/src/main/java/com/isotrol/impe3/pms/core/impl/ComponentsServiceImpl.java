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
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newArrayList;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.COMPONENT;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.pbuf.BaseProtos.ModulePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.ComponentPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.ComponentsPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.InheritedComponentPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.InheritedComponentsPB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.component.ComponentsService;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencySetTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.core.ComponentManager;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.DependencySetManager;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.ModuleInstanceComponent;
import com.isotrol.impe3.pms.core.PortalManager;
import com.isotrol.impe3.pms.core.obj.ComponentObject;
import com.isotrol.impe3.pms.core.obj.ComponentObject.Inherited;
import com.isotrol.impe3.pms.core.obj.ComponentsObject;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.ContextPortal;
import com.isotrol.impe3.pms.core.obj.ModuleObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.ComponentEntity;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.DependencySetEntity;
import com.isotrol.impe3.pms.model.ExportJobType;
import com.isotrol.impe3.pms.model.OverridenComponentValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.PortalEntity;


/**
 * Implementation of ComponentsService.
 * @author Andres Rodriguez.
 */
@Service("componentsService")
public final class ComponentsServiceImpl extends AbstractContextService implements ComponentsService, ComponentManager {
	private ModuleInstanceComponent moduleComponent;
	private PortalManager portalManager;
	private ConfigurationManager configurationManager;
	@Autowired
	private DependencySetManager dependencySetManager;
	/** Export job manager. */
	@Autowired
	private ExportJobManager exportJobManager;
	/** File Manager. */
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public ComponentsServiceImpl() {
	}

	@Autowired
	public void setModuleComponent(ModuleInstanceComponent moduleComponent) {
		this.moduleComponent = moduleComponent;
	}

	@Autowired
	public void setPortalManager(PortalManager portalManager) {
		this.portalManager = portalManager;
	}

	@Autowired
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.COMPONENT;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#delete(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public void delete(String portalId, String id) throws PMSException {
		// Check the component.
		ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		ComponentObject c = ctx.getComponents().loadOwned(id);
		// Load it
		final PortalDfn portalDfn = touchComponents(ctx);
		final ComponentDfn dfn = new DfnMap(portalDfn).get(c.getId());
		// Check use
		final ComponentEntity entity = dfn.getComponent();
		final boolean used = getDao().isUsed(getEnvironmentId(), entity, ComponentEntity.USED);
		InUseProviders.COMPONENT.checkUsed(used, id);
		// Delete the dfn
		portalDfn.getComponents().remove(dfn);
		getDao().delete(dfn);
		// If it is the last definition the entity is deleted
		if (!getDao().hasRows(ComponentEntity.DFNS, getEnvironmentId(), entity.getId())) {
			getDao().delete(entity);
		}
	}

	/**
	 * @throws PMSException
	 * @throws
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#get(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public ModuleInstanceTemplateDTO get(String portalId, String id) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getOwnedComponentTemplate(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getComponentModules()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public List<ComponentModuleDTO> getComponentModules() {
		return newArrayList(filter(getRegistry().getComponents(getLocale()), new Instantiable(loadContextGlobal())));
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getComponents(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<ModuleInstanceSelDTO> getComponents(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getOwnComponentsSel();
	}
	
	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getComponentsByCorrectness(java.lang.String, com.isotrol.impe3.pms.api.Correctness)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<ModuleInstanceSelDTO> getComponentsByCorrectness(String portalId, Correctness correctness) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getOwnComponentsSel(correctness);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#newTemplate(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public ModuleInstanceTemplateDTO newTemplate(String key) throws PMSException {
		return ModuleObject.template(loadContextGlobal(), key, ModuleType.COMPONENT);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#save(java.lang.String,
	 * com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public ModuleInstanceTemplateDTO save(String portalId, ModuleInstanceDTO dto) throws PMSException {
		Preconditions.checkNotNull(dto);
		final ContextPortal ctx = loadContextGlobal().toPortal(portalId);
		ComponentsObject components = ctx.getComponents();
		final boolean isNew;
		final ComponentEntity entity;
		final ComponentDfn dfn;
		final String id = dto.getId();
		// 1 - Touch portal
		final PortalDfn portalDfn = touchComponents(ctx);
		final PortalEntity portal = portalDfn.getEntity();
		// 2 - Load or create entity and/or definition.
		if (id == null) {
			isNew = true;
			entity = new ComponentEntity();
			entity.setPortal(portal);
			dfn = new ComponentDfn();
			dfn.setComponent(entity);
		} else {
			ComponentObject c = components.loadOwned(id);
			isNew = false;
			dfn = new DfnMap(portalDfn).get(c.getId());
			entity = dfn.getComponent();
			Preconditions.checkArgument(entity.getModuleClass().equals(dto.getKey()));
		}
		// 3 - Fill definition.
		moduleComponent.fill(dto, dfn, isNew);
		// 4 - Save if needed.
		if (isNew) {
			saveNewEntity(entity);
			saveNewEntity(dfn);
			portalDfn.getComponents().add(dfn);
		}
		// 6 - Sync and return template.
		sync();
		return get(portalId, entity.getId().toString());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getInheritedComponents(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public List<InheritedComponentInstanceSelDTO> getInheritedComponents(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getInheritedComponentsSel();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getConfiguration(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public ConfigurationTemplateDTO getConfiguration(String portalId, String componentId) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesConfiguration(), componentId);
		return c.toTemplateDTO(context).getConfiguration();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#getDependencies(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public DependencySetTemplateDTO getDependencies(String portalId, String componentId) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesDependencies(), componentId);
		DependencySetTemplateDTO dto = new DependencySetTemplateDTO();
		dto.setDependencies(c.toTemplateDTO(context).getDependencies());
		return dto;
	}

	private InheritedComponentInstanceSelDTO returnUpdated(String portalId, String componentId) throws PMSException {
		sync();
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		return context.getComponents().loadInherited(componentId).toInheritedDTO(context);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#overrideConfiguration(java.lang.String,
	 * java.lang.String, java.util.List)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public InheritedComponentInstanceSelDTO overrideConfiguration(String portalId, String componentId,
		List<ConfigurationItemDTO> config) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		overrideConfiguration(context, componentId, config);
		return returnUpdated(portalId, componentId);
	}

	private void overrideConfiguration(ContextPortal context, String componentId, List<ConfigurationItemDTO> config)
		throws PMSException {
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesConfiguration(), componentId);
		// Update it
		PortalDfn portalDfn = touchComponents(context);
		ComponentEntity entity = findById(ComponentEntity.class, c.getId());
		OverridenComponentValue ocv = portalDfn.getOverridenComponents().get(entity);
		if (ocv == null) {
			ocv = new OverridenComponentValue();
			portalDfn.getOverridenComponents().put(entity, ocv);
		}
		ConfigurationEntity ce = ocv.getConfiguration();
		if (ce == null) {
			ce = configurationManager.create(c.getModule().getConfiguration(), config);
		} else {
			ce = configurationManager.update(c.getModule().getConfiguration(), ce, config);
		}
		ocv.setConfiguration(ce);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#clearConfiguration(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public InheritedComponentInstanceSelDTO clearConfiguration(String portalId, String componentId) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesConfiguration(), componentId);
		if (!c.overridesConfiguration().booleanValue()) {
			return c.toInheritedDTO(context);
		}
		// Update it
		PortalDfn portalDfn = touchComponents(context);
		ComponentEntity entity = findById(ComponentEntity.class, c.getId());
		OverridenComponentValue ocv = portalDfn.getOverridenComponents().get(entity);
		configurationManager.delete(ocv.getConfiguration());
		if (ocv.getDependencySet() == null) {
			portalDfn.getOverridenComponents().remove(entity);
		} else {
			ocv.setConfiguration(null);
		}
		return returnUpdated(portalId, componentId);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#overrideDependencies(java.lang.String,
	 * java.lang.String, java.util.List)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public InheritedComponentInstanceSelDTO overrideDependencies(String portalId, String componentId,
		List<DependencyDTO> dependencies) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		overrideDependencies(context, componentId, dependencies);
		return returnUpdated(portalId, componentId);
	}

	private void overrideDependencies(ContextPortal context, String componentId, List<DependencyDTO> dependencies)
		throws PMSException {
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesDependencies(), componentId);
		// Update it
		PortalDfn portalDfn = touchComponents(context);
		ComponentEntity entity = findById(ComponentEntity.class, c.getId());
		OverridenComponentValue ocv = portalDfn.getOverridenComponents().get(entity);
		if (ocv == null) {
			ocv = new OverridenComponentValue();
			portalDfn.getOverridenComponents().put(entity, ocv);
		}
		DependencySetEntity se = ocv.getDependencySet();
		se = dependencySetManager.save(null, c.getModule(), dependencies, se);
		ocv.setDependencySet(se);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.component.ComponentsService#clearDependencies(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public InheritedComponentInstanceSelDTO clearDependencies(String portalId, String componentId) throws PMSException {
		final ContextPortal context = loadContextGlobal().toPortal(portalId);
		final Inherited c = context.getComponents().loadInherited(componentId);
		NotFoundProviders.COMPONENT.checkNotNull(c.overridesDependencies(), componentId);
		if (!c.overridesDependencies().booleanValue()) {
			return c.toInheritedDTO(context);
		}
		// Update it
		PortalDfn portalDfn = touchComponents(context);
		ComponentEntity entity = findById(ComponentEntity.class, c.getId());
		OverridenComponentValue ocv = portalDfn.getOverridenComponents().get(entity);
		dependencySetManager.delete(ocv.getDependencySet());
		if (ocv.getConfiguration() == null) {
			portalDfn.getOverridenComponents().remove(entity);
		} else {
			ocv.setDependencySet(null);
		}
		return returnUpdated(portalId, componentId);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ComponentManager#duplicate(com.isotrol.impe3.pms.model.ComponentDfn)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ComponentDfn duplicate(ComponentDfn dfn) throws PMSException {
		final ComponentDfn dup = new ComponentDfn();
		dup.setComponent(dfn.getComponent());
		dup.setName(dfn.getName());
		dup.setDescription(dfn.getDescription());
		dup.getDependencies().putAll(dfn.getDependencies());
		final ConfigurationEntity config = dfn.getConfiguration();
		if (config != null) {
			dup.setConfiguration(configurationManager.duplicate(config));
		}
		saveNewEntity(dup);
		return dup;
	}

	private final class Instantiable implements Predicate<ComponentModuleDTO> {
		private final ContextGlobal ctx;

		Instantiable(ContextGlobal ctx) {
			this.ctx = checkNotNull(ctx);
		}

		public boolean apply(ComponentModuleDTO input) {
			if (!input.isInstantiable()) {
				return false;
			}
			try {
				final ModuleDefinition<?> md = getRegistry().getModule(input.getId());
				return ctx.isInstantiable(md);
			} catch (RuntimeException e) {
				return false;
			}
		}
	}

	private PortalDfn touchComponents(ContextPortal context) throws PMSException {
		final PortalEntity entity = loadPortal(context.getPortalId());
		final PortalDfn portalDfn = portalManager.touchComponents(context.getPortals(), entity);
		return portalDfn;
	}

	// Definitions mapping.

	private static final Function<ComponentDfn, UUID> DFN2ID = new Function<ComponentDfn, UUID>() {
		public UUID apply(ComponentDfn from) {
			return from.getInstanceId();
		}
	};

	private static final class DfnMap extends ForwardingMap<UUID, ComponentDfn> {
		private final ImmutableMap<UUID, ComponentDfn> map;

		DfnMap(PortalDfn dfn) {
			map = Maps.uniqueIndex(dfn.getComponents(), DFN2ID);
		}

		@Override
		protected Map<UUID, ComponentDfn> delegate() {
			return map;
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String exportAll(String portalId) throws PMSException {
		return exportJobManager.create(ExportJobType.COMPONENT_ALL,
			loadContextGlobal().toPortal(portalId).getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String exportSome(String portalId, Set<String> ids) throws PMSException {
		final ComponentsObject cmps = loadContextGlobal().toPortal(portalId).getComponents();
		final Set<UUID> set = Sets.newHashSet();
		for (String id : ids) {
			set.add(cmps.loadOwned(id).getId());
		}

		return exportJobManager.create(ExportJobType.COMPONENT, cmps.getPortalId(), null, set).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public void importComponents(String portalId, String fileId, boolean overwrite) throws PMSException {
		// Load file
		final ComponentsPB msg = fileManager.parseImportFile(fileId, ComponentsPB.newBuilder(), true).build();
		try {
			for (ComponentPB c : msg.getComponentsList()) {
				importComponent(loadContextGlobal().toPortal(portalId), c, overwrite);
			}
		} finally {
			purge();
		}
	}

	private void importComponent(ContextPortal ctx, ComponentPB c, boolean overwrite) throws PMSException {
		final ComponentsObject cmps = ctx.getComponents();
		final UUID id = COMPONENT.toUUID(c.getModule().getId());
		if (cmps.isOwned(id)) {
			if (!overwrite) {
				return;
			}
			importComponent(ctx, findById(ComponentEntity.class, id), id, c);
		} else {
			// Check it is not inherited
			COMPONENT.checkCondition(!cmps.isInherited(id), id.toString());
			// Check it is not from other portal.
			ComponentEntity e = findById(ComponentEntity.class, id);
			if (e != null) {
				COMPONENT.checkCondition(ctx.getPortalId().equals(e.getPortal().getId()), id.toString());
			}
			importComponent(ctx, e, id, c);
		}
	}

	private void importComponent(ContextPortal ctx, ComponentEntity e, UUID id, ComponentPB c) throws PMSException {
		// Touch portal
		final PortalDfn portalDfn = touchComponents(ctx);
		final PortalEntity portal = portalDfn.getEntity();
		final ModulePB m = c.getModule();
		final boolean isNewDfn;
		final boolean isNewEntity;
		final ComponentDfn dfn;
		// Check module
		if (e != null) {
			checkArgument(e.getModuleClass().equals(m.getModuleClass()), "Different component module");
			isNewEntity = false;
			final DfnMap map = new DfnMap(portalDfn);
			if (!map.containsKey(id)) {
				dfn = new ComponentDfn();
				dfn.setComponent(e);
				isNewDfn = true;
			} else {
				dfn = map.get(id);
				isNewDfn = false;
			}
		} else {
			isNewDfn = true;
			isNewEntity = true;
			e = new ComponentEntity();
			e.setPortal(portal);
			e.setId(id);
			dfn = new ComponentDfn();
			dfn.setComponent(e);

		}
		moduleComponent.fill(m, dfn, isNewDfn);
		if (isNewDfn) {
			if (isNewEntity) {
				saveNewEntity(e);
			}
			saveNewEntity(dfn);
			portalDfn.getComponents().add(dfn);
		}
		sync();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String exportAllOverrides(String portalId) throws PMSException {
		return exportJobManager.create(ExportJobType.COMPONENT_OVR_ALL,
			loadContextGlobal().toPortal(portalId).getPortalId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_GET, portal=PortalAuthority.GET)
	public String exportSomeOverrides(String portalId, Set<String> ids) throws PMSException {
		final ComponentsObject cmps = loadContextGlobal().toPortal(portalId).getComponents();
		final Set<UUID> set = Sets.newHashSet();
		for (String id : ids) {
			set.add(cmps.loadInherited(id).getId());
		}

		return exportJobManager.create(ExportJobType.COMPONENT_OVR, cmps.getPortalId(), null, set).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.PORTAL_COMPONENT, portal=PortalAuthority.COMPONENT)
	public void importOverrides(String portalId, String fileId, boolean overwrite) throws PMSException {
		final InheritedComponentsPB msg = fileManager.parseImportFile(fileId, InheritedComponentsPB.newBuilder(), true)
			.build();
		try {
			for (InheritedComponentPB c : msg.getComponentsList()) {
				importOverride(loadContextGlobal().toPortal(portalId), c, overwrite);
			}
		} finally {
			purge();
		}
	}

	private void importOverride(ContextPortal ctx, InheritedComponentPB c, boolean overwrite) throws PMSException {
		final String id = c.getId();
		final ComponentsObject cmps = ctx.getComponents();
		final Inherited ic = cmps.loadInherited(id);
		if (ic.isOverriden() && !overwrite) {
			return;
		}
		if (!c.getOverridesConfig() && !c.getOverridesDeps()) {
			// Should not happen
			return;
		}
		boolean done = false;
		if (c.getOverridesConfig() && c.hasConfiguration()) {
			overrideConfiguration(ctx, id, configurationManager.pb2dto(c.getConfiguration()));
			done = true;
		}
		if (c.getOverridesDeps() && c.getDependenciesCount() > 0) {
			overrideDependencies(ctx, id, Lists.transform(c.getDependenciesList(), Mappers.DEP_PB2DTO));
			done = true;
		}
		if (done) {
			sync();
		}
	}

}
