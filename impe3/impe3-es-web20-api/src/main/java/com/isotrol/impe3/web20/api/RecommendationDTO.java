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

import com.isotrol.impe3.dto.AbstractLongId;

/**
 * Resources recommendations DTO
 * @author Emilio Escobar Reyero
 */
public class RecommendationDTO extends AbstractLongId {

	/** serial uid. */
	private static final long serialVersionUID = 2257322241441162547L;

	/** Resource id. */
	private String resource;
	
	/** Community id. */
	private String community;
	
	/** Recommendation description. */
	private String description;
	
	/** Date. */
	private Date date;
	
	/** Resource recommender member uuid (origin). */
	private String recommender;
	
	/** Resource recommended member uuid. */
	private String recommended;
	
	/** Constructor. */
	public RecommendationDTO() {
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
	 * Returns the resource key.
	 * @return The resource key.
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * Sets the resource key.
	 * @param resource The resource key.
	 */
	public void setResource(String resource) {
		this.resource = resource;
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
	 * Returns the recommendation date.
	 * @return The recommendation date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the recommendation date.
	 * @param date The recommnedation date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the recommender (origin) member id.
	 * @return The recommender member id.
	 */
	public String getRecommender() {
		return recommender;
	}
	
	/**
	 * Sets the recommender (origin) member id.
	 * @param recommender The recommender member id.
	 */
	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}
	
	/**
	 * Returns the recommended member id.
	 * @return The recommended member id.
	 */
	public String getRecommended() {
		return recommended;
	}
	
	/**
	 * Sets the recommended member id.
	 * @param recommended The recommended member id.
	 */
	public void setRecommended(String recommended) {
		this.recommended = recommended;
	}
}
