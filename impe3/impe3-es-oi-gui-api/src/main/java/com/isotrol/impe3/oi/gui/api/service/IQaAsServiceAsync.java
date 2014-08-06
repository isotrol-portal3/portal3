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
package com.isotrol.impe3.oi.gui.api.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;

/**
 * Questions and answers async service.
 * @author Emilio Escobar Reyero
 */
public interface IQaAsServiceAsync {
	
	/**
	 * Returns a question dto.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @param callback
	 */
	void getById(String serviceId, Long id, AsyncCallback<QaADTO> callback);
	
	/**
	 * Inserts a new question.
	 * @param serviceId External service id.
	 * @param interview The interview id.
	 * @param question The question.
	 * @param member The member.
	 * @param callback
	 */
	void ask(String serviceId, String interview, String question, String member, AsyncCallback<Long> callback);
	
	/**
	 * Answer a question.
	 * @param serviceId External service id.
	 * @param id The question id.
	 * @param answer The answer.
	 * @param callback
	 */
	void answer(String serviceId, Long id, String answer, AsyncCallback<Void> callback);
	
	/**
	 * Rates a question.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @param member The member.
	 * @param value Rating value.
	 * @param allowRateCreator If creators can rate itself.
	 * @param callback
	 */
	void rate(String serviceId, Long id, String member, Double value, boolean allowRateCreator, AsyncCallback<Void> callback);
	
	/**
	 * Insert sort order to question.
	 * @param serviceId External service id.
	 * @param id Question id.
	 * @param value Order value. 
	 * @param callback
	 */
	void order(String serviceId, Long id, Integer value, AsyncCallback<Void> callback);
	
	/**
	 * Validates the question.
	 * @param serviceId External service id.
	 * @param id The question id. 
	 * @param valid True if wants to accept question.
	 * @param callback
	 */
	void validate(String serviceId, Long id, boolean valid, AsyncCallback<Void> callback);

	/**
	 * Creates a new question.
	 * @param serviceId External service id.
	 * @param dto The question dto.
	 * @param callback
	 */
	void create(String serviceId, QaADTO dto, AsyncCallback<Long> callback);
	
	/**
	 * Updates question.
	 * @param serviceId External service id.
	 * @param dto The question dto.
	 * @param callback
	 */
	void update(String serviceId, QaADTO dto, AsyncCallback<Void> callback);
	
	/**
	 * Deletes a question. 
	 * @param serviceId External service id.
	 * @param id The question id.
	 * @param callback
	 */
	void delete(String serviceId, Long id, AsyncCallback<Void> callback);
	
	/**
	 * Searchs questions.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @param callback
	 */
	void search(String serviceId, PageFilter<QaAFilterDTO> filter, AsyncCallback<PageDTO<QaADTO>> callback);
}
