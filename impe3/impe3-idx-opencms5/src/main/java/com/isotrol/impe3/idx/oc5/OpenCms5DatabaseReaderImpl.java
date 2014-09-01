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
