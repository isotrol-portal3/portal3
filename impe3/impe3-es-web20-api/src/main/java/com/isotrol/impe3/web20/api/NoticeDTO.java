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
 * Community notice DTO.
 * @author Emilio Escobar Reyero
 */
public class NoticeDTO extends AbstractVersionedLongId {
	/** serial uid. */
	private static final long serialVersionUID = 907715757412884821L;

	/** Community id. */
	private String community;
	
	/** Member id */
	private String member;

	/** Notice insert date. */
	private Date date;
	
	/** Notice description. */
	private String description;
	
	/** Notice title. */
	private String title;
	
	/** Notice release date. */
	private Date release;
	
	/** Notice expiration date. */
	private Date expiration;
	
	/** Constructor. */
	public NoticeDTO() {
	}
	
	/**
	 * Returns the community id.
	 * @return The community id.
	 */
	public String getCommunity() {
		return community;
	}
	
	/**
	 * Sets the community id.
	 * @param community The community id.
	 */
	public void setCommunity(String community) {
		this.community = community;
	}
	
	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMember() {
		return member;
	}
	
	/**
	 * Sets the member id.
	 * @param member The member id.
	 */
	public void setMember(String member) {
		this.member = member;
	}
	
	/**
	 * Returns the notice insert date.
	 * @return The notice insert date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the notice insert date.
	 * @param date The notice insert date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the notice text.
	 * @return The notice text.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the notice text.
	 * @param description The notice text.
	 */
	public void setDescription(String description) {
		this.description = description;
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
	
}
