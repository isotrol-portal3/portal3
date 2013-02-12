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

package com.isotrol.impe3.palette.freemarker.model;


import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.freemarker.FreeMarker;


/**
 * Abstract base model component.
 * @author Emilio Escobar Reyero
 */
public abstract class AbstractModelComponent {
	/** File loader. */
	@Autowired
	private FileLoader fileLoader;
	/** Template model. */
	TemplateModel templateModel;
	/** Request context. */
	ComponentRequestContext context;
	/** Module Config. */
	TemplateModelConfig moduleConfig;

	AbstractModelComponent() {
	}

	@Autowired
	public void setModuleConfig(TemplateModelConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	@Inject
	public void setContext(ComponentRequestContext context) {
		this.context = context;
	}

	@Inject
	public void setTemplateModel(TemplateModel templateModel) {
		this.templateModel = templateModel;
	}

	@Extract
	public TemplateModel getTemplateModel() {
		return templateModel;
	}

	final Content loadSample() {
		final FileId sampleFile = moduleConfig.sample();
		if (sampleFile == null || (EngineMode.ONLINE == context.getPortal().getMode() && !moduleConfig.sampleOnline())) {
			return null;
		}
		return FreeMarker.loadXMLContent(fileLoader, sampleFile, context.getPortal().getContentTypes(), context
			.getPortal().getCategories());
	}

	final boolean isXML() {
		return moduleConfig != null && moduleConfig.xml();
	}

}
