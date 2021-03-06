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

package com.isotrol.impe3.api.support;


import com.isotrol.impe3.api.component.Action;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractAction;


/**
 * Action exporter component.
 * @author Andres Rodriguez.
 */
public class ActionExporterComponent implements Component {
	private String actionName;
	private WithActionExporterConfig config;

	/** Constructor. */
	public ActionExporterComponent() {
	}

	public void setConfig(WithActionExporterConfig config) {
		this.config = config;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	/** Returns counter action. */
	@ExtractAction
	public Action getAccion() {
		return Action.of(actionName, config.registrationName());
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}
}
