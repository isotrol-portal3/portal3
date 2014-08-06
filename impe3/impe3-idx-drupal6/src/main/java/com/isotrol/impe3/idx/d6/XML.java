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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;


/**
 * Xml data.
 */
public final class XML {

	private static final int NUM_BYTES = 2048;

	private final byte[] bytes;

	private final Document xml;

	/**
	 * Constructor. Creates a XML object from nu.xom Document
	 * @param doc XML Document
	 */
	public XML(Document doc) {
		super();
		this.xml = doc;
		if (doc == null) {
			this.bytes = null;
		} else {
			this.bytes = doc.toString().getBytes();
		}
	}

	/**
	 * Create a XML object from sql resultset
	 * @param rs the resultset
	 * @param id Id of the binary value
	 * @return XML object
	 * @throws SQLException luanched by getBinaryStream
	 */
	public static XML of(ResultSet rs, int id) throws SQLException {

		final InputStream is = rs.getBinaryStream(id);
		try {
			return of(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a XML object from input stream
	 * @param is binary data input strean
	 * @return object
	 * @throws IOException throwed by reading
	 */
	public static XML of(InputStream is) throws IOException {

		if (is == null) {
			return null;
		}
		final byte[] buffer = new byte[NUM_BYTES];
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int leidos;
		while ((leidos = is.read(buffer)) > 0) {
			baos.write(buffer, 0, leidos);
		}

		return new XML(baos.toByteArray());
	}

	private XML(byte[] data) throws IOException {
		this.bytes = data;

		try {
			final Builder parser = new Builder();
			if (data == null) {
				xml = new Document(new Element("nulo"));
			} else {
				xml = parser.build(new ByteArrayInputStream(this.bytes));
			}
		} catch (ValidityException e) {
			throw new RuntimeException(e);
		} catch (ParsingException e) {
			throw new RuntimeException(e);
		}
	}

	public final byte[] getBytes() {
		return bytes;
	}

	public Document getXML() {
		return xml;
	}

	private String getValue(Node nodo) {
		if (nodo instanceof Attribute || nodo instanceof Element) {
			return nodo.getValue();
		}
		return null;
	}

	/**
	 * Return the node (first one) value
	 * @param xpath the query
	 * @return node (first one) value
	 */
	public String getValue(String xpath) {
		Nodes nodes = xml.query(xpath);
		if (nodes == null || nodes.size() < 1) {
			return null;
		}
		return getValue(nodes.get(0));
	}

	/**
	 * Return a node (first one).
	 * @param xpath the query
	 * @return the first one node
	 */
	public Node getNode(String xpath) {
		Nodes nodes = xml.query(xpath);

		if (nodes != null && nodes.size() > 0) {
			return (Element) nodes.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Return nodes from a xpath query
	 * @param xpath the query
	 * @return Nodes object-
	 */
	public Nodes getNodes(String xpath) {
		return xml.query(xpath);
	}

	/**
	 * Return a list of node values from a xpath query
	 * @param xpath the query
	 * @return list of node values
	 */
	public List<String> getValues(String xpath) {
		Nodes nodes = xml.query(xpath);
		if (nodes == null || nodes.size() < 1) {
			return Collections.emptyList();
		}
		final int n = nodes.size();
		final List<String> list = new ArrayList<String>(n);
		for (int i = 0; i < n; i++) {
			final String value = getValue(nodes.get(i));
			if (value != null) {
				list.add(value);
			}
		}
		return list;
	}
};
