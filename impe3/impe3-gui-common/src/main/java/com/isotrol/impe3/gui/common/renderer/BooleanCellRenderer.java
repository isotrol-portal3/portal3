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

package com.isotrol.impe3.gui.common.renderer;


import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Grid cell renderer for boolean properties. The rendered element is a readonly checkbox.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class BooleanCellRenderer implements GridCellRenderer<ModelData> {

	/**
	 * Template for the rendered checkbox.<br/>
	 */
	private static final String TEMPLATE_DISABLED_CHECKBOX = "<input type='checkbox' disabled='disabled' ${CHECKED} />";

	/**
	 * HTML 'checked' attribute for the checkbox.<br/>
	 */
	private static final String PATTERN_CHECKED = "${CHECKED}";

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.grid
	 * .GridCellRenderer#render(com.extjs.gxt.ui.client.data.ModelData, java.lang.String, 
	 * com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int, 
	 * com.extjs.gxt.ui.client.store.ListStore, com.extjs.gxt.ui.client.widget.grid.Grid)
	 */
	/**
	 * 
	 */
	public Object render(ModelData model, String property, ColumnData config, 
		int rowIndex, int colIndex, ListStore<ModelData> store, Grid<ModelData> grid) {
		String checked = "";
		if ((Boolean) model.get(property)) {
			checked = "checked='checked'";
		}
		return TEMPLATE_DISABLED_CHECKBOX.replace(PATTERN_CHECKED, checked);
	}
}
