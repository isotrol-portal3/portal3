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


import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;


/**
 * Frame with a Component
 * @author Manuel Ruiz
 * 
 */
public class FrameComponent extends Frame<ComponentFrameDTO> implements Cloneable {

	private static final String CSS_PORTAL_COMPONENT = "estiloWidget";

	private PortalComponent component;
	/** indicates if the component in frame is a <i>Space</i> */
	private boolean space = false;

	/** the wrapped dto */
	private ComponentFrameDTO frame = null;

	/**
	 * Default constructor
	 */
	public FrameComponent() {
	}

	/**
	 * Inits this widget with a Portal component
	 * @param p
	 * @return
	 */
	public FrameComponent init(PortalComponent p) {
		getVertPanel().add(p);
		super.init(new ComponentFrameDTO());
		return this;
	}

	/**
	 * Inits this widget with a LayoutItem DTO
	 * @param dto
	 * @return
	 */
	public FrameComponent init(LayoutItemDTO dto) {
		return init(dto, new ComponentFrameDTO());
	}

	/**
	 * Inits this widget witha LayoutItem DTO
	 * @param dto
	 * @param inherited
	 * @return
	 */
	public FrameComponent init(LayoutItemDTO dto, ComponentFrameDTO frame) {
		this.frame = frame;
		this.component = new PortalComponent(dto);
		this.space = dto.isSpace();

		getVertPanel().add(component);
		super.init(frame);

		return this;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!isInherited()) {
			addStyleName(CSS_PORTAL_COMPONENT);
		}
	}

	/**
	 * 
	 * @return the cloned object
	 */
	public Object clone() {
		FrameComponent clone;
		// Clone our internal widget

		PortalComponent pc = (PortalComponent) component.clone();
		clone = PmsFactory.getInstance().getFrameComponent().init(pc);
		clone.setComponent((PortalComponent) getComponent().clone());

		return clone;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FrameComponent) {
			FrameComponent c = (FrameComponent) obj;
			return c.getComponent().equals(getComponent());
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (getComponent() != null) {
			return getComponent().hashCode();
		}
		return super.hashCode();
	}

	/**
	 * Returns the empty component
	 * @return the EmptyComponent
	 */
	public Widget getEmptyComponent() {
		Widget widget = null;

		// PortalComponent portalComponent = getComponent();
		// if (portalComponent.getDto().isSpace()) {
		// widget = portalComponent;
		// }

		return widget;
	}

	/**
	 * Add a div that disables the frame
	 */
	public void block() {
		// add((new HTML("<div id='capaBloqueo' class='estiloBloqueo'/>")));
	}

	/**
	 * Removes the frame component from the main column and adds it to the palette
	 */
	@Override
	public void removeFrameFromMainColumn() {

		if (!isInherited()) {
			removeFromParent(false);

			ComponentsPanel compsPanel = Design.getInstance().getPalettesPanel().getComponentsPanel();
			if (compsPanel != null) {
				removeStyleName("box-ondrop");
				setClosable(!isClosable());
				removeCssSelector();

				// remove fill style in layout
				if (isSpace()) {
					getComponent().removeStyleName(getPmsStyles().spaceInLayout());
				}

				// change component's html
				getComponent().setHTML(component.getDto().getName());
				compsPanel.add(this);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.design.Frame#setClosable(boolean)
	 */
	@Override
	public void setClosable(boolean closable) {
		super.setClosable(closable);
		if (!closable) {
			deleteRemoveButton();
		}
	}

	protected void showComponentHtmlOnClick() {
		if (component != null) {
			component.showHtmlOnClick();
		}
	}

	protected void removeComponentHtmlOnClick() {
		if (component != null) {
			component.removeHtmlOnClick();
		}
	}

	/**
	 * Deletes the button to remove itself from its parent
	 */
	private void deleteRemoveButton() {
		super.getMenuPanel().remove(0);
	}

	/**
	 * @return the component
	 */
	public PortalComponent getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(PortalComponent component) {
		this.component = component;
	}

	/**
	 * @return the space
	 */
	public boolean isSpace() {
		return space;
	}

	@Override
	protected String getDraggableStyle() {
		return "draggable-component";
	}
}
