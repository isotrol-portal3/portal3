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
package com.isotrol.impe3.oi.mappers;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.oi.api.InterviewSelDTO;
import com.isotrol.impe3.oi.model.InterviewEntity;

/**
 * InterviewDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum InterviewSelDTOMapper implements Function<InterviewEntity, InterviewSelDTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public InterviewSelDTO apply(InterviewEntity entity) {
		checkNotNull(entity);
		final InterviewSelDTO dto = new InterviewSelDTO();
		dto.setAuthor(entity.getAuthor());
		dto.setExpiration(entity.getExpiration() != null ? entity.getExpiration().getTime() : null);
		dto.setId(entity.getId().toString());
		dto.setInterviewee(entity.getInterviewee());
		dto.setNewQuestionsAllowed(entity.isNewQuestionsAllowed());
		dto.setRelease(entity.getRelease() != null ? entity.getRelease().getTime() : null);
		dto.setTitle(entity.getTitle());
		dto.setVersion(entity.getVersion());
		
		final String description = entity.getDescription();
		if (description != null) {
			final int max = Math.min(Lengths.NAME, description.length());
			if (max > 0) {
				dto.setShortDescription(description.substring(0, max));
			}
		}
		
		return dto;
	}
}
