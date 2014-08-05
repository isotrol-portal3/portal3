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
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.sf.derquinsej.i18n.Locales;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.api.NamedIdentifiable;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.content.api.AbstractContentDetailDTO;
import com.isotrol.impe3.content.api.CategoryRefDTO;
import com.isotrol.impe3.content.api.CategoryRefPathDTO;
import com.isotrol.impe3.content.api.CompleteContentDTO;
import com.isotrol.impe3.content.api.ContentDTO;
import com.isotrol.impe3.content.api.ContentDetailDTO;
import com.isotrol.impe3.content.api.ContentFilterDTO;
import com.isotrol.impe3.content.api.ContentQueryType;
import com.isotrol.impe3.content.api.ContentRefDTO;
import com.isotrol.impe3.content.api.ContentTypeRefDTO;
import com.isotrol.impe3.content.api.RefDTO;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeFilter;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.Schema;

/**
 * Content service support class.
 * @author Andres Rodriguez
 */
final class ContentServiceSupport {
	/** IA Model. */
	private final IAModel ia;
	/** Locale to use. */
	private final Locale locale;
	/** Node filter. */
	private final NodeFilter filter;

	private static NodeFilter filter(ContentFilterDTO filter, Locale fallback) {
		if (filter == null) {
			return locale(null, fallback).build();
		}
		NodeFilter.Builder f = locale(filter.getLocale(), fallback);
		String type = filter.getTypeId();
		if (type != null) {
			try {
				f = f.contentTypes().apply(UUID.fromString(type));
			} catch (IllegalArgumentException e) {
			}
		}
		return f.build();
	}

	private static NodeFilter.Builder locale(String locale, Locale fallback) {
		try {
			return NodeFilter.builder().locales().apply(Locales.fromString(locale));
		} catch (IllegalArgumentException e) {
			return NodeFilter.builder().locales().apply(fallback);
		}
	}

	/** Constructor. */
	ContentServiceSupport(IAModel ia, String locale, Locale fallback) {
		this.ia = checkNotNull(ia);
		this.locale = checkNotNull(fallback);
		this.filter = locale(locale, fallback).build();
	}

	/** Constructor. */
	ContentServiceSupport(IAModel ia, ContentFilterDTO filter, Locale fallback) {
		this.ia = checkNotNull(ia);
		this.locale = checkNotNull(fallback);
		this.filter = filter(filter, fallback);
	}

	final <T extends RefDTO> T fill(T dto, NamedIdentifiable ni) {
		dto.setId(ni.getStringId());
		dto.setName(ni.getName().get(locale).getDisplayName());
		return dto;
	}

	final Function<ContentType, ContentTypeRefDTO> contentType = new Function<ContentType, ContentTypeRefDTO>() {
		public ContentTypeRefDTO apply(ContentType input) {
			if (input == null) {
				return null;
			}
			return fill(new ContentTypeRefDTO(), input);
		}
	};

	final Function<String, ContentTypeRefDTO> contentTypeStr = new Function<String, ContentTypeRefDTO>() {
		public ContentTypeRefDTO apply(String input) {
			if (input == null) {
				return null;
			}
			try {
				return contentType.apply(ia.getContentTypes().get(UUID.fromString(input)));
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	};

	final Function<Category, CategoryRefDTO> category = new Function<Category, CategoryRefDTO>() {
		public CategoryRefDTO apply(Category input) {
			if (input == null) {
				return null;
			}
			return fill(new CategoryRefDTO(), input);
		}
	};

	final Function<String, CategoryRefDTO> categoryStr = new Function<String, CategoryRefDTO>() {
		public CategoryRefDTO apply(String input) {
			if (input == null) {
				return null;
			}
			try {
				return category.apply(ia.getCategories().get(UUID.fromString(input)));
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	};

	private final LoadingCache<UUID, String> paths = CacheBuilder.newBuilder().build(new CacheLoader<UUID, String>() {
		public String load(UUID input) {
			final Categories categories = ia.getCategories();
			final StringBuilder b = new StringBuilder(128);
			Category c = categories.get(input);
			while (c != null && c != categories.getRoot()) {
				b.insert(0, c.getName().get(locale).getDisplayName());
				b.insert(0, '/');
				c = categories.getParent(c.getId());
			}
			return b.toString();
		}
	});

	final Function<Category, CategoryRefPathDTO> categoryPath = new Function<Category, CategoryRefPathDTO>() {
		public CategoryRefPathDTO apply(Category input) {
			if (input == null) {
				return null;
			}
			CategoryRefPathDTO dto = fill(new CategoryRefPathDTO(), input);
			dto.setPath(paths.getUnchecked(input.getId()));
			return dto;
		}
	};

	final Function<String, CategoryRefPathDTO> categoryPathStr = new Function<String, CategoryRefPathDTO>() {
		public CategoryRefPathDTO apply(String input) {
			if (input == null) {
				return null;
			}
			try {
				return categoryPath.apply(ia.getCategories().get(UUID.fromString(input)));
			} catch (IllegalArgumentException e) {
				return null;
			}
		}
	};

	final NodeFilter filter() {
		return filter;
	}

	private ContentRefDTO contentRef(NodeKey key) {
		if (key == null) {
			return null;
		}
		final UUID typeId = key.getNodeType();
		if (typeId == null || !ia.getContentTypes().containsKey(typeId)) {
			return null;
		}
		final ContentRefDTO dto = new ContentRefDTO();
		dto.setId(key.getNodeId());
		dto.setContentType(contentType.apply(ia.getContentTypes().get(typeId)));
		return dto;
	}

	private ContentRefDTO contentRef(ContentKey key) {
		if (key == null) {
			return null;
		}
		final ContentRefDTO dto = new ContentRefDTO();
		dto.setId(key.getContentId());
		dto.setContentType(contentType.apply(key.getContentType()));
		return dto;
	}

	final Date c2d(Calendar c) {
		if (c == null) {
			return null;
		}
		long m = c.getTimeInMillis();
		return new Date(m);
	}

	NodeQuery query(ContentQueryType type, String query) {
		if (type == null || query == null) {
			return NodeQueries.matchAll();
		}
		switch (type) {
		case TITLE:
			return NodeQueries.string(Schema.TITLE, query);
		case DESCRIPTION:
			return NodeQueries.string(Schema.DESCRIPTION, query);
		case CONTENT:
			return NodeQueries.string(Schema.CONTENT_IDX, query);
		case TITLE_DESC:
			return NodeQueries.anyString(query, Schema.TITLE, Schema.DESCRIPTION);
		default:
			return NodeQueries.matchAll();
		}
	}

	private <T extends ContentDTO> T fillContent(T dto, Node from) {
		if (from == null) {
			return null;
		}
		ContentRefDTO ref = contentRef(from.getNodeKey());
		if (ref == null) {
			return null;
		}
		dto.setRef(ref);
		dto.setTitle(from.getTitle());
		dto.setDate(c2d(from.getDate()));
		return dto;
	}

	private <T extends AbstractContentDetailDTO> T fillAbstractContentDetail(T dto, Node from) {
		if (fillContent(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	private <T extends ContentDetailDTO> T fillContentDetail(T dto, Node from) {
		if (fillAbstractContentDetail(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	private <T extends CompleteContentDTO> T fillCompleteContent(T dto, Node from) {
		if (fillAbstractContentDetail(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	final Function<Node, ContentDTO> node2content = new Function<Node, ContentDTO>() {
		public ContentDTO apply(Node from) {
			if (from == null) {
				return null;
			}
			return fillContent(new ContentDTO(), from);
		}
	};

	final Function<Node, ContentDetailDTO> node2contentDetail = new Function<Node, ContentDetailDTO>() {
		public ContentDetailDTO apply(Node from) {
			if (from == null) {
				return null;
			}
			return fillContentDetail(new ContentDetailDTO(), from);
		}
	};

	final Function<Node, CompleteContentDTO> node2completeContent = new Function<Node, CompleteContentDTO>() {
		public CompleteContentDTO apply(Node from) {
			if (from == null) {
				return null;
			}
			return fillCompleteContent(new CompleteContentDTO(), from);
		}
	};

	private <T extends ContentDTO> T fillContent(T dto, Content from) {
		if (from == null) {
			return null;
		}
		ContentRefDTO ref = contentRef(from.getContentKey());
		if (ref == null) {
			return null;
		}
		dto.setRef(ref);
		dto.setTitle(from.getTitle());
		dto.setDate(c2d(from.getDate()));
		return dto;
	}

	private <T extends AbstractContentDetailDTO> T fillAbstractContentDetail(T dto, Content from) {
		if (fillContent(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	private <T extends ContentDetailDTO> T fillContentDetail(T dto, Content from) {
		if (fillAbstractContentDetail(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	private <T extends CompleteContentDTO> T fillCompleteContent(T dto, Content from) {
		if (fillAbstractContentDetail(dto, from) == null) {
			return null;
		}
		// TODO
		return dto;
	}

	final Function<Content, ContentDTO> c2content = new Function<Content, ContentDTO>() {
		public ContentDTO apply(Content from) {
			if (from == null) {
				return null;
			}
			return fillContent(new ContentDTO(), from);
		}
	};

	final Function<Content, ContentDetailDTO> c2contentDetail = new Function<Content, ContentDetailDTO>() {
		public ContentDetailDTO apply(Content from) {
			if (from == null) {
				return null;
			}
			return fillContentDetail(new ContentDetailDTO(), from);
		}
	};

	final Function<Content, CompleteContentDTO> c2completeContent = new Function<Content, CompleteContentDTO>() {
		public CompleteContentDTO apply(Content from) {
			if (from == null) {
				return null;
			}
			return fillCompleteContent(new CompleteContentDTO(), from);
		}
	};

	private static <T> Iterable<T> notNull(Iterable<T> from) {
		return Iterables.filter(from, Predicates.notNull());
	}

	static <F, T> List<T> list(Iterable<F> from, Function<F, T> f) {
		return newArrayList(notNull(transform(from, f)));
	}

	final List<ContentTypeRefDTO> getContentTypes() {
		return list(ia.getContentTypes().values(), contentType);
	}

}
