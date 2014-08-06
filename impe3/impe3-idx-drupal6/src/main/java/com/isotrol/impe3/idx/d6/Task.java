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
package com.isotrol.impe3.idx.d6;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;


/**
 * Task bean
 * @author Emilio Escobar Reyero
 */
public class Task {
	private final long sec;
	private final String id;
	private final String type;
	private final TaskOperation op;

	/**
	 * Constructor
	 * @param sec sec number
	 * @param id content id
	 * @param type content type
	 * @param op operation
	 */
	public Task(final long sec, final String id, final String type, final TaskOperation op) {
		this.sec = sec;
		this.id = id;
		this.type = type;
		this.op = op;
	}

	public long getSec() {
		return sec;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public TaskOperation getOp() {
		return op;
	}

	/**
	 * Log table row mapper.
	 */
	public static final ParameterizedRowMapper<Task> MAPPER = new ParameterizedRowMapper<Task>() {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Task(rs.getLong(1), rs.getString(2), rs.getString(3), OPERATIONS.get(rs.getInt(4)));
		}
	};

	/** Update online constant. */
	public static final TaskOperation UPDATEN = new TaskOperation("UPDATE_N", 0, false);
	/** Delete online constant. */
	public static final TaskOperation DELETEN = new TaskOperation("DELETE_N", 1, true);
	/** Update offline constant. */
	public static final TaskOperation UPDATEF = new TaskOperation("UPDATE_F", 2, false);
	/** Delete offline constant. */
	public static final TaskOperation DELETEF = new TaskOperation("DELETE_F", 3, true);

	/**
	 * Map with operations int values.
	 */
	private static final ImmutableMap<Integer, TaskOperation> OPERATIONS = new ImmutableMap.Builder<Integer, TaskOperation>()
		.put(0, UPDATEN).put(1, DELETEN).put(2, UPDATEF).put(3, DELETEF).build();

	/**
	 * Define an operation.
	 * @author Emilio Escobar Reyero
	 */
	public static final class TaskOperation {
		private final String name;
		private final int id;
		private final boolean del;

		private TaskOperation() {
			throw new AssertionError();
		}

		private TaskOperation(final String name, final int id, final boolean del) {
			this.name = name;
			this.id = id;
			this.del = del;
		}

		/** Returns id */
		public int getOrdinal() {
			return id;
		}

		public String getName() {
			return name;
		}

		public boolean isDelOperation() {
			return del;
		}

		/**
		 * Retur true if object is an instance of TaskOperation and id and name are the same.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}

			if (!(obj instanceof TaskOperation)) {
				return false;
			}

			TaskOperation t = (TaskOperation) obj;

			return this.id == t.id && this.name.equals(t.name);
		}

		/**
		 * Implemented with com.google.common.Objects
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(name, id, del);
		}
	}
}
