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

package com.isotrol.impe3.pms.core.impl;


import static com.isotrol.impe3.pms.core.support.TimingSupport.summary;

import java.util.Map.Entry;

import net.sf.derquinsej.stats.AccumulatingTimingMap;
import net.sf.derquinsej.stats.Timing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.core.CategoryManager;
import com.isotrol.impe3.pms.core.ConnectorManager;
import com.isotrol.impe3.pms.core.ContentTypeManager;
import com.isotrol.impe3.pms.core.PortalLoader;


/**
 * Performance report component.
 * @author Andres Rodriguez.
 */
@Component
public final class PerformanceReportComponent {
	@Autowired
	private TimingAspect timingAspect;
	@Autowired
	private ContentTypeManager contentTypeManager;
	@Autowired
	private CategoryManager categoryManager;
	@Autowired
	private ConnectorManager connectorManager;
	@Autowired
	private PortalLoader portalLoader;

	/**
	 * Constructor.
	 */
	public PerformanceReportComponent() {
	}

	public String getReport() {
		final StringBuilder b = new StringBuilder();
		b.append("\nLoaders\n-------\n\n");
		b.append(contentTypeManager.getPerformanceReport()).append('\n').append(categoryManager.getPerformanceReport())
			.append('\n').append(connectorManager.getPerformanceReport()).append('\n')
			.append(portalLoader.getPerformanceReport()).append('\n');
		b.append("\nServices\n--------\n\n");
		AccumulatingTimingMap<String> map = timingAspect.getTimingMap();
		for (Entry<String, Timing> entry : map.entrySet()) {
			b.append(summary(entry.getKey(), entry.getValue())).append('\n');
		}
		b.append('\n').append(summary("Total", map.getAccumulator())).append('\n').append('\n');
		return b.toString();
	}
}
