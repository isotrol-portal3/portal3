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


import static java.lang.Math.max;
import static java.lang.Math.min;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.content.Listing;

import freemarker.ext.dom.NodeModel;


/**
 * IMPE3 FreeMarker Model.
 * @author Andres Rodriguez
 */
public class Model extends ForwardingMap<String, Object> implements TemplateModel {
	/** Metadata model item. */
	public static final String METADATA = "metadata";
	/** XML model item. */
	public static final String XML = "xml";
	/** HREF local value model item. */
	public static final String HREF = "href";
	/** Listing page model item. */
	public static final String PAGE = "page";
	/** Pagination model item. */
	public static final String PAGINATION = "pagination";
	/** Page marks model item. */
	public static final String PAGEMARKS = "pageMarks";
	/** Component id model item. */
	public static final String COMPONENT_ID = "componentId";
	/** Render context. */
	public static final String CONTEXT = "context";

	/**
	 * Creates a new freemarker model with the default functions at the component level if no model is provided.
	 * @param model Current model.
	 * @param context Component request context.
	 * @return A new model with the default functions if the first argument is {@code null}, the first argument
	 * otherwise.
	 */
	public static TemplateModel createComponentModel(TemplateModel model, ComponentRequestContext context) {
		if (model != null) {
			return model;
		}
		return createComponentModel(context);
	}

	/**
	 * Creates a new freemarker model with the default functions at the component level.
	 * @param context Component request context.
	 * @return A new model with the default functions.
	 */
	public static TemplateModel createComponentModel(ComponentRequestContext context) {
		final Model model = create();
		model.set(Model.COMPONENT_ID, context.getComponentId().toString());
		model.set(Model.CONTEXT, FreeMarker.CONTEXT);
		model.set("localParam", FreeMarker.localParam(context));
		model.set("portalProperty", FreeMarker.portalProperty(context));
		model.set("actionUri", FreeMarker.action(context));
		model.set("absActionUri", FreeMarker.absAction(context));
		model.set("registeredActionUri", FreeMarker.registeredAction(context));
		model.set("absRegisteredActionUri", FreeMarker.absRegisteredAction(context));
		model.set("baseUri", FreeMarker.baseUri(context));
		model.set("mdBaseUri", FreeMarker.mdBaseUri(context));
		model.set("mainUri", FreeMarker.mainUri(context));
		model.set("thisUri", FreeMarker.THIS_URI);
		model.set("categoryUri", FreeMarker.categoryUri(context));
		model.set("categoryByPathUri", FreeMarker.categoryByPathUri(context));
		model.set("contentUri", FreeMarker.contentUri(context));
		model.set("contentTypeUri", FreeMarker.contentTypeUri(context));
		model.set("specialUri", FreeMarker.specialUri(context));
		model.set("contentWithCategoryUri", FreeMarker.contentWithCategoryUri(context));
		model.set("contentTypeWithCategoryUri", FreeMarker.contentTypeWithCategoryUri(context));
		model.set("contentWithNavigationUri", FreeMarker.contentWithNavigationUri(context));
		model.set("contentTypeWithNavigationUri", FreeMarker.contentTypeWithNavigationUri(context));
		model.set("currentNavigationUri", FreeMarker.currentNavigationUri(context));
		model.set("requestedPageBaseAbsUri", FreeMarker.requestedPageBaseAbsUri(context));
		model.set("requestedPageAbsUri", FreeMarker.requestedPageAbsUri(context));
		model.set("portalRelativeUri", FreeMarker.portalRelativeUri(context));
		// Pagination
		final Pagination p = context.getPagination();
		if (p != null) {
			model.set(Model.PAGINATION, p).set("absPage", FreeMarker.ABS_PAGE).set("relPage", FreeMarker.REL_PAGE)
				.set("firstPage", FreeMarker.FIRST_PAGE).set("lastPage", FreeMarker.LAST_PAGE)
				.set("total", FreeMarker.total(p)).set("pages", FreeMarker.pages(p));
		}
		return model;

	}

	public static NodeModel loadXML(InputStream input) {
		if (input == null) {
			return null;
		}
		try {
			final InputSource source = new InputSource(input);
			return NodeModel.parse(source);
		} catch (Exception e) {
			e.printStackTrace(); // TODO: log
			return null;
		}
	}

	public static NodeModel loadXML(byte[] input) {
		if (input != null) {
			return loadXML(new ByteArrayInputStream(input));
		}
		return null;
	}

	private final Map<String, Object> map = Maps.newHashMap();

	/**
	 * Creates a new model with the standard functions.
	 * @return The created model.
	 */
	public static Model create() {
		final Model model = new Model();
		model.putAll(FreeMarker.getFunctions());
		return model;
	}

	/** Empty model Constructor. */
	public Model() {
	}

	@Override
	protected Map<String, Object> delegate() {
		return map;
	}

	public Model set(String key, Object value) {
		put(key, value);
		return this;
	}

	public Model setIfAbsent(String key, Object value) {
		if (!containsKey(key)) {
			put(key, value);
		}
		return this;
	}

	public Model setXML(String key, InputStream input) {
		return set(key, loadXML(input));
	}

	public Model setXML(String key, byte[] input) {
		return set(key, loadXML(input));
	}

	public Model setXML(InputStream input) {
		return set(XML, loadXML(input));
	}

	public Model setXML(byte[] input) {
		return set(XML, loadXML(input));
	}

	public Model setMetadata(Object value) {
		return set(METADATA, value);
	}

	public Model setPage(RenderContext context, Listing<?> page) {
		if (page != null) {
			setPagination(context, page.getPagination());
		}
		return set(PAGE, page);
	}

	public Model setPagination(RenderContext context, Pagination pagination) {
		return set(PAGINATION, pagination).set("absPage", FreeMarker.absPage(context, pagination))
			.set("relPage", FreeMarker.relPage(context, pagination))
			.set("firstPage", FreeMarker.firstPage(context, pagination))
			.set("lastPage", FreeMarker.lastPage(context, pagination)).set("total", FreeMarker.total(pagination))
			.set("pages", FreeMarker.pages(pagination));
	}

	public Model setPageMarks(RenderContext context, Pagination pagination, Integer pagesBefore, Integer pagesAfter) {
		if (pagination == null) {
			return this;
		}
		final int before = (pagesBefore != null && pagesBefore >= 0) ? pagesBefore : 0;
		final int after = (pagesAfter != null && pagesAfter >= 0) ? pagesAfter : 0;
		final int current = Math.max(1, pagination.getPage());
		final int first = Math.max(1, current - before);
		final int last;
		Integer pages = pagination.getPages();
		if (pages != null) {
			last = max(min(pages, current + after), current);
		} else {
			last = current;
		}
		final int n = last - first + 1;
		final List<Integer> marks = Lists.newArrayListWithCapacity(n);
		for (int i = first; i <= last; i++) {
			marks.add(i);
		}
		return set(PAGEMARKS, marks);
	}

}
