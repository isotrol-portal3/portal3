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

package com.isotrol.impe3.nr.audit.jdbc;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.isotrol.impe3.nr.audit.api.AuditEngineMode;
import com.isotrol.impe3.nr.audit.api.AuditOperations;
import com.isotrol.impe3.nr.audit.api.AuditService;
import com.isotrol.impe3.nr.audit.api.AuditTask;
import com.isotrol.impe3.nr.audit.api.AuditValidationMode;
import com.isotrol.impe3.nr.audit.api.AuditedContent;


/**
 * Audit Service
 * @author Rafael Sepulveda
 */
public class AuditServiceImpl extends NamedParameterJdbcDaoSupport implements AuditService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String INDEXID = "indexid";
	private static final String ID = "id";
	private static final String CONTENTID = "contentid";
	private static final String CONTENTTYPEID = "contenttypeid";
	private static final String CATEGORYID = "categoryid";
	private static final String HASH = "hash";
	private static final String VIRTUALTIME = "virtualtime";
	private static final String CONTENTTIME = "contenttime";
	private static final String OPERATION = "operation";
	private static final String TIME = "time";
	private static final String LOCALE = "locale";
	private static final String ENGINEMODE = "enginemode";
	
	private static final String SEQNAME = "audit";
	
	private static final String sqlSelectContents = "SELECT A.CONTENT_ID, A.CONTENT_TYPE_ID, A.LOCALE, A.INDEX_ID, A.ENGINE_MODE, A.HASH, A.VIRTUAL_TIME, A.CONTENT_DATE "
		+ "FROM CONTENT_AUDIT A WHERE A.INDEX_ID = :"+INDEXID+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID AND A.OPERATION<>'"+AuditOperations.DELETE.toString()+"'";
	
	private static final String sqlInsert = "INSERT INTO CONTENT_AUDIT (CONTENT_AUDIT_ID, CONTENT_ID, CONTENT_TYPE_ID, LOCALE, INDEX_ID, ENGINE_MODE, HASH, VIRTUAL_TIME, CONTENT_DATE, OPERATION, TIME, VALID_OPERATION_ID) "
		+ " VALUES (:"+ID+", :"+CONTENTID+", :"+CONTENTTYPEID+", :"+LOCALE+", :"+INDEXID+", :"+ENGINEMODE+", :"+HASH+", :"+VIRTUALTIME+", :"+CONTENTTIME+", :"+OPERATION+", :"+TIME+", :"+ID+")";
	
//TODO get Category
	private static final String sqlgetLang = "SELECT  A.LOCALE FROM " +
			" CONTENT_AUDIT A WHERE A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID+" AND A.ENGINE_MODE=:"+ENGINEMODE+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID ";
	
	private static final String sqlUpdateValid = "UPDATE  CONTENT_AUDIT A SET A.VALID_OPERATION_ID = :"+ID
		+ "  WHERE  A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID+" AND A.ENGINE_MODE=:"+ENGINEMODE+" AND  A.LOCALE=:"+LOCALE;
	
	private static final String sqlGetSequence = "SELECT SEQ_VALUE FROM SEQUENCE_T WHERE SEQ_NAME='"+SEQNAME+"' FOR UPDATE ";
	  
	private static final String sqlupdateSequence = "UPDATE SEQUENCE_T SET SEQ_VALUE = SEQ_VALUE + 1 WHERE SEQ_NAME='"+SEQNAME+"'";
	
	private static final String sqlInsertCats = "INSERT INTO  CONTENT_AUDIT_CATEGORIES (CONTENT_AUDIT_ID,CATEGORY_ID)  VALUES ( :"+ID+", :"+CATEGORYID+")";
	
	/*
	private static final String sqlSelectCats = "SELECT cat.ID, cat.CATEGORY_ID FROM CONTENT_AUDIT_CATEGORIES cat " +
			" INNER JOIN CONTENT_AUDIT A ON A.ID=cat.ID " +
			" WHERE A.INDEX_ID = :"+INDEXID+" AND A.VALID_OPERATION_ID=A.ID AND A.OPERATION<>'"+getOperationCode(AuditOperations.DELETE)+"'";
	*/
	private static final String sqlExistFromHash = "SELECT COUNT(A.CONTENT_ID)"
			+ " FROM CONTENT_AUDIT A WHERE  A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID
			+ " AND A.ENGINE_MODE=:"+ENGINEMODE+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID AND A.LOCALE=:"+LOCALE+" AND A.HASH=:"+HASH;
	private static final String sqlExistFromVirtualTime = "SELECT COUNT(A.CONTENT_ID)"
			+ " FROM CONTENT_AUDIT A WHERE  A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID
			+ " AND A.ENGINE_MODE=:"+ENGINEMODE+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID AND A.LOCALE=:"+LOCALE+" AND A.VIRTUAL_TIME>=:"+VIRTUALTIME;
	private static final String sqlExistFromVirtualTimeAndHash = "SELECT COUNT(A.CONTENT_ID)"
			+ " FROM CONTENT_AUDIT A WHERE  A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID
			+ " AND A.ENGINE_MODE=:"+ENGINEMODE+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID AND A.LOCALE=:"+LOCALE+" AND A.VIRTUAL_TIME>=:"+VIRTUALTIME+" AND A.HASH=:"+HASH;
	
	private static final String sqlExistDeleted = "SELECT COUNT(A.CONTENT_ID)"
			+ " FROM CONTENT_AUDIT A WHERE  A.CONTENT_ID=:"+CONTENTID+" AND A.CONTENT_TYPE_ID=:"+CONTENTTYPEID+" AND A.INDEX_ID=:"+INDEXID
			+ " AND A.ENGINE_MODE=:"+ENGINEMODE+" AND A.VALID_OPERATION_ID=A.CONTENT_AUDIT_ID AND A.LOCALE=:"+LOCALE+" AND A.OPERATION = '"+AuditOperations.DELETE.toString()+"'";
	
	
	//TODO seleccionar idiomas del contenido

	private static final ParameterizedRowMapper<AuditedContent> AUDITTEDCONTENT = new ParameterizedRowMapper<AuditedContent>() {
		public AuditedContent mapRow(ResultSet rs, int rowNum) throws SQLException {
			int i=1;
			final String contentId = rs.getString(i++);
			final UUID contentTypeId = UUID.fromString(rs.getString(i++));
			final String locale = rs.getString(i++);
			final String indexId = rs.getString(i++);
			final String engineString = rs.getString(i++);
			final AuditEngineMode engineMode = AuditEngineMode.fromString(engineString);
			final String hash = rs.getString(i++);
			final String virtualTimeString = rs.getString(i++);
			final Long virtualTime;
			if (virtualTimeString!=null){
				virtualTime = Long.parseLong(virtualTimeString);
			} else {
				virtualTime = null;
			}
			final String contentTimeString = rs.getString(i++);
			final Long contentTime;
			if (contentTimeString!=null){
				contentTime = Long.parseLong(contentTimeString);
			} else {
				contentTime = null;
			}
			
			final AuditedContent content = AuditedContent.withVirtualTimeAndHash(contentId, contentTypeId, ImmutableList.<String>of(), locale, indexId,engineMode, hash, virtualTime,contentTime);

			return content;
		}
	};

	@Transactional
	public Collection<AuditedContent> getContents(String idIndexador) {
		
		final List<AuditedContent> contents = getNamedParameterJdbcTemplate().query(sqlSelectContents,
				ImmutableMap.of(INDEXID, idIndexador),AUDITTEDCONTENT);
		return contents;
	}
	
	private String getTimestamp(){
		return Long.toString(System.currentTimeMillis());
	}
	
	@Transactional
	public void registerOperation(AuditTask task) {
		String timestamp = getTimestamp();
		AuditedContent content=task.getContent();
		AuditOperations operation=task.getOp();
		String operationCode = operation.toString();
		Map<String,Object> parameterMap= Maps.newHashMap();
		int i = 0;
		
		parameterMap.put(INDEXID, content.getIndexId());
		parameterMap.put(CONTENTID, content.getContentId());
		parameterMap.put(CONTENTTYPEID, content.getContentTypeId().toString());
		parameterMap.put(LOCALE, content.getLocale());
		parameterMap.put(ENGINEMODE, content.getEngineMode().toString());
		i = getNamedParameterJdbcTemplate().queryForInt(sqlExistDeleted, parameterMap);
		if (i==0){
			if (!task.getOp().equals(AuditOperations.DELETE)){
				//solamente se verifica en el caso que la operacion no sea borrado.
				//para un borrado ya se comprueba con la opcion si existe borrado
				final String sqlVerification;
				if (AuditValidationMode.VIRTUALTIME.equals(task.getValidationMode()) || content.getHash()==null){
					sqlVerification=sqlExistFromVirtualTime;
					parameterMap.put(VIRTUALTIME, content.getVirtualTime());
				} else if (AuditValidationMode.HASH.equals(task.getValidationMode()) || content.getVirtualTime()==null){
					sqlVerification=sqlExistFromHash;
					parameterMap.put(HASH, content.getHash());
				} else {
					sqlVerification=sqlExistFromVirtualTimeAndHash;
					parameterMap.put(VIRTUALTIME, content.getVirtualTime());
					parameterMap.put(HASH, content.getHash());
				}
				i = getNamedParameterJdbcTemplate().queryForInt(sqlVerification, parameterMap);
			}
			if (i==0){
				parameterMap= Maps.newHashMap();
				Long nextId = getNamedParameterJdbcTemplate().queryForLong(sqlGetSequence, parameterMap);
				parameterMap.put(INDEXID, content.getIndexId());
				parameterMap.put(ID, nextId);
				parameterMap.put(CONTENTID, content.getContentId());
				parameterMap.put(CONTENTTYPEID, content.getContentTypeId().toString());
				parameterMap.put(OPERATION, operationCode);
				parameterMap.put(HASH, content.getHash());
				parameterMap.put(VIRTUALTIME,  content.getVirtualTime());
				parameterMap.put(CONTENTTIME,content.getContentTime());
				parameterMap.put(TIME, timestamp);
				parameterMap.put(ENGINEMODE, content.getEngineMode().toString());
				if (content.getLocale()!=null){
					parameterMap.put(LOCALE, content.getLocale());
					i = getNamedParameterJdbcTemplate().update(sqlInsert,parameterMap);
					if (i==0){
						
					}
					for (String cat : content.getcategoriesId()) {
						Map<String,Object> parameterMapCat= Maps.newHashMap();
						parameterMapCat.put(ID, nextId);
						parameterMapCat.put(CATEGORYID, cat);
						i = getNamedParameterJdbcTemplate().update(sqlInsertCats,parameterMapCat);
					}
					i=getNamedParameterJdbcTemplate().update(sqlUpdateValid, parameterMap);
					i=getNamedParameterJdbcTemplate().update(sqlupdateSequence, parameterMap);
				} else {
					List<String> langs = getNamedParameterJdbcTemplate().queryForList(sqlgetLang, parameterMap,String.class);
					for (String lang : langs) {
						parameterMap.put(LOCALE, lang);
						i = getNamedParameterJdbcTemplate().update(sqlInsert,parameterMap);
						if (i==0){
							
						}
						for (String cat : content.getcategoriesId()) {
							Map<String,Object> parameterMapCat= Maps.newHashMap();
							parameterMap.put(ID, nextId);
							parameterMap.put(CATEGORYID, cat);
							i = getNamedParameterJdbcTemplate().update(sqlInsertCats,parameterMapCat);
						}

						i=getNamedParameterJdbcTemplate().update(sqlUpdateValid, parameterMap);
						i=getNamedParameterJdbcTemplate().update(sqlupdateSequence, parameterMap);
						nextId = getNamedParameterJdbcTemplate().queryForLong(sqlGetSequence, parameterMap);
					}
				}
				
				
				
			} else {
				logger.debug("Operacion ya registrada ");
			}
		}else{
			logger.debug("Operacion sobre contenido borrado ");
		}
	}
}
