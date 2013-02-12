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

package com.isotrol.impe3.palette.excel.poi;


import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.MediaType;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.excel.ExcelRenderer;
import com.isotrol.impe3.palette.binary.AbstractBinaryRenderer;


/**
 * Base class for excel renderers based on Apache POI. This class IS NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class POIExcelRenderer extends AbstractBinaryRenderer implements ExcelRenderer {
	/** Whether to use Office Open XML format. */
	private final boolean xlsx;

	protected POIExcelRenderer(RenderContext context, POIExcelModuleConfig moduleConfig,
		POIExcelComponentConfig componentConfig) {
		super(context, moduleConfig, componentConfig);
		this.xlsx = DeviceType.XLSX == context.getDevice().getType();
	}

	public MediaType getMediaType() {
		return xlsx ? MIME_XLSX : MIME_XLS;
	}

	/**
	 * Performs actual writing.
	 * @param os Output stream.
	 * @throws IOException If an error occurs.
	 */
	protected final void doWrite(OutputStream os) throws IOException {
		// Create workbook
		final Workbook wb;
		if (xlsx) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		// Write workbook
		write(wb);
		wb.write(os);
	}

	/**
	 * Writes the result in the provided workbook.
	 * @param wb destination workbook.
	 */
	protected abstract void write(Workbook wb);

}
