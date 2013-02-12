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


import static com.google.common.base.Preconditions.checkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.sf.derquinsej.concurrent.ActiveObjectSupport;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.core.modules.StartedModule;


/**
 * Endable objects support.
 * @author Andres Rodriguez.
 */
final class EndableSupport {
	/** Executor to use. */
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

	/** Active object. */
	private final ActiveObjectSupport aos;

	EndableSupport(Runnable onStop) {
		aos = new ActiveObjectSupport(null, null, onStop, EXECUTOR);
		aos.start();
	}

	void beginRequest(Endable endable) {
		aos.begin();
		Endables.add(endable);
	}

	void endRequest() {
		aos.end();
	}

	void stop() {
		aos.stopAsynchronously();
	}
}
