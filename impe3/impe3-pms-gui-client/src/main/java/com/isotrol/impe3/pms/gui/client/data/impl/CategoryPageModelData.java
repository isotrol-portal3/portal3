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

package com.isotrol.impe3.pms.gui.client.data.impl;


import java.util.Collection;
import java.util.Set;

import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.CategoryPageDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;


/**
 * ModelData for CategoryPageDTO
 * 
 * @author Manuel Ruiz
 * 
 */
public class CategoryPageModelData extends WithCategoryPageChildrenModelData<CategoryPageDTO> {

	/** Descriptor for category's name property */
	public static final String PROPERTY_NAME = Constants.PROPERTY_NAME;
	/** Descriptor for category's property to dysplay */
	public static final String PROPERTY_TO_DISPLAY = "display";
	/**
	 * <b>ID</b> property.
	 */
	public static final String PROPERTY_ID = Constants.PROPERTY_ID;

	/**
	 * <code>true</code> if the tree node associated to this ModelData represents a Category Page
	 * associated to only current category.<br/>
	 */
	private boolean onlyThis;
	/**
	 * <code>true</code> if the tree node associated to this ModelData represents a Category Page
	 * associated to current category hierarchy (the category and its children).<br/>
	 */
	private boolean thisAndChildren;
	/**
	 * <code>true</code> if the tree node associated to this ModelData represents the default
	 * Category Page.<br/>
	 */
	private boolean defaultPage;
	/**
	 * The property to display.<br/>
	 */
	private String propertyToDisplay;

	/**
	 * <code>true</code> if the tree node associated to this ModelData is only a visual classifier,
	 * but does not represents any Page.<br/>
	 */
	private boolean isPage;

	/**
	 * <br/>
	 * @param dto
	 */
	public CategoryPageModelData(CategoryPageDTO dto) {
		super(dto);
	}

	/**
	 * Public properties for this class instances.<br/>
	 */
	public static final Set<String> PROPERTIES = propertySet(WithCategoryPageChildrenModelData.PROPERTIES, PROPERTY_NAME, PROPERTY_ID, PROPERTY_TO_DISPLAY);

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doGet(java.lang.String)
	 */
	@Override
	protected Object doGet(String property) {
		if (PROPERTY_TO_DISPLAY.equals(property)) {
			if (propertyToDisplay != null) {
				return propertyToDisplay;
			} else {
				return getDTO().getCategory().getName();
			}
		} else if (PROPERTY_NAME.equals(property)) {
			return getDTO().getCategory().getName();
		} else if (PROPERTY_ID.equals(property)) {
			Inherited<PageLoc> ot = getDTO().getOnlyThis();
			if (ot != null) {
				return ot.getValue().getId();
			}
			return null;
		} else {
			return super.doGet(property);
		}
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.data.model.DTOModelData#doSet(java.lang.String, java.lang.Object)
	 */
	@Override
	protected Object doSet(String property, Object value) {
		// nothing to write here:
		throw new IllegalArgumentException("Not writable property: " + property);
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#getPropertyNames()
	 */
	public Collection<String> getPropertyNames() {
		return PROPERTIES;
	}

	/**
	 * @return the isPage
	 */
	public boolean isPage() {
		return isPage;
	}

	/**
	 * @param page the isPage to set
	 */
	public void setPage(boolean page) {
		this.isPage = page;
	}

	/**
	 * @return the onlyThis
	 */
	public boolean isOnlyThis() {
		return onlyThis;
	}

	/**
	 * @param onlyThis the onlyThis to set
	 */
	public void setOnlyThis(boolean onlyThis) {
		this.onlyThis = onlyThis;
	}

	/**
	 * @return the thisAndChildren
	 */
	public boolean isThisAndChildren() {
		return thisAndChildren;
	}

	/**
	 * @param thisAndChildren the thisAndChildren to set
	 */
	public void setThisAndChildren(boolean thisAndChildren) {
		this.thisAndChildren = thisAndChildren;
	}

	/**
	 * @return the propertyToDisplay
	 */
	public String getPropertyToDisplay() {
		return propertyToDisplay;
	}

	/**
	 * @param propertyToDisplay the propertyToDisplay to set
	 */
	public void setPropertyToDisplay(String propertyToDisplay) {
		this.propertyToDisplay = propertyToDisplay;
	}

	/**
	 * @return the defaultPage
	 */
	public boolean isDefaultPage() {
		return defaultPage;
	}

	/**
	 * @param defaultPage the defaultPage to set
	 */
	public void setDefaultPage(boolean defaultPage) {
		this.defaultPage = defaultPage;
	}
}
