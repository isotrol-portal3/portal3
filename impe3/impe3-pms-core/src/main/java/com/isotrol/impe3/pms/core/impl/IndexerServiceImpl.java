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

package com.isotrol.impe3.pms.core.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceDTO;
import com.isotrol.impe3.pms.api.esvc.ExternalServiceType;
import com.isotrol.impe3.pms.api.esvc.ExternalServicesService;
import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersService;


@Service("indexersService")
public final class IndexerServiceImpl extends AbstractExternalService<IndexersService> implements IndexersService  {
	@Autowired
	ExternalServicesService externalService;
	public IndexerServiceImpl() {
		super(IndexersService.class);
		
	}


	@Override
	@Transactional(rollbackFor = Throwable.class)
	public IndexersDTO getIndexers() throws PMSException {
		IndexersDTO lista = new IndexersDTO();
		
		List<ExternalServiceDTO> connectors = externalService.getServices(ExternalServiceType.INDEXER_REPOSITORY);
		
		for (ExternalServiceDTO nodo : connectors) {
			lista.getList().addAll(getIndexersFromNode(nodo).getList());
			
		}
		
		return lista;
	}

	/**
	 * 
	 * @param nodo
	 * @return
	 */
	public IndexersDTO getIndexersFromNode(ExternalServiceDTO nodo) {
		
		IndexersDTO listaIndex= new IndexersDTO();
        IndexersService repository = null;
		try {
			repository = getExternalService(nodo.getId());
		} catch (PMSException e1) {
			
			e1.printStackTrace();
		}
        try {
        	for(IndexerDTO idxDto :repository.getIndexers().getList()){
        		idxDto.setNode(nodo.getName());
        		
        		listaIndex.getList().add(idxDto);
        	}
			
		} catch (ServiceException e) {
			
			e.printStackTrace();
		}
        return listaIndex;
	}
	
	
	
		@Override
		@Transactional(rollbackFor = Throwable.class)
		public IndexerDTO stopIndexer(String name, String nodeName) throws PMSException {
			List<ExternalServiceDTO> connectors = externalService.getServices(ExternalServiceType.INDEXER_REPOSITORY);
			IndexerDTO indexStopped= new IndexerDTO();
			for(ExternalServiceDTO esdto : connectors){
				if(esdto.getName().equals(nodeName)){
					try{
						IndexersService rep =getExternalService(esdto.getId());
						for (IndexerDTO idxDto :rep.getIndexers().getList()){
							if(idxDto.getId().equals(name)){
								indexStopped=rep.stopIndexer(name, esdto.getId());
								indexStopped.setNode(nodeName);
								
							}
						}
					}catch(ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			return indexStopped;
	}
		
		@Override
		@Transactional(rollbackFor = Throwable.class)
		public IndexerDTO startIndexer(String name, String nodeName) throws PMSException {
			List<ExternalServiceDTO> connectors = externalService.getServices(ExternalServiceType.INDEXER_REPOSITORY);
			IndexerDTO indexStarted= new IndexerDTO();
			for(ExternalServiceDTO esdto : connectors){
				if(esdto.getName().equals(nodeName)){
					try{
						IndexersService rep =getExternalService(esdto.getId());
						for (IndexerDTO idxDto :rep.getIndexers().getList()){
							if(idxDto.getId().equals(name)){
								indexStarted=rep.startIndexer(name, esdto.getId());
								indexStarted.setNode(nodeName);
								
							}
						}
					}catch(ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			return indexStarted;
	}
		
		@Override
		@Transactional(rollbackFor = Throwable.class)
		public IndexerDTO reindex(String name, String nodeName) throws PMSException {
			List<ExternalServiceDTO> connectors = externalService.getServices(ExternalServiceType.INDEXER_REPOSITORY);
			IndexerDTO reindexed= new IndexerDTO();
			for(ExternalServiceDTO esdto : connectors){
				if(esdto.getName().equals(nodeName)){
					try{
						IndexersService rep =getExternalService(esdto.getId());
						for (IndexerDTO idxDto :rep.getIndexers().getList()){
							if(idxDto.getId().equals(name)){
								reindexed=rep.reindex(name, esdto.getId());
								reindexed.setNode(nodeName);
								
							}
						}
					}catch(ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			return reindexed;
	}
		
		@Override
		@Transactional(rollbackFor = Throwable.class)
		public IndexerDTO reindexAll(String name, String nodeName, boolean copia) throws PMSException {
			List<ExternalServiceDTO> connectors = externalService.getServices(ExternalServiceType.INDEXER_REPOSITORY);
			IndexerDTO reindexed= new IndexerDTO();
			for(ExternalServiceDTO esdto : connectors){
				if(esdto.getName().equals(nodeName)){
					try{
						IndexersService rep =getExternalService(esdto.getId());
						for (IndexerDTO idxDto :rep.getIndexers().getList()){
							if(idxDto.getId().equals(name)){
								reindexed=rep.reindexAll(name, esdto.getId(), copia);
								reindexed.setNode(nodeName);
								
							}
						}
					}catch(ServiceException e) {
						e.printStackTrace();
					}
				}
			}
			return reindexed;
	}
	
	
	public ExternalServicesService getExternalService() {
		return externalService;
	}

	public void setExternalService(ExternalServicesService externalService) {
		this.externalService = externalService;
	}


}
