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

package com.isotrol.impe3.web20.api;


import java.util.List;
import java.util.Set;

import com.isotrol.impe3.dto.ServiceException;


/**
 * Tags Service.
 * @author Andres Rodriguez Chamorro
 */
public interface TagsService extends Web20Service {
	/**
	 * Add a tag to a tag set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param tag Tag to add.
	 * @param valid True if the tag is valid.
	 */
	void addTag(String serviceId, String set, String tag, boolean valid) throws ServiceException;
	
	/**
	 * Add a set of tags to a tag set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param tags Tags to add.
	 * @param valid True if the tag is valid.
	 */
	void addTags(String serviceId, String set, Set<String> tags, boolean valid) throws ServiceException;
	
	/**
	 * Modify a tag in a tag set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param tag Tag to modify.
	 * @param name New tag name.
	 */
	void updateTag(String serviceId, String set, String tag, String name) throws ServiceException;

	/**
	 * Removes a tag from a tag set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param tag Tag to remove.
	 */
	void deleteTag(String serviceId, String set, String tag) throws ServiceException;
	
	/**
	 * Removes a set of tags from a tag set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param tags Tags to remove.
	 */
	void deleteTags(String serviceId, String set, Set<String> tags) throws ServiceException;

	/**
	 * Returns the tags in a set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @return The tags in the set ordered by tag name.
	 */
	List<TagDTO> getTagSet(String serviceId, String set) throws ServiceException;

	/**
	 * Returns the non-validated tags in a set.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @return The tags in the set ordered by tag name.
	 */
	List<TagDTO> getPendingTags(String serviceId, String set) throws ServiceException;
	
	/**
	 * Tags a resource.
	 * @param serviceId External service id.
	 * @param resource Resource to tag.
	 * @param set Tag set name.
	 * @param tags Tags to apply.
	 * @param valid True if all the used tags must be set as valid.
	 */
	void tag(String serviceId, String resource, String set, Set<String> tags, boolean valid) throws ServiceException;

	/**
	 * Returns the most used tags.
	 * @param serviceId External service id.
	 * @param set Tag set name.
	 * @param max Maximum number of results.
	 * @return The list of resources with most hits.
	 */
	List<UsedTagDTO> getMostUsed(String serviceId, String set, int max) throws ServiceException;

	/**
	 * Returns tag suggestions.
	 * @param serviceId External service id.
	 * @param setId Tag set id.
	 * @param prefix Tag prefix.
	 * @param max Maximum number of results.
	 * @return The requested tags suggestions ordered by tag use.
	 */
	List<UsedTagDTO> suggest(String serviceId, String set, String prefix, int max) throws ServiceException;
}
