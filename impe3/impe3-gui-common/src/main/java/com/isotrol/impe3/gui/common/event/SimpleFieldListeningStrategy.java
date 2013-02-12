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

package com.isotrol.impe3.gui.common.event;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * @author Andrei Cojocaru
 * 
 */
public class SimpleFieldListeningStrategy implements IComponentListeningStrategy {

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.merlin3.gui.common.event.pms.gui.common.util.event.IComponentListeningStrategy
	 * #configListeners(com.extjs.gxt.ui.client.widget.Component, com.extjs.gxt.ui.client.widget.button.Button,
	 * com.isotrol.impe3.gui.common.util.component.IDetailPanel)
	 */
	public void configListeners(final Component component, Listener<BaseEvent> listener) {
		if (component instanceof ComboBox<?>) { // deps
			component.addListener(Events.Select, listener);
			component.addListener(Events.SelectionChange, listener);
		} else if (component instanceof FileUploadField) {
			component.addListener(Events.Submit, listener);
		} else if (component instanceof TextField<?>) {
			component.addListener(Events.OnKeyUp, listener);
			component.addListener(Events.Change, listener);
		} else {
			component.addListener(Events.Change, listener);
		}
	}

}
