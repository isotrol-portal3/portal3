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

package com.isotrol.impe3.core.content;


import java.util.List;

import com.google.common.collect.ForwardingObject;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * A forwarding content loader.
 * @author Andres Rodriguez
 */
public abstract class ForwardingContentLoader extends ForwardingObject implements ContentLoader {
	/**
	 * Constructor
	 */
	public ForwardingContentLoader() {
	}

	@Override
	protected abstract ContentLoader delegate();

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#loadNode(com.isotrol.impe3.nr.api.Node)
	 */
	public Content loadNode(Node node) {
		return delegate().loadNode(node);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#loadNodes(java.lang.Iterable)
	 */
	public List<Content> loadNodes(Iterable<? extends Node> nodes) {
		return delegate().loadNodes(nodes);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#newCriteria()
	 */
	public ContentCriteria newCriteria() {
		return delegate().newCriteria();
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#newCriteria(com.isotrol.impe3.api.ContentType)
	 */
	public ContentCriteria newCriteria(ContentType contentType) {
		return delegate().newCriteria(contentType);
	}
	
	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#forRepository(com.isotrol.impe3.nr.api.NodeRepository)
	 */
	public ContentLoader forRepository(NodeRepository repository) {
		return delegate().forRepository(repository);
	}

}
