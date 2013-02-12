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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component;


import java.util.List;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ModuleInstanceSelModelData;
import com.isotrol.impe3.pms.gui.client.renderer.CorrectnessCellRenderer;


/**
 * @author Manuel Ruiz
 * 
 */
public class AllOwnComponentManagement extends AOwnComponentManagement {
	
	/**
	 * Width in px for <b>correctness</b> column.<br/>
	 */
	private static final int COLUMN_CORRECTNESS_WIDTH = Constants.COLUMN_ICON_WIDTH;
	
	/**
	 * Correctness renderer
	 */
	private CorrectnessCellRenderer correctnessRenderer = null;

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.AOwnComponentManagement#callGetComponents(java.lang.String,
	 * com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void callGetComponents(String portalId, AsyncCallback<List<ModuleInstanceSelDTO>> callback) {
		getComponentsService().getComponents(portalId, callback);
	}

	/**
	 * Adds the <b>New Component</b> toolbar item.<br/>
	 * 
	 * @see AOwnComponentManagement#addSpecificToolItems(ToolBar);
	 */
	@Override
	protected void addSpecificToolItems(ToolBar toolbar) {
		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				tryGetComponentModules();
			}
		};
		Button ttiAdd = getButtonsSupport().createGenericButton(getMessages().labelAdd(), getStyles().iNew(),
			listener);
		toolbar.insert(ttiAdd, 0);
		toolbar.insert(new SeparatorToolItem(), 1);
	}
	
	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.AOwnComponentManagement#addSpecificColumns(com.extjs.gxt.ui.client.widget.grid.ColumnModel)
	 */
	@Override
	protected void addSpecificColumns(List<ColumnConfig> configs) {
		// correctness icon
		ColumnConfig column = new ColumnConfig();
		column.setRenderer(correctnessRenderer);
		column.setId(ModuleInstanceSelModelData.PROPERTY_CORRECTNESS);
		column.setHeader(getPmsMessages().columnHeaderValid());
		column.setWidth(COLUMN_CORRECTNESS_WIDTH);
		configs.add(3, column);
	}
	
	/**
	 * @param correctnessRenderer the correctnessRenderer to set
	 */
	@Inject
	public void setCorrectnessRenderer(CorrectnessCellRenderer correctnessRenderer) {
		this.correctnessRenderer = correctnessRenderer;
	}
}
