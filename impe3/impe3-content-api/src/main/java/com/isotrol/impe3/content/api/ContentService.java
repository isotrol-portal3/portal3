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

package com.isotrol.impe3.content.api;


import java.util.List;
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.dto.Counted;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Basic service for GUI content access. The provider id meaning depends on the context. It may be an external service
 * id or a portal id.
 * @author Emilio Escobar.
 * @author Andres Rodriguez
 */
public interface ContentService {
	/**
	 * Returns the service summary.
	 * @param locale Locale to use.
	 * @return The service summary.
	 */
	ContentSummaryDTO getSummary(String locale) throws ServiceException;

	/**
	 * Performs a paged query.
	 * @param providerId Provider Id.
	 * @param type Query type.
	 * @param query Query to perform.
	 * @param filter Filter to apply.
	 * @return A page of results.
	 */
	PageDTO<ContentDTO> getPage(String providerId, ContentQueryType type, String query, ContentFilterDTO filter)
		throws ServiceException;

	/**
	 * Get the content types available for filters.
	 * @param providerId Provider Id.
	 * @param locale Locale to use.
	 * @return The list of available content types.
	 */
	List<ContentTypeRefDTO> getContentTypes(String providerId, String locale) throws ServiceException;

	/**
	 * Get the number of contents available per content type.
	 * @param providerId Provider Id.
	 * @param locale Locale to use.
	 * @return The number of contents by content types.
	 */
	List<Counted<ContentTypeRefDTO>> getPerContentType(String providerId, String locale) throws ServiceException;

	/**
	 * Get the categories available for filters.
	 * @param providerId Provider Id.
	 * @param locale Locale to use.
	 * @return The category tree.
	 */
	CategoryRefTreeDTO getCategories(String providerId, String locale) throws ServiceException;

	/**
	 * Get the number of contents available per content type.
	 * @param providerId Provider Id.
	 * @param locale Locale to use.
	 * @return The number of contents by content types.
	 */
	CountedCategoryRefTreeDTO getPerCategory(String providerId, String locale) throws ServiceException;

	/**
	 * Gets a content by id.
	 * @param providerId Provider Id.
	 * @param key Content key.
	 * @param locale Locale to use.
	 * @return The content or {@code null} if not found.
	 */
	ContentDTO getContent(String providerId, String key, String locale) throws ServiceException;

	/**
	 * Recover a set of contents by key.
	 * @param providerId Provider Id.
	 * @param keys Content keys.
	 * @param locale Locale to use.
	 * @param detail Whether to use detail mode.
	 * @return A map with the contents found.
	 */
	Map<String, ContentDTO> getContents(String providerId, Set<String> keys, String locale) throws ServiceException;

	/**
	 * Gets a content detail by id.
	 * @param providerId Provider Id.
	 * @param key Content key.
	 * @param locale Locale to use.
	 * @return The content or {@code null} if not found.
	 */
	ContentDetailDTO getContentDetail(String providerId, String key, String locale) throws ServiceException;

	/**
	 * Recover a set of contents by key.
	 * @param providerId Provider Id.
	 * @param keys Content keys.
	 * @param locale Locale to use.
	 * @param detail Whether to use detail mode.
	 * @return A map with the contents found.
	 */
	Map<String, ContentDetailDTO> getContentsDetail(String providerId, Set<String> keys, String locale)
		throws ServiceException;

	/**
	 * Gets a complete (with blobs) content detail by id.
	 * @param providerId Provider Id.
	 * @param key Content key.
	 * @param locale Locale to use.
	 * @return The content or {@code null} if not found.
	 */
	CompleteContentDTO getCompleteContent(String providerId, String key, String locale) throws ServiceException;

	/**
	 * Recover a set of complete (with blobs) contents by key.
	 * @param providerId Provider Id.
	 * @param keys Content keys.
	 * @param locale Locale to use.
	 * @param detail Whether to use detail mode.
	 * @return A map with the contents found.
	 */
	Map<String, CompleteContentDTO> getCompleteContents(String providerId, Set<String> keys, String locale)
		throws ServiceException;
}
