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

package com.isotrol.impe3.idx;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nu.xom.Document;
import nu.xom.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.mappings.MappingDTO;
import com.isotrol.impe3.mappings.MappingTerm;
import com.isotrol.impe3.mappings.MappingsDTO;
import com.isotrol.impe3.mappings.MappingsService;

/**
 * Local mapping service implementation.
 * 
 * Only parse criterias if localVersion != remoteVersion
 * 
 * @author Emilio Escobar Reyero
 */
public class LocalMappingsServiceImpl implements LocalMappingsService,
		InitializingBean {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private final String CONTENTTYPES = "CNT";
	private final String CATEGORIES = "CAT";
	private final String SETS = "SET";

	private MappingsService mappingsService;

	private String environment = "DEFAULT";
	private String source = "local";

	private int version = -1;
	private Map<String, Set<MappingTerm>> maps;

	/**
	 * Init mappings with version = -1
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// prepareMappings(version);
	}

	private Map<String, Set<MappingTerm>> prepareMappings(int localVersion) {
		if (logger.isDebugEnabled()) {
			logger.debug("Prepare Mappings at local version {}",
					String.valueOf(localVersion));
		}

		final MappingsDTO mappings = mappingsService.getMappingsIfModified(
				environment, source, localVersion);

		if (logger.isTraceEnabled()) {
			logger.trace("Mappings recovered.");
		}

		final Map<String, Set<MappingTerm>> m = new HashMap<String, Set<MappingTerm>>();

		if (mappings == null) {
			m.put(CONTENTTYPES, Collections.<MappingTerm> emptySet());
			m.put(CATEGORIES, Collections.<MappingTerm> emptySet());
			m.put(SETS, Collections.<MappingTerm> emptySet());
			this.maps = ImmutableMap.copyOf(m);
		} else {
			if (mappings.getVersion() != localVersion) {
				m.put(CONTENTTYPES,
						getContentsMappingTerms(mappings.getContentTypes()));
				m.put(CATEGORIES,
						getCriteriaMappingTerms(mappings.getCategories()));
				m.put(SETS, getCriteriaMappingTerms(mappings.getSets()));
				this.version = mappings.getVersion();
				this.maps = ImmutableMap.copyOf(m);
			}
		}

		if (logger.isTraceEnabled()) {
			logger.trace("Returning mapping.");
		}
		return this.maps;
	}

	private Set<MappingTerm> getContentsMappingTerms(final List<MappingDTO> dtos) {
		if (dtos == null) {
			return Collections.emptySet();
		}

		final Set<MappingTerm> terms = new HashSet<MappingTerm>();

		for (MappingDTO dto : dtos) {
			if (dto.getMapping() != null) {
				final MappingTerm term = new MappingTerm(dto.getId(), "cnt:"
						+ dto.getMapping());

				terms.add(term);
			}
		}

		return terms;
	}

	private Set<MappingTerm> getCriteriaMappingTerms(final List<MappingDTO> dtos) {
		if (dtos == null) {
			return Collections.emptySet();
		}

		final Set<MappingTerm> terms = new HashSet<MappingTerm>();

		for (MappingDTO dto : dtos) {
			if (dto.getMapping() != null) {

				final MappingTerm term = new MappingTerm(dto.getId(),
						dto.getMapping());

				terms.add(term);
			}
		}

		return terms;
	}

	/**
	 * @see com.isotrol.impe3.idx.LocalMappingsService#getSets(java.lang.String,
	 *      java.lang.String, java.util.Set)
	 */
	public Set<String> getSets(String cnt, String path, Set<String> cats) {
		return getSets(cnt, path, cats, emptyXml());
	}

	public Set<String> getSets(String cnt, String path, Set<String> cats,
			Document xml) {
		final Map<String, Set<MappingTerm>> mappings = prepareMappings(version);
		final Set<MappingTerm> setTerms = mappings.get(SETS);

		final Set<String> sets = new HashSet<String>();

		for (MappingTerm term : setTerms) {
			final String set = term.containsString(cnt, path, cats, xml);
			if (set != null) {
				sets.add(set);
			}
		}

		return ImmutableSet.copyOf(sets);
	}

	/**
	 * @see com.isotrol.impe3.idx.LocalMappingsService#getCategories(java.lang.String,
	 *      java.lang.String, java.util.Set)
	 */
	public Set<UUID> getCategories(String cnt, String path, Set<String> cats) {
		return getCategories(cnt, path, cats, emptyXml());
	}

	public Set<UUID> getCategories(String cnt, String path, Set<String> cats,
			Document xml) {
		final Map<String, Set<MappingTerm>> mappings = prepareMappings(version);
		final Set<MappingTerm> categoryTerms = mappings.get(CATEGORIES);

		final Set<UUID> categories = new HashSet<UUID>();

		for (MappingTerm term : categoryTerms) {
			final UUID category = term.containsUUID(cnt, path, cats, xml);

			if (category != null) {
				categories.add(category);
			}
		}

		return ImmutableSet.copyOf(categories);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.LocalMappingsService#getContentType(java.lang.String)
	 */
	public UUID getContentType(String cnt) {
		final Map<String, Set<MappingTerm>> mappings = prepareMappings(version);
		final Set<MappingTerm> contentTypeTerms = mappings.get(CONTENTTYPES);
		final Iterator<MappingTerm> it = contentTypeTerms.iterator();

		UUID contentType = null;
		boolean stop = false;

		while (!stop && it.hasNext()) {
			final UUID uuid = it.next().containsUUID(cnt, "",
					Collections.<String> emptySet(), emptyXml());

			if (uuid != null) {
				stop = true;
				contentType = uuid;
			}
		}

		return contentType;
	}

	private Document emptyXml() {
		return new Document(new Element("xml"));
	}

	public void setMappingsService(MappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
