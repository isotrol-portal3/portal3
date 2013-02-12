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

package com.isotrol.impe3.pms.gui.client.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;

/**
 * Helper class that manages a collection of Fields, their current values and the
 * global <code>dirty</code> state.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class FieldsContainerSupport {
	/**
	 * Maps fields values on fields.<br/>
	 */
	private Map<Field<?>, Object> fieldsMap = null;
	
	/**
	 * We cannot rely on {@link FileUploadField#getOriginalValue()}, so 
	 * original values are managed here.<br/>
	 */
	private Map<FileUploadField, UploadedFileDTO> originalFilesValues = null;

	/**
	 * Constructor provided with fields collection.<br/>
	 * 
	 * @param fields
	 */
	public FieldsContainerSupport() {
		fieldsMap = new HashMap<Field<?>, Object>();
		originalFilesValues = new HashMap<FileUploadField, UploadedFileDTO>();
	}

	/**
	 * Adds a field to the managed fields collection. Does not override existing
	 * fields.<br/>
	 * 
	 * @param field
	 */
	public void addField(Field<?> field) {
		if (!fieldsMap.containsKey(field)) {
			fieldsMap.put(field, null);
		}
	}

	/**
	 * Retrieves a fields current value.<br/>
	 * 
	 * @param field the field
	 * @return the field current value.
	 */
	public Object getFieldValue(Field<?> field) {
		Object value = null;
		if (fieldsMap.containsKey(field)) {
			value = fieldsMap.get(field);
		}
		if (value == null) {
			value = field.getValue();
		}
		return value;
	}
	
	/**
	 * Returns the UploadedFileDTO bound to a file upload field.<br/>
	 * @param field
	 * @return
	 */
	public UploadedFileDTO getFieldValue(FileUploadField field) {
		if (fieldsMap.containsKey(field)) {
			return (UploadedFileDTO) fieldsMap.get(field);
		}
		return null;
	}

	/**
	 * Sets the passed value for the passed field.<br/>
	 * 
	 * @param <M>
	 * @param field
	 * @param value
	 */
	public <M> void setFieldValue(Field<M> field, M value) {
		field.setValue(value);
		fieldsMap.put(field, value);
	}

	/**
	 * Associates the passed file with the passed FileUploadField.<br/>
	 * If field does not exist, it will be added to managed fields map. 
	 * @param field
	 * @param file
	 */
	public void setFieldValue(FileUploadField field, UploadedFileDTO file) {
		String fileName = null;
		if (file != null) {
			fileName = file.getName();
		}
		field.setValue(fileName);
		
		fieldsMap.put(field, file);
	}
	
	/**
	 * Sets the original file DTO for the passed field.<br/>
	 * @param field
	 * @param file
	 */
	public void setOriginalFile(FileUploadField field, UploadedFileDTO file) {
		String fileName = null;
		if (file != null) {
			fileName = file.getName();
		}
		field.updateOriginalValue(fileName);
		originalFilesValues.put(field, file);
	}
	
	private UploadedFileDTO getOriginalFile(FileUploadField field) {
		if (originalFilesValues.containsKey(field)) {
			return originalFilesValues.get(field);
		}
		return null;
	}
	
	/**
	 * Returns <code>false</code> only if {@link Field#isDirty()} returns
	 * <code>false</code> for every managed field.<br/>
	 * 
	 * @return <code>false</code> only if {@link Field#isDirty()} returns
	 *         <code>false</code> for every managed field;<code>true</code>,
	 *         otherwise.
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Field<?>> it = fieldsMap.keySet().iterator();
		while (it.hasNext() && !dirty) {
			dirty = isFieldDirty(it.next());
		}
		return dirty;
	}

	private boolean isFieldDirty(Field<?> field) {
		// particular case FileUploadField
		if (field instanceof FileUploadField) {
			return isFileUploadDirty((FileUploadField) field);
		}
		// other cases:
		Object oValue = null;
		oValue = field.getOriginalValue();
		Object value = getFieldValue(field);
		if (oValue == null) {
			return value != null;
		}
		return !oValue.equals(value);
	}

	/**
	 * Checks if the managed original file is different from the current file.<br/>
	 * @param field
	 * @return
	 */
	private boolean isFileUploadDirty(FileUploadField field) {
		UploadedFileDTO oFile = getOriginalFile(field);
		UploadedFileDTO file = getFieldValue(field);
		if (oFile == null) {
			return file != null;
		}
		return file == null || !oFile.getId().equals(file.getId());
	}
	
}
