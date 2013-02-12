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
package com.isotrol.impe3.gui.common.util.databinding;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;

/**
 * Clase base para los ContentPanels que implementan {@link IDataBound}
 * 
 * @author Andrei Cojocaru
 * 
 * @param <T>
 *            tipo de datos asociados a esta vista
 */
public abstract class ADataBoundContentPanel<T> extends ContentPanel implements IDataBound<T> {

	/**
	 * Delegate implementation of {@link IDataBound}.<br/>
	 */
	private IDataBound<T> dataBoundSupport = null;

	/**
	 * @param model
	 */
	public ADataBoundContentPanel(T data) {
		dataBoundSupport = new DataBoundSupport<T>(data);
		setButtonAlign(HorizontalAlignment.LEFT);
		setHeaderVisible(false);
		setBodyBorder(true);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.client.ui.IDataBound#getBoundData()
	 */
	public T getBoundData() {
		return this.dataBoundSupport.getBoundData();
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.util.databinding.IDataBound#setBoundData(java.lang.Object)
	 */
	public void setBoundData(T data) {
		DataBoundSupport.throwIllegalBoundDataSettingException(this.getClass());
	};
}
