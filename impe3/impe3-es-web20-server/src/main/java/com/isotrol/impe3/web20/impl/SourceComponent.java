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


import java.util.UUID;

import org.springframework.stereotype.Component;

import com.isotrol.impe3.web20.model.MemberEntity;
import com.isotrol.impe3.web20.model.SourceEntity;
import com.isotrol.impe3.web20.server.SourceKey;


/**
 * Source component.
 * @author Andres Rodriguez.
 */
@Component
public final class SourceComponent extends AbstractMapComponent<SourceKey> {
	/** Default constructor. */
	public SourceComponent() {
	}

	/**
	 * @see com.isotrol.impe3.web20.impl.AbstractMapComponent#compute(java.lang.Object)
	 */
	@Override
	Long compute(SourceKey key) {
		final UUID memberId = key.getMember();
		final String origin = key.getOrigin();
		SourceEntity entity;
		if (memberId != null) {
			entity = getDao().getSourceByMember(memberId);
		} else {
			entity = getDao().getSourceByOrigin(origin);
		}
		if (entity == null) {
			entity = new SourceEntity();
			if (memberId != null) {
				entity.setMember(findById(MemberEntity.class, memberId));
			} else {
				entity.setOrigin(origin);
			}
			getDao().save(entity);
			flush();
		}
		return entity.getId();
	}
}
