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

package com.isotrol.impe3.nr.api;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Objects;


/**
 * A repository node key.
 * @author Emilio Escobar
 */
public final class NodeKey implements Serializable {

	private static final long serialVersionUID = 1515152849924375124L;

	private final UUID nodeType;
	private final String nodeId;

	private NodeKey(UUID nodeType, String nodeId) {
		this.nodeType = checkNotNull(nodeType, "The node type must be provided");
		this.nodeId = checkNotNull(nodeId, "The node id must be provided");
	}

	/**
	 * Creates a new node key.
	 * @param nodeType Node Type.
	 * @param nodeId Node Id.
	 * @return The requested key.
	 */
	public static NodeKey of(UUID nodeType, String nodeId) {
		return new NodeKey(nodeType, nodeId);
	}

	/**
	 * Creates a new node key.
	 * @param nodeType Node Type.
	 * @param nodeId Node Id.
	 * @return The requested key.
	 */
	public static NodeKey of(String nodeType, String nodeId) {
		return new NodeKey(UUID.fromString(nodeType), nodeId);
	}

	/**
	 * Creates a new node key from a nodeType:nodeId string
	 * @param key the nodeType:nodeId string
	 * @return The requested key.
	 */
	public static NodeKey of(final String key) {
		checkNotNull(key);
		final int pos = key.indexOf(':');
		checkArgument(pos >= 0, "Invalid key");
		final String type = key.substring(0, pos);
		final String id = key.substring(pos + 1);
		return of(type, id);
	}

	/**
	 * Returns nodeId
	 * @return node id
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * Returns node type (uuid)
	 * @return node type
	 */
	public UUID getNodeType() {
		return nodeType;
	}

	/**
	 * Return true if obj is an instance of NodeKey and each params are equals.
	 * @param obj the object to compare
	 * @return true if equals
	 */
	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof NodeKey)) {
			return false;
		}

		NodeKey nk = (NodeKey) obj;

		return this.getNodeId().equals(nk.getNodeId()) && this.getNodeType().equals(nk.getNodeType());
	}

	/**
	 * Do the hash code of the string representation
	 * @return the hash
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(nodeType, nodeId);
	}

	/**
	 * Return a string with uuid + : + id
	 * @return the string representation
	 */
	@Override
	public String toString() {
		return nodeType.toString() + ":" + nodeId;
	}

	// ////////////////////////////////////////////////////////////////////////////////

	// =================================================================
	// Serialization proxy

	private static class SerializationProxy implements Serializable {
		/** Serial UID. */
		private static final long serialVersionUID = -3894967829049006742L;
		/** Node key. */
		private final String key;

		SerializationProxy(NodeKey key) {
			this.key = key.toString();
		}

		private Object readResolve() {
			return NodeKey.of(key);
		}
	}

	private Object writeReplace() {
		return new SerializationProxy(this);
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Proxy required");
	}

}
