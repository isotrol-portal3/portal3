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


import static com.isotrol.impe3.freemarker.wrap.ModelUtils.arg;
import static com.isotrol.impe3.freemarker.wrap.ModelUtils.buildURIQueryParameters;
import static com.isotrol.impe3.freemarker.wrap.ModelUtils.uri;

import java.util.List;

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.freemarker.FreeMarkerFunction;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Methods for PageKey object.
 * @author Andres Rodriguez
 */
enum PageKeyMethods implements PortalAPIMethod<PageKey> {
	URI {
		public String getName() {
			return "uri";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final PageKey object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return uri(wrapper.getContext().getURI(object, buildURIQueryParameters(args, 0)));
				}
			};
		}
	},
	ABS_URI {
		public String getName() {
			return "absUri";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final PageKey object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return uri(wrapper.getContext().getAbsoluteURI(object, ModelUtils.buildURIQueryParameters(args, 0)));
				}
			};
		}
	},
	TO_LOCALE {
		public String getName() {
			return "toLocale";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final PageKey object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return RouteMethods.toLocale(wrapper, route(wrapper, object), arg(args, 0));
				}
			};
		}
	},
	TO_DEVICE {
		public String getName() {
			return "toDevice";
		}
		public TemplateModel get(final PortalObjectWrapper wrapper, final PageKey object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return RouteMethods.toDevice(wrapper, route(wrapper, object), arg(args, 0));
				}
			};
		}
	},
	URI_BUILDER {
		public String getName() {
			return "uriBuilder";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final PageKey object) {
			return new URIBuilderModel(wrapper, route(wrapper, object));
		}
	};

	private static Route route(RenderContext context, PageKey pageKey) {
		return context.getRoute().toPage(pageKey);
	}

	private static Route route(PortalObjectWrapper wrapper, PageKey pageKey) {
		return route(wrapper.getContext(), pageKey);
	}

}
