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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


/**
 * Abstract base class for identifiable objects. It provides: <ul> <li>Immutable storage of the uuid.</li> <li>Equals
 * and hash code implementation based on class and uuid.</li> </ul>
 * @author Andres Rodriguez
 */
public abstract class AbstractIdentifiable implements Identifiable {
	@Deprecated
	public static final Function<Identifiable, UUID> GET_ID = new Function<Identifiable, UUID>() {
		public UUID apply(Identifiable from) {
			return from.getId();
		}
	};
	
	/** Null ID error message. */
	private static final String UUID_ERROR = "An non-null id must be provided.";

	/** ID of the object. */
	private final UUID id;

	/**
	 * Default constructor.
	 * @param id ID of the object.
	 */
	public AbstractIdentifiable(final UUID id) {
		this.id = checkNotNull(id, UUID_ERROR);
	}

	/**
	 * Builder-based constructor.
	 * @param builder Builder.
	 */
	protected AbstractIdentifiable(final Builder<?, ?> builder) {
		this(builder.id);
	}

	public final UUID getId() {
		return id;
	}

	public final String getStringId() {
		return Identifiables.toStringId(id);
	}
	
	/**
	 * Computes the hash code of the object based on the class and the id.
	 * @return The hash code of the object.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(getClass(), id);
	}

	/**
	 * Checks if this object is equal to other based on the class and the id of the objects.
	 * @param obj Object to compare to.
	 * @return True if both objects are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass().equals(obj.getClass())) {
			final Identifiable i = (Identifiable) obj;
			return id.equals(i.getId());
		}
		return false;
	}

	/**
	 * Builder for AbstractIdentifiable.
	 * @author Andres Rodriguez
	 */
	protected abstract static class Builder<B extends Builder<B, T>, T extends AbstractIdentifiable> extends
		AbstractBuilder<B, T> {
		private UUID id = null;

		protected Builder() {
		}

		public B setId(UUID value) {
			this.id = checkNotNull(value, UUID_ERROR);
			return thisValue();
		}

		@Override
		protected void checkState() {
			Preconditions.checkState(id != null, UUID_ERROR);
		}
	}

}
