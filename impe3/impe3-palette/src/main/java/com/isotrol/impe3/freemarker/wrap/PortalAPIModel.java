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
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;


/**
 * Base class for Port@l API object FreeMarker model for route object.
 * @author Andres Rodriguez
 * @param <T> Wrapped object type.
 */
final class PortalAPIModel<T> implements TemplateHashModel {
	private final PortalObjectWrapper wrapper;
	private final T object;
	private final PortalAPIMethodMap<T> map;
	private final TemplateHashModel fallback;

	static <T> PortalAPIModel<T> newModel(PortalObjectWrapper wrapper, T object, PortalAPIMethodMap<T> map,
		TemplateHashModel fallback) {
		return new PortalAPIModel<T>(wrapper, object, map, fallback);
	}

	static <T> PortalAPIModel<T> newModel(PortalObjectWrapper wrapper, T object, PortalAPIMethodMap<T> map) {
		return newModel(wrapper, object, map, null);
	}

	private PortalAPIModel(PortalObjectWrapper wrapper, T object, PortalAPIMethodMap<T> map, TemplateHashModel fallback) {
		this.wrapper = checkNotNull(wrapper);
		this.object = checkNotNull(object);
		this.map = checkNotNull(map);
		this.fallback = fallback;
	}

	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

	public TemplateModel get(String key) throws TemplateModelException {
		TemplateModel model = map.get(key, wrapper, object);
		if (model != null) {
			return model;
		}
		if (fallback != null) {
			return fallback.get(key);
		}
		return TemplateScalarModel.EMPTY_STRING;
	}

}
