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

import org.apache.lucene.document.Document;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.oi.api.InterviewSelDTO;
import com.isotrol.impe3.oi.server.InterviewSchema;


import net.sf.lucis.core.DocMapper;

/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class InterviewSelDTODocMapper implements DocMapper<InterviewSelDTO> {

	public InterviewSelDTO map(int id, float score, Document doc, Multimap<String, String> fragments) {
		final InterviewSelDTO dto = new InterviewSelDTO();

		dto.setId(doc.get(InterviewSchema.ID));
		
		dto.setAuthor(doc.get(InterviewSchema.AUTHOR));
		dto.setInterviewee(doc.get(InterviewSchema.INTERVIEWEE));
		dto.setTitle(doc.get(InterviewSchema.TITLE));
		dto.setNewQuestionsAllowed(doc.get(InterviewSchema.ALLOWED).equalsIgnoreCase("TRUE"));

		
		final String description = doc.get(InterviewSchema.DESCRIPTION);
		if (description != null) {
			final int max = Math.min(Lengths.NAME, description.length());
			if (max > 0) {
				dto.setShortDescription(description.substring(0, max));
			}
		}
		
		final String expiration = doc.get(InterviewSchema.EXPIRATION);
		final String release = doc.get(InterviewSchema.RELEASE);
		
		if (!InterviewSchema.getMinDateString().equals(release)) {
			dto.setRelease(InterviewSchema.safeToCalendar(release).getTime());
		}
		if (!InterviewSchema.getMaxDateString().equals(expiration)) {
			dto.setExpiration(InterviewSchema.safeToCalendar(expiration).getTime());
		}

		
		return dto;
	}
	
	
}
