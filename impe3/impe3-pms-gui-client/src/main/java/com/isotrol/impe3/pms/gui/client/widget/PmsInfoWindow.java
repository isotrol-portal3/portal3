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

package com.isotrol.impe3.pms.gui.client.widget;


import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;


/**
 * Window with information about Port@l 3.0
 * 
 * @author Manuel Ruiz
 * 
 */
public class PmsInfoWindow extends Window {

	/** Pms Styles bundle */
	private PmsStyles pmsStyles = null;
	
	/** Pms Settings bundle */ 
	private PmsSettings pmsSettings = null;
	
	/** Pms Messages bundle */
	private PmsMessages pmsMessages = null;

	/**
	 * Default constructor
	 */
	public PmsInfoWindow() {
	}

	@Override
	protected void beforeRender() {
		configThis();
		initComponent();
	}

	private void initComponent() {
		LayoutContainer container = new LayoutContainer(new RowLayout());
		container.addStyleName(pmsStyles.aboutPanel());
		container.setHeight(Constants.HUNDRED_PERCENT);

		container.add(new Image("img/recorte_portal_blanco.jpg"));

		Html versionInfo = new Html("Build " + pmsSettings.impe3Version());
		versionInfo.addStyleName(pmsStyles.impeBuildInfo());
		container.add(versionInfo);

		Image im = new Image("img/Logo_Isotrolx100px.jpg");
		Html link = new Html("<a href='http://www.isotrol.com' target='_blank'>" + im.toString() + "</a>");
		container.add(link);

		Text copyIsotrol = new Text(pmsMessages.msgCopyrightIsotrol());
		copyIsotrol.addStyleName(pmsStyles.copyrightText());
		container.add(copyIsotrol);
		
		add(container);
	}

	/**
	 * Configures a typical window with a standard height, width and sets the closable flag.
	 */
	private void configThis() {
		setModal(true);
		setWidth(340);
		setShadow(true);
		setFrame(true);
		setAutoHeight(true);
		setClosable(true);
		setHeadingText(pmsMessages.headerAboutPortalWindow());
		getHeader().setIcon(IconHelper.createPath("img/portal30.gif"));
		setLayout(new FitLayout());
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param pmsSettings the pmsSettings to set
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
}
