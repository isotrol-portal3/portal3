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

package com.isotrol.impe3.nr.core;


import static com.isotrol.impe3.nr.api.Schema.safeToCalendar;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.sf.lucis.core.DocMapper;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;


/**
 * DocMapper implementation for Nodes.
 * @author Emilio Escobar Reyero
 * 
 */
public class NodeMapper implements DocMapper<Node> {

	private final boolean bytes;

	/**
	 * If bytes true read data.
	 * @param bytes if true read data.
	 */
	public NodeMapper(final boolean bytes) {
		this.bytes = bytes;
	}

	/**
	 * @see net.sf.lucis.core.DocMapper#map(int, float, org.apache.lucene.document.Document)
	 */
	public Node map(int id, float score, Document doc, Multimap<String, String> fragments) {
		final Node.Builder builder = Node.builder();

		final String nodeId = doc.get(Schema.ID);
		String nodeType = doc.get(Schema.TYPE);
		if (nodeType != null) {
			nodeType = nodeType.toLowerCase();
		}
		builder.setNodeKey(NodeKey.of(nodeType, nodeId)).setTitle(doc.get(Schema.TITLE))
			.setDescription(doc.get(Schema.DESCRIPTION));

		final Calendar date = safeToCalendar(doc.get(Schema.DATE));
		builder.setDate(date);

		final Calendar expirationdate = safeToCalendar(doc.get(Schema.EXPIRATIONDATE));
		builder.setExpirationdate(expirationdate);

		final Calendar releasedate = safeToCalendar(doc.get(Schema.RELEASEDATE));
		builder.setReleasedate(releasedate);

		final List<Fieldable> fields = doc.getFields();
		for (Fieldable field : fields) {
			final String name = field.name();
			if (!name.startsWith(Schema.SCHEMA_NAME) && !name.startsWith(Schema.BLOB_PREFIX)) {
				builder.addProperty(field.name(), field.stringValue());
			}
		}
		// Locales
		String[] locales = doc.getValues(Schema.LOCALE);
		for (String loc : locales) {
			if (loc != null) {
				builder.addLocale(loc);
			}
		}
		// Other locales
		String[] otherLocales = doc.getValues(Schema.OTHER_LOCALE);
		for (String loc : otherLocales) {
			if (loc != null) {
				builder.addOtherLocale(loc);
			}
		}
		// Categories
		String[] categories = doc.getValues(Schema.CATEGORY);
		for (String cat : categories) {
			if (cat != null && !Schema.NULL_UUID.equals(cat)) {
				builder.addCategory(UUID.fromString(cat));
			}
		}

		String mainCategory = doc.get(Schema.MAIN_CATEGORY);
		if (mainCategory != null && !Schema.NULL_UUID.equals(mainCategory)) {
			builder.setMainCategory(UUID.fromString(mainCategory));
		}

		String[] sets = doc.getValues(Schema.NODESET);
		for (String set : sets) {
			if (set != null) {
				builder.addSet(set);
			}
		}

		String[] relatedNodes = doc.getValues(Schema.RELATED);
		for (String rel : relatedNodes) {
			builder.addRelatedContent(NodeKey.of(rel));
		}

		if (fragments != null) {
			for (String field : fragments.keySet()) {
				for (String fragment : fragments.get(field)) {
					builder.addHighlightFragment(field, fragment);
				}
			}
		}

		if (bytes) {
			final String mime = doc.get(Schema.MIME);

			final boolean compressed = Schema.COMPRESSED_VALUE.equals(doc.get(Schema.COMPRESSED));
			final byte[] data = doc.getBinaryValue(Schema.CONTENT_STORE);

			builder.setMime(mime);
			builder.setBytes(data, compressed);

			// Other blobs
			for (Fieldable field : fields) {
				final String name = field.name();
				if (name.startsWith(Schema.BLOB_PREFIX)) {
					builder.addBlob(name, doc.getBinaryValue(name));
				}
			}
		}

		return builder.build();
	}
}
