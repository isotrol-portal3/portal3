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

package com.isotrol.impe3.pms.model;


import static com.google.common.base.Objects.equal;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import com.google.common.base.Function;
import com.google.common.base.Objects;


/**
 * Layout values. Layout values are frames and columns.
 * @author Andres Rodriguez
 */
@Embeddable
public class LayoutValue implements WithPosition {
	public static Function<LayoutValue, UUID> GET_COMPONENT = new Function<LayoutValue, UUID>() {
		public UUID apply(LayoutValue from) {
			return from.getComponent();
		}
	};

	/** Column width. */
	@Column(name = "WIDTH", nullable = true)
	private Integer width;
	/** Frame name. */
	@Column(name = "NAME", nullable = true, length = Lengths.NAME)
	private String name;
	/** Component in page. */
	@Column(name = "CIP", length = Lengths.UUID, nullable = true)
	@Type(type = "impeId")
	private UUID component;
	/** Parent column. */
	@Column(name = "PARENT_ID", nullable = true)
	private Integer parent;
	/** Position within parent. */
	@Column(name = "LYT_POSITION", nullable = true)
	private Integer position;

	/** Default constructor. */
	public LayoutValue() {
	}

	public boolean isColumn() {
		return width != null;
	}

	public boolean isFrame() {
		return width == null;
	}

	public boolean isComponentFrame() {
		return component != null;
	}

	public boolean isColumnsFrame() {
		return width == null && component == null;
	}

	/**
	 * Returns the column width.
	 * @return The column width.
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Sets the column width.
	 * @param width The column width.
	 */
	public void setWidth(Integer width) {
		this.width = width;
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
	public UUID getComponent() {
		return component;
	}

	/**
	 * Sets the component.
	 * @param component The component.
	 */
	public void setComponent(UUID component) {
		this.component = component;
	}
	
	/**
	 * Returns the parent layout value.
	 * @return The parent layout value.
	 */
	public Integer getParent() {
		return parent;
	}

	/**
	 * Sets the parent layout value.
	 * @param parent The parent layout value.
	 */
	public void setParent(Integer parent) {
		this.parent = parent;
	}

	/**
	 * Returns the position within parent.
	 * @return The position within parent.
	 */
	public Integer getPosition() {
		return position == null ? 0 : position;
	}

	/**
	 * Sets the position within parent.
	 * @param parent The position within parent.
	 */
	public void setPosition(Integer position) {
		this.position = (position == null) ? 0 : position;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(width, name, component, position, parent);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LayoutValue) {
			final LayoutValue v = (LayoutValue) obj;
			return equal(width, v.width) && equal(name, v.name) && equal(component, v.component)
				&& equal(position, v.position) && equal(parent, v.parent);
		}
		return false;
	}
}
