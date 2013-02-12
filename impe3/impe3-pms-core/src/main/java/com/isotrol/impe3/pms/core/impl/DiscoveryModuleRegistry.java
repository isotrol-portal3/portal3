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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.core.ModuleRegistry;


/**
 * Classpath discovery-based module registry factory bean.
 * @author Andres Rodriguez.
 */
@Component
public final class DiscoveryModuleRegistry implements FactoryBean {
	private static final String PATH = "META-INF/impe3.modules";
	private static final String ENCODING = "UTF-8";
	private static final char COMMENT = '#';

	private static void loadResource(URL url, Set<String> names) {
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), ENCODING));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					final int comment = line.indexOf(COMMENT);
					if (comment >= 0) {
						line = line.substring(0, comment);
					}
					line = line.trim();
					if (line.length() > 0) {
						names.add(line);
					}
				}
			}
			finally {
				reader.close();
			}
		} catch (IOException e) {
			// nothing, we did our best
		}
	}

	private static Set<String> loadNames(final ClassLoader loader) {
		final Set<String> names = Sets.newHashSet();
		try {
			final Enumeration<URL> urls = loader.getResources(PATH);
			while (urls.hasMoreElements()) {
				loadResource(urls.nextElement(), names);
			}
		} catch (IOException e) {
			// nothing, we did our best
		}
		return names;
	}

	/** Module registry. */
	private final ModuleRegistry registry;

	/**
	 * Default constructor.
	 */
	public DiscoveryModuleRegistry() {
		final Set<String> notFound = Sets.newHashSet();
		final Set<Class<?>> notModule = Sets.newHashSet();
		final Set<ModuleDefinition<?>> dfns = Sets.newHashSet();
		for (String name : loadNames(getClass().getClassLoader())) {
			try {
				Class<?> klass = Class.forName(name);
				if (!Module.class.isAssignableFrom(klass)) {
					notModule.add(klass);
				} else {
					Class<? extends Module> type = klass.asSubclass(Module.class);
					dfns.add(ModuleDefinition.getSafe(type));
				}
			} catch (ClassNotFoundException e) {
				notFound.add(name);
			}
		}
		this.registry = new DefaultModuleRegistry(dfns, notFound, notModule);
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return registry;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return ModuleRegistry.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}
}
