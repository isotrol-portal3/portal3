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

package com.isotrol.impe3.nr.core;


import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;
import net.sf.lucis.core.impl.DefaultWriter;
import net.sf.lucis.core.impl.RAMStore;
import net.sf.lucis.core.support.Queryables;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;

import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.NodeRepository;


/**
 * A RAM-based node repository with a fixed set of nodes.
 * @author Andres Rodriguez
 */
public final class ConstantRAMRepository {
	private final Store<Long> store;
	private final Queryable queryable;
	private final NodeRepository repository;

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Constructor.
	 * @param b Builder
	 * @throws InterruptedException
	 */
	private ConstantRAMRepository(Builder b) throws InterruptedException {
		final Batch<Long, Object> batch = b.batchBuilder.build(1L);
		final Writer writer = new DefaultWriter();
		this.store = new RAMStore<Long>();
		writer.write(store, batch);
		this.queryable = Queryables.simple(store);
		this.repository = new NodeRepositoryImpl(queryable);
	}

	/**
	 * Returns the node repository.
	 * @return The node repository.
	 */
	public NodeRepository getRepository() {
		return repository;
	}

	Store<Long> getStore() {
		return store;
	}

	Queryable getQueryable() {
		return queryable;
	}

	/**
	 * Builder for RAM repository.
	 * @author Andres Rodriguez
	 */
	public static final class Builder {
		private final Batch.Builder<Long> batchBuilder = Batch.builder();
		private final AtomicLong seq = new AtomicLong(0);

		/** Default Constructor. */
		Builder() {
		}

		/**
		 * Returns a new test document builder. This builder has an unique id (among those created by this builder), and
		 * test title and description.
		 * @param type Node type.
		 * @return The created document builder, with node key, title and description.
		 */
		public DocumentBuilder newTestBuilder(UUID type) {
			DocumentBuilder builder = new DocumentBuilder();
			final String id = Long.toString(seq.incrementAndGet());
			return builder.setNodeKey(NodeKey.of(type, id)).setTitle("Title " + id).setDescription("Description " + id);
		}

		/**
		 * Adds a document to repository builder
		 * @param builder document builder
		 * @param analyzer specific analyzer, could be null.
		 * @throws InterruptedException
		 */
		public Builder add(DocumentBuilder builder, Analyzer analyzer) throws InterruptedException {
			final Document d = builder.get();
			if (analyzer != null) {
				batchBuilder.add(d, analyzer);
			} else {
				batchBuilder.add(d);
			}
			return this;
		}

		/**
		 * Adds a document to repository builder using default analyzer
		 * @param builder document builder
		 * @return fluid builder
		 * @throws InterruptedException
		 */
		public Builder add(DocumentBuilder builder) throws InterruptedException {
			return add(builder, null);
		}

		/**
		 * Creates the repository.
		 * @return Thw newly created repository.
		 * @throws InterruptedException
		 */
		public ConstantRAMRepository build() throws InterruptedException {
			return new ConstantRAMRepository(this);
		}
	}

}
