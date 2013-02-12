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

package com.isotrol.impe3.gui.common.widget;


import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Shows a content panel with an html iframe
 * @author Manuel Ruiz
 * 
 */
public class IframePanel extends ContentPanel {

	/** url to open */
	private String iframeUrl = null;

	@Override
	protected void beforeRender() {
		configThis();
	}

	private void configThis() {
		setLayoutOnChange(true);
		setLayout(new FitLayout());
		setHeaderVisible(false);
	}

	private void addIFrame() {

		assert iframeUrl != null : "iframe url can't be null";

		Html iframe = new Html("<iframe width='100%' frameborder='0' height='100%' scrolling='auto' src='" + iframeUrl
			+ "'/>");
		add(iframe);
	}

	/**
	 * @param iframeUrl the iframeUrl to set
	 */
	public void setIframeUrl(String iframeUrl) {
		this.iframeUrl = iframeUrl;
		addIFrame();
	}

}
