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


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

import net.sf.derquinsej.uuid.UUIDGenerator;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ModuleRegistry;


/**
 * Level 0 context. Locale and non-publishable entities.
 * @author Andres Rodriguez
 */
public class Context0 {
	/** Environment. */
	private final UUID envId;
	/** Locale. */
	private final Locale locale;
	/** Routing domains. */
	private final RoutingDomainsObject domains;
	/** Source mappings. */
	private final SourceMappingsObject mappings;
	/** Devices. */
	private final DevicesObject devices;
	/** Module registry. */
	private final ModuleRegistry registry;
	/** UUID Generator. */
	private final UUIDGenerator uuidGenerator;

	/**
	 * Constructor
	 */
	public Context0(UUID envId, Locale locale, RoutingDomainsObject domains, SourceMappingsObject mappings,
		DevicesObject devices, ModuleRegistry registry, UUIDGenerator uuidGenerator) {
		this.envId = checkNotNull(envId);
		this.locale = checkNotNull(locale);
		this.domains = checkNotNull(domains);
		this.mappings = checkNotNull(mappings);
		this.devices = checkNotNull(devices);
		this.registry = checkNotNull(registry);
		this.uuidGenerator = checkNotNull(uuidGenerator);
	}

	/**
	 * Copy constructor
	 * @param ctx Source context.
	 */
	Context0(Context0 ctx) {
		this.envId = ctx.envId;
		this.locale = ctx.locale;
		this.domains = ctx.domains;
		this.mappings = ctx.mappings;
		this.devices = ctx.devices;
		this.registry = ctx.registry;
		this.uuidGenerator = ctx.uuidGenerator;
	}

	final UUID getEnvId() {
		return envId;
	}

	final UUID newUUID() {
		return uuidGenerator.get();
	}

	public final Locale getLocale() {
		return locale;
	}

	public final RoutingDomainsObject getDomains() {
		return domains;
	}

	public final SourceMappingsObject getMappings() {
		return mappings;
	}

	public final DevicesObject getDevices() {
		return devices;
	}

	ModuleRegistry getRegistry() {
		return registry;
	}

	public final URI getOfflineAbsoluteBase() {
		return domains.loadDefault().getDomain().getOffline().getAbsoluteBase();
	}

	/**
	 * Returns whether a content type is used by this context.
	 * @param id Content type id.
	 * @return True if the content type is used by this context.
	 */
	public boolean isContentTypeUsed(final UUID id) throws PMSException {
		return mappings.isContentTypeUsed(id);
	}

	/**
	 * Returns whether a category is used by this context.
	 * @param id Category id.
	 * @return True if the category is used by this context.
	 */
	public boolean isCategoryUsed(final UUID id) throws PMSException {
		return mappings.isCategoryUsed(id);
	}

}
