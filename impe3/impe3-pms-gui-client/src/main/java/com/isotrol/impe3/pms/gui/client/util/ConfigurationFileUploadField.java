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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.util;

import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;

/**
 * @author Andrei Cojocaru
 *
 */
public class ConfigurationFileUploadField extends FileUploadField {
	
	/**
	 * Original currentFile<br/>
	 */
	private UploadedFileDTO originalFile = null;
	
	/**
	 * Current file value<br/>
	 */
	private UploadedFileDTO currentFile = null;
	
	/**
	 * Default constructor.
	 */
	public ConfigurationFileUploadField() {}
	
	/**
	 * Sets the passed file DTO as the current and original file.<br/>
	 * @param originalFile
	 */
	public void setOriginalFile(UploadedFileDTO originalFile) {
		this.originalFile = originalFile;
		
		String fileName = null;
		if (currentFile != null) {
			fileName = currentFile.getName();
		}
		updateOriginalValue(fileName);
		
		setCurrentFile(originalFile);
	}
	
	/**
	 * Sets the current file value.<br/>
	 * @param currentFile
	 */
	public void setCurrentFile(UploadedFileDTO currentFile) {
		this.currentFile = currentFile;
		
		String fileName = null;
		if (currentFile != null) {
			fileName = currentFile.getName();
		}
		setValue(fileName);
	}
	
	/**
	 * Returns the current bound value.<br/>
	 * @return the current bound value.
	 */
	public UploadedFileDTO getCurrentFile() {
		return currentFile;
	}
	
	/* (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.form.Field#isDirty()
	 */
	/**
	 * This class of Field is dirty if the {@link #originalFile original file} and the 
	 * {@link #currentFile current file} represent the same uploaded file.<br/>
	 */
	@Override
	public boolean isDirty() {
		if (originalFile == null) {
			return currentFile != null;
		}
		
		return currentFile == null || !originalFile.getId().equals(currentFile.getId());
	}
}
