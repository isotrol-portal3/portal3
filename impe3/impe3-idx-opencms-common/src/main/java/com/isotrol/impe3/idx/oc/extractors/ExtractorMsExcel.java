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


import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * Se utiliza para extraer el texto crudo de un fichero excel.
 * @author Juan Manuel Valverde Ramírez
 */
public final class ExtractorMsExcel {

	/**
	 * Constructor privado.
	 */
	private ExtractorMsExcel() {
		//
	}

	/**
	 * Extrae el texto de un fichero excel.
	 * @param in
	 * @return String. Devuelve el texto crudo
	 * @throws Exception
	 */
	public static String extractText(InputStream in) throws Exception {

		String result = "";

		HSSFWorkbook wb = new HSSFWorkbook(in);

		ExcelExtractor ee = new ExcelExtractor(wb);
		result = ee.getText();

		// Eliminamos los caracteres que no nos sirven para indexar.
		result = ExtractorUtil.removeControlChars(result);

		return result;
	}

	/**
	 * Extracts the text from the Excel table content.<p>
	 * 
	 * @param in the document input stream
	 * @return the extracted text
	 * @throws IOException if something goes wring
	 * @deprecated
	 */
	protected String extractTableContent(InputStream in) throws IOException {

		HSSFWorkbook excelWb = new HSSFWorkbook(in);
		StringBuffer result = new StringBuffer(4096);

		int numberOfSheets = excelWb.getNumberOfSheets();

		for (int i = 0; i < numberOfSheets; i++) {
			HSSFSheet sheet = excelWb.getSheetAt(i);
			int numberOfRows = sheet.getPhysicalNumberOfRows();
			if (numberOfRows > 0) {

				if (excelWb.getSheetName(i) != null && !excelWb.getSheetName(i).trim().equals("")) {
					// append sheet name to content
					if (i > 0) {
						result.append("\n\n");
					}
					result.append(excelWb.getSheetName(i).trim());
					result.append(":\n\n");
				}

				Iterator rowIt = sheet.rowIterator();
				while (rowIt.hasNext()) {
					HSSFRow row = (HSSFRow) rowIt.next();
					if (row != null) {
						boolean hasContent = false;
						Iterator it = row.cellIterator();
						while (it.hasNext()) {
							HSSFCell cell = (HSSFCell) it.next();
							String text = null;
							try {
								switch (cell.getCellType()) {
									case HSSFCell.CELL_TYPE_BLANK:
									case HSSFCell.CELL_TYPE_ERROR:
										// ignore all blank or error cells
										break;
									case HSSFCell.CELL_TYPE_NUMERIC:
										text = Double.toString(cell.getNumericCellValue());
										break;
									case HSSFCell.CELL_TYPE_BOOLEAN:
										text = Boolean.toString(cell.getBooleanCellValue());
										break;
									case HSSFCell.CELL_TYPE_STRING:
									default:
										text = cell.getStringCellValue();
										break;
								}
							} catch (Exception e) {
								// ignore this cell
							}
							if ((text != null) && (text.length() != 0)) {
								result.append(text.trim());
								result.append(' ');
								hasContent = true;
							}
						}
						if (hasContent) {
							// append a newline at the end of each row that has content
							result.append('\n');
						}
					}
				}
			}
		}

		return result.toString();
	}

}
