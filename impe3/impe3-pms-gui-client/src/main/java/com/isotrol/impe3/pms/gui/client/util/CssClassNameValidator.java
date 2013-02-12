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

package com.isotrol.impe3.pms.gui.client.util;


import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * Validates css class names. A string a valid css class name is matches with the regex -?[_a-zA-Z]+[_a-zA-Z0-9-]*
 * 
 * @author Manuel Ruiz
 * 
 */
public class CssClassNameValidator implements Validator {

	/**
	 * Generic messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * @param messages
	 */
	@Inject
	public CssClassNameValidator(PmsMessages messages) {
		super();
		this.pmsMessages = messages;
	}

	/**
	 * @see com.extjs.gxt.ui.client.widget.form.Validator#validate(com.extjs.gxt.ui.client.widget.form.Field,
	 * java.lang.String)
	 */
	public String validate(Field<?> field, String value) {
		String res = null;
		if (!value.matches("-?[_a-zA-Z]+[_a-zA-Z0-9- ]*")) {
			res = pmsMessages.msgErrorInvalidCssName();
		}
		return res;
	}
}
