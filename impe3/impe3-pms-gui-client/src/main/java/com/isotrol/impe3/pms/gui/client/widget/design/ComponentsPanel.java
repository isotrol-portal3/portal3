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


import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;


/**
 * Panel with the components
 * @author Manuel Ruiz
 * 
 */
public class ComponentsPanel extends VerticalPanel {

	private static final String WIDTH = "96%";
	private static final String CSS_PANEL = "componentTab-panel";

	// mantenemos una estructura con los elementos contenidos:
	private List<FrameComponent> components = new LinkedList<FrameComponent>();
	
	/**
	 * The current layoutDto in design panel
	 */
	private LayoutDTO layoutDto = null;

	/**
	 * Constructor
	 * @param page
	 */
	public ComponentsPanel(LayoutDTO layoutDto) {
		this.layoutDto = layoutDto;

		configComponent();
		initsPanel();
	}

	/**
	 * Adds the components that design not contains
	 */
	protected void initsPanel() {
		List<LayoutItemDTO> componentsList = layoutDto.getItems();

		for (LayoutItemDTO dto : componentsList) {
			List<String> framesList = Design.getInstance().getFrameDtoComponents();
			if (!framesList.contains(dto.getId())) {
				add(PmsFactory.getInstance().getFrameComponent().init(dto));
			}
		}
	}

	/**
	 * Add a widget to the tab
	 * @param w Widget to add to the tab
	 */
	public ComponentsPanel(Widget w) {
		add(w);
		configComponent();
	}

	/**
	 * Removed widgets that are instances of {@link FrameComponent}
	 * 
	 * @param w the widget to remove
	 */
	@Override
	public boolean remove(Widget w) {
		int index = getWidgetIndex(w);
		if (index != -1 && w instanceof FrameComponent) {
			FrameComponent frame = (FrameComponent) w;
			components.remove(frame);
			frame.removeComponentHtmlOnClick();
		}

		return super.remove(w);
	}

	/**
	 * Adds a frame component only if the panel not contains it
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#add(com.google.gwt.user.client .ui.Widget)
	 */
	@Override
	public final void add(Widget w) {
		if (w instanceof FrameComponent && !components.contains(w)) {
			FrameComponent frame = (FrameComponent) w;
			components.add(frame);
			frame.showComponentHtmlOnClick();
			super.add(frame);
		}
	}

	/**
	 * insertamos solo si no está
	 * 
	 * @see com.google.gwt.user.client.ui.VerticalPanel#insert(com.google.gwt.user.client.ui.Widget, int)
	 */
	@Override
	public void insert(Widget w, int beforeIndex) {
		// insercion chequeando si ya lo tenemos:
		if (w instanceof FrameComponent && !components.contains(w)) {
			super.insert(w, beforeIndex);
		}
	}

	private void configComponent() {
		setWidth(WIDTH);
		addStyleName(CSS_PANEL);
	}

	/**
	 * @return the components
	 */
	public List<FrameComponent> getComponents() {
		return components;
	}
}
