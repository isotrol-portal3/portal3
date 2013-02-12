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


import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.obj.ConnectorObject.map2pb;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.CONNECTOR;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.core.ImpeIAModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.modules.ConnectorProvision;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.Provision;
import com.isotrol.impe3.core.modules.StartedModule;
import com.isotrol.impe3.core.support.IdentifiableMaps;
import com.isotrol.impe3.pbuf.connector.ConnectorProtos.ConnectorsPB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.api.mreg.AbstractModuleDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.ModuleRegistry.Dependency2DTO;
import com.isotrol.impe3.pms.core.support.EntityFunctions;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.ConnectorEntity;


/**
 * Collection of connectors domain object.
 * @author Andres Rodriguez
 */
public final class ConnectorsObject extends ModulesObject<ConnectorObject> {
	/** Connectors map. */
	private final ImmutableMap<UUID, ConnectorObject> map;
	/** Module registry. */
	private final ModuleRegistry registry;
	/** Directed graph. */
	private final ConnectorsGraph graph;
	/** Instantiation order. */
	private volatile Iterable<ConnectorObject> inOrder;

	/**
	 * Builds a collection from a set of definitions.
	 * @param registry Module Registry.
	 * @param dfns Definitions.
	 * @return The requested collection.
	 */
	public static ConnectorsObject definitions(ModuleRegistry registry, Iterable<ConnectorDfn> dfns) {
		final ConnectorsObject cnn = new ConnectorsObject(registry, dfns);
		return cnn;
	}

	/**
	 * Builds a collection from a set of current definitions.
	 * @param registry Module Registry.
	 * @param entities Entities.
	 */
	public static ConnectorsObject current(ModuleRegistry registry, Iterable<ConnectorEntity> entities) {
		return definitions(registry, transform(entities, EntityFunctions.CONNECTOR2DFN));
	}

	/**
	 * Constructor.
	 * @param registry Module Registry.
	 * @param dfns Definitions.
	 */
	private ConnectorsObject(ModuleRegistry registry, Iterable<ConnectorDfn> dfns) {
		this.registry = Preconditions.checkNotNull(registry);
		final Function<ConnectorDfn, ConnectorObject> f = new Function<ConnectorDfn, ConnectorObject>() {
			public ConnectorObject apply(ConnectorDfn from) {
				return new ConnectorObject(from);
			}
		};
		this.map = IdentifiableMaps.immutableOf(transform(dfns, f));
		this.graph = new ConnectorsGraph(this.map);
	}

	@Override
	protected Map<UUID, ConnectorObject> delegate() {
		return map;
	}

	List<ModuleInstanceSelDTO> map2sel(final Locale locale) {
		return map2sel(registry, locale, values());
	}

	List<ModuleInstanceSelDTO> map2sel(final Locale locale, Correctness correctness) {
		return map2sel(registry, locale,
			Iterables.filter(values(), compose(equalTo(correctness), ConnectorObject.CORRECTNESS)));
	}

	ConnectorObject load(UUID id) throws EntityNotFoundException {
		return CONNECTOR.checkNotNull(get(id), id);
	}

	public ConnectorObject load(String id) throws EntityNotFoundException {
		UUID uuid = null;

		try {
			uuid = UUID.fromString(id);
		} catch (Exception e) {}

		return load(uuid);
	}

	ModuleRegistry getRegistry() {
		return registry;
	}

	/**
	 * Checks whether a new instance of the provided module would be instantiable.
	 * @param module Module to check.
	 * @return True if it is instantiable.
	 */
	public boolean isInstantiable(ModuleDefinition<?> module) {
		return graph.isInstantiable(module);
	}

	/**
	 * Returns the possible providers for an specified interfaces.
	 * @param type Required interface.
	 * @return The possible providers.
	 */
	Set<Provider> getPossibleProviders(final Class<?> type) {
		return graph.getPossibleProviders(type);
	}

	/**
	 * Filters out not instantiable modules.
	 * @param modules Modules to filter.
	 * @return The filtered result.
	 */
	public <T extends AbstractModuleDTO> List<T> filter(Iterable<T> modules) {
		final Predicate<T> instantiable = new Predicate<T>() {
			public boolean apply(T input) {
				if (!input.isInstantiable()) {
					return false;
				}
				final ModuleDefinition<?> md = registry.getModule(input.getId());
				return graph.isInstantiable(md);
			};
		};
		return Lists.newArrayList(Iterables.filter(modules, instantiable));
	}

	/**
	 * Returns a template for a provided service.
	 * @param type Service interface type.
	 * @param currentConnector Connector currently providing the service.
	 * @param currentBean Bean currently providing the service.
	 * @param locale Locale.
	 * @return The requested template.
	 */
	public ProvidedTemplateDTO getProvidedTemplate(Class<?> type, ConnectorEntity currentConnector, String currentBean,
		Locale locale) {
		final UUID id = currentConnector == null ? null : currentConnector.getId();
		return getProvidedTemplate(type, id, currentBean, locale);
	}

	/**
	 * Returns a template for a provided service.
	 * @param type Service interface type.
	 * @param current Current provider.
	 * @param locale Locale.
	 * @return The requested template.
	 */
	public ProvidedTemplateDTO getProvidedTemplate(Class<?> type, Provider current, Locale locale) {
		final UUID id;
		final String bean;
		if (current == null) {
			id = null;
			bean = null;
		} else {
			id = current.getConnectorId();
			bean = current.getBean();
		}
		return getProvidedTemplate(type, id, bean, locale);
	}

	/**
	 * Returns a template for a provided service.
	 * @param type Service interface type.
	 * @param currentConnectorId Connector currently providing the service.
	 * @param currentBean Bean currently providing the service.
	 * @param locale Locale.
	 * @return The requested template.
	 */
	public ProvidedTemplateDTO getProvidedTemplate(Class<?> type, UUID currentConnectorId, String currentBean,
		Locale locale) {
		final Set<Provider> providers = getPossibleProviders(type);
		final ProvidedTemplateDTO dto = new ProvidedTemplateDTO();
		final Provider.Mapper m = Provider.mapper(this, registry, locale);
		dto.setProviders(Mappers.list(providers, m));
		// Current provider
		if (currentConnectorId == null || currentBean == null) {
			dto.setCurrent(null);
			return dto;
		}
		final ConnectorObject c = get(currentConnectorId);
		final ModuleDefinition<?> md = c.getModule();
		final ConnectorProvision p = md.getConnectorProvisions().get(currentBean);
		if (p == null) {
			Loggers.pms().error("Module [{}] does not export bean [{}]. Returning null current provider",
				new Object[] {md.getType().getName(), currentBean});
			dto.setCurrent(null);
			return dto;
		}
		if (!type.isAssignableFrom(p.getType())) {
			Loggers.pms().error(
				"Module [{}] provision bean [{}] is not assignable to [{}]. Returning null current provider",
				new Object[] {md.getType().getName(), currentBean, type.getName()});
			dto.setCurrent(null);
			return dto;
		}
		final Provider current = graph.getProviderFor(currentConnectorId, currentBean);
		final ProviderDTO pdto = m.apply(current);
		dto.setCurrent(pdto);
		return dto;
	}

	/**
	 * Checks if a provided connector reference is of the correct type.
	 * @param type Service type.
	 * @param dto Provided DTO.
	 * @return The connector entity if everything is ok, or {@code null} if the reference is null.
	 * @throws PMSException if the connector is not found.
	 * @throws IllegalArgumentException if the reference is of an incorrect type.
	 */
	public ConnectorObject checkProvided(Class<?> type, ProvidedDTO dto) throws PMSException {
		if (dto == null) {
			return null;
		}
		final UUID id = NotFoundProviders.CONNECTOR.toUUID(dto.getConnectorId());
		final String bean = dto.getBean();
		if (id == null || bean == null) {
			return null;
		}
		final ConnectorObject cnn = load(id);
		final ConnectorProvision p = cnn.getModule().getConnectorProvisions().get(bean);
		NotFoundProviders.CONNECTOR.checkCondition(p != null, id);
		Preconditions.checkArgument(type.isAssignableFrom(p.getType()));
		return cnn;
	}

	/**
	 * Returns the dependencies templates for a module instance.
	 * @param md Module definition.
	 * @param mi Module instance.
	 * @param locale Locale.
	 * @return The requested templates.
	 */
	List<DependencyTemplateDTO> getDependencies(ModuleDefinition<?> md, ModuleObject mi, Locale locale) {
		final List<DependencyTemplateDTO> deps;
		final Collection<Dependency> external = md.getExternalDependencies().values();
		if (external.isEmpty()) {
			deps = Lists.newArrayListWithCapacity(0);
		} else {
			deps = Lists.newArrayListWithCapacity(external.size());
			final Provider.Mapper f = Provider.mapper(this, registry, locale);
			final Map<String, Provider> current;
			ConnectorsGraph g = graph;
			if (mi != null) {
				current = mi.getDependencies();
				g = g.filter(mi.getId());
			} else {
				current = null;
			}
			Dependency2DTO dependency2dto = registry.dependency2dto(md, locale);
			for (final Dependency d : external) {
				final DependencyTemplateDTO dt = new DependencyTemplateDTO();
				dt.setDependency(dependency2dto.apply(d));
				final Set<Provider> providers = g.getPossibleProviders(d.getType());
				dt.setProviders(Mappers.list(providers, f));
				final String bean = d.getBeanName();
				if (current != null && current.containsKey(bean)) {
					final Provider p = current.get(bean);
					dt.setCurrent(f.apply(p));
				} else {
					dt.setCurrent(null);
				}
				deps.add(dt);
			}
		}
		return deps;
	}

	/**
	 * Validate the dependencies of a module.
	 * @param md Module definition.
	 * @param mi Module instance (optional).
	 * @param deps Dependencies to validate.
	 * @return The validated dependencies to store in the database.
	 */
	public Map<String, Provider> validateDependencies(ModuleDefinition<?> md, ModuleObject mi, List<DependencyDTO> deps)
		throws PMSException {
		final Map<String, Provider> valid = Maps.newHashMap();
		if (deps == null || deps.isEmpty()) {
			if (md.hasRequiredExternalDependencies()) {
				throw new PMSException(); // TODO
			}
			return valid; // nothing to do
		}
		final Map<String, Dependency> external = md.getExternalDependencies();
		final ConnectorsGraph g = (mi != null) ? graph.filter(mi.getId()) : graph;
		for (DependencyDTO ddto : deps) {
			final Provider provider = g.getProviderFor(ddto.getConnectorId(), ddto.getBean());
			final String name = ddto.getName();
			final ConnectorObject cnn = load(provider.getConnectorId());
			final Dependency d = external.get(name);
			if (d == null) {
				throw new PMSException(); // TODO
			}
			final ModuleDefinition<?> imd = cnn.getModule();
			final Provision p = imd.getProvisions().get(provider.getBean());
			if (p == null) {
				throw new PMSException(); // TODO
			}
			if (!d.getType().isAssignableFrom(p.getType())) {
				throw new PMSException(); // TODO
			}
			valid.put(name, provider);
		}
		// Check for unsatisfied dependencies.
		for (final Dependency d : external.values()) {
			if (d.isRequired() && !valid.containsKey(d.getBeanName())) {
				throw new PMSException(); // TODO
			}
		}
		// Done!!
		return valid;
	}

	Iterable<ConnectorObject> getInstantiationOrder() {
		if (inOrder == null) {
			final ConnectorsGraph g = new ConnectorsGraph(this.map);
			Iterable<ConnectorObject> local = g.getInstantiationOrder();
			inOrder = local;
		}
		return inOrder;
	}

	/**
	 * Starts the connectors collection, in order.
	 * @param model Model to apply.
	 * @return The started connectors.
	 */
	public StartedConnectors start(ImpeIAModel model) {
		Map<UUID, StartedModule<?>> started = Maps.newHashMap();
		Function<UUID, StartedModule<?>> f = Functions.forMap(started);
		List<StartedModule<?>> connectorStop = Lists.newLinkedList();
		for (ConnectorObject c : getInstantiationOrder()) {
			final StartedModule<?> sm = c.starter(model, f).start(null);
			started.put(c.getId(), sm);
			connectorStop.add(0, sm);
		}
		return new StartedConnectors(started, connectorStop);
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @param f Filter to apply
	 * @return The PB message.
	 */
	public final ConnectorsPB toPB(FileManager fileManager, Predicate<? super ConnectorObject> f) {
		ConnectorsPB.Builder b = ConnectorsPB.newBuilder();
		b.addAllConnectors((transform(Iterables.filter(getInstantiationOrder(), f), map2pb(fileManager))));
		return b.build();
	}

	/**
	 * Transforms the object to a protocol buffer message with no filter.
	 * @return The PB message.
	 */
	public final ConnectorsPB toPB(FileManager fileManager) {
		return toPB(fileManager, Predicates.alwaysTrue());
	}

}
