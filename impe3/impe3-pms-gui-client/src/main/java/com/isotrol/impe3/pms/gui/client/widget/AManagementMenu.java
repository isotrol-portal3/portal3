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
package com.isotrol.impe3.pms.gui.client.widget;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.common.util.Settings;

/**
 * Base class for all left menu management items.
 * 
 * @author Andrei Cojocaru
 *
 */
public abstract class AManagementMenu extends ContentPanel {

	/*
	 * Injected deps.
	 */
	/**
	 * Tab items manager.<br/>
	 */
	private PmsTabItemManager tabItemManager = null;
	
	/**
	 * PMS specific styles.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * GuiCommon styles.
	 */
	private GuiCommonStyles guiCommonStyles = null;
	
	/**
	 * Common messages.<br/>
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * Settings bundle. 
	 */
	private Settings settings = null;
	
	/**
	 * PMS specific message bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * PMS utilities 
	 */
	private PmsUtil pmsUtil = null;
	
	/**
	 * @return the menuListView
	 */
	public abstract ListView<MenuItemModelData> getMenuListView();
	
	/*
	 * Injectors
	 */
	/**
	 * Sets the PMS specific styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}
	
	/**
	 * Sets the tab items manager.<br/>
	 * {@link ICenterWidget} is used in order to use the singleton instance managed by <i>guice</i>.
	 * @param tabItemManager
	 */
	@Inject
	public void setTabItemManager(PmsTabItemManager tabItemManager) {
		this.tabItemManager = (PmsTabItemManager) tabItemManager;
	}
	
	/**
	 * Injects the PMS specific message bundle.<br/>
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the settings constants bundle.
	 * @param settings the settings bundle.
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	/**
	 * @return the settings instance
	 */
	protected final Settings getSettings() {
		return settings;
	}
	
	/**
	 * <br/>
	 * @return the tab items manager.
	 */
	protected PmsTabItemManager getTabItemManager() {
		return tabItemManager;
	}

	/**
	 * <br/>
	 * @return the PMS specific message bundle.
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}
	
	/**
	 * @param guiCommonStyles
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}
	
	/**
	 * @return GuiCommon styles bundle.
	 */
	protected final GuiCommonStyles getGuiCommonStyles() {
		return guiCommonStyles;
	}

	/**
	 * @return the messages
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @return the pmsUtil
	 */
	public PmsUtil getPmsUtil() {
		return pmsUtil;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
