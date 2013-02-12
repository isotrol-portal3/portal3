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
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategoryMappingModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * <b>Category</b> cell renderer for grids of {@link CategoryMappingModelData}. Renders the Category name, or a prompt
 * ("Select category") if no Category has been associated yet.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class CategoryCellRenderer implements GridCellRenderer<CategoryMappingModelData> {

	/** Template used to render the "Mapping" column. */
	private static final String MAPPING_COLUMN_TEMPLATE = "<span title='${PATH}'>${NAME}</span>";

	/** Pattern used to render the "Mapping" column.<br/> It's a regex, so careful with symbols. */
	private static final String PATH_COLUMN_PATTERN = "\\$\\{PATH\\}";

	/** Pattern used to render the "Mapping" column.<br/> It's a regex, so careful with symbols. */
	private static final String NAME_COLUMN_PATTERN = "\\$\\{NAME\\}";

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * @param pmsMessages
	 */
	@Inject
	public CategoryCellRenderer(PmsMessages pmsMessages) {
		super();
		this.pmsMessages = pmsMessages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.extjs.gxt.ui.client.widget.grid.GridCellRenderer #render(com.extjs.gxt.ui.client.data.ModelData,
	 * java.lang.String, com.extjs.gxt.ui.client.widget.grid.ColumnData, int, int,
	 * com.extjs.gxt.ui.client.store.ListStore)
	 */
	/**
	 * Renders the Category name, or a prompt ("Select category") if no Category has been associated yet.<br/>
	 */
	public Object render(CategoryMappingModelData model, String property, ColumnData config, int rowIndex,
		int colIndex, ListStore<CategoryMappingModelData> store, Grid<CategoryMappingModelData> grid) {
		CategorySelDTO csDto = model.getDTO().getCategory();
		if (csDto == null) {
			return pmsMessages.emptyValueCategory();
		} else {
			String path = model.getDTO().getDisplayPath();
			if (path == null) {
				path = "";
			}
			String name = csDto.getName();
			return MAPPING_COLUMN_TEMPLATE.replaceAll(PATH_COLUMN_PATTERN, path).replaceAll(NAME_COLUMN_PATTERN, name);
		}
	}

}
