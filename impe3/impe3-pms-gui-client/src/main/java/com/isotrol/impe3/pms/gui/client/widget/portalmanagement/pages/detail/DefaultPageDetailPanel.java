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

import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;


/**
 * Panel to edit a default page
 * 
 * @author Manuel Ruiz
 * 
 */
public class DefaultPageDetailPanel extends PageDetailPanel {

	@Override
	protected void saveSpecificFieldsValues() {
		
		// when the page is the default error page, the name must be null
		PageTemplateDTO template = getPageTemplate();
		if(PageClass.ERROR.equals(template.getPageClass())) {
			template.setName(null);
		}
	}
	
	/** 
	 * Change value of page name
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.PageDetailPanel#displaySpecificFieldsValues(boolean)
	 */
	@Override
	protected void displaySpecificFieldsValues(boolean firstTime) {
		
		super.displaySpecificFieldsValues(firstTime);
		
		PageTemplateDTO pageTemplate = getPageTemplate();
		PageClass pageClass = pageTemplate.getPageClass();
		
		// name field:
		String name = null;
		if (pageClass.equals(PageClass.MAIN)) {
			name = getPmsMessages().nodeMainPage();
		} else if (pageClass.equals(PageClass.DEFAULT)) {
			name = getPmsMessages().nodeDefaultPage();
		} else if (pageClass.equals(PageClass.ERROR) && pageTemplate.getName() == null) {
			name = getPmsMessages().nodeDefaultErrorPage();
		} else {
			name = pageTemplate.getName();
		}
		getTfName().setValue(name);
	}

}
