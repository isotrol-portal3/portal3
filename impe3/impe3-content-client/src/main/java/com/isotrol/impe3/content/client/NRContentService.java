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
import com.isotrol.impe3.content.api.CompleteContentDTO;
import com.isotrol.impe3.content.api.ContentDTO;
import com.isotrol.impe3.content.api.ContentDetailDTO;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.NodeSort;


/**
 * NodeRepository based content service.
 * @author Andres Rodriguez
 */
public final class NRContentService extends AbstractContentService<Node> {
	/** Node Repository. */
	private final NodeRepository nr;

	public NRContentService(NodeRepository nr, IAModel ia, Locale locale) {
		super(ia, locale);
		this.nr = checkNotNull(nr);
	}

	@Override
	Function<Node, ContentDTO> toContent(ContentServiceSupport s) {
		return s.node2content;
	}

	@Override
	Function<Node, ContentDetailDTO> toContentDetail(ContentServiceSupport s) {
		return s.node2contentDetail;
	}

	@Override
	Function<Node, CompleteContentDTO> toCompleteContent(ContentServiceSupport s) {
		return s.node2completeContent;
	}

	@Override
	Page<Node> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult, int pageSize) {
		return nr.getPage(query, filter, sort, bytes, firstResult, pageSize, null);
	}

	@Override
	GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields) {
		return nr.groupBy(query, filter, fields);
	}

	@Override
	Item<Node> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes) {
		return nr.getFirst(query, filter, sort, bytes, null);
	}

}
