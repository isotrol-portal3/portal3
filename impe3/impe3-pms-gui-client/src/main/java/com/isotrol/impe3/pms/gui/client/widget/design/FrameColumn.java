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


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.CssClassNameValidator;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.ColumnDropController;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.DesignDNDController;


/**
 * Frame with columns
 * 
 * @author Manuel Ruiz
 * 
 */
public class FrameColumn extends Frame<FrameDTO> implements Cloneable {

	/**
	 * Number of columns of the frame
	 */
	private int numColums;

	private HorizontalPanel boxPanel;
	/**
	 * The column drop controller.<br/>
	 */
	private ColumnDropController dropController;
	/** Whether the columns have been registered as droppables */
	private boolean registered;
	/** Whether the column is a template's space */
	private boolean fill;

	/** wrapped dto */
	private FrameDTO frame;

	/**
	 * Default constructor
	 * 
	 * @param columns
	 */
	public FrameColumn() {
	}

	/**
	 * Inits the widget.<br/>
	 * @param columns
	 * @param inherited
	 * @return
	 */
	public FrameColumn init(FrameDTO frame) {
		this.frame = frame;
		if (frame.isFill()) {
			this.frame.setInherited(true);
		}

		super.init(this.frame);
		initBoxColumn();

		return this;
	}

	private void initBoxColumn() {
		boxPanel = new HorizontalPanel();
		getVertPanel().add(boxPanel);

		if (frame.isFill()) {
			// fill frame
			Column col = new Column(100);
			col.setFill(true);
			addColumn(col);
			// remove borders from columns when they are inherited
			if (isInherited()) {
				col.addStyleName("inherited-column");
				col.removeStyleName("column");
			}
			numColums = 1;
		} else {
			// column frame
			numColums = frame.getColumns().size();
			createColumns(frame.getColumns());
		}
	}

	/**
	 * Add the columns to the box
	 * @param columns array with the cols to create
	 */
	private final void createColumns(List<ColumnDTO> columns) {

		if (columns != null) {
			for (ColumnDTO column : columns) {
				Column col = new Column(column.getWidth());
				col.setColumnCss(column.getName());
				addColumn(col);
				// remove borders from columns when they are inherited
				if (isInherited()) {
					col.addStyleName("inherited-column");
					col.removeStyleName("column");
					col.removeSpacer();
				}
			}
		}
	}

	/**
	 * Register the drop controllers for the box's columns
	 */
	public void registerColumnsDropController() {
		if (!registered) {
			registered = true;

			List<Column> cols = getColumns();
			Iterator<Column> it = cols.listIterator();
			while (it.hasNext()) {
				Column col = it.next();
				dropController = new ColumnDropController(col.getVp());
				DesignDNDController.getInstance().registerDropController(dropController);
				// almacenamos el drop controller para cuando haya que hacer
				// unregister
				col.setColDropController(dropController);
			}
		}
	}

	/**
	 * Unregister the drop controllers for the frame's columns
	 */
	public void unregisterColumnsDropController() {
		List<Column> cols = getColumns();
		Iterator<Column> it = cols.listIterator();
		while (it.hasNext()) {
			Column col = it.next();
			ColumnDropController dc = col.getColDropController();
			DesignDNDController.getInstance().unregisterDropController(dc);

			// si la columna tiene una frame, tb hay que llamar al método sobre
			// ese frame
			List<Frame<?>> frames = col.getFrames();
			Iterator<Frame<?>> itFrame = frames.listIterator();
			while (itFrame.hasNext()) {
				Frame<?> frame = itFrame.next();
				if (frame instanceof FrameColumn && !((FrameColumn) frame).isFill()) {
					((FrameColumn) frame).unregisterColumnsDropController();
				}
			}
		}
	}

	/**
	 * @return the list with the box's columns
	 */
	public List<Column> getColumns() {
		List<Column> l = new LinkedList<Column>();
		for (int i = 0; i < boxPanel.getWidgetCount(); i++) {
			Widget w = boxPanel.getWidget(i);
			if (w instanceof Column) {
				l.add((Column) w);
			}
		}

		return l;
	}

	/**
	 * Returns the cloned object<br/> (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	protected Object clone() {
		FrameColumn clone;
		// Clone our internal widget
		clone = PmsFactory.getInstance().getFrameColumn().init(this.frame);
		clone.setClosable(this.isClosable());
		return clone;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FrameColumn) {
			FrameColumn c = (FrameColumn) obj;
			return c.getFrame().equals(getFrame());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getFrame() != null) {
			return getFrame().hashCode();
		}
		return super.hashCode();
	}

	/**
	 * Add a column to the box
	 * 
	 * @param col column to add
	 */
	private void addColumn(Column col) {
		boxPanel.add(col);
		boxPanel.setCellWidth(col, col.getWidth() + "%");
	}

	/**
	 * @return the EmptyComponent
	 */
	public Widget getEmptyComponent() {
		Widget widget = null;

		List<Column> cols = getColumns();
		Iterator<Column> it = cols.listIterator();
		while (it.hasNext() && widget == null) {
			Column col = it.next();
			// if the column contains a frame, calls the method over this frame
			List<Frame<?>> boxes = col.getFrames();
			Iterator<Frame<?>> itBox = boxes.listIterator();
			while (itBox.hasNext() && widget == null) {
				Frame<?> box = itBox.next();
				if (box instanceof FrameColumn) {
					widget = ((FrameColumn) box).getEmptyComponent();
				} else if (box instanceof FrameComponent) {
					widget = ((FrameComponent) box).getEmptyComponent();
				}
			}
		}

		return widget;
	}

	/**
	 * Add a div that disables the frame
	 */
	public void block() {

		List<Column> cols = getColumns();

		if (getFillFrame() != null) {
			for (Column col : cols) {
				col.block();
			}
			unregisterColumnsDropController();

		} else if (!fill) {
			// add((new HTML("<div id='capaBloqueo' class='estiloBloqueo'/>")));
			unregisterColumnsDropController();

		} else if (fill) {
			Column col = getColFromFillFrame();
			ColumnDropController dc = col.getColDropController();
			DesignDNDController.getInstance().registerDropController(dc);
		}
	}

	/**
	 * Removes the frame of columns from the main column and unregister the column drop controllers. Add its components
	 * to the palette
	 * @see com.isotrol.impe3.pms.gui.client.widget.design.Frame#removeFrameFromMainColumn()
	 */
	@Override
	public void removeFrameFromMainColumn() {

		// search the components inside to add them to the components palette
		List<Column> columns = getColumns();
		for (Column col : columns) {
			List<Frame<?>> frames = col.getFrames();
			for (Frame<?> frame : frames) {
				frame.removeFrameFromMainColumn();
			}
		}

		if (!isInherited()) {
			// when delete a frame from the palette, a clone won't made
			removeFromParent(false);
			unregisterColumnsDropController();
		}
	}

	/**
	 * @return the fill frame or null if fill not exists in this frame
	 */
	public FrameColumn getFillFrame() {

		FrameColumn res = null;

		List<Column> columns = getColumns();
		Iterator<Column> itCol = columns.listIterator();
		while (res == null && itCol.hasNext()) {
			Column col = itCol.next();
			res = col.getFillFrame();
		}
		return res;
	}

	/**
	 * Returns the single column in the FillFrame
	 * @return the column in the FillFrame
	 */
	public Column getColFromFillFrame() {
		assert isFill();

		return getColumns().get(0);
	}

	/**
	 * Add a button to select a css style name
	 */
	@Override
	public void addCssSelectorButton() {
		super.addCssSelectorButton();

		getCssButton().removeSelectionListener(getCssListener());
		getCssButton().addSelectionListener(new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				final CSSWindow w = new CSSWindow();
				for (int i = 0; i < numColums; i++) {
					final int index = i;
					final TextField<String> cssField = new TextField<String>();
					cssField.setFieldLabel("Css columna " + String.valueOf(i + 1));
					cssField.setValidator(new CssClassNameValidator(getPmsMessages()));
					cssField.setValue(getColumns().get(index).getColumnCss());
					cssField.addListener(Events.OnKeyUp, w.getValidateListener());
					w.addField(cssField);
				}
				w.show();
			}
		});
	}

	@Override
	protected String getInitialToolTipText() {
		String tooltipText = "";

		if (!Util.emptyString(getCssClass())) {
			tooltipText = "CSS del frame: <b>" + getCssClass() + "</b>";
		}

		for (int i = 0; i < numColums; i++) {
			ColumnDTO column = frame.getColumns().get(i);
			if (!Util.emptyString(column.getName())) {
				if (!Util.emptyString(tooltipText)) {
					tooltipText += "<br />";
				}
				tooltipText += "CSS de columna " + String.valueOf(i + 1) + ": <b>" + column.getName() + "</b>";
			}
		}

		if (Util.emptyString(tooltipText)) {
			tooltipText = DEFAULT_CSS_TITLE;
		}

		return tooltipText;
	}

	@Override
	protected List<String> getInitialStyles() {
		List<String> styles = new ArrayList<String>();
		styles.add(getCssClass());
		if (frame.isColumns()) {
			for (ColumnDTO col : frame.getColumns()) {
				styles.add(col.getName());
			}
		}
		return styles;
	}

	@Override
	protected void saveCss(List<String> cssClasses) {
		List<String> columnsCss = cssClasses.subList(1, cssClasses.size());
		for (int i = 0; i < columnsCss.size(); i++) {
			getColumns().get(i).setColumnCss(columnsCss.get(i));
			frame.getColumns().get(i).setName(columnsCss.get(i));
		}
		super.saveCss(cssClasses);
	}

	/**
	 * @return if is a frame fill
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

	/**
	 * @return the frame
	 */
	public FrameDTO getFrame() {
		return frame;
	}

	@Override
	protected String getDraggableStyle() {
		return "draggable-column";
	}
}
