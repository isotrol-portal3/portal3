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

package com.isotrol.impe3.palette.content.loader;


import org.springframework.beans.factory.annotation.Autowired;

import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;


/**
 * Base class for components in this module.
 * @author Andres Rodriguez
 */
public abstract class AbstractLoaderComponent implements Component {
	/** Navigation key. */
	private NavigationKey navigationKey;
	/** Content loader. */
	private ContentLoader contentLoader;
	/** Module configuration. */
	private LoaderModuleConfig moduleConfig;

	/**
	 * Constructor.
	 */
	AbstractLoaderComponent() {
	}

	/* Spring configuration. */

	@Autowired
	public void setModuleConfig(LoaderModuleConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	/* Port@l injection. */

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setContentLoader(ContentLoader contentLoader) {
		this.contentLoader = contentLoader;
	}

	public final ComponentResponse execute() {
		final ContentCriteria criteria = contentLoader.newCriteria();
		if (moduleConfig != null) {
			if (navigationKey != null && moduleConfig.useNavigation()) {
				criteria.navigation(navigationKey);
			}
			criteria.setBytes(moduleConfig.loadBytes());
		}
		if (navigationKey != null && moduleConfig != null && moduleConfig.useNavigation()) {
			criteria.navigation(navigationKey);
		}
		load(criteria);
		return ComponentResponse.OK;
	}

	abstract void load(ContentCriteria criteria);
}
