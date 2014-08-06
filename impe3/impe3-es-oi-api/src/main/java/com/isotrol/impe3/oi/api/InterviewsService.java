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
package com.isotrol.impe3.oi.api;

import java.util.Set;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;

/**
 * Interviews Service
 * @author Emilio Escobar Reyero
 */
public interface InterviewsService {

	/**
	 * Returns an interview by id.
	 * @param serviceId External service id.
	 * @param id The interview id.
	 * @return The interview.
	 * @throws ServiceException
	 */
	InterviewDTO getById(String serviceId, String id) throws ServiceException;
	
	/**
	 * Searchs questions for a filter.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @return A page of results.
	 * @throws ServiceException
	 */
	PageDTO<QaADTO> getInterviewQuestions(String serviceId, PageFilter<QaAFilterDTO> filter) throws ServiceException;
	
	/**
	 * Searchs interviews.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @return A page of results.
	 * @throws ServiceException
	 */
	PageDTO<InterviewSelDTO> search(String serviceId, PageFilter<InterviewFilterDTO> filter) throws ServiceException;
	
	/**
	 * Creates new interview.
	 * @param serviceId External service id.
	 * @param interview The interview.
	 * @return The interview id.
	 * @throws ServiceException
	 */
	String create(String serviceId, InterviewDTO interview) throws ServiceException;

	/**
	 * Updates interview.
	 * @param serviceId External service id.
	 * @param interview The interview.
	 * @throws ServiceException
	 */
	void update(String serviceId, InterviewDTO interview) throws ServiceException;
	
	/**
	 * Deletes an interview.
	 * @param serviceId External service id.
	 * @param id The interview id.
	 * @throws ServiceException
	 */
	void delete(String serviceId, String id) throws ServiceException;
	
	/**
	 * Classify an interview.
	 * @param serviceId External service id.
	 * @param interview Resource to classify.
	 * @param set Class set name.
	 * @param classes Classes to apply.
	 * @param valid True if all the used classes must be set as valid.
	 */
	void classify(String serviceId, String interview, String set, Set<String> classes) throws ServiceException;
}
