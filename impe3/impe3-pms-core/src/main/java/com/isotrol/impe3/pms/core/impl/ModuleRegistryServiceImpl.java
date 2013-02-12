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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Ordering.natural;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleRegistryService;
import com.isotrol.impe3.pms.core.ModuleRegistry;
import com.isotrol.impe3.pms.core.PMSContext;


/**
 * Implementation of ModuleRegistryService.
 * @author Andres Rodriguez.
 */
@Component("moduleRegistryService")
public final class ModuleRegistryServiceImpl implements ModuleRegistryService {
	/** Module registry. */
	private final ModuleRegistry registry;
	/** PMS Context. */
	private final PMSContext context;

	/** Default constructor. */
	@Autowired
	public ModuleRegistryServiceImpl(final ModuleRegistry registry, final PMSContext context) {
		this.registry = checkNotNull(registry, "A module registry must be provided");
		this.context = checkNotNull(context, "The PMS context must be provided");
	}

	/**
	 * @see com.isotrol.impe3.pms.api.mreg.ModuleRegistryService#getComponents()
	 */
	public List<ComponentModuleDTO> getComponents() {
		return newArrayList(registry.getComponents(context.getLocale()));
	}

	/**
	 * @see com.isotrol.impe3.pms.api.mreg.ModuleRegistryService#getConnectors()
	 */
	public List<ConnectorModuleDTO> getConnectors() {
		return newArrayList(registry.getConnectors(context.getLocale()));
	}

	/**
	 * @see com.isotrol.impe3.pms.api.mreg.ModuleRegistryService#getInvalids()
	 */
	public List<InvalidModuleDTO> getInvalids() {
		return newArrayList(registry.getInvalids(context.getLocale()));
	}

	/**
	 * @see com.isotrol.impe3.pms.api.mreg.ModuleRegistryService#getNotFound()
	 */
	public List<String> getNotFound() {
		return natural().sortedCopy(registry.getNotFound());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.mreg.ModuleRegistryService#getNotModule()
	 */
	public List<String> getNotModule() {
		return natural().sortedCopy(transform(registry.getNotModule(), new Function<Class<?>, String>() {
			public String apply(Class<?> from) {
				return from.getName();
			}
		}));
	}

}
