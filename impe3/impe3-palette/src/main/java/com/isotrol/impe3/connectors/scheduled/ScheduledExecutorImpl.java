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

package com.isotrol.impe3.connectors.scheduled;


import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


/**
 * Scheduled Executor Service implementation.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class ScheduledExecutorImpl implements InitializingBean, DisposableBean, ScheduledService {
	private ScheduledExecutorService service;
	private ScheduledExecutorConfig config;

	public ScheduledExecutorImpl() {

	}

	public void setConfig(ScheduledExecutorConfig config) {
		this.config = config;
	}

	/**
	 * Set size to 1 and create scheduled thraed pool executors service.
	 */
	public void afterPropertiesSet() throws Exception {
		int size = 1;
		if (config != null) {
			Integer pool = config.poolSize();
			if (pool != null && pool > 1) {
				size = pool;
			}
		}
		service = Executors.newScheduledThreadPool(size);
	}

	/**
	 * Shutdown executors
	 */
	public void destroy() throws Exception {
		if (service == null) {
			service.shutdownNow();
		}
		service = null;
	}

	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#schedule(java.util.concurrent.Callable, long, java.util.concurrent.TimeUnit)
	 */
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		return service.schedule(callable, delay, unit);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#schedule(java.lang.Runnable, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
		return service.schedule(command, delay, unit);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#scheduleAtFixedRate(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
		return service.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#scheduleWithFixedDelay(java.lang.Runnable, long, long, java.util.concurrent.TimeUnit)
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
		return service.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#submit(java.util.concurrent.Callable)
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return service.submit(task);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#submit(java.lang.Runnable, java.lang.Object)
	 */
	public <T> Future<T> submit(Runnable task, T result) {
		return service.submit(task, result);
	}
	/**
	 * @see com.isotrol.impe3.connectors.scheduled.ScheduledService#submit(java.lang.Runnable)
	 */
	public Future<?> submit(Runnable task) {
		return service.submit(task);
	}

	public void execute(Runnable command) {
		service.execute(command);
	}
	
	
}
