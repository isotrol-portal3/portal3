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
package com.isotrol.impe3.web20.server;


import java.util.Set;


/**
 * Internal counter manager.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface TagManager extends ResourceManager {
	/**
	 * Add a tag to a tag set.
	 * @param set Tag set name.
	 * @param tag Tag to add.
	 * @param valid True if the tag is valid.
	 */
	void addTag(String set, String tag, boolean valid);

	/**
	 * Modify a tag in a tag set.
	 * @param set Tag set name.
	 * @param tag Tag to modify.
	 * @param name New tag name.
	 */
	void updateTag(String set, String tag, String name);

	/**
	 * Removes a tag from a tag set.
	 * @param set Tag set name.
	 * @param tag Tag to remove.
	 */
	void deleteTag(String set, String tag);
	
	/**
	 * Tags a resource.
	 * @param resource Resource to tag.
	 * @param set Tag set name.
	 * @param tags Tags to apply.
	 * @param valid True if all the used tags must be set as valid.
	 */
	void tag(String resource, String set, Set<String> tags, boolean valid);

	/**
	 * Loads all tags.
	 * @return A complete tag map.
	 */
	TagMap loadAll();

	/**
	 * Returns a tag set id.
	 * @param set Tag set name.
	 * @return Tag set id.
	 */
	long getSet(String set);

	/**
	 * Returns a tag name id.
	 * @param name Tag name.
	 * @return Tag name id.
	 */
	long getName(String name);

	/**
	 * Returns a tag key.
	 * @param set Tag set.
	 * @param name Tag name.
	 * @return The requested key.
	 */
	TagKey getKey(String set, String name);

}
