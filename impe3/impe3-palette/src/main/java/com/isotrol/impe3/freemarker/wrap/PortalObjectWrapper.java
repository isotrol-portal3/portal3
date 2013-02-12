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
import static com.isotrol.impe3.freemarker.wrap.PortalAPIModel.newModel;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.MapMaker;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.RenderContext;

import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * FreeMarker object wrapper. This class is NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public final class PortalObjectWrapper extends DefaultObjectWrapper {
	private final RenderContext context;
	private final Map<Object, TemplateModel> models = new MapMaker().weakKeys().makeMap();
	private final Iterable<? extends PortalFreeMarkerModelFactory> factories;

	/**
	 * Creates a new object wrapper.
	 * @param context Render context.
	 * @return The object wrapper to use.
	 */
	public static ObjectWrapper create(RenderContext context) {
		return new PortalObjectWrapper(context, null);
	}

	/**
	 * Creates a new object wrapper.
	 * @param context Render context.
	 * @param factories Additional factories.
	 * @return The object wrapper to use.
	 */
	public static ObjectWrapper create(RenderContext context, Iterable<? extends PortalFreeMarkerModelFactory> factories) {
		return new PortalObjectWrapper(context, factories);
	}

	PortalObjectWrapper(RenderContext context, Iterable<? extends PortalFreeMarkerModelFactory> factories) {
		this.context = checkNotNull(context, "The render context must be provided");
		if (factories == null || Iterables.isEmpty(factories)) {
			this.factories = Arrays.asList(Factory.values());
		} else {
			this.factories = ImmutableList.<PortalFreeMarkerModelFactory> builder()
				.addAll(Arrays.asList(Factory.values())).addAll(factories).build();
		}
	}

	public RenderContext getContext() {
		return context;
	}

	@Override
	protected TemplateModel handleUnknownType(Object obj) throws TemplateModelException {
		// Should not happen.
		if (obj == null) {
			return super.wrap(null);
		}
		if (models.containsKey(obj)) {
			return models.get(obj);
		}
		for (PortalFreeMarkerModelFactory f : factories) {
			if (f.isInstance(obj)) {
				TemplateModel model = f.build(this, obj);
				models.put(obj, model);
				return model;
			}
		}
		return super.handleUnknownType(obj);
	}

	private enum Factory implements PortalFreeMarkerModelFactory {
		ROUTE(Route.class) {
			public TemplateModel build(PortalObjectWrapper wrapper, Object obj) {
				return newModel(wrapper, (Route) obj, PortalAPIMethodMap.ROUTE);
			}
		},
		PAGE_KEY(PageKey.class) {
			public TemplateModel build(PortalObjectWrapper wrapper, Object obj) {
				return newModel(wrapper, (PageKey) obj, PortalAPIMethodMap.PAGE_KEY);
			}
		},
		CONTENT_KEY(ContentKey.class) {
			public TemplateModel build(PortalObjectWrapper wrapper, Object obj) {
				return newModel(wrapper, (ContentKey) obj, PortalAPIMethodMap.CONTENT_KEY);
			}
		},
		NAVIGATION_KEY(PageKey.class) {
			public TemplateModel build(PortalObjectWrapper wrapper, Object obj) {
				return newModel(wrapper, (NavigationKey) obj, PortalAPIMethodMap.NAVIGATION_KEY);
			}
		};

		/** Object to wrap class. */
		private final Class<?> klass;

		private Factory(Class<?> klass) {
			this.klass = klass;
		}

		public final boolean isInstance(Object obj) {
			return klass.isInstance(obj);
		}
	};

}
