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

import java.io.InputStream;
import java.util.List;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.isotrol.impe3.idx.XML;
import com.isotrol.impe3.idx.oc.Attached;
import com.isotrol.impe3.idx.oc.OpenCmsDatabaseReader;
import com.isotrol.impe3.idx.oc.OpenCmsContent.OpenCmsContentBuilder;


public class OpenCms5DatabaseReaderImpl extends NamedParameterJdbcDaoSupport implements OpenCmsDatabaseReader {

	
	@Override
	protected void initDao() throws Exception {
		// TODO Auto-generated method stub
		super.initDao();
	}
	
	public OpenCmsContentBuilder createBuilder(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<String> readAttached(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public byte[] readAttachedBytes(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Attached> readAttachedIds(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public InputStream readAttachedInputStream(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public String readContentBytes(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<String> readContentCategories(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<String[]> readContentProperties(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public XML readContentXml(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
