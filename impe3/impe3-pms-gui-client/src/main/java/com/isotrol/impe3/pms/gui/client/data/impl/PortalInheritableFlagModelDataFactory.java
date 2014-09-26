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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.data.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.store.ListStore;
import com.isotrol.impe3.pms.api.portal.PortalInheritableFlag;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * Factory for {@link PortalInheritableFlagModelData}
 * @author Andres Rodriguez
 */
public final class PortalInheritableFlagModelDataFactory {
	/** Display names map. */
	private final Map<PortalInheritableFlag, String> map;

	/**
	 * Constructor.
	 * @param messages Localized messages.
	 */
	public PortalInheritableFlagModelDataFactory(PmsMessages messages) {
		final Map<PortalInheritableFlag, String> m = new HashMap<PortalInheritableFlag, String>();
		m.put(PortalInheritableFlag.ON, messages.valueYes());
		m.put(PortalInheritableFlag.OFF, messages.valueNo());
		m.put(PortalInheritableFlag.INHERIT, messages.valueInherit());
		this.map = Collections.unmodifiableMap(m);
	}
	
	public PortalInheritableFlagModelData newModel(PortalInheritableFlag value) {
		return new PortalInheritableFlagModelData(map, value);
	}

	public PortalInheritableFlagModelData newModel() {
		return newModel(PortalInheritableFlag.INHERIT);
	}
	
	public ListStore<PortalInheritableFlagModelData> newStore() {
		final ListStore<PortalInheritableFlagModelData> store = new ListStore<PortalInheritableFlagModelData>();
		store.add(newModel(PortalInheritableFlag.INHERIT));
		store.add(newModel(PortalInheritableFlag.ON));
		store.add(newModel(PortalInheritableFlag.OFF));
		return store;
	}

}
