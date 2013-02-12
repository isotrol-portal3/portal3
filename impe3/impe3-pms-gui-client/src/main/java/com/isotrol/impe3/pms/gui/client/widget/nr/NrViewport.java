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

package com.isotrol.impe3.pms.gui.client.widget.nr;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.ICenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;


/**
 * @author Manuel Ruiz
 * 
 */
public class NrViewport extends PmsViewport {

	/**
	 * the passed current node repository id
	 */
	private String repositoryId = null;
	private NrLeftPanel nrLeftPanel = null;
	private NrCenterWidget nrCenterWidget = null;

	/**
	 * Constructor
	 */
	public NrViewport() {
		super();

		// add a listener to show the center panel
		addListener(Events.Show, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				nrLeftPanel.showInitWidget();
			}
		});
	}

	/**
	 * @return the repositoryId
	 */
	public String getRepositoryId() {
		return repositoryId;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
		nrLeftPanel.setRepositoryId(repositoryId);
	}

	@Override
	protected boolean isNorthVisible() {
		return true;
	}

	@Override
	protected ALeftPanel getLeftPanel() {
		return nrLeftPanel;
	}

	@Override
	protected ICenterWidget getCenterWidget() {
		return nrCenterWidget;
	}

	/**
	 * Injects the center widget.<br/>
	 * @param centerWidget
	 */
	@Inject
	public void setCenterWidget(NrCenterWidget centerWidget) {
		this.nrCenterWidget = centerWidget;
	}

	/**
	 * @param nrLeftPanel the nrLeftPanel to set
	 */
	@Inject
	public void setNrLeftPanel(NrLeftPanel nrLeftPanel) {
		this.nrLeftPanel = nrLeftPanel;
	}
}
