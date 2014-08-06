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


import java.util.UUID;

import net.sf.derquinsej.uuid.UUIDGenerator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.dto.InternalServiceException;
import com.isotrol.impe3.dto.ServiceException;


/**
 * Exception transformation aspect.
 * @author Andres Rodriguez.
 */
@Component
@Aspect
@Order(11)
public class ServiceExceptionAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UUIDGenerator uuidGenerator;

	/**
	 * Constructor.
	 */
	@Autowired
	public ServiceExceptionAspect(UUIDGenerator uuidGenerator) {
		this.uuidGenerator = uuidGenerator;
	}

	@Around("@within(org.springframework.stereotype.Service)")
	public Object transform(ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			final UUID id = uuidGenerator.get();
			final String msg = String.format("Internal service exception %s", id);
			logger.error(msg, e);
			throw new InternalServiceException(id);
		}
	}

}
