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

package com.isotrol.impe3.pms.core.support;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.derquinsej.i18n.Locales;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.Identifiables;
import com.isotrol.impe3.core.support.Named;
import com.isotrol.impe3.pbuf.BaseProtos.DependencyPB;
import com.isotrol.impe3.pbuf.BaseProtos.LocalizedNamePB;
import com.isotrol.impe3.pbuf.BaseProtos.NamePB;
import com.isotrol.impe3.pms.api.Described;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.WithLocalizedNameDTO;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.user.DoneDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.model.Done;
import com.isotrol.impe3.pms.model.FileEntity;
import com.isotrol.impe3.pms.model.NameValue;
import com.isotrol.impe3.pms.model.UserEntity;
import com.isotrol.impe3.pms.model.WithLocalizedName;


/**
 * Several commonly used mappers.
 * @author Andres Rodriguez.
 */
public final class Mappers {
	/** Not instantiable. */
	private Mappers() {
		throw new AssertionError();
	}

	public static void named2described(Named named, Described described, Locale locale) {
		if (locale == null) {
			described.setName(named.getName().get());
			described.setDescription(named.getDescription().get());
		} else {
			described.setName(named.getName().get(locale));
			described.setDescription(named.getDescription().get(locale));
		}
	}

	/** NameValue to NameDTO. */
	public static final Function<NameValue, NameDTO> NAME2DTO = new Function<NameValue, NameDTO>() {
		public NameDTO apply(NameValue from) {
			if (from == null) {
				return null;
			}
			final NameDTO dto = new NameDTO();
			dto.setDisplayName(from.getName());
			dto.setPath(from.getPath());
			return dto;
		}
	};

	/** NameDTO to NameValue. */
	public static final Function<NameDTO, NameValue> DTO2NAME = new Function<NameDTO, NameValue>() {
		public NameValue apply(NameDTO from) {
			checkNotNull(from);
			final NameValue value = new NameValue();
			value.setName(checkNotNull(from.getDisplayName()));
			value.setPath(from.getPath());
			return value;
		}
	};

	/** UserEntity to UserSelDTO */
	public static <T extends UserSelDTO> T user2sel(UserEntity entity, T dto) {
		dto.setId(Identifiables.toStringId(entity.getId()));
		dto.setName(entity.getName());
		dto.setDisplayName(entity.getDisplayName());
		dto.setRoot(entity.isRoot());
		dto.setActive(entity.isActive());
		return dto;
	}

	/** UserEntity to UserSelDTO */
	public static UserSelDTO user2sel(UserEntity entity) {
		return user2sel(entity, new UserSelDTO());
	}

	/** UserEntity to UserSelDTO */
	public static final Function<UserEntity, UserSelDTO> USER2SEL = new Function<UserEntity, UserSelDTO>() {
		public UserSelDTO apply(UserEntity from) {
			return user2sel(from);
		};
	};

	/** Done to DoneDTO */
	public static DoneDTO done2dto(Done done) {
		if (done == null || done.getUser() == null) {
			return null;
		}
		DoneDTO dto = new DoneDTO();
		dto.setUser(user2sel(done.getUser()));
		dto.setTimestamp(new Date(done.getTimestamp().getTimeInMillis()));
		return dto;
	}

	/** PB Name to NameValue. */
	public static final Function<NamePB, NameValue> PB2NAME = new Function<NamePB, NameValue>() {
		public NameValue apply(NamePB from) {
			checkNotNull(from);
			final NameValue value = new NameValue();
			value.setName(checkNotNull(from.getDisplayName()));
			value.setPath(from.getPath());
			return value;
		}
	};

	/** PB Name to NameValue. */
	public static final Function<DependencyPB, DependencyDTO> DEP_PB2DTO = new Function<DependencyPB, DependencyDTO>() {
		public DependencyDTO apply(DependencyPB from) {
			checkNotNull(from);
			final DependencyDTO dto = new DependencyDTO();
			dto.setConnectorId(from.getProvision().getInstanceId());
			dto.setBean(from.getProvision().getBean());
			dto.setName(from.getName());
			return dto;
		}
	};

	/** FileEntity to UploadedFileDTO. */
	public static final Function<FileEntity, UploadedFileDTO> UPLOADED_FILE = new Function<FileEntity, UploadedFileDTO>() {
		public UploadedFileDTO apply(FileEntity from) {
			if (from == null) {
				return null;
			}
			final UploadedFileDTO dto = new UploadedFileDTO();
			dto.setId(from.getId().toString());
			dto.setName(from.getName());
			return dto;
		}
	};

	/**
	 * Copy a default and localized names from the model to a dto.
	 * @param value Value
	 * @param dto DTO.
	 */
	public static void localizedName2DTO(WithLocalizedName value, WithLocalizedNameDTO dto) {
		dto.setDefaultName(NAME2DTO.apply(value.getName()));
		dto.setLocalizedNames(map(value.getLocalizedNames(), NAME2DTO));
	}
	
	/**
	 * Property map to list of property DTOs
	 * @param map Map to transform.
	 * @return List of PropertyDTO.
	 */
	public static List<PropertyDTO> prop2dto(Map<String, String> map) {
		final List<PropertyDTO> list;
		if (map == null || map.isEmpty()) {
			list = Lists.newArrayListWithCapacity(0);
		} else {
			list = Lists.newArrayListWithCapacity(map.size());
			for (final Entry<String, String> base : map.entrySet()) {
				list.add(new PropertyDTO(base.getKey(), base.getValue()));
			}
		}
		return list;
	}
	

	/**
	 * Copy a default and localized names from the model to a dto.
	 * @param value Value
	 * @param dto DTO.
	 * @throws NullPointerException if any of the names is null.
	 * @throws IllegalArgumentException if any of the locales is invalid.
	 */
	public static void dto2localizedName(WithLocalizedNameDTO dto, WithLocalizedName value) {
		// Validation
		final Map<String, NameDTO> dtoMap = dto.getLocalizedNames();
		Preconditions.checkArgument(Iterables.all(dtoMap.keySet(), MoreLocales.VALID));
		// Copy
		value.setName(DTO2NAME.apply(dto.getDefaultName()));
		Map<String, NameValue> map = value.getLocalizedNames();
		map.clear();
		map.putAll(Maps.transformValues(dtoMap, DTO2NAME));
	}

	/**
	 * Copy a default and localized names from a pb to the model.
	 * @param dn Message for default name.
	 * @param lns Messages for localized names.
	 * @param value Value
	 * @throws NullPointerException if any of the names is null.
	 * @throws IllegalArgumentException if any of the locales is invalid.
	 */
	public static void pb2localizedName(NamePB dn, Iterable<LocalizedNamePB> lns, WithLocalizedName value) {
		value.setName(PB2NAME.apply(dn));
		Map<String, NameValue> map = value.getLocalizedNames();
		map.clear();
		if (lns != null) {
			for (LocalizedNamePB ln : lns) {
				Locales.fromString(ln.getLocale());
				map.put(ln.getLocale(), PB2NAME.apply(ln.getName()));
			}
		}
	}

	/**
	 * Creates a new map with the results of a value transformation.
	 * @param <K> Key type.
	 * @param <V1> Original value type.
	 * @param <V2> Transformed value type.
	 * @param from Original map.
	 * @param function Transformation function.
	 * @return The transformed map.
	 */
	public static <K, V1, V2> Map<K, V2> map(Map<K, V1> from, Function<? super V1, ? extends V2> function) {
		return Maps.newHashMap(Maps.transformValues(from, function));
	}

	/**
	 * Creates a new list with the results of a value transformation.
	 * @param <T> To type.
	 * @param <F> From type.
	 * @param from Original list.
	 * @param function Transformation function.
	 * @return The transformed list.
	 */
	public static <T, F> List<T> list(Iterable<F> from, Function<? super F, ? extends T> function) {
		return Lists.newArrayList(Iterables.transform(from, function));
	}

}
