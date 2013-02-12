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


import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.user.DoneDTO;


/**
 * Edition DTO.
 * @author Andres Rodriguez
 */
public class EditionDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = -7631510608214952150L;
	/** Whether the edition is the current one. */
	private boolean current;
	/** Creation information. */
	private DoneDTO created;
	/** Last publishing information. */
	private DoneDTO lastPublished;

	/** Default constructor. */
	public EditionDTO() {
	}

	/**
	 * Returns whether the edition is the current one.
	 * @return True if the edition is the current one.
	 */
	public boolean isCurrent() {
		return current;
	}

	/**
	 * Sets whether the edition is the current one.
	 * @param current True if the edition is the current one.
	 */
	public void setCurrent(boolean current) {
		this.current = current;
	}

	/**
	 * Returns the creation information.
	 * @return The creation information.
	 */
	public DoneDTO getCreated() {
		return created;
	}

	/**
	 * Sets the creation information.
	 * @param created The creation information.
	 */
	public void setCreated(DoneDTO created) {
		this.created = created;
	}

	/**
	 * Returns the last publishing information.
	 * @return The last publishing information.
	 */
	public DoneDTO getLastPublished() {
		return lastPublished;
	}

	/**
	 * Sets the last publishing information.
	 * @param lastPublished The last publishing information.
	 */
	public void setLastPublished(DoneDTO lastPublished) {
		this.lastPublished = lastPublished;
	}
}
