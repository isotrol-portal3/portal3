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


/**
 * Abstract PMS Engine Model implementation.
 * @author Andres Rodriguez.
 */
public class AbstractEngineModel extends BaseEngineModel implements Endable {
	/** Endable support. */
	private final EndableSupport es;

	/**
	 * Constructor.
	 * @param model Base model.
	 */
	AbstractEngineModel(BaseEngineModel model) {
		super(model);
		this.es = new EndableSupport(new Runnable() {
			public void run() {
				shutdownChildren();
				getConnectors().stop();
			}
		});
	}

	public final void beginRequest() {
		es.beginRequest(this);
	}

	public final void endRequest() {
		es.endRequest();
	}

	public final synchronized void stop() {
		stopChildren();
		es.stop();
	}

	/**
	 * Synchronous part of the engine stop. The model is synchronized when this method is called.
	 */
	void stopChildren() {
	}

	/**
	 * Aynchronous part of the engine stop.
	 */
	void shutdownChildren() {
	}

}
