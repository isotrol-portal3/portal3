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

package com.isotrol.impe3.gui.common.renderer;


import java.util.Date;

import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;


/**
 * Formats dates with the pattern 'EEEE, dd/MM/yyyy'.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class SimpleDatePropertyEditor implements PropertyEditor<Date> {
	
	/**
	 * The date formatter.<br/>
	 */
	private DateTimeFormat formatter = null;
	
	/**
	 * Constructor injection of messages bundle dependence
	 */
	@Inject
	public SimpleDatePropertyEditor(GuiCommonMessages messages) {
		formatter = DateTimeFormat.getFormat(messages.humanDateFormat());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.extjs.gxt.ui.client.widget.form.PropertyEditor#convertStringValue(java.lang.String)
	 */
	/**
	 * <br/>
	 */
	public Date convertStringValue(String value) {
		return formatter.parse(value);
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.form.PropertyEditor#getStringValue(java.lang.Object)
	 */
	/**
	 * <br/>
	 */
	public String getStringValue(Date value) {
		return formatter.format(value);
	};
}
