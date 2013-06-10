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

import org.apache.lucene.document.Document;

import com.google.common.base.Preconditions;
import com.google.common.hash.HashCode;

public final class Portal3Document {
	
	private final Document document;
	private final HashCode hash ;
	private final Long virtualTime ;
	
	public static Portal3Document withHash(Document document, HashCode hash){
		return new Portal3Document(document, hash, null);
	}
	
	public static Portal3Document withHashAndVirtualTime(Document document, HashCode hash, Long virtualTime){
		return new Portal3Document(document, hash, virtualTime);
	}
	
	private Portal3Document(Document document, HashCode hash, Long virtualTime) {
		super();
		Preconditions.checkNotNull(document);
		Preconditions.checkNotNull(hash);
		this.document = document;
		this.hash = hash;
		this.virtualTime=virtualTime;
	}
	
	public Document getDocument() {
		return document;
	}
	public HashCode getHash() {
		return hash;
	}
	
	public Long getVirtualTime() {
		return virtualTime;
	}
}