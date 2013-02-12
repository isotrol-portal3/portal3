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

package com.isotrol.impe3.support.nr;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import net.sf.derquinsej.io.Streams;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeKey;
import com.isotrol.impe3.nr.api.Schema;


/**
 * Node and content loading support methods.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class NodeLoader {
	/** Not instantiable. */
	private NodeLoader() {
		throw new AssertionError();
	}

	/**
	 * Creates a node from a properties input stream and an optional data input stream.
	 * @param properties Properties input stream. Required.
	 * @param data Data input stream. Optional.
	 * @return The built node.
	 * @throws IOException If an error occurs.
	 */
	public static Node loadNode(InputStream properties, InputStream data) throws IOException {
		final Node.Builder builder = Node.builder();
		if (data != null) {
			builder.setBytes(Streams.consume(data, false), false);
		}

		final Properties bag = new Properties();
		if (properties != null) {
			bag.load(properties);
		}

		for (Map.Entry<Object, Object> entry : bag.entrySet()) {
			final String key = (String) entry.getKey();
			final String value = (String) entry.getValue();

			if (key.startsWith(Schema.SCHEMA_NAME)) {
				collect(builder, key, value);
			} else {
				builder.addProperty(key, value);
			}
		}

		return builder.build();
	}

	/**
	 * Creates a content. from a properties input stream and an optional data input stream.
	 * @param properties Properties input stream. Required.
	 * @param data Data input stream. Optional.
	 * @param contentTypes Available content types.
	 * @param categories Available categories.
	 * @return The built node.
	 * @throws IOException If an error occurs.
	 */
	public static Content loadContent(InputStream properties, InputStream data, ContentTypes contentTypes,
		Categories categories) throws IOException {
		final Node node = loadNode(properties, data);
		return new ContentNode(contentTypes, categories, node);
	}

	private static void collect(final Node.Builder builder, final String key, final String value) {
		if (Schema.DATE.equals(key)) {
			builder.setDate(Schema.safeToCalendar(value));
		} else if (Schema.EXPIRATIONDATE.equals(key)) {
			builder.setExpirationdate(Schema.safeToCalendar(value));
		} else if (Schema.RELEASEDATE.equals(key)) {
			builder.setReleasedate(Schema.safeToCalendar(value));
		} else if (Schema.DESCRIPTION.equals(key)) {
			builder.setDescription(value);
		} else if (Schema.LOCALE.equals(key)) {
			builder.addLocale(value);
		} else if (Schema.MIME.equals(key)) {
			builder.setMime(value);
		} else if (Schema.NODEKEY.equals(key)) {
			builder.setNodeKey(NodeKey.of(value));
		} else if (Schema.TITLE.equals(key)) {
			builder.setTitle(value);
		} else {
			//builder.addProperty(key, value);
		}
	}
}
