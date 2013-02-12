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

package com.isotrol.impe3.gui.common.data;


import java.util.Collection;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeEventSupport;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;


/**
 * ModelData backed by a DTO.
 * 
 * @author Andres Rodriguez
 * @param <D> DTO type.
 */
public final class DTOModel<D> implements Model {
	private final DTOModelData<D> modelData;
	private final ChangeEventSupport changeEventSupport = new ChangeEventSupport();

	/**
	 * <br/>
	 * @param modelData bound ModelData
	 */
	public DTOModel(DTOModelData<D> modelData) {
		this.modelData = modelData;
	}

	/**
	 * Delegate to {@link com.extjs.gxt.ui.client.data.ModelData#get(java.lang.String)}<br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#get(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <X> X get(String property) {
		return (X) modelData.get(property);
	}

	public Map<String, Object> getProperties() {
		return modelData.getProperties();
	}

	public Collection<String> getPropertyNames() {
		return modelData.getPropertyNames();
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#remove(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <X> X remove(String property) {
		X old = (X) modelData.remove(property);
		notifyPropertyChanged(property, null, old);
		return old;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ModelData#set(java.lang.String, java.lang.Object)
	 */
	public <X> X set(String property, X value) {
		X old = modelData.set(property, value);
		notifyPropertyChanged(property, value, old);
		return old;
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSource
	 * #addChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void addChangeListener(ChangeListener... listener) {
		changeEventSupport.addChangeListener(listener);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSource#notify(com.extjs.gxt.ui.client.data.ChangeEvent)
	 */
	public void notify(ChangeEvent event) {
		changeEventSupport.notify(event);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSource
	 * #removeChangeListener(com.extjs.gxt.ui.client.data.ChangeListener[])
	 */
	public void removeChangeListener(ChangeListener... listener) {
		changeEventSupport.removeChangeListener(listener);
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSource#removeChangeListeners()
	 */
	public void removeChangeListeners() {
		changeEventSupport.removeChangeListeners();
	}

	/**
	 * <br/>
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.data.ChangeEventSource#setSilent(boolean)
	 */
	public void setSilent(boolean silent) {
		changeEventSupport.setSilent(silent);
	}

	protected void fireEvent(int type) {
		notify(new ChangeEvent(type, this));
	}

	protected void fireEvent(int type, Model item) {
		notify(new ChangeEvent(type, this, item));
	}

	protected void notifyPropertyChanged(String name, Object value, Object oldValue) {
		if (value == oldValue) {
			return;
		}
		if (value != null && value.equals(oldValue)) {
			return;
		}
		notify(new PropertyChangeEvent(Update, this, name, oldValue, value));
	}
	
	/**
	 * Instances of this class are needed in ChangeEvents, and user must be able to retrieve
	 * the involved DTOs from the events eventInfo.<br/>
	 * @return bound DTO
	 */
	public D getDTO() {
		return modelData.getDTO();
	}

}
