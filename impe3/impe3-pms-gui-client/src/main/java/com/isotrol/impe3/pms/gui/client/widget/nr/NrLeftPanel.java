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
package com.isotrol.impe3.pms.gui.client.widget.nr;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.NrMessages;
import com.isotrol.impe3.pms.gui.client.i18n.NrStyles;
import com.isotrol.impe3.pms.gui.client.ioc.IPmsFactory;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;


/**
 * Nodes Repository left panel
 * @author Andrei Cojocaru
 * 
 */
public class NrLeftPanel extends ALeftPanel {

	/**
	 * supports {@link IInitializableWidget#isInitialized()}<br/>
	 */
	private boolean initialized = false;

	/**
	 * "Info" item ID.<br/>
	 */
	private static final String ID_NR_SUMMARY = "nr-info";

	/**
	 * "Details" item ID.<br/>
	 */
	private static final String ID_NR_QUERY = "nr-details";

	/**
	 * The Nodes Repository ID passed through the URL.<br/>
	 */
	private String repositoryId = null;

	/*
	 * Injected deps
	 */
	/**
	 * NR specific messages bundle.<br/>
	 */
	private NrMessages nrMessages = null;

	/**
	 * NR specific styles bundle.<br/>
	 */
	private NrStyles nrStyles = null;

	/**
	 * The center widget.<br/>
	 */
	private NrCenterWidget centerWidget = null;

	private ListView<MenuItemModelData> menuList = null;

	private MenuItemModelData iSummary = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.ALeftPanel
	 * #configMenuPanel(com.extjs.gxt.ui.client.widget.LayoutContainer)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void configMenuPanel(LayoutContainer container) {
		this.repositoryId = PmsFactory.getInstance().getNRViewport().getRepositoryId();

		ContentPanel panel = new ContentPanel();
		panel.setAutoHeight(true);
		panel.setHeading(nrMessages.menuHeaderNodesRepository());
		panel.setIconStyle(nrStyles.menuIconNrHeader());
		panel.setBodyBorder(false);
		panel.getHeader().addStyleName(getStyles().noSideBorders());
		container.add(panel);

		menuList = new ListView<MenuItemModelData>();
		menuList.setBorders(false);
		menuList.setStore(new ListStore<MenuItemModelData>());
		menuList.setSimpleTemplate(getSettings().tplListView());
		menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: Node Repo Info
		iSummary = new MenuItemModelData(nrMessages.menuItem2RepositorySummary(), nrStyles.menuIconNrInfo(),
			ID_NR_SUMMARY);
		lModels.add(iSummary);

		// item: Node Repo Details
		MenuItemModelData iQuery = new MenuItemModelData(nrMessages.menuItem2RepositoryQuery(), nrStyles
			.menuIconNrDetail(), ID_NR_QUERY);
		lModels.add(iQuery);

		menuList.getStore().add(lModels);

		menuList.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<MenuItemModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<MenuItemModelData> se) {
				if (se.getSelectedItem() != null) {
					String itemId = se.getSelectedItem().get(MenuItemModelData.PROPERTY_ID);
					ARepositoryWidget repositoryWidget = null;
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_NR_SUMMARY)) {
						repositoryWidget = factory.getRepositorySummaryWidget();
					} else /* if (itemId.equals(ID_NR_QUERY)) */{
						repositoryWidget = factory.getRepositoryQueryWidget();
					}
					// maybe init the component
					if (!repositoryWidget.isInitialized()) {
						repositoryWidget.setRepositoryId(repositoryId);
						repositoryWidget.init();
					}
					centerWidget.show((Component) repositoryWidget);
				}
			}
		});

		panel.add(menuList);

		// show summary widget:
		// called when viewport show event is fired
		// showInitWidget();

	}

	protected void showInitWidget() {
		if (menuList != null) {
			if(!menuList.getSelectionModel().getSelectedItems().isEmpty()) {
				menuList.getSelectionModel().deselectAll();
			}
			menuList.getSelectionModel().select(iSummary, false);
		}
	}

	/**
	 * Injects the NR specific message bundle.
	 * @param nrMessages the nrMessages to set
	 */
	@Inject
	public void setNrMessages(NrMessages nrMessages) {
		this.nrMessages = nrMessages;
	}

	/**
	 * Injects the NR specific styles bundle.
	 * @param nrStyles the nrStyles to set
	 */
	@Inject
	public void setNrStyles(NrStyles nrStyles) {
		this.nrStyles = nrStyles;
	}

	/**
	 * Injects the {@link NrCenterWidget NR center widget}.
	 * @param centerWidget the centerWidget to set
	 */
	@Inject
	public void setCenterWidget(NrCenterWidget centerWidget) {
		this.centerWidget = centerWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
}
