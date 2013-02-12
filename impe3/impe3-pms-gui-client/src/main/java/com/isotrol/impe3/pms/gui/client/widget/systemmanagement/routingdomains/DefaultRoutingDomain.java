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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;

/**
 * Popup window that manages the detailed information of the Default Routing Domain
 * 
 * @author Manuel Ruiz
 * 
 */
public class DefaultRoutingDomain extends RoutingDomainDetailsEditor {

	/*
	 * fields
	 */
	/**
	 * "Offline Base" field<br/>
	 */
	private TextField<String> tfOfflineBase = null;
	
	/**
	 * "Absolute Offline Path" field<br/>
	 */
	private TextField<String> tfAbsOfflineBase = null;

	/**
	 * Constructor provided with bound data.<br/>
	 * 
	 */
	public DefaultRoutingDomain() {
		super();
	}
	
	/**
	 * Inits the widget. Must be called after the properties are set.
	 * @param rdDto
	 */
	public void init(RoutingDomainDTO rdDto) {
		super.init(rdDto);
	}

	/**
	 * Adds the GUI fields for name, description, and bases.
	 */
	@Override
	protected void addFields() {
		super.addFields();
		
		getFormSupport().configReadOnly(getTfName());

		// online base
		tfOfflineBase = new TextField<String>();
		tfOfflineBase.setFieldLabel(getPmsMessages().labelOfflineBase());
		getFormSupport().configRequired(tfOfflineBase);
		tfOfflineBase.setAutoValidate(true);
		getMainContainer().add(tfOfflineBase);
		
		// absolute online base
		tfAbsOfflineBase = new TextField<String>();
		tfAbsOfflineBase.setFieldLabel(getPmsMessages().labelOfflineAbsBase());
//		tfAbsOnlineBase.setAutoValidate(true);
//		formSupport.configRequired(tfAbsOnlineBase);
		getMainContainer().add(tfAbsOfflineBase);
		
		getFields().add(tfOfflineBase);
		getFields().add(tfAbsOfflineBase);
	}

	/**
	 * Displays the vallues contained in the bound DTO.
	 */
	@Override
	protected void displayBoundDataValues() {

		super.displayBoundDataValues();

		String onBase = getRoutingDomainDto().getOfflineBase();
		tfOfflineBase.setValue(onBase);
		tfOfflineBase.updateOriginalValue(onBase);
		
		String aOnPath = getRoutingDomainDto().getOfflineAbsBase();
		tfAbsOfflineBase.setValue(aOnPath);
		tfAbsOfflineBase.updateOriginalValue(aOnPath);
		
	}

	/**
	 * Sets DTO values from GUI.<br/>
	 */
	@Override
	protected void applyValues() {
		super.applyValues();
		getRoutingDomainDto().setOfflineBase(tfOfflineBase.getValue());
		getRoutingDomainDto().setOfflineAbsBase(tfAbsOfflineBase.getValue());
	}
	
	@Override
	protected void saveRoutingDomain(AsyncCallback<RoutingDomainDTO> callback) {
		getRoutingDomainsService().setDefault(getRoutingDomainDto(), callback);
	}
}
