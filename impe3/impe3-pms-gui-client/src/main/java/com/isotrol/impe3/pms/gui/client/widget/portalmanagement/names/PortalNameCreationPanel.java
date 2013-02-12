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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;


/**
 * Class to create a portal name
 * @author Manuel Ruiz
 * 
 */
public class PortalNameCreationPanel extends APortalName {

	/**
	 * Portal parent id
	 */
	private String parentId = null;

	/**
	 * Default constructor.<br/>
	 */
	public PortalNameCreationPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependences injection.
	 * 
	 * @param portalNameDto
	 * @param parentId
	 */
	public void init(PortalNameDTO portalNameDto, String parentId) {
		this.parentId = parentId;
		super.initWidget(portalNameDto);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.config.APortalProperties#getHeadingText()
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

	/** (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.APortalName#savePortalName(com.isotrol.impe3.pms.api.portal.PortalNameDTO)
	 */
	@Override
	protected void savePortalName(PortalNameDTO portalNameDto) {

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSavePortal());
			}

			public void onFailure(Throwable caught) {
				getUtilities().unmask();
				getErrorProcessor().processError(caught, getErrorMessageResolver(),
					getPmsMessages().msgErrorSavePortal());
			}
		};
		getPortalsService().create(portalNameDto, parentId, callback);
	}

	@Override
	protected void addSpecificButtons() {
		
	}
}
