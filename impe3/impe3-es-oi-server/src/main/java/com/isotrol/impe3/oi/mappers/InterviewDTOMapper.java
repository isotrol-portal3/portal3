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

import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.oi.api.InterviewDTO;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.InterviewEntity;


/**
 * InterviewDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum InterviewDTOMapper implements Function<InterviewEntity, InterviewDTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public InterviewDTO apply(InterviewEntity entity) {
		checkNotNull(entity);
		final InterviewDTO dto = new InterviewDTO();
		dto.setAuthor(entity.getAuthor());
		dto.setDate(entity.getDate() != null ? entity.getDate().getTime() : null);
		dto.setDescription(entity.getDescription());
		dto.setExpiration(entity.getExpiration() != null ? entity.getExpiration().getTime() : null);
		dto.setId(entity.getId().toString());
		dto.setInterviewee(entity.getInterviewee());
		dto.setNewQuestionsAllowed(entity.isNewQuestionsAllowed());
		dto.setProperties(Maps.newHashMap(entity.getProperties()));
		dto.setRelease(entity.getRelease() != null ? entity.getRelease().getTime() : null);
		dto.setTitle(entity.getTitle());
		dto.setVersion(entity.getVersion());

		Set<ClassEntity> classes = entity.getClasses();
		if (classes != null) {
			Map<String, Set<String>> classifications = Maps.newHashMap();
			for (ClassEntity c : classes) {
				String set = c.getSet().getName();
				String name = c.getName().getName();
				Set<String> cs = classifications.containsKey(set) ? classifications.get(set) : Sets
					.<String> newHashSet();
				
				cs.add(name);
				
				classifications.put(set, cs);
			}
			dto.setClasses(classifications);
		}

		return dto;
	}
}
