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
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.TemplateKey;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.palette.AbstractFreeMarkerComponent;


/**
 * Apply FreeMarker Template Component.
 * @author Andres Rodriguez
 */
public class ApplyComponent extends AbstractFreeMarkerComponent {
	/** Module Configuration. */
	private ApplyModuleConfig config;
	/** Component Configuration. */
	private ApplyConfig componentConfig;
	/** Template key. */
	private TemplateKey templateKey = null;
	/** Template model. */
	private TemplateModel templateModel = null;

	/**
	 * Public constructor.
	 */
	public ApplyComponent() {
	}

	public void setConfig(ApplyModuleConfig config) {
		this.config = config;
	}

	@Inject
	public void setComponentConfig(ApplyConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	@Inject
	public void setTemplateKey(TemplateKey templateKey) {
		this.templateKey = templateKey;
	}

	@Inject
	public void setTemplateModel(TemplateModel templateModel) {
		this.templateModel = templateModel;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				// Resolve template
				String template = null;
				if (templateKey != null) {
					template = templateKey.toString();
				}
				if (template == null) {
					if (componentConfig != null && componentConfig.templateFile() != null) {
						template = componentConfig.templateFile();
					} else if (config != null && config.templateFile() != null) {
						template = config.templateFile();
					}
				}
				// �No template? We're empty.
				if (template == null) {
					return HTML.create(context).p();
				}
				// Resolve model
				final FreeMarkerService fms = getFreeMarkerService();
				final TemplateModel model;
				if (templateModel != null) {
					model = templateModel;
					fms.decorateRenderModel(context, model);
				} else {
					model = fms.createModel(context);
				}
				return fms.getFragment(template, context, model);
			}
		};
	}
}
