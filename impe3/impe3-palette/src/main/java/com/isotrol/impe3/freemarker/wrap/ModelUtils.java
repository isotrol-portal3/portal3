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

package com.isotrol.impe3.freemarker.wrap;


import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;

import java.net.URI;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateScalarModel;


/**
 * Utility methods for models..
 * @author Andres Rodriguez
 */
final class ModelUtils {
	/** Not instantiable. */
	private ModelUtils() {
		throw new AssertionError();
	}

	static String arg(List<String> args, int i) {
		if (args == null || args.size() <= i) {
			return null;
		}
		return args.get(i);
	}

	static Iterable<String> notNullArgs(List<String> args) {
		if (args == null) {
			return ImmutableList.of();
		}
		return filter(args, notNull());
	}

	static Multimap<String, String> buildURIQueryParameters(List<String> args, int index) {
		if (args == null || (args.size() - index) < 2) {
			return ImmutableMultimap.of();
		}
		final int n = args.size();
		final Multimap<String, String> map = LinkedListMultimap.create(n / 2);
		for (int i = index; (args.size() - i) >= 2; i += 2) {
			String p = args.get(i);
			String v = args.get(i + 1);
			if (p != null && v != null) {
				map.put(p, v);
			}
		}
		return map;
	}

	static TemplateModel uri(URI uri) {
		if (uri == null) {
			return TemplateScalarModel.EMPTY_STRING;
		}
		return new SimpleScalar(uri.toASCIIString());
	}
}
