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


import java.util.Arrays;
import java.util.Iterator;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;


/**
 * {@link StoreFilterField} configurable with multiple properties for lookup.<br/> Case letters and language-specific
 * characters are ignored.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <M>
 * 
 */
public class CustomizableStoreFilter<M extends ModelData> extends StoreFilterField<M> {

	/**
	 * The properties collection which the filter may be configured with.<br/>
	 */
	private Iterable<String> properties = null;

	/**
	 * Constructor provided with properties collection.<br/>
	 * @param properties
	 */
	public CustomizableStoreFilter(Iterable<String> properties) {
		super();
		this.properties = properties;
	}

	/**
	 * Constructor provided with an array of properties.
	 * @param properties
	 */
	public CustomizableStoreFilter(String[] properties) {
		this.properties = Arrays.asList(properties);
	}

	/**
	 * Returns <code>true</code> if search string is found in any of the properties collection passed in the
	 * constructor.<br/> Before the search is performed, all characters are cleaned (no 'exotic' characters) and
	 * upper-cased. (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.StoreFilterField #doSelect(com.extjs.gxt.ui.client.store.Store,
	 * com.extjs.gxt.ui.client.data.ModelData, com.extjs.gxt.ui.client.data.ModelData, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	protected boolean doSelect(Store<M> store, M parent, M record, String property, String filter) {

		String upperFilter = Util.cleanString(filter.toUpperCase());

		boolean found = false;
		Iterator<String> it = properties.iterator();
		while (!found && it.hasNext()) {
			String p = it.next(); // current property
			Object v = record.get(p); // current property value
			found = v != null && Util.cleanString(v.toString().toUpperCase()).contains(upperFilter);
		}

		return found;
	}
}
