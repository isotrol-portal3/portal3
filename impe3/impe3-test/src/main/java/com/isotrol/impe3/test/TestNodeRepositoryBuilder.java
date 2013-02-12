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


import java.util.UUID;

import org.apache.lucene.analysis.Analyzer;

import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.nr.core.ConstantRAMRepository;
import com.isotrol.impe3.nr.core.DocumentBuilder;


/**
 * Builder for Test node repositories.
 * @author Andres Rodriguez
 */
final class TestNodeRepositoryBuilder implements Builder<NodeRepository> {
	private final ConstantRAMRepository.Builder builder = ConstantRAMRepository.builder();

	/** Default Constructor. */
	TestNodeRepositoryBuilder() {
	}

	/**
	 * Adds a document to repository builder
	 * @param builder document builder
	 * @param analyzer specific analyzer, could be null.
	 */
	void add(DocumentBuilder builder, Analyzer analyzer) {
		try {
			this.builder.add(builder, analyzer);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a document to repository builder using default analyzer
	 * @param builder document builder
	 * @return fluid builder
	 */
	void add(DocumentBuilder builder) {
		try {
			this.builder.add(builder);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds some simple documents to the repository builder using default analyzer
	 * @param n Number of documents to add.
	 * @param contentType Content type.
	 * @param categories Categories.
	 * @return This builder.
	 */
	void add(int n, ContentType contentType, Category... categories) {
		final UUID typeId = contentType.getId();
		for (int i = 0; i < n; i++) {
			DocumentBuilder b = this.builder.newTestBuilder(typeId);
			if (categories != null) {
				for (Category c : categories) {
					if (c != null) {
						b.addCategory(c.getId());
					}
				}
			}
			try {
				this.builder.add(b);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Returns a new test document builder. This builder has an unique id (among those created by this builder), and
	 * test title and description.
	 * @param contentType Content type.
	 * @return The created document builder, with node key, title and description.
	 */
	DocumentBuilder newTestBuilder(ContentType contentType) {
		return this.builder.newTestBuilder(contentType.getId());
	}

	/**
	 * Creates the repository.
	 * @return Thw newly created repository.
	 */
	public NodeRepository get() {
		try {
			return builder.build().getRepository();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
