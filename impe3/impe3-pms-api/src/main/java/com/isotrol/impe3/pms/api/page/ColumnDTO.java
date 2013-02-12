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

package com.isotrol.impe3.pms.api.page;


import java.util.List;


/**
 * Value that represents a frame in a page.
 * @author Andres Rodriguez
 */
public class ColumnDTO extends LayoutElementDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2374205138697956954L;
	
	private String name;
	
	/** Column width. */
	private int width;
	/** Contained frames. */
	private List<FrameDTO> frames;

	/** Default constructor. */
	public ColumnDTO() {
	}

	/**
	 * Returns the column width.
	 * @return The column width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the column width.
	 * @param width The column width.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the contained frames.
	 * @return The contained frames.
	 */
	public List<FrameDTO> getFrames() {
		return frames;
	}

	/**
	 * Sets the contained frames.
	 * @param frames The contained frames.
	 */
	public void setFrames(List<FrameDTO> frames) {
		this.frames = frames;
	}
}
