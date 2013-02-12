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
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeMappingModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * <b>Content Type</b> cell renderer for grids of
 * {@link ContentTypeMappingModelData}. Renders the Content Type name, or a
 * prompt ("Select content type") if no Content Type has been associated.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class ContentTypeCellRenderer implements GridCellRenderer<ContentTypeMappingModelData> {

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * @param pmsMessages
	 */
	@Inject
	public ContentTypeCellRenderer(PmsMessages pmsMessages) {
		super();
		this.pmsMessages = pmsMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer
	 * #render(com.extjs.gxt.ui.client.data.ModelData, java.lang.String,
	 * com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int,
	 * com.extjs.gxt.ui.client.store.ListStore)
	 */
	/**
	 * Renders the Content Type name, or a prompt ("Select content type") if no
	 * Content Type has been associated.<br/>
	 */
	public Object render(ContentTypeMappingModelData model, String property,
			ColumnData config, int rowIndex, int colIndex,
			ListStore<ContentTypeMappingModelData> store,
			Grid<ContentTypeMappingModelData> grid) {
		ContentTypeSelDTO ctSelDto = model.getDTO().getContentType();
		if (ctSelDto == null) {
			return pmsMessages.emptyValueContentType();
		}
		return ctSelDto.getName();
	}

}
