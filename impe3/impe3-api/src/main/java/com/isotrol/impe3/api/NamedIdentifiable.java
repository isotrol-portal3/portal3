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


import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.LocalizedBuilder;
import net.sf.derquinsej.i18n.Unlocalized;

import com.google.common.base.Preconditions;


/**
 * Abstract class for identifiables with a localized name.
 * @author Andres Rodriguez
 */
public abstract class NamedIdentifiable extends AbstractIdentifiable {
	/** Name. */
	private final Localized<Name> name;

	/**
	 * Constructor.
	 * @param id Id.
	 * @param name Localized name.
	 */
	public NamedIdentifiable(UUID id, Localized<Name> name) {
		super(id);
		Preconditions.checkNotNull(name, "A localized name must be provided");
		this.name = name;
	}

	/**
	 * Constructor.
	 * @param id Id.
	 * @param name Name.
	 */
	public NamedIdentifiable(UUID id, Name name) {
		super(id);
		Preconditions.checkNotNull(name, "A name must be provided");
		this.name = Unlocalized.of(name);
	}

	/**
	 * Constructor.
	 * @param builder Builder.
	 */
	protected NamedIdentifiable(Builder<?, ?> builder) {
		super(builder);
		this.name = Preconditions.checkNotNull(builder.name);
	}

	public final Localized<Name> getName() {
		return name;
	}

	public final Name getDefaultName() {
		return name.get();
	}

	/**
	 * Builder for NamedIdentifiable.
	 * @author Andres Rodriguez
	 */
	protected abstract static class Builder<B extends Builder<B, T>, T extends NamedIdentifiable> extends
		AbstractIdentifiable.Builder<B, T> {
		/** Localized name provision mechanism. */
		private enum Mechanism {
			NONE, DIRECT, BUILDER
		};
		/** Localized name. */
		private Localized<Name> name = null;
		/** Localized name. */
		private LocalizedBuilder<Name> nameBuilder = null;
		private Mechanism mechanism = Mechanism.NONE;

		/** Constructor. */
		protected Builder() {
		}

		/**
		 * Sets the localized name.
		 * @param value Localized name.
		 * @return This builder.
		 * @throws NullPointerException if the argument is null.
		 * @throws IllegalStateException if the localized name is being built
		 */
		public B setName(final Localized<Name> value) {
			Preconditions.checkState(mechanism != Mechanism.BUILDER);
			this.name = Preconditions.checkNotNull(value);
			mechanism = Mechanism.DIRECT;
			return thisValue();
		}

		private void checkBuilder() {
			Preconditions.checkState(mechanism != Mechanism.DIRECT);
			if (nameBuilder == null) {
				nameBuilder = new LocalizedBuilder<Name>();
			}
			mechanism = Mechanism.BUILDER;
		}

		/**
		 * Sets the default name.
		 * @param value Default name.
		 * @return This builder.
		 * @throws NullPointerException if the default name is null.
		 * @throws IllegalStateException if a localized name has been set.
		 */
		public B setDefaultName(final Name value) {
			checkBuilder();
			nameBuilder.setDefault(value);
			return thisValue();
		}

		/**
		 * Puts the name for a locale.
		 * @param locale Locale.
		 * @param value Name.
		 * @return This builder.
		 * @throws NullPointerException if any of the arguments is null.
		 * @throws IllegalStateException if a localized name has been set.
		 */
		public B putName(Locale locale, Name value) {
			checkBuilder();
			nameBuilder.put(locale, value);
			return thisValue();
		}

		/**
		 * Puts the name for a locale.
		 * @param locale Locale.
		 * @param value Name.
		 * @return This builder.
		 * @throws NullPointerException if any of the arguments is null.
		 * @throws IllegalArgumentException if unable to parse the locale.
		 * @throws IllegalStateException if a localized name has been set.
		 */
		public B putName(String locale, Name value) {
			checkBuilder();
			nameBuilder.put(locale, value);
			return thisValue();
		}

		/**
		 * Puts all the localized names from the argument map.
		 * @param names Localized names to add.
		 * @return This builder.
		 * @throws NullPointerException if any of the keys or values is null.
		 * @throws IllegalStateException if a localized name has been set.
		 */
		public B putNames(Map<? extends Locale, ? extends Name> names) {
			nameBuilder.putAll(names);
			return thisValue();
		}

		/**
		 * Puts all the localized names from the argument map.
		 * @param names Localized names to add.
		 * @return This builder.
		 * @throws NullPointerException if any of the keys or values is null.
		 * @throws IllegalStateException if a localized name has been set.
		 */
		public B putNamesWithStringLocale(Map<String, Name> names) {
			for (Entry<String, Name> name : names.entrySet()) {
				putName(name.getKey(), name.getValue());
			}
			return thisValue();
		}

		@Override
		public void checkState() {
			super.checkState();
			Preconditions.checkState(mechanism != Mechanism.NONE);
			if (mechanism == Mechanism.BUILDER) {
				this.name = nameBuilder.get();
			}
		}

	}

}
