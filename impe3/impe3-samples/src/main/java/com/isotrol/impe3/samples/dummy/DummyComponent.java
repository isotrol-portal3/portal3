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

package com.isotrol.impe3.samples.dummy;


import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * Simple calculator component implementation.
 * @author Andres Rodriguez
 */
public class DummyComponent implements VisualComponent {
	/** Service. */
	private DummyService service;
	/** Config. */
	private DummyConfig config;

	public DummyComponent() {
	}

	/* Spring setters. */

	public void setService(DummyService service) {
		this.service = service;
	}

	public void setConfig(DummyConfig config) {
		this.config = config;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				return HTML.create(context).p(String.format("%s %s", service.get(), config.text()));
			}
		};
	}

	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	public void edit() {
	}
}
