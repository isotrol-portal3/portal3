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

package com.isotrol.impe3.idx.config;


import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.lucene.analysis.Analyzer;

import net.sf.lucis.core.Delays;
import net.sf.lucis.core.FullIndexer;
import net.sf.lucis.core.Indexer;
import net.sf.lucis.core.IndexerService;
import net.sf.lucis.core.ReindexingStore;
import net.sf.lucis.core.ReindexingWriter;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.impl.DefaultReindexingWriter;
import net.sf.lucis.core.impl.ReindexingIndexerService;


/**
 * Full index service factory bean.
 * @author Andres Rodriguez Chamorro
 */
public final class FullIndexServiceFactoryBean extends AbstractIndexServiceFactoryBean {
	/** Store. */
	private final ReindexingStore store;
	/** Delays. */
	private final FullIndexer<?> indexer;
	/** Service. */
	private volatile ReindexingIndexerService<?> service = null;
	
	private IndexServiceDfn definition = null;

	FullIndexServiceFactoryBean(Analyzer analyzer, ReindexingStore store, FullIndexer<?> indexer) {
		super(analyzer);
		this.store = checkNotNull(store, "The store must be provided");
		this.indexer = checkNotNull(indexer, "The indexer must be provided");
	}
	
	FullIndexServiceFactoryBean(Analyzer analyzer,  ReindexingStore store, FullIndexer<?> indexer, IndexServiceDfn definition) {
		this(analyzer, store, indexer);
		this.definition = definition;
	}

	public synchronized void afterPropertiesSet() throws Exception {
		if (service == null) {
			final DefaultReindexingWriter writer = new DefaultReindexingWriter(getSupplier());
			writer.setName(getWriterName());
			// Through a method to avoid generic warnings
			ReindexingIndexerService<?> s = createService(writer, indexer);
			s.setName(getName());
			Delays d = getDelays();
			if (d != null) {
				s.setDelays(d);
			}
			s.start();
			service = s;
			if (this.definition == null) {
				// Inicializa la definicion del servicio con valores por defecto
				String nameIdx=this.getName();
				String descIdx="indexador "+nameIdx;
				String modeIdx;
				if(nameIdx.contains("off")){
					modeIdx="Offline";
				}else{
					modeIdx="Online";
				}
				
				this.definition = new IndexServiceDfn(nameIdx, modeIdx, descIdx);
			}
			
			// Register the index service		
			IndexServiceRegister.getInstance().setIndexers(s.getName(), s, null, definition);
		}
	}
	
	private <T> ReindexingIndexerService<T> createService(ReindexingWriter writer, FullIndexer<T> indexer) {
		return new ReindexingIndexerService<T>(store, writer, indexer, null, isPasive());
	}

	public IndexerService getObject() throws Exception {
		return service;
	}

	public synchronized void destroy() throws Exception {
		if (service != null) {
			service.stop();
			service = null;
		}
	}
}
