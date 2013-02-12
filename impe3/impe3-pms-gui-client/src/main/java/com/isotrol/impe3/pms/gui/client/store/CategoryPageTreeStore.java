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

package com.isotrol.impe3.pms.gui.client.store;

import java.util.List;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.page.CategoryPageDTO;
import com.isotrol.impe3.pms.api.page.CategoryPagesDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.gui.client.data.impl.CategoryPageModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

/**
 * Tree store for category pages.
 * 
 * @author Manuel Ruiz
 */
public class CategoryPageTreeStore extends TreeStore<CategoryPageModelData> {

	/**
	 * The root ModelData.<br/>
	 */
	private CategoryPageModelData root = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Constructor
	 * @param pmsMessages PMS messages bundle.
	 */
	@Inject
	public CategoryPageTreeStore(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Add the nodes for each page class
	 * 
	 * @param pages
	 */
	public void initPageStore(CategoryPagesDTO pages) {

		root = createDefaultPageModelData(pages.getDefaultPage());

		add(root, false);
		List<CategoryPageDTO> children = pages.getChildren();
		load(root, children);
	}

	/**
	 * Creates a CategoryPageModelData for the default page which exposes these properties:<ul>
	 * <li><code>PROPERTY_ID</code>, like any ModelData property</li>
	 * <li><code>default</code>, through isDefaultPage() method</li>
	 * <li><code>page</code>, through isPage() method</li>
	 * </ul>
	 * <br/>
	 * @param defaultPage the default page PageLoc structure. May be <code>null</code>.
	 * @return
	 */
	private CategoryPageModelData createDefaultPageModelData(Inherited<PageLoc> defaultPage) {
		CategoryPageDTO pageDto = new CategoryPageDTO();
		pageDto.setOnlyThis(defaultPage);	// PROPERTY_ID is exposed through "onlyThis"
		
		CategorySelDTO catSel = new CategorySelDTO();
		catSel.setName(pmsMessages.nodeDefaultPage());
		pageDto.setCategory(catSel);
		
		CategoryPageModelData defaultModelData = createMockCategoryPage(pmsMessages.nodeDefaultPage(), pageDto, null);
		defaultModelData.setDefaultPage(true);
		defaultModelData.setPage(true);
		return defaultModelData;
	}

	/**
	 * Loads the children of the passed tree node.<br/>
	 * 
	 * @param parentPageModel
	 *            parent node for the passed subtree.
	 * @param children
	 *            subtree for the passed parent.
	 */
	private void load(CategoryPageModelData parentPageModel, List<CategoryPageDTO> children) {
		if (children == null || children.isEmpty()) {
			return;
		}

		for (CategoryPageDTO child : children) {
			final CategoryPageModelData model = new
			CategoryPageModelData(child);
//			CategoryPageModelData model = new CategoryPageModelData(
//					getNullCategoryPageDTO(child.getCategory().getName()));
			model.setPage(false);

			add(parentPageModel, model, false);
			addPageChildren(model, child);
			load(model, child.getChildren());
		}
	}

	private void addPageChildren(CategoryPageModelData parentModel, CategoryPageDTO pageDto) {

		CategoryPageDTO usedModel = null;
		CategorySelDTO csDto = pageDto.getCategory();
		if (pageDto.getOnlyThis() != null) { // is only this - let's pass it to the model node
			usedModel = pageDto;
		}
		// NODE_ONLY_THIS_CATEGORY, parentModel.getDTO());
		CategoryPageModelData onlyThis = createMockCategoryPage(pmsMessages.nodeOnlyThisCategory(), usedModel, csDto);
		
		onlyThis.setOnlyThis(true);

		usedModel = null;
		if (pageDto.getThisAndChildren() != null) {
			usedModel = pageDto;
		}
		// NODE_THIS_AND_CHILDREN, parentModel.getDTO());
		CategoryPageModelData thisAndChildren = createMockCategoryPage(
				pmsMessages.nodeThisCategoryHierarchy(), usedModel, csDto);
		thisAndChildren.setThisAndChildren(true);
		add(parentModel, onlyThis, false);
		add(parentModel, thisAndChildren, false);
	}

	private CategoryPageModelData createMockCategoryPage(String name, CategoryPageDTO categoryPageDTO, 
			CategorySelDTO categorySelDTO) {

		CategoryPageDTO pageDto = null;

		if (categoryPageDTO == null) {
			pageDto = getNullCategoryPageDTO(name);
			pageDto.setCategory(categorySelDTO);
		} else {
			pageDto = categoryPageDTO;
		}
		
		CategoryPageModelData page = new CategoryPageModelData(pageDto);
		page.setPropertyToDisplay(name);
		page.setPage(true);

		return page;
	}

	/**
	 * Returns a CategoryPageDTO with the passed name.<br/>
	 * Used to mark tree items as actually having no category (but they must be
	 * displayed anyway).
	 * 
	 * @param name
	 * @return
	 */
	public static CategoryPageDTO getNullCategoryPageDTO(String name) {
		CategoryPageDTO pageDto = new CategoryPageDTO();
		CategorySelDTO catSel = new CategorySelDTO();
		catSel.setName(name);
		pageDto.setCategory(catSel);

		return pageDto;
	}

	/**
	 * Sets the root name
	 * 
	 * @param name
	 *            root name to set
	 */
	public void setRootName(String name) {
		root.getDTO().getCategory().setName(name);
		root.setPropertyToDisplay(name);
	}

}
