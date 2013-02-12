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


import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.ContentPageTreeStore;


/**
 * Management of Content-Type listing Pages (pages of content list)
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentTypePagesManagement extends ContentPagesManagement {

	/**
	 * Default constructor
	 */
	public ContentTypePagesManagement() {}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractTreePagesManagement#getPageClass()
	 */
	@Override
	protected PageClass getPageClass() {
		return PageClass.CONTENT_TYPE;
	}

	/**
	 * Requests the service for the Content Type listing pages<br/>
	 */
	@Override
	protected void tryGetPages() {
		getUtil().mask(getPmsMessages().mskContentTypeListingPages());

		AsyncCallback<ContentPagesDTO> callback = new AsyncCallback<ContentPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
					arg0, 
					getEmrPages(), 
					getPmsMessages().msgErrorRetrieveCategoryContentTypePages());
			}

			public void onSuccess(ContentPagesDTO pages) {
				setDefaultPage(pages.getDefaultPage());
				ContentPageTreeStore store = PmsFactory.getInstance().getContentPageTreeStore();
				store.initPageStore(pages);
				setStore(store);
				repopulatePagesTree();
				enableDisableToolItems();
				
				getUtil().unmask();
			}
		};

		getPagesService().getContentTypePages(getPortalPagesLoc(), callback);
	}

	/**
	 * Retrieves the pages listing by category and device<br/>
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentPagesManagement
	 * #tryGetPages(com.isotrol.impe3.pms.gui.client.data.impl.ContentPageModelData)
	 */
	@Override
	protected void tryGetPages(final ContentPageDTO dto) {
		getUtil().mask(getPmsMessages().mskContentTypeListingPages());

		String contentId = dto.getContentType().getId();

		AsyncCallback<CategoryPagesDTO> callback = new AsyncCallback<CategoryPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(
					arg0, 
					getEmrPages(), 
					getPmsMessages().msgErrorRetrieveCategoryContentPages());				
			}

			public void onSuccess(CategoryPagesDTO pages) {
				openCategoryContentPagesWindow(dto, pages);
				getUtil().unmask();
			}
		};

		getPagesService().getCategoryContentTypePages(getPortalPagesLoc(), contentId, callback);
	}

	/**
	 * Shows the Category Content Pages window.<br/>
	 * @param dto
	 * @param pages
	 */
	private void openCategoryContentPagesWindow(ContentPageDTO dto, CategoryPagesDTO pages) {
		Window catWindow = new Window();
		catWindow.setSize(800, 600);
		catWindow.setModal(true);
		catWindow.setLayout(new FitLayout());
		CategoryContentTypePagesManagement catPages = PmsFactory.getInstance().getCategoryContentTypePagesManagement();
		catPages.init(dto, getPortalPagesLoc(), pages);
		catWindow.add(catPages);
		catWindow.show();
	}
	
}
