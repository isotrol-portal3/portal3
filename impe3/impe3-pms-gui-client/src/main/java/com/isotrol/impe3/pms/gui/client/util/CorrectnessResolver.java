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
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;

/**
 * Utility class that resolves icons per correctness
 * 
 * @author Manuel Ruiz
 * @see Correctness
 */
public final class CorrectnessResolver {

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
	public CorrectnessResolver() {}

	/**
	 * Returns the name of the graphic resource that represents the icon which
	 *         corresponds to the given correctness property.
	 * 
	 * @param correctness
	 * @return name of the graphic resource that represents the icon which
	 *         corresponds to the given correctness property.
	 * @see Correctness
	 */
	public String getIcon(Correctness correctness) {
		// default result
		String icon = pmsStyles.iconStateNew();
		if (correctness == Correctness.ERROR) {
			icon = pmsStyles.iconError();
		} else if (correctness == Correctness.WARN) {
			icon = pmsStyles.iconWarning();
		} else if (correctness == Correctness.OK) {
			icon = pmsStyles.iconOk();
		}
		return icon;
	}

	/**
	 * Returns a pretty descriptor as a display property.<br/>
	 * 
	 * @param correctness
	 * @return a pretty descriptor as a display property
	 */
	public String getPrettyDescriptor(Correctness correctness) {

		String res = "";

		if (correctness == Correctness.ERROR) {
			res = pmsMessages.correctnessError();
		} else if (correctness == Correctness.WARN) {
			res = pmsMessages.correctnessWarning();
		} else if (correctness == Correctness.OK) {
			res = pmsMessages.correctnessOk();
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
