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
package com.isotrol.impe3.web20.model;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;


/**
 * Entity that represents a tag.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_TAG")
@NamedQueries( {
	@NamedQuery(name = TagEntity.BY_KEY, query = "from TagEntity as e where e.set.id = ? and e.name.id = ?"),
	@NamedQuery(name = TagEntity.USED, query = TagEntity.Q_USED),
	@NamedQuery(name = TagEntity.BY_SET_NAME, query = "from TagEntity as e inner join fetch e.name where e.set.name = ? order by e.name.name")})
public class TagEntity extends VersionedLongIdEntity {
	/** Query by tag set name. */
	public static final String BY_KEY = "tag.byKey";
	/** Used tags loading. */
	public static final String USED = "tag.used";
	/** All tags loading. */
	public static final String BY_SET_NAME = "tag.bySetName";

	/** All groups query. */
	static final String Q_USED = "select distinct e from TagEntity e inner join fetch e.name left join fetch e.resources where e.valid is true";

	/** Valid tag predicate. */
	public static final Predicate<TagEntity> IS_VALID = new Predicate<TagEntity>() {
		public boolean apply(TagEntity input) {
			return input.valid;
		}
	};

	/** Tag set. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "TAGT_TAGS_ID", nullable = false, updatable = false)
	private TagSetEntity set;

	/** Tag name. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "TAGT_TAGN_ID", nullable = false)
	private TagNameEntity name;

	/** Whether the tag is valid. */
	@Column(name = "TAGT_VALID", nullable = false)
	private boolean valid = false;

	/** Tagged resources. */
	@ManyToMany(mappedBy = "tags")
	private Set<ResourceEntity> resources;

	/** Constructor. */
	public TagEntity() {
	}

	/**
	 * Returns the tag set.
	 * @return The tag set.
	 */
	public TagSetEntity getSet() {
		return set;
	}

	/**
	 * Sets the tag set.
	 * @param set The tag set.
	 */
	public void setSet(TagSetEntity set) {
		this.set = set;
	}

	/**
	 * Returns the tag name.
	 * @return The tag name.
	 */
	public TagNameEntity getName() {
		return name;
	}

	/**
	 * Sets the tag name.
	 * @param name The tag name.
	 */
	public void setName(TagNameEntity name) {
		this.name = name;
	}

	/**
	 * Returns whether the tag is valid.
	 * @return True if the tag is valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets whether the tag is valid.
	 * @param valid True if the tag is valid.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * Return the tagged resources.
	 * @return The tagged resources.
	 */
	public Set<ResourceEntity> getResources() {
		if (resources == null) {
			resources = Sets.newHashSet();
		}
		return resources;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return TagEntity.class;
	}

}
