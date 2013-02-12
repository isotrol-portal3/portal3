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

package com.isotrol.impe3.pms.gui.client.widget.design.dnd;


import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.isotrol.impe3.pms.gui.client.widget.design.PortalComponent;


/**
 * Controller for drag & drop operations
 * @author Manuel Ruiz
 * 
 */
public final class DesignDNDController {

	private static DesignDNDController instance;

	private PickupDragController controller;

	private DesignDNDController() {
		controller = new PickupDragController(RootPanel.get(), false);
		controller.setBehaviorMultipleSelection(false);
		controller.addDragHandler(new DesignDragHandler());
	}

	/**
	 * @return the DesignDragController instance
	 */
	public static DesignDNDController getInstance() {
		if (null == instance) {
			// instantiate the common drag controller
			instance = new DesignDNDController();
		}
		return instance;
	}

	/**
	 * Make draggable a PortalComponent
	 * 
	 * @param p component to be made draggable
	 */
	public void makeDraggable(PortalComponent p) {
		controller.makeDraggable(p);
	}

	/**
	 * Make draggable a Box
	 * 
	 * @param p component to be made draggable
	 */
	public void makeDraggable(FocusPanel p) {
		controller.makeDraggable(p);
	}

	/**
	 * Make draggable a PortalComponent
	 * 
	 * @param p component to be made draggable
	 */
	public void makeDraggable(Widget p, Widget a) {
		controller.makeDraggable(p, a);
	}

	/**
	 * Register a new DropController, representing a new drop target, with this drag controller.
	 * 
	 * @param dropController the controller to register
	 */
	public void registerDropController(ColumnDropController dropController) {
		controller.registerDropController(dropController);
	}

	/**
	 * 
	 * @param dropController
	 */
	public void registerDropController(SimpleDropController dropController) {
		controller.registerDropController(dropController);
	}

	/**
	 * 
	 * @param dropController
	 */
	public void unregisterDropController(ColumnDropController dropController) {
		controller.unregisterDropController(dropController);
	}

	/**
	 * 
	 */
	public void clearSelection() {
		controller.clearSelection();
	}

	/**
	 * Sets the instance to null
	 */
	public static void resetInstance() {
		instance = null;
	}

	/**
	 * @see com.allen_sauer.gwt.dnd.client.PickupDragController#unregisterDropControllers()
	 */
	public void unregisterDropControllers() {
		controller.unregisterDropControllers();
	}

}
