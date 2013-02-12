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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync;

/**
 * A view for wrong specified Modules.
 * 
 * @author Andrei Cojocaru
 *
 */
public class NotModulesView	extends AModuleStringView {

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView#getContentPanelTitle()
	 */
	/**
	 * <br/>
	 */
	@Override
	protected String getContentPanelHeading() {
		return getPmsMessages().headingIncorrectModules();
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #tryGetModules(com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync, 
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void tryGetModules(IModuleRegistryServiceAsync service, AsyncCallback<List<String>> callback) {
		getModulesService().getNotModule(callback);
	}

	@Override
	protected String getXDetailTemplate(ModelData model) {
		// TODO Auto-generated method stub
		return null;
	}

}
