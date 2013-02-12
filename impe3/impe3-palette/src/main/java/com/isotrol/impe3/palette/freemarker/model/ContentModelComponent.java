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


import java.io.IOException;
import java.io.InputStream;

import net.sf.derquinsej.io.Streams;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.freemarker.Model;


/**
 * 
 * @author Emilio Escobar reyero
 */
@RequiresLink(Content.class)
public class ContentModelComponent extends AbstractModelComponent implements Component {

	/** Content. */
	private Content content;

	@Inject
	public void setContent(Content content) {
		this.content = content;
	}

	public ComponentResponse execute() {
		if (content == null) {
			content = loadSample();
		}
		templateModel = Model.createComponentModel(templateModel, context);
		if (content != null) {
			templateModel.put("metadata", content);
			if (moduleConfig.xml()) {
				templateModel.put(Model.XML, Model.loadXML(content.getContent()));
			} else {
				templateModel.put("text", inputToString(content.getContent()));
			}
		}
		return ComponentResponse.OK;
	}

	private String inputToString(InputStream in) {
		if (in == null) {
			return null;
		}

		try {
			final byte[] bytes = Streams.consume(in, true);

			return new String(bytes);
		} catch (IOException e) {
			return null;
		}
	}

}
