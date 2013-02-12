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


import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PortalsController;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;


/**
 * Shows a iframe that loads the portal's offline url. Shows the online and offline urls too.
 * @author Manuel Ruiz
 * 
 */
public class PortalPreviewPanel extends ContentPanel {

	/** the current portal name */
	private PortalNameDTO portalName = null;

	/** the portal urls (offline, online y pms) */
	private PortalURLsDTO portalURLs = null;

	/** the portal's service */
	private IPortalsServiceAsync portalService = null;

	/** offline panel */
	private HorizontalPanel offlinePanel = null;

	/** online panel */
	private HorizontalPanel onlinePanel = null;

	/** panel where the offline and the online panels are added */
	private VerticalPanel urisPanel = null;

	@Override
	protected void beforeRender() {

		assert portalName != null : "a portaTemplate must be setter before render the PortalPreviewPanel";
		configThis();

		addComponents();
		configController();
	}

	private void addComponents() {
		addUrlsPanel();
		addIFrame();
	}

	private void configThis() {
		setLayoutOnChange(true);
		setLayout(new FitLayout());
		setHeaderVisible(false);
	}

	/**
	 * Adds a widget with two links, the online and offline preview links
	 */
	private void addUrlsPanel() {
		urisPanel = new VerticalPanel();
		urisPanel.setSpacing(3);
		setTopComponent(urisPanel);

		addOfflinePanel();
		addOnlinePanel();
	}

	private void addOfflinePanel() {

		if (offlinePanel == null) {
			offlinePanel = new HorizontalPanel();
			offlinePanel.setSpacing(3);
			offlinePanel.setLayoutOnChange(true);
			urisPanel.add(offlinePanel);
		} else {
			offlinePanel.removeAll();
		}

		offlinePanel.add(new Html("<b>Offline:</b>"));

		String urlOffline = portalURLs.getOffline();
		if (urlOffline != null) {
			offlinePanel.add(new Html(PmsConstants.HTML_LINK.replaceAll(PmsConstants.PATTERN_HREF, urlOffline)
				.replaceAll(PmsConstants.PATTERN_TEXT, urlOffline)));
		}
	}

	private void addOnlinePanel() {

		if (onlinePanel == null) {
			onlinePanel = new HorizontalPanel();
			onlinePanel.setSpacing(3);
			onlinePanel.setLayoutOnChange(true);
			urisPanel.add(onlinePanel);
		} else {
			onlinePanel.removeAll();
		}

		onlinePanel.add(new Html("<b>Online:</b>"));

		String urlOnline = portalURLs.getOnline();
		if (urlOnline != null) {
			onlinePanel.add(new Html(PmsConstants.HTML_LINK.replaceAll(PmsConstants.PATTERN_HREF, urlOnline)
				.replaceAll(PmsConstants.PATTERN_TEXT, urlOnline)));
		}
	}

	private void addIFrame() {
		Html iframe = new Html("<iframe width='100%' frameborder='0' height='100%' scrolling='auto' src='"
			+ portalURLs.getPMS() + "'/>");
		add(iframe);
	}

	/**
	 * Configures the portal controller with a listener.
	 */
	private void configController() {
		final PortalsController portalsController = (PortalsController) portalService;

		final ChangeListener changeListener = new ChangeListener() {
			public void modelChanged(ChangeEvent event) {
				PmsChangeEvent pmsEvent = (PmsChangeEvent) event;
				switch (event.getType()) {
					case PmsChangeEvent.UPDATE_PORTAL_NAME: // fire when we rename portal
						PortalNameDTO info = (PortalNameDTO) pmsEvent.getEventInfo();
						updatePortalNameValues(info);
						break;
					case PmsChangeEvent.UPDATE: // fire when portal properties have changed
						getAndUpdateUrls();
						break;
					default:
						break;
				}
			}
		};

		portalsController.addChangeListener(changeListener);

		// remove this listener from portal controller when this widget is dettached
		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				portalsController.removeChangeListener(changeListener);

			}
		});
	}

	/**
	 * Changes portal name values in this widget if they have changed in server
	 * @param info the new PortalNameDTO
	 */
	private void updatePortalNameValues(PortalNameDTO info) {
		// changes portal display name
		String newDisplayName = info.getName().getDisplayName();
		TabItem tabItem = (TabItem) getParent();
		tabItem.setText(newDisplayName);
		tabItem.layout();

		// changes portal path
		if (info.getName().getPath() != null && !info.getName().getPath().equals(portalName.getName().getPath())) {
			getAndUpdateUrls();
		}

		setPortalName(info);
	}

	private void getAndUpdateUrls() {

		portalService.getURLs(portalName.getId(), new AsyncCallback<PortalURLsDTO>() {

			public void onSuccess(PortalURLsDTO result) {
				setPortalURLs(result);
				addOfflinePanel();
				addOnlinePanel();
			}

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * @param portalName the portalName to set
	 */
	public void setPortalName(PortalNameDTO portalName) {
		this.portalName = portalName;
	}

	/**
	 * @param portalURLs
	 */
	public void setPortalURLs(PortalURLsDTO portalURLs) {
		this.portalURLs = portalURLs;
	}

	/**
	 * @param portalService the portalService to set
	 */
	@Inject
	public void setPortalService(IPortalsServiceAsync portalService) {
		this.portalService = portalService;
	}
}
