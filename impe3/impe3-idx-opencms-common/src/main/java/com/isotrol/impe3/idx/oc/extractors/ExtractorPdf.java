package com.isotrol.impe3.idx.oc.extractors;


import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;


/**
 * Esta clase extrae la información de un pdf y la convierte a texto crudo separado por espacios. Utiliza la librería
 * PdfBox.
 * @author Juan Manuel Valverde Ramírez
 */
public final class ExtractorPdf {

	static final Logger logger = LoggerFactory.getLogger(ExtractorPdf.class);

	/**
	 * Constructor privado
	 */
	private ExtractorPdf() {
		//
	}

	/**
	 * Extrae la información de un pdf y la convierte a texto crudo.
	 * @param inputStream InputStream
	 * @return String
	 */
	public static String extract(InputStream inputStream) {
		String text = "";

		// Crear un PDDocument a partir del Attach
		PDDocument document = null;
		try {
			document = PDDocument.load(inputStream);
		} catch (IOException ioe) {
			logger.warn("Error: No pudo cargarse el PDF.");
			logger.debug("Error: No pudo cargarse el PDF.", ioe);
			return text;
		}

		if (document == null) {
			logger.warn("PDDocument is null");
			return text;
		}

		PDFTextStripper stripper;
		try {
			// Crear el Stripper de Texto
			stripper = new PDFTextStripper();
			// Setear parámetros del PDFTextStripper
			stripper.setSortByPosition(false);
			stripper.setStartPage(1);
			stripper.setEndPage(Integer.MAX_VALUE);
			// Transformar contenido del PDFTextStripper a una String
			text = stripper.getText(document);
		} catch (IOException ioe) {
			logger.warn("Error al extraer el texto del PDF");
			logger.debug("Error al extraer el texto del PDF", ioe);

		} catch (Exception e) {
			logger.warn("Error al extraer el texto del PDF");
			logger.debug("Error trace", e);
		}
		finally {
			// Cerrar el documento PDF
			if (document != null) {
				try {
					document.close();
				} catch (IOException ioe) {}
			}
			if (inputStream != null) {
				try {
					// Liberar los recursos utilizados por el InputStream
					inputStream.close();
				} catch (java.io.IOException ioe) {}
			}
		}

		// Eliminamos los caracteres que no nos sirven para indexar.
		text = ExtractorUtil.removeControlChars(text);

		return text;
	}
}
