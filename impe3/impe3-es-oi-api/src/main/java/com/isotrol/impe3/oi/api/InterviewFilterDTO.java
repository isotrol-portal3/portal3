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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.dto.StringFilterDTO;


/**
 * Interview Filter.
 * @author Emilio Escobar Reyero.
 */
public class InterviewFilterDTO implements Serializable {
	/** serial uid. */
	private static final long serialVersionUID = 939819735213038538L;

	
	public static int PENDING = -1;
	public static int ONAIR = 0;
	public static int ENDED = 1;
	
	/** Title. */
	private StringFilterDTO title;
	/** Interviewee*/
	private StringFilterDTO interviewee;
	/** Author. */
	private StringFilterDTO author;
	/** Description. */
	private StringFilterDTO description;
	
	/** null means all, and PENDING, ONAIR, or ENDED. */
	private Integer active;

	/** classifications */
	private Map<String, Set<String>> classification;
	/** null means all, true classifications included, false classifications excluded. */
	private boolean inverse = false;
	
	private Map<String, StringFilterDTO> properties;
	
	/** Constructor. */
	public InterviewFilterDTO() {
	}
	
	/**
	 * Returns the title.
	 * @return The title.
	 */
	public StringFilterDTO getTitle() {
		return title;
	}
	/**
	 * Sets the title.
	 * @param title The title.
	 */
	public void setTitle(StringFilterDTO title) {
		this.title = title;
	}
	/**
	 * Sets the title.
	 * @param title The title.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putTitle(StringFilterDTO title) {
		this.title = title;
		return this;
	}
	
	/**
	 * Returns the author.
	 * @return The author.
	 */
	public StringFilterDTO getAuthor() {
		return author;
	}
	/**
	 * Sets the author.
	 * @param author The author.
	 */
	public void setAuthor(StringFilterDTO author) {
		this.author = author;
	}
	/**
	 * Sets the author.
	 * @param author The author.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putAuthor(StringFilterDTO author) {
		this.author = author;
		return this;
	}
	
	/**
	 * Returns the interviewee.
	 * @return The interviewee.
	 */
	public StringFilterDTO getInterviewee() {
		return interviewee;
	}
	/**
	 * Sets the interviewee.
	 * @param interviewee The interviewee.
	 */
	public void setInterviewee(StringFilterDTO interviewee) {
		this.interviewee = interviewee;
	}
	/**
	 * Sets the interviewee.
	 * @param interviewee The interviewee.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putInterviewee(StringFilterDTO interviewee) {
		this.interviewee = interviewee;
		return this;
	}
	
	/**
	 * Returns the description.
	 * @return The description.
	 */
	public StringFilterDTO getDescription() {
		return description;
	}
	/**
	 * Sets the description. 
	 * @param description The description.
	 */
	public void setDescription(StringFilterDTO description) {
		this.description = description;
	}
	/**
	 * Sets the description. 
	 * @param description The description.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putDescription(StringFilterDTO description) {
		this.description = description;
		return this;
	}
	
	/**
	 * {@code null} means all, true released and not expired, false not released or expired. 
	 * @return The status
	 */
	public Integer isActive() {
		return active;
	}
	/**
	 * Sets the status.
	 * @param active The status.
	 */
	public void setActive(Integer active) {
		this.active = active;
	}
	/**
	 * Sets the status.
	 * @param active The status.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putActive(Integer active) {
		this.active = active;
		return this;
	}
	
	/**
	 * Returns classifications inclusion or exclusion.
	 * @return The inverse value.
	 */
	public boolean isInverse() {
		return inverse;
	}
	/**
	 * Sets the inverse value.
	 * @param inverse The inverse value.
	 */
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}
	/**
	 * Sets the inverse value.
	 * @param inverse The inverse value.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	/**
	 * Returns the classifications.
	 * @return The classifications.
	 */
	public Map<String, Set<String>> getClassification() {
		return classification;
	}
	/**
	 * Sets the classifications.
	 * @param classification The classifications.
	 */
	public void setClassification(Map<String, Set<String>> classification) {
		this.classification = classification;
	}
	/**
	 * Sets the classifications.
	 * @param classification The classifications.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putClassification(Map<String, Set<String>> classification) {
		this.classification = classification;
		return this;
	}
	
	/**
	 * Returns the custom properties map.
	 * @return The custom properties map.
	 */
	public Map<String, StringFilterDTO> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the custom properties map.
	 * @param properties The custom properties map.
	 */
	public void setProperties(Map<String, StringFilterDTO> properties) {
		this.properties = properties;
	}
	
	/**
	 * Sets the custom properties map.
	 * @param properties The custom properties map.
	 * @return The fluid builder.
	 */
	public InterviewFilterDTO putProperties(Map<String, StringFilterDTO> properties) {
		this.properties = properties;
		return this;
	}
	
}
