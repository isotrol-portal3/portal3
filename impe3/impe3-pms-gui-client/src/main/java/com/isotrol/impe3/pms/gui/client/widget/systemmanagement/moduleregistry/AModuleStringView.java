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

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;

/**
 * A view (readonly widget) for Modules whose unique information returned by service is
 * their names as Strings (Not Found Modules and Not Modules).
 * 
 * @author Andrei Cojocaru
 *
 */
public abstract class AModuleStringView extends AModuleView<ModelData, String> {
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #addSpecificColumnConfig(java.util.List)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void addSpecificColumnConfig(List<ColumnConfig> configs) {
		// no specific cols to add
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView#storeModules(
	 * java.util.List)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void storeModules(List<String> modules) {
		List<ModelData> models = new LinkedList<ModelData>();
		for (String name : modules) {
			BaseModelData model = new BaseModelData();
			model.set(AbstractModuleModelData.PROPERTY_NAME, name);
			models.add(model);
		}
		ListStore<ModelData> store = getStore();
		store.removeAll();
		store.add(models);
	}
}
