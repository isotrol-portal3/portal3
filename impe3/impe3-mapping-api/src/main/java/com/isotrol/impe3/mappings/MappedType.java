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

package com.isotrol.impe3.mappings;

/**
 * Mapped type enumeration.
 * @author Emilio Escobar Reyero
 */
enum MappedType {

	CAT("cat"), CNT("cnt"), PTH("pth"), XML("xml");
	
	private final String type;
	
	private MappedType(final String type) {
		this.type = type;
	}
	
	/**
	 * gets pretty type
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * creates a safe MappedType
	 * @param typ type
	 * @return safe mapped type
	 */
	public static MappedType getMT(String typ) {
		MappedType mt = MappedType.PTH;
		
		if (typ.equals(MappedType.CNT.getType())) {
			mt = MappedType.CNT;
		} else if (typ.equals(MappedType.CAT.getType())) {
			mt = MappedType.CAT;
		} else if (typ.equals(MappedType.XML.getType())) {
			mt = MappedType.XML;
		} else {
			mt = MappedType.PTH;
		}
		
		return mt;
	}
}
