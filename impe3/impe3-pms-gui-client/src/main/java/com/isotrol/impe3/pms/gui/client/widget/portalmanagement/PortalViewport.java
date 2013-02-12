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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.ICenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;


/**
 * Viewport used in portal management.
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalViewport extends PmsViewport {

	/** portal template dto */
	private PortalNameDTO portalName = null;

	/** portal left panel */
	private PortalLeftMenu portalLeftMenu = null;

	/** portal center widget */
	private PortalTabItemManager portalCenterWidget = null;

	/**
	 * Constructor
	 */
	public PortalViewport() {
		super();
		// add a listener to show the default panel
		addListener(Events.Show, new Listener<BaseEvent>() {

			public void handleEvent(BaseEvent be) {
				portalLeftMenu.showInitWidget();
			}
		});
	}

	@Override
	protected boolean isNorthVisible() {
		return true;
	}

	@Override
	protected ALeftPanel getLeftPanel() {
		return portalLeftMenu;
	}

	@Override
	protected ICenterWidget getCenterWidget() {
		return portalCenterWidget;
	}

	/**
	 * @return the portalTemplate
	 */
	public PortalNameDTO getPortalName() {
		return portalName;
	}

	/**
	 * @param portalName the portalName to set
	 */
	public void setPortalName(PortalNameDTO portalName) {
		this.portalName = portalName;
		portalLeftMenu.setPortalName(portalName);
	}

	/**
	 * @param portalLeftMenu the portalLeftMenu to set
	 */
	@Inject
	public void setLeftMenu(PortalLeftMenu leftMenu) {
		this.portalLeftMenu = leftMenu;
	}

	/**
	 * @param portalCenterWidget the portalCenterWidget to set
	 */
	@Inject
	public void setPortalCenterWidget(PortalTabItemManager centerWidget) {
		this.portalCenterWidget = centerWidget;
	}

}
