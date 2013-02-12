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

package com.isotrol.impe3.gui.common.widget.multivalue;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.isotrol.impe3.gui.common.util.Util;


/**
 * @author Manuel Ruiz
 * 
 */
public class MultiValueTextField extends AMultiValueField {

	/**
	 * The values editor.<br/>
	 */
	private TextField<String> sourceField;

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget.multivalue
	 * .AMultiValueField#addSourceField(com.extjs.gxt.ui.client.widget.LayoutContainer, 
	 * 									com.extjs.gxt.ui.client.widget.LayoutContainer)
	 */
	@Override
	protected void addSourceField(LayoutContainer lc, LayoutContainer rc) {
		sourceField  = new TextField<String>();
		sourceField.setFieldLabel(getFieldLabel());
		lc.add(sourceField);
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget.AMultiValueField#maybeAddSourceValue()
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void maybeAddSourceValues() {
		String text = sourceField.getValue();
		if (!Util.emptyString(text)) {
			sourceField.setValue("");
			ModelData data = new BaseModelData();
			data.set(getDisplayProperty(), text);
			addValue(data);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget.multivalue.AMultiValueField#getValues()
	 */
	/**
	 * <br/>
	 */
	@Override
	public List<String> getValues() {
		
		ListStore<ModelData> store = getListView().getStore();
		
		List<String> res = new LinkedList<String>();
		for(ModelData model : store.getModels()) {
			res.add((String) model.get(getDisplayProperty()));
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget
	 * .AMultiValueField#setSourceValue(com.extjs.gxt.ui.client.data.ModelData)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void setSourceValue(ModelData model) {
		String value = model.get(getDisplayProperty());
		sourceField.setValue(value);
	}
	
	/** (non-Javadoc)
	 * @see com.isotrol.merlin3.gui.common.widget.multivalue.AMultiValueField#resetSource()
	 */
	@Override
	protected void resetSource() {
		sourceField.setValue("");
	}

}
