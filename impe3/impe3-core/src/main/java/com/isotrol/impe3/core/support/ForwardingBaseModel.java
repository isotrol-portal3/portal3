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

package com.isotrol.impe3.core.support;


import com.google.common.collect.ForwardingObject;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.core.BaseModel;


/**
 * Forwarding base model.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingBaseModel extends ForwardingObject implements BaseModel {
	/** Default constructor. */
	public ForwardingBaseModel() {
	}

	protected abstract BaseModel delegate();

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getMode()
	 */
	public EngineMode getMode() {
		return delegate().getMode();
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getContentTypes()
	 */
	public ContentTypes getContentTypes() {
		return delegate().getContentTypes();
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getCategories()
	 */
	public Categories getCategories() {
		return delegate().getCategories();
	}

	/**
	 * @see com.isotrol.impe3.core.BaseModel#getFileLoader()
	 */
	public FileLoader getFileLoader() {
		return delegate().getFileLoader();
	}

}
