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
package com.isotrol.impe3.idx.oc.extractors;


import java.io.InputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;


/**
 * Se utiliza para extraer el texto crudo de un fichero word.
 * @author Juan Manuel Valverde Ramírez
 */
public final class ExtractorMsWord {

	/**
	 * Constructor privado.
	 */
	private ExtractorMsWord() {
		//
	}

	/**
	 * Extrae el texto de un fichero word.
	 * @param in
	 * @return String. Devuelve el texto crudo
	 * @throws Exception
	 */
	public static String extractText(InputStream in) throws Exception {

		String result = "";

		HWPFDocument doc = new HWPFDocument(in);

		WordExtractor we = new WordExtractor(doc);
		result = we.getText();

		// Eliminamos los caracteres que no nos sirven para indexar.
		result = ExtractorUtil.removeControlChars(result);

		return result;
	}

}
