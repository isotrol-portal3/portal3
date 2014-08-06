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
package com.isotrol.impe3.oi.api;

import java.util.Date;

import com.isotrol.impe3.dto.AbstractVersionedStringId;

/**
 * DTO lite for interviews. 
 * @author Emilio Escobar Reyero
 */
public class InterviewSelDTO extends AbstractVersionedStringId {
	/** serial uid.  */
	private static final long serialVersionUID = 3251394960524337514L;
	
	/** Title. */
	private String title;
	/** Cutted description. */
	private String shortDescription;
	/** Interviewee. */
	private String interviewee;
	/** Author. */
	private String author;
	/** Questions allowed. */
	private Boolean newQuestionsAllowed;
	/** Releasing date. */
	private Date release;
	/** Expiration date. */
	private Date expiration;

	/** Constructor. */
	public InterviewSelDTO() {
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
	 * Returns cutted description.
	 * @return The cutted description.
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	
	/**
	 * Sets the cutted description. 
	 * @param shortDescription The cutted descripiption.
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	/**
	 * Returns the interviewee. 
	 * @return The interviewee.
	 */
	public String getInterviewee() {
		return interviewee;
	}
	
	/**
	 * Sets the interviewee.
	 * @param interviewee The interviewee.
	 */
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
	
	/**
	 * Returns the author.
	 * @return The author.
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Sets the author.
	 * @param author The author.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	
	/**
	 * Returns whether new questions are allowed.
	 * @return True if new questions are allowed.
	 */
	public Boolean isNewQuestionsAllowed() {
		return newQuestionsAllowed;
	}
	
	/**
	 * Sets whether new questions are allowed.
	 * @param deleted True if new questions are allowed.
	 */
	public void setNewQuestionsAllowed(Boolean newQuestionsAllowed) {
		this.newQuestionsAllowed = newQuestionsAllowed;
	}
	
	/**
	 * Returns the release date.
	 * @return The release date.
	 */
	public Date getRelease() {
		return release;
	}
	
	/**
	 * Sets the release date.
	 * @param release The release date.
	 */
	public void setRelease(Date release) {
		this.release = release;
	}
	
	/**
	 * Returns the expiration date.
	 * @return The expiration date.
	 */
	public Date getExpiration() {
		return expiration;
	}
	
	/**
	 * Sets the expiration date.
	 * @param expiration The expiration date.
	 */
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
}
