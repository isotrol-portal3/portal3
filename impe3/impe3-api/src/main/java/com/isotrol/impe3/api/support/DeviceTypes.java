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

package com.isotrol.impe3.api.support;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.Maps;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.atom.ATOMRenderer;
import com.isotrol.impe3.api.component.excel.ExcelRenderer;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.pdf.PDFRenderer;
import com.isotrol.impe3.api.component.sitemap.SitemapRenderer;


/**
 * Support methods for device types.
 * @author Andres Rodriguez.
 */
public final class DeviceTypes {
	/** Not instantiable. */
	private DeviceTypes() {
		throw new AssertionError();
	}

	private static final Map<DeviceType, Class<? extends ComponentRenderer>> MAP;

	static {
		MAP = Maps.newEnumMap(DeviceType.class);
		MAP.put(DeviceType.HTML, HTMLRenderer.class);
		MAP.put(DeviceType.XHTML, HTMLRenderer.class);
		MAP.put(DeviceType.ATOM, ATOMRenderer.class);
		MAP.put(DeviceType.SITEMAP, SitemapRenderer.class);
		MAP.put(DeviceType.XLS, ExcelRenderer.class);
		MAP.put(DeviceType.XLSX, ExcelRenderer.class);
		MAP.put(DeviceType.PDF, PDFRenderer.class);
	}

	public static Class<? extends ComponentRenderer> getRendererType(DeviceType type) {
		return MAP.get(checkNotNull(type, "Null device type"));
	}
}
