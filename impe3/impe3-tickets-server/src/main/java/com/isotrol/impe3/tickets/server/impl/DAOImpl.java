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
package com.isotrol.impe3.tickets.server.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.stereotype.Repository;

import com.isotrol.impe3.tickets.server.DAO;
import com.isotrol.impe3.tickets.server.model.SubjectEntity;


/**
 * General DAO implementation for Web 2.0 server.
 * @author Emilio Escobar Reyero
 */
@Repository
public class DAOImpl extends com.isotrol.impe3.hib.dao.DAOImpl implements DAO {
	public DAOImpl() {
	}

	/**
	 * @see com.isotrol.impe3.tickets.server.DAO#findSubjectByName(java.lang.String)
	 */
	public SubjectEntity findSubjectByName(String name) {
		checkNotNull(name, "The subject name must be provided");
		return unique(SubjectEntity.class, getNamedQuery(SubjectEntity.BY_NAME).setString(0, name));
	}
}
