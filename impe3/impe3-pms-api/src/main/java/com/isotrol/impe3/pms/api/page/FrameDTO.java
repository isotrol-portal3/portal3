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
public abstract class FrameDTO extends LayoutElementDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 7203741709646597645L;

	/** Frame name. */
	private String name;

	/** Default constructor. */
	public FrameDTO() {
	}

	/**
	 * Returns whether the frame is a component frame.
	 * @return True if frame is a component frame.
	 */
	public boolean isComponent() {
		return false;
	}

	/**
	 * Returns whether the frame is a columns frame.
	 * @return True if frame is a columns frame.
	 */
	public boolean isColumns() {
		return false;
	}

	/**
	 * Returns whether the frame is a fill frame.
	 * @return True if frame is a fill frame.
	 */
	public boolean isFill() {
		return false;
	}
	
	/**
	 * Returns the frame name.
	 * @return The frame name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the frame name.
	 * @param name The frame name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the component.
	 * @return The component.
	 */
	public String getComponent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the columns.
	 * @return The columns.
	 */
	public List<ColumnDTO> getColumns() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Returns the frames.
	 * @return The frames.
	 */
	public List<FrameDTO> getFrames() {
		throw new UnsupportedOperationException();
	}
	
}
