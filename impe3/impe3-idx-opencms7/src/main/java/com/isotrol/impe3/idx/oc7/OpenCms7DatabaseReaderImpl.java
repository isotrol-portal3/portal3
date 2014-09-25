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
package com.isotrol.impe3.idx.oc7;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.idx.XML;
import com.isotrol.impe3.idx.oc.Attached;
import com.isotrol.impe3.idx.oc.OpenCmsContent;
import com.isotrol.impe3.idx.oc.OpenCmsContent.OpenCmsContentBuilder;


/**
 * 
 * @author Emilio Escobar Reyero
 * @modified Juan Manuel Valverde Ramírez
 */
public class OpenCms7DatabaseReaderImpl extends NamedParameterJdbcDaoSupport implements OpenCms7DatabaseReader {

	final Logger logger = LoggerFactory.getLogger(getClass());

	/** Mode [OFFLINE, ONLINE] id */
	private String table = "OFFLINE";
	/** Default encoding utf-8 */
	private String encoding = "UTF-8";

	private String selectContent;
	private String selectBytes;
	private String selectProperties;
	private String selectCategories;
	/**
	 * Consulta para obtener los ficheros adjuntos (pdf,xls,doc) de un id dado.
	 */
	private String selectAttached;
	/**
	 * Obtiene el id de los adjuntos de un id dado. Con este id podemos obtener el byte[].
	 */
	private String selectAttachedId;
	/**
	 * Obtiene el byte[] de los ficheros adjuntos a un resource_id dado.
	 */
	private String selectAttachedBytes;

	/**
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	@Override
	protected void initDao() throws Exception {
		this.logger.info("Init OpenCms 7 database reader.");

		this.selectContent = "select S.STRUCTURE_ID, R.RESOURCE_TYPE, R.RESOURCE_STATE, "
			+ "R.DATE_CREATED, R.DATE_LASTMODIFIED, S.RESOURCE_PATH,S.DATE_RELEASED,S.DATE_EXPIRED " + "from CMS_"
			+ this.table + "_RESOURCES R, CMS_" + this.table + "_STRUCTURE S "
			+ "where S.STRUCTURE_ID = :id and R.RESOURCE_ID=S.RESOURCE_ID";

		String table_contents = this.table.equals("ONLINE") ? "CMS_CONTENTS" : "CMS_OFFLINE_CONTENTS";

		this.selectBytes = "select C.FILE_CONTENT from CMS_" + this.table + "_STRUCTURE S, " + table_contents
			+ " C where S.STRUCTURE_ID = :id and S.RESOURCE_ID = C.RESOURCE_ID"
			+ (this.table.equals("ONLINE") ? " AND C.ONLINE_FLAG='1'" : "");

		this.selectProperties = "select D.PROPERTYDEF_NAME, P.PROPERTY_VALUE "
			+ "from CMS_OFFLINE_STRUCTURE S, CMS_OFFLINE_PROPERTIES P, CMS_OFFLINE_PROPERTYDEF D where "
			+ "S.STRUCTURE_ID = :id and "
			+ " ( (P.PROPERTY_MAPPING_ID = S.STRUCTURE_ID AND PROPERTY_MAPPING_TYPE='1' )"
			+ " OR ( P.PROPERTY_MAPPING_ID = S.RESOURCE_ID AND PROPERTY_MAPPING_TYPE='2' )) "
			+ "and P.PROPERTYDEF_ID = D.PROPERTYDEF_ID";

		this.selectCategories = "select RELATION_TARGET_ID " + "from CMS_" + this.table
			+ "_RESOURCE_RELATIONS where RELATION_SOURCE_ID=:id and RELATION_TYPE=9";

		this.selectAttached = "SELECT RELATION_TARGET_ID FROM CMS_" + this.table
			+ "_RESOURCE_RELATIONS WHERE RELATION_SOURCE_ID = :id"
			+ " AND RELATION_TYPE = 4 AND (RELATION_TARGET_PATH LIKE '%.pdf' OR RELATION_TARGET_PATH LIKE '%.doc'"
			+ " OR RELATION_TARGET_PATH LIKE '%.xls')";

		this.selectAttachedId = "SELECT DISTINCT c.RESOURCE_ID, s.RESOURCE_PATH"
			+ " FROM CMS_"
			+ this.table
			+ "_RESOURCE_RELATIONS r, CMS_"
			+ this.table
			+ "_STRUCTURE s, "
			+ table_contents
			+ " c"
			+ " WHERE r.RELATION_TYPE = 4 AND r.RELATION_SOURCE_ID = :id AND"
			+ " (r.RELATION_TARGET_PATH LIKE '%.pdf' OR r.RELATION_TARGET_PATH LIKE '%.doc' OR r.RELATION_TARGET_PATH LIKE '%.xls')"
			+ " AND s.STRUCTURE_ID = r.RELATION_TARGET_ID AND c.RESOURCE_ID = s.RESOURCE_ID"
			+ (this.table.equals("ONLINE") ? " AND c.ONLINE_FLAG='1'" : "");

		this.selectAttachedBytes = "SELECT c.FILE_CONTENT FROM " + table_contents + " c" + " WHERE c.RESOURCE_ID = :id"
			+ (this.table.equals("ONLINE") ? " AND c.ONLINE_FLAG='1'" : "");

		super.initDao();
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	private static final SingleColumnRowMapper CATEGORY_MAPPER = new SingleColumnRowMapper(String.class);

	private static final SingleColumnRowMapper ATTACHED_MAPPER = new SingleColumnRowMapper(String.class);

	private static final ParameterizedRowMapper<OpenCmsContentBuilder> CONTENT_MAPPER = new ParameterizedRowMapper<OpenCmsContentBuilder>() {
		public OpenCmsContentBuilder mapRow(ResultSet rs, int rowNum) throws SQLException {
			OpenCmsContentBuilder builder = OpenCmsContent.builder();

			// R.RESOURCE_ID, R.RESOURCE_TYPE, R.RESOURCE_STATE, R.DATE_CREATED, R.DATE_LASTMODIFIED, S.RESOURCE_PATH

			int i = 1;
			builder.setId(rs.getString(i++));
			builder.setType(rs.getInt(i++));
			builder.setState(rs.getInt(i++));
			builder.setDateCreated(new Date(rs.getLong(i++)));
			builder.setDateLastModified(new Date(rs.getLong(i++)));
			builder.setPath(rs.getString(i++));
			builder.setDateReleased(new Date(rs.getLong(i++)));
			builder.setDateExpired(new Date(rs.getLong(i++)));

			return builder;
		}
	};

	/**
	 * Devuelve un objeto Adjunto con el id y su path.
	 * @author Juan Manuel Valverde Ramírez
	 */
	private static final ParameterizedRowMapper<Attached> ATTACHED_DATA_MAPPER = new ParameterizedRowMapper<Attached>() {
		public Attached mapRow(ResultSet rs, int rowNum) throws SQLException {

			Attached result = new Attached();

			result.setId(rs.getString("RESOURCE_ID"));
			result.setPath(rs.getString("RESOURCE_PATH"));

			return result;
		}
	};

	/**
	 * Devuelve el InputStream del adjunto del que podemos sacar la información.
	 * @author Juan Manuel Valverde Ramírez
	 */
	private static final ParameterizedRowMapper<InputStream> ATTACHED_INPUTSTREAM_MAPPER = new ParameterizedRowMapper<InputStream>() {
		public InputStream mapRow(ResultSet rs, int rowNum) throws SQLException {

			// OJO: el InputStream que devuelve es del tipo del campo de la base de datos, en oracle es
			// OracleBlobInputStream
			final InputStream result = rs.getBinaryStream("FILE_CONTENT");

			return result;
		}
	};

	private final ParameterizedRowMapper<byte[]> ATTACHED_BYTES_MAPPER = new ParameterizedRowMapper<byte[]>() {
		public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {

			byte[] value = null;

			value = rs.getBytes("FILE_CONTENT");

			return value;
		}
	};

	private static final ParameterizedRowMapper<XML> XML_MAPPER = new ParameterizedRowMapper<XML>() {
		public XML mapRow(ResultSet rs, int rowNum) throws SQLException {

			XML xml = XML.of(rs, 1);

			return xml;
		}
	};

	private static final ParameterizedRowMapper<String[]> PROPERTY_MAPPER = new ParameterizedRowMapper<String[]>() {
		public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
			int i = 1;
			final String[] property = {rs.getString(i++), rs.getString(i++)};

			return property;
		}
	};

	private final ParameterizedRowMapper<String> BYTES_MAPPER = new ParameterizedRowMapper<String>() {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			final InputStream is = rs.getBinaryStream(1);

			String value;
			if (is != null) {
				final byte[] buffer = new byte[2048];
				final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
				int leidos;
				try {
					while ((leidos = is.read(buffer)) > 0) {
						baos.write(buffer, 0, leidos);
					}
					byte[] data = baos.toByteArray();
					value = new String(data, encoding);
				} catch (IOException e) {
					value = null;
				}
			} else {
				value = null;
			}

			return value;
		}
	};

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc7.OpenCms7DatabaseReader#createBuilder(java.lang.String)
	 */
	@Transactional
	public OpenCmsContentBuilder createBuilder(String id) {
		return (OpenCmsContentBuilder) getNamedParameterJdbcTemplate().queryForObject(selectContent,
			ImmutableMap.of("id", id), CONTENT_MAPPER);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc7.OpenCms7DatabaseReader#readContentBytes(java.lang.String)
	 */
	@Transactional
	public String readContentBytes(String id) {
		return (String) getNamedParameterJdbcTemplate().queryForObject(selectBytes, ImmutableMap.of("id", id),
			BYTES_MAPPER);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc7.OpenCms7DatabaseReader#readContentCategories(java.lang.String)
	 */
	@Transactional
	public List<String> readContentCategories(String id) {
		return getNamedParameterJdbcTemplate().query(selectCategories, ImmutableMap.of("id", id), CATEGORY_MAPPER);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc7.OpenCms7DatabaseReader#readContentProperties(java.lang.String)
	 */
	@Transactional
	public List<String[]> readContentProperties(String id) {
		return (List<String[]>) getNamedParameterJdbcTemplate().query(selectProperties, ImmutableMap.of("id", id),
			PROPERTY_MAPPER);
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.oc7.OpenCms7DatabaseReader#readContentXml(java.lang.String)
	 */
	@Transactional
	public XML readContentXml(String id) {
		return (XML) getNamedParameterJdbcTemplate().queryForObject(selectBytes, ImmutableMap.of("id", id), XML_MAPPER);
	}

	/**
	 * Lee los ficheros adjuntos al id pasado.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	@Transactional
	public List<String> readAttached(String id) {
		return getNamedParameterJdbcTemplate().query(this.selectAttached, ImmutableMap.of("id", id),
			this.ATTACHED_MAPPER);
	}

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	@Transactional
	public List<Attached> readAttachedIds(String id) {

		return getNamedParameterJdbcTemplate().query(this.selectAttachedId, ImmutableMap.of("id", id),
			this.ATTACHED_DATA_MAPPER);
	}

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	@Transactional
	public byte[] readAttachedBytes(String id) {
		return (byte[]) getNamedParameterJdbcTemplate().queryForObject(this.selectAttachedBytes,
			ImmutableMap.of("id", id), this.ATTACHED_BYTES_MAPPER);
	}

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	@Transactional
	public InputStream readAttachedInputStream(String id) {
		return (InputStream) getNamedParameterJdbcTemplate().queryForObject(this.selectAttachedBytes,
			ImmutableMap.of("id", id), ATTACHED_INPUTSTREAM_MAPPER);
	}

}
