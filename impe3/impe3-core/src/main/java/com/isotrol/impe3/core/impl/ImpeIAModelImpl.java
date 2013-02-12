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

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.core.ImpeIAModel;
import com.isotrol.impe3.core.ImpeModel;


/**
 * Basic implementation of the level 1 model.
 * @author Andres Rodriguez
 */
public class ImpeIAModelImpl extends ImpeModelImpl implements ImpeIAModel {
	/** Content types. */
	private final ContentTypes contentTypes;
	/** Categories. */
	private final Categories categories;

	/**
	 * Constructor.
	 * @param model Level 0 model.
	 * @param contentTypes Content types.
	 * @param categories Categories.
	 */
	public ImpeIAModelImpl(ImpeModel model, ContentTypes contentTypes, Categories categories) {
		super(model);
		this.contentTypes = checkNotNull(contentTypes);
		this.categories = checkNotNull(categories);
	}

	/**
	 * Copy constructor with id change.
	 * @param id Engine or portal id.
	 * @param version Model version.
	 * @param model Original model.
	 */
	protected ImpeIAModelImpl(UUID id, UUID version, ImpeIAModel model) {
		super(id, version, model);
		this.contentTypes = model.getContentTypes();
		this.categories = model.getCategories();
	}

	/**
	 * Copy constructor.
	 * @param model Original model.
	 */
	protected ImpeIAModelImpl(ImpeIAModel model) {
		super(model);
		this.contentTypes = model.getContentTypes();
		this.categories = model.getCategories();
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getContentTypes()
	 */
	public final ContentTypes getContentTypes() {
		return contentTypes;
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getCategories()
	 */
	public final Categories getCategories() {
		return categories;
	}
}
