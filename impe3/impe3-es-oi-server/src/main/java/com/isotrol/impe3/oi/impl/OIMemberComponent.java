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

import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.isotrol.impe3.oi.model.OIMemberEntity;
import com.isotrol.impe3.oi.server.MemberManager;

/**
 * Members component. 
 * @author Emilio Escobar Reyero
 */
@Component
public class OIMemberComponent extends AbstractMapComponent<String> implements MemberManager {
	private final LoadingCache<Long, String> cache;
	
	/** Constructor. */
	public OIMemberComponent() {
		final CacheLoader<Long, String> computer = new CacheLoader<Long, String>() {
			public String load(Long from) {
				return findById(OIMemberEntity.class, from).getMemberId();
			}
		};
		cache = CacheBuilder.newBuilder().softValues().build(computer);
	}
	
	@Override
	Long compute(String key) {
		OIMemberEntity entity = getDao().getMemberById(key);
		if (entity == null) {
			entity = new OIMemberEntity();
			entity.setMemberId(key);
			getDao().save(entity);
			flush();
		}
		Long id = entity.getId();
		cache.put(id, key);
		return id;
	}

	/**
	 * @see com.isotrol.impe3.oi.server.MemberManager#getMember(java.lang.String)
	 */
	public long getMember(String key) {
		return get(key);
	}

	/**
	 * Returns member key.
	 * @param id Member id.
	 * @return The member key.
	 */
	public String getMemberById(long id) {
		return cache.getUnchecked(id);
	}
}
