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

package com.isotrol.impe3.pms.gui.client.widget;


import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;


/**
 * Manages the navigation
 * @author Manuel Ruiz
 * 
 */
public class NavigationPanel extends LayoutContainer {

	/** splitter bar in navigation bread crumbs */
	private final String htmlNavigationSeparator = "<span class='separatorNavigationItem'>/</span>";

	private HorizontalPanel hp = null;

	/**
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {

		initThis();

		initNavigation();
		add(hp);
	}

	private void initThis() {
		setAutoWidth(true);
		setLayoutOnChange(true);
		hp = new HorizontalPanel();
		hp.setLayoutOnChange(true);
		hp.setVerticalAlign(VerticalAlignment.MIDDLE);
	}

	/**
	 * Adds an item to the navigation
	 * @param item
	 * @param last if the item is the last item of breadcrumb
	 */
	public void addNewNavigationItem(String item, boolean last) {
		String html = htmlNavigationSeparator;
		if (last) {
			html = html + "<b>" + item + "</b>";
		} else {
			html = html + item;
		}

		hp.add(new Html(html));
	}

	/**
	 * Adds an item with link to the navigation
	 * @param item
	 * @param link
	 */
	public void addNewNavigationItem(String item, String link) {
		hp.add(new Html(htmlNavigationSeparator));
		hp.add(new Hyperlink(item, link));
	}

	/**
	 * Removes all items except initial from navigation
	 */
	public void initNavigation() {
		hp.removeAll();
		Image portal3Logo = new Image("img/portal30.gif");
		portal3Logo.addStyleName("margin-right-5px");
		hp.add(portal3Logo);
		hp.add(new Hyperlink("Port@l", PmsConstants.INIT_TOKEN));
	}

	/**
	 * Change the last item of the breadcumb
	 * @param value the new last item
	 */
	public void changeLastItem(String item) {
		// remove current last item
		Widget lastHtml = hp.getWidget(hp.getItemCount() - 1);
		hp.remove(lastHtml);

		// add the new last item
		String html = htmlNavigationSeparator;
		html = html + "<b>" + item + "</b>";
		hp.add(new Html(html));
	}
}
