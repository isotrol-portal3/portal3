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
