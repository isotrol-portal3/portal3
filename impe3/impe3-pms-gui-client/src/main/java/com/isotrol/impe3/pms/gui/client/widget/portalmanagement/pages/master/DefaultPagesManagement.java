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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master;


import java.util.Arrays;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.DefaultPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.InheritedPageSelModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ToolbarSupport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.PageDetailPanel;


/**
 * Manages the special pages
 * 
 * @author Manuel Ruiz
 * 
 */
public class DefaultPagesManagement extends AbstractGridPagesManagement {

	/**
	 * Default constructor.
	 */
	public DefaultPagesManagement() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#tryGetPages()
	 */
	@Override
	protected final void tryGetPages() {
		getUtil().mask(getPmsMessages().mskDefaultPages());

		AsyncCallback<DefaultPagesDTO> callback = new AsyncCallback<DefaultPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorRetrieveDefaultPages());
			}

			public void onSuccess(DefaultPagesDTO pages) {
				storeDefaultPages(pages);
				getUtil().unmask();
			}
		};

		getPagesService().getDefaultPages(getPortalPagesLoc(), callback);
	}

	/**
	 * Does not remove the ModelData from grid store. There is always an entry for Default Page and another one for Main
	 * Page (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement
	 * #onPageRemove(java.lang.String)
	 */
	@Override
	protected final void onPageRemove(String pageId) {
		Grid<InheritedPageSelModelData> theGrid = getGrid();
		InheritedPageSelModelData model = theGrid.getStore().findModel(InheritedPageSelModelData.PROPERTY_ID, pageId);
		if (model != null) {
			model.getDTO().getValue().setId(null);
			theGrid.getView().refresh(false);
			controlEnabledButtons(theGrid.getSelectionModel().getSelectedItem());
		}
	}

	/**
	 * Populates the grid store with the passed default pages info.<br/>
	 * 
	 * @param pages
	 */
	private void storeDefaultPages(DefaultPagesDTO pages) {
		getToolBarSupport().enableContextCommonToolItems(false, false, false);
		enableCreateButton(false);

		Inherited<PageLoc> mainPageLoc = pages.getMainPage();
		Inherited<PageLoc> defaultPageLoc = pages.getDefaultPage();
		Inherited<PageLoc> errPageLoc = pages.getDefaultErrorPage();

		// main page
		Inherited<PageSelDTO> mainDto = new Inherited<PageSelDTO>();
		PageSelDTO mainPageDto = new PageSelDTO();
		mainDto.setValue(mainPageDto);
		if (mainPageLoc != null) {
			mainDto.setInherited(mainPageLoc.isInherited());
			mainPageDto.setId(mainPageLoc.getValue().getId());
		}
		mainDto.getValue().setName(getPmsMessages().nodeMainPage());
		InheritedPageSelModelData mainModelData = new InheritedPageSelModelData(mainDto);

		// default page
		Inherited<PageSelDTO> defDto = new Inherited<PageSelDTO>();
		PageSelDTO defPageDto = new PageSelDTO();
		defDto.setValue(defPageDto);
		defDto.getValue().setName(getPmsMessages().nodeDefaultPage());
		if (defaultPageLoc != null) {
			defDto.setInherited(defDto.isInherited());
			defPageDto.setId(defaultPageLoc.getValue().getId());
		}
		InheritedPageSelModelData defModelData = new InheritedPageSelModelData(defDto);

		// default error page
		Inherited<PageSelDTO> errDto = new Inherited<PageSelDTO>();
		PageSelDTO errPageDto = new PageSelDTO();
		// name in default error page must be null
		errPageDto.setName(null);
		errDto.setValue(errPageDto);
		if (errPageLoc != null) {
			errDto.setInherited(errPageLoc.isInherited());
			errPageDto.setId(errPageLoc.getValue().getId());
		}
		InheritedPageSelModelData errModelData = new InheritedPageSelModelData(errDto);

		ListStore<InheritedPageSelModelData> store = getStore();
		store.removeAll();
		store.add(Arrays.asList(new InheritedPageSelModelData[] {mainModelData, defModelData, errModelData}));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#getPageClass()
	 */
	@Override
	protected final PageClass getPageClass() {

		InheritedPageSelModelData pageSelected = getGrid().getSelectionModel().getSelectedItem();

		if (pageSelected != null) {
			String pageName = pageSelected.getDTO().getValue().getName();
			if (pageName == null) {
				// is the default error page
				return PageClass.ERROR;
			} else if (pageName.equals(getPmsMessages().nodeMainPage())) {
				return PageClass.MAIN;
			} else if (pageName.equals(getPmsMessages().nodeDefaultPage())) {
				return PageClass.DEFAULT;
			}
		}

		return PageClass.DEFAULT;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#getManagedPageClass()
	 */
	@Override
	protected final PageClass getManagedPageClass() {
		return PageClass.DEFAULT;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.APagesManagement#configToolBarItems()
	 */
	@Override
	protected final void controlEnabledButtons(InheritedPageSelModelData selectedItem) {
		ToolbarSupport tbSupport = getToolBarSupport();
		if (selectedItem != null) {
			boolean dtoSelected = true;
			if (selectedItem.getDTO().getValue().getId() == null) {
				dtoSelected = false;
			}
			tbSupport.enableContextCommonToolItems(dtoSelected, selectedItem.getDTO().isInherited(), getCurrentDevice().getDTO().isLayout());
			enableCreateButton(!dtoSelected);
		}
	}

	/**
	 * Enables or disables the "Add" button, according to the passed boolean value.<br/>
	 * 
	 * @param enabled
	 */
	private void enableCreateButton(boolean enabled) {
		getToolBarSupport().getMiNewPage().setEnabled(enabled);
	}

	/**
	 * @return the page detail panel
	 */
	@Override
	protected PageDetailPanel createDetailPanel() {
		return PmsFactory.getInstance().getDefaultPageDetailPanel();
	}
}
