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
package com.isotrol.impe3.pms.gui.client.ioc;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.isotrol.impe3.pms.gui.client.widget.GeneralTabItemManager;
import com.isotrol.impe3.pms.gui.client.widget.LeftMenu;
import com.isotrol.impe3.pms.gui.client.widget.NavigationPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsMainPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsTabItemManager;
import com.isotrol.impe3.pms.gui.client.widget.PmsTopPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;
import com.isotrol.impe3.pms.gui.client.widget.comment.CommentsCenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.comment.CommentsLeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.comment.CommentsViewport;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.ServicesManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.help.HelpMenu;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.InformationArchitectureMenu;
import com.isotrol.impe3.pms.gui.client.widget.nr.NrCenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.nr.NrLeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.nr.NrViewport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalLeftMenu;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalTabItemManager;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalViewport;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.UsersLeftMenu;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.UsersViewport;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.SystemManagementMenu;
import com.isotrol.impe3.pms.gui.client.widget.top.UserInfoPanel;

/**
 * @author Andrei Cojocaru
 *
 */
public class PmsViewportGinModule extends AbstractGinModule {

	/* (non-Javadoc)
	 * @see com.google.gwt.inject.client.AbstractGinModule#configure()
	 */
	/**
	 * Configures the viewport and its deps: the left menu and the center widget.<br/>
	 */
	@Override
	protected void configure() {
		bind(PmsViewport.class).in(Singleton.class);
		bind(NrViewport.class).in(Singleton.class);
		bind(UsersViewport.class).in(Singleton.class);
		bind(PortalViewport.class).in(Singleton.class);
		bind(PmsMainPanel.class).in(Singleton.class);
		bind(CommentsViewport.class).in(Singleton.class);

		// top panel
		bind(PmsTopPanel.class).in(Singleton.class);
		bind(UserInfoPanel.class).in(Singleton.class);
		bind(NavigationPanel.class).in(Singleton.class);
		
		// center panel
		bind(PmsTabItemManager.class).in(Singleton.class);
		bind(NrCenterWidget.class).in(Singleton.class);
		bind(GeneralTabItemManager.class).in(Singleton.class);
		bind(PortalTabItemManager.class).in(Singleton.class);
		bind(CommentsCenterWidget.class).in(Singleton.class);
		
		// //////// left menu:
		bind(LeftMenu.class).in(Singleton.class);
		bind(NrLeftPanel.class).in(Singleton.class);
		bind(UsersLeftMenu.class).in(Singleton.class);
		bind(PortalLeftMenu.class).in(Singleton.class);
		bind(CommentsLeftPanel.class).in(Singleton.class);

		bind(SystemManagementMenu.class).in(Singleton.class);
		bind(InformationArchitectureMenu.class).in(Singleton.class);
		bind(PortalManagementMenu.class).in(Singleton.class);
		bind(ServicesManagementMenu.class).in(Singleton.class);
		bind(HelpMenu.class).in(Singleton.class);
	}
}
