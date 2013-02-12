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

package com.isotrol.impe3.gui.common.util;


import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.Model;


/**
 * {@link ChangeEvent} can only provide information about involved Models.<br/> Our services don't always work with
 * Models, so some extra event info must be supplied when firing events.<br/> This class offers an additional property
 * where anything can be inserted.<br/>
 * 
 * @author Andrei Cojocaru
 * 
 */
public class GenericChangeEvent extends ChangeEvent {

	/*
	 * Event types
	 */
	/**
	 * Alias for {@link ChangeEventSource#Add}<br/>
	 * @see ChangeEventSource#Add
	 */
	public static final int ADD = ChangeEventSource.Add;
	/**
	 * Alias for {@link ChangeEventSource#Remove}<br/>
	 * @see ChangeEventSource#Remove
	 */
	public static final int REMOVE = ChangeEventSource.Remove;
	/**
	 * Alias for {@link ChangeEventSource#Update}<br/>
	 * @see ChangeEventSource#Update
	 */
	public static final int UPDATE = ChangeEventSource.Update;

	/**
	 * Some event information that couldn't be cast as a Model.<br/>
	 */
	private Object data = null;

	/**
	 * @param type
	 */
	public GenericChangeEvent(int type) {
		super(type, null);
	}

	/**
	 * <br/>
	 * @param type
	 * @param source
	 * @param item
	 */
	public GenericChangeEvent(int type, Model source, Model item) {
		super(type, source, item);
	}

	/**
	 * <br/>
	 * @param type
	 * @param source
	 */
	public GenericChangeEvent(int type, Model source) {
		super(type, source);
	}

	/**
	 * <br/>
	 * @param type
	 * @param data
	 */
	public GenericChangeEvent(int type, Object data) {
		this(type, (Model) null);
		this.data = data;
	}

	/**
	 * Constructor created for compatibility purposes (type and source were used before, now type and info are being
	 * used).<br/>
	 * @param type
	 * @param source
	 * @param data
	 */
	public GenericChangeEvent(int type, Model source, Object data) {
		this(type, source);
		this.data = data;
	}

	/**
	 * @return the {@link #data}
	 */
	@SuppressWarnings("unchecked")
	public <X> X getData() {
		return (X) data;
	}
}
