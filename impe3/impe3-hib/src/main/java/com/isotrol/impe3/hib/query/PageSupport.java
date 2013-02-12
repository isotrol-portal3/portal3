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

package com.isotrol.impe3.hib.query;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PaginationDTO;


/**
 * Support class for building page results from hibernate queries.
 * @author Andres Rodriguez.
 */
public final class PageSupport {
	/** Not instantiable. */
	private PageSupport() {
		throw new AssertionError();
	}

	/**
	 * Page support for criteria queries.
	 * @param <E> Query output type.
	 * @param <D> DTO type.
	 * @param count Count criteria (optional).
	 * @param select Selection criteria (required).
	 * @param pag Pagination (optional).
	 * @param transformer Transformer (required).
	 * @return
	 */
	public static <E, D> PageDTO<D> getPage(Criteria count, Criteria select, PaginationDTO pag,
		Function<? super E, ? extends D> transformer) {
		checkNotNull(select, "A selection query must be provided");
		final PageDTO<D> page = new PageDTO<D>();
		if (count != null) {
			count.setProjection(Projections.rowCount());
			page.setTotal(((Number) count.uniqueResult()).intValue());
		}
		if (pag != null) {
			page.setFirst(pag.getFirst());
			page.setSize(pag.getSize());
			select.setFirstResult(pag.getFirst());
			select.setMaxResults(pag.getSize());
		}
		@SuppressWarnings("unchecked")
		final List<E> list = select.list();
		page.setElements(Lists.newArrayList(Iterables.transform(list, transformer)));
		return page;
	}
}
