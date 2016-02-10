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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.sf.lucis.core.Indexer;
import net.sf.lucis.core.Store;
import net.sf.lucis.core.Writer;
import net.sf.lucis.core.impl.AbstractIndexService;
import net.sf.lucis.core.impl.DefaultWriter;


/**
 * Index service register bean.
 * @author Enrique Diaz
 */
public final class IndexServiceRegister {

	private static IndexServiceRegister instance = new IndexServiceRegister();
	
	/** Service map. */
	private Map<String, List<Object>> indexers = Maps.newHashMap();
	
	
	
	private IndexServiceRegister() { 
	}
	
	/**
	 * get instance.
	 * @return
	 */
	public static final IndexServiceRegister getInstance() {
		return instance;
	}
	
	/**
	 * get the register indexers.
	 * @return
	 */
	public Map<String, List<Object>> getRegisters() {
		return indexers;
	}
	
	public void setIndexers(String nameIdx, AbstractIndexService service, Store<?> store,IndexServiceDfn definition) {
		List<Object> properties = new ArrayList<Object>();
		properties.add(service);
		//properties.add(writer);
		properties.add(store);
		properties.add(definition);
		
		indexers.put(nameIdx, properties);
		
	}
	
	public AbstractIndexService getIndexService(String key) {
		AbstractIndexService ais = null;
		
		for(Object obj : indexers.get(key)) {
			if(obj instanceof AbstractIndexService) {
				ais= (AbstractIndexService) obj;
			}
		}
		return ais;
	}
	
	public IndexServiceDfn getDefinition(String key) {
		IndexServiceDfn dw=null;
		
		for(Object obj:indexers.get(key)){
			if(obj instanceof IndexServiceDfn){
				dw= (IndexServiceDfn)obj;
			}
		}
		return dw;
		
		
	}
	
	public Store<?> getStore(String key) {
		Store<?> store=null;
		
		for(Object obj:indexers.get(key)){
			if(obj instanceof Store){
				store= (Store<?>)obj;
			}
		}
		return store;
		
		
	}
	
	public Indexer<?, ?> getIndexer(String key) {
		Indexer<?, ?> indexer=null;
		
		for(Object obj:indexers.get(key)){
			if(obj instanceof Indexer){
				indexer= (Indexer<?,?>)obj;
			}
		}
		return indexer;
		
		
	}
	
}


