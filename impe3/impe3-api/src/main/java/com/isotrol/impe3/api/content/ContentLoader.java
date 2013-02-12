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

package com.isotrol.impe3.api.content;


import java.util.List;

import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * Content loading interface.
 * @author Andres Rodriguez
 */
public interface ContentLoader {
	/**
	 * Loads a node as a content.
	 * @param node Node to load.
	 * @return The loaded content.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	Content loadNode(Node node);

	/**
	 * Loads a collection of nodes as a list of contents.
	 * @param nodes Nodes to load.
	 * @return A list containing the loaded contents.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	List<Content> loadNodes(Iterable<? extends Node> nodes);

	/**
	 * Creates a new content criteria. The criteria is created with the following flags: <ul> <li> Uncategorized
	 * contents: as specified by the portal. <li> Due contents: as specified by the portal. <li> Fetch bytes: no. <li>
	 * Use request locale: yes. </ul>
	 * @return A newly created content criteria.
	 */
	ContentCriteria newCriteria();

	/**
	 * Creates a new content type-specific criteria. The criteria is created with the following flags: <ul> <li>
	 * Uncategorized contents: as specified by the portal. <li> Due contents: as specified by the portal. <li> Fetch
	 * bytes: no. <li> Use request locale: yes. </ul>
	 * @param contentType Content type to filter.
	 * @return A newly created content query.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the content type is not part of the portal.
	 */
	ContentCriteria newCriteria(ContentType contentType);

	/**
	 * Creates a content loader that uses an external node repository.
	 * @param repository Node repository.
	 * @return A specific content loader.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	ContentLoader forRepository(NodeRepository repository);

}
