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

import com.isotrol.impe3.dto.PageFilterDTO;

/**
 * DTO for recommendations filter.
 * @author Emilio Escobar Reyero
 */
public class RecommendationFilterDTO extends PageFilterDTO {

	/** Serial uid */
	private static final long serialVersionUID = 3079832558471284648L;

	/** Community id. */
	private String community;

	/** Resource recommender member uuid (origin). */
	private String recommender;
	
	/** Resource recommended member uuid. */
	private String recommended;
	
	/** Group id */
	private Long group;
	
	/** constructor. */
	public RecommendationFilterDTO() {
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
	
	/**
	 * Returns the recommender member id.
	 * @return The recommender member id.
	 */
	public String getRecommender() {
		return recommender;
	}
	
	/**
	 * Sets the recommender member id.
	 * @param recommender The recommender member id.
	 */
	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}
	
	/**
	 * Returns the group id.
	 * @return The group id.
	 */
	public Long getGroup() {
		return group;
	}
	
	/**
	 * Sets the group id.
	 * @param group The group id.
	 */
	public void setGroup(Long group) {
		this.group = group;
	}
	
}
