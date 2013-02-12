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

package com.isotrol.impe3.gui.common.widget;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.Widget;

/**
 * A Window configured as modal, framed and not closable. Must be used as base class for all window objects
 * in the app.<br/>
 * When set as closable through {@link #setClosable(boolean)}, it is also configured to be closed when 
 * pressing ESCAPE key.
 * 
 * @author Andrei Cojocaru
 */
public class GenericWindow extends Window {

	/**
	 * Default constructor.
	 */
	public GenericWindow() {
		setModal(true);
		setShadow(true);
		setFrame(true);
		
		setClosable(false);
	}
	
	/**
	 * A generic window, initialized its content.
	 * @param widget
	 */
	public GenericWindow(Widget widget) {
		this();
		setLayout(new FitLayout());
		add(widget);
	}
	
	@Override
	public void setClosable(boolean closable) {
		super.setClosable(closable);
		setOnEsc(closable);
	}
	
}
