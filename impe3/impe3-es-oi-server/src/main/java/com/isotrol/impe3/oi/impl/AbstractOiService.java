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
package com.isotrol.impe3.oi.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.oi.dao.DAO;
import com.isotrol.impe3.oi.model.InterviewEntity;

/**
 * Abstract.
 * @author Emilio Escobar Reyero
 */
public abstract class AbstractOiService extends AbstractService<DAO> {

	/** Constructor. */
	public AbstractOiService() {
	}
	
	/**
	 * Loads an entity from db.
	 * @param id Interview id.
	 * @return The entity.
	 * @throws ServiceException 
	 */
	InterviewEntity loadInterviewEntity(String id) throws ServiceException {
		if (id == null) {
			throw new ServiceException();
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new ServiceException();
		}
		final InterviewEntity entity = getDao().findById(InterviewEntity.class, uuid, false);
		if (entity == null || entity.isDeleted()) {
			throw new ServiceException();
		}
		return entity;
	}

	/**
	 * Returns a new Calendar from date.
	 * @param date The date.
	 * @return The calendar representation.
	 */
	Calendar date2calendar(Date date) {
		if (date == null) {
			return null;
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
