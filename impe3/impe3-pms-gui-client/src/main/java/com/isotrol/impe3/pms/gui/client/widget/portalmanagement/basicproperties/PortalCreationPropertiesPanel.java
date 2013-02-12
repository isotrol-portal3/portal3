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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;


/**
 * @author Andrei Cojocaru
 * 
 */
public class PortalCreationPropertiesPanel extends APortalProperties {

	/**
	 * On success, closes all tab items and opens the portal-specific tabs:<br/> <ul> <li>portal properties tab</li>
	 * <li>portal info architecture tab (categories + content types)</li> <li>portal components tab</li> <li>portal
	 * bases tab</li> <li>portal pages tab</li> </ul> On error, shows error alert.
	 */
	private AsyncCallback<Void> saveCallback = null;

	/**
	 * Default constructor.<br/>
	 * @param portalTemplate
	 * @param portalDto
	 */
	public PortalCreationPropertiesPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependences injection.
	 * 
	 * @param portalTemplate
	 * @return
	 */
	public PortalCreationPropertiesPanel init(PortalTemplateDTO portalTemplate) {
		saveCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getErrorProcessor()
					.processError(arg0, getErrorMessageResolver(), getPmsMessages().msgErrorSavePortal());
			}

			public void onSuccess(Void arg0) {
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSavePortal());
			}
		};

		super.initWidget(portalTemplate);

		return this;
	}

	/**
	 * Returns a "save" action callback for portal creation. (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.APortalProperties#getSaveCallback()
	 */
	@Override
	protected AsyncCallback<Void> getSaveCallback() {
		return saveCallback;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.APortalProperties#getHeadingText()
	 */
	@Override
	protected String getHeadingText() {
		return getPmsMessages().headerPortalCreationPanel();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return false;
	}

	@Override
	protected void addSpecificButtons() {
		
	}
}
