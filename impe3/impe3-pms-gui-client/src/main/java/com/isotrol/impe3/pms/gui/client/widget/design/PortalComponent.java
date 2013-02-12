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

package com.isotrol.impe3.pms.gui.client.widget.design;


import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;


/**
 * UI for Component
 * 
 * @author Manuel Ruiz
 * 
 */
public class PortalComponent extends HTML implements Cloneable {

	private String htmlDrop;

	private LayoutItemDTO dto;

	private HandlerRegistration clickHandler = null;

	/**
	 * Constructor
	 * 
	 * @param html
	 */
	public PortalComponent(String html) {
		setHTML(html);
	}

	/**
	 * Constructor
	 * @param model
	 */
	public PortalComponent(LayoutItemDTO dto) {
		setHTML(dto.getName());
		setTitle(dto.getName());
		this.dto = dto;
	}

//	@Override
//	protected void onLoad() {
//		super.onLoad();
//		//addStyleName(CSS_PORTAL_COMPONENT);
//	}

	/**
	 * Adds a click listener to show the component html in a popup
	 */
	protected void showHtmlOnClick() {
		final HTML html = new HTML(dto.getMarkup(0));
		final PopupPanel htmlPopup = new PopupPanel(true);
		htmlPopup.setAnimationEnabled(true);
		htmlPopup.setPixelSize(200, 200);
		htmlPopup.setWidget(html);
		htmlPopup.addStyleName("popup-component-html");

		clickHandler = addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				// z-index of design window is different each time, so we find out it to set the popup z-index
				final Style designStyle = Design.getInstance().getElement().getStyle();
				String zIndex = designStyle.getZIndex();
				if (!Util.emptyString(zIndex)) {
					int popUpZIndex = Integer.parseInt(zIndex) + 1;
					htmlPopup.getElement().getStyle().setZIndex(popUpZIndex);
				}

				htmlPopup.setPopupPosition(event.getClientX(), event.getClientY());
				htmlPopup.show();
			}
		});
	}

	/**
	 * Adds the click listener that shows the component html in a popup
	 */
	protected void removeHtmlOnClick() {
		if (clickHandler != null) {
			clickHandler.removeHandler();
		}
	}

	/**
	 * 
	 * @return the cloned object
	 */
	protected Object clone() {
		PortalComponent c = new PortalComponent(this.getDto());
		// c.setHtmlDrop(this.getHtmlDrop());
		return c;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PortalComponent) {
			PortalComponent c = (PortalComponent) obj;
			return c.getDto().getId().equals(getDto().getId());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getDto().getId().hashCode();
	}

	/**
	 * @return the htmlDrop
	 */
	public String getHtmlDrop() {
		return htmlDrop;
	}

	/**
	 * @param htmlDrop the htmlDrop to set
	 */
	public void setHtmlDrop(String htmlDrop) {
		this.htmlDrop = htmlDrop;
	}

	/**
	 * @return the dto
	 */
	public LayoutItemDTO getDto() {
		return dto;
	}

	/**
	 * @param dto the dto to set
	 */
	public void setDto(LayoutItemDTO dto) {
		this.dto = dto;
	}

}
