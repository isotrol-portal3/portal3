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

package com.isotrol.impe3.pms.gui.client.widget.design.dnd;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;

/**
 * Shared drag handler which display events as they are received by the various
 * drag controllers.
 */
public final class DesignDragHandler extends DragHandlerAdapter {

	DesignDragHandler() {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.DragHandlerAdapter#onDragEnd(com.allen_sauer.gwt.dnd.client.DragEndEvent)
	 */
	@Override
	public void onDragEnd(DragEndEvent event) {
		super.onDragEnd(event);
		DesignDNDController.getInstance().clearSelection();

		// hacemos visible el widget ya que lo hemos ocultado en el dragmove
		event.getContext().draggable.setVisible(true);
	}
}
