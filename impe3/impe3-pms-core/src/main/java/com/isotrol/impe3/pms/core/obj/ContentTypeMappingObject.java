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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Function;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.ContentTypeMapping;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.pms.api.smap.ContentTypeMappingDTO;
import com.isotrol.impe3.pms.model.ContentTypeMappingValue;


/**
 * Content type mapping domain object.
 * @author Andres Rodriguez
 */
public final class ContentTypeMappingObject extends AbstractMappingObject<ContentTypeMappingDTO, ContentTypeMapping> {
	static final Function<ContentTypeMappingValue, ContentTypeMappingObject> OF = new Function<ContentTypeMappingValue, ContentTypeMappingObject>() {
		public ContentTypeMappingObject apply(ContentTypeMappingValue from) {
			return new ContentTypeMappingObject(from);
		}
	};

	/**
	 * Constructor.
	 * @param mapping Mapping.
	 */
	ContentTypeMappingObject(ContentTypeMappingValue mapping) {
		super(checkNotNull(mapping, "The content type mapping must be provided"));
	}

	ContentTypeMappingDTO toDTO(Context1 context) {
		ContentTypeObject ct = context.getContentTypes().get(getId());
		if (ct == null) {
			return null;
		}
		ContentTypeMappingDTO dto = new ContentTypeMappingDTO();
		dto.setContentType(ct.toSelDTO());
		dto.setMapping(getMapping());
		return dto;
	}

	ContentTypeMapping toMapping(BaseModel model) {
		ContentType ct = model.getContentTypes().get(getId());
		if (ct == null) {
			return null;
		}
		return new ContentTypeMapping(ct, getMapping());
	}


}
