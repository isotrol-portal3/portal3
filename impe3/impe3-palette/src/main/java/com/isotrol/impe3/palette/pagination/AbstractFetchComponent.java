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


import java.util.List;

import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.EditModeComponent;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;


/**
 * Base class for pagination fetch components.
 * @author Andres Rodriguez
 */
public abstract class AbstractFetchComponent extends AbstractComponent implements EditModeComponent {
	/** Request parameters. */
	private RequestParams params;
	/** Pagination. */
	private Pagination pagination = null;

	/**
	 * Constructor.
	 */
	AbstractFetchComponent() {
	}

	/** Page size to use. */
	abstract Integer getFetchPageSize();

	@Inject
	public final void setParams(RequestParams params) {
		this.params = params;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public final void edit() {
		pagination = new Pagination(getParameterName(), 1, getFetchPageSize(), null);
	}

	/**
	 * Execute component.
	 */
	public final ComponentResponse execute() {
		final String name = getParameterName();
		int page = 1;
		try {
			if (params != null) {
				final List<String> values = params.get(name);
				if (values != null && !values.isEmpty()) {
					int value = Integer.parseInt(values.get(0));
					if (value > 0) {
						page = value;
					}
				}
			}
		} catch (RuntimeException e) {
			// do nothing
		}
		pagination = new Pagination(name, page, getFetchPageSize(), null);
		return ComponentResponse.OK;
	}

	@Extract
	public final Pagination getPagination() {
		return pagination;
	}

	@Extract
	public final ETag getETag() {
		if (pagination == null) {
			return null;
		}
		return ETag.of(String.format("%s/%d", pagination.getParameter(), pagination.getPage()));
	}
}
