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
package com.isotrol.impe3.idx.d6;

import java.util.List;

import net.sf.lucis.core.Batch;
import net.sf.lucis.core.Indexer;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isotrol.impe3.idx.d6.api.Drupal6Schema;

/**
 * Indexer implementation for Drupal 6
 */
public class Drupal6Indexer implements Indexer<Long, Object> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private AuditReader<Task, Long> auditReader;
	private DocumentContentBuilder<Task> documentContentBuilder;

	/**
	 * Constructor.
	 * @param auditReader log table reader
	 * @param documentContentBuilder lucene document builder
	 */
	public Drupal6Indexer(AuditReader<Task, Long> auditReader, DocumentContentBuilder<Task> documentContentBuilder) {
		this.auditReader = auditReader;
		this.documentContentBuilder = documentContentBuilder;
	}

	/**
	 * @see net.sf.lucis.core.Indexer#index(java.lang.Object)
	 */
	public Batch<Long, Object> index(Long checkpoint) throws InterruptedException {

		if (logger.isDebugEnabled()) {
			logger.debug("Beggining index checkpoint: {}", checkpoint);
		}

		final Batch<Long, Object> batch;
		if (checkpoint == null) {
			batch = generateBatch(0L);
		} else {
			batch = generateBatch(checkpoint);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("New index checkpoint at {}", batch.getCheckpoint());
		}

		return batch;
	}

	private Batch<Long, Object> generateBatch(final Long startPoint) throws InterruptedException {

		logger.trace("Batch starting at {} position.", startPoint);

		List<Task> tasks = auditReader.readAuditBatch(startPoint);
		long checkpoint = startPoint.longValue();

		final Batch.Builder<Long> builder = Batch.builder();

		for (Task t : tasks) {
			if (logger.isTraceEnabled()) {
				logger.trace("Task sec position {} ", t.getSec());
			}

			if (t.getOp().isDelOperation()) {
				if (logger.isTraceEnabled()) {
					logger.trace("Task delete operation for content id {} ", t.getId());
				}

				builder.delete(Drupal6Schema.ID, t.getId());
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Task update operation. Generating document for content id {} ", t.getId());
				}

				try {
					Document[] documents = documentContentBuilder.createDocuments(t);

					if (logger.isTraceEnabled()) {
						logger.trace("Build {} document(s) for content id {} ", documents.length, t.getId());
					}

					for (Document document : documents) {
						if (document != null) {
							builder.update(document, Drupal6Schema.ID, t.getId());
						}
					}

				} catch (Exception e) {
					logger.warn("[{}, {}, {}, {}] {}", new Object[] { t.getSec(), t.getId(), t.getType(), t.getOp().getOrdinal(),
							e.getMessage() });
					logger.debug("Error trace. ", e);
				}

			}
			checkpoint = t.getSec();
		}

		logger.trace("Batch ends at {} ", checkpoint);

		return builder.build(checkpoint);
	}

	@Override
	public void afterCommit(Object payload) {
	}
}
