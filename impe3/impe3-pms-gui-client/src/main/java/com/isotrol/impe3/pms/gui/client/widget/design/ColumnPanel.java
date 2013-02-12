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


import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * 
 * @author Manuel Ruiz
 * 
 */
public class ColumnPanel extends VerticalPanel {

	private static final String EMPTY_COMPONENT_CSS = "box-empty-component";
	private static final String CSS_COLUMN_INDEXED_PANEL_SPACER = "column-IndexedPanel-spacer";

	/** if column is the fill's column */
	private boolean fill = false;

	/** spacer to prevent vertical panel from collapsing to zero when last widget is removed */
	private Label spacerLabel;

	public ColumnPanel() {
		// prevent vertical panel from collapsing to zero when last widget is
		// removed
		spacerLabel = new Label("");
		spacerLabel.setStylePrimaryName(CSS_COLUMN_INDEXED_PANEL_SPACER);
		super.add(spacerLabel);
	}

	/**
	 * Si se añade un frame column, registramos sus columnas como drops
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#add(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void add(Widget w) {
		if (w instanceof FrameColumn) {
			FrameColumn b = (FrameColumn) w;
			b.registerColumnsDropController();

		} else if (w instanceof FrameComponent) {
			FrameComponent b = (FrameComponent) w;
			if (b.getEmptyComponent() != null) {
				b.addStyleName(EMPTY_COMPONENT_CSS);
			}
		}
		super.insert(w, getWidgetCount() - 1);
	}

	/**
	 * if adds a frame, register the columns as drops
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#insert(com.google.gwt.user.client.ui.Widget, int)
	 */
	@Override
	public void insert(Widget w, int beforeIndex) {
		if (w instanceof FrameColumn) {
			FrameColumn b = (FrameColumn) w;
			b.registerColumnsDropController();

		} else if (w instanceof FrameComponent) {
			FrameComponent b = (FrameComponent) w;
			if (b.getEmptyComponent() != null) {
				b.addStyleName(EMPTY_COMPONENT_CSS);
			}
		}
		if (beforeIndex == getWidgetCount() && getWidgetCount() > 0) {
			beforeIndex--;
		}
		super.insert(w, beforeIndex);

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
	}

	protected void removeSpacerLabel() {
		remove(spacerLabel);
	}

}
