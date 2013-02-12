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

package com.isotrol.impe3.idx;

import java.util.Set;
import java.util.UUID;

import nu.xom.Document;

/**
 * Local mappings service translate original mappings service 
 * @author Emilio Escobar Reyero
 */
public interface LocalMappingsService {

	/**
	 * Return impe3 content type uuid from a local content type. 1 uuid ... n local.
	 * @param cnt datasource content type.
	 * @return impe3 content type, null if cant find.
	 */
	UUID getContentType (String cnt);
	/**
	 * Return impe3 categories uuids from criterias.
	 * @param cnt local content type
	 * @param path local path
	 * @param cats local categories
	 * @param xml local xml
	 * @return impe3 categories
	 */
	Set<UUID> getCategories(String cnt, String path, Set<String> cats, Document xml);
	/**
	 * Return impe3 categories uuids from criterias.
	 * @param cnt local content type
	 * @param path local path
	 * @param cats local categories
	 * @return impe3 categories
	 */
	@Deprecated
	Set<UUID> getCategories(String cnt, String path, Set<String> cats);
	/**
	 * Return impe3 sets from criterias 
	 * @param cnt local content type
	 * @param path local path
	 * @param cats local categories
	 * @param xml local xml
	 * @return impe3 sets
	 */
	Set<String> getSets(String cnt, String path, Set<String> cats, Document xml);
	/**
	 * Return impe3 sets from criterias 
	 * @param cnt local content type
	 * @param path local path
	 * @param cats local categories
	 * @return impe3 sets
	 */
	@Deprecated
	Set<String> getSets(String cnt, String path, Set<String> cats);
	
}
