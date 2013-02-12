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

package com.isotrol.impe3.lucene;


import java.io.Reader;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.lucis.core.Factory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.base.Strings;


/**
 * Portal standard spanish analyzer.
 * @author Andres Rodriguez
 */
public final class PortalSpanishAnalyzer extends Analyzer implements InitializingBean {
	/** Base analyzer. */
	private final SpanishAnalyzer base = new SpanishAnalyzer(Factory.get().version());
	/** Post analysis synonyms. */
	private Map<String, String> postSynonyms;
	/** Post analysis synonym map. */
	private SynonymMap postSynonymMap = null;

	/**
	 * Constructor.
	 */
	public PortalSpanishAnalyzer() {
	}

	/** Sets the post-analysis synonym map. */
	public void setPostSynonyms(Map<String, String> postSynonyms) {
		this.postSynonyms = postSynonyms;
	}

	public void afterPropertiesSet() throws Exception {
		if (postSynonyms == null || postSynonyms.isEmpty()) {
			return;
		}
		final SynonymMap.Builder b = new SynonymMap.Builder(true);
		boolean used = false;
		for (Entry<String, String> entry : postSynonyms.entrySet()) {
			final String input = entry.getKey();
			final String output = entry.getValue();
			if (!Strings.isNullOrEmpty(input) && !Strings.isNullOrEmpty(output) && !input.equals(output)) {
				b.add(new CharsRef(input), new CharsRef(output), false);
				used = true;
			}
		}
		if (used) {
			postSynonymMap = b.build();
		}
	}

	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream src = base.tokenStream(fieldName, reader);
		if (postSynonymMap != null) {
			return new SynonymFilter(src, postSynonymMap, true);
		}
		return src;
	}
}