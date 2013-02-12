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

package com.isotrol.impe3.pms.gui.client.widget.design;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.ColumnDropController;


/**
 * @author Manuel Ruiz
 * 
 */
public class Column extends FlowPanel {

	private static final String CSS_COLUMN = "column";

	private ColumnPanel vp;

	/** percent width */
	private int width;

	/** pixels width */
	private int pixelWidth;

	/** column css style */
	private String columnCss;

	private ColumnDropController colDropController;

	/** index where insert the widgets */
	private int widgetIndex;
	
	/** if column is the fill's column */
	private boolean fill = false;

	/**
	 * Constructor
	 */
	public Column() {
		addStyleName(CSS_COLUMN);

		vp = new ColumnPanel();
		vp.addStyleName("column-panel");

		add(vp);
	}

	/**
	 * Constructor
	 * 
	 * @param width the column width (in %)
	 * 
	 */
	public Column(int width) {
		this();
		this.width = width;
		if (width != 0) {
			setTitle(width + "%");
		}
	}

	/**
	 * @return the vp
	 */
	public ColumnPanel getVp() {
		return vp;
	}

	/**
	 * @return the list with the column's frames
	 */
	public List<Frame<?>> getFrames() {
		List<Frame<?>> frames = new LinkedList<Frame<?>>();
		int widgetsCount = vp.getWidgetCount();

		// recorremos los widgets del column panel y añadimos a la lista los que
		// sean instancias de Frame
		for (int i = 0; i < widgetsCount; i++) {
			Widget w = vp.getWidget(i);
			if (w instanceof Frame<?>) {
				frames.add((Frame<?>) w);
			}
		}

		return frames;
	}

	/**
	 * @param colDropController the colDropController to set
	 */
	public void setColDropController(ColumnDropController colDropController) {
		this.colDropController = colDropController;
	}

	/**
	 * Add a div that disables the column's boxes
	 */
	public void block() {
		List<Frame<?>> frames = getFrames();
		Iterator<Frame<?>> itFrame = frames.listIterator();
		while (itFrame.hasNext()) {
			itFrame.next().block();
		}
	}

	/**
	 * Inserts a frame in the specified index
	 * @param frame
	 * @param index
	 */
	public void insertFrame(Frame<?> frame, int index) {
		getVp().insert(frame, index);
	}

	/**
	 * Add a widget in the Column
	 * @param w widget to add
	 */
	public void addWidget(Widget w) {
		vp.add(w);
	}

	/**
	 * Returns the FrameColumn that contains a fill
	 * @return the FrameColumn that contains a fill or null if fill not exits
	 */
	public FrameColumn getFillFrame() {

		FrameColumn fill = null;
		List<Frame<?>> frames = getFrames();

		Iterator<Frame<?>> itFrames = frames.listIterator();
		while (fill == null && itFrames.hasNext()) {
			Frame<?> b = itFrames.next();
			if (b instanceof FrameColumn) {
				FrameColumn frameColumn = (FrameColumn) b;
				if (frameColumn.isFill()) {
					fill = frameColumn;
				} else {
					fill = frameColumn.getFillFrame();
				}
			}
		}

		return fill;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the colDropController
	 */
	public ColumnDropController getColDropController() {
		return colDropController;
	}

	/**
	 * @return the widgetIndex
	 */
	public int getWidgetIndex() {
		return widgetIndex;
	}

	/**
	 * @param widgetIndex the widgetIndex to set
	 */
	public void setWidgetIndex(int widgetIndex) {
		this.widgetIndex = widgetIndex;
	}

	/**
	 * @return the pixelWidth
	 */
	public int getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * @param pixelWidth the pixelWidth to set
	 */
	public void setPixelWidth(int pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	/**
	 * @return the columnCss
	 */
	public String getColumnCss() {
		return columnCss;
	}

	/**
	 * @param columnCss the columnCss to set
	 */
	public void setColumnCss(String columnCss) {
		this.columnCss = columnCss;
	}

	/**
	 * @return the fill
	 */
	public boolean isFill() {
		return fill;
	}

	/**
	 * @param fill the fill to set
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
		vp.setFill(fill);
	}
	
	protected void removeSpacer() {
		vp.removeSpacerLabel();
	}
}
