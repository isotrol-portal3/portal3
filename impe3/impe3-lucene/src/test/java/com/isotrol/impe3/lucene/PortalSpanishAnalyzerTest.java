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

package com.isotrol.impe3.lucene;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;


public class PortalSpanishAnalyzerTest {
	private static final String[] WORDS = {"extranjero", "extranjeros", "camion", "cami\u00f3n", "tramitacion",
		"tramitaci\u00f3n", "expedicion", "expedici\u00f3n", "voluntario", "voluntariado", "universitarios"};

	private final Analyzer a1 = new PortalSpanishAnalyzer();

	private void test(String name, Analyzer a, String text) throws IOException {
		final Reader r = new StringReader(text);
		final TokenStream s = a.tokenStream(null, r);
		List<String> list = Lists.newLinkedList();
		s.reset();
		while (s.incrementToken()) {
			if (s.hasAttribute(CharTermAttribute.class)) {
				list.add(s.getAttribute(CharTermAttribute.class).toString());
			}
		}
		System.out.printf("[%s] %s => %s\n", name, text, list);
	}

	private void test(String name, Analyzer a) throws IOException {
		for (String w : WORDS) {
			test(name, a, w);
		}
	}

	@Test
	public void noSym() throws Exception {
		test("noSym", a1);
	}

	@Test
	public void sym() throws Exception {
		PortalSpanishAnalyzer a = new PortalSpanishAnalyzer();
		Map<String, String> map = ImmutableMap.<String, String> builder().put("voluntariad", "voluntari").build();
		a.setPostSynonyms(map);
		a.afterPropertiesSet();
		test("Sym", a);
	}

	@Test
	public void loadedSyms() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/isotrol/impe3/lucene/ctx.xml");
		Analyzer a = ctx.getBean("analyzer", Analyzer.class);
		test("loadedSyms", a);
	}

}
