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

package com.isotrol.impe3.core.support;


import java.net.URI;
import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import org.junit.Test;

import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.ContentTypes;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.core.impl.CategoriesFactory;
import com.isotrol.impe3.core.impl.ContentTypesFactory;


/**
 * Tests for AbstractURIGenerator.
 * @author Andres Rodriguez.
 */
public class URIGeneratorTest {
	@Test
	public void test() throws Exception {
		final URI base = URI.create("http://test.example.com");
		final ContentTypes cts = ContentTypesFactory.of();
		final Categories cgs = CategoriesFactory.of();
		final Device d = new Device(UUID.randomUUID(), DeviceType.HTML, "Test Device", "", 980, null, null);
		final Portal p = Portal.builder().setId(UUID.randomUUID()).setDevice(d).setName("Test Portal")
			.setContentTypes(cts).setCategories(cgs).get();
		final URIGenerator g = new AbstractURIGenerator(p, base, null) {
		};
		System.out.println(g.getURI(FileId.of(UUID.randomUUID(), "test.txt")));
	}

	private void test(String s) {
		try {
			URI u = URI.create(s);
			System.out.println(String.format("%s ** %s", u.toASCIIString(), u.normalize().toASCIIString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test2() throws Exception {
		test("hola");
		test("/hola");
		test("/hola/");
	}

	@Test
	public void builder() throws Exception {
		final URI base = URI.create("http://test.example.com");
		System.out.println(UriBuilder.fromUri(base).path("hello/world").build().toASCIIString());
		System.out.println(UriBuilder.fromUri(base).path("/hello/world").build().toASCIIString());
	}

	@Test
	public void empty() throws Exception {
		// final URI base = new URI("");
		final UriBuilder b = UriBuilder.fromUri("");
		System.out.println(b.build().toASCIIString());
		b.queryParam("size", 3);
		System.out.println(b.build().toASCIIString());
	}

	@Test
	public void abs() throws Exception {
		final URI base = new URI("http://example.com/context");
		final UriBuilder b = UriBuilder.fromUri(base);
		final URI abs = new URI("/path1/path2/path3?a=1&b=2");
		b.path(abs.getPath()).replaceQuery(abs.getQuery());
		System.out.println(b.build().toASCIIString());
		b.queryParam("size", 3);
		System.out.println(b.build().toASCIIString());
	}

	@Test
	public void queryNum() throws Exception {
		final URI base = new URI("http://example.com");
		final UriBuilder b = UriBuilder.fromUri(base);
		b.queryParam("3", "3");
		System.out.println(b.build().toASCIIString());
	}

	@Test
	public void queryAbs() throws Exception {
		final URI base = new URI("/root");
		System.out.println(UriBuilder.fromUri(base).path("hello/world").build().toASCIIString());
		System.out.println(UriBuilder.fromUri(base).path("/hello/world").build().toASCIIString());
	}

	private void slash(String uri) throws Exception {
		final URI u = new URI(uri);
		System.out.printf("URI: %s -- Path: %s -- Split: %s\n", u.toASCIIString(), u.getPath(),
			Arrays.asList(u.getPath().split("/")));
	}

	@Test
	public void slash() throws Exception {
		slash("http://www.example.com");
		slash("http://www.example.com/");
		slash("http://www.example.com/s");
		slash("http://www.example.com/s/");
		slash("http://www.example.com/s/?a=b");
	}

	@Test
	public void trailing() throws Exception {
		final URI base = new URI("http://example.com/context/");
		final UriBuilder b = UriBuilder.fromUri(base);
		System.out.println(b.build().toASCIIString());
		b.path("p");
		System.out.println(b.build().toASCIIString());
		b.path("");
		System.out.println(b.build().toASCIIString());
		b.segment("p");
		System.out.println(b.build().toASCIIString());
		b.segment("/");
		System.out.println(b.build().toASCIIString());
		b.path("h/");
		System.out.println(b.build().toASCIIString());
	}

	@Test
	public void pathSegment() throws Exception {
		final URI base = new URI("http://example.com/context/");
		final UriBuilder b = UriBuilder.fromUri(base);
		System.out.println(b.build().toASCIIString());
		b.segment("/noticia/level2/level.xml");
		System.out.println(b.build().toASCIIString());
	}
	
}
