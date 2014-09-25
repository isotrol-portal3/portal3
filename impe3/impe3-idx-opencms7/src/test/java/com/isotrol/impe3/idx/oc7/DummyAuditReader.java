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
package com.isotrol.impe3.idx.oc7;


import java.util.List;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.idx.oc.AuditReader;
import com.isotrol.impe3.idx.Task;


public class DummyAuditReader implements AuditReader<Task, Long> {

	public List<Task> readAuditBatch(Long checkpoint) {
		Task updateTask = new Task(1L, String.valueOf(1), 1, Task.UPDATEN);
		Task deleteTask = new Task(2L, String.valueOf(1), 1, Task.DELETEN);

		return ImmutableList.of(updateTask, deleteTask);
	}

}
