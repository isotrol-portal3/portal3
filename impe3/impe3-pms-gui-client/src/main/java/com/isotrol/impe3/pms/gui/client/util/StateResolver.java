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
package com.isotrol.impe3.pms.gui.client.util;

import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;

/**
 * Utility class that resolves icons per states
 * 
 * @author Andrei Cojocaru
 * @see State
 */
public final class StateResolver {

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Default constructor.
	 */
	public StateResolver() {}

	/**
	 * Returns the name of the graphic resource that represents the icon which
	 *         corresponds to the given state.
	 * 
	 * @param state
	 * @return name of the graphic resource that represents the icon which
	 *         corresponds to the given state.
	 * @see State
	 */
	public String getIcon(State state) {
		// default result
		String icon = pmsStyles.iconStateNew();
		if (state == State.MODIFIED) {
			icon = pmsStyles.iconStateModified();
		} else if (state == State.NEW) {
			icon = pmsStyles.iconStateNew();
		} else if (state == State.PUBLISHED) {
			icon = pmsStyles.iconStatePublished();
		} else if (state == State.UNPUBLISHED) {
			icon = pmsStyles.iconStateUnpublished();
		}
		return icon;
	}

	/**
	 * Returns a pretty descriptor as a display property.<br/>
	 * 
	 * @param state
	 * @return a pretty descriptor as a display property
	 */
	public String getPrettyDescriptor(State state) {

		String res = "";

		if (state == State.MODIFIED) {
			res = pmsMessages.stateModified();
		} else if (state == State.NEW) {
			res = pmsMessages.stateNew();
		} else if (state == State.PUBLISHED) {
			res = pmsMessages.statePublished();
		} else if (state == State.UNPUBLISHED) {
			res = pmsMessages.stateUnpublished();
		}

		return res;
	}

	/**
	 * Injects the PMS specific styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
	
	
}
