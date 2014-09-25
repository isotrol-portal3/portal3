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

package com.isotrol.impe3.web20.api;

import java.util.Date;

import com.isotrol.impe3.dto.AbstractVersionedLongId;

/**
 * DTO for resource comments.
 * @author Emilio Escobar Reyero
 */
public class CommentDTO extends AbstractVersionedLongId {
	/** serial uid. */
	private static final long serialVersionUID = 6669555620445472715L;

	/** Title. */
	private String title;
	/** Description. */
	private String description;
	/** Email. */
	private String email;
	/** True means valid comment. */
	private boolean valid = false;
	/** Comment rate value. */
	private double rate = 0;
	/** Number of rates */
	private int numberOfRates = 0;
	/** Insert comment date. */
	private Date date;
	/** Last moderation date. */
	private Date lastModeration;
	/** Resource id commented. */
	private String resourceKey;
	/** Community where resource is commented. */
	private String communityId;
	/** Member id. optional*/
	private String memberId;
	/** Origin mark*/
	private String origin;
	
	/** Default constructor. */
	public CommentDTO() {
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
	 * Returns the email.
	 * @return The email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 * @param email The email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns whether the comment is valid.
	 * @return True if the comment is valid.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Sets whether the comment is valid.
	 * @param deleted True if the comment is valid.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * Returns the comment rate.
	 * @return The comment rate.
	 */
	public double getRate() {
		return rate;
	}
	
	/**
	 * Sets the comment rate.
	 * @param rate The comment rate.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	/**
	 * Returns the number of rates.
	 * @return The number of rates.
	 */
	public int getNumberOfRates() {
		return numberOfRates;
	}
	
	/**
	 * Sets the number of rates.
	 * @param numberOfRates The number of rates.
	 */
	public void setNumberOfRates(int numberOfRates) {
		this.numberOfRates = numberOfRates;
	}
	
	/**
	 * Returns the comment commit date.
	 * @return The comment commit date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the comment commit date.
	 * @param date The comment commit date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the last mederation date.
	 * @return The last moderation date.
	 */
	public Date getLastModeration() {
		return lastModeration;
	}
	
	/**
	 * Sets the last moderation date.
	 * @param lastModeration The last moderation date.
	 */
	public void setLastModeration(Date lastModeration) {
		this.lastModeration = lastModeration;
	}
	
	/**
	 * Returns the resource key.
	 * @return The resource key.
	 */
	public String getResourceKey() {
		return resourceKey;
	}
	
	/**
	 * Sets the resource key. 
	 * @param resourceId The resource key.
	 */
	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}
	
	/**
	 * Returns the community id.
	 * @return The community id.
	 */
	public String getCommunityId() {
		return communityId;
	}
	
	/**
	 * Sets the community id.
	 * @param communityId The community id.
	 */
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	/**
	 * Returns the member id;
	 * @return The member id.
	 */
	public String getMemberId() {
		return memberId;
	}
	
	/**
	 * Sets the member id.
	 * @param memberId The member id.
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	/**
	 * Returns the origin.
	 * @return the origin.
	 */
	public String getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin.
	 * @param origin The origin.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
