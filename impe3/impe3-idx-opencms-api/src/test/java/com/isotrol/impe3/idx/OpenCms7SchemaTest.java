package com.isotrol.impe3.idx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.idx.oc7.api.OpenCms7Schema;

/**
 * Basic schema test
 * @author Emilio Escobar Reyero
 *
 */
public class OpenCms7SchemaTest {

	@Test
	public void prefixValuesTest() {
		assertEquals(OpenCms7Schema.NODEKEY, OpenCms7Schema.SCHEMA_NAME + "NODEKEY");
		assertEquals(OpenCms7Schema.PARENT, OpenCms7Schema.SCHEMA_NAME + "PARENT");
		assertEquals(OpenCms7Schema.PREDECESORS, OpenCms7Schema.SCHEMA_NAME + "PREDECESORS");
		assertEquals(OpenCms7Schema.ID, OpenCms7Schema.SCHEMA_NAME + "ID");
		assertEquals(OpenCms7Schema.TYPE, OpenCms7Schema.SCHEMA_NAME + "TYPE");
		assertEquals(OpenCms7Schema.CONTENT_TYPE, OpenCms7Schema.SCHEMA_NAME + "CONTENTTYPE");
		assertEquals(OpenCms7Schema.PATH, OpenCms7Schema.SCHEMA_NAME + "PATH");
		assertEquals(OpenCms7Schema.DATE_CREATED, OpenCms7Schema.SCHEMA_NAME + "DATECREATED");
		assertEquals(OpenCms7Schema.CHANNEL, OpenCms7Schema.SCHEMA_NAME + "CHANNEL");
		assertEquals(OpenCms7Schema.DATE_LAST_MODIFIED, OpenCms7Schema.SCHEMA_NAME + "DATELASTMODIFIED");
		assertEquals(OpenCms7Schema.DATE_RELEASED, OpenCms7Schema.SCHEMA_NAME + "DATERELEASED");
		assertEquals(OpenCms7Schema.DATE_EXPIRED, OpenCms7Schema.SCHEMA_NAME + "DATEEXPIRED");
		assertEquals(OpenCms7Schema.IN_NAV, OpenCms7Schema.SCHEMA_NAME + "BROWSEABLE");
	}
	
	@Test
	public void propertyValuesTest() {
		assertTrue(OpenCms7Schema.property("TEST").startsWith(OpenCms7Schema.PROPERTY_PREFIX));
	}
	
	@Test
	public void customValuesTest() {
		assertTrue(OpenCms7Schema.custom("TEST").startsWith(OpenCms7Schema.CUSTOM_PREFIX));
	}
	
	
}
