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
package com.isotrol.impe3.web20.mappers;

import net.sf.lucis.core.DocMapper;

import org.apache.lucene.document.Document;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.web20.api.MemberSelDTO;
import com.isotrol.impe3.web20.server.MemberSchema;

/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class MemberSelDTODocMapper implements DocMapper<MemberSelDTO> {

	public MemberSelDTO map(int id, float score, Document doc, Multimap<String, String> fragments) {
		final MemberSelDTO dto = new MemberSelDTO();

		dto.setId(doc.get(MemberSchema.ID));
		dto.setCode(doc.get(MemberSchema.CODE));
		dto.setName(doc.get(MemberSchema.NAME));
		dto.setDisplayName(doc.get(MemberSchema.DISPLAYNAME));
		dto.setBlocked(doc.get(MemberSchema.DISPLAYNAME).equalsIgnoreCase("TRUE"));
		
		return dto;
	}
	
	
}
