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
package com.isotrol.impe3.idx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.idx.d6.api.Drupal6Schema;


/**
 * Basic schema test
 * @author Emilio Escobar Reyero
 *
 */
public class Drupal6SchemaTest {
	@Test
	public void prefixValuesTest() {
		assertEquals(Drupal6Schema.NODEKEY, Drupal6Schema.SCHEMA_NAME + "NODEKEY");
		assertEquals(Drupal6Schema.TRANSLATIONKEY, Drupal6Schema.SCHEMA_NAME + "TRANSLATIONKEY");
		assertEquals(Drupal6Schema.ID, Drupal6Schema.SCHEMA_NAME + "ID");
		assertEquals(Drupal6Schema.TYPE, Drupal6Schema.SCHEMA_NAME + "TYPE");
		assertEquals(Drupal6Schema.CONTENT_TYPE, Drupal6Schema.SCHEMA_NAME + "CONTENTTYPE");
		assertEquals(Drupal6Schema.CATEGORY, Drupal6Schema.SCHEMA_NAME + "CATEGORY");
		assertEquals(Drupal6Schema.DATE_CREATED, Drupal6Schema.SCHEMA_NAME + "DATECREATED");
		
		assertEquals(Drupal6Schema.DATE_LAST_MODIFIED, Drupal6Schema.SCHEMA_NAME + "DATELASTMODIFIED");
	}
	
	@Test
	public void propertyValuesTest() { 
		assertTrue(Drupal6Schema.property("TEST").startsWith(Drupal6Schema.PROPERTY_PREFIX));
	}
	
	@Test
	public void customValuesTest() {
		assertTrue(Drupal6Schema.custom("TEST").startsWith(Drupal6Schema.CUSTOM_PREFIX));
	}

}
