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


/**
 * Level 1 context. Includes level 0 and global IA.
 * @author Andres Rodriguez
 */
public class Context1 extends Context0 {
	/** Information architecture. */
	private final IA ia;

	/**
	 * Constructor
	 * @param ctx Source level 0 context.
	 * @param ia Information architecture.
	 */
	public Context1(Context0 ctx, IA ia) {
		super(ctx);
		this.ia = checkNotNull(ia, "The information architecture must be provided");
	}

	/**
	 * Copy constructor
	 * @param ctx Source context.
	 */
	Context1(Context1 ctx) {
		super(ctx);
		this.ia = ctx.ia;
	}

	/**
	 * Returns the information architecture.
	 * @return The information architecture (never {@code null}).
	 */
	public final IA getIA() {
		return ia;
	}

	/**
	 * Returns the content types.
	 * @return The content types (never {@code null}).
	 */
	public final ContentTypesObject getContentTypes() {
		return ia.getContentTypes();
	}

	/**
	 * Returns the categories.
	 * @return The categories (never {@code null}).
	 */
	public final CategoriesObject getCategories() {
		return ia.getCategories();
	}
}
