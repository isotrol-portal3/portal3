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


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.page.CategoryPageDTO;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategoryPageModelData;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.CategoryPageTreeStore;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractSpecificPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.CategoryPageDetailPanel;


/**
 * Management of Categories Pages
 * 
 * @author Manuel Ruiz
 * 
 */
public class CategoryPagesManagement extends
	AbstractTreePagesManagement<CategoryPageModelData, AbstractSpecificPageDetailPanel> {

	/**
	 * Default constructor.
	 */
	@Inject
	public CategoryPagesManagement() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #createDetailPanel(com.isotrol.impe3.pms.api.page.PageTemplateDTO,
	 * com.isotrol.impe3.pms.gui.client.data.DTOModelData)
	 */
	@Override
	protected CategoryPageDetailPanel createDetailPanel(PageTemplateDTO ptDto, CategoryPageModelData model) {
		CategoryPageDetailPanel categoryPageDetailPanel = PmsFactory.getInstance().getCategoryPageDetailPanel();
		categoryPageDetailPanel.setLayoutButton(getCurrentDevice().getDTO().isLayout());
		categoryPageDetailPanel.init(ptDto, model);
		return categoryPageDetailPanel;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #mustDisableContextCommonButtons(com.isotrol.impe3.pms.gui.common.data.model.DTOModelData)
	 */
	@Override
	protected boolean mustDisableContextCommonButtons(CategoryPageModelData model) {
		if (model == null || !model.isPage()) { // no item selected
			return true;
		}
		return (model.isOnlyThis() && model.getDTO().getOnlyThis() == null)
			|| (model.isThisAndChildren() && model.getDTO().getThisAndChildren() == null)
			|| (model.isDefaultPage() && getDefaultPage() == null);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #mustDisableMiNew(com.isotrol.impe3.pms.gui.common.data.model.DTOModelData)
	 */
	@Override
	protected boolean mustDisableMiNew(CategoryPageModelData model) {
		if (model == null || !model.isPage()) {
			return true;
		}
		CategoryPageDTO dto = model.getDTO();
		// disable if already have something:
		return (model.isDefaultPage() && getDefaultPage() != null) || (model.isOnlyThis() && dto.getOnlyThis() != null)
			|| (model.isThisAndChildren() && dto.getThisAndChildren() != null);
	}

	@Override
	protected PageClass getPageClass() {

		return PageClass.CATEGORY;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractTreePagesManagement
	 * #getPageLoc(com.isotrol.impe3.pms.gui.common.data.model.DTOModelData)
	 */
	@Override
	protected PageLoc getPageLoc(CategoryPageModelData model) {

		PageLoc pageLoc = null;

		if (model.isDefaultPage()) {
			pageLoc = getDefaultPage().getValue();
		} else if (model.isOnlyThis()) {
			pageLoc = model.getDTO().getOnlyThis().getValue();
		} else if (model.isThisAndChildren()) {
			pageLoc = model.getDTO().getThisAndChildren().getValue();
		}

		return pageLoc;

	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master
	 * .AbstractTreePagesManagement#getPropertyToDisplay()
	 */
	@Override
	protected String getPropertyToDisplay() {
		return CategoryPageModelData.PROPERTY_TO_DISPLAY;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.AbstractPagesManagement#tryGetPages()
	 */
	@Override
	protected void tryGetPages() {
		getUtil().mask(getPmsMessages().mskCategoryPages());

		AsyncCallback<CategoryPagesDTO> callback = new AsyncCallback<CategoryPagesDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				getErrorProcessor().processError(arg0, getEmrPages(), getPmsMessages().msgErrorRetrieveCategoryPages());
			}

			public void onSuccess(CategoryPagesDTO pages) {
				getUtil().unmask();

				setDefaultPage(pages.getDefaultPage());

				CategoryPageTreeStore store = PmsFactory.getInstance().getCategoryPageTreeStore();

				List<String> lExpanded = new LinkedList<String>();
				if (getStore() != null) {
					for (CategoryPageModelData model : getStore().getRootItems()) {
						computeExpanded(getPagesTree(), model, lExpanded);
					}
				}

				store.initPageStore(pages);
				setStore(store);
				repopulatePagesTree();
				enableDisableToolItems();

				final List<String> expendedIds = lExpanded;
				
				// restore expanded. Expand at least depth 1 elements:
				getPagesTree().addListener(Events.ViewReady, new Listener<BaseEvent>() {
					public void handleEvent(BaseEvent be) {
						List<CategoryPageModelData> rootModels = getStore().getRootItems();
						for (CategoryPageModelData model : rootModels) {
							getPagesTree().setExpanded(model, true);
							// children nodes:
							for (CategoryPageModelData child : getStore().getChildren(model)) {
								maybeExpand(child, expendedIds);
							}
						}
					}
				});
			}
		};

		getPagesService().getCategoryPages(getPortalPagesLoc(), callback);
	}

	/**
	 * Recursively computes the passed model<br/>
	 * @param t
	 * @param m
	 * @param lExpanded
	 */
	private void computeExpanded(TreeGrid<CategoryPageModelData> t, CategoryPageModelData m, List<String> lExpanded) {
		if (t.isExpanded(m)) {
			lExpanded.add((String) m.getDTO().getCategoryId());
			List<CategoryPageModelData> lChildren = t.getTreeStore().getChildren(m);
			for (CategoryPageModelData child : lChildren) {
				computeExpanded(t, child, lExpanded);
			}
		}
	}

	/**
	 * Expands the passed model branches whose IDs are contained in the expanded list.
	 * 
	 * @param model
	 * @param lExpanded
	 */
	private void maybeExpand(CategoryPageModelData model, List<String> lExpanded) {
		if (lExpanded.contains(model.getDTO().getCategoryId())) {
			getPagesTree().setExpanded(model, true);
			for (CategoryPageModelData child : getPagesTree().getTreeStore().getChildren(model)) {
				maybeExpand(child, lExpanded);
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.AbstractTreePagesManagement
	 * #clearItem(com.extjs.gxt.ui.client.widget.tree.TreeItem)
	 */
	@Override
	protected void clearItem(CategoryPageModelData item) {
		// set "null" visually:
		super.clearItem(item);

		String nodeName = item.get(CategoryPageModelData.PROPERTY_TO_DISPLAY);
		// set "null" logically:
		CategoryPageModelData model = new CategoryPageModelData(CategoryPageTreeStore.getNullCategoryPageDTO(nodeName));
		model.setDefaultPage(item.isDefaultPage());
		model.setOnlyThis(item.isOnlyThis());
		model.setPage(item.isPage());
		model.setThisAndChildren(item.isThisAndChildren());
	}

	@Override
	protected boolean shouldBeDisabled(CategoryPageModelData item) {
		return (item == null || item.isDefaultPage() && item.getDTO().getOnlyThis() == null || item.isOnlyThis()
			&& item.getDTO().getOnlyThis() == null || item.isThisAndChildren()
			&& item.getDTO().getThisAndChildren() == null);
	}

	@Override
	protected boolean isInheritedPage(CategoryPageModelData model) {
		if (model != null && model.getDTO().getOnlyThis() != null && model.isOnlyThis()) {
			return model.getDTO().getOnlyThis().isInherited();
		} else if (model != null && model.getDTO().getThisAndChildren() != null && model.isThisAndChildren()) {
			return model.getDTO().getThisAndChildren().isInherited();
		}
		return false;
	}

	@Override
	protected void addSpecificColumnConfigs(List<ColumnConfig> columns) {
		
		// page defined?
		ColumnConfig config = new ColumnConfig();
		config.setId(CategoryPageModelData.PROPERTY_OWNPAGES);
		config.setFixed(true);
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setRenderer(new GridCellRenderer<CategoryPageModelData>() {
			public Object render(CategoryPageModelData model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<CategoryPageModelData> store, Grid<CategoryPageModelData> grid) {
				if ((Boolean) model.get(property) && !model.isPage()) {
					String title = getPmsMessages().ttCategoryPageDefined();
					return "<img src='img/folder_page.gif' title='" + title + "'>";
				} else {
					return "";
				}
			}
		});
		columns.add(0, config);

		// inherited page defined?
		config = new ColumnConfig();
		config.setId(CategoryPageModelData.PROPERTY_INHPAGES);
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setFixed(true);
		config.setRenderer(new GridCellRenderer<CategoryPageModelData>() {
			public Object render(CategoryPageModelData model, String property, ColumnData config, int rowIndex, int colIndex,
				ListStore<CategoryPageModelData> store, Grid<CategoryPageModelData> grid) {
				if ((Boolean) model.get(property) && !model.isPage()) {
					String title = getPmsMessages().ttCategoryInheritedPageDefined();
					return "<img src='img/folder_page_h.gif' title='" + title + "'>";
				} else {
					return "";
				}
			}
		});
		columns.add(1, config);
	}
}
