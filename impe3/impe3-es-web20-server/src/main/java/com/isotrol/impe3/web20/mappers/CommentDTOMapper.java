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
package com.isotrol.impe3.web20.mappers;


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.model.CommentEntity;
import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.SourceEntity;


/**
 * CommentDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum CommentDTOMapper implements Function<CommentEntity, CommentDTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public CommentDTO apply(CommentEntity entity) {
		Preconditions.checkNotNull(entity);

		final CommentDTO dto = new CommentDTO();

		dto.setId(entity.getId());
		dto.setVersion(entity.getVersion());

		dto.setEmail(entity.getEmail());
		if (entity.getLastModeration() != null) {
			dto.setLastModeration(entity.getLastModeration().getTime());
		}
		dto.setTitle(entity.getTitle());
		dto.setDescription(entity.getDescription());
		dto.setDate(entity.getDate().getTime());
		dto.setRate(entity.getRate());
		dto.setNumberOfRates(entity.getNumberOfRates());
		dto.setValid(entity.isValid());

		dto.setResourceKey(entity.getRelationship().getResource().getResourceId());
		dto.setCommunityId(entity.getRelationship().getGroup().getCommunity().getId().toString());

		SourceEntity source = entity.getSource();
		if (source != null) {
			MemberEntity member = source.getMember();
			if (member != null) {
				dto.setMemberId(member.getId().toString());
			}

			if (source.getOrigin() != null) {
				dto.setOrigin(source.getOrigin());
			}
		}

		return dto;
	}
}
