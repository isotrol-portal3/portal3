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

package com.isotrol.impe3.api;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import net.sf.derquinsej.CaseIgnoringString;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


/**
 * Value representing a page key.
 * @author Andres Rodriguez
 */
public abstract class PageKey {
	private PageKey() {
	}

	/** Value representing the main page. */
	private static final PageKey MAIN = new PageKey() {
	};

	/** Value representing the default page. */
	private static final PageKey DEFAULT = new PageKey() {
	};

	/**
	 * Returns the page key for the main page.
	 * @return The requested key.
	 */
	public static PageKey main() {
		return MAIN;
	}

	/**
	 * Returns the page key for the default page.
	 * @return The requested key.
	 */
	public static PageKey defaultPage() {
		return DEFAULT;
	}

	/**
	 * Returns the page key for a special page.
	 * @param name Name of the special page.
	 * @return The requested key.
	 */
	public static Special special(final String name) {
		checkNotNull(name);
		return new Special(name);
	}

	/**
	 * Returns the page key for the default error page.
	 * @return The requested key.
	 */
	public static ErrorPage error() {
		return ERROR;
	}

	/**
	 * Returns the page key for a error page.
	 * @param name Name of the error page.
	 * @return The requested key.
	 */
	public static ErrorPage error(final String name) {
		if (name == null) {
			return ERROR;
		}
		try {
			Class<?> klass = Class.forName(name);
			if (klass == Exception.class) {
				return ERROR;
			} else if (Exception.class.isAssignableFrom(klass)) {
				return error(klass.asSubclass(Exception.class));
			}
		} catch (Exception e) {}
		return new NameErrorPage(name);
	}

	/**
	 * Returns the page key for a error page.
	 * @param exceptionClass Exception causing the error.
	 * @return The requested key.
	 */
	public static ErrorPage error(final Class<? extends Exception> exceptionClass) {
		Preconditions.checkNotNull(exceptionClass);
		return new ExceptionErrorPage(exceptionClass);
	}

	/**
	 * Returns the page key for a navigation page.
	 * @param navigationKey Navigation key of the page.
	 * @return The requested key.
	 */
	public static PageKey navigation(final NavigationKey navigationKey) {
		checkNotNull(navigationKey);
		return navigationKey.isContentType() ? new ContentTypePage(navigationKey) : new NavigationPage(navigationKey);
	}

	/**
	 * Returns the page key for a category navigation page.
	 * @param category Category.
	 * @return The requested key.
	 */
	public static PageKey navigation(final Category category) {
		return navigation(NavigationKey.category(category));
	}

	/**
	 * Returns the page key for a content page.
	 * @param navigationKey Navigation key of the page.
	 * @param contentKey Content key of the page.
	 * @return The requested key.
	 */
	public static PageKey content(final NavigationKey navigationKey, final ContentKey contentKey) {
		checkNotNull(contentKey);
		checkArgument(navigationKey == null || !navigationKey.isContentType());
		return new ContentPage(navigationKey, contentKey);
	}

	/**
	 * Returns the page key for a content page without navegation key.
	 * @param contentKey Content key of the page.
	 * @return The requested key.
	 */
	public static PageKey content(ContentKey contentKey) {
		return content(null, contentKey);
	}

	/**
	 * Returns the page key for a content type listing page.
	 * @param navigationKey Navigation key of the page.
	 * @return The requested key.
	 */
	public static PageKey contentType(final NavigationKey navigationKey) {
		checkNotNull(navigationKey);
		checkArgument(navigationKey.isContentType());
		return new ContentTypePage(navigationKey);
	}

	/**
	 * Returns the page key for a content type listing page.
	 * @param navigationKey Navigation key of the page.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageKey contentType(final NavigationKey navigationKey, final ContentType contentType) {
		if (navigationKey == null) {
			return contentType(contentType);
		}
		return new ContentTypePage(navigationKey.withContentType(contentType));
	}

	/**
	 * Returns the page key for a content type listing page without navegation key.
	 * @param contentType Content type of the page.
	 * @return The requested key.
	 */
	public static PageKey contentType(ContentType contentType) {
		return new ContentTypePage(NavigationKey.contentType(contentType));
	}

	/**
	 * Value representing special page keys.
	 * @author Andres Rodriguez
	 */
	public static final class Special extends PageKey {
		private final CaseIgnoringString name;

		private Special(String name) {
			this.name = CaseIgnoringString.valueOf(name);
		}

		/**
		 * Returns the name of the special page.
		 * @return The name of the page.
		 */
		public CaseIgnoringString getName() {
			return name;
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
	}

	/**
	 * Value representing error page keys.
	 * @author Andres Rodriguez
	 */
	public abstract static class ErrorPage extends PageKey {
		private ErrorPage() {
		}

		/**
		 * Returns the name of the error page.
		 * @return The name of the page.
		 */
		public abstract String getName();

		/**
		 * Returns the key of the parent error page.
		 * @return The key of the parent error page or {@code null} if it has no parent.
		 * @throws IllegalStateException if the page is not an error page.
		 */
		public PageKey getParent() {
			return ERROR;
		}
	}

	/**
	 * Value representing the default error page key.
	 */
	private static final ErrorPage ERROR = new ErrorPage() {
		@Override
		public String getName() {
			return null;
		}

		@Override
		public ErrorPage getParent() {
			return null;
		}
		
		@Override
		public String toString() {
			return "Default error page";
		}
	};

	/**
	 * Value representing a named error page key.
	 */
	private static final class NameErrorPage extends ErrorPage {
		private final String name;

		private NameErrorPage(final String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof NameErrorPage) {
				return equal(name, ((NameErrorPage) obj).name);
			}
			return false;
		}
		
		@Override
		public String toString() {
			return String.format("Error [%s]", name);
		}
	};

	/**
	 * Value representing an exception-based error page key.
	 */
	private static final class ExceptionErrorPage extends ErrorPage {
		private final Class<? extends Exception> exceptionClass;

		private ExceptionErrorPage(final Class<? extends Exception> exceptionClass) {
			this.exceptionClass = exceptionClass;
		}

		@Override
		public String getName() {
			return exceptionClass.getName();
		}

		@Override
		public int hashCode() {
			return exceptionClass.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ExceptionErrorPage) {
				return equal(exceptionClass, ((ExceptionErrorPage) obj).exceptionClass);
			}
			return false;
		}

		@Override
		public PageKey getParent() {
			try {
				if (Exception.class != exceptionClass) {
					return error(exceptionClass.getSuperclass().asSubclass(Exception.class));
				}
			} catch (ClassCastException e) {
				return ERROR;
			}
			return ERROR;
		}

	};

	/**
	 * Base class for page keys with navigation.
	 */
	public static abstract class WithNavigation extends PageKey {
		private final NavigationKey navigationKey;

		private WithNavigation(final NavigationKey navigationKey) {
			this.navigationKey = navigationKey;
		}

		/**
		 * Returns the navigation key.
		 * @return The navigation key.
		 */
		public NavigationKey getNavigationKey() {
			return navigationKey;
		}

		@Override
		public int hashCode() {
			return navigationKey != null ? navigationKey.hashCode() : 0;
		}

		boolean equalNK(WithNavigation wn) {
			return equal(navigationKey, wn.navigationKey);
		}
	};

	/**
	 * Value representing a navigation page key.
	 */
	public static final class NavigationPage extends WithNavigation {
		private NavigationPage(final NavigationKey navigationKey) {
			super(checkNotNull(navigationKey));
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof NavigationPage) {
				return equalNK((NavigationPage) obj);
			}
			return false;
		}
	};

	/**
	 * Value representing a content page key.
	 */
	public static final class ContentPage extends WithNavigation {
		private final ContentKey contentKey;

		private ContentPage(final NavigationKey navigationKey, final ContentKey contentKey) {
			super(navigationKey);
			this.contentKey = contentKey;
		}

		/**
		 * Returns the content key.
		 * @return The content key.
		 */
		public ContentKey getContentKey() {
			return contentKey;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(getNavigationKey(), contentKey);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ContentPage) {
				final ContentPage cp = (ContentPage) obj;
				return equal(contentKey, cp.contentKey) && equalNK(cp);
			}
			return false;
		}
	};

	/**
	 * Value representing a content type listing page key.
	 */
	public static final class ContentTypePage extends WithNavigation {

		private ContentTypePage(final NavigationKey navigationKey) {
			super(navigationKey);
			checkArgument(navigationKey.isContentType());
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ContentTypePage) {
				final ContentTypePage p = (ContentTypePage) obj;
				return equalNK(p);
			}
			return false;
		}
	};

}
