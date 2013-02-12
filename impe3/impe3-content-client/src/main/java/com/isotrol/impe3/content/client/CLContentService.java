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

package com.isotrol.impe3.content.client;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Locale;

import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;

import com.google.common.base.Function;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.content.api.CompleteContentDTO;
import com.isotrol.impe3.content.api.ContentDTO;
import com.isotrol.impe3.content.api.ContentDetailDTO;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * Content-loader based content service.
 * @author Andres Rodriguez
 */
public final class CLContentService extends AbstractContentService<Content> {
	/** Content loader. */
	private final ContentLoader loader;

	public CLContentService(ContentLoader loader, IAModel ia, Locale locale) {
		super(ia, locale);
		this.loader = checkNotNull(loader);
	}

	@Override
	Function<Content, ContentDTO> toContent(ContentServiceSupport s) {
		return s.c2content;
	}

	@Override
	Function<Content, ContentDetailDTO> toContentDetail(ContentServiceSupport s) {
		return s.c2contentDetail;
	}

	@Override
	Function<Content, CompleteContentDTO> toCompleteContent(ContentServiceSupport s) {
		return s.c2completeContent;
	}

	private ContentCriteria criteria(NodeQuery query, NodeFilter filter, boolean bytes) {
		return loader.newCriteria().setBytes(bytes).must(query);
		// TODO
	}

	private ContentCriteria criteria(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes) {
		return criteria(query, filter, bytes);
		// TODO
	}

	@Override
	Page<Content> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult,
		int pageSize) {
		return criteria(query, filter, sort, bytes).getPage(
			new Pagination(Pagination.PAGE, firstResult / pageSize, pageSize, null));
	}

	@Override
	GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return criteria(query, filter, false).groupBy(fields);
	}

	@Override
	Item<Content> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes) {
		return criteria(query, filter, sort, bytes).getFirst();
	}

}
