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

package com.isotrol.impe3.core.impl;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.ModelInfo;
import com.isotrol.impe3.core.ImpeModel;


/**
 * Basic implementation of the level 0 model.
 * @author Andres Rodriguez
 */
public class ImpeModelImpl extends AbstractIdentifiable implements ImpeModel {
	/** Model creation information. */
	private final ModelInfo info;
	/** Engine mode. */
	private final EngineMode mode;
	/** File loader. */
	private final FileLoader fileLoader;
	/** Devices. */
	private final Devices devices;

	/**
	 * Constructor.
	 * @param id Engine or portal id.
	 * @param version Model version.
	 * @param mode Engine mode.
	 * @param fileLoader File loader.
	 * @param devices Devices.
	 */
	public ImpeModelImpl(UUID id, UUID version, EngineMode mode, FileLoader fileLoader, Devices devices) {
		super(id);
		this.info = ModelInfo.create(version);
		this.mode = checkNotNull(mode);
		this.fileLoader = checkNotNull(fileLoader);
		this.devices = checkNotNull(devices);
	}

	/**
	 * Copy constructor.
	 * @param id Engine or portal id.
	 * @param info Model info.
	 * @param model Original model.
	 */
	private ImpeModelImpl(UUID id, ModelInfo info, ImpeModel model) {
		super(id);
		this.info = checkNotNull(info);
		this.mode = model.getMode();
		this.fileLoader = model.getFileLoader();
		this.devices = model.getDevices();
	}

	/**
	 * Copy constructor with id and version change.
	 * @param id Engine or portal id.
	 * @param version Model version.
	 * @param model Original model.
	 */
	protected ImpeModelImpl(UUID id, UUID version, ImpeModel model) {
		this(id, ModelInfo.create(version), model);
	}

	/**
	 * Copy constructor.
	 * @param model Original model.
	 */
	protected ImpeModelImpl(ImpeModel model) {
		this(model.getId(), model.getModelInfo(), model);
	}

	/**
	 * @see com.isotrol.impe3.core.ImpeModel#getModelInfo()
	 */
	public final ModelInfo getModelInfo() {
		return info;
	}

	/**
	 * @see com.isotrol.impe3.core.ImpeModel#getMode()
	 */
	public final EngineMode getMode() {
		return mode;
	}

	/**
	 * @see com.isotrol.impe3.core.ImpeModel#getFileLoader()
	 */
	public FileLoader getFileLoader() {
		return fileLoader;
	}

	/**
	 * @see com.isotrol.impe3.core.ImpeModel#getDevices()
	 */
	public Devices getDevices() {
		return devices;
	}

}
