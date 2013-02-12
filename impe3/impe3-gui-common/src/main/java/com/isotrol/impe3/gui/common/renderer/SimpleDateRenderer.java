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

import java.util.Date;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;

/**
 * Renders only the date, with the format "dd/MM/yyyy"
 * 
 * @author Andrei Cojocaru
 *
 */
public class SimpleDateRenderer implements GridCellRenderer<ModelData> {

	/**
	 * The GWT formatter.<br/>
	 */
	private DateTimeFormat formatter = null;
	
	/**
	 * GRM messages 
	 */
	@Inject
	public SimpleDateRenderer(GuiCommonMessages messages) {
		this.formatter = DateTimeFormat.getFormat(messages.humanShortDateFormat());
	}
	
	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer#render(com.extjs.gxt.ui.client.data.ModelData, 
	 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int, 
	 * com.extjs.gxt.ui.client.store.ListStore, com.extjs.gxt.ui.client.widget.grid.Grid)
	 */
	/**
	 * <br/>
	 */
	public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex,
		ListStore<ModelData> store, Grid<ModelData> grid) {
		Date date = model.get(property);
		return formatter.format(date);
	}
	
}
