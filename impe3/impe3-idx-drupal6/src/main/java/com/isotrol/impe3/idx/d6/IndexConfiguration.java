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


import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.google.common.base.Preconditions;


/**
 * Guarda la configuración de los índices: idiomas, tipos de recursos, campos que indexar por tipo de recurso,...
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class IndexConfiguration implements InitializingBean {

	private static final String NAME_ATTRIBUTE = "name";

	/**
	 * Idiomas disponibles
	 */
	private Set<Locale> locales = new HashSet<Locale>();

	/**
	 * Idioma por defecto
	 */
	private Locale defaultLanguage;

	/**
	 * Decide si se indexa la versión del idioma por defecto de un contenido si no existe la versión en el idioma del
	 * índice.
	 */
	private boolean indexDefaultLanguage;

	private Map<String, ResourceTypeDescriptor> resourceTypesNames = new HashMap<String, ResourceTypeDescriptor>();

	private Resource configurationFile;

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Preconditions.checkNotNull(configurationFile);

		Builder builder = new Builder();
		nu.xom.Document doc = builder.build(configurationFile.getInputStream());
		loadXMLConfiguration(doc);
	}

	private void loadXMLConfiguration(nu.xom.Document doc) {

		// each element represents a content
		final Nodes contentElements = doc.query("/configuration/resourceTypes/resourceType");
		for (int i = 0; i < contentElements.size(); i++) {
			final Element contentElement = (Element) contentElements.get(i);
			// resource type name and id
			final String typeName = contentElement.getAttributeValue(NAME_ATTRIBUTE);

			if (typeName != null) {
				final Set<ResourceTypeDescriptor.Field> customFields = new HashSet<ResourceTypeDescriptor.Field>();
				// indexing fields
				final Elements fieldElements = contentElement.getChildElements("field");
				for (int j = 0; j < fieldElements.size(); j++) {
					final Element fieldElement = fieldElements.get(j);
					// String attributes
					final String name = fieldElement.getAttributeValue(NAME_ATTRIBUTE);
					final String value = fieldElement.getAttributeValue("value");
					final String function = fieldElement.getAttributeValue("function");

					// boolean attributes
					final boolean stored = Boolean.parseBoolean(fieldElement.getAttributeValue("stored"));
					final boolean tokenized = Boolean.parseBoolean(fieldElement.getAttributeValue("tokenized"));

					customFields.add(new ResourceTypeDescriptor.Field(name, stored, tokenized, value, function));
				}

				final ResourceTypeDescriptor resourceType = new ResourceTypeDescriptor(typeName, customFields);
				resourceTypesNames.put(resourceType.getName(), resourceType);

			}
		}

		// Languages configuration
		final Element languages = doc.getRootElement().getFirstChildElement("languages");
		final String indexDefaultLanguageString = languages.getAttributeValue("indexDefaultLanguage");
		if (indexDefaultLanguageString != null) {
			indexDefaultLanguage = Boolean.parseBoolean(indexDefaultLanguageString);
		} else {
			indexDefaultLanguage = true;
		}

		// Available languages
		final Elements languageElements = languages.getChildElements("language");
		for (int i = 0; i < languageElements.size(); i++) {
			final Element languageElement = languageElements.get(i);
			final Locale language = new Locale(languageElement.getValue());
			this.locales.add(language);

			// Is default lang?
			final String defaultLang = languageElement.getAttributeValue("default");
			if (defaultLang != null) {
				defaultLanguage = language;
			}
		}
	}

	public Set<Locale> getLanguages() {
		return locales;
	}

	public Locale getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setConfigurationFile(Resource configurationFile) {
		this.configurationFile = configurationFile;
	}

	public boolean isIndexDefaultLanguage() {
		return indexDefaultLanguage;
	}

	/**
	 * Returns the ResourceTypeDescriptor for a name
	 * @param name The name of a type
	 * @return the ResourceTypeDescriptor for this name
	 */
	public ResourceTypeDescriptor getResourceTypeDescriptor(String name) {
		return resourceTypesNames.get(name);
	}
}
