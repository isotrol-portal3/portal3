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

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageSelDTO;

/**
 * Manages the special pages
 * 
 * @author Manuel Ruiz
 * 
 */
public class SpecialPagesManagement extends AbstractGridPagesManagement {

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#tryGetPages()
	 */
	@Override
	protected final void tryGetPages() {
		getUtil().mask(getPmsMessages().mskSpecialPages());
		
		AsyncCallback<List<Inherited<PageSelDTO>>> callback = new AsyncCallback<List<Inherited<PageSelDTO>>>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
					arg0, 
					getEmrPages(), 
					getPmsMessages().msgErrorRetrieveSpecialPages());
			}

			public void onSuccess(List<Inherited<PageSelDTO>> pages) {
				storePages(pages);
				getUtil().unmask();
			}
		};

		getPagesService().getSpecialPages(
			super.getPortalPagesLoc(),
			callback);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#getPageClass()
	 */
	@Override
	protected final PageClass getPageClass() {
		return PageClass.SPECIAL;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#getManagedPageClass()
	 */
	@Override
	protected final PageClass getManagedPageClass() {
		return PageClass.SPECIAL;
	}

}
