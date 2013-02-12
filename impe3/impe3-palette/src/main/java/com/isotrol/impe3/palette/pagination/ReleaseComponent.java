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

package com.isotrol.impe3.palette.pagination;


import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentETag;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractDynQuery;
import com.isotrol.impe3.api.component.ExtractedParameter;
import com.isotrol.impe3.api.component.Inject;


/**
 * Pagination Release Component.
 * @author Andres Rodriguez
 */
@ComponentETag(ComponentETagMode.IGNORE)
public class ReleaseComponent extends AbstractComponent {
	/** Pagination. */
	private Pagination pagination = null;

	/**
	 * Constructor.
	 */
	public ReleaseComponent() {
	}

	@Inject
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	/**
	 * Execute component.
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	@ExtractDynQuery
	public ExtractedParameter<Integer> getPage() {
		if (pagination != null) {
			int page = pagination.getPage();
			if (page > 0) {
				return ExtractedParameter.of(pagination.getParameter(), page);
			}
		}
		return null;
	}

}
