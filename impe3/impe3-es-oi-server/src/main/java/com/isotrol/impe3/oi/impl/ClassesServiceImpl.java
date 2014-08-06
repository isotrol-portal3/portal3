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


import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.oi.api.ClassDTO;
import com.isotrol.impe3.oi.api.ClassesService;
import com.isotrol.impe3.oi.dao.DAO;
import com.isotrol.impe3.oi.mappers.ClassDTOMapper;
import com.isotrol.impe3.oi.server.ClassManager;



/**
 * Implementation of TagsService.
 * @author Andres Rodriguez.
 */
@Service("oiClassesService")
public final class ClassesServiceImpl extends AbstractService<DAO> implements ClassesService {
	@Autowired
	private ClassManager classManager;


	/** Constructor. */
	public ClassesServiceImpl() {
	}



	@Transactional(rollbackFor = Throwable.class)
	public void addClassification(String serviceId, String set, String classification) throws ServiceException {
		classManager.addClass(set, classification);
	}
	

	@Transactional(rollbackFor = Throwable.class)
	public void updateClass(String serviceId, String set, String classification, String name) throws ServiceException {
		classManager.updateClass(set, classification, name);
	}
	

	@Transactional(rollbackFor = Throwable.class)
	public void deleteClass(String serviceId, String set, String classification) throws ServiceException {
		classManager.deleteClass(set, classification);
	}


	@Transactional(rollbackFor = Throwable.class)
	public List<ClassDTO> getClassSet(String serviceId, String set) throws ServiceException {
		Preconditions.checkNotNull(set);
		return newArrayList(transform(getDao().loadClassesBySetName(set), ClassDTOMapper.INSTANCE));
	}

	public void setClassManager(ClassManager classManager) {
		this.classManager = classManager;
	}

}
