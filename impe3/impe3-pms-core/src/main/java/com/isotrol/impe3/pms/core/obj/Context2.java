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


import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;


/**
 * Level 2 context. Includes level 1 plus connectors information.
 * @author Andres Rodriguez
 */
public abstract class Context2 extends Context1 {
	/**
	 * Copy constructor
	 * @param ctx Level 2 source context.
	 */
	Context2(Context1 ctx) {
		super(ctx);
	}

	/**
	 * Constructor
	 * @param ctx Source level 0 context.
	 * @param ia Information architecture.
	 */
	Context2(Context0 ctx, IA ia) {
		super(ctx, ia);
	}

	/**
	 * Returns a module selection DTO.
	 * @param definition Module definition.
	 * @return The requested dto.
	 * @throws NullPointerException if the definition is null.
	 * @throws IllegalArgumentException if the definition is not part of the registry.
	 */
	public final ModuleSelDTO getModuleSel(ModuleDefinition<?> module) {
		return getRegistry().getModuleSel(module, getLocale());
	}

	/**
	 * Checks whether a new instance of the provided module would be instantiable.
	 * @param module Module to check.
	 * @return True if it is instantiable.
	 */
	public abstract boolean isInstantiable(ModuleDefinition<?> module);

	/**
	 * Returns the dependencies templates for a module instance.
	 * @param md Module definition.
	 * @param mi Module instance.
	 * @return The requested templates.
	 */
	abstract List<DependencyTemplateDTO> getDependencies(ModuleDefinition<?> md, ModuleObject mi);

	/**
	 * Validate the dependencies of a module.
	 * @param md Module definition.
	 * @param mi Module instance (optional).
	 * @param deps Dependencies to validate.
	 * @return The validated dependencies to store in the database.
	 */
	public abstract Map<String, Provider> validateDependencies(ModuleDefinition<?> md, ModuleObject mi,
		List<DependencyDTO> deps) throws PMSException;

	/**
	 * Checks if a provided connector reference is of the correct type.
	 * @param type Service type.
	 * @param dto Provided DTO.
	 * @return The connector entity if everything is ok, or {@code null} if the reference is null.
	 * @throws PMSException if the connector is not found.
	 * @throws IllegalArgumentException if the reference is of an incorrect type.
	 */
	public abstract ConnectorObject checkProvided(Class<?> type, ProvidedDTO dto) throws PMSException;

	public abstract ConnectorObject loadConnector(UUID id) throws EntityNotFoundException;

	public final ConnectorObject loadConnector(String id) throws EntityNotFoundException {
		return loadConnector(NotFoundProviders.CONNECTOR.toUUID(id));
	}

	/**
	 * Returns whether a connector is used by this context.
	 * @param id Connector id.
	 * @return True if the connector is used by this context.
	 */
	public abstract boolean isConnectorUsed(UUID id) throws PMSException;

	/**
	 * Returns the portals.
	 * @return The portals.
	 */
	public abstract PortalsObject getPortals();

}
