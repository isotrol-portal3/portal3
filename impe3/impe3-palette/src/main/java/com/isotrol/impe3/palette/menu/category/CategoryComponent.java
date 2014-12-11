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

package com.isotrol.impe3.palette.menu.category;


import static com.google.common.base.Predicates.and;
import static com.google.common.collect.Iterables.filter;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageKey.WithNavigation;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.content.OneLevelGroupResult;
import com.isotrol.impe3.freemarker.FreeMarkerService;
import com.isotrol.impe3.freemarker.Model;
import com.isotrol.impe3.palette.AbstractFreeMarkerComponent;
import com.isotrol.impe3.palette.menu.MenuItem;


/**
 * Abstract class for components in this module.
 * @author Andres Rodriguez
 */
public abstract class CategoryComponent extends AbstractFreeMarkerComponent {
	
	/** Module Configuration. */
	private CategoryMenuModuleConfig config;
	
	/** Component configuration. */
	private CategoryMenuConfig componentConfig;
	
	/** Categories. */
	private Categories categories;
	
	/** URI Generator. */
	private URIGenerator uriGenerator;
	
	/** Route. */
	private Route route = null;
	
	/** Current navigation key. */
	private NavigationKey navigationKey;
	
	/** Locale. */
	private Locale locale = null;
	
	/** Menu items. */
	private final List<MenuItem> items = Lists.newArrayList();
	
	/** Content loader. */
	private ContentLoader contentLoader;
	
	/** Category predicate. */
	private Predicate<Category> filter = null;
	
	/** External predicate provider. */
	private CategoryPredicateProvider filterProvider = null;
	
	/** Filter by content predicate. */
	private Predicate<? super Category> filterByContent = null;
	
	/** Whether to include not visible categories. */
	private Predicate<? super Category> includeNotVisible = null;
	
	/** Whether to include not routable categories. */
	private Predicate<? super Category> includeNotRoutable = null;
	
	/** Predicate for selected category. */
	private Predicate<Category> selected = null;
	

	/**
	 * Public constructor.
	 */
	public CategoryComponent() {
	}

	public void setConfig(CategoryMenuModuleConfig config) {
		this.config = config;
	}

	public void setFilterProvider(CategoryPredicateProvider filterProvider) {
		this.filterProvider = filterProvider;
	}

	@Inject
	public void setComponentConfig(CategoryMenuConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	@Inject
	public void setContentLoader(ContentLoader contentLoader) {
		this.contentLoader = contentLoader;
	}

	public void setCategories(Categories categories) {
		this.categories = categories;
	}

	public void setUriGenerator(URIGenerator uriGenerator) {
		this.uriGenerator = uriGenerator;
	}

	@Inject
	public void setRoute(Route route) {
		this.route = route;
		this.locale = (route != null) ? route.getLocale() : null;
	}
	
	@Inject
	public void setNavigationKey(NavigationKey navigationKey) {
		this.navigationKey = navigationKey;
	}

	protected Categories getCategories() {
		return categories;
	}

	protected List<MenuItem> getItems() {
		return items;
	}

	protected MenuItem createItem(Category category, Predicate<Category> selected) {
		URI uri = null; 
		if(category.isRoutable()) {
			final NavigationKey nk = NavigationKey.category(category);
			final PageKey pk = PageKey.navigation(nk);
			final Route r = (route != null) ? route.toPage(pk) : Route.of(false, pk, null, null);
			uri = uriGenerator.getURI(r);
		}
		final Name name = (locale != null) ? category.getName().get(locale) : category.getDefaultName();
		final MenuItem item = new MenuItem(name.getDisplayName(), uri, selected.apply(category));
		return item;
	}

	protected MenuItem createParentItem(Category category, Predicate<Category> selected) {
		if (categories == null || category == null) {
			return null;
		}
		final UUID id = category.getId();
		if (!categories.containsKey(id)) {
			return null;
		}
		final Category parent = categories.getParent(id);
		if (parent == null || !include().apply(parent)) {
			return null;
		}
		final MenuItem item = createItem(parent, selected);
		item.setParent(createParentItem(parent, selected));
		return item;
	}

	/**
	 * Returns the category included in the navigation key, if any.
	 * @return The category or {@code null}.
	 */
	final Category getCurrent() {
		if (componentConfig != null && componentConfig.current() != null) {
			return componentConfig.current();
		} else if (config != null && config.current() != null) {
			return config.current();
		}
		
		if (navigationKey != null && navigationKey.isCategory()) {
			Category c = navigationKey.getCategory();
			if (categories.containsValue(c)) {
				return c;
			}
		}

		return null;
	}

	protected void add(Iterable<Category> categories, Predicate<Category> selected, MenuItem parent) {
		add(items, 0, depth(), categories, selected, parent);
	}

	protected void add(List<MenuItem> list, int depth, int maxDepth, Iterable<Category> level,
		Predicate<Category> selected, MenuItem parent) {
		if (depth > maxDepth) {
			return;
		}
		for (Category c : filter(level, include())) {
			MenuItem mi = createItem(c, selected);
			mi.setParent(parent);
			list.add(mi);
			add(mi.getChildren(), depth + 1, maxDepth, categories.getChildren(c.getId()), selected, mi);
		}
	}

	@SuppressWarnings("unchecked")
	final Predicate<? super Category> include() {
		if (filter == null) {
			filter = and(includeNotVisible(), includeNotRoutable(), filterByContent());
			if (filterProvider != null) {
				Predicate<Category> f = filterProvider.getCategoryPredicate(filter);
				if (f != null) {
					filter = f;
				}
			}
		}
		return filter;
	}

	// Configuration items resolution

	final Predicate<? super Category> filterByContent() {
		if (filterByContent == null) {
			filterByContent = calculateFilterByContent();
		}
		return filterByContent;
	}

	private Predicate<? super Category> calculateFilterByContent() {
		if (config != null && config.filterByContentAvailability() && contentLoader != null) {
			final OneLevelGroupResult<Category> g = contentLoader.newCriteria().groupByCategory();
			final Predicate<Category> p = new Predicate<Category>() {
				public boolean apply(Category input) {
					return g.apply(input) > 0;
				}
			};
			return p;
		}
		return Predicates.alwaysTrue();
	}

	final Predicate<? super Category> includeNotVisible() {
		if (includeNotVisible == null) {
			includeNotVisible = calculateIncludeNotVisible();
		}
		return includeNotVisible;
	}

	private Predicate<? super Category> calculateIncludeNotVisible() {
		boolean include = false;
		if (componentConfig != null && componentConfig.includeNotVisible() != null) {
			include = componentConfig.includeNotVisible().booleanValue();
		} else if (config != null && config.includeNotVisible() != null) {
			include = config.includeNotVisible().booleanValue();
		}
		if (include) {
			return Predicates.alwaysTrue();
		}
		return Category.VISIBLE;
	}

	final Predicate<? super Category> includeNotRoutable() {
		if (includeNotRoutable == null) {
			includeNotRoutable = calculateIncludeNotRoutable();
		}
		return includeNotRoutable;
	}

	private Predicate<? super Category> calculateIncludeNotRoutable() {
		boolean include = false;
		if (componentConfig != null && componentConfig.includeNotRoutable() != null) {
			include = componentConfig.includeNotRoutable().booleanValue();
		} else if (config != null && config.includeNotRoutable() != null) {
			include = config.includeNotRoutable().booleanValue();
		}
		if (include) {
			return Predicates.alwaysTrue();
		}
		return Category.IS_ROUTABLE;
	}

	private int parseDepth(int depth) {
		return depth < 0 ? Integer.MAX_VALUE : depth;

	}

	final int depth() {
		if (componentConfig != null && componentConfig.depth() != null) {
			return parseDepth(componentConfig.depth());
		} else if (config != null && config.includeNotVisible() != null) {
			return parseDepth(config.depth());
		}
		return 0;
	}

	final Predicate<Category> selected() {
		if (selected == null) {
			selected = calculateSelected();
		}
		return selected;
	}

	private Predicate<Category> calculateSelected() {
		Category c = getSelected();
		if (c == null || !categories.containsValue(c)) {
			return Predicates.alwaysFalse();
		}
		if (config != null && config.selectParents()) {
			Set<Category> selected = Sets.newHashSet();
			do {
				selected.add(c);
				c = categories.getParent(c.getId());
			}
			while (c != null);
			return Predicates.in(selected);
		}
		return Predicates.equalTo(c);
	}

	private Category getSelected() {
		if (componentConfig != null && componentConfig.selected() != null) {
			return componentConfig.selected();
		} else if (config != null && config.selected() != null) {
			return config.selected();
		}
		if (config != null && config.decoupledSelection()) {
			final PageKey p = route.getPage();
			if(p instanceof WithNavigation) {
				final NavigationKey navigationKey = ((WithNavigation)p).getNavigationKey();
				if (navigationKey != null && navigationKey.isCategory()) {
					Category c = navigationKey.getCategory();
					if (categories.containsValue(c)) {
						return c;
					}
				}
			}
		}
		return getCurrent();
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				final String template;
				if (componentConfig != null && componentConfig.templateFile() != null) {
					template = componentConfig.templateFile();
				} else if (config != null && config.templateFile() != null) {
					template = config.templateFile();
				} else {
					return HTMLFragments.empty();
				}
				final FreeMarkerService fms = getFreeMarkerService();
				final Model model = fms.createModel(context);
				model.put("items", items);
				return fms.getFragment(template, context, model);
			}
		};
	}

}
