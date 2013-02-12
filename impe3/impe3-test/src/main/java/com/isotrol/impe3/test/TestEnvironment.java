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


import java.util.Locale;

import net.sf.derquinsej.Proxies;

import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.modules.ComponentProvision;
import com.isotrol.impe3.core.modules.ConnectorProvision;
import com.isotrol.impe3.core.modules.Dependency;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.core.modules.ModuleType;


/**
 * IMPE3 Test Environment.
 * @author Andres Rodriguez
 */
public abstract class TestEnvironment extends AbstractTestModel {
	/** Default constructor. */
	TestEnvironment(Portal portal, Device device, Locale locale) {
		super(portal, device, locale, null);
	}

	/** Returns the a repository buider. */
	public TestRepositoryBuilder getRepositoryBuilder() {
		return new TestRepositoryBuilder(this);
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
