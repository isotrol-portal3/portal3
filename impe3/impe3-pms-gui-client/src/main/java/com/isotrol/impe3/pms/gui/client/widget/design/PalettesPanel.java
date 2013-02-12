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

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * Left Panel with Components Palette and Columns
 * @author Manuel Ruiz
 * 
 */
public class PalettesPanel extends ContentPanel {

	private TabItem componentsTabItem;
	private ComponentsPanel componentsPanel;
	
	/** page model to design */
	private LayoutDTO layoutDto;

	/*
	 * Injected deps
	 */
	/**
	 * The columns palette.<br/>
	 */
	private ColumnsPalette columnsPalette = null;
	
	/**
	 * Pms messages
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Default constructor
	 */
	public PalettesPanel() {}
	
	/**
	 * <br/>
	 * @param layout
	 */
	public void init(LayoutDTO layout) {
		this.layoutDto = layout;
		
		initComponent();
		configComponent();
	}

	private void configComponent() {

		setLayout(new FitLayout());
	}

	private void initComponent() {

		addTabPanel();
	}

	/**
	 * Add a tab panel with 2 tab items (the components list and the column selector)
	 */
	private final void addTabPanel() {

		TabPanel tabPanel = new TabPanel();
		tabPanel.setMinTabWidth(70);
		tabPanel.setBorderStyle(false);
		tabPanel.setBodyBorder(false);
		tabPanel.setTabPosition(TabPosition.BOTTOM);
		tabPanel.setTabScroll(true);

		// components selector tab item
		componentsTabItem = new TabItem();
		componentsTabItem.setText(pmsMessages.headerComponentsManagement());
		componentsTabItem.setScrollMode(Scroll.AUTO);
		addComponentsToolBar();
		componentsPanel = new ComponentsPanel(layoutDto);
		componentsTabItem.add(componentsPanel);
		tabPanel.add(componentsTabItem);

		// column selector tab item
		TabItem columnsItem = new TabItem();
		columnsItem.setText(pmsMessages.titleColumns());
		columnsItem.setScrollMode(Scroll.AUTO);
		columnsPalette.init();
		columnsItem.add(columnsPalette);
		tabPanel.add(columnsItem);

		add(tabPanel);
	}

	private void addComponentsToolBar() {

		ToolBar toolBar = new ToolBar();
		toolBar.addStyleName("components-palette-toolbar");

		final TriggerField<String> componentsFilter = new TriggerField<String>();
		componentsFilter.setTriggerStyle("x-form-clear-trigger");

		componentsFilter.addListener(Events.KeyUp, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				filterComponentsInPalette(componentsFilter.getValue());
			}
		});

		// show all components
		componentsFilter.addListener(Events.TriggerClick, new Listener<FieldEvent>() {

			public void handleEvent(FieldEvent be) {
				componentsFilter.setValue(null);
				showComponentsInPalette();
			}

		});

		toolBar.add(componentsFilter);

		componentsTabItem.add(toolBar);
	}

	private void filterComponentsInPalette(String f) {

		List<FrameComponent> list = componentsPanel.getComponents();
		if (f != null) {
			String filter = f.toLowerCase();
			for (FrameComponent component : list) {
				String name = component.getComponent().getDto().getName().toLowerCase();
				if (!name.contains(filter)) {
					component.hide();
				} else {
					component.show();
				}
			}
		} else {
			showComponentsInPalette();
		}
	}

	private void showComponentsInPalette() {

		List<FrameComponent> list = componentsPanel.getComponents();
		for (FrameComponent component : list) {
			component.show();
		}
	}

	/**
	 * @return the layoutDto
	 */
	public LayoutDTO getLayoutDto() {
		return layoutDto;
	}

	/**
	 * @return the componentsPanel
	 */
	public ComponentsPanel getComponentsPanel() {
		return componentsPanel;
	}
	
	/**
	 * @param componentsPanel the componentsPanel to set
	 */
	public void setComponentsPanel(ComponentsPanel componentsPanel) {
		this.componentsPanel = componentsPanel;
		componentsTabItem.add(componentsPanel);
		componentsTabItem.layout();
	}
	
	/**
	 * Injects the Columns Palette component
	 * @param columnsPalette
	 */
	@Inject
	public void setColumnsPalette(ColumnsPalette columnsPalette) {
		this.columnsPalette = columnsPalette;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the componentsTabItem
	 */
	public TabItem getComponentsTabItem() {
		return componentsTabItem;
	}
}
