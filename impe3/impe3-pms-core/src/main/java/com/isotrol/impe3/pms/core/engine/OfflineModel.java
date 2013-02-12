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


import java.util.UUID;

import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.core.BaseModel;


/**
 * Offline PMS Engine Model implementation.
 * @author Andres Rodriguez.
 */
public abstract class OfflineModel /*extends Endable*/ implements BaseModel {
	private final UUID id;
	private final int version;
	private final FileLoader fileLoader;

	public OfflineModel(final UUID id, final int version, FileLoader fileLoader) {
		this.id = id;
		this.version = version;
		this.fileLoader = fileLoader;
	}

	public final UUID getId() {
		return id;
	}

	public final int getVersion() {
		return version;
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getMode()
	 */
	public final EngineMode getMode() {
		return EngineMode.OFFLINE;
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getFileLoader()
	 */
	public final FileLoader getFileLoader() {
		return fileLoader;
	}
}
