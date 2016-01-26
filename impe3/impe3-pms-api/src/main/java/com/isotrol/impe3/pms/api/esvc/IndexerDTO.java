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

import com.isotrol.impe3.pms.api.AbstractWithId;

import com.isotrol.impe3.pms.api.Described;

import net.sf.lucis.core.IndexStatus;

public class IndexerDTO extends AbstractWithId implements Described {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4870680008470132789L;
	

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

	
	private String name;
	private String description;
	private Estado state;
	private String type;
	private String node;

	public Estado getState() {
		return state;
	}

	public void setState(Estado state) {
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name=name;
		
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public void setDescription(String description) {
		// TODO Auto-generated method stub
		this.description=description;
		
	}

}
