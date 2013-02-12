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

package com.isotrol.impe3.core;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.UUID;

import net.sf.derquinsej.CaseIgnoringString;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;


/**
 * Value representing a page key.
 * @author Andres Rodriguez
 */
public abstract class PageMapKey {
	private static PageMapKey of(NavigationKey key) {
		if (key == null) {
			return DEFAULT;
		}
		if (key.isContentType()) {
			return of(key.withoutContentType());
		}
		if (key.isCategory()) {
			return category(key.getCategory(), false);
		}
		return tag(key.getTag());
	}

	/**
	 * Clone a pageKey.
	 * @param key page key to be cloned
	 * @return new cloned page key
	 */
	public static PageMapKey of(PageKey key) {
		if (key == PageKey.main()) {
			return MAIN;
		}
		if (key instanceof PageKey.Special) {
			return special(((PageKey.Special) key).getName());
		}
		if (key instanceof PageKey.ErrorPage) {
			return new ErrorPage((PageKey.ErrorPage) key);
		}
		if (key instanceof PageKey.WithNavigation) {
			final NavigationKey navKey = ((PageKey.WithNavigation) key).getNavigationKey();
			final PageMapKey nk = of(navKey);
			if (key instanceof PageKey.NavigationPage) {
				return nk;
			}
			if (key instanceof PageKey.ContentPage) {
				final ContentKey ck = ((PageKey.ContentPage) key).getContentKey();
				final ContentType ct = ck != null ? ck.getContentType() : null;
				return content(nk, ct);
			}
			if (key instanceof PageKey.ContentTypePage) {
				return contentType(nk, navKey.getContentType());
			}
		}
		return DEFAULT;
	}

	private PageMapKey() {
	}

	public abstract PageMapKey getParent(Portal portal);

	/**
	 * Returns true if the page is a content detail filtered by category.
	 * @param contentTypeId Content type id to check.
	 * @return True if the contition is met for the provided content type.
	 */
	public boolean isContentWithCategory(UUID contentTypeId) {
		return false;
	}

	/**
	 * Returns true if the page is a content listing filtered by category.
	 * @param contentTypeId Content type id to check.
	 * @return True if the contition is met for the provided content type.
	 */
	public boolean isContentTypeWithCategory(UUID contentTypeId) {
		return false;
	}

	/**
	 * Returns a predicate to check if a page is a content detail filtered by category.
	 * @param contentTypeId Content type id to check.
	 * @return The requested predicate.
	 */
	public static final Predicate<PageMapKey> contentWithCategory(final UUID contentTypeId) {
		return new Predicate<PageMapKey>() {
			public boolean apply(PageMapKey input) {
				return input != null && input.isContentWithCategory(contentTypeId);
			}
		};
	}

	/**
	 * Returns a predicate to check if a page is a content listing filtered by category.
	 * @param contentTypeId Content type id to check.
	 * @return The requested predicate.
	 */
	public static final Predicate<PageMapKey> contentTypeWithCategory(final UUID contentType) {
		return new Predicate<PageMapKey>() {
			public boolean apply(PageMapKey input) {
				return input != null && input.isContentTypeWithCategory(contentType);
			}
		};
	}

	/** Value representing the main page. */
	private static final PageMapKey MAIN = new PageMapKey() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Main Page";
		}
	};

	/** Value representing the default page. */
	private static final PageMapKey DEFAULT = new PageMapKey() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Default Page";
		}

	};

	/**
	 * Returns the page key for the main page.
	 * @return The requested key.
	 */
	public static PageMapKey main() {
		return MAIN;
	}

	/**
	 * Returns the page key for the default page.
	 * @return The requested key.
	 */
	public static PageMapKey defaultPage() {
		return DEFAULT;
	}

	/**
	 * Returns the page key for a special page.
	 * @param name Name of the special page.
	 * @return The requested key.
	 */
	public static Special special(final String name) {
		Preconditions.checkNotNull(name);
		return new Special(name);
	}

	/**
	 * Returns the page key for a special page.
	 * @param name Name of the special page.
	 * @return The requested key.
	 */
	public static Special special(final CaseIgnoringString name) {
		Preconditions.checkNotNull(name);
		return new Special(name);
	}

	/**
	 * Returns the page key for the default error page.
	 * @return The requested key.
	 */
	public static PageMapKey error() {
		return new ErrorPage(PageKey.error());
	}

	/**
	 * Returns the page key for a error page.
	 * @param name Name of the error page.
	 * @return The requested key.
	 */
	public static PageMapKey error(final String name) {
		return new ErrorPage(PageKey.error(name));
	}

	/**
	 * Returns the page key for a error page.
	 * @param exceptionClass Exception causing the error.
	 * @return The requested key.
	 */
	public static PageMapKey error(final Class<? extends Exception> exceptionClass) {
		return new ErrorPage(PageKey.error(exceptionClass));
	}

	/**
	 * Returns the page key for a tag navigation page.
	 * @param tag Tag.
	 * @return The requested key.
	 */
	public static PageMapKey tag(final String tag) {
		if (tag == null) {
			return DEFAULT_TAG;
		}
		return new TagPage(tag);
	}

	/**
	 * Returns the page key for the default category navigation page.
	 * @return The requested key.
	 */
	public static PageMapKey category() {
		return DEFAULT_CNP;
	}

	/**
	 * Returns the page key for a category navigation page.
	 * @param category Category.
	 * @param umbrella Include children.
	 * @return The requested key.
	 */
	public static PageMapKey category(final UUID category, final boolean umbrella) {
		if (category == null) {
			return DEFAULT_CNP;
		}
		return new CategoryPage(category, umbrella);
	}

	/**
	 * Returns the page key for a category navigation page.
	 * @param category Category.
	 * @param umbrella Include children.
	 * @return The requested key.
	 */
	public static PageMapKey category(final Category category, final boolean umbrella) {
		return category(category != null ? category.getId() : null, umbrella);
	}

	/**
	 * Returns the default page key for a content page.
	 * @return The requested key.
	 */
	public static PageMapKey content() {
		return DEFAULT_CP;
	}

	/**
	 * Returns the page key for a content page.
	 * @param navigation Navigation key of the page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey content(final PageMapKey navigation, final UUID contentType) {
		if (navigation == DEFAULT && contentType == null) {
			return DEFAULT_CP;
		}
		return new ContentPage(navigation, contentType, true);
	}

	/**
	 * Returns the page key for a content page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey content(final UUID contentType) {
		return content(DEFAULT, contentType);
	}

	/**
	 * Returns the page key for a content page.
	 * @param navigation Navigation key of the page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey content(final PageMapKey navigation, final ContentType contentType) {
		return content(navigation, contentType != null ? contentType.getId() : null);
	}

	/**
	 * Returns the default page key for a content type listing page.
	 * @return The requested key.
	 */
	public static PageMapKey contentType() {
		return contentType(null);
	}

	/**
	 * Returns the page key for a content type listing page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey contentType(final UUID contentType) {
		return contentType(DEFAULT, contentType);
	}

	/**
	 * Returns the page key for a content type listing page.
	 * @param navigation Navigation key of the page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey contentType(final PageMapKey navigation, final UUID contentType) {
		if (navigation == DEFAULT && contentType == null) {
			return DEFAULT_CTP;
		}
		return new ContentTypePage(navigation, contentType, true);
	}

	/**
	 * Returns the page key for a content type listing page.
	 * @param navigation Navigation key of the page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageMapKey contentType(final PageMapKey navigation, final ContentType contentType) {
		return contentType(navigation, contentType != null ? contentType.getId() : null);
	}

	/**
	 * Value representing special page keys.
	 * @author Andres Rodriguez
	 */
	private static final class Special extends PageMapKey {
		private final CaseIgnoringString name;

		private Special(String name) {
			this.name = CaseIgnoringString.valueOf(name);
		}

		private Special(CaseIgnoringString name) {
			this.name = name;
		}

		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Special) {
				return equal(name, ((Special) obj).name);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("Special Page [%s]", name.toString());
		}
	}

	/**
	 * Value representing error page keys.
	 * @author Andres Rodriguez
	 */
	private static class ErrorPage extends PageMapKey {
		private final PageKey.ErrorPage key;

		private ErrorPage(PageKey.ErrorPage key) {
			this.key = key;
		}

		@Override
		public PageMapKey getParent(Portal portal) {
			if (key == null || key == PageKey.error()) {
				return DEFAULT;
			}
			final PageKey pk = key.getParent();
			if (pk instanceof PageKey.ErrorPage) {
				return new ErrorPage((PageKey.ErrorPage) pk);
			}
			return DEFAULT;
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ErrorPage) {
				return equal(key, ((ErrorPage) obj).key);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("Error Page [%s]", key.toString());
		}

	}

	/** Default tag navigation page key. */
	private static final TagPage DEFAULT_TAG = new TagPage() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Default tag navigation page";
		}

	};

	/**
	 * Value representing a tag navigation page key.
	 */
	private static class TagPage extends PageMapKey {
		private final CaseIgnoringString tag;

		private TagPage() {
			this.tag = null;
		}

		private TagPage(final CaseIgnoringString tag) {
			this.tag = tag;
		}

		private TagPage(final String tag) {
			this.tag = CaseIgnoringString.valueOf(tag);
		}

		@Override
		public PageMapKey getParent(Portal portal) {
			return tag != null ? DEFAULT_TAG : DEFAULT;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(TagPage.class, tag);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof TagPage) {
				return equal(tag, ((TagPage) obj).tag);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("Tag navigation [%s]", tag);
		}

	};

	/** Default category navigation page key. */
	private static final CategoryPage DEFAULT_CNP = new CategoryPage() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Default category navigation page";
		}
	};

	/**
	 * Value representing a category navigation page key.
	 */
	private static class CategoryPage extends PageMapKey {
		private final UUID category;
		private final boolean umbrella;

		private CategoryPage() {
			this.category = null;
			this.umbrella = true;
		}

		private CategoryPage(UUID category, boolean umbrella) {
			this.category = category;
			this.umbrella = umbrella;
		}

		private CategoryPage(UUID category) {
			this(category, false);
		}

		@Override
		public PageMapKey getParent(Portal portal) {
			if (category == null) {
				return DEFAULT;
			}
			if (umbrella == false) {
				return new CategoryPage(category, true);
			}
			UUID parent;
			try {
				parent = portal.getCategories().getParentKey(category);
			} catch (IllegalArgumentException e) {
				parent = null;
			}
			if (parent == null) {
				return DEFAULT_CNP;
			}
			return new CategoryPage(parent, true);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(CategoryPage.class, category, umbrella);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof CategoryPage) {
				final CategoryPage p = (CategoryPage) obj;
				return umbrella == p.umbrella && equal(category, p.category);
			}
			return false;
		}

		@Override
		public String toString() {
			return String.format("Category NP: [%s] Children: [%s]", category, Boolean.valueOf(umbrella).toString());
		}

	};

	/**
	 * Content related page keyss.
	 */
	private static abstract class OfContentPage extends PageMapKey {
		private final PageMapKey navigation;
		private final UUID contentType;
		private final boolean useType;

		private OfContentPage() {
			this.navigation = DEFAULT;
			this.contentType = null;
			this.useType = true;
		}

		private OfContentPage(PageMapKey navigation, UUID contentType, boolean useType) {
			checkArgument(navigation == DEFAULT || navigation instanceof TagPage || navigation instanceof CategoryPage);
			this.navigation = (navigation == null) ? DEFAULT : navigation;
			this.contentType = contentType;
			this.useType = navigation == DEFAULT ? true : useType;
		}

		abstract OfContentPage create(PageMapKey navigation, UUID contentType, boolean useType);

		abstract OfContentPage getDefault();

		@Override
		public PageMapKey getParent(Portal portal) {
			if (navigation == DEFAULT) {
				return getDefault();
			}
			if (useType == false) {
				return create(navigation.getParent(portal), contentType, true);
			} else {
				return create(navigation, contentType, false);
			}
		}

		final boolean isContentType(UUID id) {
			return id != null && id.equals(contentType);
		}

		final boolean isWithCategory() {
			return (navigation instanceof CategoryPage);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(getClass(), navigation, useType);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof OfContentPage) {
				final OfContentPage p = (OfContentPage) obj;
				return useType == p.useType && getClass() == p.getClass() && equal(navigation, p.navigation)
					&& (!useType || equal(contentType, p.contentType));
			}
			return false;
		}

		@Override
		public String toString() {
			return String
				.format("[%s] Navigation [%s] ContentType [%s]", getClass().getName(), navigation, contentType);
		}
	};

	/** Default content page key. */
	private static final ContentPage DEFAULT_CP = new ContentPage() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Default Content Page";
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}
	};

	/**
	 * Value representing a content page key.
	 */
	private static class ContentPage extends OfContentPage {
		private ContentPage() {
		}

		private ContentPage(PageMapKey navigation, UUID contentType, boolean useType) {
			super(navigation, contentType, useType);
		}

		@Override
		OfContentPage create(PageMapKey navigation, UUID contentType, boolean useType) {
			return new ContentPage(navigation, contentType, useType);
		}

		@Override
		OfContentPage getDefault() {
			return DEFAULT_CP;
		}

		@Override
		public boolean isContentWithCategory(UUID contentTypeId) {
			return isContentType(contentTypeId) && isWithCategory();
		}
	};

	/** Default content type page key. */
	private static final ContentTypePage DEFAULT_CTP = new ContentTypePage() {
		@Override
		public PageMapKey getParent(Portal portal) {
			return DEFAULT;
		}

		@Override
		public String toString() {
			return "Default Content Type Page";
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			return obj == this;
		}
	};

	/**
	 * Value representing a content page key.
	 */
	private static class ContentTypePage extends OfContentPage {
		private ContentTypePage() {
		}

		private ContentTypePage(PageMapKey navigation, UUID contentType, boolean useType) {
			super(navigation, contentType, useType);
		}

		@Override
		OfContentPage create(PageMapKey navigation, UUID contentType, boolean useType) {
			return new ContentTypePage(navigation, contentType, useType);
		}

		@Override
		ContentTypePage getDefault() {
			return DEFAULT_CTP;
		}

		@Override
		public boolean isContentTypeWithCategory(UUID contentTypeId) {
			return isContentType(contentTypeId) && isWithCategory();
		}
	};

}
