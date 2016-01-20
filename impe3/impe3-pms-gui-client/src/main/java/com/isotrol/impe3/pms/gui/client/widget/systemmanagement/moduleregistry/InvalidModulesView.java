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


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.mreg.InvalidModuleDTO;
import com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.InvalidModuleModelData;


/**
 * Shows the invalid modules list
 * @author Manuel Ruiz
 * 
 */
public class InvalidModulesView extends AModuleDTOView<InvalidModuleModelData, InvalidModuleDTO> {
	
	private static final String TEMPLATE_TEMPLATE = 
			"<p style='margin-left:25px;color:red'>${ERROR}</p>"
		+	"<p style='margin-left:25px'><b>Copyright: </b>${COPYRIGHT}</p>"
		+	"<p style='margin-left:25px'><b>Descripción: </b>${DESCRIPTION}";	

	/**
	 * "Error" pattern, to replace in the template.<br/>
	 */
	private static final String PATTERN_ERROR = "\\$\\{ERROR\\}";
	/**
	 * "Copyright" pattern, to replace in the template.<br/>
	 */
	private static final String PATTERN_COPYRIGHT = "\\$\\{COPYRIGHT\\}";
	/**
	 * "Description" pattern, to replace in the template.<br/>
	 */
	private static final String PATTERN_DESCRIPTION = "\\$\\{DESCRIPTION\\}";

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #tryGetModules(com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync,
	 * 				  com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void tryGetModules(IModuleRegistryServiceAsync service, AsyncCallback<List<InvalidModuleDTO>> callback) {
		service.getInvalids(callback);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #storeModules(java.util.List)
	 */
	@Override
	protected void storeModules(List<InvalidModuleDTO> modules) {

		List<InvalidModuleModelData> invalidsModules = new LinkedList<InvalidModuleModelData>();
		for (InvalidModuleDTO dto : modules) {
			invalidsModules.add(new InvalidModuleModelData(dto));
		}

		getStore().add(invalidsModules);
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #getContentPanelHeading()
	 */
	@Override
	protected String getContentPanelHeading() {
		return getPmsMessages().headingInvalidModules();
	}

	@Override
	protected String getXDetailTemplate(InvalidModuleModelData model) {

		InvalidModuleDTO dto = model.getDTO();
		ContentPanel detail = new ContentPanel();
		detail.setFrame(true);
		detail.setHeadingText(dto.getName());

		String template = TEMPLATE_TEMPLATE
			.replaceAll(PATTERN_ERROR, dto.getError())
			.replaceAll(PATTERN_COPYRIGHT, dto.getCopyright())
			.replaceAll(PATTERN_DESCRIPTION, dto.getDescription());
		
		return template;
	}

}
