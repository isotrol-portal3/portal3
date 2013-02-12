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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.CheckChangedEvent;
import com.extjs.gxt.ui.client.event.CheckChangedListener;
import com.extjs.gxt.ui.client.event.CheckProvider;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;


/**
 * A Grid that implements {@link CheckProvider} interface. The checkbox selector column is automatically created and
 * added to the provided column config.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <M>
 */
public class CheckProviderGrid<M extends ModelData> extends Grid<M> implements CheckProvider<M> {

	/**
	 * The checkbox selection model.
	 */
	private CheckBoxSelectionModel<M> sm = new CheckBoxSelectionModel<M>();

	/**
	 * A hashmap that relates {@link CheckProvider} typical CheckChangedListeners to grid typical
	 * {@link SelectionChangedListener}
	 */
	private Map<CheckChangedListener<M>, SelectionChangedListener<M>> mListeners = 
		new HashMap<CheckChangedListener<M>, SelectionChangedListener<M>>();

	/**
	 * Unique constructor.
	 * @param store
	 * @param cm
	 */
	public CheckProviderGrid(ListStore<M> store, ColumnModel cm) {
		super(store, cm);
		List<ColumnConfig> lcc = cm.getColumns();
		lcc.add(0, sm.getColumn());
		addPlugin(sm);
		sm.setSelectionMode(SelectionMode.MULTI);
		setSelectionModel(sm);
	}

	CheckProviderGrid() {
		super();
	}

	@Override
	public CheckBoxSelectionModel<M> getSelectionModel() {
		return sm;
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.event
	 * .CheckProvider#addCheckListener(com.extjs.gxt.ui.client.event.CheckChangedListener)
	 */
	/**
	 * <br/>
	 */
	public void addCheckListener(final CheckChangedListener<M> listener) {
		SelectionChangedListener<M> lsc = new SelectionChangedListener<M>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<M> se) {
				CheckChangedEvent<M> event = new CheckChangedEvent<M>(CheckProviderGrid.this, getCheckedSelection());
				listener.checkChanged(event);
			}
		};
		mListeners.put(listener, lsc);
		sm.addSelectionChangedListener(lsc);
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.event.CheckProvider#getCheckedSelection()
	 */
	public List<M> getCheckedSelection() {
		return sm.getSelectedItems();
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.event.CheckProvider#isChecked(com.extjs.gxt.ui.client.data.ModelData)
	 */
	public boolean isChecked(M model) {
		return sm.isSelected(model);
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.event
	 * .CheckProvider#removeCheckListener(com.extjs.gxt.ui.client.event.CheckChangedListener)
	 */
	public void removeCheckListener(CheckChangedListener<M> listener) {
		if (!mListeners.containsKey(listener)) {
			return;
		}
		mListeners.remove(listener);
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.event.CheckProvider#setCheckedSelection(java.util.List)
	 */
	public void setCheckedSelection(List<M> selection) {
		sm.setSelection(selection);
	}
}
