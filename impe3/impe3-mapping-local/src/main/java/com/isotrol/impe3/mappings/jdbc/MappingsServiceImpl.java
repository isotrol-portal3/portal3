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

package com.isotrol.impe3.mappings.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.mappings.MappingDTO;
import com.isotrol.impe3.mappings.MappingsDTO;
import com.isotrol.impe3.mappings.MappingsService;


/**
 * MappingsService jdbc implementation
 * @author Emilio Escobar Reyero
 */
public class MappingsServiceImpl extends NamedParameterJdbcDaoSupport implements MappingsService {

	private Map<Key, MappingsDTO> mappings;

	private String sqlSelectVersion = "SELECT S.ID, S.VERSION "
		+ "FROM SOURCE_MAPPING S INNER JOIN ENVIRONMENT E ON S.ENVT_ID = E.ID WHERE E.NAME = :env AND S.NAME = :name";
	private String sqlSelectContents = "SELECT COTP_ID, MAPPING FROM CONTENT_TYPE_MAPPING WHERE SRCM_ID = :id";
	private String sqlSelectCategories = "SELECT CTGY_ID, MAPPING FROM CATEGORY_MAPPING WHERE SRCM_ID = :id";
	private String sqlSelectSets = "SELECT SRCM_SET, MAPPING FROM SET_MAPPING WHERE SRCM_ID = :id";

	private static final String ID = "id";

	/**
	 * Instance hash map
	 */
	@Override
	protected void initDao() throws Exception {
		super.initDao();
		this.mappings = Maps.newHashMap();
	}

	private static final ParameterizedRowMapper<MappingDTO> MAPPING_UUID = new ParameterizedRowMapper<MappingDTO>() {
		public MappingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			final MappingDTO dto = new MappingDTO();

			final String id = rs.getString(1);
			final String mapping = rs.getString(2);

			dto.setId(UUID.fromString(id).toString());
			dto.setMapping(mapping);

			return dto;
		}
	};

	private static final ParameterizedRowMapper<MappingDTO> MAPPING_STRING = new ParameterizedRowMapper<MappingDTO>() {
		public MappingDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
			final MappingDTO dto = new MappingDTO();

			final String id = rs.getString(1);
			final String mapping = rs.getString(2);

			dto.setId(id);
			dto.setMapping(mapping);

			return dto;
		}
	};

	private static final ParameterizedRowMapper<Object[]> VERSION = new ParameterizedRowMapper<Object[]>() {
		public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			final Object[] pair = new Object[2];

			final String id = rs.getString(1);
			final Integer version = Integer.valueOf(rs.getInt(2));

			pair[0] = id;
			pair[1] = version;

			return pair;
		}
	};

	/**
	 * 
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappings(java.lang.String, java.lang.String)
	 */
	@Transactional
	public MappingsDTO getMappings(String environment, String source) {
		if (environment == null || source == null) {
			return null;
		}

		final Object[] pair = queryForIdAndVersion(environment, source);

		if (pair == null) {
			return null;
		}

		final String id = (String) pair[0];
		final Integer version = (Integer) pair[1];

		return lookForId(Key.of(environment, source), version, id);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.mappings.MappingsService#getMappingsIfModified(java.lang.String, java.lang.String, int)
	 */
	@Transactional
	public MappingsDTO getMappingsIfModified(String environment, String source, int remote) {
		if (environment == null || source == null) {
			return null;
		}

		final Object[] pair = queryForIdAndVersion(environment, source);

		if (pair == null) {
			return null;
		}

		final String id = (String) pair[0];
		final Integer version = (Integer) pair[1];

		return lookForIdLastVersion(Key.of(environment, source), version, remote, id);
	}

	private Object[] queryForIdAndVersion(String environment, String source) {
		final Object pair = getNamedParameterJdbcTemplate().queryForObject(sqlSelectVersion,
			ImmutableMap.of("env", environment, "name", source), VERSION);

		if (pair == null) {
			return null;
		}

		return (Object[]) pair;
	}

	private MappingsDTO recoverMappingsDTO(String environment, String source, int version, String id) {

		final List<MappingDTO> categories = getNamedParameterJdbcTemplate().query(sqlSelectCategories,
			ImmutableMap.of(ID, id), MAPPING_UUID);
		final List<MappingDTO> contentTypes = getNamedParameterJdbcTemplate().query(sqlSelectContents,
			ImmutableMap.of(ID, id), MAPPING_UUID);
		final List<MappingDTO> sets = getNamedParameterJdbcTemplate().query(sqlSelectSets, ImmutableMap.of(ID, id),
			MAPPING_STRING);

		final MappingsDTO dto = new MappingsDTO();
		dto.setVersion(version);
		dto.setCategories(categories);
		dto.setContentTypes(contentTypes);
		dto.setSets(sets);

		return dto;
	}

	private synchronized MappingsDTO lookForId(Key key, int version, String id) {
		MappingsDTO mappingsDTO = mappings.get(key);

		if (mappingsDTO != null && mappingsDTO.getVersion() == version) {
			return cloneMappings(mappingsDTO, false);
		}

		mappingsDTO = recoverMappingsDTO(key.getEnvironment(), key.getSource(), version, id);

		if (mappingsDTO == null) {
			return null;
		}

		mappings.put(key, mappingsDTO);

		return cloneMappings(mappingsDTO, true);
	}

	private synchronized MappingsDTO lookForIdLastVersion(Key key, int lastversion, int remote, String id) {
		MappingsDTO mappingsDTO = mappings.get(key);

		if (mappingsDTO != null && mappingsDTO.getVersion() == lastversion && lastversion != remote) {
			return cloneMappings(mappingsDTO, true);
		}
		else if (mappingsDTO != null && mappingsDTO.getVersion() == remote && mappingsDTO.getVersion() == lastversion) {
			return cloneMappings(mappingsDTO, false);
		}

		mappingsDTO = recoverMappingsDTO(key.getEnvironment(), key.getSource(), lastversion, id);

		if (mappingsDTO == null) {
			return null;
		}

		mappings.put(key, mappingsDTO);

		return cloneMappings(mappingsDTO, true);
	}

	private MappingsDTO cloneMappings(MappingsDTO original, boolean includeLists) {
		if (original == null) {
			return null;
		}

		final MappingsDTO dto = new MappingsDTO();
		dto.setVersion(original.getVersion());

		if (includeLists) {
			if (original.getCategories() != null) {
				dto.setCategories(ImmutableList.copyOf(original.getCategories()));
			}

			if (original.getContentTypes() != null) {
				dto.setContentTypes(ImmutableList.copyOf(original.getContentTypes()));
			}

			if (original.getSets() != null) {
				dto.setSets(ImmutableList.copyOf(original.getSets()));
			}
		}

		return dto;
	}

	/** sets select categories sql string */
	public void setSqlSelectCategories(String sqlSelectCategories) {
		this.sqlSelectCategories = sqlSelectCategories;
	}

	/** sets select contents sql string */
	public void setSqlSelectContents(String sqlSelectContents) {
		this.sqlSelectContents = sqlSelectContents;
	}

	/** sets select version sql string */
	public void setSqlSelectVersion(String sqlSelectVersion) {
		this.sqlSelectVersion = sqlSelectVersion;
	}

	/** sets select sets sql string */
	public void setSqlSelectSets(String sqlSelectSets) {
		this.sqlSelectSets = sqlSelectSets;
	}

	/**
	 * Mapping map key implementation.
	 * @author Emilio Escobar Reyero
	 * 
	 */
	private static final class Key {
		private final String environment;
		private final String source;
		private final int hash;

		/**
		 * static factory for create new keys
		 * @param environment pms environment
		 * @param source pms source
		 * @return new key
		 */
		public static Key of(String environment, String source) {
			return new Key(environment, source);
		}

		private Key(String environment, String source) {
			Preconditions.checkNotNull(environment);
			Preconditions.checkNotNull(source);
			this.environment = environment;
			this.source = source;
			this.hash = Objects.hashCode(environment, source);
		}

		/** return environment */
		public String getEnvironment() {
			return environment;
		}

		/** return source */
		public String getSource() {
			return source;
		}

		/**
		 * Returns hash code (obtained form environment and source)
		 */
		@Override
		public int hashCode() {
			return hash;
		}

		/**
		 * Returns true if obj is instance of Key and environment and source are equals.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (!(obj instanceof Key)) {
				return false;
			}
			return this.environment.equals(((Key) obj).getEnvironment()) && this.source.equals(((Key) obj).getSource());
		}
	}

}
