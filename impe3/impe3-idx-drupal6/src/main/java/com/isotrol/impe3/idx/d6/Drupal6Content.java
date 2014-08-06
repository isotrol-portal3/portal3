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
package com.isotrol.impe3.idx.d6;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


/**
 * Drupal 6 Content DTO
 */
public final class Drupal6Content {

	/** Node id */
	private final int nid;

	/** Version node */
	private final int vid;

	/** Node type */
	private final String type;

	/** Language */
	private final String language;

	/** Title */
	private final String title;

	/** User id that */
	private final int uid;

	/** Wether the node is published */
	private final int status;

	/** When the node was created */
	private final Date created;

	/** When the node was most recently saved */
	private final Date changed;

	/** Whether comments are allowed on this node. 0: No, 1: Read-only, 2: Read/Write */
	private final int comment;

	/** Wheter the node should be displayed on the front page */
	private final int promote;

	/** Not used */
	private final int moderate;

	/** Whether the node should be displayed at the top of lists */
	private final int sticky;

	/** The translation set id for this node, which equals the node id of source post in each set */
	private final int tnid;

	/** Whethet this translaion page needs to be updated */
	private final int translate;

	/** XML that represent the node */
	private final XML xml;

	/** Especific fields */
	private final ImmutableMap<String, String> fields;

	/** Taxonomy */
	private final ImmutableList<String> categories;

	private Drupal6Content(Drupal6ContentBuilder builder) {
		this.nid = builder.nid;
		this.vid = builder.vid;
		this.type = builder.type;
		this.language = builder.language;
		this.title = builder.title;
		this.uid = builder.uid;
		this.status = builder.status;
		this.created = builder.created;
		this.changed = builder.changed;
		this.comment = builder.comment;
		this.promote = builder.promote;
		this.moderate = builder.moderate;
		this.sticky = builder.sticky;
		this.tnid = builder.tnid;
		this.translate = builder.translate;
		this.xml = builder.xml;
		this.fields = ImmutableMap.copyOf(builder.fields);
		this.categories = ImmutableList.copyOf(builder.categories);
	}

	/**
	 * Returns a new instantiation of Drupal6ContentBuilder
	 * @return a new Drupal6ContentBuilder
	 */
	public static Drupal6ContentBuilder builder() {
		return new Drupal6ContentBuilder();
	}

	public int getNid() {
		return nid;
	}

	public int getVid() {
		return vid;
	}

	public String getType() {
		return type;
	}

	public String getLanguage() {
		return language;
	}

	public String getTitle() {
		return title;
	}

	public int getUid() {
		return uid;
	}

	public int getStatus() {
		return status;
	}

	public Date getCreated() {
		return created;
	}

	public Date getChanged() {
		return changed;
	}

	public int getComment() {
		return comment;
	}

	public int getPromote() {
		return promote;
	}

	public int getModerate() {
		return moderate;
	}

	public int getSticky() {
		return sticky;
	}

	public int getTnid() {
		return tnid;
	}

	public int getTranslate() {
		return translate;
	}

	public XML getXml() {
		return xml;
	}

	public ImmutableMap<String, String> getFields() {
		return fields;
	}

	public ImmutableList<String> getCategories() {
		return categories;
	}

	/**
	 * Clase Interna
	 */
	public static class Drupal6ContentBuilder {

		private int nid;
		private int vid;
		private String type;
		private String language;
		private String title;
		private int uid;
		private int status;
		private Date created;
		private Date changed;
		private int comment;
		private int promote;
		private int moderate;
		private int sticky;
		private int tnid;
		private int translate;
		private XML xml;
		private Map<String, String> fields = new HashMap<String, String>();
		private List<String> categories = new LinkedList<String>();

		/**
		 * Adds or modifies a property.
		 * 
		 * @param key property key
		 * @param value property value
		 */
		public Drupal6ContentBuilder addField(String key, String value) {
			this.fields.put(key, value);
			return this;
		}

		/**
		 * Adds a category in categories list.
		 * 
		 * @param category channel id.
		 */
		public Drupal6ContentBuilder addCategory(String category) {
			this.categories.add(category);
			return this;
		}

		/**
		 * Returns a new instantiation of Drupal6Content
		 * @return
		 */
		public Drupal6Content build() {
			return new Drupal6Content(this);
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setNid(int arg) {
			this.nid = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setVid(int arg) {
			this.vid = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setType(String arg) {
			this.type = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setLanguage(String arg) {
			this.language = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setTitle(String arg) {
			this.title = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setUid(int arg) {
			this.uid = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setStatus(int arg) {
			this.status = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setCreated(Date arg) {
			this.created = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setChanged(Date arg) {
			this.changed = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setComment(int arg) {
			this.comment = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setPromote(int arg) {
			this.promote = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setModerate(int arg) {
			this.moderate = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setSticky(int arg) {
			this.sticky = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setTnid(int arg) {
			this.tnid = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setTranslate(int arg) {
			this.translate = arg;
			return this;
		}

		/**
		 * Setter
		 */
		public Drupal6ContentBuilder setXml(XML arg) {
			this.xml = arg;
			return this;
		}

		public int getNid() {
			return nid;
		}

		public String getTitle() {
			return title;
		}

		public String getType() {
			return type;
		}
	}
}
