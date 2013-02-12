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

package com.isotrol.impe3.test;


import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.lucene.analysis.Analyzer;

import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.IAModel;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.DocumentBuilder;
import com.isotrol.impe3.support.nr.ContentRepository;


/**
 * Builder for Test node repositories.
 * @author Andres Rodriguez
 */
public class TestRepositoryBuilder implements Builder<ContentRepository> {
	private final IAModel iaModel;
	private final TestNodeRepositoryBuilder nrb = new TestNodeRepositoryBuilder();

	/** Default Constructor. */
	TestRepositoryBuilder(final IAModel iaModel) {
		this.iaModel = checkNotNull(iaModel, "The IA model is required");
	}

	/**
	 * adds a document to repository builder
	 * @param builder document builder
	 * @param analyzer specific analyzer, could be null.
	 * @return fluid builder
	 */
	public TestRepositoryBuilder add(DocumentBuilder builder, Analyzer analyzer) {
		nrb.add(builder, analyzer);
		return this;
	}

	/**
	 * adds a document to repository builder using default analyzer
	 * @param builder document builder
	 * @return fluid builder
	 */
	public TestRepositoryBuilder add(DocumentBuilder builder) {
		nrb.add(builder);
		return this;
	}

	/**
	 * Adds some simple documents to the repository builder using default analyzer
	 * @param n Number of documents to add.
	 * @param contentType Content type.
	 * @return This builder.
	 */
	public TestRepositoryBuilder add(int n, ContentType contentType) {
		nrb.add(n, contentType);
		return this;
	}

	/**
	 * creates repository from builder.
	 */
	public NodeRepository getNodeRepository() {
		return nrb.get();
	}

	/**
	 * creates repository from builder.
	 */
	public ContentRepository get() {
		final NodeRepository nodeRepository = getNodeRepository();
		return new ContentRepository(nodeRepository, iaModel.getContentTypes(), iaModel.getCategories());
	}
}
