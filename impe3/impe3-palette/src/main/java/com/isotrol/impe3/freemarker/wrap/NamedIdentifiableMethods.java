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

import java.util.List;
import java.util.Locale;

import net.sf.derquinsej.i18n.Locales;

import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.NamedIdentifiable;
import com.isotrol.impe3.freemarker.FreeMarkerFunction;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Methods for Identifiable object.
 * @author Andres Rodriguez
 */
enum NamedIdentifiableMethods implements PortalAPIMethod<NamedIdentifiable> {
	DISPLAY_NAME {
		public String getName() {
			return "displayName";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new SimpleScalar(n(wrapper, object).getDisplayName());
		}
	},
	L7D_DISPLAY_NAME {
		public String getName() {
			return "l7dDisplayName";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return new SimpleScalar(n(wrapper, object, arg(args, 0)).getDisplayName());
				}
			};
		}
	},
	DEFAULT_DISPLAY_NAME {
		public String getName() {
			return "defaultDisplayName";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new SimpleScalar(object.getName().get().getDisplayName());
		}
	},
	PATH {
		public String getName() {
			return "path";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new SimpleScalar(n(wrapper, object).getPath());
		}
	},
	L7D_PATH {
		public String getName() {
			return "l7dPath";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new FreeMarkerFunction() {
				@Override
				public Object apply(List<String> args) throws TemplateModelException {
					return new SimpleScalar(n(wrapper, object, arg(args, 0)).getPath());
				}
			};
		}
	},
	DEFAULT_PATH {
		public String getName() {
			return "defaultPath";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
			return new SimpleScalar(object.getName().get().getPath());
		}
	};

	private static Name n(final PortalObjectWrapper wrapper, final NamedIdentifiable object) {
		return object.getName().get(wrapper.getContext().getLocale());
	}

	private static Name n(final PortalObjectWrapper wrapper, final NamedIdentifiable object, String localeStr) {
		final Locale locale = Locales.safeFromString(localeStr);
		if (locale == null) {
			return n(wrapper, object);
		}
		return object.getName().get(locale);
	}
}
