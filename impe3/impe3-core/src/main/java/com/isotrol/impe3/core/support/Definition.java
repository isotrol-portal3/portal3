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

package com.isotrol.impe3.core.support;


import net.sf.derquinsej.i18n.Localized;

import com.google.common.base.Objects;
import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Version;


/**
 * Base class for the definitions of modules, components and configurations. Definitions must be immutable objects.
 * @author Andres Rodriguez.
 * @param <T> Element class.
 */
public abstract class Definition<T> extends TypeRelated<T> implements Named {
	/** Name information. */
	private final NamedSupport name;
	/** Version information. */
	private final String version;
	/** Copyright information. */
	private final String copyright;
	/** Hash code. */
	private final int hash;

	/**
	 * Constructor.
	 * @param type Type to analyze.
	 */
	protected Definition(Class<T> type) {
		super(type);
		this.name = new NamedSupport(type);
		this.version = StringValue.value(type, Version.class);
		this.copyright = StringValue.value(type, Copyright.class);
		this.hash = Objects.hashCode(getClass(), type);
	}

	public String getVersion() {
		return version;
	}

	public String getCopyright() {
		return copyright;
	}

	/**
	 * @see com.isotrol.impe3.core.support.Named#getDescription()
	 */
	public Localized<String> getDescription() {
		return name.getDescription();
	}

	/**
	 * @see com.isotrol.impe3.core.support.Named#getName()
	 */
	public Localized<String> getName() {
		return name.getName();
	}

	/**
	 * Return hash atribute value
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return hash;
	}

	/**
	 * Compare object class and type
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof Definition<?>) {
			final Definition<?> d = (Definition<?>) obj;
			return getClass().equals(d.getClass()) && getType().equals(d.getType());
		}
		return false;
	}
}
