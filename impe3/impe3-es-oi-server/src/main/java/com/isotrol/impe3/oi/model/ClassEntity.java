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
package com.isotrol.impe3.oi.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.common.collect.Sets;
import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;


/**
 * Entity that represents a classification.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_CLASS")
@NamedQueries( {
	@NamedQuery(name = ClassEntity.BY_KEY, query = "from ClassEntity as e where e.set.id = ? and e.name.id = ?"),
	@NamedQuery(name = ClassEntity.USED, query = ClassEntity.Q_USED),
	@NamedQuery(name = ClassEntity.BY_SET_NAME, query = "from ClassEntity as e inner join fetch e.name where e.set.name = ? order by e.name.name")})
public class ClassEntity extends VersionedLongIdEntity {
	/** Query by class set name. */
	public static final String BY_KEY = "class.byKey";
	/** Used classes loading. */
	public static final String USED = "class.used";
	/** All classes loading. */
	public static final String BY_SET_NAME = "class.bySetName";

	/** All groups query. */
	static final String Q_USED = "select distinct e from ClassEntity e inner join fetch e.name left join fetch e.interviews";

	/** Class set. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLST_CLSS_ID", nullable = false, updatable = false)
	private ClassSetEntity set;

	/** Class name. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CLST_CLSN_ID", nullable = false)
	private ClassNameEntity name;


	/** Classificated interviews. */
	@ManyToMany(mappedBy = "classes")
	private Set<InterviewEntity> interviews;

	/** Constructor. */
	public ClassEntity() {
	}

	/**
	 * Returns the class set.
	 * @return The class set.
	 */
	public ClassSetEntity getSet() {
		return set;
	}

	/**
	 * Sets the class set.
	 * @param set The class set.
	 */
	public void setSet(ClassSetEntity set) {
		this.set = set;
	}

	/**
	 * Returns the class name.
	 * @return The class name.
	 */
	public ClassNameEntity getName() {
		return name;
	}

	/**
	 * Sets the class name.
	 * @param name The class name.
	 */
	public void setName(ClassNameEntity name) {
		this.name = name;
	}


	/**
	 * Return the classificated interviews.
	 * @return The classificated interviews.
	 */
	public Set<InterviewEntity> getInterviews() {
		if (interviews == null) {
			interviews = Sets.newHashSet();
		}
		return interviews;
	}

	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return ClassEntity.class;
	}

}
