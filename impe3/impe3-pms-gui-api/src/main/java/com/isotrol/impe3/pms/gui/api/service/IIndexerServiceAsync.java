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

package com.isotrol.impe3.pms.gui.api.service;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.esvc.IndexerDTO;
import com.isotrol.impe3.pms.api.esvc.IndexersDTO;

public interface IIndexerServiceAsync {
	

	/**
	 * Returns the manageable services.
	 * @param type Service type.
	 */
	void getIndexers(AsyncCallback<IndexersDTO> callback);
	
	
	void stopIndexer(String name, String id ,AsyncCallback<IndexerDTO> callback);
	
	void startIndexer(String name, String id ,AsyncCallback<IndexerDTO> callback);

	void reindex(String name, String id ,AsyncCallback<IndexerDTO> callback);

	void reindexAll(String name, String id, boolean copia ,AsyncCallback<IndexerDTO> callback);
	
}