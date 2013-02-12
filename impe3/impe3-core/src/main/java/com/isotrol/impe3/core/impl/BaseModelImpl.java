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

import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.ImpeIAModel;


/**
 * Basic implementation of the level 2 model.
 * @author Andres Rodriguez
 */
public class BaseModelImpl extends ImpeIAModelImpl implements BaseModel {
	/** Device router. */
	private final DeviceRouter deviceRouter;
	/** Portal router. */
	private final LocaleRouter localeRouter;

	/**
	 * Constructor.
	 * @param id Engine or portal id.
	 * @param version Model version.
	 * @param model Level 1 model.
	 * @param deviceRouter Device router.
	 * @param localeRouter Portal router.
	 */
	public BaseModelImpl(UUID id, UUID version, ImpeIAModel model, DeviceRouter deviceRouter, LocaleRouter localeRouter) {
		super(id, version, model);
		this.deviceRouter = checkNotNull(deviceRouter);
		this.localeRouter = checkNotNull(localeRouter);
	}

	/**
	 * Constructor.
	 * @param model Level 1 model.
	 * @param deviceRouter Device router.
	 * @param localeRouter Portal router.
	 */
	public BaseModelImpl(ImpeIAModel model, DeviceRouter deviceRouter, LocaleRouter localeRouter) {
		super(model);
		this.deviceRouter = checkNotNull(deviceRouter);
		this.localeRouter = checkNotNull(localeRouter);
	}

	/**
	 * Copy constructor.
	 * @param model Original model.
	 */
	protected BaseModelImpl(BaseModel model) {
		super(model);
		this.deviceRouter = model.getDeviceRouter();
		this.localeRouter = model.getLocaleRouter();
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getDeviceRouter()
	 */
	public final DeviceRouter getDeviceRouter() {
		return deviceRouter;
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getLocaleRouter()
	 */
	public final LocaleRouter getLocaleRouter() {
		return localeRouter;
	}

}
