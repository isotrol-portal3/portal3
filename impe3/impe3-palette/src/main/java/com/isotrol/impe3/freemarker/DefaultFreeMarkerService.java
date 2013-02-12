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


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.util.StringUtils;

import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.connectors.uri.URIBuilderService;
import com.isotrol.impe3.freemarker.wrap.PortalObjectWrapper;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;


/**
 * Default implementation of the IMPE3 Interface to the freemarker templating engine.
 * 
 * @author Andres Rodriguez
 */
public class DefaultFreeMarkerService extends AbstractFreeMarkerService {
	private static final String IMG_URI = "imgUri";

	private final Configuration configuration;
	/** Image URI builder. */
	private URIBuilderService imgUri;
	/** Engine mode. */
	private final EngineMode engineMode;
	/** Whether to use wrapper. */
	private final boolean wrap;

	/**
	 * Constructs the service.
	 * 
	 * @param loader File loader.
	 */
	public DefaultFreeMarkerService(FileLoader loader, TemplateLoader externalTemplateLoader, EngineMode engineMode,
		FreeMarkerConfiguration configuration) {
		final TemplateLoader templateLoader;
		boolean external = (externalTemplateLoader != null && !(externalTemplateLoader instanceof EmptyTemplateLoader));
		boolean internal = configuration.templateBundle() != null;
		if (internal && external) {
			templateLoader = new ChainingTemplateLoader(new ImpeTemplateLoader(loader, configuration.templateBundle()),
				externalTemplateLoader);
		} else if (internal) {
			templateLoader = new ImpeTemplateLoader(loader, configuration.templateBundle());
		} else if (external) {
			templateLoader = externalTemplateLoader;
		} else {
			templateLoader = new EmptyTemplateLoader();
		}
		final Configuration c = new Configuration();
		c.setTemplateLoader(templateLoader);
		c.setDefaultEncoding(configuration.encoding() != null ? configuration.encoding() : "UTF-8");

		if (configuration.numberformat() != null && StringUtils.hasText(configuration.numberformat())) {
			c.setNumberFormat(configuration.numberformat());
		}
		this.engineMode = engineMode;
		this.wrap = configuration.wrap();
		this.configuration = c;

	}

	public void setImgUri(URIBuilderService imgUri) {
		this.imgUri = imgUri;
	}

	private void safeWrite(Writer output, String s) {
		try {
			output.write(s);
		} catch (Exception e) {
			// Nothing.
		}
	}

	private void process(Exception e, Writer output) {
		if (EngineMode.OFFLINE == engineMode) {
			safeWrite(output, e.getMessage());
		} else {
			safeWrite(output, "");
		}
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#process(java.lang.String,
	 * com.isotrol.impe3.api.component.RenderContext, java.util.Locale, java.lang.Object, java.io.Writer,
	 * freemarker.template.ObjectWrapper, boolean)
	 */
	public void process(String template, RenderContext context, Locale locale, Object dataModel, Writer output,
		ObjectWrapper wrapper, boolean safeMode) throws Exception {
		checkNotNull(context);
		if (locale == null) {
			locale = context.getLocale();
		}
		if (wrapper == null && wrap) {
			wrapper = PortalObjectWrapper.create(context);
		}
		try {
			final StringWriter sw = new StringWriter();
			configuration.getTemplate(template, locale).process(dataModel, sw, wrapper);
			output.write(sw.toString());
		} catch (Exception e) {
			if (safeMode) {
				process(e, output);
			}
			throw e;
		}
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#createModel(com.isotrol.impe3.api.component.RenderContext)
	 */
	public Model createModel(RenderContext context) {
		final Model model = Model.create();
		if (imgUri != null) {
			model.set("imgUri", FreeMarker.uri(context, imgUri));
		}
		model.set(Model.COMPONENT_ID, context.getComponentId().toString());
		model.set(Model.CONTEXT, context);
		model.set("localParam", FreeMarker.localParam(context));
		model.set("portalProperty", FreeMarker.portalProperty(context));
		model.set("actionUri", FreeMarker.action(context));
		model.set("absActionUri", FreeMarker.absAction(context));
		model.set("registeredActionUri", FreeMarker.registeredAction(context));
		model.set("absRegisteredActionUri", FreeMarker.absRegisteredAction(context));
		model.set("baseUri", FreeMarker.baseUri(context));
		model.set("mdBaseUri", FreeMarker.mdBaseUri(context));
		model.set("mainUri", FreeMarker.mainUri(context));
		model.set("thisUri", FreeMarker.thisUri(context));
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
		return model;
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#decorateRenderModel(com.isotrol.impe3.api.component.RenderContext,
	 * com.isotrol.impe3.api.component.TemplateModel)
	 */
	public void decorateRenderModel(RenderContext context, TemplateModel model) {
		// Img Uri
		if (!model.containsKey(IMG_URI) && imgUri != null) {
			model.put("imgUri", FreeMarker.uri(context, imgUri));
		}
		final Map<String, Object> map = Maps.newHashMap();
		for (Entry<String, Object> e : Maps.filterValues(model, Predicates.instanceOf(RenderingPhaseObject.class))
			.entrySet()) {
			RenderingPhaseObject rpo = (RenderingPhaseObject) e.getValue();
			Object val = rpo.apply(context);
			map.put(e.getKey(), val);
		}
		model.putAll(map);
	}

}
