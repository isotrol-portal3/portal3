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

package com.isotrol.impe3.palette.breadcrumb;


import org.springframework.util.StringUtils;

import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.Content;


/**
 * Manual Breadcrumb Component.
 * @author Andres Rodriguez
 */
public class ContentBreadcrumbComponent extends WithNavigationBreadcrumbComponent {
	/** Component config. */
	private ContentBreadcrumbConfig componentConfig;
	/** Content key. */
	private ContentKey contentKey;
	/** Content. */
	private Content content;

	/**
	 * Constructor.
	 */
	public ContentBreadcrumbComponent() {
	}

	@Inject
	public void setContentKey(ContentKey contentKey) {
		this.contentKey = contentKey;
	}

	@Inject
	public void setContent(Content content) {
		this.content = content;
	}

	private ContentType getContentType() {
		if (content != null) {
			return content.getContentType();
		} else if (contentKey != null) {
			return contentKey.getContentType();
		}
		return null;
	}

	public void edit() {
		prepare();
		final BreadcrumbConfig config = getConfig();
		final boolean cg = Boolean.TRUE.equals(config.withCategoryNav());
		final boolean ct = Boolean.TRUE.equals(config.withContentTypeNav());
		// Add navigation key
		addNavigation(cg, false);
		// Add content type if requested
		if (ct) {
			final ContentType contentType = getContentType();
			if (contentType != null) {
				add(contentType, getNavigationKey());
			}
		}
		// Title
		if (content != null && componentConfig != null && Boolean.TRUE.equals(componentConfig.includeTitle())) {
			final String title = content.getTitle();
			if (StringUtils.hasText(title)) {
				add(title, (String) null);
			}
		}
	}
}
