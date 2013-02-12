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

package com.isotrol.impe3.nr.api;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static com.isotrol.impe3.nr.api.Util.STRING2UUID;
import static com.isotrol.impe3.nr.api.Util.safeStrings2Locales;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;


/**
 * Node definition.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public final class Node implements Serializable {

	private static final long serialVersionUID = -7733005008635431438L;

	private static Set<String> ONLY_ALL_LOCALES = ImmutableSet.of(Schema.ALL_LOCALES);

	/** node type plus node id */
	private NodeKey nodeKey;

	/** node title */
	private String title;
	/** node description */
	private String description;
	/** node order date */
	private Calendar date;
	/** node release date */
	private Calendar releasedate;
	/** node expiration date */
	private Calendar expirationdate;

	/** node content mime */
	private String mime;

	/** node content */
	private byte[] bytes;
	/** Is the content compressed. */
	private boolean compressed;

	/** Node aditional properties */
	private transient ImmutableListMultimap<String, String> properties = null;
	/** Hessian hack. */
	private Map<String, Collection<String>> propertiesMap;
	/** Blob properties. */
	private Map<String, byte[]> blobs = null;

	/** Node sets */
	private Set<String> sets = new HashSet<String>();
	/** Main category. */
	private String mainCategory = null;
	/** Node categories */
	private Set<String> categories = Sets.newHashSet();
	/** Whether this node is in the default locale. */
	private boolean defaultLocale = true;
	/** Node locales (as strings). */
	private Set<String> stringLocales = Sets.newHashSet();
	/** Other node locales (as strings). */
	private Set<String> stringOtherLocales = Sets.newHashSet();
	/** Node locales. */
	private transient ImmutableSet<Locale> locales = null;
	/** Other Node locales. */
	private transient ImmutableSet<Locale> otherLocales = null;
	/** Related nodes */
	private Set<NodeKey> related = Sets.newHashSet();

	/** node highlight content */
	private transient ImmutableMultimap<String, String> highlight = null;
	/** Hessian hack. */
	private Map<String, Collection<String>> highlightMap;

	/**
	 * Gets the node builder, the only way to create a node.
	 * @return the node builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	private Node() {
		categories = new HashSet<String>();
		sets = new HashSet<String>();
	}

	public NodeKey getNodeKey() {
		return nodeKey;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Calendar getDate() {
		return date;
	}

	public Calendar getExpirationdate() {
		return expirationdate;
	}

	public Calendar getReleasedate() {
		return releasedate;
	}

	public Set<String> getSets() {
		return ImmutableSet.copyOf(sets);
	}

	public UUID getMainCategory() {
		return mainCategory == null ? null : STRING2UUID.apply(mainCategory);
	}

	public Set<UUID> getCategories() {
		return ImmutableSet.copyOf(Collections2.transform(categories, STRING2UUID));
	}

	/**
	 * Returns whether the node is the default locale.
	 * @return True if the node is the default locale.
	 */
	public boolean isDefaultLocale() {
		return defaultLocale;
	}

	private ImmutableSet<Locale> toLocaleSet(Set<String> set) {
		if (set == null || set.isEmpty() || ONLY_ALL_LOCALES.equals(set)) {
			return ImmutableSet.of();
		}
		return ImmutableSet.copyOf(safeStrings2Locales(set));
	}

	/**
	 * Returns the node locales.
	 * @return The node locales.
	 */
	public Set<Locale> getLocales() {
		if (locales == null) {
			locales = toLocaleSet(stringLocales);
		}
		return locales;
	}

	/**
	 * Returns the other node locales.
	 * @return The other node locales.
	 */
	public ImmutableSet<Locale> getOtherLocales() {
		if (otherLocales == null) {
			otherLocales = toLocaleSet(stringOtherLocales);
		}
		return otherLocales;
	}

	public Multimap<String, String> getHighlight() {
		if (highlight == null) {
			if (highlightMap != null) {
				ImmutableMultimap.Builder<String, String> b = ImmutableMultimap.builder();
				for (Entry<String, Collection<String>> entry : highlightMap.entrySet()) {
					final Collection<String> values = entry.getValue();
					if (values != null) {
						b.putAll(entry.getKey(), values);
					}
				}
				highlight = b.build();
			} else {
				highlight = ImmutableMultimap.of();
			}
		}
		return highlight;
	}

	public Set<NodeKey> getRelatedContent() {
		return ImmutableSet.copyOf(related);
	}

	public Multimap<String, String> getProperties() {
		if (properties == null) {
			if (propertiesMap != null) {
				ImmutableListMultimap.Builder<String, String> b = ImmutableListMultimap.builder();
				for (Entry<String, Collection<String>> entry : propertiesMap.entrySet()) {
					final Collection<String> values = entry.getValue();
					if (values != null) {
						b.putAll(entry.getKey(), values);
					}
				}
				properties = b.build();
			} else {
				properties = ImmutableListMultimap.of();
			}
		}
		return properties;
	}

	public Map<String, byte[]> getBlobs() {
		if (blobs == null) {
			return ImmutableMap.of();
		}
		return Collections.unmodifiableMap(blobs);
	}

	public String getMime() {
		return mime;
	}

	private void setNodeKey(NodeKey nodeKey) {
		this.nodeKey = nodeKey;
	}

	private void setTitle(String title) {
		this.title = title;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	private void setDate(Calendar date) {
		this.date = date;
	}

	private void setExpirationdate(Calendar expirationdate) {
		this.expirationdate = expirationdate;
	}

	private void setReleasedate(Calendar releasedate) {
		this.releasedate = releasedate;
	}

	private void setMime(String mime) {
		this.mime = mime;
	}

	private void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	private void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	private void setProperties(Multimap<String, String> properties) {
		this.propertiesMap = Maps.newHashMap(properties.asMap());
	}

	private void setBlobs(Map<String, byte[]> blobs) {
		this.blobs = Maps.newHashMap(blobs);
	}

	private void setSets(Set<String> sets) {
		this.sets = sets;
	}

	private void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	private void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	private void setLocales(Set<String> locales) {
		if (locales == null) {
			this.stringLocales = null;
			this.defaultLocale = true;
		} else {
			this.defaultLocale = locales.contains(Schema.ALL_LOCALES);
			this.stringLocales = newHashSet(locales);
			if (this.defaultLocale) {
				this.stringLocales.remove(Schema.ALL_LOCALES);
			}
		}
		this.locales = null;
	}

	private void setOtherLocales(Set<String> otherLocales) {
		this.stringOtherLocales = otherLocales;
		this.otherLocales = null;
	}

	private void setRelated(Set<NodeKey> related) {
		this.related = related;
	}

	private void setHighlight(Multimap<String, String> highlight) {
		this.highlightMap = Maps.newHashMap(highlight.asMap());
	}

	/**
	 * Return de inputstream contains the bytes.
	 * @return the input stream
	 */
	public InputStream getContent() {
		if (bytes == null) {
			return null;
		}
		try {
			if (compressed) {
				return new GZIPInputStream(new ByteArrayInputStream(bytes));
			} else {
				return new ByteArrayInputStream(bytes);
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Node builder.
	 * 
	 * @author Emilio Escobar Reyero
	 */
	public static final class Builder {
		private NodeKey nodeKey;
		private String title;
		private String description;
		private Calendar date;
		private Calendar expirationdate;
		private Calendar releasedate;
		private String mime;
		private byte[] bytes;
		private boolean compressed = false;
		private ListMultimap<String, String> properties = LinkedListMultimap.create();
		private Map<String, byte[]> blobs = Maps.newHashMap();
		private Set<String> sets = newHashSet();
		private String mainCategory = null;
		private Set<String> categories = newHashSet();
		private Set<String> locales = newHashSet();
		private Set<String> otherLocales = newHashSet();
		private Set<NodeKey> related = newHashSet();
		private Multimap<String, String> highlight = HashMultimap.create();

		/**
		 * NodeKey setter
		 * @param nodeKey the node key.
		 * @return fluid builder.
		 */
		public Builder setNodeKey(NodeKey nodeKey) {
			this.nodeKey = nodeKey;
			return this;
		}

		/**
		 * tittle setter
		 * @param title node tittle
		 * @return fluid builder
		 */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/**
		 * description setter
		 * @param description node description
		 * @return fluid builder
		 */
		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		/**
		 * date setter
		 * @param date node order date
		 * @return fluid builder
		 */
		public Builder setDate(Calendar date) {
			this.date = date;
			return this;
		}

		/**
		 * expiration date setter
		 * @param expirationdate node expiration date
		 * @return fluid builder
		 */
		public Builder setExpirationdate(Calendar expirationdate) {
			this.expirationdate = expirationdate;
			return this;
		}

		/**
		 * release date setter
		 * @param releasedate node release date
		 * @return fluid builder
		 */
		public Builder setReleasedate(Calendar releasedate) {
			this.releasedate = releasedate;
			return this;
		}

		/**
		 * Adds a category to the node
		 * @param categoryKey the category node key
		 * @return fluid builder
		 */
		public Builder addCategory(UUID categoryKey) {
			categories.add(categoryKey.toString());
			return this;
		}

		/**
		 * Adds a set to the node
		 * @param set Set node set to add.
		 * @return fluid builder
		 */
		public Builder addSet(String set) {
			if (set != null) {
				sets.add(set);
			}
			return this;
		}

		/**
		 * Sets the node main category.
		 * @param categoryId The main category id.
		 * @return fluid builder
		 */
		public Builder setMainCategory(UUID categoryId) {
			if (categoryId != null) {
				String c = categoryId.toString();
				mainCategory = c;
				categories.add(c);
			}
			return this;
		}

		public Builder setDefaultLocale(boolean isDefault) {
			if (isDefault) {
				locales.add(Schema.ALL_LOCALES);
			} else {
				locales.remove(Schema.ALL_LOCALES);
			}
			return this;
		}

		public Builder addLocale(String locale) {
			if (locale != null) {
				locales.add(locale);
			}
			return this;
		}

		public Builder addOtherLocale(String locale) {
			if (locale != null) {
				otherLocales.add(locale);
			}
			return this;
		}

		/**
		 * Adds a new property
		 * @param key property name
		 * @param value property value
		 * @return fluid builder
		 */
		public Builder addProperty(String key, String value) {
			checkNotNull(key);
			checkNotNull(value);
			checkArgument(!key.startsWith(Schema.SCHEMA_NAME));
			properties.put(key, value);
			return this;
		}

		/**
		 * Adds a new blob property
		 * @param key property name
		 * @param value property value
		 * @return fluid builder
		 */
		public Builder addBlob(String key, byte[] value) {
			checkNotNull(key);
			checkNotNull(value);
			checkArgument(key.startsWith(Schema.BLOB_PREFIX));
			blobs.put(key, value);
			return this;
		}

		/**
		 * Adds a node related identify by its node key
		 * @param contentKey related node node key
		 * @return fluid builder
		 */
		public Builder addRelatedContent(NodeKey contentKey) {
			related.add(contentKey);
			return this;
		}

		/**
		 * Adds a highlight text fragment results of a search.
		 * @param field field name
		 * @param fragment text highlight
		 * @return fuild builder
		 */
		public Builder addHighlightFragment(String field, String fragment) {
			Preconditions.checkNotNull(field);
			Preconditions.checkNotNull(fragment);
			highlight.put(field, fragment);
			return this;
		}

		/**
		 * mime setter. Content mime type plus content encoding. For example, text/html; charset=utf-8
		 * @param mime node mime type
		 * @return fluid builder
		 */
		public Builder setMime(String mime) {
			this.mime = mime;
			return this;
		}

		/**
		 * content setter. True compressed value sign bytes comes gzip
		 * @param bytes node content bytes
		 * @param compressed if true bytes comes gzip
		 * @return fluid builder
		 */
		public Builder setBytes(byte[] bytes, boolean compressed) {
			this.bytes = bytes;
			this.compressed = compressed;
			return this;
		}

		/**
		 * Create a new node.
		 * @return the node.
		 */
		public Node build() {
			Node node = new Node();

			node.setNodeKey(nodeKey);
			node.setTitle(title);
			node.setDescription(description);
			node.setDate(date);
			node.setMime(mime);
			node.setBytes(bytes);
			node.setCompressed(compressed);
			node.setProperties(properties);
			node.setBlobs(blobs);
			node.setSets(sets);
			node.setMainCategory(mainCategory);
			node.setCategories(categories);
			node.setLocales(locales);
			node.setOtherLocales(otherLocales);
			node.setRelated(related);
			node.setHighlight(highlight);
			node.setExpirationdate(expirationdate);
			node.setReleasedate(releasedate);

			return node;
		}

	}
}
