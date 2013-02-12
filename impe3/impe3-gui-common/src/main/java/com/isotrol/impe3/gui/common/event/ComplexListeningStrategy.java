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
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * @author Andrei Cojocaru
 *
 */
public class ComplexListeningStrategy implements IComponentListeningStrategy {

	/**
	 * Wrapped logic for simple fields.<br/>
	 */
	private IComponentListeningStrategy simpleFieldsStrategy = new SimpleFieldListeningStrategy();
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.event.IComponentListeningStrategy
	 * #configListeners(com.extjs.gxt.ui.client.widget.Component, 
	 * com.extjs.gxt.ui.client.widget.button.Button, 
	 * com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel, 
	 * com.extjs.gxt.ui.client.event.Listener)
	 */
	/**
	 * <br/>
	 */
	public void configListeners(Component component, Listener<BaseEvent> lEnableSave) {
		if (component instanceof Field<?>) {
			simpleFieldsStrategy.configListeners((Field<?>) component, lEnableSave);
		} else {
			component.addListener(Events.Change, lEnableSave);
		}
	}

}
