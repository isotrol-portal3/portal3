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
import static com.google.common.collect.Iterables.transform;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.pbuf.BaseProtos.LocalizedNamePB;
import com.isotrol.impe3.pbuf.BaseProtos.NamePB;
import com.isotrol.impe3.pbuf.BaseProtos.ProvisionPB;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntriesPB;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntryPB;


/**
 * Several commonly used protocol buffers message mappers.
 * @author Andres Rodriguez.
 */
public final class MessageMappers {
	/** Not instantiable. */
	private MessageMappers() {
		throw new AssertionError();
	}

	public static Function<Name, NamePB> name() {
		return NameMapper.INSTANCE;
	}

	private static enum NameMapper implements Function<Name, NamePB> {
		INSTANCE;

		public NamePB apply(Name from) {
			NamePB.Builder b = NamePB.newBuilder();
			b.setDisplayName(from.getDisplayName());
			if (from.getPath() != null) {
				b.setPath(from.getPath());
			}
			return b.build();
		}
	}

	public static Iterable<LocalizedNamePB> localizedName(Map<Locale, Name> names) {
		return Iterables.transform(names.entrySet(), LocalizedNameMapper.INSTANCE);
	}

	private static enum LocalizedNameMapper implements Function<Entry<Locale, Name>, LocalizedNamePB> {
		INSTANCE;

		public LocalizedNamePB apply(Entry<Locale, Name> from) {
			LocalizedNamePB.Builder b = LocalizedNamePB.newBuilder();
			b.setLocale(from.getKey().toString());
			b.setName(NameMapper.INSTANCE.apply(from.getValue()));
			return b.build();
		}
	}

	public static StringEntryPB stringEntry(Object key, Object value) {
		checkNotNull(key, "The key must be provided");
		checkNotNull(value, "The value must be provided");
		return StringEntryPB.newBuilder().setKey(key.toString()).setValue(value.toString()).build();
	}

	public static <K, V> Function<Entry<K, V>, StringEntryPB> stringEntry() {
		return new Function<Entry<K, V>, StringEntryPB>() {
			public StringEntryPB apply(Entry<K, V> from) {
				return stringEntry(from.getKey(), from.getValue());
			}
		};
	}

	public static <K, V> Iterable<StringEntryPB> transformSE(Iterable<Entry<K, V>> entries) {
		return transform(entries, MessageMappers.<K, V> stringEntry());
	}

	public static <K, V> Iterable<StringEntryPB> transformSE(Map<K, V> map) {
		return transformSE(map.entrySet());
	}

	public static <K, V> StringEntriesPB stringEntries(Map<K, V> map) {
		return StringEntriesPB.newBuilder().addAllEntries(transformSE(map)).build();
	}

	private static enum SEKey implements Function<StringEntryPB, String> {
		INSTANCE;

		public String apply(StringEntryPB from) {
			return from.getKey();
		}
	}

	private static enum SEValue implements Function<StringEntryPB, String> {
		INSTANCE;

		public String apply(StringEntryPB from) {
			return from.getValue();
		}
	}

	public static Iterable<String> seKeys(Iterable<StringEntryPB> entries) {
		return transform(entries, SEKey.INSTANCE);
	}

	public static Iterable<String> seKeys(StringEntriesPB entries) {
		return seKeys(entries.getEntriesList());
	}

	public static Iterable<String> seValues(Iterable<StringEntryPB> entries) {
		return transform(entries, SEValue.INSTANCE);
	}

	public static Iterable<String> seValues(StringEntriesPB entries) {
		return seValues(entries.getEntriesList());
	}

	public static Map<String, String> seMap(Iterable<StringEntryPB> entries) {
		Map<String, String> map = Maps.newHashMap();
		for (StringEntryPB e : entries) {
			map.put(e.getKey(), e.getValue());
		}
		return map;
	}

	public static Map<String, String> seMap(StringEntriesPB entries) {
		return seMap(entries.getEntriesList());
	}

	public static ProvisionPB provisionPB(String instanceId, String bean) {
		return ProvisionPB.newBuilder().setInstanceId(instanceId).setBean(bean).build();
	}

	public static ProvisionPB provisionPB(UUID instanceId, String bean) {
		return provisionPB(instanceId.toString(), bean);
	}

	public static ProvisionPB provisionPB(Provider p) {
		return provisionPB(p.getConnectorId(), p.getBean());
	}

}
