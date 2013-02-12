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

package com.isotrol.impe3.palette.metadata;


import static com.isotrol.impe3.api.component.html.HTMLConstants.CONTENT;
import static com.isotrol.impe3.api.component.html.HTMLConstants.HTTP_EQUIV;
import static com.isotrol.impe3.api.component.html.HTMLConstants.NAME;

import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * Component to generate META elements.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class MetaComponent implements Component {
	private MetaConfig moduleConfig;
	private MetaComponentConfig componentConfig;
	private String name;
	private String content;
	private boolean httpEquiv;

	/** Default constructor. */
	public MetaComponent() {
	}

	@Autowired
	public void setModuleConfig(MetaConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	@Inject
	public void setComponentConfig(MetaComponentConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	public ComponentResponse execute() {
		if (componentConfig != null && componentConfig.override()) {
			name = componentConfig.name();
			content = componentConfig.content();
			httpEquiv = componentConfig.httpEquiv();
		} else if (moduleConfig != null) {
			name = moduleConfig.name();
			content = moduleConfig.content();
			httpEquiv = moduleConfig.httpEquiv();
		} else {
			name = null;
			content = null;
		}
		return ComponentResponse.OK;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getHeader() {
				if (name == null || content == null) {
					return null;
				}
				String a = httpEquiv ? HTTP_EQUIV : NAME;
				return HTML.create(context).meta().set(a, name).set(CONTENT, content);
			}
		};
	}
}
