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


import java.util.ArrayList;
import java.util.List;


/**
 * DTO for a resource counter by community.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class ResourceByCommunityCounterDTO extends AbstractResourceDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 1448338221523697723L;

	/** Communities. */
	private List<CommunityCounterDTO> communities;

	/**
	 * Constructor.
	 * @param resource Resource.
	 */
	public ResourceByCommunityCounterDTO(String resource) {
		super(resource);
	}

	/** Constructor. */
	public ResourceByCommunityCounterDTO() {
	}

	private List<CommunityCounterDTO> communities() {
		if (communities == null) {
			communities = new ArrayList<CommunityCounterDTO>();
		}
		return communities;
	}

	/**
	 * Returns the community counters.
	 * @return The community counters.
	 */
	public List<CommunityCounterDTO> getCommunities() {
		return communities();
	}

	/**
	 * Sets the community counters.
	 * @param communities The community counters.
	 */
	public void setCommunities(List<CommunityCounterDTO> communities) {
		this.communities = communities;
	}

	/**
	 * Add a community counter.
	 * @param counter The community counter. Only added if it is non-null.
	 * @return This DTO.
	 */
	public ResourceByCommunityCounterDTO addCommunity(CommunityCounterDTO counter) {
		if (counter != null) {
			communities().add(counter);
		}
		return this;
	}
}
