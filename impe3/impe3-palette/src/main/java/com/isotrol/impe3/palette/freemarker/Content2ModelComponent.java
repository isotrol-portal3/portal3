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


import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.freemarker.Model;

import freemarker.ext.dom.NodeModel;


/**
 * Component that includes the content in the request context in the template model. A default FreeMarker Template Model
 * is created if no model has been injected.
 * @author Andres Rodriguez
 */
public final class Content2ModelComponent extends AbstractModelTransformerComponent {
	private Content2ModelConfig config;

	/**
	 * Public constructor.
	 */
	public Content2ModelComponent() {
	}

	@Inject
	public void setConfig(Content2ModelConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		final Content c = getContext().getContent();
		if (c != null && config != null) {
			TemplateModel model = getTemplateModel();
			if (config.parseXML()) {
				NodeModel xml = Model.loadXML(c.getContent());
				if (xml != null) {
					c.getLocalValues().put(config.xmlLocalValue(), xml);
					model.put(config.xml(), model);
				}
			}
			model.put(config.content(), c);
		}
		return ComponentResponse.OK;
	}
}
