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

package com.isotrol.impe3.pms.gui.client.util;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;


/**
 * Utility class that is able to:<ul><li>Load frame columns from a json file to show them in the columns
 * palette</li>Read external services visibility from a json file<li></ul>
 * 
 * @author Manuel Ruiz
 * 
 */
public final class PmsUtil {

	/**
	 * List with the structure of columns
	 */
	private List<List<Integer>> columnsTemplates = null;

	/**
	 * Visibility of Comment Service menu
	 */
	private boolean commentsServiceVisible = false;

	/**
	 * Visibility of Nodes Repository menu
	 */
	private boolean nodeRepositoryVisible = true;
	
	
	private boolean IndexersServiceVisible=true;

	/**
	 * Visibility of Portal Users Service menu
	 */
	private boolean portalUsersServiceVisible = false;
	
	/** Whether disable normal login form */
	private boolean disableLogin = false;

	/**
	 * Common gui utilities
	 */
	private Util util = null;

	/**
	 * Pms messages bundle
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Pms Settings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;

	/**
	 * Default constructor.
	 */
	public PmsUtil() {
	}

	/**
	 * Read the json file "frameColumns.json" to build the arrays to show the default columns in columns palette in the
	 * page design.
	 */
	public void loadDesignColumns() {
		RequestBuilder json = new RequestBuilder(RequestBuilder.GET, "properties.json");
		json.setCallback(new RequestCallback() {

			public void onResponseReceived(Request request, Response response) {
				JSONObject fileJson = null;
				try {
					fileJson = (JSONObject) JSONParser.parseLenient(response.getText()).isObject();
				} catch (JSONException e) {
					util.error(pmsMessages.msgErrorParseColumnsJson());
				}

				if (fileJson != null) {
					JSONObject properties = fileJson.get("properties").isObject();
					JSONObject oProp = properties.isObject();
					// read frame columns
					parseFrameColumns(oProp);
					// read visibility of external services menus and disable login
					parseOtherProperties(oProp);
				}
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

	/**
	 * @param oProp properties json object
	 */
	private void parseFrameColumns(JSONObject oProp) {
		JSONObject frames = oProp.get("frames").isObject();

		JSONArray frameArray = frames.get("frame").isArray();

		columnsTemplates = new ArrayList<List<Integer>>();
		for (int i = 0; i < frameArray.size(); i++) {
			JSONObject frame = frameArray.get(i).isObject();
			JSONArray columns = frame.get("column").isArray();
			List<Integer> widths = new ArrayList<Integer>();
			for (int j = 0; j < columns.size(); j++) {
				JSONObject column = columns.get(j).isObject();
				JSONNumber width = column.get("width").isNumber();
				GWT.log("Anchura columna: " + width.doubleValue());
				widths.add(Integer.valueOf((int) width.doubleValue()));
			}
			if (totalWidthIs100(widths)) {
				columnsTemplates.add(widths);
			} else {
				util.error(pmsMessages.msgErrorColumnsWidth());
				columnsTemplates = null;
				return;
			}
		}

	}

	private void parseOtherProperties(JSONObject oProp) {

		// External services visibility
		JSONObject extServices = oProp.get("externalservices").isObject();
		// portal user service
		JSONObject pus = extServices.get("portalusersservice").isObject();
		JSONBoolean pusVisible = pus.get("visible").isBoolean();
		portalUsersServiceVisible = pusVisible.booleanValue();
		// comments service
		JSONObject cs = extServices.get("commentsservice").isObject();
		JSONBoolean csVisible = cs.get("visible").isBoolean();
		commentsServiceVisible = csVisible.booleanValue();
		// nodes repository
		JSONObject nr = extServices.get("noderepository").isObject();
		JSONBoolean nrVisible = nr.get("visible").isBoolean();
		nodeRepositoryVisible = nrVisible.booleanValue();
		
		// Normal login
		JSONBoolean disableLoginProperty = oProp.get("disableLogin").isBoolean();
		disableLogin = disableLoginProperty.booleanValue();
	}

	/**
	 * @param widths array with integers
	 * @return true if the integers add up to 100
	 */
	public boolean totalWidthIs100(List<Integer> widths) {
		int res = 0;

		for (Integer i : widths) {
			res += i;
		}

		return res == 100;
	}

	/**
	 * Download a pms export file
	 * @param id the file id to export
	 */
	public void exportPmsFile(String id) {
		util.openDocumentHref(Util.getBaseApplicationContext() + pmsSettings.exportUrl() + id);
	}

	/**
	 * Open a window with the passed page
	 * @param pageLoc the page to preview
	 */
	public void openPagePreview(PageLoc pageLoc) {
		String url = Util.getBaseApplicationContext() + pmsSettings.previewUrl() + pageLoc.getPortalId() + "/" + pageLoc.getId();
		Window.open(url, pmsMessages.headerPreviewWindow(), PmsConstants.NEW_WINDOW_FEATURES);
	}

	/**
	 * @return the columnsTemplates
	 */
	public List<List<Integer>> getColumnsTemplates() {
		return columnsTemplates;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @return the commentsServiceVisible
	 */
	public boolean isCommentsServiceVisible() {
		return commentsServiceVisible;
	}

	/**
	 * @return the nodeRepositoryVisible
	 */
	public boolean isNodeRepositoryVisible() {
		return nodeRepositoryVisible;
	}
	
	public boolean isIndexersServiceVisible(){
		return IndexersServiceVisible;
	}

	/**
	 * @return the portalUsersServiceVisible
	 */
	public boolean isPortalUsersServiceVisible() {
		return portalUsersServiceVisible;
	}

	/**
	 * @param pmsSettings the pmsSettings to set
	 */
	@Inject
	public void setPmsSettings(PmsSettings pmsSettings) {
		this.pmsSettings = pmsSettings;
	}

	/**
	 * @return the disableLogin
	 */
	public final boolean isDisableLogin() {
		return disableLogin;
	}

	public void loadProperties(Response response) {
		JSONObject fileJson = null;
		try {
			fileJson = (JSONObject) JSONParser.parseLenient(response.getText()).isObject();
		} catch (JSONException e) {
			util.error(pmsMessages.msgErrorParseColumnsJson());
		}

		if (fileJson != null) {
			JSONObject properties = fileJson.get("properties").isObject();
			JSONObject oProp = properties.isObject();
			// read frame columns
			parseFrameColumns(oProp);
			// read visibility of external services menus and disable login
			parseOtherProperties(oProp);
		}
	}
}
