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

package com.isotrol.impe3.gui.common.widget;



import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;


/**
 * A component used for search. Consists of a horizontal panel with a text field for the search string,
 * and an icon button to fire the search. The button logic must be set configuring a SelectionListener
 * for the component (see {@link #addSelectionListener(SelectionListener)}).
 * 
 * @author Andrei Cojocaru
 *
 */
public class SearchField extends HorizontalPanel {

	/**
	 * Query string field.<br/>
	 */
	private TextField<String> textField = null;

	/**
	 * Search button.<br/>
	 */
	private IconButton bSearch = null;
	
	/**
	 * if the empty search is enabled
	 */
	private boolean enabledEmptySearch = true;
	
	/**
	 * Injected constructor.
	 */
	@Inject
	public SearchField(GuiCommonMessages messages, GuiCommonStyles styles, Buttons buttons) {
		
		textField = new TextField<String>();
		textField.addKeyListener(new KeyListener() {
			@Override
			public void componentKeyUp(ComponentEvent event) {
				boolean enableSearch = true;
				if (Util.emptyString(textField.getValue()) && !enabledEmptySearch) {
					enableSearch = false;
				} else {
					enableSearch = true;
				}
				bSearch.setEnabled(enableSearch);
			}
		});
		add(textField);
		
		bSearch  = buttons.createGenericIconButton(messages.btSearch(), styles.iSearch(), null);
		bSearch.addStyleName(styles.marginLeft4px());
		add(bSearch);
	}

	/**
	 * Adds the passed {@link SelectionListener} to the "Search" button.<br/>
	 * @param listener
	 */
	public void addSelectionListener(SelectionListener<IconButtonEvent> listener) {
		bSearch.addSelectionListener(listener);
	}
	
	/**
	 * Add a key listener to the text field
	 * @param keyListener
	 */
	public void addKeyListener(KeyListener keyListener) {
		textField.addKeyListener(keyListener);
	}
	
	/**
	 * <br/>
	 * @return
	 */
	public String getSearchString() {
		return textField.getValue();
	}

	/**
	 * @param enabledEmptySearch the enabledEmptySearch to set
	 */
	public void setEnabledEmptySearch(boolean enabledEmptySearch) {
		this.enabledEmptySearch = enabledEmptySearch;
	}
	
}
