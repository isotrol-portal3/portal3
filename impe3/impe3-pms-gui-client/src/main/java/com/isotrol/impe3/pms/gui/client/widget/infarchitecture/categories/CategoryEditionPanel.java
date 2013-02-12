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
import com.isotrol.impe3.pms.api.category.CategoryDTO;


/**
 * Creates the category edition form.
 * 
 * @author Manuel Ruiz
 * 
 */
public class CategoryEditionPanel extends ACategoryDetailsEditor {

	/**
	 * Constructor
	 */
	public CategoryEditionPanel() {
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories
	 * .ACategoryDetailsEditor#getAcceptButtonListener()
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
							trySaveCategory();
						}
					}
				};
				MessageBox.confirm(getPmsMessages().headerConfirmUpdateCategory(),
					getPmsMessages().msgConfirmUpdateCategory(), mbListener).setModal(true);
			}
		};
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.ACategoryDetailsEditor#getHeadingText()
	 */
	protected String getHeadingText() {
		return getPmsMessages().headerCategoryEditWindow() + ": " + getCategory().getDefaultName().getDisplayName();
	}

	/**
	 * fired when user confirmed that he wants to commit the current Category changes.<br/>
	 */
	private void trySaveCategory() {
		getUtilities().mask(getPmsMessages().mskSaveCategory());

		CategoryDTO cDto = getCategory();

		String sName = getTfName().getValue();
		String sPath = getTfPath().getValue();
		NameDTO defaultName = new NameDTO(sName, sPath);
		cDto.setDefaultName(defaultName);

		cDto.setLocalizedNames(getLocalizedNames());

		cDto.setVisible(getCbVisible().getValue());
		cDto.setRoutable(getCbRoutable().getValue());

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

		getCategoriesService().update(cDto, callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true;
	}

}
