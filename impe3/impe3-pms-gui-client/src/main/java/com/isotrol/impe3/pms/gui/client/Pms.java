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

package com.isotrol.impe3.pms.gui.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.gui.client.ioc.IPmsFactory;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.LoginPanel;


/**
 * Entry Point for Portal Management System app.
 */
public class Pms implements EntryPoint {
	
	/**
	 * Application entry point. <br/> Shows the login widget or enter in application directly if you are logged
	 */
	public void onModuleLoad() {

		IPmsFactory factory = PmsFactory.getInstance();
		final LoginPanel loginPanel = factory.getLoginPanel();

		// checks if the session is still active
		AsyncCallback<SessionDTO> callback = new AsyncCallback<SessionDTO>() {

			public void onSuccess(SessionDTO result) {
				if (result != null) {
					loginPanel.showPmsViewport(result);
					// load frame columns from JSON
					loginPanel.getPmsUtil().loadDesignColumns();
				} else {
					getPropertiesFileAndShowLogin(loginPanel);
				}
			}

			public void onFailure(Throwable caught) {
				getPropertiesFileAndShowLogin(loginPanel);
			}
		};
		loginPanel.getSessionsService().getSession(callback);
	}

	private void getPropertiesFileAndShowLogin(final LoginPanel loginPanel) {
		RequestBuilder json = new RequestBuilder(RequestBuilder.GET, "properties.json");
		json.setCallback(new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				loginPanel.getPmsUtil().loadProperties(response);
				showLogin(loginPanel);
			}

			public void onError(Request request, Throwable exception) {
				GWT.log("Throwable: " + exception.getMessage());
				exception.printStackTrace();
			}
		});
		try {
			json.send();
		} catch (RequestException e) {
			GWT.log("RequestException: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	private void showLogin(LoginPanel loginPanel) {
		if (GWT.isScript()) {
			removeHash();
		}
		loginPanel.show();
	}

	private native void removeHash() /*-{
		if($wnd.location.hash) {
			$wnd.location.hash = "";
		}
	}-*/;
	
}
