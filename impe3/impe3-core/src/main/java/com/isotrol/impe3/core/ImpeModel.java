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

package com.isotrol.impe3.core;


import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.Identifiable;
import com.isotrol.impe3.api.ModelInfo;


/**
 * Level 0 model. For engines the id is that of the environment, for portals id the portal id.
 * @author Andres Rodriguez
 */
public interface ImpeModel extends Identifiable {
	/**
	 * Returns the model creation information.
	 * @return The model creation information.
	 */
	ModelInfo getModelInfo();

	/**
	 * Returns the engine mode.
	 * @return The engine mode.
	 */
	EngineMode getMode();

	/**
	 * Returns the file loader.
	 * @return The file loader.
	 */
	FileLoader getFileLoader();

	/**
	 * Returns the devices.
	 * @return The devices.
	 */
	Devices getDevices();
}
