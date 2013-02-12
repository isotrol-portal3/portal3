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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail;


import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * Panel with a Page details
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentPageDetailPanel extends AbstractSpecificPageDetailPanel {
	
	/**
	 * The bound Content Type data.<br/>
	 */
	private ContentTypeSelDTO content = null;

	/**
	 * Constructor
	 */
	public ContentPageDetailPanel() {}

	/**
	 * Inits the widget. Must be explicitly called after the properties are injected.
	 * @param template
	 * @param ctsDto
	 */
	public void init(PageTemplateDTO template, ContentTypeSelDTO ctsDto) {
		this.content = ctsDto;
		super.init(template);
	}
	
	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail
	 * .AbstractPageDetailPanel#saveSpecificFieldsValues()
	 */
	@Override
	protected void saveSpecificFieldsValues() {
		
		PageTemplateDTO template = getPageTemplate();
		template.setContentType(content);
	}

}
