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
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.model.QaAEntity;

/**
 * InterviewDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum QaADTOMapper implements Function<QaAEntity, QaADTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public QaADTO apply(QaAEntity entity) {
		checkNotNull(entity);
		final QaADTO dto = new QaADTO();
		dto.setAnswer(entity.getAnswer());
		dto.setDate(entity.getDate() != null ? entity.getDate().getTime() : null);
		dto.setId(entity.getId());
		dto.setMemberId(entity.getMember() != null ? entity.getMember().getMemberId() : null);
		dto.setQuestion(entity.getQuestion());
		dto.setVersion(entity.getVersion());
		dto.setOrder(entity.getOrder());
		dto.setRate(entity.getRate());
		dto.setValid(entity.isValid());
		
		return dto;
	}
}
