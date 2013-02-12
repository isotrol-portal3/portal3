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


import static com.google.common.base.Preconditions.checkNotNull;

import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.impl.BaseModelImpl;
import com.isotrol.impe3.pms.core.obj.StartedConnectors;


/**
 * Base class for PMS models. Level 2 model plus started connectors.
 * @author Andres Rodriguez.
 */
public class BasePMSModel extends BaseModelImpl {
	/** Started connectors. */
	private final StartedConnectors connectors;

	BasePMSModel(BaseModel model, StartedConnectors connectors) {
		super(model);
		this.connectors = checkNotNull(connectors);
	}

	/**
	 * Copy constructor.
	 * @param model Original model.
	 */
	BasePMSModel(BasePMSModel model) {
		super(model);
		this.connectors = model.connectors;
	}

	/**
	 * Returns the started connectors.
	 * @return The started connectors.
	 */
	final StartedConnectors getConnectors() {
		return connectors;
	}
}
