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

package com.isotrol.impe3.palette.content;


import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.support.RenderContexts;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.palette.AbstractFreeMarkerComponent;


/**
 * Abstract base class for content output components.
 * @author Andres Rodriguez
 */
public abstract class AbstractVisualContentComponent extends AbstractFreeMarkerComponent {
	/**
	 * Public constructor.
	 */
	public AbstractVisualContentComponent() {
	}

	protected Model createSampleModel(RenderContext context, ContentOutputConfig config) {
		if (config != null) {
			final FileId bundle = config.sample();
			final String base = config.sampleBase();
			if (bundle != null && base != null) {
				context = RenderContexts.changeBase(context, base, bundle, true);
			}
		}
		return getFreeMarkerService().createModel(context);
	}
}
