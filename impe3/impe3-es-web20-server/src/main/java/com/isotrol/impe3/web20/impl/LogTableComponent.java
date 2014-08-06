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
package com.isotrol.impe3.web20.impl;


import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.MemberNotFoundException;
import com.isotrol.impe3.web20.mappers.LogTableDTOMapper;
import com.isotrol.impe3.web20.model.LogTableEntity;
import com.isotrol.impe3.web20.server.LogTableDTO;
import com.isotrol.impe3.web20.server.LogTableFilterDTO;


/**
 * 
 * @author Emilio Escobar Reyero
 */
@Component
public class LogTableComponent extends AbstractWeb20Service {

	void insert(String item, String name, int task) throws ServiceException {
		Preconditions.checkNotNull(item);

		UUID uuid = null;
		try {
			uuid = UUID.fromString(item);
		} catch (IllegalArgumentException e) {
			throw new MemberNotFoundException(item);
		}

		final LogTableEntity entity = new LogTableEntity();
		final long next = getDao().getNextValue(name);

		entity.setId(next);
		entity.setItem(uuid);
		entity.setName(name);
		entity.setTask(task);
		entity.setDate(Calendar.getInstance());

		getDao().save(entity);
	}

	List<LogTableDTO> readBatch(long checkpoint, String name) {
		final LogTableFilterDTO filter = new LogTableFilterDTO();
		filter.setName(name);
		filter.setFirst(checkpoint);

		final List<LogTableDTO> page = getDao().findBatch(filter, LogTableDTOMapper.INSTANCE);

		final List<LogTableDTO> batch = clean(page);

		return batch;
	}

	private List<LogTableDTO> clean(List<LogTableDTO> elements) {
		final List<LogTableDTO> tasks = Lists.newArrayList();
		final Set<String> ids = new HashSet<String>();

		for (int i = elements.size() - 1; i >= 0; i--) {
			LogTableDTO t = elements.get(i);
			if (!ids.contains(t.getItem())) {
				tasks.add(0, t);
				ids.add(t.getItem());
			}
		}

		return tasks;
	}

}
