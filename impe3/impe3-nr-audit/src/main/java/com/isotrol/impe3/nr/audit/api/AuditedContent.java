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

package com.isotrol.impe3.nr.audit.api;

import java.util.List;
import java.util.UUID;

import com.google.common.base.Preconditions;

/**
 * Class Storing a content that should be covered by the audit service
 *  
 * 
 * 
 * @author fujitsu
 *
 */
public class AuditedContent {
	
	private final String contentId;
	private final UUID contentTypeId;
	private final List<String> categories;
	private final String indexId;
	private final String hash;
	private final Long virtualTime;
	private final String locale;
	private final Long contentTime;
	private final AuditEngineMode engineMode;
	
	public static AuditedContent withVirtualTime(String contentId, UUID contentTypeId, List<String> categories, String locale, String indexId, AuditEngineMode engineMode, Long virtualTime, Long contentTime) {
		return new AuditedContent(contentId, contentTypeId,categories, locale, indexId,engineMode, null, virtualTime,contentTime);
	}
	
	public static AuditedContent withHash(String contentId, UUID contentTypeId, List<String> categories, String locale, String indexId, AuditEngineMode engineMode, String hash, Long contentTime) {
		return new AuditedContent(contentId, contentTypeId,categories, locale, indexId, engineMode, hash , null,contentTime);
	}
	
	public static AuditedContent withVirtualTimeAndHash(String contentId, UUID contentTypeId, List<String> categories, String locale, String indexId, AuditEngineMode engineMode, String hash, Long virtualTime, Long contentTime) {
		return new AuditedContent(contentId, contentTypeId,categories, locale, indexId, engineMode, hash , virtualTime,contentTime);
	}
	
	public static AuditedContent simple(String contentId, UUID contentTypeId,List<String> categories, String locale, String indexId, AuditEngineMode engineMode, Long contentTime) {
		return new AuditedContent(contentId, contentTypeId, categories, locale, indexId, engineMode, null , null,contentTime);
	}
	
	private AuditedContent(String contentId, UUID contentTypeId,List<String> categories, String locale, String indexId,AuditEngineMode engineMode, String hash, Long virtualTime, Long contentTime) {
		super();
		Preconditions.checkNotNull(contentId);
		Preconditions.checkNotNull(contentTypeId);
		Preconditions.checkNotNull(indexId);
		this.contentId = contentId;
		this.contentTypeId = contentTypeId;
		this.indexId = indexId;
		this.hash = hash;
		this.virtualTime = virtualTime;
		this.locale = locale;
		this.categories=categories;
		this.contentTime=contentTime;
		this.engineMode=engineMode;
	}
	public String getContentId() {
		return contentId;
	}
	public UUID getContentTypeId() {
		return contentTypeId;
	}
	public String getIndexId() {
		return indexId;
	}
	public String getHash() {
		return hash;
	}
	public Long getVirtualTime() {
		return virtualTime;
	}
	public String getLocale() {
		return locale;
	}
	public List<String> getcategoriesId() {
		return categories;
	}

	public Long getContentTime() {
		return contentTime;
	}
	
	public AuditEngineMode getEngineMode() {
		return engineMode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentId == null) ? 0 : contentId.hashCode());
		result = prime * result + ((contentTypeId == null) ? 0 : contentTypeId.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((indexId == null) ? 0 : indexId.hashCode());
		result = prime * result + ((virtualTime == null) ? 0 : virtualTime.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((engineMode == null) ? 0 : engineMode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditedContent other = (AuditedContent) obj;
		if (contentId == null) {
			if (other.contentId != null)
				return false;
		} else if (!contentId.equals(other.contentId))
			return false;
		if (contentTypeId == null) {
			if (other.contentTypeId != null)
				return false;
		} else if (!contentTypeId.equals(other.contentTypeId))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (indexId == null) {
			if (other.indexId != null)
				return false;
		} else if (!indexId.equals(other.indexId))
			return false;
		if (virtualTime == null) {
			if (other.virtualTime != null)
				return false;
		} else if (!virtualTime.equals(other.virtualTime))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (engineMode == null) {
			if (other.engineMode != null)
				return false;
		} else if (!engineMode.equals(other.engineMode))
			return false;
		return true;
	}
	
	
	
}
