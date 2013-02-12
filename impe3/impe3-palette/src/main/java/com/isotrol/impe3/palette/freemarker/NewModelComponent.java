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

package com.isotrol.impe3.palette.freemarker;


import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.freemarker.Model;


/**
 * Components that creates and export the default FreeMarker Template Model.
 * @author Andres Rodriguez
 */
public final class NewModelComponent implements Component {
	/** Request context. */
	private ComponentRequestContext context;
	/** Template model. */
	private TemplateModel templateModel = null;

	/**
	 * Public constructor.
	 */
	public NewModelComponent() {
	}
	
	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

	@Extract
	public TemplateModel getTemplateModel() {
		return templateModel;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		templateModel = Model.createComponentModel(context);
		return ComponentResponse.OK;
	}
}
