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


import java.util.List;

import com.isotrol.impe3.dto.ServiceException;


/**
 * Classes Service.
 * @author Andres Rodriguez Chamorro
 * @author Emilio Escobar Reyero
 */
public interface ClassesService {
	/**
	 * Add a class to a class set.
	 * @param serviceId External service id.
	 * @param set Class set name.
	 * @param classification Class to add.
	 * @param valid True if the class is valid.
	 */
	void addClassification(String serviceId, String set, String classification) throws ServiceException;
	
	/**
	 * Modify a class in a class set.
	 * @param serviceId External service id.
	 * @param set Class set name.
	 * @param classification Class to modify.
	 * @param name New tag name.
	 */
	void updateClass(String serviceId, String set, String classification, String name) throws ServiceException;

	/**
	 * Removes a class from a class set.
	 * @param serviceId External service id.
	 * @param set Class set name.
	 * @param classification Class to remove.
	 */
	void deleteClass(String serviceId, String set, String classification) throws ServiceException;
	
	/**
	 * Returns the classes in a set.
	 * @param serviceId External service id.
	 * @param set Class set name.
	 * @return The classes in the set ordered by class name.
	 */
	List<ClassDTO> getClassSet(String serviceId, String set) throws ServiceException;

}
