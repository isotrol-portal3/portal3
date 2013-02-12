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

package com.isotrol.impe3.core.modules;


import static com.google.common.base.Predicates.not;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.common.collect.Iterables.concat;

import java.awt.image.renderable.RenderContext;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.PortalKeys;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.content.ContentLoader;


/**
 * Utility methods and values related to modules.
 */
public final class Modules {
	/** Not instantiable. */
	private Modules() {
		throw new AssertionError();
	}

	/** Internal dependencies. */
	private static final Class<?>[] INTERNAL_DEPENDENCIES_ARRAY = {Devices.class, Categories.class, PortalKeys.class,
		ContentTypes.class, URIGenerator.class, FileLoader.class, EngineMode.class};
	/** Internal dependencies. */
	public static final ImmutableSet<Class<?>> INTERNAL_DEPENDENCIES = copyOf(INTERNAL_DEPENDENCIES_ARRAY);

	/** Forbidden types in relationships. */
	@SuppressWarnings("unchecked")
	private static final ImmutableSet<Class<?>> FORBIDDEN = of(Component.class, ModuleStarter.class,
		StartedModule.class, Configuration.class, URIGenerator.class, RenderContext.class, ComponentRenderer.class,
		ContentLoader.class);

	/** Forbidden provisions. */
	private static final ImmutableSet<Class<?>> FORBIDDEN_PROVISIONS = copyOf(concat(INTERNAL_DEPENDENCIES, FORBIDDEN));

	/**
	 * Checks if the given type is an internal dependency.
	 */
	public static boolean isInternalDependency(Class<?> type) {
		return INTERNAL_DEPENDENCIES.contains(type);
	}

	/**
	 * Checks if the given type is a forbidden dependency.
	 */
	public static boolean isForbiddenDependency(Class<?> type) {
		return FORBIDDEN.contains(type);
	}

	/**
	 * Checks if the given type is a forbidden provision.
	 */
	public static boolean isForbiddenProvision(Class<?> type) {
		return FORBIDDEN_PROVISIONS.contains(type);
	}

	/**
	 * Returns a predicate for module selection by type.
	 * @param moduleType Requested type.
	 * @return The requested predicate.
	 * @throws NullPointerException if the argument is {@code null}
	 */
	public static Predicate<ModuleDefinition<?>> ofModuleType(final ModuleType moduleType) {
		Preconditions.checkNotNull(moduleType);
		return new Predicate<ModuleDefinition<?>>() {
			public boolean apply(ModuleDefinition<?> input) {
				return moduleType == input.getModuleType();
			}
		};
	}

	/** Predefined predicate returns true if is a connector module */
	public static final Predicate<ModuleDefinition<?>> IS_CONNECTOR = ofModuleType(ModuleType.CONNECTOR);
	/** Predefined predicate returns true if is a component module */
	public static final Predicate<ModuleDefinition<?>> IS_COMPONENT = ofModuleType(ModuleType.COMPONENT);
	/** Predefined predicate returns true if is an invalid module */
	public static final Predicate<ModuleDefinition<?>> IS_INVALID = ofModuleType(ModuleType.INVALID);
	/** Predefined predicate returns true if is a valid module */
	public static final Predicate<ModuleDefinition<?>> IS_VALID = not(IS_INVALID);

}
