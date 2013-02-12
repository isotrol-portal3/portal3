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
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageSelDTO;

/**
 * Widget that manages the Error Pages
 * 
 * @author Andrei Cojocaru
 *
 */
public class ErrorPagesManagement extends AbstractGridPagesManagement {

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractGridPagesManagement#getManagedPageClass()
	 */
	@Override
	protected PageClass getManagedPageClass() {
		return PageClass.ERROR;
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractPagesManagement#getPageClass()
	 */
	@Override
	protected PageClass getPageClass() {
		return PageClass.ERROR;
	}

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement#tryGetPages()
	 */
	@Override
	protected void tryGetPages() {
		getUtil().mask(getPmsMessages().mskErrorPages());
		
		AsyncCallback<List<Inherited<PageSelDTO>>> callback = new AsyncCallback<List<Inherited<PageSelDTO>>>() {
			public void onSuccess(List<Inherited<PageSelDTO>> arg0) {
				storePages(arg0);
				getUtil().unmask();
			}
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
					arg0, 
					getEmrPages(), 
					getPmsMessages().msgErrorRetrieveErrorPages());
			}
		};
		getPagesService().getErrorPages(getPortalPagesLoc(), callback);
	}

}
