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

/**
 * Abstract DTO for actions.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class AbstractActionDTO extends AbstractResourceDTO {
	/** Serial uid */
	private static final long serialVersionUID = 5378761780241589412L;

	/** Event source. */
	private SourceDTO source;
	/** Community Id. */
	private String communityId;
	/** Event date. */
	private Date date;

	/**
	 * Constructor.
	 * @param resource Resource.
	 */
	public AbstractActionDTO(String resource) {
		super(resource);
	}

	/** Constructor */
	public AbstractActionDTO() {
	}
	
	/**
	 * Returns the event source.
	 * @return The event source.
	 */
	public SourceDTO getSource() {
		return source;
	}

	/**
	 * Sets the event source.
	 * @param code The event source.
	 */
	public void setSource(SourceDTO source) {
		this.source = source;
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
	 * @param name The community id.
	 */
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	/**
	 * Returns the event date.
	 * @return The event date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the event date.
	 * @param date The event date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
