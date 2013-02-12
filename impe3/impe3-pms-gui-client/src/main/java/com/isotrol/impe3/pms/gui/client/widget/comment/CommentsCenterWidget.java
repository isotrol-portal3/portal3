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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.comment;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.widget.ICenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;

/**
 * Container of all elements displayed in the middle of the screen (at the right of the menu).<br/>
 * The bound layout type is {@link CardLayout}, so only one widget will be shown at once.
 * It also publishes a method to add and/or show a passed inner component (see {@link #show(Component)}).  
 * 
 * @author Manuel Ruiz
 *
 */
public class CommentsCenterWidget extends PmsContentPanel implements ICenterWidget {

	/**
	 * supports {@link IInitializableWidget#isInitialized()}<br/>
	 */
	private boolean initialized = false;
	
	/**
	 * 
	 */
	public CommentsCenterWidget() {
		setLayout(new CardLayout());
		
		setBorders(false);
		setBodyBorder(false);
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.ISimpleInitializableWidget#init()
	 */
	/**
	 * <br/>
	 */
	public CommentsCenterWidget init() {
		
		initialized = true;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.LayoutContainer#getLayout()
	 */
	/**
	 * Overrides in order to return a CardLayout type.<br/>
	 */
	@Override
	public CardLayout getLayout() {
		return (CardLayout) super.getLayout();
	}
	
	/**
	 * If not already added, adds the passed component to this panel.<br/>
	 * Shows the passed component.
	 * @param component
	 */
	public void show(Component component) {
		if (!component.isAttached()) {
			add(component);
		}
		getLayout().setActiveItem(component);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}
	
}
