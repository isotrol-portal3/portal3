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
package com.isotrol.impe3.web20.server;


import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import com.isotrol.impe3.web20.api.SourceDTO;


/**
 * Value that represents a participation source key.
 * @author Andres Rodriguez
 */
public abstract class SourceKey {
	/** Default origin. */
	public static final String DEFAULT_ORIGIN = "PUBLIC_ORIGIN";
	/** Default source. */
	public static final SourceKey DEFAULT = new Origin(DEFAULT_ORIGIN);

	/**
	 * Creates a new member source key.
	 * @param memberId Member Id.
	 * @return The requested key.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public static SourceKey member(UUID memberId) {
		return new Member(checkNotNull(memberId));
	}

	public static SourceKey origin(String origin) {
		if (origin == null) {
			return DEFAULT;
		}
		return new Origin(origin);
	}

	/**
	 * Creates a new member source key.
	 * @param source Source DTO.
	 * @return The requested key.
	 * @throws IllegalArgumentException if an invalid member id is provided.
	 */
	public static SourceKey source(SourceDTO source) {
		if (source == null) {
			return DEFAULT;
		}
		String memberId = source.getMemberId();
		if (memberId != null) {
			return new Member(UUID.fromString(memberId));
		}
		return origin(source.getOrigin());
	}

	/**
	 * Constructor.
	 */
	private SourceKey() {
	}

	/**
	 * Returns the member.
	 * @return The member.
	 */
	public UUID getMember() {
		return null;
	}

	/**
	 * Returns the origin.
	 * @return The origin.
	 */
	public String getOrigin() {
		return null;
	}

	private static final class Member extends SourceKey {
		/** Member. */
		private final UUID member;

		/**
		 * Constructor.
		 * @param member Registered member.
		 */
		Member(UUID member) {
			this.member = member;
		}

		/**
		 * Returns the member.
		 * @return The member.
		 */
		public UUID getMember() {
			return member;
		}

		@Override
		public int hashCode() {
			return member.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Member) {
				final Member k = (Member) obj;
				return equal(member, k.member);
			}
			return false;
		}
	}

	private static final class Origin extends SourceKey {
		/** Origin name. */
		private final String origin;

		/**
		 * Constructor.
		 * @param origin Request origin.
		 */
		Origin(String origin) {
			this.origin = origin;
		}

		/**
		 * Returns the origin.
		 * @return The origin.
		 */
		public String getOrigin() {
			return origin;
		}

		@Override
		public int hashCode() {
			return origin.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Origin) {
				final Origin k = (Origin) obj;
				return equal(origin, k.origin);
			}
			return false;
		}
	}

}
