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

package com.isotrol.impe3.palette.breadcrumb;


import com.isotrol.impe3.api.component.ComponentETag;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;


/**
 * Manual Breadcrumb Component.
 * @author Andres Rodriguez
 */
@ComponentETag(ComponentETagMode.IGNORE)
public class ManualBreadcrumbComponent extends AbstractBreadcrumbComponent {
	/** Component config. */
	private ManualBreadcrumbConfig componentConfig;

	/**
	 * Constructor.
	 */
	public ManualBreadcrumbComponent() {
	}

	@Inject
	public void setComponentConfig(ManualBreadcrumbConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	public void edit() {
		prepare();
		if (componentConfig != null) {
			add(componentConfig.text1(), componentConfig.uri1());
			add(componentConfig.text2(), componentConfig.uri2());
			add(componentConfig.text3(), componentConfig.uri3());
			add(componentConfig.text4(), componentConfig.uri4());
			add(componentConfig.text5(), componentConfig.uri5());
		}
	}

	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

}
