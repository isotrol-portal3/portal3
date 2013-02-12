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

package com.isotrol.impe3.support.nr;


import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;


/**
 * A content from a node repository.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class ContentNode implements Content {
	/** Known content types. */
	private final ContentTypes contentTypes;
	/** Known categories. */
	private final Categories categoriesHierarchy;
	private final Node node;
	private ImmutableSet<Category> categories = null;
	private ImmutableSet<ContentKey> related = null;
	/** Local values store */
	private Map<String, Object> local = null;

	public ContentNode(final ContentTypes contentTypes, final Categories categories, final Node node) {
		this.contentTypes = contentTypes;
		this.categoriesHierarchy = categories;
		this.node = node;
		this.local = Maps.newHashMap();
	}

	private ContentKey toContentKey(NodeKey key) {
		if (key == null || contentTypes == null) {
			return null;
		}
		final UUID typeId = key.getNodeType();
		if (typeId == null) {
			return null;
		}
		final ContentType type = contentTypes.get(typeId);
		if (type == null) {
			return null;
		}
		return ContentKey.of(type, key.getNodeId());
	}

	/**
	 * Returns original node object
	 * @return original node object.
	 */
	public Node getNode() {
		return node;
	}

	/**
	 * Returns immutable categories ids set.
	 * @return categories id set.
	 */
	public Set<UUID> getCategoriesId() {
		return node.getCategories();
	}

	/**
	 * returns content id
	 */
	public String getContentId() {
		final NodeKey key = getNodeKey();
		if (key == null) {
			return null;
		}
		return key.getNodeId();
	}

	/**
	 * returns content key
	 */
	public ContentKey getContentKey() {
		final NodeKey key = getNodeKey();
		if (key == null) {
			return null;
		}
		return toContentKey(key);
	}

	public Set<String> getSets() {
		return node.getSets();
	}

	/**
	 * Return categories asociated to current content
	 * @return categories-
	 */
	public Set<Category> getCategories() {
		if (categories != null) {
			return categories;
		}
		final Set<UUID> nodes = getCategoriesId();
		if (nodes == null) {
			return null;
		}
		final Set<Category> set = Sets.newHashSet();
		for (final UUID id : nodes) {
			final Category c = categoriesHierarchy.get(id);
			if (c != null) {
				set.add(c);
			}
		}
		categories = ImmutableSet.copyOf(set);
		return categories;
	}

	/**
	 * Returns highlighted fields as list of string fragments.
	 */
	public Map<String, Collection<String>> getHighlighted() {
		Multimap<String, String> multimap = node.getHighlight();
		if (multimap == null) {
			return ImmutableMap.of();
		}
		return multimap.asMap();
	}

	/**
	 * Returns content bytes
	 */
	public InputStream getContent() {
		return node.getContent();
	}

	/**
	 * returns content date
	 */
	public Calendar getDate() {
		return node.getDate();
	}

	/**
	 * returns content expiration date
	 */
	public Calendar getExpirationDate() {
		return node.getExpirationdate();
	}

	/**
	 * returns content release date
	 */
	public Calendar getReleaseDate() {
		return node.getReleasedate();
	}

	/**
	 * returns content description
	 */
	public String getDescription() {
		return node.getDescription();
	}

	/**
	 * @see com.isotrol.impe3.api.content.Content#isDefaultLocale()
	 */
	public boolean isDefaultLocale() {
		return node.isDefaultLocale();
	}

	/**
	 * @see com.isotrol.impe3.api.content.Content#getLocales()
	 */
	public Set<Locale> getLocales() {
		return node.getLocales();
	}

	/**
	 * @see com.isotrol.impe3.api.content.Content#getOtherLocales()
	 */
	public Set<Locale> getOtherLocales() {
		return node.getOtherLocales();
	}

	/**
	 * returns content mime type
	 */
	public String getMime() {
		return node.getMime();
	}

	/**
	 * returns node key
	 * @return node key
	 */
	public NodeKey getNodeKey() {
		if (node == null) {
			return null;
		}
		return node.getNodeKey();
	}

	/**
	 * Return the content type from current node key
	 * @return content type
	 */
	public ContentType getContentType() {
		final ContentKey key = toContentKey(node.getNodeKey());
		if (key == null) {
			return null;
		}
		return key.getContentType();
	}

	/**
	 * returns content properties
	 */
	public Map<String, Collection<String>> getProperties() {
		Multimap<String, String> multimap = node.getProperties();
		if (multimap == null) {
			return ImmutableMap.of();
		}
		return multimap.asMap();
	}

	/**
	 * @see com.isotrol.impe3.api.content.Content#getBlobs()
	 */
	public Map<String, byte[]> getBlobs() {
		return node.getBlobs();
	}

	/**
	 * returns collection of contentkey represents related content
	 */
	public Set<ContentKey> getRelatedContentKey() {
		if (related != null) {
			return related;
		}
		if (node == null) {
			related = ImmutableSet.of();
			return related;
		}
		final Set<NodeKey> nodeKeys = node.getRelatedContent();
		if (nodeKeys == null || nodeKeys.isEmpty()) {
			related = ImmutableSet.of();
			return related;
		}
		final Set<ContentKey> set = Sets.newHashSet();
		for (NodeKey nk : nodeKeys) {
			final ContentKey ck = toContentKey(nk);
			if (ck != null) {
				set.add(ck);
			}
		}
		related = ImmutableSet.copyOf(set);
		return related;
	}

	/**
	 * returns content title
	 */
	public String getTitle() {
		return node.getTitle();
	}

	/**
	 * @see com.isotrol.impe3.api.content.Content#getLocalValues()
	 */
	public Map<String, Object> getLocalValues() {
		return local;
	}
}
