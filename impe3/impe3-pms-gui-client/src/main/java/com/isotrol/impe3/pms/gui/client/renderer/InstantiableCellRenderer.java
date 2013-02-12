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
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.mreg.AbstractModuleDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * Grid cell renderer for "Instantiable" cells in grids of AbstractModuleModelData.<br/>
 * Renders an icon with a tooltip that indicates if module is instantiable (OK icon) or not (ERROR icon)
 * 
 * @author Andrei Cojocaru
 *
 */
public class InstantiableCellRenderer implements GridCellRenderer<DTOModelData<?>> {
	
	/**
	 * Template for grids icon cells.<br/>
	 * The pattern <b>${ICON}</b> represents the url of the icon, and must be relative to <b>img</b> folder. 
	 */
	private static final String TEMPLATE_ICON_CELL = 
		"<div style='text-align: center;'><img src='img/${ICON}' title='${TITLE}' /></div>";
	/**
	 * Pattern "icon" to replace in the template.<br/>
	 */
	private static final String PATTERN_ICON = "\\$\\{ICON\\}";
	/**
	 * Pattern "title" to be replaced in the template.<br/>
	 */
	private static final String PATTERN_TITLE = "\\$\\{TITLE\\}";
	
	/**
	 * Generic messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * @param messages the messages to set
	 */
	@Inject
	
	public void setMessages(PmsMessages messages) {
		this.pmsMessages = messages;
	}

	/** (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer#render(com.extjs.gxt.ui.client.data.ModelData,
	 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int, 
	 * com.extjs.gxt.ui.client.store.ListStore, com.extjs.gxt.ui.client.widget.grid.Grid)
	 */
	public Object render(DTOModelData<?> model, String property,
			ColumnData config, int rowIndex, int colIndex,
			ListStore<DTOModelData<?>> store, Grid<DTOModelData<?>> grid) {
		
		String icon = null;
		String title = null;
		if (((AbstractModuleDTO) model.getDTO()).isInstantiable()) {
			icon = Constants.OK_IMAGE;
			title = pmsMessages.titleModuleInstantiable();
		} else {
			icon = Constants.ERROR_IMAGE;
			title = pmsMessages.titleModuleNotInstantiable();
		}
		return TEMPLATE_ICON_CELL
				.replaceAll(PATTERN_ICON, icon)
				.replaceAll(PATTERN_TITLE, title);
	}
}
