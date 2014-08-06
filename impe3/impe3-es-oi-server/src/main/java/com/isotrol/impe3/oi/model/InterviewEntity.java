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


import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.MapKey;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.hib.model.VersionedEntity;



/**
 * Entity that represents an Interview.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_INTERVIEW")
public class InterviewEntity extends VersionedEntity {
	/** Not deleted predicate. */
	public static final Predicate<InterviewEntity> NOT_DELETED = new Predicate<InterviewEntity>() {
		public boolean apply(InterviewEntity input) {
			return !input.isDeleted();
		}
	};
	/** Title. */
	@Column(name = "INTE_TITLE", length = Lengths.TITLE, nullable = false)
	private String title;
	
	/** Description. */
	@Column(name = "INTE_DESCRIPTION", length = Lengths.LONGTEXT, nullable = true)
	private String description;
	
	/** Interviewed. */
	@Column(name = "INTE_INTERVIEWEE", length = Lengths.TITLE, nullable = true)
	private String interviewee;
	
	/** Author. */
	@Column(name = "INTE_AUTHOR", length = Lengths.TITLE, nullable = true)
	private String author;
	
	/** True if new questions are allowed. */
	@Column(name = "INTE_ALLOWED", nullable = false)
	private Boolean newQuestionsAllowed;
	
	/** Deleted interview. */
	@Column(name = "INTE_DELETED", nullable = false)
	private boolean deleted = false;

	/** Timestamp. */
	@Column(name = "INTE_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;
	
	/** Release date. */
	@Column(name = "INTE_RELEASE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar release;

	/** Expiration date. */
	@Column(name = "INTE_EXPIRATION", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar expiration;
	
	/** Classes. */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "OI_INTERVIEW_CLASS", joinColumns = {@JoinColumn(name = "INTE_ID")}, inverseJoinColumns = {@JoinColumn(name = "CLST_ID")})
	private Set<ClassEntity> classes;

	/** Interview properties. */
	@CollectionOfElements
	@JoinTable(name = "OI_INTERVIEW_PROPERTY", joinColumns = @JoinColumn(name = "INTE_ID", nullable = false))
	@MapKey(columns = @Column(name = "INTE_PROPERTY_NAME", length = Lengths.NAME))
	@Column(name = "INTE_PROPERTY_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;
	
	/** constructor. */
	public InterviewEntity() {
		super();
	}

	/**
	 * Returns the title.
	 * @return The title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 * @param title The title.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Returns the description.
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the interviewee name or description.
	 * @return The interviewee name or description.
	 */
	public String getInterviewee() {
		return interviewee;
	}
	
	/**
	 * Sets the interviewee name or description.
	 * @param interviewee The interviewee name or description..
	 */
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
	
	/**
	 * Returns the interview author name.
	 * @return The interview author name.
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Sets the interview author name.
	 * @param author The interview author name.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Returns whether the interview accept new questions.
	 * @return True if the interview accept new questions.
	 */
	public boolean isNewQuestionsAllowed() {
		return newQuestionsAllowed;
	}

	/**
	 * Sets whether the interview accept new questions.
	 * @param deleted True if the interview accept new questions.
	 */
	public void setNewQuestionsAllowed(boolean newQuestionsAllowed) {
		this.newQuestionsAllowed = newQuestionsAllowed;
	}
	
	/**
	 * Returns whether the interview is deleted.
	 * @return True if the interview is deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the interview is deleted.
	 * @param deleted True if the interview is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * Returns the insertion date.
	 * @return The insertion date.
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Sets the insertion date.
	 * @param date The insertion date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Returns the release date.
	 * @return The release date.
	 */
	public Calendar getRelease() {
		return release;
	}
	
	/**
	 * Sets the release date.
	 * @param release The release date.
	 */
	public void setRelease(Calendar release) {
		this.release = release;
	}
	
	/**
	 * Returns the expiration date.
	 * @return the expiration date.
	 */
	public Calendar getExpiration() {
		return expiration;
	}
	
	/**
	 * Sets the expiration date.
	 * @param expiration The expiration date.
	 */
	public void setExpiration(Calendar expiration) {
		this.expiration = expiration;
	}
	
	
	/**
	 * Returns the resource classes.
	 * @return The resource classes.
	 */
	public Set<ClassEntity> getClasses() {
		if (classes == null) {
			classes = Sets.newHashSet();
		}
		return classes;
	}
	
	/**
	 * Returns the interview properties.
	 * @return The interview properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}
}
