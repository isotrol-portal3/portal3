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



import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.gui.common.util.Constants;

/**
 * A typical window has a typical height (75% of the viewport height) 
 * and width (85% of its container) and is closable when pressing "Close" button or ESC key.
 * 
 * @author Andrei Cojocaru
 *
 */
public class TypicalWindow extends GenericWindow {

	/**
	 * Default constructor
	 */
	public TypicalWindow() {
		configThis();
	}

	/**
	 * @param widget
	 */
	public TypicalWindow(Widget widget) {
		super(widget);
		configThis();
	}
	
	/**
	 * Configures a typical window with a standard height, width and sets the closable flag.
	 */
	private void configThis() {
		setHeight((int) (Constants.SEVENTY_FIVE_PERCENT_FLOAT * RootPanel.get().getOffsetHeight()));
		setWidth(Constants.EIGHTY_FIVE_PERCENT);
		setClosable(true);
	}

	/**
	 * Sets the title text for the panel as HTML.
	 * @param dateTimePickerText The title text.
	 */
	public void setHeading(String text) {
		this.setHeadingText(text);
	}
}
