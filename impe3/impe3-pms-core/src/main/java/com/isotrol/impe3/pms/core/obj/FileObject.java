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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.core.config.ConfigurationDefinition.Item;


/**
 * File domain object.
 * @author Andres Rodriguez
 */
public final class FileObject {
	/** File Id. */
	private final FileId id;
	/** File Configuration Item. */
	private final Item item;

	/**
	 * Constructor.
	 * @param id File Id.
	 * @param item File Configuration Item.
	 */
	FileObject(FileId id, Item item) {
		this.id = checkNotNull(id);
		this.item = checkNotNull(item);
	}

	public FileId getId() {
		return id;
	}

	public Item getItem() {
		return item;
	}
}
