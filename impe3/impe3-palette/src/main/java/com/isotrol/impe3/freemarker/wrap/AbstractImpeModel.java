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

package com.isotrol.impe3.freemarker.wrap;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.isotrol.impe3.api.component.RenderContext;

import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;


/**
 * Abstract class for non-API-based FreeMarker models.
 * @author Andres Rodriguez
 */
abstract class AbstractImpeModel implements TemplateHashModel {
	/** Object wrapper. */
	private final PortalObjectWrapper wrapper;

	/** Destination route. */

	/**
	 * Constructor.
	 * @param wrapper Object wrapper.
	 */
	AbstractImpeModel(PortalObjectWrapper wrapper) {
		this.wrapper = checkNotNull(wrapper);
	}

	abstract Map<String, TemplateModel> getModels();

	final PortalObjectWrapper getWrapper() {
		return wrapper;
	}

	final RenderContext getContext() {
		return wrapper.getContext();
	}

	public final boolean isEmpty() throws TemplateModelException {
		return getModels().isEmpty();
	}

	public final TemplateModel get(String key) throws TemplateModelException {
		TemplateModel model = getModels().get(key);
		if (model != null) {
			return model;
		}
		return TemplateScalarModel.EMPTY_STRING;
	}
}
