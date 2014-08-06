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
package com.isotrol.impe3.oi.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.oi.server.LogTableDTO;
import com.isotrol.impe3.oi.server.LogTableManager;

/**
 * Log Table manager implementation.
 * @author Emilio Escobar Reyero
 */
@Service("oiLogTableManager")
public class LogTableManagerImpl implements LogTableManager {
	/** Log table component. */
	private OILogTableComponent logTable;
	
	/**
	 * @see com.isotrol.impe3.web20.server.LogTableManager#readBatch(long, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<LogTableDTO> readBatch(long checkpoint, String name) {
		return logTable.readBatch(checkpoint, name);
	}
	
	@Autowired
	public void setLogTable(OILogTableComponent logTable) {
		this.logTable = logTable;
	}
}
