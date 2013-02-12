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

package com.isotrol.impe3.gui.common.util;


import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.StoreSorter;


/**
 * Sorts alphabetically, ignoring case letters.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class AlphabeticalStoreSorter extends StoreSorter<ModelData> {
	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.store.StoreSorter #compare(com.extjs.gxt.ui.client.store.Store,
	 * com.extjs.gxt.ui.client.data.ModelData, com.extjs.gxt.ui.client.data.ModelData, java.lang.String)
	 */
	public int compare(com.extjs.gxt.ui.client.store.Store<ModelData> store, ModelData m1, 
		ModelData m2, String property) {

		if (property == null) { // call super's default comparator
			return super.compare(store, m1, m2, property);
		}

		Object v1 = m1.get(property);
		Object v2 = m2.get(property);

		if (v1 == null) {
			return -1;
		}
		if (v2 == null) {
			return 1;
		}

		return v1.toString().toUpperCase().compareTo(v2.toString().toUpperCase());
	};
}
