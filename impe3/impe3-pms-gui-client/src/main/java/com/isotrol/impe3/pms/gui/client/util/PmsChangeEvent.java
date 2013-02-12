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

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSource;
import com.extjs.gxt.ui.client.data.Model;

/**
 * {@link ChangeEvent} can only provide information about involved Models.<br/>
 * PMS services don't work always with Models, so some extra event info must be supplied
 * when firing events.<br/>
 * This class offers an additional property where anything can be inserted.<br/>
 * It also defines additional event types.
 *  
 * @author Andrei Cojocaru
 *
 */
public class PmsChangeEvent extends ChangeEvent {
	
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
	public static final int DELETE = ChangeEventSource.Remove;
	/**
	 * Alias for {@link ChangeEventSource#Update}<br/>
	 * @see ChangeEventSource#Update
	 */
	public static final int UPDATE = ChangeEventSource.Update;
	
	/**
	 * Fired when a portal bases are set.<br/>
	 */
	public static final int UPDATE_BASES = 50;
	
	/**
	 * Fired when a Portal Properties are set.
	 */
	public static final int UPDATE_PROPERTIES = 60;
	
	/**
	 * Fired when a Portal SetFilters are set.
	 */
	public static final int UPDATE_SET_FILTERS = 70;
	
	/**
	 * Fired when we want refresh a collection
	 */
	public static final int REFRESH = 80;
	
	/**
	 * Fired when a portal name dto is changed
	 */
	public static final int UPDATE_PORTAL_NAME = 90;
	
	/**
	 * Fired when import something
	 */
	public static final int IMPORT = 100;
	
	/**
	 * Fired when import something
	 */
	public static final int UPDATE_PORTAL_DEVICES = 110;
	
	/**
	 * Some event information that couldn't be cast as a Model.<br/>
	 */
	private Object eventInfo = null;

	/**
	 * <br/>
	 * @param type
	 * @param source
	 * @param item
	 */
	public PmsChangeEvent(int type, Model source, Model item) {
		super(type, source, item);
	}

	/**
	 * <br/>
	 * @param type
	 * @param source
	 */
	public PmsChangeEvent(int type, Model source) {
		super(type, source);
	}
	
	/**
	 * <br/>
	 * @param type
	 * @param eventInfo
	 */
	public PmsChangeEvent(int type, Object info) {
		this(type,(Model)null);
		this.eventInfo = info;
	}
	
	/**
	 * Constructor created for compatibility purposes
	 * (type and source were used before, now type and info are being used).<br/>
	 * @param type
	 * @param source
	 * @param info
	 */
	public PmsChangeEvent(int type, Model source, Object info) {
		this(type,source);
		this.eventInfo = info;
	}
	
	/**
	 * @return the {@link #eventInfo}
	 */
	@SuppressWarnings("unchecked")
	public <X> X getEventInfo() {
		return (X) eventInfo;
	}
}
