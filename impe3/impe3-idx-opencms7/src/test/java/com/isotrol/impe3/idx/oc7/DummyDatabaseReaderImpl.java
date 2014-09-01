package com.isotrol.impe3.idx.oc7;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.core.io.Resource;

import com.isotrol.impe3.idx.XML;
import com.isotrol.impe3.idx.oc.Attached;
import com.isotrol.impe3.idx.oc.OpenCmsContent;
import com.isotrol.impe3.idx.oc.OpenCmsContent.OpenCmsContentBuilder;


public class DummyDatabaseReaderImpl implements OpenCms7DatabaseReader {

	final Resource file;

	public DummyDatabaseReaderImpl(Resource file) {

		this.file = file;

	}

	public OpenCmsContentBuilder createBuilder(String id) {
		OpenCmsContentBuilder builder = OpenCmsContent.builder();

		
		if (id != null && id.startsWith("CANAL")){
			builder.setId(id);
			builder.setType(0);
			builder.setState(1);
			builder.setDateCreated(new Date());
			builder.setDateLastModified(new Date());
			builder.setPath("/");
			builder.setDateReleased(new Date());
			builder.setDateExpired(new Date());
		} else {
			builder.setId(id);
			builder.setType(30);
			builder.setState(1);
			builder.setDateCreated(new Date());
			builder.setDateLastModified(new Date());
			builder.setPath("/");
			builder.setDateReleased(new Date());
			builder.setDateExpired(new Date());
			// builder.setAttached(new LinkedList<String>());
		}

		return builder;
	}

	public String readContentBytes(String id) {
		return readContentXml(id).getXML().toXML();
	}

	public List<String> readContentCategories(String id) {
		return Collections.EMPTY_LIST;
	}

	public List<String[]> readContentProperties(String id) {
		return Arrays.asList(new String[] {"key1", "value1"}, new String[] {"key2", "value2"});
	}

	public XML readContentXml(String id) {

		final XML xml;

		try {
			xml = XML.of(file.getInputStream());
		} catch (IOException e) {
			return null;
		}

		return xml;
	}

	public List<String> readAttached(String id) {
		return Collections.EMPTY_LIST;
	}

	public List<Attached> readAttachedIds(String id) {
		return Collections.EMPTY_LIST;
	}

	public byte[] readAttachedBytes(String id) {
		return null;
	}

	public InputStream readAttachedInputStream(String id) {
		return null;
	}

}
