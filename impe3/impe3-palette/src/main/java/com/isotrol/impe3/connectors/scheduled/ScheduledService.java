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
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Scheduled Service.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface ScheduledService extends Executor {
	/**
	 * Submit a new task
	 * @param <T> tast type
	 * @param task task to execute
	 * @return when is going to execute
	 */
	<T> Future<T> submit(Callable<T> task);

	<T> Future<T> submit(Runnable task, T result);

	Future<?> submit(Runnable task);

	<V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

	ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);

	ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit);

	ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
}
