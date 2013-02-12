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


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.Text;
import nu.xom.ValidityException;
import nu.xom.XPathContext;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;


/**
 * XML data.
 * @author Emilio Escobar Reyero
 * @author Alejandro Guerra
 * @author Andres Rodriguez
 */
public final class XML {
	public static Predicate<Element> localNameIn(final Set<String> names) {
		if (names == null || names.isEmpty()) {
			return Predicates.alwaysFalse();
		} else {
			return new Predicate<Element>() {
				public boolean apply(Element input) {
					return input != null && names.contains(input.getLocalName());
				}
			};
		}
	}

	public static Predicate<Element> namespace(final String namespaceURI) {
		if (namespaceURI == null) {
			return Predicates.alwaysFalse();
		} else {
			return new Predicate<Element>() {
				public boolean apply(Element input) {
					return input != null && namespaceURI.equals(input.getNamespaceURI());
				}
			};
		}
	}

	/** Original bytes. */
	private final byte[] bytes;
	/** Current document. */
	private final Document xml;

	/**
	 * Create a XML object from sql resultset
	 * @param rs the resultset
	 * @param id Id of the binary value
	 * @return XML object
	 * @throws SQLException luanched by getBinaryStream
	 */
	public static XML of(ResultSet rs, int id) throws SQLException {

		// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: get binary stream");

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
		// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: read input stream");

		if (is == null) {
			// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: input stream null");
			return null;
		}
		final byte[] buffer = new byte[2048];
		final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		int leidos;
		while ((leidos = is.read(buffer)) > 0) {
			baos.write(buffer, 0, leidos);
		}
		// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: input stream readed");
		return new XML(baos.toByteArray());
	}

	/**
	 * Create a XML object from a byte array.
	 * @param bytes Bytes.
	 * @param copy Whether copy the array.
	 * @return The XML document.
	 * @throws IOException In case of an I/O error.
	 */
	public static XML of(byte[] bytes, boolean copy) throws IOException {
		checkNotNull(bytes, "Bytes");
		final byte[] data;
		if (copy) {
			data = new byte[bytes.length];
			System.arraycopy(bytes, 0, data, 0, bytes.length);
		} else {
			data = bytes;
		}
		return new XML(data);
	}

	private XML(byte[] data) throws IOException {
		this.bytes = data;
		// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: creating ");

		try {
			final Builder parser = new Builder();
			xml = data == null ? new Document(new Element("nulo")) : parser.build(new ByteArrayInputStream(this.bytes));
		} catch (ValidityException e) {
			// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: error " + e.getMessage());

			throw new RuntimeException(e);
		} catch (ParsingException e) {

			// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: error " + e.getMessage());

			throw new RuntimeException(e);
		}

		// TODO com.isotrol.impe3.idx.oc7.LOG.of("XML: created ");
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

	private String getValue(Nodes nodes) {
		if (nodes == null || nodes.size() < 1) {
			return null;
		}
		return getValue(nodes.get(0));
	}

	/**
	 * Return the node (first one) value
	 * @param xpath the query
	 * @return node (first one) value
	 */
	public String getValue(String xpath) {
		return getValue(getNodes(xpath));
	}

	/**
	 * Return the node (first one) value
	 * @param xpath the query.
	 * @param xpc XPathContext.
	 * @return node (first one) value
	 */
	public String getValue(String xpath, XPathContext xpc) {
		return getValue(getNodes(xpath, xpc));
	}

	private Node getNode(Nodes nodes) {
		if (nodes != null && nodes.size() > 0) {
			return nodes.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Return a node (first one).
	 * @param xpath the query
	 * @return the first one node
	 */
	public Node getNode(String xpath) {
		return getNode(getNodes(xpath));
	}

	/**
	 * Return a node (first one).
	 * @param xpath the query
	 * @param xpc XPathContext.
	 * @return the first one node
	 */
	public Node getNode(String xpath, XPathContext xpc) {
		return getNode(getNodes(xpath, xpc));
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
	 * Return nodes from a xpath query
	 * @param xpath the query
	 * @param xpc Namespaces.
	 * @return Nodes object.
	 */
	public Nodes getNodes(String xpath, XPathContext xpc) {
		return xml.query(xpath, xpc);
	}

	private List<String> getValues(Nodes nodes) {
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

	/**
	 * Return a list of node values from a xpath query
	 * @param xpath the query
	 * @return list of node values
	 */
	public List<String> getValues(String xpath) {
		Nodes nodes = xml.query(xpath);
		return getValues(nodes);
	}

	/**
	 * Return a list of node values from a xpath query
	 * @param xpath the query
	 * @param xpc Namespaces.
	 * @return list of node values
	 */
	public List<String> getValues(String xpath, XPathContext xpc) {
		Nodes nodes = xml.query(xpath, xpc);
		return getValues(nodes);
	}

	public void visitElements(Predicate<Element> p, Visitor<Element> v) {
		checkNotNull(v, "The element visitor must be provided");
		visitElement(xml.getRootElement(), p, v);
	}

	public void visitTextNodes(Predicate<Element> p, final Visitor<Text> tv) {
		checkNotNull(tv, "The text node visitor must be provided");
		final Visitor<Element> ev = new Visitor<Element>() {
			public void visit(Element object) {
				visitTextNodes(object, tv);
			}
		};
		visitElements(p, ev);
	}

	public List<String> getTextNodeValues(Predicate<Element> p) {
		final List<String> list = Lists.newArrayList();
		final Visitor<Text> v = new Visitor<Text>() {
			public void visit(Text object) {
				if (object != null) {
					String value = object.getValue();
					if (value != null) {
						list.add(value);
					}
				}
			}
		};
		visitTextNodes(p, v);
		return list;
	}

	private void visitElement(Element e, Predicate<Element> p, Visitor<Element> v) {
		if (p.apply(e)) {
			v.visit(e);
		}
		final Elements children = e.getChildElements();
		for (int i = 0; i < children.size(); i++) {
			visitElement(children.get(i), p, v);
		}
	}

	private void visitTextNodes(Element e, Visitor<Text> v) {
		for (int i = 0; i < e.getChildCount(); i++) {
			final Node n = e.getChild(i);
			if (n instanceof Text) {
				v.visit((Text) n);
			}
		}
	}

	/**
	 * Interface for a XML node visitor.
	 * @author Andres Rodriguez
	 * @param <T> Node type.
	 */
	public interface Visitor<T extends Node> {
		void visit(T object);
	}
};
