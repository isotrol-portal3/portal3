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

package com.isotrol.impe3.freemarker;


import java.io.InputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import net.sf.derquinsej.i18n.Locales;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageKey.WithNavigation;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.RequestParams;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.connectors.uri.URIBuilderService;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.support.nr.NodeLoader;

import freemarker.template.TemplateModelException;


/**
 * IMPE3 FreeMarker Constants and Functions.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class FreeMarker {
	private static Logger logger = LoggerFactory.getLogger(FreeMarker.class);

	/** Not instantiable. */
	private FreeMarker() {
		throw new AssertionError();
	}

	/** Metadata file bundle item. */
	public static final String FITEM_METADATA = "metadata.properties";
	/** XML Data file bundle item. */
	public static final String FITEM_XML = "data.xml";

	private static final ImmutableMap<String, Object> FUNCTIONS;
	private static final String EMPTY = "";

	/**
	 * Loads a node from a file bundle.
	 * @param loader File loader.
	 * @param bundle File bundle.
	 * @return The loaded node or {@code null} if there is an error.
	 */
	public static Node loadXMLNode(FileLoader loader, FileId bundle) {
		try {
			final FileData metadata = loader.loadFromBundle(bundle, FITEM_METADATA);
			final FileData data = loader.loadFromBundle(bundle, FITEM_XML);
			return NodeLoader.loadNode(metadata.getData(), data.getData());
		} catch (Exception e) {
			logger.trace("Problem loading xml node...", e);
			return null; // TODO
		}
	}

	/**
	 * Loads a content from a file bundle.
	 * @param loader File loader.
	 * @param bundle File bundle.
	 * @param contentTypes Content types.
	 * @param categories Categories.
	 * @return The loaded content or {@code null} if there is an error.
	 */
	public static Content loadXMLContent(FileLoader loader, FileId bundle, ContentTypes contentTypes,
		Categories categories) {
		try {
			final InputStream metadata = safeLoad(loader, bundle, FITEM_METADATA);
			final InputStream data = safeLoad(loader, bundle, FITEM_XML);
			return NodeLoader.loadContent(metadata, data, contentTypes, categories);
		} catch (Exception e) {
			logger.trace("Problem loading xml content...", e);
			return null; // TODO
		}
	}

	private static InputStream safeLoad(FileLoader loader, FileId bundle, String name) {
		try {
			return loader.loadFromBundle(bundle, name).getData();
		} catch (RuntimeException e) {
			logger.trace("Problem with safe load input stream...", e);
			return null;
		}
	}

	/**
	 * Return functions map
	 * @return functions immutable map
	 */
	public static ImmutableMap<String, Object> getFunctions() {
		return FUNCTIONS;
	}

	/**
	 * Format timestamp to date.
	 * @param timestamp original format
	 * @param format pattern
	 * @param locale lang locale
	 * @return formatted date
	 */
	public static String timestamp2Date(String timestamp, String format, Locale locale) {
		try {
			Long time = Long.parseLong(timestamp);
			final Date d = new Date();
			d.setTime(time);
			final SimpleDateFormat sdf;
			if (locale != null) {
				sdf = new SimpleDateFormat(format, locale);
			} else {
				sdf = new SimpleDateFormat(format);
			}
			return sdf.format(d);
		} catch (Exception e) {
			return "XXXXXX";
		}
	}

	private static Locale getLocale(String locale) {
		if (!StringUtils.hasText(locale)) {
			return null;
		}
		try {
			return Locales.fromString(locale);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Format timestamp to date.
	 * @param timestamp original format
	 * @param format pattern
	 * @param locale lang locale
	 * @return formatted date
	 */
	public static String timestamp2Date(String timestamp, String format, String locale) {
		return timestamp2Date(timestamp, format, getLocale(locale));
	}

	/**
	 * Format timestamp to date with default locale.
	 * @param timestamp original format
	 * @param format pattern
	 * @return formatted date
	 */
	public static String timestamp2Date(String timestamp, String format) {
		return timestamp2Date(timestamp, format, (Locale) null);
	}

	/**
	 * Current time.
	 * @param format pattern
	 * @param locale lang locale
	 * @return formatted date
	 */
	public static String currentTime(String format, Locale locale) {
		try {
			final Date d = new Date();
			final SimpleDateFormat sdf;
			if (locale != null) {
				sdf = new SimpleDateFormat(format, locale);
			} else {
				sdf = new SimpleDateFormat(format);
			}
			return sdf.format(d);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Current time.
	 * @param format pattern
	 * @param locale lang locale
	 * @return formatted date
	 */
	public static String currentTime(String format, String locale) {
		return currentTime(format, getLocale(locale));
	}

	/**
	 * Current time.
	 * @param format pattern
	 * @return formatted date
	 */
	public static String currentTime(String format) {
		return currentTime(format, (Locale) null);
	}

	/**
	 * Format numbers.
	 * @param number original number
	 * @param format pattern
	 * @return formatted number
	 */
	public static String numberFormat(String number, String format) {
		if (number == null) {
			return "XXXXXX";
		}

		try {
			final DecimalFormat df = new DecimalFormat(format);
			final Long l = Long.parseLong(number);

			return df.format(l);
		} catch (NumberFormatException e) {
			return "XXXXXX";
		} catch (IllegalArgumentException e) {
			return "XXXXXX";
		} catch (NullPointerException e) {
			return "XXXXXX";
		}
	}

	/**
	 * Returns the value of a local parameter.
	 * @param context Component request context.
	 * @return The value of the parameter as a string or the empty string if not found.
	 */
	public static FreeMarkerFunction localParam(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return EMPTY;
				}
				final String arg = args.get(0);
				if (arg == null) {
					return EMPTY;
				}
				final LocalParams lp = context.getLocalParams();
				if (lp == null) {
					return EMPTY;
				}
				final Object value = lp.get(arg);
				return value != null ? value.toString() : EMPTY;
			}
		};
	}

	/**
	 * Returns the value of a portal property.
	 * @param context Component request context.
	 * @return The value of the parameter as a string or the empty string if not found.
	 */
	public static FreeMarkerFunction portalProperty(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final String name = arg(args, 0);
				final String fallback = arg(args, 1);
				final String value;
				if (name != null) {
					value = context.getPortal().getProperty(name);
				} else {
					value = null;
				}
				return safe(value, fallback);
			}
		};
	}

	/**
	 * Generate a impe3 uri
	 * @param context Component request context.
	 * @param builder uri builder
	 * @return uri string
	 */
	public static FreeMarkerFunction uri(final ComponentRequestContext context, final URIBuilderService builder) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return EMPTY;
				}
				final String arg = args.get(0);
				if (arg == null) {
					return EMPTY;
				}
				final String uri = builder.getURI(context, args.get(0));
				return uri != null ? returnUri(uri, args, 1) : arg;
			}
		};
	}

	/**
	 * Generate base uri
	 * @param context Component request context.
	 * @return base string
	 */
	public static FreeMarkerFunction baseUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					return EMPTY;
				}
				final String base = args.get(0);
				final String arg = args.get(1);
				if (base == null) {
					return arg;
				}
				if (arg == null) {
					return EMPTY;
				}
				final URI uri = context.getURIByBase(base, arg);
				return uri != null ? uri.toASCIIString() : arg;
			}
		};
	}

	/**
	 * Generate base uri (mode dependent)
	 * @param context Component request context.
	 * @return mode dependent base string
	 */
	public static FreeMarkerFunction mdBaseUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.size() < 2) {
					return EMPTY;
				}
				final String base = args.get(0);
				final String arg = args.get(1);
				if (base == null) {
					return arg;
				}
				if (arg == null) {
					return EMPTY;
				}
				final URI uri = context.getURIByMDBase(base, arg);
				return uri != null ? uri.toASCIIString() : arg;
			}
		};
	}

	/**
	 * Generate action uri
	 * @param context Component request context.
	 * @return action uri string
	 */
	public static FreeMarkerFunction action(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return null;
				}
				final Multimap<String, Object> parameters = LinkedListMultimap.create();
				final Iterator<String> i = args.iterator();
				final String name = i.next();
				while (i.hasNext()) {
					final String pn = i.next();
					if (i.hasNext()) {
						parameters.put(pn, i.next());
					}
				}
				final URI uri = context.getActionURI(name, parameters);
				return uri != null ? uri.toASCIIString() : EMPTY;
			}
		};
	}

	/**
	 * Generate an absolute action uri
	 * @param context Component request context.
	 * @return action uri string
	 */
	public static FreeMarkerFunction absAction(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return null;
				}
				final Multimap<String, Object> parameters = LinkedListMultimap.create();
				final Iterator<String> i = args.iterator();
				final String name = i.next();
				while (i.hasNext()) {
					final String pn = i.next();
					if (i.hasNext()) {
						parameters.put(pn, i.next());
					}
				}
				final URI uri = context.getAbsoluteActionURI(name, parameters);
				return uri != null ? uri.toASCIIString() : EMPTY;
			}
		};
	}

	/**
	 * Generate registered action uri
	 * @param context Component request context.
	 * @return action uri string
	 */
	public static FreeMarkerFunction registeredAction(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return null;
				}
				final Multimap<String, Object> parameters = LinkedListMultimap.create();
				final Iterator<String> i = args.iterator();
				final String name = i.next();
				while (i.hasNext()) {
					final String pn = i.next();
					if (i.hasNext()) {
						parameters.put(pn, i.next());
					}
				}
				final URI uri = context.getRegisteredActionURI(name, parameters);
				return uri != null ? uri.toASCIIString() : EMPTY;
			}
		};
	}

	/**
	 * Generate registered action uri
	 * @param context Component request context.
	 * @return action uri string
	 */
	public static FreeMarkerFunction absRegisteredAction(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return null;
				}
				final Multimap<String, Object> parameters = LinkedListMultimap.create();
				final Iterator<String> i = args.iterator();
				final String name = i.next();
				while (i.hasNext()) {
					final String pn = i.next();
					if (i.hasNext()) {
						parameters.put(pn, i.next());
					}
				}
				final URI uri = context.getAbsoluteRegisteredActionURI(name, parameters);
				return uri != null ? uri.toASCIIString() : EMPTY;
			}
		};
	}

	private static String returnUri(URI uri) {
		return uri != null ? uri.toASCIIString() : EMPTY;
	}

	private static int getInt(List<String> args, int index, int defaultValue) {
		if (args == null || args.size() <= index) {
			return defaultValue;
		}
		String a = args.get(index);
		if (a == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(a);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Return a uri string to an absolute page number
	 * @param context uri generator context
	 * @param pagination page position
	 * @return uri string
	 */
	public static FreeMarkerFunction absPage(final RenderContext context, final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				return returnUri(context.getAbsolutePageURI(pagination, getInt(args, 0, 1)));
			}
		};
	}

	public static RenderingPhaseObject ABS_PAGE = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return absPage(from, from.getPagination());
		}
	};

	public static RenderingPhaseObject CONTEXT = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return from;
		}
	};

	/**
	 * Return a uri string to an relative page number
	 * @param context uri generator context
	 * @param pagination page position
	 * @return uri string
	 */
	public static FreeMarkerFunction relPage(final RenderContext context, final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				return returnUri(context.getRelativePageURI(pagination, getInt(args, 0, 0)));
			}
		};
	}

	public static RenderingPhaseObject REL_PAGE = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return relPage(from, from.getPagination());
		}
	};

	/**
	 * Return an uri to the first page position
	 * @param context uri generator context
	 * @param pagination page position
	 * @return uri string
	 */
	public static FreeMarkerFunction firstPage(final RenderContext context, final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				return returnUri(context.getFirstPageURI(pagination));
			}
		};
	}

	public static RenderingPhaseObject FIRST_PAGE = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return firstPage(from, from.getPagination());
		}
	};

	/**
	 * Return an uri to the last page position
	 * @param context uri generator context
	 * @param pagination page position
	 * @return uri string
	 */
	public static FreeMarkerFunction lastPage(final RenderContext context, final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				return returnUri(context.getFirstPageURI(pagination));
			}
		};
	}

	public static RenderingPhaseObject LAST_PAGE = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return lastPage(from, from.getPagination());
		}
	};

	/**
	 * Return total records for list
	 * @param pagination page position
	 * @return total count
	 */
	public static FreeMarkerFunction total(final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (pagination != null) {
					final Integer records = pagination.getRecords();
					if (records != null) {
						return records.toString();
					}
				}
				return EMPTY;
			}
		};
	}

	/**
	 * Return total pages for list
	 * @param pagination page position
	 * @return pages count
	 */
	public static FreeMarkerFunction pages(final Pagination pagination) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (pagination != null) {
					final Integer pages = pagination.getPages();
					if (pages != null) {
						return pages.toString();
					}
				}
				return EMPTY;
			}
		};
	}

	private static Multimap<String, String> getParameters(List<String> args, int index) {
		if (args == null || (args.size() - index) < 2) {
			return ImmutableMultimap.of();
		}
		final int n = args.size();
		final Multimap<String, String> map = LinkedListMultimap.create(n / 2);
		for (int i = index; (args.size() - i) >= 2; i += 2) {
			String p = args.get(i);
			String v = args.get(i + 1);
			if (p != null && v != null) {
				map.put(p, v);
			}
		}
		return map;
	}

	private static String returnUri(URI uri, List<String> args, int index) {
		if (uri == null) {
			return EMPTY;
		}
		final Multimap<String, String> map = getParameters(args, index);
		if (map.isEmpty()) {
			return uri.toASCIIString();
		}
		final UriBuilder b = UriBuilder.fromUri(uri);
		for (Entry<String, ?> entry : map.entries()) {
			b.queryParam(entry.getKey(), entry.getValue());
		}
		return b.build().toASCIIString();
	}

	private static String returnUri(String uri, List<String> args, int index) {
		if (uri == null) {
			return EMPTY;
		}
		final Multimap<String, String> map = getParameters(args, index);
		if (map.isEmpty()) {
			return uri;
		}
		try {
			final UriBuilder b = UriBuilder.fromUri(uri);
			for (Entry<String, ?> entry : map.entries()) {
				b.queryParam(entry.getKey(), entry.getValue());
			}
			return b.build().toASCIIString();
		} catch (IllegalArgumentException e) {
			return uri;
		}
	}

	private static String returnUri(ComponentRequestContext context, PageKey key, List<String> args, int index) {
		if (key == null) {
			return EMPTY;
		}
		final URI uri = context.getURI(key);
		return returnUri(uri, args, index);
	}

	/**
	 * Return main page uri
	 * @param context Component request context.
	 * @return string main page uri
	 */
	public static FreeMarkerFunction mainUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				return returnUri(context, PageKey.main(), args, 0);
			}
		};
	}

	/**
	 * Return actual page uri
	 * @param context uri generator context
	 * @return string actual page uri
	 */
	public static FreeMarkerFunction thisUri(final RenderContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final URI uri = context.getSamePageURI(getParameters(args, 0));
				return returnUri(uri);
			}
		};
	}

	public static RenderingPhaseObject THIS_URI = new RenderingPhaseObject() {
		public Object apply(RenderContext from) {
			return firstPage(from, from.getPagination());
		}
	};

	private static String arg(List<String> args, int i) {
		if (args == null || args.size() <= i) {
			return null;
		}
		return args.get(i);
	}

	private static String safe(String s) {
		if (s != null) {
			return s;
		}
		return EMPTY;
	}

	private static String safe(String value, String fallback) {
		if (value != null) {
			return value;
		}
		return safe(fallback);
	}

	private static UUID getUUID(List<String> args, int i) {
		String arg = arg(args, i);
		if (arg == null) {
			return null;
		}
		try {
			return UUID.fromString(arg);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private static Category getCategory(final ComponentRequestContext context, List<String> args, int i) {
		final UUID id = getUUID(args, i);
		if (id == null) {
			return null;
		}
		return context.getPortal().getCategories().get(id);
	}

	private static Category getCategoryByPath(final ComponentRequestContext context, List<String> args, int i) {
		final String path = arg(args, i);
		;
		if (path == null) {
			return null;
		}
		return context.getPortal().getCategories().getByPath(path, false, false);
	}

	private static ContentType getContentType(final ComponentRequestContext context, List<String> args, int i) {
		final UUID id = getUUID(args, i);
		if (id == null) {
			return null;
		}
		return context.getPortal().getContentTypes().get(id);
	}

	/**
	 * Return string uri for category page
	 * @param context Component request context.
	 * @return category page string uri
	 */
	public static FreeMarkerFunction categoryUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final Category category = getCategory(context, args, 0);
				if (category == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.navigation(category);
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Return string uri for category page by path
	 * @param context Component request context.
	 * @return category page string uri by path
	 */
	public static FreeMarkerFunction categoryByPathUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final Category category = getCategoryByPath(context, args, 0);
				if (category == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.navigation(category);
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Return string uri for content page
	 * @param context Component request context.
	 * @return content page string uri
	 */
	public static FreeMarkerFunction contentUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final String id = arg(args, 1);
				if (id == null) {
					return EMPTY;
				}
				final ContentType c = getContentType(context, args, 0);
				if (c == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.content(ContentKey.of(c, id));
				return returnUri(context, key, args, 2);
			}
		};
	}

	/**
	 * Return string uri for content type page
	 * @param context Component request context.
	 * @return content type page string uri
	 */
	public static FreeMarkerFunction contentTypeUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final ContentType c = getContentType(context, args, 0);
				if (c == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.contentType(c);
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Return string uri for special page
	 * @param context Component request context.
	 * @return special page string uri
	 */
	public static FreeMarkerFunction specialUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final String name = arg(args, 0);
				if (name == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.special(name);
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Returns uri to concrete content on a concrete category
	 */
	public static FreeMarkerFunction contentWithCategoryUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final String id = arg(args, 2);
				if (id == null) {
					return EMPTY;
				}
				final ContentType contentType = getContentType(context, args, 1);
				if (contentType == null) {
					return EMPTY;
				}
				final Category category = getCategory(context, args, 0);
				if (category == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.content(NavigationKey.category(category), ContentKey.of(contentType, id));
				return returnUri(context, key, args, 3);
			}
		};
	}

	/**
	 * Return uri to content type with category
	 * @param context Component request context.
	 * @return uri to content type with category
	 */
	public static FreeMarkerFunction contentTypeWithCategoryUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final ContentType contentType = getContentType(context, args, 1);
				if (contentType == null) {
					return EMPTY;
				}
				final Category category = getCategory(context, args, 0);
				if (category == null) {
					return EMPTY;
				}
				final PageKey key = PageKey.contentType(NavigationKey.category(category), contentType);
				return returnUri(context, key, args, 2);
			}
		};
	}

	/**
	 * Returns uri to concrete content with navigation
	 */
	public static FreeMarkerFunction contentWithNavigationUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final String id = arg(args, 1);
				if (id == null) {
					return EMPTY;
				}
				final ContentType contentType = getContentType(context, args, 0);
				if (contentType == null) {
					return EMPTY;
				}
				NavigationKey nk = null;
				final PageKey actual = context.getPageKey();
				if (actual instanceof WithNavigation) {
					NavigationKey pnk = ((WithNavigation) actual).getNavigationKey();
					if (pnk != null) {
						nk = pnk.withoutContentType();
					}
				}
				final PageKey key = PageKey.content(nk, ContentKey.of(contentType, id));
				return returnUri(context, key, args, 2);
			}
		};
	}

	/**
	 * Returns uri to content type with navigation
	 */
	public static FreeMarkerFunction contentTypeWithNavigationUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final ContentType contentType = getContentType(context, args, 0);
				if (contentType == null) {
					return EMPTY;
				}
				NavigationKey nk = null;
				final PageKey actual = context.getPageKey();
				if (actual instanceof WithNavigation) {
					NavigationKey pnk = ((WithNavigation) actual).getNavigationKey();
					if (pnk != null) {
						nk = pnk.withoutContentType();
					}
				}
				final PageKey key = PageKey.contentType(nk, contentType);
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Returns the URI to contents current navigation key. If no navigation is found, the main page URI is returned.
	 */
	public static FreeMarkerFunction currentNavigationUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final NavigationKey nk = context.getNavigationKey();
				final PageKey key;
				if (nk == null) {
					key = PageKey.main();
				} else {
					key = PageKey.navigation(nk);
				}
				return returnUri(context, key, args, 1);
			}
		};
	}

	/**
	 * Returns the absolute URI to the requested page key. Query parameters in the request are not included.
	 */
	public static FreeMarkerFunction requestedPageBaseAbsUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				URI uri = context.getAbsoluteURI(context.getRoute());
				return returnUri(uri, args, 1);
			}
		};
	}

	/**
	 * Returns the absolute URI to the requested page key. Query parameters in the request are included.
	 */
	public static FreeMarkerFunction requestedPageAbsUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				final Multimap<String, String> qp = LinkedListMultimap.create();
				final RequestParams rp = context.getRequestParams();
				for (String name : rp.getNames()) {
					qp.putAll(name, rp.get(name));
				}
				URI uri = context.getAbsoluteURI(context.getRoute(), qp);
				return returnUri(uri, args, 1);
			}
		};
	}

	/**
	 * Returns portal relative uri
	 */
	public static FreeMarkerFunction portalRelativeUri(final ComponentRequestContext context) {
		return new FreeMarkerFunction() {
			public Object apply(List<String> args) throws TemplateModelException {
				if (args == null || args.isEmpty()) {
					return mainUri(context).apply(null);
				}
				String path = arg(args, 0);
				if (path == null || path.length() == 0) {
					return mainUri(context).apply(null);
				}
				try {
					final UriBuilder b;
					if (path.startsWith("-/") || path.startsWith("/-/")) {
						b = context.getBase();
					} else {
						URI base = context.getPortalRelativeURI(EMPTY, null);
						if (base == null) {
							return EMPTY;
						}
						b = UriBuilder.fromUri(base);
					}
					URI uri = new URI(path);
					String p = uri.getPath();
					if (StringUtils.hasText(p)) {
						b.path(p);
					}
					String q = uri.getQuery();
					if (StringUtils.hasText(q)) {
						b.replaceQuery(q);
					}
					return returnUri(b.build(), args, 1);
				} catch (Exception e) {
					return EMPTY;
				}
			}
		};
	}

	/** timestamp2Date static function */
	public static final FreeMarkerFunction TIMESTAMP2DATE = new FreeMarkerFunction() {
		public Object apply(List<String> args) throws TemplateModelException {
			String locale = null;
			if (args.size() > 2) {
				locale = args.get(2);
			}
			return timestamp2Date(args.get(0), args.get(1), locale);
		}
	};

	/** currentTime static function */
	public static final FreeMarkerFunction CURRENT_TIME = new FreeMarkerFunction() {
		public Object apply(List<String> args) throws TemplateModelException {
			final String format = arg(args, 0);
			if (format == null) {
				return "";
			}
			String locale = arg(args, 1);
			return currentTime(format, locale);
		}
	};

	/** numberFormat static function */
	public static final FreeMarkerFunction NUMBERFORMAT = new FreeMarkerFunction() {
		public Object apply(List<String> args) throws TemplateModelException {
			return numberFormat(args.get(0), args.get(1));
		}
	};

	/** Adds static functions (timestamp2Date and numberFormat). */
	static {
		ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
		builder.put("timestamp2Date", TIMESTAMP2DATE);
		builder.put("numberFormat", NUMBERFORMAT);
		builder.put("currentTime", CURRENT_TIME);
		FUNCTIONS = builder.build();

		try {
			freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_JAVA);
		} catch (Exception e) {
			LoggerFactory.getLogger(FreeMarker.class).warn("Unable to set FreeMarker logger to SLF4J", e);
		}

	}

}
