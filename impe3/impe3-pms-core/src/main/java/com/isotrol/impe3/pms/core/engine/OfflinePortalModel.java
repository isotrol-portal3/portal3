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

package com.isotrol.impe3.pms.core.engine;


import com.isotrol.impe3.pms.api.PMSException;


/**
 * Offline PMS Portal Model implementation.
 * @author Andres Rodriguez.
 */
public final class OfflinePortalModel extends AbstractPortalModel implements Endable {
	/** Portal model version. */
	private final int version;
	/** Endable support. */
	private final EndableSupport es;

	OfflinePortalModel(PortalLoadingSupport pls) throws PMSException {
		super(pls);
		this.version = pls.getVersion();
		this.es = new EndableSupport(new Runnable() {
			public void run() {
				stopComponents();
			}
		});
	}

	/**
	 * Returns the model version.
	 * @return The model version.
	 */
	final int getVersion() {
		return version;
	}

	public void beginRequest() {
		es.beginRequest(this);
	}

	public void endRequest() {
		es.endRequest();
	}

	public synchronized void stop() {
		es.stop();
	}

}
