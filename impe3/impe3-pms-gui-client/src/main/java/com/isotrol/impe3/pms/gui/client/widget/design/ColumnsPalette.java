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
import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ColumnsFrameDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * Panel with the columns selector
 * @author Manuel Ruiz
 * 
 */
public class ColumnsPalette extends LayoutContainer {

	/** panel width */
	private static final String WIDTH = "96%";
	/** panel css class */
	private static final String PANEL_CSS = "columns-palette";
	private static final String CSS_COLSELECTOR_BUTTON = "colSelector-button";
	private static final int WIDTH_19 = 19;
	private static final int WIDTH_25 = 25;
	private static final int WIDTH_27 = 27;
	private static final int WIDTH_50 = 50;
	private static final int WIDTH_73 = 73;
	private static final int WIDTH_75 = 75;
	private static final int WIDTH_81 = 81;
	private static final int WIDTH_100 = 100;

	private VerticalPanel vp = null;

	/*
	 * Injected deps
	 */
	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * The column configuration selector.<br/>
	 */
	private ColumnSelector columnSelector = null;

	/**
	 * Pms utilities bundle
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Constructor
	 */
	public ColumnsPalette() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependencies injection.
	 */
	public void init() {
		configThis();
		initComponents();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		vp = new PalettePanel();
		vp.setWidth(Constants.HUNDRED_PERCENT);

		// add the default templates
		addTemplates();

		// add the column selector
		final Button colSelectorButton = new Button(pmsMessages.labelCustomize());
		colSelectorButton.addStyleName(CSS_COLSELECTOR_BUTTON);
		columnSelector.init(vp);
		columnSelector.hide();
		colSelectorButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				columnSelector.show();
			}
		});
		vp.add(colSelectorButton);
		vp.add(columnSelector);

		add(vp);
	}

	/**
	 * Config this container properties.<br/>
	 */
	private void configThis() {
		setWidth(WIDTH);
		addStyleName(PANEL_CSS);
	}

	private void addTemplates() {

		if (pmsUtil.getColumnsTemplates() != null && !pmsUtil.getColumnsTemplates().isEmpty()) {
			for(List<Integer> widthList : pmsUtil.getColumnsTemplates()) {
				vp.add(createCols(widthList));
			}
		} else {
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_100})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_50, WIDTH_50})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_25, WIDTH_75})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_19, WIDTH_81})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_73, WIDTH_27})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_75, WIDTH_25})));
			vp.add(createCols(Arrays.asList(new Integer[] {WIDTH_25, WIDTH_25, WIDTH_25, WIDTH_25})));
		}
	}

	private FrameColumn createCols(List<Integer> widthList) {

		ColumnsFrameDTO frame = new ColumnsFrameDTO();
		List<ColumnDTO> columns = new ArrayList<ColumnDTO>();
		for (Integer width : widthList) {
			ColumnDTO col = new ColumnDTO();
			col.setWidth(width);
			columns.add(col);
		}
		frame.setChildren(columns);
		FrameColumn frameCol = PmsFactory.getInstance().getFrameColumn().init(frame);

		return frameCol;
	}

	/**
	 * Injects the PMS specific messages
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the column selector.
	 * @param columnSelector
	 */
	@Inject
	public void setColumnSelector(ColumnSelector columnSelector) {
		this.columnSelector = columnSelector;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
