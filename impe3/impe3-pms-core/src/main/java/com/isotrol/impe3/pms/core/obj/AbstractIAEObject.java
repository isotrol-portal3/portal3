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
import static com.isotrol.impe3.pms.core.obj.ObjectFunctions.NAME2DTO;
import static com.isotrol.impe3.pms.core.support.Functions.value2name;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.derquinsej.i18n.Locales;
import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.LocalizedBuilder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.RoutableNamedIdentifiable;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.WithLocalizedNameDTO;
import com.isotrol.impe3.pms.core.support.AbstractSimpleValueLoader;
import com.isotrol.impe3.pms.model.AbstractIAEDfn;
import com.isotrol.impe3.pms.model.NameValue;


/**
 * Abstract superclass for information architecture domain objects.
 * @author Andres Rodriguez
 */
public abstract class AbstractIAEObject extends AbstractRoutableObject {
	static <B extends RoutableNamedIdentifiable.Builder<B, ?>> B set(B builder, AbstractIAEObject obj) {
		return builder.setId(obj.getId()).setName(obj.names.name).setRoutable(obj.isRoutable());
	}

	private static final NamesLoader LOADER = new NamesLoader();

	/** Names. */
	private final Names names;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 * @param function State function to apply.
	 */
	public AbstractIAEObject(AbstractIAEDfn<?, ?, ?> dfn) {
		super(dfn);
		this.names = LOADER.get(dfn);
	}

	public final Localized<Name> getName() {
		return names.name;
	}

	public final Name getDefaultName() {
		return names.name.get();
	}

	public Map<Locale, Name> getLocalizedNames() {
		return names.localizedNames;
	}

	final void fillName(WithLocalizedNameDTO dto) {
		dto.setDefaultName(NAME2DTO.apply(getName().get()));
		Map<String, NameDTO> map = Maps.newHashMap();
		for (Entry<Locale, Name> e : getLocalizedNames().entrySet()) {
			map.put(e.getKey().toString(), NAME2DTO.apply(e.getValue()));
		}
		dto.setLocalizedNames(map);
	}

	private static final class Names {
		/** Name. */
		private final Localized<Name> name;
		/** Localized names. */
		private final ImmutableMap<Locale, Name> localizedNames;

		Names(Localized<Name> name, ImmutableMap<Locale, Name> localizedNames) {
			this.name = checkNotNull(name);
			this.localizedNames = checkNotNull(localizedNames);
		}
	}

	private static final class NamesLoader extends AbstractSimpleValueLoader<AbstractIAEDfn<?, ?, ?>, Names> {
		NamesLoader() {
			super("IA Names");
		}

		@Override
		protected Names load(AbstractIAEDfn<?, ?, ?> dfn) {
			final LocalizedBuilder<Name> b = LocalizedBuilder.create();
			final ImmutableMap.Builder<Locale, Name> mb = ImmutableMap.builder();
			b.setDefault(value2name(dfn.getName()));
			for (Entry<String, NameValue> entry : dfn.getLocalizedNames().entrySet()) {
				final String k = entry.getKey();
				try {
					final Locale locale = Locales.fromString(k);
					final Name n = value2name(entry.getValue());
					b.put(locale, n);
					mb.put(locale, n);
				} catch (IllegalArgumentException e) {
					// TODO: warn
				}
			}
			return new Names(b.get(), mb.build());
		}
	}

}
