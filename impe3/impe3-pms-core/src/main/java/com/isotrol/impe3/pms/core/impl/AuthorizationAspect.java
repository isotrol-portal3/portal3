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


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.api.AbstractWithId;
import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.NoSessionException;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;


/**
 * Authorization aspect.
 * @author Andres Rodriguez.
 */
@Component
@Aspect
@Order(11)
public class AuthorizationAspect {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final AuthorizationComponent component;

	/**
	 * Constructor.
	 */
	@Autowired
	public AuthorizationAspect(AuthorizationComponent component) {
		this.component = component;
	}

	@Around("@annotation(com.isotrol.impe3.pms.core.impl.Authorized)")
	public Object global(ProceedingJoinPoint pjp) throws Throwable {
		final Authorization a = component.getAuthorization();
		if (a == null) {
			log(pjp, "Not logged in. Not authorized");
			throw new NoSessionException();
		}
		final Method m = ((MethodSignature) (pjp.getSignature())).getMethod();
		final Method targetMethod = pjp.getTarget().getClass().getMethod(m.getName(), m.getParameterTypes());
		final Authorized ann = targetMethod.getAnnotation(Authorized.class);
		// Required global authorities
		final boolean hasGA = ann.global().length > 0;
		// Required portal authorities
		final boolean hasPA = ann.portal().length > 0;
		// If no authorities are required the operation is allowed to proceed.
		if (!hasGA && !hasPA) {
			log(pjp, "No required authorities. Proceed");
			return pjp.proceed();
		}
		// If the user is root and the operation is allowed to root, let it go
		if (a.isRoot() && ann.root()) {
			log(pjp, "Root access allowed. Proceed");
			return pjp.proceed();
		}
		// Global authorities.
		if (hasGA && a.hasGlobal(Arrays.asList(ann.global()))) {
			log(pjp, "Global authorities checked. Proceed");
			return pjp.proceed();
		}
		if (hasPA && a.hasPortal(getPortalId(pjp), Arrays.asList(ann.portal()))) {
			log(pjp, "Portal authorities checked. Proceed");
			return pjp.proceed();
		}
		log(pjp, "Not authorized");
		throw new AuthorizationException();
	}

	private void log(ProceedingJoinPoint pjp, String message) {
		if (logger.isTraceEnabled()) {
			logger.trace("Authorized method {}: {}", pjp.toShortString(), message);
		}
	}

	private UUID getPortalId(ProceedingJoinPoint pjp) throws AuthorizationException {
		final Object[] args = pjp.getArgs();
		if (args == null || args.length == 0) {
			log(pjp, "Unable to fetch portal id. Not authorized");
			throw new AuthorizationException();
		}
		final Object arg = args[0];
		String id = null;
		if (arg instanceof String) {
			id = (String) arg;
		} else if (arg instanceof PortalPagesLoc) {
			id = ((PortalPagesLoc) arg).getPortalId();
		} else if (arg instanceof AbstractWithId) {
			id = ((AbstractWithId) arg).getId();
		}
		if (id == null) {
			log(pjp, "Unable to fetch portal id. Not authorized");
			throw new AuthorizationException();
		}
		try {
			return UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			log(pjp, "Invalid portal id. Not authorized");
			throw new AuthorizationException();
		}
	}

}
