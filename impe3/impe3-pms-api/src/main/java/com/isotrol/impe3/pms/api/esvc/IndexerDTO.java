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

package com.isotrol.impe3.pms.api.esvc;

import com.isotrol.impe3.pms.api.AbstractDescribedWithId;
import com.google.gwt.user.client.rpc.IsSerializable;


public final class IndexerDTO extends AbstractDescribedWithId {//implements IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6898975278968413984L;
	
	private String state;
	private String type;
	private String node;

	public IndexerDTO() {		
	}

	public IndexerDTO(String id, String name, String description, String state, String type, String node) {
		super();
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.state = state;
		this.type = type;
		this.node = node;
	}

	public enum Estado {
		INICIADO,
		PARADO,
		REINDEXANDO;
	
		public String toString() {
			if (this.equals(Estado.INICIADO)) {
				return "Iniciado".toString();
			} else if (this.equals(Estado.PARADO)) {
				return "Parado".toString();
			} else if (this.equals(Estado.REINDEXANDO)) {
				return "Reindexando".toString();
			}else{
				return "";
			}
		}
	}

	


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
}
