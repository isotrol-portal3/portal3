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


import java.util.UUID;

import net.sf.derquinsej.Proxies;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.core.modules.ConnectorProvision;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.core.support.AbstractFileLoader;


/**
 * IMPE3 Test Information Architecture.
 * @author Andres Rodriguez
 */
public final class TestContext extends AbstractTestModel {
	private static final TestContext EMPTY = new TestIABuilder().get().get();

	private final FileLoader fileLoader;
	private final URIGenerator uriGenerator;

	public static TestContext empty() {
		return EMPTY;
	}

	/**
	 * Tries to loads and start a module in a empty context with fake dependencies.
	 * @param module Module interface.
	 * @return The started module.
	 */
	public static <T extends Module> T fakeStart(Class<T> module) {
		final ModuleTester<T> tester = empty().getModule(module);
		return tester.fakeStart();
	}

	/**
	 * Default constructor.
	 * @param builder Builder.
	 */
	TestContext(TestContextBuilder builder) {
		super(builder.getPortal(), builder.getDevice(), builder.getLocale(), builder.getNodeRepositories());
		final ImmutableMap<UUID, FileData> files = builder.getFiles();
		this.fileLoader = new AbstractFileLoader() {
			@Override
			protected FileData doLoad(UUID id) {
				if (!files.containsKey(id)) {
					throw new IllegalArgumentException();
				}
				return files.get(id);
			}
		};
		this.uriGenerator = new TestURIGenerator(getPortal());
	}

	/** Returns the file loader. */
	public FileLoader getFileLoader() {
		return fileLoader;
	}

	/** Returns the URI generator. */
	public URIGenerator getURIGenerator() {
		return uriGenerator;
	}

	/**
	 * Create a module tester for a type of module
	 * @param <T> module class type
	 * @param module module class
	 * @return module tester
	 */
	public <T extends Module> ModuleTester<T> getModule(Class<T> module) {
		return new ModuleTester<T>(module, this);
	}

	/**
	 * Tries to autowire a module and access its provisions.
	 * @param module Module class
	 */
	public <T extends Module> void autowireModule(Class<T> module) {
		final ModuleTester<T> tester = getModule(module);
		final ModuleDefinition<T> md = tester.getModuleDefinition();
		// If invalid, nothing to do
		if (md.getModuleType() == ModuleType.INVALID) {
			return;
		}
		// Provide null dependencies.
		for (Dependency d : md.getExternalDependencies().values()) {
			tester.put(d.getBeanName(), Proxies.alwaysNull(d.getType()));
		}
		// Provide null configuration
		if (md.isConfigurationDependencyRequired()) {
			tester.put(md.getConfigurationBeanName(), Proxies.alwaysNull(md.getConfiguration().getType()));
		}
		// Start module
		tester.start();
		// If a connector, we access services...
		if (md.getModuleType() == ModuleType.CONNECTOR) {
			for (ConnectorProvision p : md.getConnectorProvisions().values()) {
				try {
					tester.getConnector(p.getBeanName());
				} catch (RuntimeException e) {
					System.err.println("Unable to access connector " + p.getBeanName());
					throw e;
				}
			}
		} else {
			// Components
			for (ComponentProvision p : md.getComponentProvisions().values()) {
				try {
					tester.getComponent(p.getBeanName()).getComponent();
				} catch (RuntimeException e) {
					System.err.println("Unable to access component " + p.getBeanName());
					throw e;
				}
			}
			// Actions
			for (String action : md.getActions()) {
				try {
					tester.getAction(action);
				} catch (RuntimeException e) {
					System.err.println("Unable to access action " + action);
					throw e;
				}
			}
		}
	}

}
