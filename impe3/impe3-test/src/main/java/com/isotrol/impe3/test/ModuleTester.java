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

package com.isotrol.impe3.test;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import net.sf.derquinsej.Proxies;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleStarter;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.core.modules.Provision;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * A module tester.
 * @param <T> specific module
 * @author Andres Rodriguez
 */
public final class ModuleTester<T extends Module> {
	private final ModuleDefinition<T> module;
	private final TestModel model;
	private StartedModule<T> started = null;
	private ModuleStarter<T> starter = null;

	/** Default Constructor. */
	ModuleTester(final Class<T> module, final TestModel model) {
		this.module = ModuleDefinition.of(module);
		this.model = model;
	}

	private void off() {
		Preconditions.checkState(started == null);
	}

	private void starting() {
		off();
		if (starter == null) {
			final ModuleStarter<T> mstarter = module.starter();
			mstarter.set(EngineMode.class, EngineMode.OFFLINE);
			mstarter.set(ContentTypes.class, model.getContentTypes());
			mstarter.set(Categories.class, model.getCategories());
			mstarter.set(FileLoader.class, model.getFileLoader());
			if (module.getModuleType() == ModuleType.COMPONENT) {
				mstarter.set(URIGenerator.class, model.getURIGenerator());
				mstarter.set(ContentLoader.class, model.getContentLoader());
			}
			this.starter = mstarter;
		}
	}

	/**
	 * Returns the module definition.
	 * @return The module definition.
	 */
	public ModuleDefinition<T> getModuleDefinition() {
		return module;
	}

	/**
	 * Supplies a dependency to the module.
	 * @param name Dependency name.
	 * @param value Dependency value.
	 * @return This object for method chaining.
	 * @throws NullPointerException if any of the arguments is null.
	 * @throws IllegalArgumentException if the dependency has incorrect name or value.
	 * @throws IllegalArgumentException if the dependency is an internal dependency.
	 * @throws IllegalStateException if the module is already on.
	 */
	public ModuleTester<T> put(String name, Object value) {
		starting();
		starter.put(name, value);
		return this;
	}

	/**
	 * Starts the module.
	 * @return The started module.
	 */
	public T start() {
		if (started == null) {
			starting();
			started = starter.start(null);
		}
		return started.getModule();
	}

	/**
	 * Loads and starts a module with one dependency.
	 * @param name1 First dependency name.
	 * @param value1 First dependency value.
	 * @return The started module.
	 */
	public T start(String name1, Object value1) {
		return put(name1, value1).start();
	}

	/**
	 * Loads and starts a module with two dependencies.
	 * @param name1 First dependency name.
	 * @param value1 First dependency value.
	 * @param name2 Second dependency name.
	 * @param value2 Second dependency value.
	 * @return The started module.
	 */
	public T start(String name1, Object value1, String name2, Object value2) {
		return put(name1, value1).put(name2, value2).start();
	}

	/**
	 * Loads and starts a module with three dependencies.
	 * @param name1 First dependency name.
	 * @param value1 First dependency value.
	 * @param name2 Second dependency name.
	 * @param value2 Second dependency value.
	 * @return The started module.
	 */
	public T start(String name1, Object value1, String name2, Object value2, String name3, Object value3) {
		return put(name1, value1).put(name2, value2).put(name3, value3).start();
	}

	/**
	 * Loads and starts a module with fake dependencies.
	 * @return The started module.
	 */
	public T fakeStart() {
		for (Dependency d : module.getDependencies().values()) {
			if (d.isRequired() && !d.isInternal()) {
				put(d.getBeanName(), Proxies.alwaysNull(d.getType()));
			}
		}
		return start();
	}

	private Provision getComponentProvision(final String name) {
		Preconditions.checkNotNull(name);
		Preconditions.checkState(ModuleType.COMPONENT == module.getModuleType());
		final Provision p = module.getProvisions().get(name);
		Preconditions.checkArgument(p != null);
		return p;
	}

	private <C extends Component> ComponentTester<C> getComponent(Class<C> type, Provision p) {
		Preconditions.checkNotNull(type);
		Preconditions.checkArgument(p != null);
		Preconditions.checkArgument(type.equals(p.getType()));
		start();
		final Object component = started.getProvision(p.getBeanName());
		return ComponentTester.of(type, type.cast(component), model);
	}

	/**
	 * get a component by name of class type
	 * @param <C> component class type
	 * @param type class type (must be not null
	 * @param name component name
	 * @return the component
	 */
	public <C extends Component> ComponentTester<C> getComponent(Class<C> type, final String name) {
		Preconditions.checkNotNull(type);
		final Provision p = getComponentProvision(name);
		return getComponent(type, p);
	}

	/**
	 * get a component by name as generic subclass of component
	 * @param name component name
	 * @return the component
	 */
	public ComponentTester<?> getComponent(final String name) {
		final Provision p = getComponentProvision(name);
		return getComponent(p.getType().asSubclass(Component.class), p);
	}

	/**
	 * Returns an action bean.
	 * @param name Action name.
	 * @return The requested action.
	 */
	public Object getAction(String name) {
		checkState(started != null);
		checkNotNull(name);
		checkState(ModuleType.COMPONENT == module.getModuleType());
		checkArgument(module.getActions().contains(name));
		final ComponentRequestContext crc = model.getContext();
		return started.getAction(RequestContexts.action(crc, name, crc.getComponentId(), crc.getRoute()));
	}

	/**
	 * Returns an action bean.
	 * @param actionType Action type.
	 * @param name Action name.
	 * @return The requested action.
	 */
	public <A> A getAction(Class<A> actionType, String name) {
		return actionType.cast(getAction(name));
	}

	/**
	 * Returns a connector by name.
	 * @param name Connector name
	 * @return The requested connector.
	 */
	public Object getConnector(final String name) {
		Preconditions.checkState(started != null);
		Preconditions.checkNotNull(name);
		Preconditions.checkState(ModuleType.CONNECTOR == module.getModuleType());
		return started.getProvision(name);
	}

	/**
	 * Returns a connector by name and type.
	 * @param type Connector type.
	 * @param name Connector name.
	 * @return The requested connector.
	 */
	public <C> C getConnector(Class<C> type, final String name) {
		Preconditions.checkNotNull(type);
		return type.cast(getConnector(name));
	}

}
