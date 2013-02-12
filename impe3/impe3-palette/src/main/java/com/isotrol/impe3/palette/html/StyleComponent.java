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

package com.isotrol.impe3.palette.html;


import static com.isotrol.impe3.api.component.html.HTMLConstants.CLASS;

import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.component.html.Tag;


/**
 * Style Component.
 * @author Andres Rodriguez
 */
public class StyleComponent implements VisualComponent {
	/** Component configuration. */
	private StyleConfig config;
	/** CSS class to apply. */
	private String klass = null;

	/**
	 * Public constructor.
	 */
	public StyleComponent() {
	}

	@Inject
	public void setConfig(StyleConfig config) {
		this.config = config;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (config != null) {
			klass = config.classAtt();
			if (klass.length() == 0) {
				klass = null;
			}
		}
	}

	/**
	 * Execute component.
	 */
	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				final Tag div = HTML.create(context).div();
				if (klass != null) {
					div.set(CLASS, klass);
				}
				return div;
			}
		};
	}
}
