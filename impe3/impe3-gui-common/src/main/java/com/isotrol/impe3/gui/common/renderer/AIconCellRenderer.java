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
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

/**
 * Abstract class for Renderers that show an icon in the grid cell .
 * 
 * @author Andrei Cojocaru
 * @param <M>
 *
 */
public abstract class AIconCellRenderer<M extends ModelData> implements GridCellRenderer<M> {

	/**
	 * Template for the rendered HTML.<br/>
	 */
	private static final String TEMPLATE =
		"<div style='text-align: center'><img src='img/grid-icons/${FILE}' /></div>";
	
	/**
	 * Pattern to replace in {@link #TEMPLATE}.<br/>
	 */
	private static final String PATTERN = "${FILE}";

	/**
	 * Replaces the {@link #PATTERN} with the passed value, in {@link #TEMPLATE}<br/>
	 * @param value
	 * @return
	 */
	protected final String apply(String value) {
		return TEMPLATE.replace(PATTERN, value);
	}
}
