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
 * DTO for page selection.
 * @author Andres Rodriguez
 */
public final class LayoutDTO extends PageLoc {
	/** Serial UID. */
	private static final long serialVersionUID = 9053936257312726016L;
	/** Style sheets (if any). */
	private List<StylesheetDTO> stylesheets;
	/** IE6 Additional Stylesheets (if any). */
	private List<StylesheetDTO> ie6Stylesheets;
	/** IE7 Additional Stylesheets (if any). */
	private List<StylesheetDTO> ie7Stylesheets;
	/** IE8 Additional Stylesheets (if any). */
	private List<StylesheetDTO> ie8Stylesheets;
	/** Layout width. */
	private Integer width;
	/** Layout items. */
	private List<LayoutItemDTO> items;
	/** Frames. */
	private List<FrameDTO> frames;

	/** Default constructor. */
	public LayoutDTO() {
	}

	/**
	 * Returns the stylesheets.
	 * @return The stylesheets.
	 */
	public List<StylesheetDTO> getStylesheets() {
		return stylesheets;
	}

	/**
	 * Sets the stylesheets.
	 * @param stylesheets The stylesheets.
	 */
	public void setStylesheets(List<StylesheetDTO> stylesheets) {
		this.stylesheets = stylesheets;
	}

	/**
	 * Returns the IE 6 additional stylesheets.
	 * @return The IE 6 additional stylesheets.
	 */
	public List<StylesheetDTO> getIE6Stylesheets() {
		return ie6Stylesheets;
	}

	/**
	 * Sets the IE 6 additional stylesheets.
	 * @param stylesheets The IE 6 additional stylesheets.
	 */
	public void setIE6Stylesheets(List<StylesheetDTO> ie6Stylesheets) {
		this.ie6Stylesheets = ie6Stylesheets;
	}

	/**
	 * Returns the IE 7 additional stylesheets.
	 * @return The IE 7 additional stylesheets.
	 */
	public List<StylesheetDTO> getIE7Stylesheets() {
		return ie7Stylesheets;
	}

	/**
	 * Sets the IE 7 additional stylesheets.
	 * @param stylesheets The IE 7 additional stylesheets.
	 */
	public void setIE7Stylesheets(List<StylesheetDTO> ie7Stylesheets) {
		this.ie7Stylesheets = ie7Stylesheets;
	}

	/**
	 * Returns the IE 8 additional stylesheets.
	 * @return The IE 8 additional stylesheets.
	 */
	public List<StylesheetDTO> getIE8Stylesheets() {
		return ie8Stylesheets;
	}

	/**
	 * Sets the IE 8 additional stylesheets.
	 * @param stylesheets The IE 8 additional stylesheets.
	 */
	public void setIE8Stylesheets(List<StylesheetDTO> ie8Stylesheets) {
		this.ie8Stylesheets = ie8Stylesheets;
	}

	/**
	 * Returns the layout width.
	 * @return The layout width.
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * Sets the layout width.
	 * @param items The layout width.
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * Returns the layout items.
	 * @return The layout items.
	 */
	public List<LayoutItemDTO> getItems() {
		return items;
	}

	/**
	 * Sets the layout items.
	 * @param items The layout items.
	 */
	public void setItems(List<LayoutItemDTO> items) {
		this.items = items;
	}

	/**
	 * Returns the frames.
	 * @return The frames.
	 */
	public List<FrameDTO> getFrames() {
		return frames;
	}

	/**
	 * Sets the frames.
	 * @param frames The frames.
	 */
	public void setFrames(List<FrameDTO> frames) {
		this.frames = frames;
	}

	public PageLoc toPageLoc() {
		return new PageLoc(this);
	}
}
