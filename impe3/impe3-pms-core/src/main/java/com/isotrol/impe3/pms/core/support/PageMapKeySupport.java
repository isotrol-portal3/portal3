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

package com.isotrol.impe3.pms.core.support;


import java.util.UUID;

import com.isotrol.impe3.core.PageMapKey;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PagePB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.core.obj.PageObject;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.PageDfn;


/**
 * Utility functions for calculating page map keys.
 * @author Andres Rodriguez.
 */
public final class PageMapKeySupport {
	private PageMapKeySupport() {
		throw new AssertionError();
	}

	/**
	 * Gets the key for a set of page parameters.
	 * @param pc Page class.
	 * @param name Page name.
	 * @param tag Page tag.
	 * @param category Category Id.
	 * @param contentType Content type Id.
	 * @param umbrella Umbrella.
	 */
	private static PageMapKey of(PageClass pc, String name, String tag, UUID category, UUID contentType, boolean umbrella) {
		final PageMapKey nk;
		switch (pc) {
			case SPECIAL:
				return PageMapKey.special(name);
			case ERROR:
				return PageMapKey.error(name);
			case TEMPLATE:
				return null;
			case MAIN:
				return PageMapKey.main();
			case DEFAULT:
				return PageMapKey.defaultPage();
			case TAG:
				return PageMapKey.tag(tag);
			case CATEGORY:
				return PageMapKey.category(category, umbrella);
			case CONTENT:
				if (category != null) {
					nk = PageMapKey.category(category, umbrella);
				} else if (tag != null) {
					nk = PageMapKey.tag(tag);
				} else {
					nk = PageMapKey.defaultPage();
				}
				return PageMapKey.content(nk, contentType);
			case CONTENT_TYPE:
				if (category != null) {
					nk = PageMapKey.category(category, umbrella);
				} else if (tag != null) {
					nk = PageMapKey.tag(tag);
				} else {
					nk = PageMapKey.defaultPage();
				}
				return PageMapKey.contentType(nk, contentType);
			default:
				throw new AssertionError();
		}
	}
	
	/**
	 * Gets the key for a page pb message..
	 * @param page Page message.
	 */
	public static PageMapKey of(PagePB page) throws PMSException {
		final PageClass pc = PageObject.toPageClass(page.getPageClass());
		final String name = page.hasName() ? page.getName() : null;
		final String tag = page.hasTagName() ? page.getTagName() : null;
		final String cg = page.hasCategoryId() ? page.getCategoryId() : null;
		final UUID cgId = cg != null ? NotFoundProviders.CATEGORY.toUUID(cg) : null;
		final String ct = page.hasContentTypeId() ? page.getContentTypeId() : null;
		final UUID ctId = ct != null ? NotFoundProviders.CONTENT_TYPE.toUUID(ct) : null;
		final boolean umbrella = page.getUmbrella();
		return of(pc, name, tag, cgId, ctId, umbrella);
	}
	

	/**
	 * Gets the key for a page definition.
	 * @param page Page definition.
	 */
	public static PageMapKey of(PageDfn page) {
		final PageClass pc = page.getPage().getPageClass();
		final String name = page.getName();
		final String tag = page.getTag();
		final CategoryEntity cg = page.getCategory();
		final UUID category = (cg == null) ? null : cg.getId();
		final ContentTypeEntity ct = page.getContentType();
		final UUID contentType = (ct == null) ? null : ct.getId();
		final PageMapKey nk;
		switch (pc) {
			case SPECIAL:
				return PageMapKey.special(name);
			case ERROR:
				return PageMapKey.error(name);
			case TEMPLATE:
				return null;
			case MAIN:
				return PageMapKey.main();
			case DEFAULT:
				return PageMapKey.defaultPage();
			case TAG:
				return PageMapKey.tag(tag);
			case CATEGORY:
				return PageMapKey.category(category, page.isUmbrella());
			case CONTENT:
				if (category != null) {
					nk = PageMapKey.category(category, page.isUmbrella());
				} else if (tag != null) {
					nk = PageMapKey.tag(tag);
				} else {
					nk = PageMapKey.defaultPage();
				}
				return PageMapKey.content(nk, contentType);
			case CONTENT_TYPE:
				if (category != null) {
					nk = PageMapKey.category(category, page.isUmbrella());
				} else if (tag != null) {
					nk = PageMapKey.tag(tag);
				} else {
					nk = PageMapKey.defaultPage();
				}
				return PageMapKey.contentType(nk, contentType);
			default:
				throw new AssertionError();
		}
	}
}
