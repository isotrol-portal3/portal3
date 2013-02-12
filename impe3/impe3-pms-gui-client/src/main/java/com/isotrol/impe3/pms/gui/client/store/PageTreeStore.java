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


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.PageSelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * Tree store for a list of pages
 * @author Manuel Ruiz
 */
public class PageTreeStore extends TreeStore<PageSelModelData> {

	/**
	 * PMS messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;
	
	/**
	 * Constructor
	 */
	public PageTreeStore() {}
	
	/**
	 * Adds the nodes for each page class. Must be called after dependencies injection.
	 * @param pages
	 * @return an instance of <code>this</code>
	 */
	public PageTreeStore init(Map<PageClass, List<PageSelDTO>> pages) {
		List<PageSelDTO> contentPages = pages.get(PageClass.CONTENT);
		List<PageSelDTO> errorPages = pages.get(PageClass.ERROR);
		List<PageSelDTO> categoryNavigationPages = pages.get(PageClass.CATEGORY);
		List<PageSelDTO> tagNavigationPages = pages.get(PageClass.TAG);
		List<PageSelDTO> specialPages = pages.get(PageClass.SPECIAL);
		List<PageSelDTO> templatePages = pages.get(PageClass.TEMPLATE);
		List<PageSelDTO> mainPage = pages.get(PageClass.MAIN);
		List<PageSelDTO> defaultPage = pages.get(PageClass.DEFAULT);
		List<PageSelDTO> contentListPages = pages.get(PageClass.CONTENT_TYPE);

		List<PageSelModelData> depth1 = new LinkedList<PageSelModelData>();
		addNode(mainPage, pmsMessages.nodeMainPage());
		addNode(defaultPage, pmsMessages.nodeDefaultPage());
		addNode(specialPages, pmsMessages.nodeSpecialPages());
		addNode(categoryNavigationPages, pmsMessages.nodeCategoryNavigationPages());
		addNode(tagNavigationPages, pmsMessages.nodeTagNavigationPages());
		addNode(contentPages, pmsMessages.nodeContentTypePages());
		addNode(contentListPages, pmsMessages.nodeContentListPages());
		addNode(templatePages, pmsMessages.nodeTemplatePages());
		addNode(errorPages, pmsMessages.nodeErrorPages());

		add(depth1, true);
		
		return this;
	}

	private void addNode(List<PageSelDTO> pages, String name) {
		PageSelDTO dto = new PageSelDTO();
		dto.setName(name);
		PageSelModelData pageType = new PageSelModelData(dto);
		
		String nodeDefaultPage = pmsMessages.nodeDefaultPage();
		String nodeMainPage = pmsMessages.nodeMainPage();
		
		// the root nodes are folders, except main and default page
		if (name.equals(nodeDefaultPage) || name.equals(nodeMainPage)) {
			if (!pages.isEmpty()) {
				pageType = new PageSelModelData(pages.get(0));
			}
		} else {
			pageType.setPage(false);
		}
		add(pageType, false);

		if (pages != null && !name.equals(nodeDefaultPage) && !name.equals(nodeMainPage)) {
			for (PageSelDTO p : pages) {
				PageSelModelData page = new PageSelModelData(p);
				add(pageType, page, false);
			}
		}
	}
	
	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
}
