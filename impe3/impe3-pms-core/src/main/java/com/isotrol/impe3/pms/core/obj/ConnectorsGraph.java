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
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Maps.filterKeys;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.Provision;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;


/**
 * A connectors graph.
 * @author Andres Rodriguez.
 */
public final class ConnectorsGraph {
	/** Directed graph. */
	private final Graph graph;
	/** Map from instance id to module definition. */
	private final ImmutableMap<UUID, ConnectorObject> map;
	/** Provided types. */
	private final ImmutableSet<Class<?>> types;

	/**
	 * Constructor.
	 * @param cnns Connector definitions.
	 */
	ConnectorsGraph(Map<UUID, ConnectorObject> cnns) {
		this.graph = new Graph();
		this.map = ImmutableMap.copyOf(cnns);
		for (final ConnectorObject c : map.values()) {
			graph.addVertex(c);
		}
		final ImmutableSet.Builder<Class<?>> tb = ImmutableSet.builder();
		for (final ConnectorObject c : map.values()) {
			for (final Provider p : c.getDependencies().values()) {
				graph.addEdge(map.get(p.getConnectorId()), c);
			}
			for (Provision p : c.getModule().getProvisions().values()) {
				tb.add(p.getType());
			}
		}
		this.types = tb.build();
	}

	/**
	 * Graph filtering helper recursive method.
	 * @param excluded Already excluded connectors ids.
	 * @param cnn Connector to exclude.
	 */
	private void exclude(final Set<UUID> excluded, final ConnectorObject cnn) {
		if (excluded.add(cnn.getId())) {
			for (final ConnectorObject target : Graphs.successorListOf(graph, cnn)) {
				exclude(excluded, target);
			}
		}
	}

	/**
	 * Filters a graph taking out edges directed to the provided connector Id.
	 * @param connectorId Connector to filter out.
	 */
	ConnectorsGraph filter(final UUID connectorId) {
		if (connectorId == null || !map.containsKey(connectorId)) {
			return this;
		}
		final Set<UUID> excluded = Sets.newHashSet();
		exclude(excluded, map.get(connectorId));
		return new ConnectorsGraph(filterKeys(map, not(in(excluded))));
	}

	Set<Provider> getPossibleProviders(final Class<?> type) {
		checkNotNull(type);
		final Set<Provider> set = Sets.newHashSet();
		for (ConnectorObject c : graph.vertexSet()) {
			final ModuleDefinition<?> md = c.getModule();
			for (final Provision p : md.getProvisions().values()) {
				if (type.isAssignableFrom(p.getType())) {
					set.add(Provider.of(c, p.getBeanName()));
				}
			}
		}
		return set;
	}

	Provider getProviderFor(final String connectorId, final String bean) throws PMSException {
		return getProviderFor(NotFoundProviders.CONNECTOR.toUUID(connectorId), bean);
	}

	Provider getProviderFor(final UUID connectorId, final String bean) {
		final ConnectorObject c = map.get(connectorId);
		checkArgument(c != null);
		checkArgument(c.getModule().getProvisions().containsKey(bean));
		return Provider.of(c, bean);
	}

	/**
	 * Returns the iteration order. WARNING: destructive operation.
	 * @return The iteration order.
	 */
	Iterable<ConnectorObject> getInstantiationOrder() {
		final Predicate<ConnectorObject> inDegree = new Predicate<ConnectorObject>() {
			public boolean apply(ConnectorObject input) {
				return graph.inDegreeOf(input) == 0;
			}
		};
		final Set<ConnectorObject> v = graph.vertexSet();
		final List<ConnectorObject> ordered = Lists.newArrayListWithCapacity(v.size());
		while (!v.isEmpty()) {
			final Set<ConnectorObject> zero = Sets.newHashSet(Sets.filter(v, inDegree));
			for (ConnectorObject c : zero) {
				ordered.add(c);
				graph.removeVertex(c);
			}
		}
		return ordered;
	}

	private boolean isSatisfiable(Class<?> neededType) {
		for (Class<?> provided : types) {
			if (neededType.isAssignableFrom(provided)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a new instance of the provided module would be instantiable.
	 * @param module Module to check.
	 * @return True if it is instantiable.
	 */
	boolean isInstantiable(ModuleDefinition<?> module) {
		for (Dependency d : Iterables.filter(module.getExternalDependencies().values(), Dependency.IS_REQUIRED)) {
			if (!isSatisfiable(d.getType())) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("serial")
	private static final class Graph extends DirectedMultigraph<ConnectorObject, DefaultEdge> {
		public Graph() {
			super(DefaultEdge.class);
		}
	}
}
