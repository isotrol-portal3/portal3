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

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.UnableToLoadFileException;
import com.isotrol.impe3.core.support.AbstractFileLoader;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.core.FileManager;


/**
 * FileLoader implementation.
 * @author Andres Rodriguez.
 */
@Component
public class FileLoaderImpl extends AbstractFileLoader {
	private final FileManager fileManager;

	/**
	 * Constructor.
	 * @param fileManager File manager.
	 */
	@Autowired
	public FileLoaderImpl(final FileManager fileManager) {
		this.fileManager = fileManager;
	}

	/**
	 * @see com.isotrol.impe3.core.support.AbstractFileLoader#doLoad(java.util.UUID)
	 */
	@Override
	protected FileData doLoad(UUID id) {
		checkNotNull(id);
		try {
			return fileManager.getFile(id);
		} catch (EntityNotFoundException nfe) {
			throw new IllegalArgumentException();
		} catch (Exception e) {
			throw new UnableToLoadFileException(e);
		}
	}
}
