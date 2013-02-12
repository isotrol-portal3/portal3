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

package com.isotrol.impe3.palette.content.page;


import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.RequiresLink;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.GroupItem;
import com.isotrol.impe3.api.content.Listing;
import com.isotrol.impe3.freemarker.FreeMarker;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.palette.content.AbstractVisualContentComponent;
import com.isotrol.impe3.support.listing.ContentListingPage;
import com.isotrol.impe3.support.listing.GroupedContentListingPage;


/**
 * Listing Page Component.
 * @author Andres Rodriguez
 */
@RequiresLink(Listing.class)
public class ListingPageComponent extends AbstractVisualContentComponent {
	/** Configuration. */
	private ListingPageModuleConfig config;
	/** Listing page. */
	private Listing<?> page = null;

	/**
	 * Public constructor.
	 */
	public ListingPageComponent() {
	}

	public void setConfig(ListingPageModuleConfig config) {
		this.config = config;
	}

	@Inject
	public void setPage(Listing<?> page) {
		this.page = page;
	}

	/**
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
		if (page == null) {
			final FileId file = config.sample();
			if (file != null) {
				final Integer requestedSize = config.size();
				final boolean group = (config != null && Boolean.TRUE.equals(config.groupPreview()));
				final Content content = FreeMarker.loadXMLContent(getFileLoader(), file, null, null);
				if (content != null) {
					final int size = Math.max(3, requestedSize == null ? 3 : requestedSize.intValue());
					final List<Content> list = Lists.newArrayListWithCapacity(size);
					for (int i = 0; i < size; i++) {
						list.add(content);
					}
					if (!group) {
						page = new ContentListingPage(null, null, list);
					} else {
						final List<GroupItem<Content>> glist = Lists.newArrayListWithCapacity(size);
						for (Content c : list) {
							glist.add(GroupItem.of(c));
						}
						page = new GroupedContentListingPage(null, null, glist);
					}
				}
			}
		}
	}

	private void prepareXML(RenderContext c, Iterable<?> page) {
		if (page == null) {
			return;
		}
		for (Object o : page) {
			if (o instanceof GroupItem) {
				final GroupItem<?> g = (GroupItem<?>) o;
				if (g.isElementItem()) {
					prepareContentXML(c, g.getElement());
				} else {
					prepareXML(c, g.getGroup().getItems());
				}
			} else {
				prepareContentXML(c, o);
			}
		}
	}

	private boolean prepareContentXML(RenderContext context, Object o) {
		if (o instanceof Content) {
			final Content c = (Content) o;
			final Map<String, Object> local = c.getLocalValues();
			if (local.get(Model.XML) == null) {
				local.put(Model.XML, Model.loadXML(c.getContent()));
			}
			final ContentKey key = c.getContentKey();
			if (key != null) {
				local.put(Model.HREF, context.getURI(PageKey.content(key)));
			}
			return true;
		}
		return false;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				if (page == null) {
					return HTML.create(context).p();
				}
				prepareXML(context, page);
				final FreeMarkerService fms = getFreeMarkerService();
				final Model model = fms.createModel(context).setPage(context, page);
				final int before;
				final int after;
				if (config == null) {
					before = 2;
					after = 2;
				} else {
					final Integer pagesBefore = config.pagesBefore();
					final Integer pagesAfter = config.pagesAfter();
					before = (pagesBefore != null && pagesBefore >= 0) ? pagesBefore : 2;
					after = (pagesAfter != null && pagesAfter >= 0) ? pagesAfter : 2;
				}
				model.setPageMarks(context, page.getPagination(), before, after);
				return fms.getFragment(config.templateFile(), context, model);
			}
		};
	}
}
