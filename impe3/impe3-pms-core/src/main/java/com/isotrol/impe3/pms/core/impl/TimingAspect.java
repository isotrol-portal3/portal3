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

package com.isotrol.impe3.pms.core.impl;


import static net.sf.derquinsej.stats.Timings.createAccumulatingMap;

import java.util.concurrent.TimeUnit;

import net.sf.derquinsej.stats.AccumulatingTimingMap;
import net.sf.derquinsej.stats.Timing;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.common.base.Stopwatch;
import com.isotrol.impe3.core.Loggers;


/**
 * Timing aspect.
 * @author Andres Rodriguez.
 */
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TimingAspect {
	/** Logger. */
	private final Logger logger = Loggers.pms();
	/** Timing map. */
	private final AccumulatingTimingMap<String> map = createAccumulatingMap(TimeUnit.MILLISECONDS);

	/**
	 * Constructor.
	 */
	public TimingAspect() {
	}

	@Around("@within(org.springframework.stereotype.Service)")
	public Object time(ProceedingJoinPoint pjp) throws Throwable {
		final Stopwatch w = new Stopwatch().start();
		try {
			return pjp.proceed();
		}
		finally {
			final long t = w.elapsedMillis();
			final String key = pjp.getTarget().getClass().getName() + "." + pjp.getSignature().toShortString();
			map.add(key, t);
			if (t > 500) {
				logger.warn(String.format("[%s] took [%d] ms", key, t));
			}
		}
	}

	public AccumulatingTimingMap<String> getTimingMap() {
		return map;
	}

	public Timing getTiming() {
		return map.getAccumulator();
	}
}
