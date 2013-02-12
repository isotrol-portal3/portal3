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
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;


import java.util.Arrays;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.pms.gui.client.data.impl.PageDeviceModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PageSelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;


/**
 * Helper class for Toolbar creation in Page Mangement widgets.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public class ToolbarSupport {

	/** page management panel tool bar */
	private ToolBar toolBar;

	/** page management panel bottom tool bar */
	private ToolBar bottomToolBar;

	/**
	 * "Edit Page" toolbar item<br/>
	 */
	private Button miEditPage;
	/**
	 * "Design Page" toolbar item.<br/>
	 */
	private Button miDesign;
	/**
	 * "Delete Page" toolbar item.<br/>
	 */
	private Button miDeletePage;
	/**
	 * "Preview Page" toolbar item.<br/>
	 */
	private Button miPreviewPage;
	/**
	 * "New Page" toolbar item.<br/>
	 */
	private Button miNewPage;

	/**
	 * "Export" toolbar item.
	 */
	private Button bExport;

	/**
	 * "Import" toolbar item.
	 */
	private Button bImport;

	/**
	 * Filter for Pages store.<br/>
	 */
	private CustomizableStoreFilter<ModelData> miFilter;

	/**
	 * "Refresh" tool button.<br/>
	 */
	private ToolButton tbRefresh;

	/**
	 * Page devices combo box
	 */
	private ComboBox<PageDeviceModelData> cbDevices = null;

	/*
	 * Injected deps.
	 */
	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	/**
	 * Generic messages service<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Generic styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Default constructor. Creates a new toolbar and configures it with common items: new, edit, design, preview,
	 * delete, and a refresh to the right.<br/>
	 * 
	 * @see #initToolBar()
	 * @param pmsMessages
	 * @param messages
	 * @param buttonsSupport
	 */
	@Inject
	public ToolbarSupport(PmsMessages pmsMessages, GuiCommonMessages messages, Buttons buttonsSupport,
		GuiCommonStyles styles, PmsStyles pmsStyles) {
		super();
		this.pmsMessages = pmsMessages;
		this.messages = messages;
		this.buttonsSupport = buttonsSupport;
		this.styles = styles;
		this.pmsStyles = pmsStyles;

		toolBar = new ToolBar();
		bottomToolBar = new ToolBar();

		initToolBar();
		initBottomToolBar();
	}

	/**
	 * Adds common items to toolbar.<br/>
	 */
	private void initToolBar() {
		// new page item
		miNewPage = buttonsSupport.addAddButton(toolBar, null, null);
		buttonsSupport.addSeparator(toolBar);

		// edit page item
		miEditPage = buttonsSupport.addEditButton(toolBar, null);
		miEditPage.disable();
		buttonsSupport.addSeparator(toolBar);

		// page design item
		miDesign = buttonsSupport.addGenericButton(pmsMessages.labelDesign(), pmsStyles.iconDesign(), toolBar, null);
		miDesign.disable();
		buttonsSupport.addSeparator(toolBar);

		// page preview item
		miPreviewPage = buttonsSupport.addGenericButton(pmsMessages.labelPreview(), styles.iPreview(), toolBar, null);
		miPreviewPage.disable();
		buttonsSupport.addSeparator(toolBar);

		// delete page item
		miDeletePage = buttonsSupport.addDeleteButton(toolBar, null, null);
		miDeletePage.disable();

		toolBar.add(new FillToolItem());
		
		addPagesFilter();

		tbRefresh = buttonsSupport.addRefreshButton(toolBar, null);
	}

	/**
	 * Adds common items to bottom toolbar.<br/>
	 */
	private void initBottomToolBar() {

		addDevicesComboBox();

		// export button
		bExport = buttonsSupport.addGenericButton(pmsMessages.labelExport(), pmsMessages.ttExportPages(), pmsStyles.exportIcon(), bottomToolBar,
			null);
		buttonsSupport.addSeparator(bottomToolBar);
		// import button
		bImport = buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), bottomToolBar,
			null);
	}

	/**
	 * Creates and adds a miFilter to the toolbar. Does <b>not</b> bind it to the grid store.<br/>
	 */
	public void addPagesFilter() {
		miFilter = new CustomizableStoreFilter<ModelData>(Arrays.asList(new String[] {PageSelModelData.PROPERTY_NAME}));
		miFilter.setHideLabel(false);
		miFilter.setFieldLabel(messages.labelFilter());

		toolBar.add(miFilter);
	}

	/**
	 * Enables or disables the toolbar common items which need a model context (i.e. a selected page), according to
	 * passed param value.<br/> The involved items are: "Edit Page", "Delete Page", "Design" and "Preview"
	 * 
	 * @param enabled if <code>true</code> enables, otherwise disables.
	 * @param inherited if <code>true</code> disables delete button.
	 * @param designed if <code>true</code> design button can be enabled
	 */
	public final void enableContextCommonToolItems(boolean enabled, boolean inherited, boolean designed) {
		if (enabled && inherited) {
			miDeletePage.setEnabled(false);
		} else {
			miDeletePage.setEnabled(enabled);
		}

		miDesign.setEnabled(enabled && designed);
		miEditPage.setEnabled(enabled);
		miPreviewPage.setEnabled(enabled);
	}

	/**
	 * Binds filter to passed store, if filter not null<br/>
	 * @param store
	 */
	public void maybeBindFilter(Store<ModelData> store) {
		if (miFilter != null) {
			miFilter.bind(store);
		}
	}

	private void addDevicesComboBox() {
		IconButton filterBtn = new IconButton(pmsStyles.menuIconDevices());
		filterBtn.setWidth(20);

		cbDevices = new ComboBox<PageDeviceModelData>();
		cbDevices.setTriggerAction(TriggerAction.ALL);
		cbDevices.setEditable(false);
		cbDevices.setDisplayField(PageDeviceModelData.PROPERTY_DISPLAY_NAME);
		ListStore<PageDeviceModelData> stPageDevices = new ListStore<PageDeviceModelData>();
		cbDevices.setStore(stPageDevices);

		bottomToolBar.add(filterBtn);
		bottomToolBar.add(cbDevices);
	}

	/**
	 * <br/>
	 * @return the "Edit Page" toolbar item.
	 */
	public final Button getMiEditPage() {
		return miEditPage;
	}

	/**
	 * <br/>
	 * @return the "Design" toolbar item.
	 */
	public final Button getMiDesign() {
		return miDesign;
	}

	/**
	 * <br/>
	 * @return the "Delete" toolbar item.
	 */
	public final Button getMiDeletePage() {
		return miDeletePage;
	}

	/**
	 * <br/>
	 * @return the "Preview" toolbar item.
	 */
	public final Button getMiPreviewPage() {
		return miPreviewPage;
	}

	/**
	 * <br/>
	 * @return the "New Page" toolbar item.
	 */
	public final Button getMiNewPage() {
		return miNewPage;
	}

	/**
	 * <br/>
	 * @return the filter
	 */
	public final CustomizableStoreFilter<ModelData> getMiFilter() {
		return miFilter;
	}

	/**
	 * @return the tbRefresh
	 */
	public ToolButton getTbRefresh() {
		return tbRefresh;
	}

	/**
	 * @return the toolBar
	 */
	public ToolBar getToolBar() {
		return toolBar;
	}

	/**
	 * Injects the PMS specific styles
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the bImport
	 */
	public final Button getbImport() {
		return bImport;
	}

	/**
	 * @param bImport the bImport to set
	 */
	public final void setbImport(Button bImport) {
		this.bImport = bImport;
	}

	/**
	 * @return the bExport
	 */
	public final Button getbExport() {
		return bExport;
	}

	/**
	 * @return the cbDevices
	 */
	public final ComboBox<PageDeviceModelData> getCbDevices() {
		return cbDevices;
	}

	/**
	 * @return the bottomToolBar
	 */
	public final ToolBar getBottomToolBar() {
		return bottomToolBar;
	}
}
