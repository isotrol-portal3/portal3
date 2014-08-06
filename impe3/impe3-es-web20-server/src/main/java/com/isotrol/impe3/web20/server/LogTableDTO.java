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
package com.isotrol.impe3.web20.server;

import com.isotrol.impe3.dto.AbstractLongId;

/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class LogTableDTO extends AbstractLongId {
	/** serial uid. */
	private static final long serialVersionUID = 8233821743642933776L;

	private String item;
	private String name;
	private int task;
	private Long replacedBy;
	
	
	public LogTableDTO() {
	}
	
	public String getItem() {
		return item;
	}
	
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTask() {
		return task;
	}
	
	public void setTask(int task) {
		this.task = task;
	}
	
	public Long getReplacedBy() {
		return replacedBy;
	}
	
	public void setReplacedBy(Long replacedBy) {
		this.replacedBy = replacedBy;
	}
	
	
}
