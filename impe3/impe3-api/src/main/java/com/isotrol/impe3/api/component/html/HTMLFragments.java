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

package com.isotrol.impe3.api.component.html;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;


/**
 * Some HTML fragments.
 * @author Andres Rodriguez
 */
public final class HTMLFragments {
	/** Not instantiable. */
	private HTMLFragments() {
		throw new AssertionError();
	}

	private static final HTMLFragment EMPTY = new HTMLFragment() {
		public void writeTo(OutputStream output, Charset charset) throws IOException {
		}
	};

	/**
	 * Returns an empty HTML fragment.
	 * @return An empty HTML fragment.
	 */
	public static HTMLFragment empty() {
		return EMPTY;
	}

	/**
	 * Returns an string-backed HTML fragment.
	 * @param fragment String to wrap.
	 * @return The requested fragment.
	 */
	public static HTMLFragment of(final String fragment) {
		if (fragment == null || fragment.length() == 0) {
			return EMPTY;
		}
		return new AbstractWriterHTMLFragment() {
			public void writeTo(OutputStreamWriter writer) throws IOException {
				writer.write(fragment);
			}
		};
	}

	/**
	 * Returns an HTML fragment that prints a throwable stack trace.
	 * @param t Throwable to write.
	 * @return The requested fragment.
	 */
	public static HTMLFragment stackTrace(final Throwable t) {
		if (t == null) {
			return EMPTY;
		}
		final HTMLFragment f = new AbstractWriterHTMLFragment() {
			public void writeTo(OutputStreamWriter writer) throws IOException {
				final PrintWriter w = new PrintWriter(writer, true);
				t.printStackTrace(w);
				w.flush();
			}
		};
		return f;
	}

	/**
	 * Returns a HTML fragment from an object.
	 * @param object Object to wrap.
	 * @return The requested fragment.
	 */
	public static HTMLFragment object(Object object) {
		if (object == null) {
			return EMPTY;
		}
		if (object instanceof HTMLFragment) {
			return (HTMLFragment) object;
		}
		if (object instanceof Throwable) {
			return stackTrace((Throwable) object);
		}
		return of(object.toString());
	}
}
