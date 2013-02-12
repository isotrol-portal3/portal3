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

package com.isotrol.impe3.pms.core;


import java.util.UUID;

import com.google.common.base.Supplier;
import com.isotrol.impe3.pms.model.WithIdVersion;


/**
 * Portal transaction provider.
 * @author Andres Rodriguez.
 */
public interface PortalTxProvider {
	/** Wraps a supplier in a required transaction. */
	<T> Supplier<T> wrapInTx(Supplier<T> supplier, boolean memoize);

	/** Returns a memoized transactional supplier which loads the identified entity using the provided loader. */
	<E extends WithIdVersion, V, P> Supplier<V> getTxSupplier(Class<E> type, UUID id, P payload,
		ValueLoader<E, V, P> loader);
}
