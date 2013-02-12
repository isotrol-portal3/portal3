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

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.google.gwt.user.client.Element;

/**
 * @author Andrei Cojocaru
 *
 * @param <M>
 */
public class DualListFieldWithHeader<M extends ModelData> extends DualListField<M> {

	/**
	 * Template for the ListField headers<br/>
	 */
	private static final String TEMPLATE_HEADER = 
		"<div class='x-small-editor x-panel-header x-unselectable'>" 
		+	"<span class='x-panel-header-text'>${HEADING}</span></div>";
	
	/**
	 * Pattern to be replaced by the heading value<br/>
	 */
	private static final String PATTERN_HEADING = "${HEADING}";
	
	/**
	 * "From" heading.<br/>
	 */
	private String sFrom = null;
	
	/**
	 * "To" heading.<br/>
	 */
	private String sTo = null;
	
	/**
	 * Default constructor. Adds the headings after rendering.
	 */
	public DualListFieldWithHeader() {
		addListener(Events.Render, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				addHeading(getFromList(), sFrom);
				addHeading(getToList(), sTo);
			};
		});
	}

	/**
	 * <br/>
	 * @param s
	 */
	public void setFromHeading(String s) {
		this.sFrom = s;
	}

	/**
	 * <br/>
	 * @param s
	 */
	public void setToHeading(String s) {
		this.sTo = s;
	}
	
	/**
	 * Adds the passed heading to the passed list.<br/>
	 * The passed list must not have a heading.
	 * @param list
	 * @param heading
	 */
	private void addHeading(ListField<M> listField, String heading) {
		Element tdElement = listField.el().findParentElement("td", 3);
		String html = TEMPLATE_HEADER.replace(PATTERN_HEADING, heading);
		new El(tdElement).insertFirst(new El(html).dom);
	}
}
