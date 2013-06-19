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

package com.isotrol.impe3.nr.audit.api;

public enum AuditEngineMode {
	ONLINE, OFFLINE, BOTH;

	public static AuditEngineMode fromString(String value) {
		AuditEngineMode salida;

		if (value.equals("N")) {
			salida = ONLINE;
		} else if (value.equals("F")) {
			salida = OFFLINE;
		} else if (value.equals("B")) {
			salida = BOTH;
		} else {
			salida = null;
		}

		return salida;
	}

	public String toString() {
		if (AuditEngineMode.ONLINE.equals(this)) {
			return "N";
		} else if (AuditEngineMode.OFFLINE.equals(this)) {
			return "F";
		} else if (AuditEngineMode.BOTH.equals(this)) {
			return "B";
		}
		return null;
	}
	
	public static final AuditEngineMode fromLongString(String value){
		AuditEngineMode salida;

		if (value.equals("ONLINE")) {
			salida = ONLINE;
		} else if (value.equals("OFFLINE")) {
			salida = OFFLINE;
		} else if (value.equals("BOTH")) {
			salida = BOTH;
		} else {
			salida = null;
		}

		return salida;
	}
}
