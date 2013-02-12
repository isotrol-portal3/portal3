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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.google.common.base.Function;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.support.nr.ContentNode;


/**
 * Default content loader based on a node repository.
 * @author Andres Rodriguez
 */
public final class NRContentLoader implements ContentLoader {
	/** Portal. */
	private final Portal portal;
	/** Node repositories. */
	private final Function<UUID, NodeRepository> nodeRepositories;
	/** Request locale. */
	private final Locale locale;

	/** Node to content function. */
	private final Function<Node, Content> node2content = new Function<Node, Content>() {
		public Content apply(Node from) {
			return new ContentNode(portal.getContentTypes(), portal.getCategories(), from);
		}
	};

	/**
	 * Constructor.
	 * @param portal Portal.
	 * @param repository Node repository to use.
	 * @param locale Request locale.
	 * @throws IllegalArgumentException if the portal does not contain the content type.
	 */
	public NRContentLoader(Portal portal, Function<UUID, NodeRepository> nodeRepositories, Locale locale) {
		this.portal = checkNotNull(portal, "The portal for the content loader MUST be provided");
		this.nodeRepositories = checkNotNull(nodeRepositories,
			"The repositories for the content loader MUST be provided");
		this.locale = locale;
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#loadNode(com.isotrol.impe3.nr.api.Node)
	 */
	public Content loadNode(Node node) {
		checkNotNull(node, "A node must be provided");
		return node2content.apply(node);
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#loadNodes(java.lang.Iterable)
	 */
	public List<Content> loadNodes(Iterable<? extends Node> nodes) {
		checkNotNull(nodes, "A iterable of nodes must be provided");
		return newArrayList(transform(filter(nodes, notNull()), node2content));
	}

	private ContentCriteria createQuery(ContentType contentType) {
		checkArgument(contentType == null || portal.getContentTypes().containsValue(contentType),
			"The portal must contain the specific content type");
		return new ContentCriteriaImpl(portal, contentType, nodeRepositories, node2content, locale);
	}

	public ContentCriteria newCriteria() {
		return createQuery(null);
	}

	public ContentCriteria newCriteria(ContentType contentType) {
		return createQuery(checkNotNull(contentType));
	}

	/**
	 * @see com.isotrol.impe3.api.content.ContentLoader#forRepository(com.isotrol.impe3.nr.api.NodeRepository)
	 */
	public ContentLoader forRepository(final NodeRepository repository) {
		checkNotNull(repository, "The node repository must be provided");
		Function<UUID, NodeRepository> f = new Function<UUID, NodeRepository>() {
			public NodeRepository apply(UUID from) {
				return repository;
			}
		};
		return new NRContentLoader(portal, f, locale);
	}

}
