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
