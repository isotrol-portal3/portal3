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

import org.apache.lucene.store.Directory;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.sf.lucis.core.DirectoryProvider;

/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class SearcherFactoryTest {

	
	private DirectoryProvider provider() {
		DirectoryProvider provider = mock(DirectoryProvider.class);
		Directory dir = mock(Directory.class);
		
		when(provider.getDirectory()).thenReturn(dir);
		
		return provider;
	}

	@Test
	public void singleTest() {
		SearcherFactory factory = new SearcherFactory(provider());
		
		try {
		Object o = factory.getObject();
		assertNotNull(o);
		} catch (Exception e) {
			Assert.fail();
		}
		
	}
	
	@Test
	public void multiTest() {
		SearcherFactory factory = new SearcherFactory(Lists.newArrayList(provider(), provider()));
		
		try {
			Object o = factory.getObject();
			assertNotNull(o);
			} catch (Exception e) {
				Assert.fail();
			}
	}
	
	
}
