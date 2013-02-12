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
package com.isotrol.impe3.pms.gui.client.error;

import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * Base class for all PMS message resolvers. 
 * It inherites the general message resolver logic from {@link SimpleErrorMessageResolver}, and
 * deals with the injected message bundle.
 * 
 * @author Andrei Cojocaru
 *
 */
public class APmsErrorMessageResolver extends SimpleErrorMessageResolver {

	/**
	 * PMS messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * @return the pmsMessages
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}
	
	/**
	 * Injects the pms messages bundle.
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
	
}
