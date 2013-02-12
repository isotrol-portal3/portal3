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

package com.isotrol.impe3.gui.common.util;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;

/**
 * Validates non null strings. A string is null if only contains zero or more spaces, 
 * tabs or such characters.
 * 
 * @author Andrei Cojocaru
 *
 */
public class NonEmptyStringValidator implements Validator {
	
	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * @param messages
	 */
	@Inject
	public NonEmptyStringValidator(GuiCommonMessages messages) {
		super();
		this.messages = messages;
	}
	
	/**
	 * <br>
	 */
	public String validate(Field<?> field, String value) {
		String res = null;
		if (Util.emptyString(value)) {
			res = messages.vmRequired();
		}
		return res;
	}
}
