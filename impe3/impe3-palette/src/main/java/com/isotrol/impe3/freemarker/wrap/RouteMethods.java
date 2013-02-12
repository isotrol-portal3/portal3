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
import java.util.Locale;

import net.sf.derquinsej.i18n.Locales;

import com.isotrol.impe3.api.DeviceInPortal;
import com.isotrol.impe3.api.DevicesInPortal;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.freemarker.FreeMarkerFunction;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Methods for RouteObject.
 * @author Andres Rodriguez
 */
enum RouteMethods implements PortalAPIMethod<Route> {
	URI {
		public String getName() {
			return "uri";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
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

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return uri(wrapper.getContext().getAbsoluteURI(object, ModelUtils.buildURIQueryParameters(args, 0)));
				}
			};
		}
	},
	TO_SPECIAL_PAGE {
		public String getName() {
			return "toSpecialPage";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					final String name = arg(args, 0);
					final PageKey pk;
					if (name == null) {
						pk = PageKey.main();
					} else {
						pk = PageKey.special(name);
					}
					return wrapper.wrap(object.toPage(pk));
				}
			};
		}
	},
	TO_LOCALE {
		public String getName() {
			return "toLocale";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return toLocale(wrapper, object, arg(args, 0));
				}
			};
		}
	},
	TO_DEVICE {
		public String getName() {
			return "toDevice";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return toDevice(wrapper, object, arg(args, 0));
				}
			};
		}
	},
	URI_BUILDER {
		public String getName() {
			return "uriBuilder";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final Route object) {
			return new URIBuilderModel(wrapper, object);
		}
	};

	static Route toLocale(RenderContext context, Route route, String localeString) {
		final Locale locale = Locales.safeFromString(localeString);
		if (locale == null) {
			return route;
		}
		return route.toLocale(locale);
	}

	static TemplateModel toLocale(PortalObjectWrapper wrapper, Route route, String localeString)
		throws TemplateModelException {
		return wrapper.wrap(toLocale(wrapper.getContext(), route, localeString));
	}

	static Route toDevice(RenderContext context, Route route, String deviceName) {
		if (deviceName == null) {
			return route;
		}
		final DevicesInPortal dips = context.getPortal().getDevices();
		if (dips.containsName(deviceName)) {
			final DeviceInPortal dip = dips.getByName(deviceName);
			return route.toDevice(dip.getDevice());
		}
		return route;
	}

	static TemplateModel toDevice(PortalObjectWrapper wrapper, Route route, String deviceName)
		throws TemplateModelException {
		return wrapper.wrap(toDevice(wrapper.getContext(), route, deviceName));
	}
}
