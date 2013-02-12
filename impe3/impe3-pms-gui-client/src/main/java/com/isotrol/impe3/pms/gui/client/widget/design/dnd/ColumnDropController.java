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


import java.util.List;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.gui.client.widget.design.Column;
import com.isotrol.impe3.pms.gui.client.widget.design.ColumnPanel;
import com.isotrol.impe3.pms.gui.client.widget.design.Design;
import com.isotrol.impe3.pms.gui.client.widget.design.Frame;
import com.isotrol.impe3.pms.gui.client.widget.design.FrameColumn;
import com.isotrol.impe3.pms.gui.client.widget.design.FrameComponent;
import com.isotrol.impe3.pms.gui.client.widget.design.PortalComponent;


/**
 * IndexedDropController that disallows dropping after the last child, which is assumed to be dummy spacer widget
 * preventing parent collapse.
 * 
 * @author Manuel Ruiz
 * 
 */
public class ColumnDropController extends VerticalPanelDropController {

	private final static String CSS_FILL_COLUMN_HOVER = "fill-hover";

	/** the drop column */
	private ColumnPanel dropColumn = null;
	
	/** 
	 * @param dropTarget
	 */
	public ColumnDropController(ColumnPanel dropTarget) {
		super(dropTarget);
		this.dropColumn = dropTarget;
	}

	/**
	 * When dropping, add a close button to the box. Change box's html if widget is a EmptyComponent. Add the component
	 * to the palette of dependences.
	 * 
	 * @see com.allen_sauer.gwt.dnd.client.drop.AbstractIndexedDropController#onDrop
	 * (com.allen_sauer.gwt.dnd.client.DragContext)
	 */
	@Override
	public void onDrop(DragContext context) {
		super.onDrop(context);

		Widget w = context.draggable;
		Frame<?> b = null;

		if (w instanceof Frame<?>) {
			b = (Frame<?>) w;
			b.addStyleName("box-ondrop");
			if (!b.isClosable()) {
				b.setClosable(true);
			}

			// add the css selector button only if it hasn't one
			if (!b.isCssSelector()) {
				b.addCssSelectorButton();
			}
		}

		if (w instanceof FrameColumn) {
			FrameColumn fCol = (FrameColumn) w;
			List<Column> cols = fCol.getColumns();
			Column colParent = (Column) fCol.getParent().getParent();
			calculateAbsoluteWidth(colParent, cols);
		} else if (w instanceof FrameComponent) {
			FrameComponent fComp = (FrameComponent) w;
			// add extra style if is fill
			if (fComp.isSpace()) {
				fComp.getComponent().addStyleName(Design.getInstance().getPmsStyles().spaceInLayout());
			}
		}

		if (b != null && b.getVertPanel().getWidget() instanceof PortalComponent) {
			// change component html on drop
			Widget c = b.getVertPanel().getWidget();
			PortalComponent component = (PortalComponent) c;
			Column colParent = (Column) b.getParent().getParent();
			int columnWidth = colParent.getPixelWidth();
			if (columnWidth != 0 && component.getDto().getMarkup(columnWidth) != null) {
				component.setHTML(component.getDto().getMarkup(columnWidth));
			}
		}
		
		Design.getInstance().setDirty(true);
	}

	/**
	 * Calculates the pixels width for each column
	 * @param colParent
	 * @param cols
	 */
	private void calculateAbsoluteWidth(Column colParent, List<Column> cols) {

		if (colParent != null) {
			for (Column col : cols) {
				int width = (col.getWidth() * colParent.getPixelWidth()) / 100;
				col.setPixelWidth(width);
			}
		}
	}

	@Override
	public void onEnter(DragContext context) {
		super.onEnter(context);
		dropColumn.addStyleName(CSS_FILL_COLUMN_HOVER);
	}

	@Override
	public void onLeave(DragContext context) {
		super.onLeave(context);
		dropColumn.removeStyleName(CSS_FILL_COLUMN_HOVER);
	}
}
