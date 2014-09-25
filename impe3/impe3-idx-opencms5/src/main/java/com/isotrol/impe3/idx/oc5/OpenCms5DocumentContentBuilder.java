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
package com.isotrol.impe3.idx.oc5;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.apache.lucene.document.Document;

import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.Task;
import com.isotrol.impe3.idx.oc.DocumentContentBuilder;
import com.isotrol.impe3.idx.oc.IndexCommand;
import com.isotrol.impe3.idx.oc.IndexConfiguration;
import com.isotrol.impe3.idx.oc.OpenCmsDatabaseReader;

public class OpenCms5DocumentContentBuilder implements DocumentContentBuilder<Task>{

	/** Default encoding utf-8 */
	private String encoding = "UTF-8";
	/** If set, only index that locale */
	private Locale lang;
	/** Categories base path */
	private String categoriesBase = "/system/categories/";
	/** Index default lang when isn't translate */
	private boolean indexDefaultLanguageContent = true;

	private boolean readAttachments = true;

	private boolean compress = true;

	private IndexConfiguration indexResourceTypes;
	private LocalMappingsService mappingsService;
	private OpenCmsDatabaseReader databaseReader;
	private Map<String, IndexCommand> indexCommandBeans = Collections.emptyMap();
	
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setLang(Locale lang) {
		this.lang = lang;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public void setReadAttachments(boolean readAttachments) {
		this.readAttachments = readAttachments;
	}

	public void setCategoriesBase(String categoriesBase) {
		this.categoriesBase = categoriesBase;
	}

	public void setIndexDefaultLanguageContent(boolean indexDefaultLanguageContent) {
		this.indexDefaultLanguageContent = indexDefaultLanguageContent;
	}

	public void setIndexResourceTypes(IndexConfiguration indexResourceTypes) {
		this.indexResourceTypes = indexResourceTypes;
	}

	public void setMappingsService(LocalMappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}

	public void setDatabaseReader(OpenCmsDatabaseReader databaseReader) {
		this.databaseReader = databaseReader;
	}
	
	public Document[] createDocuments(Task task) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
