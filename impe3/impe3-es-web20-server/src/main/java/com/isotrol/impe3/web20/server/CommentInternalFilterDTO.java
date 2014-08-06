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
package com.isotrol.impe3.web20.server;

import com.isotrol.impe3.web20.api.CommentFilterDTO;

public class CommentInternalFilterDTO extends CommentFilterDTO {

	private static final long serialVersionUID = -9081049441385861724L;

	private Long relationship;

	public void setRelationship(Long relationship) {
		this.relationship = relationship;
	}

	public Long getRelationship() {
		return relationship;
	}
	
	public static CommentInternalFilterDTO empty() {
		final CommentInternalFilterDTO filter = new CommentInternalFilterDTO();
		return filter;
	}

	public static CommentInternalFilterDTO of(CommentFilterDTO original) {
		return of(original, null);
	}

	public static CommentInternalFilterDTO of(CommentFilterDTO original, Long relationship) {
		final CommentInternalFilterDTO filter = new CommentInternalFilterDTO();
		filter.setCommunityId(original.getCommunityId());
		filter.setHighDate(original.getHighDate());
		filter.setLowDate(original.getLowDate());
		filter.setModerated(original.isModerated());
		filter.setOrderings(original.getOrderings());
		filter.setPagination(original.getPagination());
		filter.setRelationship(relationship);
		filter.setResourceKey(original.getResourceKey());
		filter.setResourcePrefix(original.getResourcePrefix());
		filter.setValid(original.isValid());
		
		return filter;
	}
}
