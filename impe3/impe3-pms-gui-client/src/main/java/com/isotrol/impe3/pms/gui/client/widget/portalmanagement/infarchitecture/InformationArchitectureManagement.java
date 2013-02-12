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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.infarchitecture;

import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.portal.PortalIATemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.CategoryTreeStore;
import com.isotrol.impe3.pms.gui.client.util.Labels;

/**
 * @author Andrei Cojocaru
 *
 */
public class InformationArchitectureManagement extends LayoutContainer {

	/*
	 * PmsConstants.
	 */
	/**
	 * "Name" column width.<br/>
	 */
	private static final int COLUMN_NAME_WIDTH = 200;

//	private static final String CONTENT_TYPES_PANEL_HEADER = "Tipos de Contenido";

	/**
	 * Height for {@link #pPossibleCat} and {@link #wPortals}.<br/>
	 */
	private static final int POPUPS_HEIGHT = 300;

	/*
	 * Widgets.
	 */
	private TreePanel<CategorySelModelData> tPossibleCat = null;
	
	/**
	 * Categories popup window. User can choose the root category with this.<br/>
	 */
	private ContentPanel pPossibleCat = null;

	/**
	 * Associated categories Grid<br/>
	 */
	private Grid<CategorySelModelData> gCat = null;
	
	/**
	 * Associated categories panel.<br/>
	 */
	private ContentPanel pCat = null;
	
	/*
	 * Stores.
	 */
	/**
	 * Possible categories Tree Store.<br/>
	 */
	private CategoryTreeStore tsPossibleCat;
	/**
	 * Associated categories List Store.<br/>
	 */
	private ListStore<CategorySelModelData> sCat;

	/**
	 * Upper panel. Contains the associated categories (left) and possible categories (right).<br/>
	 */
	private ContentPanel pUpper = null;
	
	/*
	 * Toolbar items:
	 */
	/**
	 * Toolbar item "Add"<br/>
	 */
	private Button ttiAdd = null;
	/**
	 * Toolbar item "Remove"<br/>
	 */
	private Button ttiRem = null;

	/**
	 * Current Portal Information Architecture template.<br/>
	 */
	private PortalIATemplateDTO portalIaTemplate = null;
	
	/*
	 * Injected deps
	 */
	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * PMS specific styles service <br/>
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * Constructor provided with bound DTO.<br/>
	 * @param data
	 */
	public InformationArchitectureManagement() {}
	
	/**
	 * Inits the component. Must be explicitly called after dependencies injection.
	 * @param data
	 */
	public void init(PortalIATemplateDTO data) {
		this.portalIaTemplate = data;
		initComponent();
	}

	/**
	 * Inits this container inner components.
	 */
	private void initComponent() {
		// upper panel contains 
		pUpper  = new ContentPanel();
		pUpper.setLayout(new ColumnLayout());
		pUpper.setHeaderVisible(false);
		pUpper.addStyleName(pmsStyles.upperPanel());	// margin-bottom 20px
		
		// categories:
		addAssociatedCategoriesPanel();
		addAssociatedCategoriesToolbar();
		
		// content types:
		add(new ContentTypesPanel(portalIaTemplate));
	}

	private void addAssociatedCategoriesPanel() {
		pCat = new ContentPanel();
		pCat.setHeading(pmsMessages.headerCategoriesManagement());
		
		ColumnConfig ccTitle = new ColumnConfig(
				CategorySelModelData.PROPERTY_NAME,
				pmsMessages.labelName(),
				COLUMN_NAME_WIDTH);
		ColumnModel colModel = new ColumnModel(Arrays.asList(new ColumnConfig [] {ccTitle}));
		
		sCat = new ListStore<CategorySelModelData>();
		
		gCat = new Grid<CategorySelModelData>(sCat, colModel);
		
		gCat.addListener(Events.RowClick, new Listener<GridEvent<CategorySelModelData>>() {
			public void handleEvent(GridEvent<CategorySelModelData> be) {
				List<CategorySelModelData> lSelected = gCat.getSelectionModel().getSelectedItems();
				boolean enabled = false;
				if(!lSelected.isEmpty()) {
					enabled = true;
				}
				ttiRem.setEnabled(enabled);
			}
		});
		
		pCat.add(gCat);
		pUpper.add(pCat, new ColumnData(Constants.FIFTY_PERCENT_FLOAT));
	}
	
	/**
	 * Adds the toolbar for the associated categories
	 */
	private void addAssociatedCategoriesToolbar() {
		ToolBar toolbar = new ToolBar();
		
		ttiAdd = new Button(messages.labelAdd());
		ttiAdd.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent e) {
				showPossibleCategoriesPanel();
			}
		});
		toolbar.add(ttiAdd);
		
		toolbar.add(new SeparatorToolItem());
		
		ttiRem = new Button(messages.labelDelete());
		ttiRem.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				List<CategorySelModelData> lSelected = gCat.getSelectionModel().getSelectedItems();
				for(CategorySelModelData csModelData : lSelected) {
					sCat.remove(csModelData);
				}
			}
		});
		ttiRem.disable();
		toolbar.add(ttiRem);
		
		pCat.setTopComponent(toolbar);
	}
	
	/**
	 * Shows the panel with available Categories.
	 */
	private void showPossibleCategoriesPanel() {
		if(pPossibleCat == null) {
			createPossibleCategoriesPanel();
		}
		pPossibleCat.show();
	}

	private void createPossibleCategoriesPanel() {
		pPossibleCat = new ContentPanel();
		pPossibleCat.addStyleName(pmsStyles.detailPanelRight());
		pPossibleCat.hide();
		pPossibleCat.setHeading(pmsMessages.headerCategorySelector());
		pPossibleCat.setScrollMode(Scroll.AUTO);
		pPossibleCat.setHeight(POPUPS_HEIGHT);

		tsPossibleCat = new CategoryTreeStore(/*getBoundData().getCategoryTree()*/);
		
		tPossibleCat = new TreePanel<CategorySelModelData>(tsPossibleCat);
		
		tPossibleCat.setDisplayProperty(CategorySelModelData.PROPERTY_NAME);
		
		tPossibleCat.addListener(Events.SelectionChange, new Listener<TreePanelEvent<CategorySelModelData>>() {
			public void handleEvent(TreePanelEvent<CategorySelModelData> be) {
				List<CategorySelModelData> rootItems = tPossibleCat.getStore().getRootItems();
				CategorySelModelData item = tPossibleCat.getSelectionModel().getSelectedItem();
				
				if (item != null && !rootItems.isEmpty() && item != rootItems.get(0)) {
					// add to grid if not already existing:
					if (sCat.findModel(CategorySelModelData.PROPERTY_ID, item.getDTO().getId()) == null) {
						sCat.add(item);
					}
				}
				
				pPossibleCat.hide();
				tPossibleCat.getSelectionModel().deselectAll();
			}
		});		
		
		tPossibleCat.getStyle().setLeafIcon(IconHelper.createStyle(Labels.TREE_ITEM_DEFAULT_STYLE));
		
		pPossibleCat.addListener(Events.Show, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				tPossibleCat.expandAll();
			}
		});
		
		pPossibleCat.add(tPossibleCat);
		pUpper.add(pPossibleCat,new ColumnData(Constants.FIFTY_PERCENT_FLOAT));
	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the PMS styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
	
	

}
