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

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.event.SimpleFieldListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.ConfigurationPanel;
import com.isotrol.impe3.pms.gui.client.widget.DependencesPanel;

/**
 * Works for any Component needed in Pms
 * @author Andrei Cojocaru
 *
 */
public class PmsListeningStrategy implements IComponentListeningStrategy {

	/**
	 * Wrapped logic for simple fields.<br/>
	 */
	private IComponentListeningStrategy simpleFieldsStrategy = new SimpleFieldListeningStrategy();
	
	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.event.IComponentListeningStrategy
	 * #configListeners(com.extjs.gxt.ui.client.widget.Component, 
	 * 					com.extjs.gxt.ui.client.widget.button.Button, 
	 * 					com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel, 
	 * 					com.extjs.gxt.ui.client.event.Listener)
	 */
	public void configListeners(Component component, Listener<BaseEvent> enableSave) {
		if (component instanceof Field<?>) {
			// delegate to simple fields strategy
			simpleFieldsStrategy.configListeners((Field<?>)component, enableSave);
		} else if (component instanceof DependencesPanel) {
			component.addListener(Events.Select, enableSave);
			component.addListener(Events.SelectionChange, enableSave);
		} else if (component instanceof ConfigurationPanel) {
			component.addListener(Events.Select, enableSave);
			component.addListener(Events.OnKeyUp, enableSave);
			component.addListener(Events.Submit, enableSave);
			component.addListener(Events.Change, enableSave);
		} else {
			component.addListener(Events.Change, enableSave);
		}
	}
}
