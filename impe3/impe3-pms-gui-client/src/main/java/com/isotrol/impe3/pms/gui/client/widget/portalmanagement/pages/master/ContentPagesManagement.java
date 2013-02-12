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


import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentPageModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.ContentPageTreeStore;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.ContentPageDetailPanel;


/**
 * Management of Content Pages
 * 
 * @author Manuel Ruiz
 * 
 */
public class ContentPagesManagement extends AbstractTreePagesManagement<ContentPageModelData, ContentPageDetailPanel> {

	/**
	 * Height for the window that manages the pages associated to Categories and Contents<br/>
	 */
	private static final int CAT_WINDOW_HEIGHT = 600;

	/**
	 * Width for the window that manages the pages associated to Categories and Contents<br/><br/>
	 */
	private static final int CAT_WINDOW_WIDTH = 800;

	/**
	 * "Associate Category" tool item.<br/>
	 */
	private Button miCategory = null;

	/**
	 * Default constructor.
	 */
	@Inject
	public ContentPagesManagement() {
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #createDetailPanel(PageTemplateDTO, ModelData)
	 */
	@Override
	protected ContentPageDetailPanel createDetailPanel(PageTemplateDTO ptDto, ContentPageModelData model) {
		ContentPageDetailPanel contentPageDetailPanel = PmsFactory.getInstance().getContentPageDetailPanel();
		contentPageDetailPanel.setLayoutButton(getCurrentDevice().getDTO().isLayout());
		contentPageDetailPanel.init(ptDto, model.getDTO().getContentType());
		return contentPageDetailPanel;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractTreePagesManagement#getPageClass()
	 */
	@Override
	protected PageClass getPageClass() {
		return PageClass.CONTENT;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #getPageLoc(ModelData)
	 */
	@Override
	protected PageLoc getPageLoc(ContentPageModelData model) {

		PageLoc pageLoc = null;

		if (model.isDefaultPage()) {
			pageLoc = getDefaultPage().getValue();
		} else {
			pageLoc = model.getDTO().getPage().getValue();
		}

		return pageLoc;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #addSpecificToolItems()
	 */
	@Override
	protected void addSpecificToolItems() {
		addMiCategory();
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #tryGetPages()
	 */
	@Override
	protected void tryGetPages() {
		getUtil().mask(getPmsMessages().mskContentTypePages());

		AsyncCallback<ContentPagesDTO> callback = new AsyncCallback<ContentPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorRetrieveContentPages());
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

		getPagesService().getContentPages(getPortalPagesLoc(), callback);
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #getPropertyToDisplay()
	 */
	@Override
	protected String getPropertyToDisplay() {
		return ContentPageModelData.PROPERTY_NAME;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #mustDisableContextCommonButtons (ModelData)
	 */
	@Override
	protected boolean mustDisableContextCommonButtons(ContentPageModelData model) {
		return model == null || model.getDTO().getPage() == null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #mustDisableMiNew(com.isotrol.impe3.pms.gui.client.data.DTOModelData)
	 */
	@Override
	protected boolean mustDisableMiNew(ContentPageModelData model) {
		return model == null || model.getDTO().getPage() != null;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #enableDisableSpecificToolItems(com.isotrol.impe3.pms.gui.common.data.model.DTOModelData)
	 */
	@Override
	protected void enableDisableSpecificToolItems(ContentPageModelData model) {
		boolean enabled = false;
		if (model != null && !model.isDefaultPage()) {
			enabled = true;
		}
		miCategory.setEnabled(enabled);
	}

	/**
	 * Requests the service for the category content pages.<br/> On success opensthe category content pages window.
	 * 
	 * @param dto the content page dto.
	 */
	protected void tryGetPages(final ContentPageDTO dto) {
		getUtil().mask(getPmsMessages().mskContentTypePages());

		String contentId = dto.getContentType().getId();

		AsyncCallback<CategoryPagesDTO> callback = new AsyncCallback<CategoryPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(),
					getPmsMessages().msgErrorRetrieveCategoryContentPages());
			}

			public void onSuccess(CategoryPagesDTO pages) {
				openCategoryContentPagesWindow(dto, pages);
				getUtil().unmask();
			}
		};

		getPagesService().getCategoryContentPages(getPortalPagesLoc(), contentId, callback);
	}

	/**
	 * Opens the category pages management window.<br/>
	 * 
	 * @param dto
	 * @param pages
	 */
	private void openCategoryContentPagesWindow(ContentPageDTO dto, CategoryPagesDTO pages) {
		Window catWindow = new Window();
		catWindow.setSize(CAT_WINDOW_WIDTH, CAT_WINDOW_HEIGHT);
		catWindow.setShadow(false);
		catWindow.setModal(true);
		catWindow.setLayout(new FitLayout());
		CategoryContentPagesManagement catPages = PmsFactory.getInstance().getCategoryContentPagesManagement();
		catPages.init(dto, getPortalPagesLoc(), pages);
		catWindow.add(catPages);
		catWindow.show();
	}

	// /** (non-Javadoc)
	// * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	// * #clearItem(com.extjs.gxt.ui.client.widget.tree.TreeItem)
	// */
	// @Override
	// protected void clearItem(TreeItem item) {
	// super.clearItem(item);
	//		
	// ContentPageModelData cpModelData = (ContentPageModelData) item
	// .getModel();
	// // String nodeName = cpModelData.get(ContentPageModelData.PROPERTY_NAME);
	// ContentPageModelData newModel = null;
	// ContentPageTreeStore store = (ContentPageTreeStore) getStore();
	// if (cpModelData.isDefaultPage()) {
	// newModel = store.createDefaultPageModelData(null);
	// ComponentHelper.setModel(item, newModel);
	// } else {
	// cpModelData.getDTO().setPage(null);
	// }
	// }

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractTreePagesManagement
	 * #clearItem(com.extjs.gxt.ui.client.widget.tree.TreeItem)
	 */
	@Override
	protected void clearItem(ContentPageModelData item) {
		super.clearItem(item);
		
		if (!item.isDefaultPage()) {
			item.getDTO().setPage(null);
		}
	}

	/**
	 * Adds an "Associate Category" menu item to the toolbar and disables it.<br/>
	 */
	protected final void addMiCategory() {

		miCategory = new Button(getPmsMessages().labelAssociateCategory());
		miCategory.setIconStyle(getPmsStyles().menuIconCategories());

		miCategory.disable();

		ToolBar toolBar = getToolBarSupport().getToolBar();
		toolBar.insert(miCategory, 6);
		toolBar.insert(new SeparatorToolItem(), 7);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractTreePagesManagement#addListeners()
	 */
	@Override
	protected void addListeners() {
		// add all superclass listeners
		super.addListeners();

		miCategory.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				ContentPageModelData selected = getPagesTree().getSelectionModel().getSelectedItem();
				if (selected != null) {
					tryGetPages(selected.getDTO());
				}
			}
		});
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages
	 * .AbstractTreePagesManagement#noPage(com.isotrol.impe3.pms.gui.common.data.model.DTOModelData)
	 */
	@Override
	protected boolean shouldBeDisabled(ContentPageModelData model) {
		return model == null || model.getDTO().getPage() == null;
	}

	@Override
	protected boolean isInheritedPage(ContentPageModelData model) {
		if (model != null && model.getDTO().getPage() != null) {
			return model.getDTO().getPage().isInherited();
		}
		return false;
	}

	@Override
	protected void addSpecificColumnConfigs(List<ColumnConfig> columns) {
		// page defined?
		ColumnConfig config = new ColumnConfig();
		config.setId(ContentPageModelData.PROPERTY_WITH_CATEGORY);
		config.setFixed(true);
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setRenderer(new GridCellRenderer<ContentPageModelData>() {
			public Object render(ContentPageModelData model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<ContentPageModelData> store, Grid<ContentPageModelData> grid) {
				if ((Boolean) model.get(property)) {
					String title = getPmsMessages().ttContentCategoryPageDefined();
					return "<img src='img/folder_page.gif' title='" + title + "'>";
				} else {
					return "";
				}
			}
		});
		columns.add(0, config);
	}
}
