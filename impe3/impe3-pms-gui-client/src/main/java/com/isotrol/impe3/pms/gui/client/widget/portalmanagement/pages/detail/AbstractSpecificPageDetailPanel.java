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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail;


/**
 * Panel with the details of a page. Subclasses can manage only one "kind" of pages (see subclasses).
 * 
 * @author Manuel Ruiz
 * 
 */
public abstract class AbstractSpecificPageDetailPanel extends AbstractPageDetailPanel {

	/**
	 * Constructor
	 */
	public AbstractSpecificPageDetailPanel() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail
	 * .AbstractPageDetailPanel#initComponents()
	 */
	@Override
	protected final void initComponents() {

		// page properties:
		addSimpleFields();

		// page components:
		addPageComponentsPanel();

		addButtonBar();
	}

	/**
	 * Adds a text field for page name, a field for page description and a combo selector for template.<br/>
	 */
	private void addSimpleFields() {

		// field page name
		initTfName();
		getTopContainer().add(getTfName());

		// field page description
		initFDescription();
		getTopContainer().add(getFDescription());

		// template selector
		initCbSelectTemplate();
		getTopContainer().add(getCbSelectTemplate());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return getPageTemplate().getId() != null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractPageDetailPanel
	 * #displaySpecificFieldsValues(boolean)
	 */
	@Override
	protected final void displaySpecificFieldsValues(boolean firstTime) {
		// nothing specific is shown - see subclasses
	}
}
