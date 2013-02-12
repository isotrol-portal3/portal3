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

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.newLinkedList;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import net.sf.derquinsej.Proxies;

import org.springframework.util.StringUtils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.palette.AbstractFreeMarkerComponent;

/**
 * Base class for Breadcrumb Components.
 * 
 * @author Andres Rodriguez
 */
public abstract class AbstractBreadcrumbComponent extends
		AbstractFreeMarkerComponent {
	/** Portal. */
	private Portal portal;
	/** Building list. */
	private final List<BreadcrumbItem> items = Lists.newArrayList();
	/** Built breadcrumb. */
	private Breadcrumb breadcrumb = null;
	/** Module config. */
	private BreadcrumbConfig config;
	/** Categories. */
	private Categories categories;
	/** Locale. */
	private Locale locale;
	/** URI Generator. */
	private URIGenerator uriGenerator;
	/** Route. */
	private Route route;

	/**
	 * Constructor.
	 */
	public AbstractBreadcrumbComponent() {
	}

	public void setConfig(BreadcrumbConfig config) {
		this.config = config;
	}

	public final BreadcrumbConfig getConfig() {
		if (config == null) {
			return Proxies.alwaysNull(BreadcrumbConfig.class);
		}
		return config;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	};

	@Inject
	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	protected final Portal getPortal() {
		return portal;
	}

	@Inject
	public final void setLocale(Locale locale) {
		this.locale = locale;
	}

	protected final Locale getLocale() {
		return locale;
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
	}

	protected final Breadcrumb getBreadcrumb() {
		if (breadcrumb == null) {
			final boolean rlu = config != null
					&& config.removeLastURI().booleanValue();
			breadcrumb = Breadcrumb.of(filter(items, notNull()), rlu);
		}
		return breadcrumb;
	}

	/**
	 * Adds a new item.
	 * 
	 * @param name
	 *            Item name.
	 * @param uri
	 *            Item destination URI.
	 */
	protected final void add(String name, String uri) {
		if (StringUtils.hasText(name)) {
			if (!StringUtils.hasText(uri)) {
				uri = null;
			}
			items.add(new BreadcrumbItem(name, uri));
		}
	}

	/**
	 * Adds a new item.
	 * 
	 * @param name
	 *            Item name.
	 * @param uri
	 *            Item destination URI.
	 */
	protected final void add(String name, URI uri) {
		final String u = uri != null ? uri.toASCIIString() : null;
		add(name, u);
	}

	/**
	 * Adds a new item.
	 * 
	 * @param name
	 *            Item name.
	 * @param pk
	 *            Page key.
	 */
	protected final void add(String name, PageKey pk) {
		if (pk == null) {
			return;
		}
		add(name, getURI(pk));
	}

	private URI getURI(PageKey pk) {
		if (route == null || pk == null || uriGenerator == null) {
			return null;
		}
		final Route r = route.toPage(pk);
		return uriGenerator.getURI(r);
	}

	/**
	 * Adds items related to a category.
	 * 
	 * @param category
	 *            Category to add.
	 */
	protected final void add(Category category) {
		if (category == null || !categories.containsKey(category.getId())) {
			return;
		}
		final List<Category> cats = newLinkedList();
		Category parent;
		while ((parent = categories.getParent(category.getId())) != null) {
			cats.add(0, category);
			category = parent;
		}
		for (Category c : cats) {
			add(c.getName().get(locale).getDisplayName(), getURI(PageKey
					.navigation(c)));
		}
	}

	/**
	 * Adds an item related to a content type.
	 * 
	 * @param contentType
	 *            Content type to add.
	 * @param navigation
	 *            Navigation to use.
	 */
	protected final void add(ContentType contentType,
			NavigationKey navigationKey) {
		if (contentType == null) {
			return;
		}
		final PageKey pk = PageKey.contentType(navigationKey, contentType);
		add(contentType.getName().get(locale).getDisplayName(), pk);
	}

	/**
	 * Adds items related to a navigation.
	 * 
	 * @param navigationKey
	 *            Navigation to add.
	 * @param category
	 *            True if the category navigation must be included.
	 * @param contentType
	 *            True if the content type navigation must be included.
	 */
	protected final void add(NavigationKey navigationKey, boolean category,
			boolean contentType) {
		if (navigationKey == null || config == null) {
			return;
		}
		if (category && navigationKey.isCategory()) {
			add(navigationKey.getCategory());
		}
		if (contentType && navigationKey.isContentType()) {
			add(navigationKey.getContentType(), navigationKey);
		}
	}

	protected final void prepare() {
		breadcrumb = null;
		items.clear();
		// Prefix
		add(config.prefixText(), config.prefixURI());
		// Main page link
		if (Boolean.TRUE.equals(config.includePortal())) {
			String name = config.portalName();
			if (!StringUtils.hasText(name)) {
				name = portal.getName().get();
			}
		}
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getHeader() {
				// Is title requested
				if (config == null
						|| !Boolean.TRUE.equals(config.generateTitle())) {
					return null;
				}
				final Breadcrumb b = getBreadcrumb();
				if (b.isEmpty()) {
					return null;
				}
				// Get separator
				String separator = config.titleSeparator();
				if (!StringUtils.hasText(separator)) {
					separator = " / ";
				}
				final String title = Joiner.on(separator).join(b);
				if (!StringUtils.hasText(title)) {
					return null;
				}
				return HTML.create(context).title(title);
			}

			@Override
			public HTMLFragment getBody() {
				String template = null;
				final FreeMarkerService fms = getFreeMarkerService();
				final Model model = fms.createModel(context);
				return fms.getFragment(template, context, model);
			}
		};
	}
}
