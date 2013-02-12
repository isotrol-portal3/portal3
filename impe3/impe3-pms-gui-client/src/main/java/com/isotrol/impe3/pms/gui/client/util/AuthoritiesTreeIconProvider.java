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
package com.isotrol.impe3.pms.gui.client.util;

import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.DTOModelData;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.user.Granted;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;

/**
 * Provides with authorities tree icons:<ul>
 * <li>depth 1 elements: icon for granted authority;</li>
 * <li>depth 2 elements: icon for implied authority.</li>
 * </ul>
 * @author Andrei Cojocaru
 *
 */
public class AuthoritiesTreeIconProvider 
	implements	ModelStringProvider<DTOModelData<?>>, ModelIconProvider<DTOModelData<?>> {

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;
	
	/**
	 * Styles bundle.<br/>
	 */
	private GuiCommonStyles styles = null;
	
	/**
	 * <ul>
	 * <li>depth 1 elements: icon for granted authority;</li>
	 * <li>depth 2 elements: icon for implied authority.</li>
	 * </ul><br/>
	 */
	public String getStringValue(DTOModelData<?> model, String property) {
		if(model.getDTO() instanceof Granted<?>) {
			return pmsStyles.iconAuthorityGranted();
		}
		return pmsStyles.iconAuthorityImplied()	+ Constants.SPACE + styles.marginLeft17px();
	}

	/** (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelIconProvider#getIcon(com.extjs.gxt.ui.client.data.ModelData)
	 */
	public AbstractImagePrototype getIcon(DTOModelData<?> model) {
		return IconHelper.create(getStringValue(model, null));
	}
	
	/**
	 * Injects the PMS specific styles bundle.<br/>
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the generic styles bundle.<br/>
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

}
