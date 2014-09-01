package com.isotrol.impe3.idx.oc;


import java.util.List;

import net.sf.lucis.core.Batch;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isotrol.impe3.idx.Task;


public abstract class AbstractOpenCmsIndexer {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected AuditReader<Task, Long> auditReader;
	protected DocumentContentBuilder<Task> documentContentBuilder;

	protected abstract String fieldNameId();

	/**
	 * Constructor.
	 * @param auditReader log table reader
	 * @param documentContentBuilder lucene document builder
	 */
	protected AbstractOpenCmsIndexer(AuditReader<Task, Long> auditReader,
		DocumentContentBuilder<Task> documentContentBuilder) {
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

		final Batch<Long, Object> batch = generateBatch(checkpoint == null ? 0L : checkpoint);

		if (logger.isDebugEnabled()) {
			logger.debug("New index checkpoint at {}", batch.getCheckpoint());
		}

		return batch;
	}

	protected Batch<Long, Object> generateBatch(final Long startPoint) throws InterruptedException {

		if (logger.isTraceEnabled()) {
			logger.trace("Batch starting at {} position.", startPoint);
		}

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

				builder.delete(fieldNameId(), t.getId());
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
							builder.update(document, fieldNameId(), t.getId());
						}
					}

				} catch (Exception e) {
					logger.warn("[{}, {}, {}, {}] {}", new Object[] {t.getSec(), t.getId(), t.getType(),
						t.getOp().getOrdinal(), e.getMessage()});
					logger.debug("Error trace. ", e);
				}

			}
			checkpoint = t.getSec();
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Batch ends at {} ", checkpoint);
		}

		return builder.build(checkpoint);
	}

}
