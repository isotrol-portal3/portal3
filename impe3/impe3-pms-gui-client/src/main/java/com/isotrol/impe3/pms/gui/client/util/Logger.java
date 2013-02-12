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

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.ui.Label;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;

/**
 * 
 * 
 * @author Andrei Cojocaru
 * 
 */
public final class Logger {
	
	
	private boolean configured = false;
	
	private String buffer = null;
	
	/**
	 * PMS specific pmsSettings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;

	/**
	 * LayoutContainer en el que se escriben los mensajes de log.
	 */
	private LayoutContainer logPanel = null;

	/**
	 * Default constructor.
	 */
	public Logger() {};

	/**
	 * configura el Logger fijando el panel en el que se van a registrar los
	 * mensajes
	 * 
	 * @param logPanel
	 */
	public void setLogPanel(LayoutContainer logPanel) {
		this.logPanel = logPanel;
		configured = true;
		if(buffer != null) {
			log(buffer);
			buffer = null;
		}
	}

	/**
	 * registra un mensaje en el {@link logPanel}
	 * 
	 * @param message
	 */
	public void log(String message) {
		if(Boolean.valueOf(pmsSettings.isDebugMode())) {
			if(configured) {
				logPanel.add(new Label(message));
				logPanel.layout();
			} else {
				if(buffer == null) {
					buffer = message;
				} else {
					buffer += "\n" + message;
				}
			}
		}
	}
	
	/**
	 * Injects the pmsSettings bundle.
	 * @param settings
	 */
	@Inject
	public void setSettings(PmsSettings settings) {
		this.pmsSettings = settings;
	}
}
