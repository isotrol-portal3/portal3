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
package com.isotrol.impe3.tickets.server.support;


import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.isotrol.impe3.hib.model.Entity;
import com.isotrol.impe3.tickets.server.model.SubjectEntity;


/**
 * Default configuration for the Tickets Server Session Factory.
 * @author Andres Rodriguez
 */
public class SessionFactoryBean extends AnnotationSessionFactoryBean {
	private static final String[] ANN_PACKAGES = {Entity.class.getPackage().getName()};
	private static final String[] SCAN_PACKAGES = {SubjectEntity.class.getPackage().getName()};

	/** Default constructor. */
	public SessionFactoryBean() {
		setAnnotatedPackages(ANN_PACKAGES);
		setPackagesToScan(SCAN_PACKAGES);
	}
}
