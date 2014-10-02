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

package com.isotrol.impe3.idx;

import java.util.List;

import net.sf.lucis.core.DirectoryProvider;
import net.sf.lucis.core.Queryable;
import net.sf.lucis.core.support.Queryables;

import org.springframework.beans.factory.FactoryBean;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Support class implements a factory for lucis queryable.
 * @author Emilio Escobar Reyero
 */
public class SearcherFactory implements FactoryBean<Queryable> {

	final List<DirectoryProvider> stores;

	/**
	 * Return Queryables.simple if only have one store and Queryables.multi in other case
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Queryable getObject() throws Exception {
		if (stores.size() == 1) {
			return Queryables.simple(stores.get(0));
		} else {
			return Queryables.multi(stores);
		}
	}

	/** Returns Queryable class */
	public Class<Queryable> getObjectType() {
		return Queryable.class;
	}

	/** Return false. */
	public boolean isSingleton() {
		return false;
	}

	/**
	 * Create a new factory.
	 * @param store lucis store must be non null object
	 */
	public SearcherFactory(DirectoryProvider store) {
		Preconditions.checkNotNull(store);
		this.stores = ImmutableList.of(store);
	}

	/**
	 * Create a new factory.
	 * @param store lucis store must be non null object
	 */
	public SearcherFactory(List<DirectoryProvider> stores) {
		Preconditions.checkNotNull(stores);
		this.stores = ImmutableList.copyOf(stores);
	}
}
