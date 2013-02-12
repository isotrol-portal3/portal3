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


import com.google.common.base.Function;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.pbuf.type.ContentTypeProtos.ContentTypePB;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.model.ContentTypeDfn;


/**
 * Content type domain object.
 * @author Andres Rodriguez
 */
public final class ContentTypeObject extends AbstractIAEObject {
	public static Function<ContentTypeObject, ContentTypeSelDTO> MAP2SEL = new Function<ContentTypeObject, ContentTypeSelDTO>() {
		public ContentTypeSelDTO apply(ContentTypeObject from) {
			return from.toSelDTO();
		}
	};

	public static Function<ContentTypeObject, ContentTypeDTO> MAP2DTO = new Function<ContentTypeObject, ContentTypeDTO>() {
		public ContentTypeDTO apply(ContentTypeObject from) {
			return from.toDTO();
		}
	};

	static final Function<ContentTypeObject, ContentType> MAP2API = new Function<ContentTypeObject, ContentType>() {
		public ContentType apply(ContentTypeObject from) {
			return set(ContentType.builder(), from).setNavigable(from.isNavigable()).get();
		}
	};

	static final Function<ContentTypeObject, ContentTypePB> MAP2PB = new Function<ContentTypeObject, ContentTypePB>() {
		public ContentTypePB apply(ContentTypeObject from) {
			return from.toPB();
		}
	};

	/** Content type description. */
	private final String description;
	/** Whether the content type is navigable. */
	private final boolean navigable;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 */
	ContentTypeObject(ContentTypeDfn dfn) {
		super(dfn);
		this.description = dfn.getDescription();
		this.navigable = dfn.isNavigable();
	}

	/**
	 * Returns the content type description.
	 * @return The content type description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns whether the content type is navigable.
	 * @return True if the content type is navigable.
	 */
	public boolean isNavigable() {
		return navigable;
	}

	/**
	 * Transforms the object to a selection DTO.
	 * @return The selection DTO.
	 */
	public ContentTypeSelDTO toSelDTO() {
		final ContentTypeSelDTO dto = new ContentTypeSelDTO();
		dto.setId(getStringId());
		dto.setName(getName().get().getDisplayName());
		dto.setState(getState());
		dto.setDescription(description);
		dto.setRoutable(isRoutable());
		dto.setNavigable(isNavigable());
		return dto;
	}

	/**
	 * Transforms the object to a management DTO.
	 * @return The management DTO.
	 */
	public ContentTypeDTO toDTO() {
		final ContentTypeDTO dto = new ContentTypeDTO();
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setDescription(description);
		dto.setRoutable(isRoutable());
		dto.setNavigable(isNavigable());
		fillName(dto);
		return dto;
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @return The PB message.
	 */
	final ContentTypePB toPB() {
		ContentTypePB.Builder b = ContentTypePB.newBuilder();
		b.setId(getStringId());
		b.setDefaultName(MessageMappers.name().apply(getDefaultName()));
		b.addAllLocalizedNames(MessageMappers.localizedName(getLocalizedNames()));
		if (getDescription() != null) {
			b.setDescription(getDescription());
		}
		b.setRoutable(isRoutable());
		b.setNavigable(isNavigable());
		return b.build();
	}

}
