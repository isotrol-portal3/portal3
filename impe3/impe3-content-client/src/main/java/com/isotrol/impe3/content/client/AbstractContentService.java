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
import java.util.Map;
import java.util.Set;

import net.sf.derquinse.lucis.Group;
import net.sf.derquinse.lucis.GroupResult;
import net.sf.derquinse.lucis.Item;
import net.sf.derquinse.lucis.Page;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.content.api.CategoryRefTreeDTO;
import com.isotrol.impe3.content.api.CompleteContentDTO;
import com.isotrol.impe3.content.api.ContentDTO;
import com.isotrol.impe3.content.api.ContentDetailDTO;
import com.isotrol.impe3.content.api.ContentFilterDTO;
import com.isotrol.impe3.content.api.ContentQueryType;
import com.isotrol.impe3.content.api.ContentService;
import com.isotrol.impe3.content.api.ContentSummaryDTO;
import com.isotrol.impe3.content.api.ContentTypeRefDTO;
import com.isotrol.impe3.content.api.CountedCategoryRefTreeDTO;
import com.isotrol.impe3.dto.Counted;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeSort;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Abstract class for a content service.
 * @author Andres Rodriguez
 * @param <T> Content representation.
 */
abstract class AbstractContentService<T> implements ContentService {
	/** IA Model. */
	private final IAModel ia;
	/** Locale to use. */
	private final Locale locale;

	AbstractContentService(IAModel ia, Locale locale) {
		this.ia = checkNotNull(ia);
		this.locale = checkNotNull(locale);
	}

	final IAModel ia() {
		return ia;
	}

	private ContentServiceSupport support(String locale) {
		return new ContentServiceSupport(ia, locale, this.locale);
	}

	private ContentServiceSupport support(ContentFilterDTO filter) {
		return new ContentServiceSupport(ia, filter, this.locale);
	}

	abstract Function<T, ContentDTO> toContent(ContentServiceSupport s);

	abstract Function<T, ContentDetailDTO> toContentDetail(ContentServiceSupport s);

	abstract Function<T, CompleteContentDTO> toCompleteContent(ContentServiceSupport s);

	abstract Page<T> getPage(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes, int firstResult, int pageSize);

	abstract GroupResult groupBy(NodeQuery query, NodeFilter filter, List<String> fields);

	abstract Item<T> getFirst(NodeQuery query, NodeFilter filter, NodeSort sort, boolean bytes);

	public final ContentSummaryDTO getSummary(String locale) throws ServiceException {
		return null;
	}

	public final PageDTO<ContentDTO> getPage(String providerId, ContentQueryType type, String query,
		ContentFilterDTO filter) throws ServiceException {
		// TODO: sort
		final int first;
		final int size;
		final PaginationDTO pag = (filter != null) ? filter.getPagination() : null;
		if (pag != null) {
			first = pag.getFirst();
			size = pag.getSize();
		} else {
			first = 0;
			size = PaginationDTO.SIZE;
		}
		final ContentServiceSupport s = support(filter);
		Page<T> p = getPage(s.query(type, query), s.filter(), null, false, first, size);
		if (p == null) {
			return null;
		}
		PageDTO<ContentDTO> page = new PageDTO<ContentDTO>();
		page.setElements(Lists.newArrayList(Iterables.transform(p.getItems(), toContent(s))));
		page.setFirst(p.getFirstResult());
		page.setSize(1 + p.getLastResult() - p.getFirstResult());
		page.setTotal(p.getTotalHits());
		return page;
	}

	public final List<ContentTypeRefDTO> getContentTypes(String providerId, String locale) throws ServiceException {
		return support(locale).getContentTypes();
	}

	public final List<Counted<ContentTypeRefDTO>> getPerContentType(String providerId, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		final GroupResult result = groupBy(NodeQueries.matchAll(), s.filter(), Lists.newArrayList(Schema.TYPE));
		final Group root = result.getGroup();
		final List<Counted<ContentTypeRefDTO>> contentTypes = Lists.newArrayListWithCapacity(root.getGroupNames()
			.size());
		for (String name : root.getGroupNames()) {
			final Group g = root.getGroup(name);
			final ContentTypeRefDTO ref = s.contentTypeStr.apply(name);
			if (ref != null) {
				final Counted<ContentTypeRefDTO> dto = new Counted<ContentTypeRefDTO>();
				dto.setCount(g.getHits());
				dto.setItem(ref);
				contentTypes.add(dto);
			}
		}
		return contentTypes;
	}

	public final CategoryRefTreeDTO getCategories(String providerId, String locale) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	public final CountedCategoryRefTreeDTO getPerCategory(String providerId, String locale) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	private <C extends ContentDTO> C getContent(ContentServiceSupport s, String key, boolean bytes, Function<T, C> f)
		throws ServiceException {
		if (key == null) {
			return null;
		}
		final Item<T> item = getFirst(NodeQueries.term(Schema.NODEKEY, key), s.filter(), null, bytes);
		if (item == null) {
			return null;
		}
		T t = item.getItem();
		if (t == null) {
			return null;
		}
		return f.apply(t);
	}

	private <C extends ContentDTO> Map<String, C> getContents(ContentServiceSupport s, Set<String> keys, boolean bytes,
		Function<T, C> f) throws ServiceException {
		final Map<String, C> map = Maps.newHashMap();
		if (keys != null) {
			for (String key : keys) {
				final C c = getContent(s, key, bytes, f);
				if (c != null) {
					map.put(key, c);
				}
			}

		}
		return map;
	}

	public final ContentDTO getContent(String providerId, String key, String locale) throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContent(s, key, false, toContent(s));
	}

	public final Map<String, ContentDTO> getContents(String providerId, Set<String> keys, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContents(s, keys, false, toContent(s));
	}

	public final ContentDetailDTO getContentDetail(String providerId, String key, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContent(s, key, false, toContentDetail(s));
	}

	public final Map<String, ContentDetailDTO> getContentsDetail(String providerId, Set<String> keys, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContents(s, keys, false, toContentDetail(s));
	}

	public final CompleteContentDTO getCompleteContent(String providerId, String key, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContent(s, key, false, toCompleteContent(s));
	}

	public final Map<String, CompleteContentDTO> getCompleteContents(String providerId, Set<String> keys, String locale)
		throws ServiceException {
		final ContentServiceSupport s = support(locale);
		return getContents(s, keys, false, toCompleteContent(s));
	}

}
