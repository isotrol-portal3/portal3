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


import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ColumnsFrameDTO;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.FillFrameDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.ColumnDropController;
import com.isotrol.impe3.pms.gui.client.widget.design.dnd.DesignDNDController;


/**
 * Design Panel
 * @author Manuel Ruiz
 * 
 */
public class DesignPanel extends VerticalPanel {

	private static final String CSSPANEL = "design-zone-panel";
	private static final String CSSFRAMEDROP = "box-ondrop";

	private static Integer designPanelWidth;
	private static final int DEFAULT_WIDTH = 1024;

	private Column mainColumn;
	private LayoutDTO layoutDto;

	/**
	 * Constructor
	 * @param layoutDto
	 */
	public DesignPanel(LayoutDTO layoutDto) {
		this.layoutDto = layoutDto;

		setBorders(true);
		addStyleName(CSSPANEL);
		setScrollMode(Scroll.AUTO);
		// center the main column
		setHorizontalAlign(HorizontalAlignment.CENTER);

		// add the default column
		addMainColumn();
	}

	private void addMainColumn() {

		int width = DEFAULT_WIDTH;
		designPanelWidth = layoutDto.getWidth();
		if (designPanelWidth != null) {
			width = designPanelWidth;
		}

		mainColumn = new Column(width);
		mainColumn.setWidth(String.valueOf(width));
		mainColumn.setPixelWidth(width);
		mainColumn.setTitle(width + " px");
		mainColumn.addStyleName("maincolumn");

		add(mainColumn);

		// registramos como drop la columna principal
		ColumnDropController dropController = new ColumnDropController(mainColumn.getVp());
		DesignDNDController.getInstance().registerDropController(dropController);
		mainColumn.setColDropController(dropController);

		createLayout();

		FrameColumn fillFrame = mainColumn.getFillFrame();
		if (fillFrame != null) {
			addBlock();
		}
	}

	private final void addBlock() {
		mainColumn.block();
		ColumnDropController mainColumnDropController = mainColumn.getColDropController();
		DesignDNDController.getInstance().unregisterDropController(mainColumnDropController);
	}

	/**
	 * Adds the page's frames to the layout
	 */
	private void createLayout() {

		List<FrameDTO> frames = layoutDto.getFrames();
		if (frames != null && !frames.isEmpty()) {
			addFrameToColumn(mainColumn, frames);
		}
	}

	/**
	 * Removes the main column from the design panel and build it with the passed {@link LayoutDTO}
	 * @param pageLayout the layout to build the design
	 */
	protected void reset(LayoutDTO pageLayout) {
		setLayoutDto(pageLayout);
		remove(mainColumn);
		DesignDNDController.getInstance().unregisterDropControllers();
		addMainColumn();
		layout();
	}

	protected void addFrameToColumn(Column col, List<FrameDTO> frames) {

		for (FrameDTO frame : frames) {
			if (frame.isComponent()) {
				addComponentToColumn(col, frame);
			} else if (frame.isColumns()) {
				addColumnsToColumn(col, frame);
			} else if (frame.isFill()) {
				addFillToColumn(col, frame);
			}
		}
	}

	/**
	 * Creates a visual FillFrame and iserts it in the current column
	 * @param col
	 * @param frame
	 */
	private void addFillToColumn(Column col, FrameDTO frame) {

		FrameColumn fillColumn = PmsFactory.getInstance().getFrameColumn().init((FillFrameDTO) frame);
		fillColumn.setFill(true);
		fillColumn.addStyleName(CSSFRAMEDROP);
		
		col.insertFrame(fillColumn, col.getWidgetIndex());
		col.setWidgetIndex(col.getWidgetIndex() + 1);

		List<Column> columns = fillColumn.getColumns();
		calculateAbsoluteWidth(col, columns);
		Column c = columns.get(0);
		c.getVp().addStyleName("fill-frame");
		c.addStyleName("column");
		c.removeStyleName("inherited-column");

		addFrameToColumn(c, frame.getFrames());
	}

	private void addColumnsToColumn(Column col, FrameDTO frame) {

		List<ColumnDTO> columnsDto = frame.getColumns();
		int[] widths = new int[columnsDto.size()];
		String[] names = new String[columnsDto.size()];
		for (int i = 0; i < columnsDto.size(); i++) {
			widths[i] = columnsDto.get(i).getWidth();
			names[i] = columnsDto.get(i).getName();
		}

		FrameColumn fra = PmsFactory.getInstance().getFrameColumn().init((ColumnsFrameDTO) frame);
		if (!frame.isInherited()) {
			fra.setClosable(true);
			fra.setCssClass(frame.getName());
			fra.addCssSelectorButton();
		}
		fra.addStyleName(CSSFRAMEDROP);

		col.insertFrame(fra, col.getWidgetIndex());
		col.setWidgetIndex(col.getWidgetIndex() + 1);

		List<Column> columns = fra.getColumns();
		calculateAbsoluteWidth(col, columns);
		for (int i = 0; i < columns.size(); i++) {
			addFrameToColumn(columns.get(i), columnsDto.get(i).getFrames());
		}
	}

	private void addComponentToColumn(Column col, FrameDTO frame) {

		LayoutItemDTO component = Design.getInstance().getLayoutItemsMap().get(frame.getComponent());
		FrameComponent fComp = PmsFactory.getInstance().getFrameComponent().init(component, (ComponentFrameDTO) frame);
		if (!frame.isInherited()) {
			fComp.setClosable(true);
			fComp.setCssClass(frame.getName());
			fComp.addCssSelectorButton();

			if (component.isSpace()) {
				fComp.getComponent().addStyleName(Design.getInstance().getPmsStyles().spaceInLayout());
			}
		}
		fComp.addStyleName(CSSFRAMEDROP);

		// add the markup html
		PortalComponent comp = fComp.getComponent();
		int columnWidth = col.getPixelWidth();
		if (columnWidth != 0 && component.getMarkup(columnWidth) != null) {
			comp.setHTML(comp.getDto().getMarkup(columnWidth));
			fComp.setComponent(comp);
			fComp.layout();
		}

		col.insertFrame(fComp, col.getWidgetIndex());
		col.setWidgetIndex(col.getWidgetIndex() + 1);
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

	public Column getMainColum() {
		return (Column) getWidget(0);
	}

	/**
	 * @param layoutDto the layoutDto to set
	 */
	public void setLayoutDto(LayoutDTO layoutDto) {
		this.layoutDto = layoutDto;
	}
}
