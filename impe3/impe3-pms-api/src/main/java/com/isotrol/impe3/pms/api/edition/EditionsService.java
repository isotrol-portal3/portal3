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

package com.isotrol.impe3.pms.api.edition;


import java.util.List;

import com.isotrol.impe3.pms.api.PMSException;


/**
 * Editions service.
 * @author Andres Rodriguez.
 */
public interface EditionsService {
	/**
	 * Returns the last published editions.
	 * @return The last published editions
	 */
	List<EditionDTO> getLastEditions() throws PMSException;
	
	/**
	 * Create a new edition with the current offline environment.
	 */
	void publish() throws PMSException;

	/**
	 * Sets the edition to be used for the online environment.
	 * @param editionId Edition Id.
	 */
	void setOnlineEdition(String editionId) throws PMSException;
	
	/**
	 * Create a new edition with the current online environment and the offline of the provided portal.
	 * @param portalId Portal to publish.
	 * @param tryParents Whether to publish the parents if needed.
	 * @throws ModifiedGlobalsException If there are modified global elements.
	 * @throws ModifiedParentPortalsException If there are modified parent portals.
	 */
	boolean publishPortal(String portalId, boolean tryParents) throws PMSException;

}
