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

/**
 * Delegate manager for data binding.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <T>
 *            bound data type.
 */
public class DataBoundSupport<T> implements IDataBound<T> {

	private T boundData = null;

	/**
	 * Constructor provided with the {@link #boundData} property.<br/>
	 * 
	 * @param data
	 */
	public DataBoundSupport(T data) {
		this.boundData = data;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.util.databinding.IDataBound#getBoundData()
	 */
	public T getBoundData() {
		return boundData;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.util.databinding.IDataBound#setBoundData(java.lang.Object)
	 */
	public void setBoundData(T data) {
		boundData = data;
	};
	
	/**
	 * Throws an exception. Called in setBoundData(), for classes where setBoundData is not allowed.<br/>
	 * @param clazz
	 */
	public static void throwIllegalBoundDataSettingException(Class<?> clazz) {
		throw new IllegalArgumentException(clazz.getName() + ": setBoundData() is not allowed!");
	}
}
