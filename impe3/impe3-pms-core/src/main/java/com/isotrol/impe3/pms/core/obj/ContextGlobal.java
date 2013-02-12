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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;


/**
 * Global context: Level 2 context, using global connectors.
 * @author Andres Rodriguez
 */
public final class ContextGlobal extends Context2 {
	/** Connectors. */
	private final ConnectorsObject connectors;
	/** Portal loader. */
	private final PortalLoader portalLoader;
	/** Portals. */
	private PortalsObject portals = null;

	/**
	 * Constructor
	 * @param ctx Level 1 source context.
	 * @param connectors Connectors.
	 */
	public ContextGlobal(Context1 ctx, ConnectorsObject connectors, PortalLoader portalLoader) {
		super(ctx);
		this.connectors = checkNotNull(connectors);
		this.portalLoader = checkNotNull(portalLoader);
	}

	public final ConnectorsObject getConnectors() {
		return connectors;
	}

	final PortalLoader getPortalLoader() {
		return portalLoader;
	}

	/**
	 * Returns the dependencies templates for a module instance.
	 * @param md Module definition.
	 * @param mi Module instance.
	 * @return The requested templates.
	 */
	final List<DependencyTemplateDTO> getDependencies(ModuleDefinition<?> md, ModuleObject mi) {
		return connectors.getDependencies(md, mi, getLocale());
	}

	public ContextPortal toPortal(UUID portalId) throws PMSException {
		return ContextPortal.of(this, portalId);
	}

	public ContextPortal toPortal(String portalId) throws PMSException {
		return ContextPortal.of(this, NotFoundProviders.PORTAL.toUUID(portalId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.obj.Context2#isInstantiable(com.isotrol.impe3.core.modules.ModuleDefinition)
	 */
	@Override
	public boolean isInstantiable(ModuleDefinition<?> module) {
		return connectors.isInstantiable(module);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.obj.Context2#validateDependencies(com.isotrol.impe3.core.modules.ModuleDefinition,
	 * com.isotrol.impe3.pms.core.obj.ModuleObject, java.util.List)
	 */
	@Override
	public Map<String, Provider> validateDependencies(ModuleDefinition<?> md, ModuleObject mi, List<DependencyDTO> deps)
		throws PMSException {
		return connectors.validateDependencies(md, mi, deps);
	}

	@Override
	public ConnectorObject checkProvided(Class<?> type, ProvidedDTO dto) throws PMSException {
		return connectors.checkProvided(type, dto);
	}

	@Override
	public ConnectorObject loadConnector(UUID id) throws EntityNotFoundException {
		return connectors.load(id);
	}

	@Override
	public boolean isCategoryUsed(UUID id) throws PMSException {
		return super.isCategoryUsed(id) || connectors.isCategoryUsed(id);
	}

	@Override
	public boolean isContentTypeUsed(UUID id) throws PMSException {
		return super.isContentTypeUsed(id) || connectors.isContentTypeUsed(id);
	}

	@Override
	public boolean isConnectorUsed(UUID id) {
		return connectors.isConnectorUsed(id);
	}

	public ModuleInstanceTemplateDTO getConnectorTemplate(String id) throws PMSException {
		return loadConnector(id).toTemplateDTO(this);
	}

	public List<ConnectorModuleDTO> getConnectorModules() throws PMSException{
		return connectors.filter(getRegistry().getConnectors(getLocale()));
	}

	public List<ModuleInstanceSelDTO> getConnectorsSel() {
		return connectors.map2sel(getLocale());
	}

	public List<ModuleInstanceSelDTO> getConnectorsSel(Correctness correctness) {
		return connectors.map2sel(getLocale(), correctness);
	}

	/**
	 * Returns the possible providers for an specified interfaces.
	 * @param type Required interface.
	 * @return The possible providers.
	 */
	public Set<Provider> getPossibleProviders(final Class<?> type) {
		return connectors.getPossibleProviders(type);
	}

	@Override
	public PortalsObject getPortals() {
		if (portals == null) {
			portals = portalLoader.loadOffline(getEnvId());
		}
		return portals;
	}

}
