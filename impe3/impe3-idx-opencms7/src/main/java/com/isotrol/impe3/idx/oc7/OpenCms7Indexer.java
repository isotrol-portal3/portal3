package com.isotrol.impe3.idx.oc7;


import net.sf.lucis.core.Indexer;

import com.isotrol.impe3.idx.Task;
import com.isotrol.impe3.idx.oc.AbstractOpenCmsIndexer;
import com.isotrol.impe3.idx.oc.AuditReader;
import com.isotrol.impe3.idx.oc.DocumentContentBuilder;
import com.isotrol.impe3.idx.oc7.api.OpenCms7Schema;


/**
 * Indexer implementation for OpenCms 7
 * 
 * @author Emilio Escobar Reyero
 */
public class OpenCms7Indexer extends AbstractOpenCmsIndexer implements Indexer<Long, Object> {

	/**
	 * Constructor.
	 * @param auditReader log table reader
	 * @param documentContentBuilder lucene document builder
	 */
	public OpenCms7Indexer(AuditReader<Task, Long> auditReader, DocumentContentBuilder<Task> documentContentBuilder) {
		super(auditReader, documentContentBuilder);
	}


	@Override
	protected String fieldNameId() {
		return OpenCms7Schema.ID;
	}
	
	@Override
	public void afterCommit(Object payload) {
	}
	
}
