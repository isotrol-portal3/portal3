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
import com.isotrol.impe3.web20.api.FavoriteDTO;
import com.isotrol.impe3.web20.model.FavoriteEntity;


/**
 * FavoriteDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum FavoriteDTOMapper implements Function<FavoriteEntity, FavoriteDTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public FavoriteDTO apply(FavoriteEntity entity) {
		Preconditions.checkNotNull(entity);

		final FavoriteDTO dto = new FavoriteDTO();

		dto.setId(entity.getId());

		dto.setCommunity(entity.getRelationship().getGroup().getCommunity().getId().toString());
		dto.setMember(entity.getMember().getId().toString());
		dto.setDate(entity.getDate().getTime());
		dto.setResource(entity.getRelationship().getResource().getResourceId());
		dto.setDescription(entity.getDescription());

		return dto;
	}
}
