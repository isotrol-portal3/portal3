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
package com.isotrol.impe3.idx.oc7;


import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.lucene.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Function;
import com.isotrol.impe3.idx.Encoder;
import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.Task;
import com.isotrol.impe3.idx.XML;
import com.isotrol.impe3.idx.XmlStripper;
import com.isotrol.impe3.idx.oc.Attached;
import com.isotrol.impe3.idx.oc.DocumentContentBuilder;
import com.isotrol.impe3.idx.oc.FileTypes;
import com.isotrol.impe3.idx.oc.IndexCommand;
import com.isotrol.impe3.idx.oc.IndexConfiguration;
import com.isotrol.impe3.idx.oc.OpenCmsContent;
import com.isotrol.impe3.idx.oc.OpenCmsContent.OpenCmsContentBuilder;
import com.isotrol.impe3.idx.oc.ResourceTypeDescriptor;
import com.isotrol.impe3.idx.oc.extractors.ExtractorMsExcel;
import com.isotrol.impe3.idx.oc.extractors.ExtractorMsWord;
import com.isotrol.impe3.idx.oc.extractors.ExtractorPdf;
import com.isotrol.impe3.idx.oc7.api.OpenCms7Schema;
import com.isotrol.impe3.nr.api.ISO9075;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;
import com.isotrol.impe3.nr.core.DocumentBuilder;


/**
 * TODO: Refactor needed!!!!!!! Just a copy of com.isotrol.index.server.opencms.common.NodoDocument
 * 
 * 
 * @author Alejandro Guerra
 * @author Rafael Sepúlveda
 * @author Emilio Escobar Reyero
 * @modified Juan Manuel Valverde Ramírez
 */
public class OpenCms7DocumentContentBuilder implements DocumentContentBuilder<Task>, InitializingBean {

	final Logger logger = LoggerFactory.getLogger(getClass());

	// TODO extract to Api ¿?
	/** */
	private static final String PROP_TITLE = "Title";
	private static final String PROP_TYPE = "DocType";
	private static final String PROP_NAVTEXT = "NavText";

	private static final String GROUPPATH_SEPARATOR = "|";
	private static final String ID_SEPARATOR = ":";

	private static final String BEAN_PREFIX = "bean";

	/** Default encoding utf-8 */
	private String encoding = "UTF-8";
	/** If set, only index that locale */
	private Locale lang;
	/** Categories base path */
	private String categoriesBase = "/system/categories/";
	/** Index default lang when isn't translate */
	private boolean indexDefaultLanguageContent = true;

	private boolean readAttachments = true;

	private boolean compress = true;

	private IndexConfiguration indexResourceTypes;
	private LocalMappingsService mappingsService;
	private OpenCms7DatabaseReader databaseReader;
	private Map<String, IndexCommand> indexCommandBeans = Collections.emptyMap();

	private static final ThreadLocal<Map<String, Pattern>> PATTERNS = new ThreadLocal<Map<String, Pattern>>();

	/**
	 * Init patterns
	 */
	public void afterPropertiesSet() {
		PATTERNS.set(new HashMap<String, Pattern>());
	}

	private Map<String, Pattern> getPatters() {
		Map<String, Pattern> patterns = PATTERNS.get();

		if (patterns == null) {
			patterns = new HashMap<String, Pattern>();
			PATTERNS.set(new HashMap<String, Pattern>());
		}

		return patterns;
	}

	/** Returns the pattern for the current thread. */
	private Pattern patternLang() {
		Pattern pattern = getPatters().get("LANG");
		if (pattern == null) {
			pattern = getLanguageSuffixesPattern();
			Map<String, Pattern> map = PATTERNS.get();
			map.put("LANG", pattern);
			PATTERNS.set(map);
		}
		return pattern;
	}

	/** Returns the pattern for the current thread. */
	private Pattern patternEntity() {
		Pattern pattern = getPatters().get("ENTITY");
		if (pattern == null) {
			pattern = Pattern.compile("&[a-zA-z0-9]+;");
			final Map<String, Pattern> map = PATTERNS.get();
			map.put("ENTITY", pattern);

			PATTERNS.set(map);
		}

		return pattern;
	}

	private Pattern getLanguageSuffixesPattern() {
		final Pattern pattern;

		final StringBuilder sb = new StringBuilder("(.*)_(");
		boolean first = true;
		for (Locale locale : indexResourceTypes.getLanguages()) {
			if (first) {
				first = false;
			} else {
				sb.append("|");
			}
			sb.append("(");
			sb.append(locale);
			sb.append(")");
		}
		sb.append(")$");
		pattern = Pattern.compile(sb.toString());

		return pattern;
	}

	/**
	 * @see com.isotrol.impe3.idx.oc.DocumentContentBuilder#createDocument(java.lang.Object)
	 */
	public Document[] createDocuments(Task task) {
		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Creating document for {} task. ", task.getId());
		}

		final OpenCmsContent content = readContent(task.getId());

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Content {} created, so translating...", content.getId());
		}

		final Document[] doc = this.TRANSLATOR.apply(content);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("Generate {} document(s) for content {}", doc.length, content.getId());
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

	public void setReadAttachments(boolean readAttachments) {
		this.readAttachments = readAttachments;
	}

	public void setCategoriesBase(String categoriesBase) {
		this.categoriesBase = categoriesBase;
	}

	public void setIndexDefaultLanguageContent(boolean indexDefaultLanguageContent) {
		this.indexDefaultLanguageContent = indexDefaultLanguageContent;
	}

	public void setIndexResourceTypes(IndexConfiguration indexResourceTypes) {
		this.indexResourceTypes = indexResourceTypes;
	}

	public void setMappingsService(LocalMappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	public void setDatabaseReader(OpenCms7DatabaseReader databaseReader) {
		this.databaseReader = databaseReader;
	}

	public void setIndexCommandBeans(Map<String, IndexCommand> indexCommandBeans) {
		this.indexCommandBeans = indexCommandBeans;
	}

	/**
	 * Reads content from OpenCms data base.
	 * @param id OpenCms data base resource uuid
	 * @return OpenCms7Content bean.
	 */
	private OpenCmsContent readContent(String id) {
		if (logger.isDebugEnabled()) {
			logger.debug("Ask {} id document to database. ", id);
		}

		final OpenCmsContentBuilder builder = databaseReader.createBuilder(id);

		if (logger.isTraceEnabled()) {
			logger.trace("Builder for content {} created. ", id);
		}

		final ResourceTypeDescriptor descriptor = indexResourceTypes.getResourceTypeDescriptor(builder.getType());

		if (descriptor.isContent()) {
			if (logger.isTraceEnabled()) {
				logger.trace("We've content node for {} id. Reading bytes...", id);
			}

			final XML xml = databaseReader.readContentXml(id);
			builder.setXml(xml);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Reading properties for {} id.", id);
		}

		final List<String[]> properties = databaseReader.readContentProperties(id);
		for (String[] property : properties) {
			if (logger.isTraceEnabled()) {
				logger.trace("Id {} [{}, {}].", new Object[] {id, property[0], property[1]});
			}
			builder.addProperty(property[0], property[1]);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Reading categories for {} id.", id);
		}

		final List<String> categories = this.databaseReader.readContentCategories(id);
		for (String category : categories) {
			builder.addChannel(category);

		}

		if (readAttachments) {
			List<Attached> datasAttacheds = this.databaseReader.readAttachedIds(id);
			List<String> attachedString = new LinkedList<String>();
			if (datasAttacheds != null && datasAttacheds.size() > 0) {
				for (Attached attached : datasAttacheds) {
					if (attached != null) {

						InputStream is = null;
						try {

							is = this.databaseReader.readAttachedInputStream(attached.getId());

						} catch (Exception e) {
							this.logger.warn("Failure on load attaced for ID: {}", attached.getId());
							this.logger.debug("Error trace", e);
						}

						String textoCrudo = readAttached(is, attached.getType());
						if (textoCrudo != null) {
							attachedString.add(textoCrudo);
						}

					}
				}
			}
			if (attachedString != null) {
				// Añadimos los adjuntos al builder.
				for (String a : attachedString) {
					builder.addAttached(a);
				}
			}
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Building {} id.", id);
		}

		return builder.build();
	}

	/**
	 * Lee el contenido del fichero adjunto y lo pasa a texto crudo. Lee ficheros adjuntos del tipo: PDF, DOC y XLS
	 * @author Juan Manuel Valverde Ramírez
	 * @return
	 */
	private String readAttached(InputStream data, FileTypes type) {

		String result = "";
		try {
			if (type == FileTypes.PDF) {
				// I_CmsExtractionResult result = CmsExtractorPdf.getExtractor().extractText(data);

				result = ExtractorPdf.extract(data);
				return result;
			} else if (type == FileTypes.XLS) {
				// I_CmsExtractionResult xls = CmsExtractorMsExcel.getExtractor().extractText(data);
				String xls = ExtractorMsExcel.extractText(data);
				return xls;
			} else if (type == FileTypes.DOC) {
				// I_CmsExtractionResult result = CmsExtractorMsWord.getExtractor().extractText(data);
				// return result.getContent();

				String doc = ExtractorMsWord.extractText(data);
				return doc;
			}

		} catch (Exception e) {
			this.logger.debug(e.getMessage());
		}

		return null;
	}

	private final Function<OpenCmsContent, Document[]> TRANSLATOR = new Function<OpenCmsContent, Document[]>() {

		public Document[] apply(OpenCmsContent input) {
			final Document[] documents;
			if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
				OpenCms7DocumentContentBuilder.this.logger.trace("Applying translation to content {} ", input.getId());
			}

			if (OpenCms7DocumentContentBuilder.this.lang != null) {
				documents = new Document[1];

				documents[0] = getDocument(input, OpenCms7DocumentContentBuilder.this.lang);
			} else {
				final Set<Locale> locales = OpenCms7DocumentContentBuilder.this.indexResourceTypes.getLanguages();
				documents = new Document[locales.size()];

				int i = 0;
				for (Locale locale : locales) {
					documents[i] = getDocument(input, locale);
					i++;
				}
			}
			if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
				OpenCms7DocumentContentBuilder.this.logger.trace("Content {} translated.", input.getId());
			}
			return documents;
		}

		private Document getDocument(final OpenCmsContent content, final Locale locale) {
			final DocumentBuilder builder = new DocumentBuilder();

			if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
				OpenCms7DocumentContentBuilder.this.logger.trace("Getting localized ({}) document for {}", locale,
					content.getId());
			}

			final ResourceTypeDescriptor descriptor = OpenCms7DocumentContentBuilder.this.indexResourceTypes
				.getResourceTypeDescriptor(content.getType());

			final UUID mappedDescriptor = OpenCms7DocumentContentBuilder.this.mappingsService.getContentType(descriptor
				.getName());

			if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
				OpenCms7DocumentContentBuilder.this.logger.trace("Mapped content type from {} to {}", descriptor
					.getName(), mappedDescriptor.toString());
			}

			if (mappedDescriptor != null) {
				builder.setNodeKey(NodeKey.of(mappedDescriptor, content.getId()));
			}

			String navText = content.getProperties().get("NavText");
			builder.setField(OpenCms7Schema.IN_NAV, (navText != null && !"".equals(navText.trim())));

			if (descriptor.isContent()) {

				if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
					logger.trace("Parsing xml for {} ", content.getId());
				}

				Element contentElement = (Element) content.getXml().getNode("/*/*[@language='" + locale + "']");
				if (contentElement == null && OpenCms7DocumentContentBuilder.this.indexDefaultLanguageContent) {
					contentElement = (Element) content.getXml().getNode(
						"/*/*[@language='"
							+ OpenCms7DocumentContentBuilder.this.indexResourceTypes.getDefaultLanguage() + "']");
				}

				// if content has no data in the language to index, it won't be indexed.
				if (contentElement != null) {
					if (OpenCms7DocumentContentBuilder.this.logger.isTraceEnabled()) {
						OpenCms7DocumentContentBuilder.this.logger.trace("Xml data find for {} ", content.getId());
					}

					Element elem = (Element) contentElement.copy();

					processPublicXML(elem, content.getChannels());
					final Attribute contentLanguage = elem.getAttribute("language");

					if (contentLanguage != null) {
						elem.removeAttribute(contentLanguage);
					}

					final String xml = elem.toXML();
					builder.setBytes(xml.getBytes(), compress);

					String textoLimpio = XmlStripper.strip(xml.toCharArray());
					// builder.setText(xml);
					builder.setText(textoLimpio);

					if (logger.isTraceEnabled()) {
						logger.trace("Xml for {} builded. ", content.getId());
					}
					builder.setField(OpenCms7Schema.PATH, ISO9075.encode(content.getPath()), true, false);

					predecesors(builder, content.getPath());
					addCommonFields(builder, content, locale);

					builder.setField(OpenCms7Schema.TYPE, "CONTENT", true, false);
					builder.setMime("text/html; charset=" + encoding);
					builder.setField(OpenCms7Schema.CONTENT_TYPE, descriptor.getName(), true, false);

					if (logger.isTraceEnabled()) {
						logger.trace("Working with {} categories... ", content.getId());
					}

					List<String> channels = content.getChannels();
					Set<String> sch = new HashSet<String>();

					for (String ch : channels) {
						sch.add(ch);
						builder.setField(OpenCms7Schema.CHANNEL, ch, true, false);
					}

					final nu.xom.Document d = new nu.xom.Document((Element) contentElement.copy());
					
					Set<UUID> categories = mappingsService.getCategories(descriptor.getName(), content.getPath(), sch, d);
					for (UUID category : categories) {
						builder.addCategory(category);
					}

					final Set<String> smapped = mappingsService.getSets(descriptor.getName(), content.getPath(), sch, d);
					for (String set : smapped) {
					    builder.addSet(set);
					}
					
					// Adjuntos
					List<String> attacheds = content.getAttached();
					if (attacheds != null) {
						for (String attached : attacheds) {
							// builder.setField(OpenCms7Schema.ATTACHED, attached, true, true);
							builder.addAttachment(attached);
						}
					}

					addCustomFields(builder, content, elem, locale);
					addCustomGroups(builder, content, elem, locale, descriptor.getName());
				} else {
					logger.debug("Nothing to do cause we've no content data for {}", content.getId());
				}

			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("{} is a node without bytes (folder or category). ", content.getId());
				}
				addCommonFields(builder, content, locale);

				final String path = content.getPath();
				if (path.startsWith(categoriesBase)) {
					builder.setField(OpenCms7Schema.TYPE, "CATEGORY", true, false);
					builder.setField(OpenCms7Schema.PATH, ISO9075.encode(path.substring(categoriesBase.length() - 1)),
						true, false);
					predecesors(builder, path.substring(categoriesBase.length() - 1));
				} else {
					builder.setField(OpenCms7Schema.TYPE, "FOLDER", true, false);
					builder.setField(OpenCms7Schema.PATH, ISO9075.encode(path), true, false);
					predecesors(builder, path);
				}
			}

			builder.setDate(content.getDateReleased());
			if (content.getProperties().containsKey("description")) {
				builder.setDescription(content.getProperties().get("description"));
			}
			if (content.getProperties().containsKey("title")) {
				builder.setTitle(content.getProperties().get("title"));
			}

			if (logger.isTraceEnabled()) {
				logger.trace("Translation finished for {} ", content.getId());
			}

			return builder.get();
		}

		private void predecesors(final DocumentBuilder builder, final String path) {

			String parent = path;
			int posFinalSlash = path.lastIndexOf('/');
			if (posFinalSlash != -1) {
				parent = path.substring(0, posFinalSlash);

				StringBuilder sb = new StringBuilder();
				String[] folders = parent.split("/");
				String encodedPath = null;
				for (String folder : folders) {
					sb.append(folder);
					sb.append("/");
					encodedPath = ISO9075.encode(sb.toString());

					builder.setField(OpenCms7Schema.PREDECESORS, encodedPath, true, false);
				}
				builder.setField(OpenCms7Schema.PARENT, encodedPath, true, false);
			}
		}

		private void processPublicXML(Element xml, List<String> channels) {
			processWysiwyg(xml);
			processSingleLinks(xml, channels);
		}

		private void processSingleLinks(Element xml, List<String> channels) {
			// Another links like categories links
			final Nodes links = xml.query("//link");
			for (int i = 0; i < links.size(); i++) {
				final Element link = (Element) links.get(i);
				final boolean internal = Boolean.parseBoolean(link.getAttributeValue("internal"));
				final String type = link.getAttributeValue("type");
				if (internal && type != null && (type.equals("WEAK") || type.equals("STRONG"))) {
					final Element eUuid = link.getFirstChildElement("uuid");
					if (eUuid != null) {
						final String uuid = eUuid.getValue();
						final OpenCmsContent content = databaseReader.createBuilder(uuid).build();
						// Es un canal si pertenece a los canales asociados del contenido
						// if (content.getType() == 0 && content.getPath().startsWith(categoriesBase)) {
						if (content.getType() == 0 && channels != null && channels.contains(uuid)) {
							// It's a channel
							final Element categoryField = (Element) link.getParent();
							final Element categoryElement = readCategoryNode(uuid);
							categoryField.replaceChild(link, categoryElement);
						} else if (content.getType() == 1 || content.getType() == 2 || content.getType() == 3) {
							// It's a static resource
							// type = 1 - Plain
							// Type = 2 - Binary
							// Type = 3 - Image
							final Element target = link.getFirstChildElement("target");
							Node text = target.getChild(0);
							((Text) text).setValue(content.getPath());
						} else {
							// OpenCms Content mapped by portal, include mappedDescriptor.
							final ResourceTypeDescriptor descriptor = OpenCms7DocumentContentBuilder.this.indexResourceTypes
								.getResourceTypeDescriptor(content.getType());
							if (descriptor != null) {
								final UUID mappedDescriptor = OpenCms7DocumentContentBuilder.this.mappingsService
									.getContentType(descriptor.getName());
								if (mappedDescriptor != null) {
									link.addAttribute(new Attribute(PROP_TYPE, mappedDescriptor.toString()));
								}
							}
						}
					}
				}
			}
		}

		private Element readCategoryNode(String uuid) {
			final Element categoryElement = new Element("category");
			categoryElement.appendChild(uuid);
			// Title Added
			String titulo = null;
			final List<String[]> properties = databaseReader.readContentProperties(uuid);
			for (String[] property : properties) {
				if (PROP_NAVTEXT.equals(property[0])) {
					titulo = property[1];
				} else if (titulo == null && PROP_TITLE.equals(property[0])) {
					titulo = property[1];
				}
			}
			if (titulo != null) {
				categoryElement.addAttribute(new Attribute(PROP_TITLE, titulo));
			}
			return categoryElement;
		}

		protected void processWysiwyg(Element xml) {
			// Links wysiwyg
			final String xPath = "//links";

			// Xom document builder
			final Builder builder = new Builder();
			final Nodes listaWis = xml.query(xPath);

			for (int i = 0; i < listaWis.size(); i++) {
				final Node node = listaWis.get(i);
				if (node instanceof Element) {
					final Element xmlEtiquetaLinks = (Element) node;
					final Element xmlWis = (Element) xmlEtiquetaLinks.getParent();
					final Element contentElement = xmlWis.getFirstChildElement("content");

					String text = contentElement.getValue();

					xmlWis.removeChild(contentElement);

					final Elements listaLinks = xmlEtiquetaLinks.getChildElements("link");

					for (int j = 0; j < listaLinks.size(); j++) {
						final Element xmlLink = listaLinks.get(j);
						text = processLink(text, xmlLink);
					}

					// Integramos el wysiwyg en el contenido XML.
					text = processHtmlEntities(text);

					try {
						nu.xom.Document partialDoc = builder.build(new StringReader("<contenido>" + text
							+ "</contenido>"));

						xmlWis.appendChild(partialDoc.getRootElement().copy());

						xmlWis.removeChild(xmlEtiquetaLinks);

					} catch (ValidityException e) {
						logger.warn("Validating xml failure.");
					} catch (ParsingException e) {
						logger.warn("Parsing xml failure.");
					} catch (IOException e) {
						logger.warn("IO failure.");
					}
				}
			}
		}

		private String processLink(String textoOrig, Element xmlLink) {
			String texto = textoOrig;
			try {
				final String nombre = xmlLink.getAttributeValue("name");
				String target = xmlLink.getFirstChildElement("target").getValue();
				final String internal = xmlLink.getAttributeValue("internal");
				final String query;
				if (xmlLink.getFirstChildElement("query") != null) {
					query = xmlLink.getFirstChildElement("query").getValue();
				} else {
					query = null;
				}
				final String anchor;
				if (xmlLink.getFirstChildElement("anchor") != null) {
					anchor = xmlLink.getFirstChildElement("anchor").getValue();
				} else {
					anchor = null;
				}
				String type = xmlLink.getAttributeValue("type");

				final Element eUuid = xmlLink.getFirstChildElement("uuid");
				final String uuid;
				if (eUuid != null) {
					uuid = eUuid.getValue();
				} else {
					uuid = null;
				}

				if (internal != null && "true".equalsIgnoreCase(internal) && uuid != null && !uuid.equals("")) {
					final OpenCmsContent interno = databaseReader.createBuilder(uuid).build();
					// Si es una imagen no se accede a opencms.
					if (type != null && "IMG".equals(type)) {
						String ruta = target;
						// Leemos la ruta de la BD aunque en target este la ruta.
						ruta = interno.getPath();
						texto = texto.replaceFirst("%\\(" + nombre + "\\)", ruta);
					} else {
						// Si es un enlace externo
						if (interno.getType() == 5) {
							target = databaseReader.readContentBytes(uuid);
							texto = texto.replaceFirst("%\\(" + nombre + "\\)", target);
						}
						/*
						 * enlace a documento (tipos bynary y plain) Las Imagenes se incluyen aqui puesto que serán
						 * imagenes en un enlace, no en un img (Tratadas previamente)
						 */
						else if (interno.getType() == 2 || interno.getType() == 1 || interno.getType() == 3) {
							String ruta = target;
							ruta = interno.getPath();
							texto = texto.replaceFirst("%\\(" + nombre + "\\)\"", ruta + "\" tipo=\"documento\"");
						}
						// enlace a detalle contenido, mapeado en impe3
						else {
							String tipo = "";
							final ResourceTypeDescriptor descriptor = OpenCms7DocumentContentBuilder.this.indexResourceTypes
								.getResourceTypeDescriptor(interno.getType());
							if (descriptor != null) {
								final UUID mappedDescriptor = OpenCms7DocumentContentBuilder.this.mappingsService
									.getContentType(descriptor.getName());
								if (mappedDescriptor != null) {
									tipo = mappedDescriptor.toString();
								}
							}
							texto = texto.replaceFirst("%\\(" + nombre + "\\)\"", uuid + "\" tipo=\"contenido\" "
								+ PROP_TYPE + "=\"" + tipo + "\"");
						}
					}
				} else {
					if (anchor != null) {
						texto = texto.replaceFirst("%\\(" + nombre + "\\)", "#" + anchor);
					} else {
						String url = target;
						if (query != null) {
							url += "?" + query;
						}
						// Enlace que no es a un contenido (hay UUID, pero vacio) -
						texto = texto.replaceFirst("%\\(" + nombre + "\\)", url);
					}
				}
			} catch (Exception e) {
				logger.warn("No ha sido posible parsear los enlaces del contenido.");
			}

			return texto;
		}

		private String processHtmlEntities(String text) {
			final StringBuffer newText = new StringBuffer();
			final Matcher matcher = patternEntity().matcher(text);

			while (matcher.find()) {
				final String group = matcher.group();
				final String unescapedGroup;
				if (!group.equals("&lt;") && !group.equals("&gt;") && !group.equals("&amp;")) {
					unescapedGroup = StringEscapeUtils.unescapeHtml(group);
				} else {
					unescapedGroup = group;
				}

				matcher.appendReplacement(newText, unescapedGroup);
			}

			matcher.appendTail(newText);

			return newText.toString();
		}

		private void addCommonFields(final DocumentBuilder builder, final OpenCmsContent content, final Locale locale) {
			builder.addLocale(locale.toString());

			builder.setField(OpenCms7Schema.ID, content.getId(), true, false);
			builder.setField(OpenCms7Schema.DATE_CREATED, content.getDateCreated(), true, false);
			builder.setField(OpenCms7Schema.DATE_LAST_MODIFIED, content.getDateLastModified(), true, false);
			builder.setField(OpenCms7Schema.DATE_RELEASED, content.getDateReleased(), true, false);
			builder.setField(OpenCms7Schema.DATE_EXPIRED, content.getDateExpired(), true, false);

			builder.setExpirationDate(content.getDateExpired());
			builder.setReleaseDate(content.getDateReleased());

			final Map<String, String> properties = content.getProperties();
			final Set<String> indexedMultilangProperties = new HashSet<String>();

			for (String name : properties.keySet()) {
				Matcher matcher = patternLang().matcher(name);
				if (matcher.matches()) {
					final int lastUnderscore = name.lastIndexOf("_");
					final Locale propertyLocale = new Locale(name.substring(lastUnderscore + 1));
					if (propertyLocale.equals(locale)) {
						final String newName = name.substring(0, lastUnderscore);

						builder.setField(OpenCms7Schema.property(newName), properties.get(name), true, false);

						indexedMultilangProperties.add(newName);
					}
				} else {
					if (!indexedMultilangProperties.contains(name)) {

						if (name.equals("NavPos")) {
							builder.setField(OpenCms7Schema.property(name), OpenCms7Schema.NAVPOSFORMAT.format(Double
								.parseDouble(properties.get(name))), true, false);
						} else {
							builder.setField(OpenCms7Schema.property(name), properties.get(name), true, false);
						}
					}
				}
			}
		}

		private void addCustomGroups(final DocumentBuilder builder, final OpenCmsContent content, final Node node,
			final Locale locale, String contentTypeName) {
			final Set<ResourceTypeDescriptor.Groupping> customGroupping = indexResourceTypes.getResourceTypeDescriptor(
				content.getType()).getCustomGroups();

			for (ResourceTypeDescriptor.Groupping groupping : customGroupping) {
				final String name = groupping.getName();

				final Set<String> values = readCustomGrouppingValue(content, node, groupping, contentTypeName);
				if (values != null && !values.isEmpty()) {
					for (String value : values) {
						builder.setField(name, Encoder.escapeXml(value), true, false);
					}
				}
			}
		}

		private Set<String> readCustomGrouppingValue(final OpenCmsContent content, final Node node,
			final ResourceTypeDescriptor.Groupping field, String contentTypeName) {
			final List<ResourceTypeDescriptor.Group> groups = field.getGroups();
			List<Set<String>> valores = new LinkedList<Set<String>>();

			for (ResourceTypeDescriptor.Group group : groups) {

				Set<String> valoresGrupo = new HashSet<String>();
				// obtenemos los valores del grupo.
				Nodes values = node.query(group.getPath() + "/link");
				for (int i = 0; i < values.size(); i++) {
					final Element link = (Element) values.get(i);
					final boolean internal = Boolean.parseBoolean(link.getAttributeValue("internal"));
					final String type = link.getAttributeValue("type");
					if (internal && type != null && (type.equals("WEAK") || type.equals("STRONG"))) {
						final Element eUuid = link.getFirstChildElement("uuid");
						if (eUuid != null) {
							final String uuid = eUuid.getValue();
							final OpenCmsContent contentCanal = databaseReader.createBuilder(uuid).build();
							// Solo queremos agrupar canales, En este caso suponemos que todo lo obtenido es canal
							if (contentCanal.getType() == 0) {
								if (group.isMapped()) {
									// Elemento Mapeado, guardamos el id normal y al final parseamos todas las
									// categorias
									valoresGrupo.add(contentCanal.getPath());
								} else {
									// Elemento no mapeado, se guarda el id y el canal
									valoresGrupo.add(contentCanal.getId() + ID_SEPARATOR
										+ contentCanal.getProperties().get(PROP_NAVTEXT));
								}

							}
						}
					}
				}
				// En el caso de estar mapeado modificamos los grupos para pasarlos de path a ids.
				if (group.isMapped()) {
					Set<UUID> idCategorias = mappingsService.getCategories(contentTypeName, content.getPath(),
						valoresGrupo);
					valoresGrupo = new HashSet<String>();
					for (UUID idCategoria : idCategorias) {
						valoresGrupo.add(idCategoria.toString());
					}
				}
				if (valoresGrupo.isEmpty()) {
					// En el caso que todos los valores de un grupo sean vacios.
					valoresGrupo.add(Schema.NULL_UUID);
				}
				valores.add(valoresGrupo);
			}

			Set<String> result = obtenerValorGrupo(valores);

			// tenemos cada uno de los valores de cada grupo en la lista de valores.
			// ahora hay que montar las combinaciones con el codigo.

			return result;
		}

		private Set<String> obtenerValorGrupo(List<Set<String>> valores) {
			Set<String> result = new HashSet<String>();

			if (valores == null || valores.size() == 0) {
				// En este caso no deberiamos entrar, se devuelve la lista vacia
			} else if (valores.size() == 1) {
				// Si solo hay un elemento esos son los valores correctos.
				result = valores.get(0);
			} else {
				Set<String> internos = obtenerValorGrupo(valores.subList(1, valores.size()));

				Set<String> valoresLineaActual = valores.get(0);
				// generamos todas las posibles combinaciones entre los resultados internos y el actual
				for (String valorConcreto : valoresLineaActual) {
					for (String rutaConcreta : internos) {
						result.add(valorConcreto + GROUPPATH_SEPARATOR + rutaConcreta);
					}
				}
			}
			return result;
		}

		private void addCustomFields(final DocumentBuilder builder, final OpenCmsContent content, final Node node,
			final Locale locale) {
			final Set<ResourceTypeDescriptor.Field> customFields = indexResourceTypes.getResourceTypeDescriptor(
				content.getType()).getCustomFields();

			for (ResourceTypeDescriptor.Field field : customFields) {
				final String name = field.getName();

				final List<String> values = readCustomFieldValue(content, node, field);
				if (values != null && !values.isEmpty()) {

					for (String value : values) {

						if (Schema.DESCRIPTION.equals(name)) {
							builder.setDescription(Encoder.escapeXml(value));
						} else if (Schema.TITLE.equals(name)) {
							builder.setTitle(Encoder.escapeXml(value));
							// } else if (Schema.DATE.equals(name)) {
							// builder.setDate((Encoder.escapeXml(value));
						} else {
							builder.setField(name, Encoder.escapeXml(value), field.isStored(), field.isTokenized());
						}

					}
				}
			}
		}

		private List<String> readCustomFieldValue(final OpenCmsContent content, final Node node,
			final ResourceTypeDescriptor.Field field) {
			final String kind = field.getValue();
			Object value = null;

			if (kind != null && !kind.trim().equals("")) {
				// el valor se obtiene de una propiedad del recurso
				if (kind.toUpperCase().startsWith("PROP")) {
					String property = kind.substring(kind.indexOf("(") + 1, kind.length() - 1);
					if (property != null && !property.equals("")) {
						value = content.getProperties().get(property);
					} else {
						logger.warn("Property null for custom field {} for content {}", kind, content.getId());
					}
				} else {
					// el valor se obtiene del xml público del recurso
					value = node.query(kind);
				}
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

		private List<String> applyFunctionToCustomField(final String function, final OpenCmsContent content,
			final Node node, final Object value, final ResourceTypeDescriptor.Field field) {

			List<String> res = null;

			// Comprobamos si lo que tenemos que cargar es un bean o una clase para ejecutar una funcion
			if (function.startsWith(BEAN_PREFIX + "(")) {
				// Cargamos el bean
				IndexCommand bean = (IndexCommand) indexCommandBeans.get(function.substring((BEAN_PREFIX + "(")
					.length(), function.length() - 1));
				if (value != null) {
					res = Arrays.asList(bean.execute((String) value));
				} else {
					res = Arrays.asList(bean.execute(node));
				}
			} else {
				int i = function.lastIndexOf(".");
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
					logger.warn("Failure at {} function for {} content", function, content.getId());
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

