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


import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.isotrol.impe3.oi.model.OILogTableEntity;
import com.isotrol.impe3.oi.server.LogTableDTO;


/**
 * LogTableDTO mapper.
 * @author Emilio Escobar Reyero.
 */
public enum LogTableDTOMapper implements Function<OILogTableEntity, LogTableDTO> {
	/** Singleton instance. */
	INSTANCE;

	/**
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	public LogTableDTO apply(OILogTableEntity entity) {
		Preconditions.checkNotNull(entity);

		final LogTableDTO dto = new LogTableDTO();
		dto.setId(entity.getId());
		dto.setItem(entity.getItem().toString());
		dto.setName(entity.getName());
		dto.setTask(entity.getTask());
		if (entity.getReplacedBy() != null) {
			dto.setReplacedBy(entity.getReplacedBy().getId());
		}


		return dto;
	}
}
