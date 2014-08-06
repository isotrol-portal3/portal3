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
package com.isotrol.impe3.oi.dao;


import java.util.List;

import com.google.common.base.Function;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.api.InterviewFilterDTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.ClassNameEntity;
import com.isotrol.impe3.oi.model.ClassSetEntity;
import com.isotrol.impe3.oi.model.InterviewEntity;
import com.isotrol.impe3.oi.model.OIMemberEntity;
import com.isotrol.impe3.oi.model.QaAEntity;
import com.isotrol.impe3.oi.server.ClassKey;
import com.isotrol.impe3.oi.model.OILogTableEntity;
import com.isotrol.impe3.oi.server.LogTableFilterDTO;



/**
 * General DAO for OI server.
 * @author Emilio Escobar Reyero
 */
public interface DAO extends com.isotrol.impe3.hib.dao.DAO {
	/**
	 * Finds a class set.
	 * @param name Class set name.
	 * @return The requested class set or {@code null} if it is not found.
	 */
	ClassSetEntity getClassSet(String name);

	/**
	 * Finds a class name.
	 * @param name Class name.
	 * @return The requested class name or {@code null} if it is not found.
	 */
	ClassNameEntity getClassName(String name);

	/**
	 * Finds a class by key.
	 * @param key Class key.
	 * @return The requested class or {@code null} if it is not found.
	 */
	ClassEntity getClassification(ClassKey key);

	/**
	 * Load applied classes.
	 * @return Every applied class grouped by set and name.
	 */
	List<ClassEntity> loadAppliedClasses();

	/**
	 * Load all created classes for a set.
	 * @param set Set name.
	 * @return Every created class for the provided set.
	 */
	List<ClassEntity> loadClassesBySetName(String set);

	/**
	 * Searchs interviews.
	 * @param <D> The result type.
	 * @param filter The filter.
	 * @param transformer Entity to DTO transformer.
	 * @return A page of results.
	 */
	<D> PageDTO<D> findInterviews(PageFilter<InterviewFilterDTO> filter,
		Function<? super InterviewEntity, ? extends D> transformer);
	
	/**
	 * Searchs questions.
	 * @param <D> The result type.
	 * @param filter The filter.
	 * @param transformer Entity to DTO transformer.
	 * @return A page of results.
	 */
	<D> PageDTO<D> findQaAs(PageFilter<QaAFilterDTO> filter,
		Function<? super QaAEntity, ? extends D> transformer);
	
	/**
	 * Returns a member.
	 * @param key The member key.
	 * @return The member.
	 */
	OIMemberEntity getMemberById(String key);
	
	/**
	 * Returns sequence next vale.
	 * @param id Sequence id
	 * @return Next value
	 * @throws ServiceException
	 */
	long getNextValue(String id) throws ServiceException;
	
	/**
	 * 
	 * @param <D>
	 * @param filter
	 * @param transformer
	 * @return
	 */
	<D> List<D> findBatch(LogTableFilterDTO filter, Function<? super OILogTableEntity, ? extends D> transformer);
}
