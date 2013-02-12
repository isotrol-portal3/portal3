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

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.ContentPageDTO;
import com.isotrol.impe3.pms.api.page.ContentPagesDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentPageModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;


/**
 * Tree store for content pages and lists of content pages
 * 
 * @author Manuel Ruiz
 */
public class ContentPageTreeStore extends TreeStore<ContentPageModelData> {

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Default constructor.<br/>
	 */
	public ContentPageTreeStore() {
		setSortInfo(new SortInfo(ContentPageModelData.PROPERTY_NAME, SortDir.ASC));
	}

	/**
	 * Add the nodes for each page class
	 * @param pages
	 */
	public void initPageStore(ContentPagesDTO pages) {

		ContentPageModelData root = createDefaultPageModelData(pages.getDefaultPage());

		add(root, false);
		List<ContentPageDTO> children = pages.getChildren();
		load(root, children);
	}

	/**
	 * Loads the children of the passed tree node.<br/>
	 * @param root parent node for the passed subtree.
	 * @param children subtree for the passed parent.
	 */
	private void load(ContentPageModelData root, List<ContentPageDTO> children) {
		if (children == null || children.isEmpty()) {
			return;
		}

		for (final ContentPageDTO child : children) {
			final ContentPageModelData model = new ContentPageModelData(child);

			add(root, model, false);
		}
	}

	/**
	 * Creates the tree root ModelData, which represents the default page.<br/>
	 * @param defaultPage the default page data. May be <code>null</code>.
	 * @return
	 */
	public ContentPageModelData createDefaultPageModelData(Inherited<PageLoc> defaultPage) {
		
		// PROPERTY_NAME is exposed through associated ContentTypeSelDTO name
		ContentTypeSelDTO ctsDto = new ContentTypeSelDTO();
		ctsDto.setName(pmsMessages.nodeDefaultPage());
		
		ContentPageDTO pageDto = new ContentPageDTO();
		pageDto.setContentType(ctsDto);
		pageDto.setPage(defaultPage);
		
		ContentPageModelData defaultModelData = new ContentPageModelData(pageDto);
		defaultModelData.setDefaultPage(true);
		
		return defaultModelData;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}
	
	/**
	 * Injets the alphabetic store sorter.<br/>
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		super.setStoreSorter((StoreSorter)storeSorter);
	}
}
