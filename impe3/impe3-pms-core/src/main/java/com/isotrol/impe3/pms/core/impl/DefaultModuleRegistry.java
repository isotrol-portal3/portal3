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
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Maps.uniqueIndex;
import static com.isotrol.impe3.pms.core.support.Mappers.list;
import static com.isotrol.impe3.pms.core.support.Mappers.named2described;

import java.util.Locale;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.core.modules.ConnectorProvision;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleException;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.core.modules.Modules;
import com.isotrol.impe3.core.modules.Relationship;
import com.isotrol.impe3.pms.api.mreg.AbstractModuleDTO;
import com.isotrol.impe3.pms.api.mreg.AbstractValidModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleDependencyDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleRelationDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;
import com.isotrol.impe3.pms.api.mreg.ProvidedComponentDTO;
import com.isotrol.impe3.pms.api.mreg.ProvidedConnectorDTO;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.support.SatisfiabilitySupport;


/**
 * Default implementacion of a module registry.
 * @author Andres Rodriguez.
 */
public final class DefaultModuleRegistry implements ModuleRegistry {
	/** Indexing function by type. */
	private static final Function<ModuleDefinition<?>, Class<?>> BY_TYPE = new Function<ModuleDefinition<?>, Class<?>>() {
		public Class<?> apply(ModuleDefinition<?> from) {
			return from.getType();
		}
	};

	/** Indexing function by type name. */
	private static final Function<ModuleDefinition<?>, String> BY_NAME = new Function<ModuleDefinition<?>, String>() {
		public String apply(ModuleDefinition<?> from) {
			return from.getType().getName();
		}
	};

	private static void fillSel(ModuleSelDTO dto, ModuleDefinition<?> module, Locale locale) {
		dto.setId(module.getType().getName());
		named2described(module, dto, locale);
	}

	private static void fill(AbstractModuleDTO dto, ModuleDefinition<?> module, Locale locale) {
		fillSel(dto, module, locale);
		dto.setVersion(module.getVersion());
		dto.setCopyright(module.getCopyright());
	}

	private static void fill(ModuleRelationDTO dto, Relationship r, Locale locale) {
		named2described(r, dto, locale);
		dto.setBean(r.getBeanName());
		dto.setType(r.getType().getName());
	}

	private static Function<ModuleDefinition<?>, ModuleSelDTO> moduleSel(final Locale locale) {
		return new Function<ModuleDefinition<?>, ModuleSelDTO>() {
			public ModuleSelDTO apply(ModuleDefinition<?> from) {
				final ModuleSelDTO dto = new ModuleSelDTO();
				fillSel(dto, from, locale);
				return dto;
			}
		};
	}

	private static Function<ConnectorProvision, ProvidedConnectorDTO> providedConnector(final Locale locale) {
		return new Function<ConnectorProvision, ProvidedConnectorDTO>() {
			public ProvidedConnectorDTO apply(ConnectorProvision from) {
				final ProvidedConnectorDTO dto = new ProvidedConnectorDTO();
				fill(dto, from, locale);
				return dto;
			}
		};
	}

	private static Function<ComponentProvision, ProvidedComponentDTO> providedComponent(final Locale locale) {
		return new Function<ComponentProvision, ProvidedComponentDTO>() {
			public ProvidedComponentDTO apply(ComponentProvision from) {
				final ProvidedComponentDTO dto = new ProvidedComponentDTO();
				fill(dto, from, locale);
				dto.setVersion(from.getComponent().getVersion());
				dto.setCopyright(from.getComponent().getCopyright());
				return dto;
			}
		};
	}

	/**
	 * Filters a collection of module definitions by type.
	 * @param definitions Definitions.
	 * @param moduleType Requested type.
	 * @return The filtered collection.
	 */
	private static Iterable<ModuleDefinition<?>> byModuleType(final Iterable<ModuleDefinition<?>> definitions,
		final ModuleType moduleType) {
		return filter(definitions, Modules.ofModuleType(moduleType));
	}

	/** Module index by type. */
	private final ImmutableMap<Class<?>, ModuleDefinition<?>> byType;
	/** Module index by name. */
	private final ImmutableMap<String, ModuleDefinition<?>> byName;
	/** Unsatisfiable dependencies. */
	private final SatisfiabilitySupport satisfiability;
	/** Not found names. */
	private final ImmutableSet<String> notFound;
	/** Not module types. */
	private final ImmutableSet<Class<?>> notModule;

	/**
	 * Default constructor.
	 * @param definitions Definitions to include in the registry.
	 * @param notFound Not found names.
	 * @param notModule Not module types.
	 */
	protected DefaultModuleRegistry(final Iterable<ModuleDefinition<?>> definitions, Set<String> notFound,
		Set<Class<?>> notModule) {
		this.byType = uniqueIndex(definitions, BY_TYPE);
		this.byName = uniqueIndex(definitions, BY_NAME);
		this.satisfiability = new SatisfiabilitySupport(definitions);
		this.notFound = ImmutableSet.copyOf(notFound);
		this.notModule = ImmutableSet.copyOf(notModule);
	}

	private void check(ModuleDefinition<?> module) {
		Preconditions.checkNotNull(module, "A module definition must be provided");
		checkArgument(byType.containsKey(module.getType()));
	}

	private void check(ModuleDefinition<?> module, ModuleType moduleType) {
		check(module);
		checkArgument(module.getModuleType() == moduleType);
	}

	/**
	 * Returns all registered modules.
	 * @return All registered modules.
	 */
	public Iterable<ModuleDefinition<?>> getModules() {
		return byType.values();
	}

	/**
	 * Returns all registered modules of the specified type.
	 * @param moduleType Requested type.
	 * @return All registered modules of the specified type.
	 */
	public Iterable<ModuleDefinition<?>> getModules(final ModuleType moduleType) {
		Preconditions.checkNotNull(moduleType);
		return byModuleType(getModules(), moduleType);
	}

	/**
	 * Returns the definition of a module if registered in this registry.
	 * @param moduleClass Requested module.
	 * @return The module definition of {@code null} if the module is not registered.
	 */
	public <T extends Module> ModuleDefinition<T> getModule(Class<T> moduleClass) {
		@SuppressWarnings("unchecked")
		final ModuleDefinition<T> md = (ModuleDefinition<T>) byType.get(moduleClass);
		return md;
	}

	/**
	 * Returns the definition of a module if registered in this registry.
	 * @param moduleClassName Requested module class name.
	 * @return The module definition of {@code null} if no module with the specified class name is registered.
	 */
	public ModuleDefinition<?> getModule(String moduleClassName) {
		return byName.get(moduleClassName);
	}
	
	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getNotFound()
	 */
	public Set<String> getNotFound() {
		return notFound;
	}
	
	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getNotModule()
	 */
	public Set<Class<?>> getNotModule() {
		return notModule;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#dependency2dto(com.isotrol.impe3.core.modules.ModuleDefinition,
	 * java.util.Locale)
	 */
	public Dependency2DTO dependency2dto(final ModuleDefinition<?> definition, final Locale locale) {
		check(definition);
		return new Dependency2DTO() {
			public ModuleDependencyDTO apply(Dependency from) {
				final ModuleDependencyDTO dto = new ModuleDependencyDTO();
				fill(dto, from, locale);
				dto.setRequired(from.isRequired());
				dto.setSatisfiable(satisfiability.isSatisfiable(definition, from));
				return dto;
			}
		};
	}

	private void fillValid(final AbstractValidModuleDTO dto, final ModuleDefinition<?> module, final Locale locale) {
		fill(dto, module, locale);
		dto.setInstantiable(satisfiability.isInstantiable(module));
		dto.setDependencies(list(module.getExternalDependencies().values(), dependency2dto(module, locale)));
	}

	private Function<ModuleDefinition<?>, ConnectorModuleDTO> connector(final Locale locale) {
		return new Function<ModuleDefinition<?>, ConnectorModuleDTO>() {
			public ConnectorModuleDTO apply(ModuleDefinition<?> from) {
				final ConnectorModuleDTO dto = new ConnectorModuleDTO();
				fillValid(dto, from, locale);
				dto.setConnectors(list(from.getConnectorProvisions().values(), providedConnector(locale)));
				return dto;
			}
		};
	}

	private Function<ModuleDefinition<?>, ComponentModuleDTO> component(final Locale locale) {
		return new Function<ModuleDefinition<?>, ComponentModuleDTO>() {
			public ComponentModuleDTO apply(ModuleDefinition<?> from) {
				final ComponentModuleDTO dto = new ComponentModuleDTO();
				fillValid(dto, from, locale);
				dto.setComponents(list(from.getComponentProvisions().values(), providedComponent(locale)));
				return dto;
			}
		};
	}

	private Function<ModuleDefinition<?>, InvalidModuleDTO> invalid(final Locale locale) {
		return new Function<ModuleDefinition<?>, InvalidModuleDTO>() {
			public InvalidModuleDTO apply(ModuleDefinition<?> from) {
				final InvalidModuleDTO dto = new InvalidModuleDTO();
				fill(dto, from, locale);
				dto.setInstantiable(false);
				final ModuleException e = from.getError();
				if (e != null) {
					dto.setError(e.getMessage());
				}
				return dto;
			}
		};
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getModuleSel(com.isotrol.impe3.core.modules.ModuleDefinition,
	 * java.util.Locale)
	 */
	public ModuleSelDTO getModuleSel(ModuleDefinition<?> definition, Locale locale) {
		check(definition);
		return moduleSel(locale).apply(definition);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getConnector(com.isotrol.impe3.core.modules.ModuleDefinition,
	 * java.util.Locale)
	 */
	public ConnectorModuleDTO getConnector(ModuleDefinition<?> definition, Locale locale) {
		check(definition, ModuleType.CONNECTOR);
		return connector(locale).apply(definition);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getComponent(com.isotrol.impe3.core.modules.ModuleDefinition,
	 * java.util.Locale)
	 */
	public ComponentModuleDTO getComponent(ModuleDefinition<?> definition, Locale locale) {
		check(definition, ModuleType.COMPONENT);
		return component(locale).apply(definition);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getComponents(java.util.Locale)
	 */
	public Iterable<ComponentModuleDTO> getComponents(Locale locale) {
		return Iterables.transform(getModules(ModuleType.COMPONENT), component(locale));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getConnectors(java.util.Locale)
	 */
	public Iterable<ConnectorModuleDTO> getConnectors(Locale locale) {
		return Iterables.transform(getModules(ModuleType.CONNECTOR), connector(locale));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ModuleRegistry#getInvalids(java.util.Locale)
	 */
	public Iterable<InvalidModuleDTO> getInvalids(Locale locale) {
		return Iterables.transform(getModules(ModuleType.INVALID), invalid(locale));
	}
}
