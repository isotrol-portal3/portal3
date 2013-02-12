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


import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;

import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Methods for ContentKey object.
 * @author Andres Rodriguez
 */
enum NavigationKeyMethods implements PortalAPIMethod<NavigationKey> {
	PAGE {
		public String getName() {
			return "page";
		}

		public TemplateModel get(final PortalObjectWrapper wrapper, final NavigationKey object)
			throws TemplateModelException {
			final PageKey pk = PageKey.navigation(object);
			return wrapper.wrap(pk);
		}
	};
}
