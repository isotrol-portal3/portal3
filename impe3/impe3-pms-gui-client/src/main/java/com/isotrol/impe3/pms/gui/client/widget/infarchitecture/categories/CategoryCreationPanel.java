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
package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.category.CategoryDTO;


/**
 * @author Andrei Cojocaru
 * 
 */
public class CategoryCreationPanel extends ACategoryDetailsEditor {

	/**
	 * ID of the parent category.<br/>
	 */
	private String parentId = null;

	/**
	 * ¿¿??<br/>
	 */
	private int order = 1000;

	/**
	 * Default no-args constructor<br/>
	 * 
	 * @param cat
	 * @param op
	 */
	public CategoryCreationPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are set.
	 * @param cat bound Category
	 * @param id parent unique ID
	 * @param o ¿¿??
	 */
	public void init(CategoryDTO cat, String id, int o) {
		this.parentId = id;
		this.order = o;
		super.init(cat);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories
	 * .CategoryEditionPanel#getAcceptButtonListener()
	 */
	@Override
	protected SelectionListener<ButtonEvent> getAcceptButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				// pedir confirmacion:
				Listener<MessageBoxEvent> mbListener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button b = we.getButtonClicked();
						if (b.getItemId().equals(Dialog.YES)) {
							// confirmado:
							trySaveNewCategory();
						}
					}
				};
				MessageBox.confirm(getPmsMessages().headerConfirmCreateNewCategory(),
					getPmsMessages().msgConfirmCreateNewCategory(), mbListener).setModal(true);
			}
		};
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryEditionPanel#getHeadingText()
	 */
	@Override
	protected String getHeadingText() {
		return getPmsMessages().headerCategoryCreationWindow();
	}

	/**
	 * fired when user confirmed that he wants to create the current Category.<br/>
	 */
	private void trySaveNewCategory() {
		getUtilities().mask(getPmsMessages().mskSaveCategory());

		CategoryDTO category = new CategoryDTO();
		category.setState(State.NEW);
		category.setRoutable(getCbRoutable().getValue());
		category.setVisible(getCbVisible().getValue());

		String sName = getTfName().getValue();
		String sPath = getTfPath().getValue();
		NameDTO defaultName = new NameDTO(sName, sPath);
		category.setDefaultName(defaultName);

		category.setLocalizedNames(getLocalizedNames());

		AsyncCallback<CategoryDTO> callback = new AsyncCallback<CategoryDTO>() {
			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getErrorProcessor().processError(arg0, getEmrCategories(), getPmsMessages().msgErrorSaveCategory());
			}

			public void onSuccess(CategoryDTO arg0) {
				hide();
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSaveCategory());
			}
		};

		getCategoriesService().create(category, parentId, order, callback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return false;
	}

}
