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
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * Standard callback for operation:
 * {@link PagesService#delete(com.isotrol.impe3.pms.api.page.PageLoc)} <br/>
 * On success, shows an info message. On error, resolves the message according
 * to exception type and shows it in an error window.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class DeletePageCallback implements AsyncCallback<Void> {

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Pages error messages bundle.<br/>
	 */
	private PageErrorMessageResolver errorMessageResolver = null;

	/**
	 * Helper object<br/>
	 */
	private Util util = null;

	private ServiceErrorsProcessor errorProcessor;

	/**
	 * Constructor injected with all required dependences
	 * @param messages
	 * @param pmsMessages
	 * @param errorMessages
	 * @param u utilities helper
	 */
	@Inject
	public DeletePageCallback(GuiCommonMessages messages, PmsMessages pmsMessages,
			PageErrorMessageResolver emr, Util u, ServiceErrorsProcessor p) {
		super();
		this.pmsMessages = pmsMessages;
		this.errorMessageResolver = emr;
		this.util = u;
		this.errorProcessor = p;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
	 */
	/**
	 * <br/>
	 */
	public void onFailure(Throwable arg0) {
		util.unmask();
		errorProcessor.processError(arg0, errorMessageResolver, pmsMessages.msgErrorDeletePage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
	 */
	/**
	 * Displays a popup with that informs of the successful operation.<br/>
	 */
	public void onSuccess(Void arg0) {
		util.unmask();
		util.info(pmsMessages.msgSuccessDeletePage());
	}

}
