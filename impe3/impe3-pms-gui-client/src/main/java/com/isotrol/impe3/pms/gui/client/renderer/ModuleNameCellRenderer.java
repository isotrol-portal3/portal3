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
package com.isotrol.impe3.pms.gui.client.renderer;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;

/**
 * Renderer for "module name" grid cell.
 * 
 * @author Andrei Cojocaru
 *
 */
public class ModuleNameCellRenderer implements GridCellRenderer<ModuleInstanceSelModelData> {
	/**
	 * Just returns the module name from the wrapped DTO.<br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer#render(com.extjs.gxt.ui.client.data.ModelData, 
	 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int, 
	 * com.extjs.gxt.ui.client.store.ListStore, com.extjs.gxt.ui.client.widget.grid.Grid)
	 */
	public Object render(ModuleInstanceSelModelData model, String property,
			ColumnData config, int rowIndex, int colIndex,
			ListStore<ModuleInstanceSelModelData> store,
			Grid<ModuleInstanceSelModelData> grid) {
		return model.getDTO().getModule().getName();
	}
}
