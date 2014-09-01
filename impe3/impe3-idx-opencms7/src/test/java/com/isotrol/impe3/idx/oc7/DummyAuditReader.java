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
