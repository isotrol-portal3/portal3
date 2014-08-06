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
package com.isotrol.impe3.oi.server;

/**
 * Internal counter manager.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface ClassManager {
	/**
	 * Add a class to a class set.
	 * @param set Class set name.
	 * @param classification Class to add.
	 */
	void addClass(String set, String classification);

	/**
	 * Modify a class in a class set.
	 * @param set Class set name.
	 * @param classification Class to modify.
	 * @param name New class name.
	 */
	void updateClass(String set, String classification, String name);

	/**
	 * Removes a class from a class set.
	 * @param set Class set name.
	 * @param classification Class to remove.
	 */
	void deleteClass(String set, String classification);

	/**
	 * Returns a class set id.
	 * @param set Class set name.
	 * @return Clas set id.
	 */
	long getSet(String set);

	/**
	 * Returns a class name id.
	 * @param name Class name.
	 * @return Class name id.
	 */
	long getName(String name);

	/**
	 * Returns a class key.
	 * @param set Class set.
	 * @param name Class name.
	 * @return The requested key.
	 */
	ClassKey getKey(String set, String name);

}
