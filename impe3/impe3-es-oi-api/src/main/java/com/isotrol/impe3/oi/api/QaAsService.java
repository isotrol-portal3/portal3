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

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.ServiceException;

/**
 * Questions and answers service.
 * @author Emilio Escobar Reyero
 */
public interface QaAsService {
	
	/**
	 * Returns a question dto.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @return The question.
	 * @throws ServiceException
	 */
	QaADTO getById(String serviceId, Long id) throws ServiceException;
	
	/**
	 * Inserts a new question.
	 * @param serviceId External service id.
	 * @param interview The interview id.
	 * @param question The question.
	 * @param member The member.
	 * @return Question id.
	 * @throws ServiceException
	 */
	Long ask(String serviceId, String interview, String question, String member) throws ServiceException;
	
	/**
	 * Answer a question.
	 * @param serviceId External service id.
	 * @param id The question id.
	 * @param answer The answer.
	 * @throws ServiceException
	 */
	void answer(String serviceId, Long id, String answer) throws ServiceException;
	
	/**
	 * Rates a question.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @param member The member.
	 * @param value Rating value.
	 * @param allowRateCreator If creators can rate itself.
	 * @throws ServiceException
	 */
	void rate(String serviceId, Long id, String member, Double value, boolean allowRateCreator) throws ServiceException;
	
	/**
	 * Insert sort order to question.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @param value Order value. 
	 * @throws ServiceException
	 */
	void order(String serviceId, Long id, Integer value) throws ServiceException;
	
	/**
	 * Validates the question.
	 * @param serviceId External service id.
	 * @param id The question id. 
	 * @param valid True if wants to accept question.
	 * @throws ServiceException
	 */
	void validate(String serviceId, Long id, boolean valid) throws ServiceException;

	/**
	 * Creates a new question.
	 * @param serviceId External service id.
	 * @param dto The question dto.
	 * @return Question id.
	 * @throws ServiceException
	 */
	Long create(String serviceId, QaADTO dto) throws ServiceException;
	
	/**
	 * Updates question.
	 * @param serviceId External service id.
	 * @param dto The question dto.
	 * @throws ServiceException
	 */
	void update(String serviceId, QaADTO dto) throws ServiceException;
	
	/**
	 * Deletes a question. 
	 * @param serviceId External service id.
	 * @param id The question id.
	 * @throws ServiceException
	 */
	void delete(String serviceId, Long id) throws ServiceException;
	
	/**
	 * Searchs questions.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @return A page of results.
	 * @throws ServiceException
	 */
	PageDTO<QaADTO> search(String serviceId, PageFilter<QaAFilterDTO> filter) throws ServiceException;
}
