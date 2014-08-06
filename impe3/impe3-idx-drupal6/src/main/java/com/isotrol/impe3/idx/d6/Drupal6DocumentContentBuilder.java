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
package com.isotrol.impe3.idx.d6;


import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.isotrol.impe3.idx.Encoder;
import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.d6.Drupal6Content.Drupal6ContentBuilder;
import com.isotrol.impe3.idx.d6.api.Drupal6Schema;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.core.DocumentBuilder;


/**
 * Drupal6 Document Content Builder
 */
public class Drupal6DocumentContentBuilder implements DocumentContentBuilder<Task> {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/** Prefix to identify a bean function */
	private static final String PREFIX_BEAN = "bean(";

	/** Default encoding utf-8 */
	private String encoding = "UTF-8";

	/** If set, only index that locale */
	private Locale lang;

	private boolean compress = true;

	private IndexConfiguration indexResourceTypes;

	private LocalMappingsService mappingsService;

	private Drupal6DatabaseReader databaseReader;

	private Map<String, IndexCommand> indexCommandBeans = Collections.emptyMap();

	/**
	 * @see com.isotrol.impe3.idx.d6.DocumentContentBuilder#createDocument(java.lang.Object)
	 */
	public Document[] createDocuments(Task task) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Creating document for {} task. ", task.getId());
		}

		final Drupal6Content content = readContent(task.getId());

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Content {} created, so translating...", content.getNid());
		}

		final Document[] doc = translator.apply(content);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Generate {} document(s) for content {}", doc.length, content.getNid());
		}

		return doc;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setLang(Locale lang) {
		this.lang = lang;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public void setIndexResourceTypes(IndexConfiguration indexResourceTypes) {
		this.indexResourceTypes = indexResourceTypes;
	}

	public void setMappingsService(LocalMappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	public void setDatabaseReader(Drupal6DatabaseReader databaseReader) {
		this.databaseReader = databaseReader;
	}

	public void setIndexCommandBeans(Map<String, IndexCommand> indexCommandBeans) {
		this.indexCommandBeans = indexCommandBeans;
	}

	/**
	 * Reads content from Drupal 6 database.
	 * @param id Drupal database resource uuid
	 * @return Drupal6Content bean.
	 */
	private Drupal6Content readContent(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Ask {} id document to database. ", id);
		}

		final Drupal6ContentBuilder builder = databaseReader.createBuilder(id);

		if (logger.isTraceEnabled()) {
			logger.trace("Builder for content {} created. ", id);
		}

		final List<String> categories = this.databaseReader.readContentCategories(id);
		for (String category : categories) {
			builder.addCategory(category);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Building {} id.", id);
		}

		return builder.build();
	}

	private final Function<Drupal6Content, Document[]> translator = new Function<Drupal6Content, Document[]>() {

		/** Transform drupal6 content to lucene document */
		public Document[] apply(Drupal6Content input) {
			final Document[] documents;
			if (logger.isTraceEnabled()) {
				logger.trace("Applying translation to content {} ", input.getNid());
			}

			if (lang != null) {
				documents = new Document[1];

				documents[0] = getDocument(input, lang);
			} else {
				final Set<Locale> locales = indexResourceTypes.getLanguages();
				documents = new Document[locales.size()];

				int i = 0;
				for (Locale locale : locales) {
					documents[i] = getDocument(input, locale);
					i++;
				}
			}
			if (logger.isTraceEnabled()) {
				logger.trace("Content {} translated.", input.getNid());
			}
			return documents;
		}

		private Document getDocument(final Drupal6Content content, final Locale locale) {

			Document doc = null;

			logger.trace("Getting localized ({}) document for {}", locale, content.getNid());

			Element contentElement = (Element) content.getXml().getXML().getRootElement();
			Map<String,String> map=content.getFields();
			// if content has no data in the language to index, it won't be indexed.
			if (contentElement != null
				&& ((content.getLanguage() != null && content.getLanguage().equals(locale.getLanguage())) || (content
					.getLanguage() == null || content.getLanguage().equals("")))) {

				final DocumentBuilder builder = new DocumentBuilder();

				final ResourceTypeDescriptor descriptor = indexResourceTypes.getResourceTypeDescriptor(content
					.getType());

				final UUID mappedDescriptor = mappingsService.getContentType(descriptor.getName());

				if (mappedDescriptor != null) {
					builder.setNodeKey(NodeKey.of(mappedDescriptor, String.valueOf(content.getNid())));
				}
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry e = (Map.Entry)it.next();
					if(e.getKey().toString().indexOf("indexar")>0){
						builder.setField(e.getKey().toString(), e.getValue().toString(), true, false);	
					}
				}				
				
				Element elem = (Element) contentElement.copy();

				final Attribute contentLanguage = elem.getAttribute("language");

				if (contentLanguage != null) {
					elem.removeAttribute(contentLanguage);
				}

				final String xml = elem.toXML();
				builder.setBytes(xml.getBytes(), compress);
				builder.setText(xml);

				logger.trace("Xml for {} builded. ", content.getNid());

				addContentFields(builder, content, locale);

				builder.setField(Drupal6Schema.TYPE, "CONTENT", true, false);				
				
				builder.setMime("text/html; charset=" + encoding);
				builder.setField(Drupal6Schema.CONTENT_TYPE, descriptor.getName(), true, false);

				addCustomFields(builder, content, elem, locale);

				List<String> categories = content.getCategories();
				Set<String> scat = new HashSet<String>();

				for (String cat : categories) {
					scat.add(cat);
					builder.setField(Drupal6Schema.CATEGORY, cat, true, false);
				}

				final nu.xom.Document d = new nu.xom.Document((Element) contentElement.copy());
				
				Set<UUID> mappedCategories = mappingsService.getCategories(descriptor.getName(), String.valueOf(content
					.getNid()), scat, d);
				for (UUID category : mappedCategories) {
					builder.addCategory(category);
				}
				
				final Set<String> smapped = mappingsService.getSets(descriptor.getName(), String.valueOf(content
					.getNid()), scat, d);
				for (String set : smapped) {
				    builder.addSet(set);
				}				
				
				doc = builder.get();
			} else {
				logger.warn("Nothing to do cause we've no content data for {} in {}", content.getNid(), locale);
			}

			logger.trace("Translation finished for {} ", content.getNid());

			return doc;
		}

		private void addContentFields(final DocumentBuilder builder, final Drupal6Content content, final Locale locale) {
			//builder.addLocale(locale.toString());

			builder.setField(Drupal6Schema.ID, String.valueOf(content.getNid()), true, false);
			builder.setField(Drupal6Schema.TRANSLATIONKEY, String.valueOf(content.getTnid()), true, false);
			builder.setField(Drupal6Schema.DATE_CREATED, content.getCreated(), true, false);
			builder.setField(Drupal6Schema.DATE_LAST_MODIFIED, content.getChanged(), true, false);
		}

		private void addCustomFields(final DocumentBuilder builder, final Drupal6Content content, final Node node,
			final Locale locale) {

			builder.setTitle(Encoder.escapeXml(content.getTitle()));

			final Set<ResourceTypeDescriptor.Field> customFields = indexResourceTypes.getResourceTypeDescriptor(
				content.getType()).getCustomFields();

			for (ResourceTypeDescriptor.Field field : customFields) {
				final String name = field.getName();

				final List<String> values = readCustomFieldValue(content, node, field);
				if (values != null && !values.isEmpty()) {

					for (String value : values) {
						builder.setField(name, Encoder.escapeXml(value), field.isStored(), field.isTokenized());
					}
				}
			}
		}

		private List<String> readCustomFieldValue(final Drupal6Content content, final Node node,
			final ResourceTypeDescriptor.Field field) {
			final String kind = field.getValue();
			Object value = null;

			if (kind != null && !kind.trim().equals("")) {
				// el valor se obtiene de una propiedad del recurso
				value = node.query(kind);
			}

			List<String> result;
			// Ya tenemos el valor inicial. El siguiente paso consiste en
			// aplicarle la función si esta definido asi
			final String function = field.getFunction();
			if (function != null && !"".equals(function.trim())) {
				result = applyFunctionToCustomField(function, content, node, value, field);
			} else {
				result = applyDefaultFunctionToCustomField(value);
			}

			return result;
		}

		private List<String> applyFunctionToCustomField(final String function, final Drupal6Content content,
			final Node node, final Object value, final ResourceTypeDescriptor.Field field) {

			List<String> res = null;

			// Comprobamos si lo que tenemos que cargar es un bean o una clase para ejecutar una funcion
			if (function.startsWith(PREFIX_BEAN)) {
				// Cargamos el bean
				IndexCommand bean = (IndexCommand) indexCommandBeans.get(function.substring((PREFIX_BEAN).length(),
					function.length() - 1));
				if (value != null) {
					res = Arrays.asList(bean.execute((String) value));
				} else {
					res = Arrays.asList(bean.execute(node));
				}
			} else {
				int i = function.lastIndexOf('.');
				String nombreClase = null;
				if (i != -1) {
					nombreClase = function.substring(0, i);
				}
				Class<?>[] arrayC = new Class<?>[1];
				arrayC[0] = Object.class;
				Method m = null;
				try {
					Class<?> c = null;
					if (nombreClase != null) {
						c = Class.forName(nombreClase);
						m = c.getDeclaredMethod(function.substring(i + 1), arrayC);
					} else {
						m = this.getClass().getDeclaredMethod(function, arrayC);
					}
					Object[] arrayO = new Object[1];
					if (value != null) {
						arrayO[0] = value;
					} else {
						arrayO[0] = node;
					}
					if (nombreClase != null) {
						res = (List<String>) m.invoke(c.newInstance(), arrayO);
					} else {
						res = (List<String>) m.invoke(this, arrayO);
					}
				} catch (Exception e) {
					logger.warn("Failure at {} function for {} content", function, content.getNid());
					logger.debug("Error trace", e);
				}

			}
			return res;
		}

		private List<String> applyDefaultFunctionToCustomField(Object originalValue) {

			final List<String> value;

			if (originalValue instanceof Nodes) {
				value = new LinkedList<String>();
				final Nodes nodes = (Nodes) originalValue;
				for (int i = 0; i < nodes.size(); i++) {
					final Node node = nodes.get(i);
					if (node instanceof Attribute) {
						value.add(((Attribute) node).getValue());
					} else if (node instanceof Element) {
						value.add(((Element) node).getValue());
					}
				}
			} else if (originalValue instanceof String) {
				value = Arrays.asList((String) originalValue);
			} else {
				value = (List<String>) originalValue;
			}

			return value;
		}
	};
}
