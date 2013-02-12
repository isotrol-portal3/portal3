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

package com.isotrol.impe3.hib.support;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

import com.isotrol.impe3.hib.model.Entity;


/**
 * Factory bean for IMPE3 session factories.
 * @author Andres Rodriguez
 */
public abstract class AbstractSessionFactoryBean extends AnnotationSessionFactoryBean {
	private static final String PACKAGE = Entity.class.getPackage().getName();
	private static final String[] PACKAGES = new String[] {PACKAGE};

	private static String[] join(String[] annotated) {
		if (annotated == null || annotated.length == 0) {
			return PACKAGES;
		}
		final String[] array = new String[annotated.length + 1];
		array[0] = PACKAGE;
		System.arraycopy(annotated, 0, array, 1, annotated.length);
		return array;
	}

	/**
	 * Constructor.
	 * @param annotated Annotated packages.
	 * @param scan Packages to scan.
	 */
	protected AbstractSessionFactoryBean(String[] annotated, String[] scan) {
		checkNotNull(scan);
		checkArgument(scan.length > 0);
		setAnnotatedPackages(join(annotated));
		setPackagesToScan(PACKAGES);
	}

	/**
	 * Constructor.
	 * @param scan Packages to scan.
	 */
	protected AbstractSessionFactoryBean(String[] scan) {
		this(null, scan);
	}

}
