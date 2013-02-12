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

package com.isotrol.impe3.samples.component;


import net.sf.derquinse.lucis.Item;

import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.component.html.Tag;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeQuery;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Single node component configuration.
 * @author Andres Rodriguez
 */
public class NodeComponent implements VisualComponent {
	/** Node repository. */
	private NodeRepository repository;
	/** Configuration. */
	private NodeComponentConfig config;
	/** Node. */
	private Node node = null;
	private Exception exception;

	public NodeComponent() {
	}

	/* Spring setters. */

	public void setRepository(NodeRepository repository) {
		this.repository = repository;
	}

	/* IMPE injections. */

	@Inject
	public void setConfig(NodeComponentConfig config) {
		this.config = config;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				final HTML html = HTML.create(context);
				if (exception != null) {
					return html.stackTrace(exception);
				}
				if (node == null) {
					return html.p("No node");
				}
				Tag ul = html.ul();
				final NodeKey key = node.getNodeKey();
				if (key == null) {
					ul.li("No node key");
				} else {
					ul.li(String.format("ID: %s", key.getNodeId()));
					ul.li(String.format("Content type: %s", key.getNodeType()));
					ul.li(String.format("Title: %s", node.getTitle()));
				}
				return ul;
			}
		};
	}

	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	public void edit() {
		try {
			final NodeQuery q;
			final String contentType = config.contentType();
			final NodeQuery qct = contentType == null ? null : NodeQueries.term(Schema.TYPE, contentType);
			final String id = config.contentId();
			final NodeQuery qid = id == null ? null : NodeQueries.term(Schema.ID, id);
			if (qid != null && qct != null) {
				q = NodeQueries.bool().must(qct).must(qid);
			} else if (qid != null) {
				q = qid;
			} else if (qct != null) {
				q = qct;
			} else {
				q = NodeQueries.matchAll();
			}
			final Item<Node> item = repository.getFirst(q, null, null, false, null);
			node = item != null ? item.getItem() : null;
		} catch (Exception e) {
			exception = e;
		}
	}
}
