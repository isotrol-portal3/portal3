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

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.gui.client.util.StateResolver;

/**
 * <b>State</b> cell renderer in grids that display rows of ModelData
 * that have a "state" property.<br/>
 * @author Andrei Cojocaru
 *
 */
public class  StateCellRenderer implements GridCellRenderer<ModelData> {

	/**
	 * Template for the resulting HTML.<br/>
	 */
	private static final String TEMPLATE_CELL = 
		"<div style='text-align: center;'><img src='img/state-icons/${ICON_NAME}' title='${TITLE}' /></div>";
	/**
	 * The pattern for the icon name in the {@link #TEMPLATE_CELL template}.<br/>
	 */
	private static final String PATTERN_ICON_NAME = "\\$\\{ICON_NAME\\}";
	/**
	 * Pattern for the icon title into the {@link #TEMPLATE_CELL template}.<br/>
	 */
	private static final String PATTERN_TITLE = "\\$\\{TITLE\\}";
	
	/*
	 * Injected dependences
	 */
	/**
	 * State-to-icon resolver.<br/>
	 */
	private StateResolver stateResolver = null;
	
	/**
	 * Default constructor.
	 */
	public StateCellRenderer() {}
	
	/**
	 * Injects the state resolver.<br/>
	 * @param stateResolver
	 */
	@Inject
	public void setStateResolver(StateResolver stateResolver) {
		this.stateResolver = stateResolver;
	}

	/**
	 * Renders an icon with the model title as a tooltip.<br/>
	 */
	public Object render(ModelData model, String property, ColumnData config,
			int rowIndex, int colIndex, ListStore<ModelData> store,
			Grid<ModelData> grid) {
		State state = model.get(Constants.PROPERTY_STATE);
		String title = stateResolver.getPrettyDescriptor(state);
		String icon = stateResolver.getIcon(state);
		return TEMPLATE_CELL
				.replaceAll(PATTERN_ICON_NAME, icon)
				.replaceAll(PATTERN_TITLE, title);
	}

}
