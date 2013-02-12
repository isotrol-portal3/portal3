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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry;


import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.mreg.ComponentModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ProvidedComponentDTO;
import com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ComponentModuleModelData;


/**
 * Shows the components packages list
 * 
 * @author Manuel Ruiz
 * 
 */
public class ComponentsPackagesView extends AValidModuleView<ComponentModuleModelData, ComponentModuleDTO> {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #tryGetModules(com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void tryGetModules(IModuleRegistryServiceAsync service, AsyncCallback<List<ComponentModuleDTO>> callback) {
		service.getComponents(callback);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #storeModules(java.util.List)
	 */
	@Override
	protected void storeModules(List<ComponentModuleDTO> modules) {

		List<ComponentModuleModelData> componentsModules = new LinkedList<ComponentModuleModelData>();
		for (ComponentModuleDTO dto : modules) {
			componentsModules.add(new ComponentModuleModelData(dto));
		}

		getStore().add(componentsModules);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #getContentPanelHeading()
	 */
	@Override
	protected String getContentPanelHeading() {
		return getPmsMessages().headingComponentsPackages();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AValidModuleView#getLabelModulesMessage()
	 */
	@Override
	protected String getLabelModulesMessage() {
		return getPmsMessages().labelExportedComponents();
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ValidModuleDetailPanel#getModules(com.isotrol.impe3.pms.api.mreg.AbstractValidModuleDTO)
	 */
	@Override
	protected String getModules(ComponentModuleDTO dto) {
		// compute modules replacement:
		String modules = "";
		for (ProvidedComponentDTO pc : dto.getComponents()) {
			String module = getModuleTemplate().replaceAll(getPatternModuleDesc(), pc.getDescription()).replaceAll(
				getPatternModuleName(), pc.getType());
			modules = modules.concat(module);
		}
		return modules;
	}
}
