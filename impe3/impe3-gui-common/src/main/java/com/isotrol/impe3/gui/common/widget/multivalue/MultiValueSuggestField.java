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
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.isotrol.impe3.gui.common.util.Util;


/**
 * @author Manuel Ruiz
 * 
 */
public class MultiValueSuggestField extends AMultiValueField {

	/**
	 * The suggest list<br/>
	 */
	private MultiWordSuggestOracle suggestList;
	
	/**
	 * the source field
	 */
	private SuggestBox suggestBox;
	
	/**
	 * Constructor
	 */
	public MultiValueSuggestField(List<String> list) {
		suggestList = new MultiWordSuggestOracle();
		for (int i = 0; i < list.size(); ++i) {
			suggestList.add(list.get(i));
		}
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget.multivalue
	 * .AMultiValueField#addSourceField(com.extjs.gxt.ui.client.widget.LayoutContainer, 
	 * 									com.extjs.gxt.ui.client.widget.LayoutContainer)
	 */
	@Override
	protected void addSourceField(final LayoutContainer lc, LayoutContainer rc) {
		lc.setLayout(new ColumnLayout());
		// a void label for the suggest box.
		Text label = new Text(getFieldLabel());
		label.addStyleName("generic-label");
		label.setWidth(getLabelWidth());
		lc.add(label);
		// the suggest box.
		suggestBox = new SuggestBox(suggestList);
		suggestBox.addStyleName("x-form-field");
		suggestBox.addStyleName("x-form-text");
		suggestBox.addStyleName("x-form-item");
		lc.add(suggestBox);
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
		String v = model.get(getDisplayProperty());
		suggestBox.setValue(v);
	}
	
	/** (non-Javadoc)
	 * @see com.isotrol.merlin3.gui.common.widget.multivalue.AMultiValueField#resetSource()
	 */
	@Override
	protected void resetSource() {
		suggestBox.setValue("");
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.gui.common.widget.AMultiValueField#maybeAddSourceValues()
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void maybeAddSourceValues() {
		String rawValue = this.suggestBox.getValue();
		String [] values = rawValue.split(",");
		for (String value : values) {
			if (!Util.emptyString(value) && !getValues().contains(value)) {
				ModelData model = new BaseModelData();
				model.set(getDisplayProperty(), value.trim());
				addValue(model);
			}
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

}
