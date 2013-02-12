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

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Manuel Ruiz
 * 
 */
public class PalettePanel extends VerticalPanel {

	// mantenemos una estructura con los elementos contenidos:
	private List<Frame<?>> components = new LinkedList<Frame<?>>();

	/**
	 * Removed widgets that are instances of {@link Frame} are immediately
	 * replaced with a cloned copy of the original.
	 * 
	 * @param w
	 *            the widget to remove
	 */
	@Override
	public boolean remove(Widget w) {
		int index = getWidgetIndex(w);
		if (index != -1 && w instanceof FrameColumn) {
			FrameColumn frameColumn = (FrameColumn) w;
			// lo clonamos si tiene clonable a true
			if (frameColumn.isClonable()) {
				FrameColumn clone = (FrameColumn) frameColumn.clone();
				super.insert(clone, index);
			}
		}

		return super.remove(w);
	}

	/**
	 * añadimos solo si no está ya (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#add(com.google.gwt.user.client.ui.Widget)
	 */
	@Override
	public void add(Widget w) {
		if (w instanceof Frame<?> && !components.contains(w)) {
			Frame<?> b = (Frame<?>) w;
			components.add(b);
			super.add(w);
		}
		if (!(w instanceof Frame<?>)) {
			super.add(w);
		}
	}

	/**
	 * insertamos solo si no está
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#insert(com.google.gwt.user.client.ui.Widget,
	 *      int)
	 */
	@Override
	public void insert(Widget w, int beforeIndex) {
		// insercion chequeando si ya lo tenemos:
		if (w instanceof Frame<?> && !components.contains(w)) {
			super.insert(w, beforeIndex);
		}
	}
}
