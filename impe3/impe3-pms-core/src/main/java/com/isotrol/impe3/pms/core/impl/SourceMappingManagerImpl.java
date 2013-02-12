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

package com.isotrol.impe3.pms.core.impl;


import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.core.SourceMappingManager;
import com.isotrol.impe3.pms.core.obj.SourceMappingsObject;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;


/**
 * Source mapping component implementation.
 * @author Andres Rodriguez.
 */
@Component
public class SourceMappingManagerImpl extends AbstractStateLoaderComponent<SourceMappingsObject> implements
	SourceMappingManager {

	/** Loader. */
	private final SourceMappingsLoader loader;

	/**
	 * Default constructor.
	 */
	public SourceMappingManagerImpl() {
		this.loader = new SourceMappingsLoader();
	}

	@Override
	SourceMappingsLoader getLoader() {
		return loader;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getMappingVersion();
	}

	private static class SourceMappingsLoader implements Loader<SourceMappingsObject> {
		public SourceMappingsObject load(EnvironmentEntity e) {
			return SourceMappingsObject.of(e.getSourceMappings());
		}

		public SourceMappingsObject load(EditionEntity e) {
			return load(e.getEnvironment());
		}

		@Override
		public String toString() {
			return "Source mappings";
		}
	}
}
