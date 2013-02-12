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


import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Left panel elements shared between all PMS apps
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class ALeftPanel extends ContentPanel implements ILeftPanel, IInitializableWidget {

	/**
	 * Settings bundle.
	 */
	private Settings settings = null;

	/**
	 * Reference to general messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	// private PmsStyles pmsStyles = null;

	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS messages bundle
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS styles bundle
	 */
	private PmsStyles pmsStyles = null;

	/** Utilities bundle */
	private Util util = null;

	public ALeftPanel() {
		setLayout(new RowLayout());
		setHeaderVisible(true);
		setBodyBorder(true);
		// TODO Change this - put scroll in inner content panels
		setScrollMode(Scroll.AUTOY);
	}

	/**
	 * Adds and configs the logo panel and the menu panel<br/>
	 * @see com.isotrol.impe3.pms.gui.common.widget.ISimpleInitializableWidget#init()
	 */
	public Widget init() {
		addLogoPanel();
		addMenuPanel();

		return this;
	}

	/**
	 * Creates, configs and adds the Company logo panel.<br/>
	 */
	private void addLogoPanel() {
		LayoutContainer pLogo = new LayoutContainer(new CenterLayout());
		pLogo.addStyleName("logo-portal-blue");
		pLogo.addListener(Events.OnClick, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				PmsFactory.getInstance().getPmsInfoWindow().show();
			}
		});
		pLogo.setToolTip("Acerca de Port@l");
		pLogo.setStyleAttribute("cursor", "pointer");

		add(pLogo);
	}

	/**
	 * Creates, configs and adds the menu panel.<br/>
	 */
	private void addMenuPanel() {
		LayoutContainer pMenu = new LayoutContainer(new AccordionLayout());
		add(pMenu);

		configMenuPanel(pMenu);
	}

	protected abstract void configMenuPanel(LayoutContainer container);

	/**
	 * Content Panel where adding the menu items
	 * @author Manuel Ruiz
	 * 
	 */
	protected class MenuItemLevelOnePanel extends ContentPanel {
		public MenuItemLevelOnePanel() {
			super();
			setAutoHeight(true);
			setBodyBorder(false);
			getHeader().addStyleName(getStyles().noSideBorders());
		}
	}

	/**
	 * Setter implemented for dependency injection
	 * 
	 * @param messages generic message bundle.
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	// /**
	// * Injects the generic styles bundle.
	// * @param styles
	// */
	// @Inject
	// public void setPmsStyles(PmsStyles pmsStyles) {
	// this.pmsStyles = pmsStyles;
	// }

	/**
	 * <br/>
	 * @return the generic messages bundle.
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
	}

	// /**
	// * <br/>
	// * @return the generic styles bundle.
	// */
	// protected PmsStyles getPmsStyles() {
	// return pmsStyles;
	// }

	/**
	 * Injects the settings bundle
	 * @param settings
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @return the generic settings bundle.
	 */
	protected final Settings getSettings() {
		return settings;
	}

	/**
	 * @return the styles
	 */
	public GuiCommonStyles getStyles() {
		return styles;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the pmsMessages
	 */
	public PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @return the util
	 */
	public Util getUtil() {
		return util;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}
}
