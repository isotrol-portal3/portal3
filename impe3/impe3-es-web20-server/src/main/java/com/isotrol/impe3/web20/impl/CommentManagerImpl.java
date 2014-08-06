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
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.web20.server.CommentManager;
import com.isotrol.impe3.web20.server.CommentMap;


/**
 * Implementation of CommentManager.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("commentManager")
public final class CommentManagerImpl extends AbstractWeb20Service implements CommentManager {
	/** Constructor. */
	public CommentManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.web20.server.WithTimeMapManager#loadTimeMap(java.lang.Long)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CommentMap loadTimeMap(Long interval) {
		final CommentMap.Builder builder = CommentMap.builder();
		List<Object[]> records = interval == null ? getDao().loadComments() : getDao().loadComments(
			Calendar.getInstance().getTimeInMillis() - interval*1000);
		for (Object[] counter : records) {
			builder.add(counter);
		}
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.web20.server.WithTimeMapManager#createEmptyTimeMap()
	 */
	public CommentMap createEmptyTimeMap() {
		return CommentMap.builder().get();
	}
}
