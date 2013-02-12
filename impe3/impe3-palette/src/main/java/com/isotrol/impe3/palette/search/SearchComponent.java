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

package com.isotrol.impe3.palette.search;


import java.util.List;

import net.sf.derquinse.lucis.Page;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.InjectRequest;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.api.support.PaginationSupport;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.support.listing.DefaultListingPage;


/**
 * Simple searcher implementation.
 * 
 * @author Emilio Escobar Reyero
 */
@RequiresLink(Pagination.class)
public class SearchComponent implements Component {

	private SearchModuleConfig moduleConfig;
	private SearchComponentConfig componentConfig;

	private Pagination pagination;

	private ContentLoader contentLoader;

	private NavigationKey navigationKey;

	private String param;

	private Listing<Content> listing;

	@Inject
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	@Inject
	public void setContentLoader(ContentLoader contentLoader) {
		this.contentLoader = contentLoader;
	}

	@Inject
	public void setComponentConfig(SearchComponentConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	@Autowired
	public void setModuleConfig(SearchModuleConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}

	@InjectRequest("q")
	public void setParam(String param) {
		this.param = param;
	}

	private void setPage(Page<Content> page) {
		if (page == null) {
			listing = null;
		} else {
			listing = new DefaultListingPage<Content>(page.getTotalHits(), pagination, page.getItems());
		}
	}

	private Pagination loadPagination(int defaultSize) {
		this.pagination = PaginationSupport.load(pagination, moduleConfig, defaultSize);
		return pagination;
	}

	@Extract
	public Pagination getPagination() {
		return listing != null ? listing.getPagination() : pagination;
	}

	@Extract
	public Listing<Content> getListing() {
		return listing;
	}

	public ComponentResponse execute() {
		final ContentCriteria criteria = contentLoader.newCriteria();
		pagination = loadPagination(Pagination.SIZE);

		if (componentConfig.navigation() && navigationKey != null) {
			if (navigationKey.isCategory()) {
				criteria.categories().apply(navigationKey.getCategory(), FilterType.REQUIRED);
			}
			if (navigationKey.isContentType()) {
				criteria.contentTypes().apply(navigationKey.getContentType(), FilterType.REQUIRED);
			}
			/*
			 * if (navigationKey.isCategory() && navigationKey.isContentType()) {
			 * criteria.category(navigationKey.getCategory()).contentType(navigationKey.getContentType()); } else if
			 * (navigationKey.isCategory() && !navigationKey.isContentType()) {
			 * criteria.category(navigationKey.getCategory()); } else if (!navigationKey.isCategory() &&
			 * navigationKey.isContentType()) {
			 * 
			 * }
			 */
		}

		final List<String> f = fields();
		if (param != null && !"".equals(param.trim()) && !f.isEmpty()) {
			criteria.must(NodeQueries.anyString(param, f));
		}

		setPage(criteria.getPage(pagination));

		return ComponentResponse.OK;
	}

	private List<String> fields() {
		final List<String> l = Lists.newArrayList();

		if (componentConfig.title()) {
			l.add(Schema.TITLE);
		}
		if (componentConfig.description()) {
			l.add(Schema.DESCRIPTION);
		}
		if (componentConfig.text()) {
			l.add(Schema.CONTENT_IDX);
		}

		return l;
	}

}
