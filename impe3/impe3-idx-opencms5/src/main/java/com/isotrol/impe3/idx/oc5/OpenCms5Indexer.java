package com.isotrol.impe3.idx.oc5;


import net.sf.lucis.core.Indexer;

import com.isotrol.impe3.idx.Task;
import com.isotrol.impe3.idx.oc.AbstractOpenCmsIndexer;
import com.isotrol.impe3.idx.oc.AuditReader;
import com.isotrol.impe3.idx.oc.DocumentContentBuilder;
import com.isotrol.impe3.idx.oc.api.OpenCmsSchema;


/**
 * Indexer implementation for OpenCms5
 * 
 * @author Emilio Escobar Reyero
 */
public class OpenCms5Indexer extends AbstractOpenCmsIndexer implements Indexer<Long, Object> {

	/**
	 * Constructor.
	 * @param auditReader log table reader
	 * @param documentContentBuilder lucene document builder
	 */
	public OpenCms5Indexer(AuditReader<Task, Long> auditReader, DocumentContentBuilder<Task> documentContentBuilder) {
		super(auditReader, documentContentBuilder);
	}


	@Override
	protected String fieldNameId() {
		return OpenCmsSchema.ID;
	}
	
	@Override
	public void afterCommit(Object payload) {
	}
}
