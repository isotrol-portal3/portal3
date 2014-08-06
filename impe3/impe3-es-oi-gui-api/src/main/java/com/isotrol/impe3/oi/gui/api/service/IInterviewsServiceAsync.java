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

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.oi.api.InterviewDTO;
import com.isotrol.impe3.oi.api.InterviewFilterDTO;
import com.isotrol.impe3.oi.api.InterviewSelDTO;
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;

/**
 * Interviews async Service
 * @author Manuel Ruiz
 */
public interface IInterviewsServiceAsync {

	/**
	 * Returns an interview by id.
	 * @param serviceId External service id.
	 * @param id The interview id.
	 * @param callback
	 */
	void getById(String serviceId, String id, AsyncCallback<InterviewDTO> callback);
	
	/**
	 * Searchs questions for a filter.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @param callback
	 */
	void getInterviewQuestions(String serviceId, PageFilter<QaAFilterDTO> filter, AsyncCallback<PageDTO<QaADTO>> callback);
	
	/**
	 * Searchs interviews.
	 * @param serviceId External service id.
	 * @param filter The filter.
	 * @param callback
	 */
	void search(String serviceId, PageFilter<InterviewFilterDTO> filter, AsyncCallback<PageDTO<InterviewSelDTO>> callback);
	
	/**
	 * Creates new interview.
	 * @param serviceId External service id.
	 * @param interview The interview.
	 * @param callback
	 */
	void create(String serviceId, InterviewDTO interview, AsyncCallback<String> callback);

	/**
	 * Updates interview.
	 * @param serviceId External service id.
	 * @param interview The interview.
	 * @param callback
	 */
	void update(String serviceId, InterviewDTO interview, AsyncCallback<Void> callback);
	
	/**
	 * Deletes an interview.
	 * @param serviceId External service id.
	 * @param id The interview id.
	 * @param callback
	 */
	void delete(String serviceId, String id, AsyncCallback<Void> callback);
	
	/**
	 * Classify an interview.
	 * @param serviceId External service id.
	 * @param interview Resource to classify.
	 * @param set Class set name.
	 * @param classes Classes to apply.
	 * @param valid True if all the used classes must be set as valid.
	 * @param callback
	 */
	void classify(String serviceId, String interview, String set, Set<String> classes, AsyncCallback<Void> callback);
}
