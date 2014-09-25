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
package com.isotrol.impe3.idx.oc;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.idx.oc.AuditReader;
import com.isotrol.impe3.idx.Task;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class OpenCmsAuditReader extends NamedParameterJdbcDaoSupport implements AuditReader<Task, Long> {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private int total = 100;
	private String sql = "SELECT ID_SEC, ID_ITEM, TIPO, OP FROM LOG WHERE OP IN(0,1) AND ID_SEC > :checkpoint ORDER BY ID_SEC LIMIT :total";

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc.AuditReader#readAuditBatch(java.lang.Object)
	 */
	@Transactional
	public List<Task> readAuditBatch(Long checkpoint) {

		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("checkpoint", checkpoint);
		map.put("total", total);

		@SuppressWarnings("unchecked")
		final List<Task> list = getNamedParameterJdbcTemplate().query(sql, map, Task.MAPPER);

		return cleanTaskList(list);
	}

	private List<Task> cleanTaskList(List<Task> original) {
		final List<Task> tasks = new LinkedList<Task>();
		final Set<String> ids = new HashSet<String>();

		for (int i = original.size() - 1; i >= 0; i--) {
			Task t = original.get(i);
			if (!ids.contains(t.getId())) {
				tasks.add(0, t);
				ids.add(t.getId());
			}
		}

		return tasks;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
