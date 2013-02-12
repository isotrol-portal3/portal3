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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry;


import java.util.List;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.mreg.AbstractModuleDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;


/**
 * Generic master view for modules whose information is encoded in DTOs
 * (Connectors, Components and Invalids).<br/>
 * 
 * @author Manuel Ruiz
 * 
 * @param <M>	DTOModelData for the Module type managed by this widget.
 * @param <D>	DTO for the Module type managed by this widget.
 * 
 */
public abstract class AModuleDTOView<M extends AbstractModuleModelData<D>, D extends AbstractModuleDTO> 
	extends	AModuleView<M, D> {

	/**
	 * Cell renderer for "ID" grid column.<br/>
	 */
	private InformationCellRenderer idCellRenderer = null;
	
	/**
	 * Width for grid column <b>version</b>, in pixels.<br/>
	 */
	private static final int COLUMNVERSIONWIDTH = 80;

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleView
	 * #addSpecificColumnConfig(java.util.List)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void addSpecificColumnConfig(List<ColumnConfig> configs) {
		addColumnId(configs);
		addColumnVersion(configs);
	}
	
	/**
	 * Adds a "version" column config to the passed list.<br/>
	 */
	private void addColumnVersion(List<ColumnConfig> configs) {
		ColumnConfig column = new ColumnConfig();
		column.setId(AbstractModuleModelData.PROPERTY_VERSION);
		column.setHeader(getPmsMessages().labelVersion());
		column.setWidth(COLUMNVERSIONWIDTH);
		
		configs.add(column);
	}

	/**
	 * adds the ID column<br/>
	 * @param configs
	 */
	private void addColumnId(List<ColumnConfig> configs) {
		ColumnConfig config = new ColumnConfig(AbstractModuleModelData.PROPERTY_ID, 
				getMessages().columnHeaderId(), Constants.COLUMN_ICON_WIDTH);
		config.setSortable(false);
		config.setRenderer(idCellRenderer);
		configs.add(1, config);
	}
	
	/**
	 * Injects the ID cell renderer.
	 * @param idCellRenderer the idCellRenderer to set
	 */
	@Inject
	public void setIdCellRenderer(InformationCellRenderer idCellRenderer) {
		this.idCellRenderer = idCellRenderer;
	}
	
}
