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


import static com.google.common.base.Predicates.equalTo;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.CategoryMapping;
import com.isotrol.impe3.api.ContentTypeMapping;
import com.isotrol.impe3.api.SetMapping;
import com.isotrol.impe3.api.SourceMapping;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntryPB;
import com.isotrol.impe3.pbuf.mappings.MappingProtos.MappingPB;
import com.isotrol.impe3.pms.api.smap.CategoryMappingDTO;
import com.isotrol.impe3.pms.api.smap.ContentTypeMappingDTO;
import com.isotrol.impe3.pms.api.smap.SetMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.model.MappingValue;
import com.isotrol.impe3.pms.model.SetMappingValue;
import com.isotrol.impe3.pms.model.SourceMappingEntity;


/**
 * Value that represents a source mapping.
 * @author Andres Rodriguez
 */
public final class SourceMappingObject extends AbstractIdentifiable {
	private static final Predicate<MappingValue> NOT_DELETED = new Predicate<MappingValue>() {
		public boolean apply(MappingValue input) {
			return !input.isDeleted();
		}
	};

	private static final Function<SetMappingValue, SetMapping> SM2API = new Function<SetMappingValue, SetMapping>() {
		public SetMapping apply(SetMappingValue from) {
			if (from == null) {
				return null;
			}
			return new SetMapping(from.getSet(), from.getMapping());
		}
	};

	private static final Function<SetMapping, SetMappingDTO> SM2DTO = new Function<SetMapping, SetMappingDTO>() {
		public SetMappingDTO apply(SetMapping from) {
			if (from == null) {
				return null;
			}
			final SetMappingDTO dto = new SetMappingDTO();
			dto.setSet(from.getSet());
			dto.setMapping(from.getMapping());
			return dto;
		}
	};

	static final Function<SourceMappingObject, MappingPB> MAP2PB = new Function<SourceMappingObject, MappingPB>() {
		public MappingPB apply(SourceMappingObject from) {
			return from.toPB();
		}
	};

	/** Apply and filter not null. */
	private static <F, T> Iterable<T> af(Iterable<F> from, Function<F, T> function) {
		return filter(transform(from, function), notNull());
	}

	static final ToDto map2dto(final Context1 context) {
		final Function<ContentTypeMappingObject, ContentTypeMappingDTO> contentType = ContentTypeMappingObject
			.<ContentTypeMappingDTO, ContentTypeMapping, ContentTypeMappingObject> map2dto(context);
		final Function<CategoryMappingObject, CategoryMappingDTO> category = ContentTypeMappingObject
			.<CategoryMappingDTO, CategoryMapping, CategoryMappingObject> map2dto(context);
		return new ToDto() {
			public SourceMappingDTO apply(SourceMappingObject from) {
				SourceMappingDTO dto = new SourceMappingDTO();
				dto.setId(from.getStringId());
				dto.setName(from.name);
				dto.setDescription(from.description);
				dto.setSets(newArrayList(af(from.sets, SM2DTO)));
				dto.setContentTypes(newArrayList(af(from.contentTypes, contentType)));
				dto.setCategories(newArrayList(af(from.categories, category)));
				return dto;
			}
		};
	}

	static final ToMapping map2mapping(final BaseModel model) {
		final Function<ContentTypeMappingObject, ContentTypeMapping> contentType = ContentTypeMappingObject
			.<ContentTypeMappingDTO, ContentTypeMapping, ContentTypeMappingObject> map2mapping(model);
		final Function<CategoryMappingObject, CategoryMapping> category = ContentTypeMappingObject
			.<CategoryMappingDTO, CategoryMapping, CategoryMappingObject> map2mapping(model);
		return new ToMapping() {
			public SourceMapping apply(SourceMappingObject from) {
				SourceMapping.Builder b = SourceMapping.builder();
				b.setId(from.getId());
				b.setName(from.name);
				b.setDescription(from.description);
				b.addSets(from.sets);
				b.addContentTypes(af(from.contentTypes, contentType));
				b.addCategories(af(from.categories, category));
				return b.get();
			}
		};
	}

	/** Mapping version. */
	private final int version;
	/** Mapping name. */
	private final String name;
	/** Mapping description. */
	private final String description;
	/** Set mappings. */
	private final ImmutableList<SetMapping> sets;
	/** Content type mappings. */
	private final ImmutableList<ContentTypeMappingObject> contentTypes;
	/** Category mappings. */
	private final ImmutableList<CategoryMappingObject> categories;

	/**
	 * Constructor.
	 * @param mapping Source mapping entity.
	 */
	SourceMappingObject(SourceMappingEntity mapping) {
		super(mapping.getId());
		this.version = mapping.getVersion();
		this.name = mapping.getName();
		this.description = mapping.getDescription();
		this.sets = copyOf(af(mapping.getSets(), SM2API));
		this.contentTypes = copyOf(transform(filter(mapping.getContentTypes(), NOT_DELETED),
			ContentTypeMappingObject.OF));
		this.categories = copyOf(transform(filter(mapping.getCategories(), NOT_DELETED), CategoryMappingObject.OF));
	}

	/**
	 * Returns the mapping version.
	 * @return The mapping version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Returns the mapping name.
	 * @return The mapping name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the mapping description.
	 * @return The mapping description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the content type mappings.
	 * @return The content type mappings.
	 */
	public List<ContentTypeMappingObject> getContentTypes() {
		return contentTypes;
	}

	/**
	 * Returns the category mappings.
	 * @return The category mappings.
	 */
	public List<CategoryMappingObject> getCategories() {
		return categories;
	}

	public SourceMappingSelDTO toSel() {
		SourceMappingSelDTO dto = new SourceMappingSelDTO();
		dto.setId(getStringId());
		dto.setName(name);
		dto.setDescription(description);
		return dto;
	}

	public SourceMappingDTO toDTO(Context1 ctx) {
		return map2dto(ctx).apply(this);
	}

	public SourceMapping toMapping(BaseModel model) {
		return map2mapping(model).apply(this);
	}

	private boolean isUsed(Iterable<? extends MappingObject> mappings, UUID id) {
		return any(transform(mappings, ID), equalTo(id));
	}

	/**
	 * Returns whether a content type is used by this configuration.
	 * @param id Content type id.
	 * @return True if the content type is used by this configuration.
	 */
	public boolean isContentTypeUsed(UUID id) {
		return isUsed(contentTypes, id);
	}

	/**
	 * Returns whether a category is used by this configuration.
	 * @param id Category id.
	 * @return True if the category is used by this configuration.
	 */
	public boolean isCategoryUsed(UUID id) {
		return isUsed(categories, id);
	}

	static interface ToDto extends Function<SourceMappingObject, SourceMappingDTO> {
	}

	static interface ToMapping extends Function<SourceMappingObject, SourceMapping> {
	}

	public final MappingPB toPB() {
		final MappingPB.Builder b = MappingPB.newBuilder();
		b.setId(getStringId());
		b.setName(getName());
		if (description != null) {
			b.setDescription(getDescription());
		}

		if (sets != null) {
			for (SetMapping s : sets) {
				b.addSets(StringEntryPB.newBuilder().setKey(s.getSet()).setValue(s.getMapping()).build());
			}
		}

		if (contentTypes != null) {
			for (ContentTypeMappingObject c : contentTypes) {
				b.addContentTypes(c.toPB());
			}
		}

		if (categories != null) {
			for (CategoryMappingObject c : categories) {
				b.addCategories(c.toPB());
			}
		}

		return b.build();
	}

	public final MappingPB toContentTypesPB() {
		final MappingPB.Builder b = MappingPB.newBuilder();
		b.setId(getStringId());
		b.setName(getName());
		if (description != null) {
			b.setDescription(getDescription());
		}

		if (contentTypes != null) {
			for (ContentTypeMappingObject c : contentTypes) {
				b.addContentTypes(c.toPB());
			}
		}
		return b.build();
	}

	public final MappingPB toCategoriesPB() {
		final MappingPB.Builder b = MappingPB.newBuilder();
		b.setId(getStringId());
		b.setName(getName());
		if (description != null) {
			b.setDescription(getDescription());
		}

		if (categories != null) {
			for (CategoryMappingObject c : categories) {
				b.addCategories(c.toPB());
			}
		}

		return b.build();
	}

	public final MappingPB toSetsPB() {
		final MappingPB.Builder b = MappingPB.newBuilder();
		b.setId(getStringId());
		b.setName(getName());
		if (description != null) {
			b.setDescription(getDescription());
		}

		if (sets != null) {
			for (SetMapping s : sets) {
				b.addSets(StringEntryPB.newBuilder().setKey(s.getSet()).setValue(s.getMapping()).build());
			}
		}
		return b.build();
	}

}
