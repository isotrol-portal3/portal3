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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractWithCategoryContentPageManagement;


/**
 * Management of Content Type Pages associated to Categories.
 * 
 * @author Manuel Ruiz
 * 
 */
public class CategoryContentPagesManagement extends AbstractWithCategoryContentPageManagement {

	/**
	 * Constructor
	 */
	public CategoryContentPagesManagement() {}
	
	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractWithCategoryContentPageManagement
	 * #getManagedPageClass()
	 */
	@Override
	protected PageClass getManagedPageClass() {
		return PageClass.CONTENT;
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractWithCategoryContentPageManagement
	 * #doCallGetPages(java.lang.String, com.google.gwt.user.client.rpc.AsyncCallback)
	 */
	@Override
	protected void doCallGetPages(String contentId, AsyncCallback<CategoryPagesDTO> callback) {
		getPagesService().getCategoryContentPages(getPortalPagesLoc(), contentId, callback);
	}

}
