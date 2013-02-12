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
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;


/**
 * Renders an "Information" icon with the passed property value as tooltip.<br/> In PMS, the passed property is usually
 * the ID of the DTO.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class InformationCellRenderer implements GridCellRenderer<ModelData> {

	/**
	 * Template for the rendered HTML.<br/>
	 */
	private static final String TEMPLATE = "<div style='text-align: center'><img src='img/grid-icons/info.gif' /></div>";

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer #render(com.extjs.gxt.ui.client.data.ModelData,
	 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int,
	 * com.extjs.gxt.ui.client.store.ListStore)
	 */
	/**
	 * <br/>
	 */
	public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex,
		ListStore<ModelData> store, Grid<ModelData> g) {
		Object value = model.get(property);
		if (value != null) {
			Html html = new Html(TEMPLATE);
			html.setTitle(value.toString());
			// tooltip autowidth not works properly in windows
			// html.setToolTip(value.toString());
			// html.getToolTip().setAutoWidth(true);
			return html;
		}
		return "";
	}

}
