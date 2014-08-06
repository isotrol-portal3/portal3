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
package com.isotrol.impe3.web20.impl;


import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;


/**
 * Scheduled executor component.
 * @author Andres Rodriguez.
 */
@Component
public final class SchedulerComponent implements DisposableBean {
	/** Executor service. */
	private final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

	/** Constructor. */
	public SchedulerComponent() {
	}

	public void execute(Runnable command) {
		ses.execute(command);
	}

	public Future<?> submit(Runnable task) {
		return ses.submit(task);
	}

	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		final long wait = unit.convert(30L, TimeUnit.SECONDS);
		return ses.scheduleWithFixedDelay(command, Math.max(initialDelay, wait), delay, unit);
	}

	public void destroy() throws Exception {
		ses.shutdown();
		ses.awaitTermination(300, TimeUnit.SECONDS);
	}

}
