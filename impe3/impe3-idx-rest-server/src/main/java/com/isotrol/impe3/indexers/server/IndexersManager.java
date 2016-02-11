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

package com.isotrol.impe3.indexers.server;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.idx.config.IndexServiceDfn;
import com.isotrol.impe3.idx.config.IndexServiceRegister;
import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;

import net.sf.lucis.core.Store;
import net.sf.lucis.core.impl.AbstractIndexService;

/**
 * Indexer sevices manager.
 * @author Zineb Ghamiri
 */
@Component
public class IndexersManager {
	
	public IndexersManager() {
		super();
	}

	/**
	 * Obtain list of indexers services.
	 * @return
	 */
	public IndexersDTO getIndexers(){
		Map<String, List<Object>> mapIsr= IndexServiceRegister.getInstance().getRegisters();
		
		IndexersDTO listaIndexadores = new IndexersDTO();
		if(!mapIsr.isEmpty()){
			for(Entry<String, List<Object>> idx: mapIsr.entrySet()){
				IndexerDTO indexer=getDTO(idx.getKey());
				listaIndexadores.getList().add(indexer);
			}
		}
		
		return listaIndexadores;
	}
	
	/**
	 * Stop the indexer with id equal 'name'.
	 * @param name
	 * @return
	 */
	public IndexerDTO stop(String name){
		AbstractIndexService indexador= IndexServiceRegister.getInstance().getIndexService(name);
		indexador.stop();
		IndexerDTO indexer=getDTO(name);
		return indexer;
		
		
	}
	
	/**
	 * Start the indexer with id equal 'name'.
	 * @param name
	 * @return
	 */
	public IndexerDTO start(String name){
		AbstractIndexService indexador= IndexServiceRegister.getInstance().getIndexService(name);
		indexador.start();
		IndexerDTO indexer=getDTO(name);
		return indexer;
		
		
	}
	
	/**
	 * Restart the indexation task.
	 * @param name
	 * @return
	 */
	public IndexerDTO reindex(String name){
		AbstractIndexService indexador= IndexServiceRegister.getInstance().getIndexService(name);
		indexador.stop();
		indexador.start();
		IndexerDTO indexer=getDTO(name);
		return indexer;
		
		
	}
	
	/**
	 * Launch a full indexation.
	 * @param name
	 * @param copia
	 * @return
	 */
	public IndexerDTO reindexAll(String name, boolean copia) {
		AbstractIndexService indexador= IndexServiceRegister.getInstance().getIndexService(name);
		
		indexador.stop();
		try {
			Store<?> store = IndexServiceRegister.getInstance().getStore(name);
			
			if (store != null && store.getDirectory() != null) {
				String[] fileNames = store.getDirectory().listAll();
				if (copia) {
					if(IndexServiceRegister.getInstance().getStore(name).getDirectory() instanceof FSDirectory){
						File backup = new File(((FSDirectory) store.getDirectory()).getDirectory().getAbsolutePath().concat("-backup"));
						Directory to = FSDirectory.open(backup);
						for (String file : fileNames) {
							
							store.getDirectory().copy(to, file, file);
							store.getDirectory().deleteFile(file);
						}
					}
				} else {			
					for (String fileName : fileNames) {
						store.getDirectory().deleteFile(fileName);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		indexador.start();
		IndexerDTO indexer=getDTO(name);
		return indexer;
		
	}
	
	/**
	 * Build indexer service dto.
	 * @param name
	 * @return
	 */
	public IndexerDTO getDTO(String name){
		
		AbstractIndexService indexador=IndexServiceRegister.getInstance().getIndexService(name);
		IndexServiceDfn def=IndexServiceRegister.getInstance().getDefinition(name);
		IndexerDTO myDTO=new IndexerDTO();
		myDTO.setId(indexador.getName());
		myDTO.setName(def.getPrettyName());
		myDTO.setState(indexador.getStatus().toString());
		myDTO.setDescription(def.getDescription());
		myDTO.setType(def.getIndexationMode());
		
		return myDTO;
		
	}

}
