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

package com.isotrol.impe3.palette.content.xmldetail;


import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.freemarker.FreeMarker;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.palette.content.AbstractVisualContentComponent;


/**
 * XML Content Detail Component.
 * @author Andres Rodriguez
 */
@RequiresLink(Content.class)
public class XMLContentDetailComponent extends AbstractVisualContentComponent {
	/** Configuration. */
	private XMLContentDetailModuleConfig config;
	/** Content. */
	private Content content;
	/** Whether we are using the sample content. */
	private boolean sample = false;

	/**
	 * Public constructor.
	 */
	public XMLContentDetailComponent() {
	}

	public void setConfig(XMLContentDetailModuleConfig config) {
		this.config = config;
	}

	@Inject
	public void setContent(Content content) {
		this.content = content;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		sample = false;
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (content == null) {
			final FileId file = config.sample();
			if (file != null) {
				content = FreeMarker.loadXMLContent(getFileLoader(), file, null, null);
				sample = true;
			}
		}
	}

	Content getContent() {
		return content;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				final FreeMarkerService fms = getFreeMarkerService();
				final Model model = sample ? createSampleModel(context, config) : fms.createModel(context);
				if (content != null) {
					model.setMetadata(content).setXML(content.getContent());
				}
				return fms.getFragment(config.templateFile(), context, model);
			}
		};
	}
}
