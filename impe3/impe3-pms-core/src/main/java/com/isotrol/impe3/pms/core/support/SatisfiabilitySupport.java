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

package com.isotrol.impe3.pms.core.support;


import static com.isotrol.impe3.core.modules.Dependency.IS_REQUIRED;
import static com.isotrol.impe3.core.modules.Modules.IS_CONNECTOR;
import static com.isotrol.impe3.core.modules.Modules.IS_VALID;

import java.util.Collection;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.Provision;


/**
 * A satisfiability support object. This class is not thread-safe.
 * @author Andres Rodriguez.
 */
public final class SatisfiabilitySupport {
	/** Unsatisfiable dependencies. */
	private final ImmutableMultimap<ModuleDefinition<?>, String> unsatisfiable;

	/**
	 * Constructor.
	 * @param dfns Connector definitions.
	 */
	public SatisfiabilitySupport(Iterable<ModuleDefinition<?>> modules) {
		final SatisfiabilityGraph graph = new SatisfiabilityGraph(modules);
		final ImmutableMultimap.Builder<ModuleDefinition<?>, String> builder = ImmutableMultimap.builder();
		for (DependencyNode dn : graph.getDependencies()) {
			if (!graph.isSatisfiable(dn)) {
				builder.put(dn.module.module, dn.name);
			}
		}
		unsatisfiable = builder.build();
	}

	public boolean isInstantiable(ModuleDefinition<?> module) {
		return !unsatisfiable.containsKey(module);
	}

	public boolean isSatisfiable(ModuleDefinition<?> module, Dependency d) {
		return !unsatisfiable.containsEntry(module, d.getBeanName());
	}

	private final class SatisfiabilityGraph {
		/** Directed graph. */
		private final Graph graph = new Graph();
		/** Modules without required dependencies. */
		private final Set<ModuleNode> start = Sets.newHashSet();
		/** Connectivity. */
		private final ConnectivityInspector<Node, DefaultEdge> inspector;

		/**
		 * Constructor.
		 * @param dfns Connector definitions.
		 */
		private SatisfiabilityGraph(Iterable<ModuleDefinition<?>> modules) {
			final Set<ProvisionNode> provisions = Sets.newHashSet();
			;
			for (ModuleDefinition<?> module : Iterables.filter(modules, IS_VALID)) {
				final ModuleNode mn = new ModuleNode(module);
				graph.addVertex(mn);
				final Collection<Dependency> required = Collections2.filter(module.getExternalDependencies().values(),
					IS_REQUIRED);
				if (required.isEmpty()) {
					start.add(mn);
				} else {
					for (Dependency d : required) {
						final DependencyNode dn = new DependencyNode(mn, d);
						graph.addVertex(dn);
						graph.addEdge(dn, mn);
					}
				}
				if (IS_CONNECTOR.apply(module)) {
					for (Provision p : module.getProvisions().values()) {
						final ProvisionNode pn = new ProvisionNode(mn, p);
						provisions.add(pn);
						graph.addVertex(pn);
						graph.addEdge(mn, pn);
					}
				}
			}
			for (final DependencyNode dn : getDependencies()) {
				final Predicate<ProvisionNode> satisfies = new Predicate<ProvisionNode>() {
					public boolean apply(ProvisionNode input) {
						return (input.module != dn.module && dn.type.isAssignableFrom(input.type));
					}
				};
				for (ProvisionNode pn : Iterables.filter(provisions, satisfies)) {
					graph.addEdge(pn, dn);
				}
			}
			this.inspector = new ConnectivityInspector<Node, DefaultEdge>(graph);
		}

		private Iterable<DependencyNode> getDependencies() {
			return Iterables.filter(graph.vertexSet(), DependencyNode.class);
		}

		private boolean isSatisfiable(DependencyNode dn) {
			for (ModuleNode mn : start) {
				if (inspector.pathExists(mn, dn)) {
					return true;
				}
			}
			return false;
		}
	}

	@SuppressWarnings("serial")
	private static final class Graph extends DirectedMultigraph<Node, DefaultEdge> {
		public Graph() {
			super(DefaultEdge.class);
		}
	}

	private abstract static class Node {
	}

	private static final class ModuleNode extends Node {
		private final ModuleDefinition<?> module;

		ModuleNode(ModuleDefinition<?> module) {
			this.module = module;
		}
	}

	private abstract static class RelationshipNode extends Node {
		final ModuleNode module;
		final Class<?> type;

		RelationshipNode(ModuleNode module, Class<?> type) {
			this.module = module;
			this.type = type;
		}
	}

	private static final class ProvisionNode extends RelationshipNode {
		ProvisionNode(ModuleNode module, Provision p) {
			super(module, p.getType());
		}
	}

	private static final class DependencyNode extends RelationshipNode {
		private final String name;

		DependencyNode(ModuleNode module, Dependency d) {
			super(module, d.getType());
			this.name = d.getBeanName();
		}
	}

}
