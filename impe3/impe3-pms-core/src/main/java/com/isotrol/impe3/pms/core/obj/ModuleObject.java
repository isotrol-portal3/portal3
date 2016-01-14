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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.obj.MessageMappers.provisionPB;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.core.ImpeIAModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleException;
import com.isotrol.impe3.core.modules.ModuleStarter;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.pbuf.BaseProtos.DependencyPB;
import com.isotrol.impe3.pbuf.BaseProtos.ModulePB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.Described;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.support.AbstractValueLoader;
import com.isotrol.impe3.pms.model.DependencySetEntity;
import com.isotrol.impe3.pms.model.OverridenComponentValue;
import com.isotrol.impe3.pms.model.PortalConfigurationValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.WithDependencies;
import com.isotrol.impe3.pms.model.WithIdVersion;
import com.isotrol.impe3.pms.model.WithModuleDfn;


/**
 * Module instance domain object.
 * @author Andres Rodriguez
 */
public abstract class ModuleObject extends AbstractIdentifiable implements WithCorrectnessObject {
	private static final String LOG_PREFIX = "Module [{}] type [{}]: ";

	static final Function<ModuleObject, String> NAME = new Function<ModuleObject, String>() {
		public String apply(ModuleObject from) {
			return from == null ? null : from.name;
		}
	};

	private static final Function<Entry<String, Provider>, DependencyPB> DEP2PB = new Function<Entry<String, Provider>, DependencyPB>() {
		public DependencyPB apply(Entry<String, Provider> from) {
			final Provider p = from.getValue();
			return DependencyPB.newBuilder().setName(from.getKey()).setProvision(provisionPB(p)).build();
		}
	};

	static final Ordering<ModuleObject> BY_NAME = Ordering.<String> natural().onResultOf(NAME);

	/**
	 * Creates a template for a module instance.
	 * @param ctx Level 2 context.
	 * @param module Module definition.
	 * @param mi Module instance.
	 * @return The requested template.
	 */
	private static ModuleInstanceTemplateDTO create(Context2 ctx, ModuleDefinition<?> module, ModuleObject mi) {
		final ModuleInstanceTemplateDTO dto = new ModuleInstanceTemplateDTO();
		dto.setModule(ctx.getModuleSel(module));
		dto.setDependencies(ctx.getDependencies(module, mi));
		return dto;
	}

	/**
	 * Creates a template for a new module instance.
	 * @param ctx Level 2 context.
	 * @param module Module key.
	 * @param type Required type.
	 * @return The requested template.
	 */
	public static ModuleInstanceTemplateDTO template(Context2 ctx, String module, ModuleType type) {
		final ModuleDefinition<?> md;
		try {
			md = ModuleDefinition.of(module);
		} catch (ModuleException e) {
			throw new IllegalArgumentException("Invalid module");
		}
		checkArgument(type == md.getModuleType());
		return ModuleObject.template(ctx, md);
	}

	/**
	 * Creates a template for a new module instance.
	 * @param ctx Level 2 context.
	 * @param module Module definition.
	 * @return The requested template.
	 */
	public static ModuleInstanceTemplateDTO template(Context2 ctx, ModuleDefinition<?> module) {
		checkArgument(ctx.isInstantiable(module));
		final ModuleInstanceTemplateDTO dto = create(ctx, module, null);
		final ModuleSelDTO m = dto.getModule();
		dto.setName(m.getName());
		dto.setDescription(m.getDescription());
		final ConfigurationDefinition<?> cd = module.getConfiguration();
		if (cd != null) {
			dto.setConfiguration(ConfigurationObject.template(cd, ctx));
		}
		return dto;
	}

	private static final DepsLoader DEPS_LOADER = new DepsLoader();
	private static final OverridenDepsLoader ODEPS_LOADER = new OverridenDepsLoader();

	private final ModuleDefinition<?> module;
	private final String name;
	private final String description;
	private final ConfigurationObject configuration;
	private final PortalConfigurationObject portalConfiguration;
	private final boolean missingConfiguration;
	/** Dependencies. */
	private final Deps deps;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 * @param portalDfn 
	 */
	ModuleObject(WithModuleDfn dfn) {
		super(dfn.getInstanceId());
		this.module = dfn.getModuleDefinition();
		this.name = dfn.getName();
		this.description = dfn.getDescription();
		this.configuration = ConfigurationObject.of(this.module.getConfiguration(), dfn.getConfiguration());
		this.portalConfiguration = PortalConfigurationObject.of(this.module.getPortalConfiguration(), dfn);
		this.missingConfiguration = this.module.isConfigurationDependencyRequired() && this.configuration == null;
		this.deps = DEPS_LOADER.get(dfn, this.module);
	}
	
	/**
	 * Constructor.
	 * @param dfn Definition.
	 * @param portalDfn 
	 */
	ModuleObject(WithModuleDfn dfn, PortalDfn portalDfn) {
		super(dfn.getInstanceId());
		this.module = dfn.getModuleDefinition();
		this.name = dfn.getName();
		this.description = dfn.getDescription();
		this.configuration = ConfigurationObject.of(this.module.getConfiguration(), dfn.getConfiguration());
		PortalConfigurationDefinition<?> pcd = this.module.getPortalConfiguration();
		PortalConfigurationValue pcv = portalDfn.getActivePortalConfigurationValue(pcd.getType().getName());
		if (pcv != null) {
			this.portalConfiguration = PortalConfigurationObject.of(pcd, pcv.getPortalConfiguration());
		} else {
			this.portalConfiguration = null;
		}
		this.missingConfiguration = this.module.isConfigurationDependencyRequired() && this.configuration == null;
		this.deps = DEPS_LOADER.get(dfn, this.module);
		portalDfn.getActivePortalConfigurationValue(this.module.getPortalConfiguration().getType().getName());
	}

	/**
	 * Overriding constructor.
	 * @param m Module to override.
	 * @param o Overriding information.
	 * @param dfn 
	 */
	ModuleObject(ModuleObject m, OverridenComponentValue o, PortalDfn dfn) {
		super(m.getId());
		this.module = m.getModule();
		this.name = m.getName();
		this.description = m.getDescription();
		// Configuration
		if (o != null && o.getConfiguration() != null) {
			this.configuration = ConfigurationObject.of(this.module.getConfiguration(), o.getConfiguration());
			this.missingConfiguration = this.module.isConfigurationDependencyRequired() && this.configuration == null;
		} else {
			this.configuration = m.configuration;
			this.missingConfiguration = m.missingConfiguration;
		}
		PortalConfigurationDefinition<?> pcd = this.module.getPortalConfiguration();
		PortalConfigurationValue pcv = dfn.getActivePortalConfigurationValue(pcd.getType().getName());
		if (pcv != null) {
			this.portalConfiguration = PortalConfigurationObject.of(pcd, pcv.getPortalConfiguration());
		} else {
			this.portalConfiguration = null;
		}
		
		// Dependencies
		if (o != null && o.getDependencySet() != null) {
			this.deps = ODEPS_LOADER.get(o.getDependencySet(), this.module);
		} else {
			this.deps = m.deps;
		}
	}

	abstract State getState();

	public ModuleDefinition<?> getModule() {
		return module;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	ImmutableMap<String, Provider> getDependencies() {
		return deps.dependencies;
	}

	/**
	 * Returns whether a content type is used by this module.
	 * @param id Content type id.
	 * @return True if the content type is used by this module.
	 */
	public boolean isContentTypeUsed(UUID id) {
		return configuration != null ? configuration.isContentTypeUsed(id) : false;
	}

	/**
	 * Returns whether a category is used by this module.
	 * @param id Category id.
	 * @return True if the category is used by this module.
	 */
	public boolean isCategoryUsed(UUID id) {
		return configuration != null ? configuration.isCategoryUsed(id) : false;
	}

	/**
	 * Returns whether a connector is used by this module.
	 * @param id Connector id.
	 * @return True if the connector is used by this module.
	 */
	public boolean isConnectorUsed(UUID id) {
		return any(transform(deps.dependencies.values(), Provider.CONNECTOR_ID), equalTo(id));
	}

	public boolean isError() {
		return missingConfiguration || !deps.missing.isEmpty() || (configuration != null && configuration.isError());
	}

	public boolean isWarning() {
		return isError() || !deps.extra.isEmpty() || (configuration != null && configuration.isWarning());
	}

	public boolean isPortalConfigurationError() {
		return portalConfiguration == null || portalConfiguration.isError();
	}
	
	public Correctness getCorrectness() {
		if (isError()) {
			return Correctness.ERROR;
		} else if (isWarning()) {
			return Correctness.WARN;
		}
		return Correctness.OK;
	}

	private Object[] logArgs(Object o) {
		return new Object[] {getStringId(), module.getType().getName(), o};
	}

	public void log(Logger logger) {
		if (!deps.extra.isEmpty() && logger.isWarnEnabled()) {
			logger.warn(LOG_PREFIX + "Unused saved parameters: ", logArgs(deps.extra));
		}
		if (!deps.missing.isEmpty() && logger.isErrorEnabled()) {
			logger.warn(LOG_PREFIX + "Missing required parameters: ", logArgs(deps.missing));
		}
	}

	ModuleInstanceSelDTO toSelDTO(ModuleRegistry registry, Locale locale) {
		final ModuleInstanceSelDTO dto = new ModuleInstanceSelDTO();
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setCorrectness(getCorrectness());
		dto.setModule(registry.getModuleSel(module, locale));
		dto.setName(name);
		dto.setDescription(description);
		return dto;
	}

	public ModuleInstanceTemplateDTO toTemplateDTO(Context2 ctx) {
		final ModuleInstanceTemplateDTO dto = create(ctx, module, this);
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setCorrectness(getCorrectness());
		dto.setName(name);
		dto.setDescription(description);
		final ConfigurationDefinition<?> cd = module.getConfiguration();
		if (cd != null) {
			final ConfigurationTemplateDTO ct;
			if (configuration != null) {
				ct = configuration.toTemplateDTO(ctx);
			} else {
				ct = ConfigurationObject.template(cd, ctx);
			}
			dto.setConfiguration(ct);
		}
		
		final PortalConfigurationDefinition<?> pcd = module.getPortalConfiguration();
		if (pcd != null) {
			final ConfigurationTemplateDTO ct;
			if (portalConfiguration != null) {
				ct = portalConfiguration.toTemplateDTO(ctx);
			} else {
				ct = PortalConfigurationObject.template(pcd, ctx);
			}
			dto.setPortalConfiguration(ct);
		}
		
		return dto;
	}

	public final void fill(Described d) {
		if (d != null) {
			d.setName(name);
			d.setDescription(description);
		}
	}

	/**
	 * Returns a module started with all common dependencies provided.
	 * @param model Model to apply.
	 * @param connectors Connectors source.
	 * @return The module starter.
	 */
	public final ModuleStarter<?> starter(ImpeIAModel model, Function<UUID, StartedModule<?>> connectors) {
		// TODO: corner cases.
		Loggers.pms().info("Starting module Id [{}]...", getId());
		Loggers.pms().info("--> Module name: [{}]", getName());
		try {
			checkState(!isError(), "Invalid module state");
			final ModuleDefinition<?> md = getModule();
			Loggers.pms().info("--> Module class: [{}]", md.getClass().getName());
			final ModuleStarter<?> ms = md.starter();
			// 1 - Common internal dependencies
			ms.set(EngineMode.class, model.getMode());
			ms.set(ContentTypes.class, model.getContentTypes());
			ms.set(Categories.class, model.getCategories());
			ms.set(FileLoader.class, model.getFileLoader());
			// TODO Complete
			// 2 - External dependencies
			final Set<String> externalDependenciesNames = md.getExternalDependencies().keySet();
			for (final Entry<String, Provider> ed : deps.dependencies.entrySet()) {
				final String name = ed.getKey();
				if (!externalDependenciesNames.contains(name)) {
					Loggers.pms().warn("Module [{}] type [{}]: Unused dependency [{}]",
						new Object[] {getId().toString(), md.getType().getName(), name});
					continue;
				}
				final Provider p = ed.getValue();
				ms.put(name, connectors.apply(p.getConnectorId()).getProvision(p.getBean()));
			}
			// 3 - Configuration
			if (configuration != null) {
				ms.put(md.getConfigurationBeanName(), configuration.get(model));
			}
			
			// 4 - Portal Configuration
			if (portalConfiguration != null) {
				ms.put(md.getPortalConfigurationBeanName(), portalConfiguration.get(model));
			}
			
			return ms;
		} catch (RuntimeException e) {
			Loggers.pms().error("--> Error starting module Id [{}]...", getId());
			Loggers.pms().error("--> Module name: [{}]", getName());
			Loggers.pms().error("--> Error: ", e);
			throw e;
		}
		finally {
			Loggers.pms().info("Finished starting module Id [{}].", getId());
		}
	}

	ConfigurationObject getConfiguration() {
		return configuration;
	}

	/**
	 * @return the portalConfiguration
	 */
	public PortalConfigurationObject getPortalConfiguration() {
		return portalConfiguration;
	}

	final Iterable<DependencyPB> dependenciesPB() {
		if (getDependencies() != null) {
			return Iterables.transform(getDependencies().entrySet(), DEP2PB);
		}
		return ImmutableList.of();
	}

	final ModulePB modulePB(FileManager fileManager) {
		final ModulePB.Builder b = ModulePB.newBuilder();
		b.setId(getStringId());
		b.setName(getName());
		if (getDescription() != null) {
			b.setDescription(getDescription());
		}
		b.setModuleClass(getModule().getTypeName());
		if (getConfiguration() != null) {
			b.setConfiguration(getConfiguration().toPB(fileManager));
		}
		return b.addAllDependencies(dependenciesPB()).build();
	}

	private static final class Deps {
		/** Dependencies. */
		private final ImmutableMap<String, Provider> dependencies;
		/** Extra dependencies. */
		private final ImmutableMap<String, Provider> extra;
		/** Missing required dependencies. */
		private final ImmutableSet<String> missing;

		Deps(ImmutableMap<String, Provider> dependencies, ImmutableMap<String, Provider> extra,
			ImmutableSet<String> missing) {
			this.dependencies = checkNotNull(dependencies);
			this.extra = checkNotNull(extra);
			this.missing = checkNotNull(missing);
		}
	}

	private static abstract class AbstractDepsLoader<E extends WithIdVersion & WithDependencies> extends
		AbstractValueLoader<E, Deps, ModuleDefinition<?>> {
		AbstractDepsLoader(String name) {
			super(name);
		}

		final Deps loadDeps(WithDependencies deps, ModuleDefinition<?> module) {
			final Map<String, Provider> providers = Maps.transformValues(deps.getDependencies(),
				Provider.FROM_DEPENDENCY_VALUE);
			final Map<String, Dependency> external = module.getExternalDependencies();
			final Predicate<String> isExternal = in(external.keySet());
			ImmutableMap<String, Provider> dependencies = ImmutableMap.copyOf(Maps.filterKeys(providers, isExternal));
			ImmutableMap<String, Provider> extra = ImmutableMap.copyOf(Maps.filterKeys(providers, not(isExternal)));
			ImmutableSet<String> missing = ImmutableSet.copyOf(Sets.difference(module.getRequiredExternalDependencies()
				.keySet(), dependencies.keySet()));
			return new Deps(dependencies, extra, missing);
		}
	}

	private static final class DepsLoader extends AbstractDepsLoader<WithModuleDfn> {
		DepsLoader() {
			super("Module dependencies");
		}

		@Override
		protected Deps load(WithModuleDfn dfn, ModuleDefinition<?> module) {
			return loadDeps(dfn, module);
		}
	}

	private static final class OverridenDepsLoader extends AbstractDepsLoader<DependencySetEntity> {
		OverridenDepsLoader() {
			super("Overriden Module dependencies");
		}

		@Override
		protected Deps load(DependencySetEntity set, ModuleDefinition<?> module) {
			return loadDeps(set, module);
		}
	}

}
