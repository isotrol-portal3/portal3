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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.common.collect.ImmutableList;


/**
 * A column object in a layout.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class Column {
	/** Min width of a column relative to its parent. */
	public static final int MIN_WIDTH = 0;
	/** Max width of a column relative to its parent. */
	public static final int MAX_WIDTH = 100;

	private final String name;
	/** The width of the column. */
	private final int width;
	/** The list of frames contained in this column. */
	private final ImmutableList<Frame> frames;

	public Column(final String name, final int width, final Iterable<Frame> frames) {
		checkArgument(width >= MIN_WIDTH && width <= MAX_WIDTH);
		this.width = width;
		this.name = name;
		this.frames = ImmutableList.copyOf(checkNotNull(frames));
	}

	/**
	 * Returns the width of the column. The width is in per cent units of the widht of its parent column.
	 * @return The width of the column.
	 */
	public int getWidth() {
		return width;
	}

	public String getName() {
		return name;
	}
	
	/**
	 * Returns the frames contained in this column.
	 * @return The list of frames contained in this column.
	 */
	public List<Frame> getFrames() {
		return frames;
	}
}
