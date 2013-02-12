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


import static com.google.common.base.Preconditions.checkNotNull;
import static net.sf.derquinsej.io.Streams.consume;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Utility class to uploaded file compression.
 * @author Andres Rodriguez
 */
public final class FileCompression {
	/** Not instantiable. */
	private FileCompression() {
		throw new AssertionError();
	}

	public static byte[] compress(byte[] original) {
		try {
			original = checkNotNull(original);
			final InputStream is = new ByteArrayInputStream(original);
			final ByteArrayOutputStream bos = new ByteArrayOutputStream();
			final GZIPOutputStream gos = new GZIPOutputStream(bos);
			consume(is, gos, true);
			gos.close();
			return bos.toByteArray();
		} catch (IOException e) {
			return original;
		}
	}

	public static byte[] decompress(byte[] original) {
		try {
			original = checkNotNull(original);
			final InputStream is = new GZIPInputStream(new ByteArrayInputStream(original));
			return consume(is, true);
		} catch (IOException e) {
			return original;
		}
	}

}
