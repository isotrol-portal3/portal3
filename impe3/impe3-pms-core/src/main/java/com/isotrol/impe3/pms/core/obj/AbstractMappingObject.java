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
import com.google.common.base.Objects;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntryPB;
import com.isotrol.impe3.pms.model.MappingValue;


/**
 * Abstract mapping domain object.
 * @author Andres Rodriguez
 */
public abstract class AbstractMappingObject<D, M> extends AbstractIdentifiable implements MappingObject {
	static final <D, M, T extends AbstractMappingObject<D, M>> Function<T, D> map2dto(final Context1 context) {
		return new Function<T, D>() {
			public D apply(T from) {
				return from.toDTO(context);
			}
		};
	}

	static final <D, M, T extends AbstractMappingObject<D, M>> Function<T, M> map2mapping(final BaseModel model) {
		return new Function<T, M>() {
			public M apply(T from) {
				return from.toMapping(model);
			}
		};
	}

	/** Mapping. */
	private final String mapping;

	/**
	 * Constructor.
	 * @param mapping Mapping.
	 */
	AbstractMappingObject(MappingValue mapping) {
		super(mapping.getId());
		this.mapping = mapping.getMapping();
	}

	abstract D toDTO(Context1 context);

	abstract M toMapping(BaseModel model);

	/**
	 * Returns the mapping.
	 * @return The mapping.
	 */
	public String getMapping() {
		return mapping;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return Objects.hashCode(getId(), mapping);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj != null && getClass().equals(obj.getClass())) {
			final AbstractMappingObject<?, ?> m = (AbstractMappingObject<?, ?>) obj;
			return Objects.equal(getId(), m.getId()) && Objects.equal(mapping, m.mapping);
		}
		return false;
	}
	
	public StringEntryPB toPB() {
		final StringEntryPB.Builder b = StringEntryPB.newBuilder();
		
		b.setKey(getStringId());
		b.setValue(getMapping());
		
		return b.build();
	}
}
