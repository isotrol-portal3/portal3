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

package com.isotrol.impe3.pms.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * Value that represents a configuration item.
 * @author Andres Rodriguez
 */
@Embeddable
public class ConfigurationValue {
	/** String value. */
	@Column(name = "CV_STRING", length = Lengths.DESCRIPTION)
	private String stringValue;
	/** Integer value. */
	@Column(name = "CV_INTEGER")
	private Integer integerValue;
	/** Integer value. */
	@Column(name = "CV_BOOLEAN")
	private Boolean booleanValue;
	/** File value. */
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "CV_FLDT_ID", nullable = true)
	private FileEntity fileValue;
	/** Content type value. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CV_COTP_ID", nullable = true)
	private ContentTypeEntity contentTypeValue;
	/** Category value. */
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "CV_CTGY_ID", nullable = true)
	private CategoryEntity categoryValue;

	/** Default constructor. */
	public ConfigurationValue() {
	}

	/**
	 * Duplicates a configuration value.
	 * @return A copy of this value.
	 */
	public ConfigurationValue duplicate() {
		final ConfigurationValue v = new ConfigurationValue();
		v.stringValue = getStringValue();
		v.integerValue = getIntegerValue();
		v.fileValue = getFileValue();
		v.contentTypeValue = getContentTypeValue();
		v.categoryValue = getCategoryValue();
		v.booleanValue = getBooleanValue();
		return v;
	}

	/**
	 * Returns the string value.
	 * @return The string value.
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Sets the string value.
	 * @param stringValue The string value.
	 */
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	/**
	 * Returns the integer value.
	 * @return The integer value.
	 */
	public Integer getIntegerValue() {
		return integerValue;
	}

	/**
	 * Sets the integer value.
	 * @param integerValue The integer value.
	 */
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	/**
	 * Returns the boolean value.
	 * @return The boolean value.
	 */
	public Boolean getBooleanValue() {
		return booleanValue;
	}

	/**
	 * Sets the boolean value.
	 * @param booleanValue The boolean value.
	 */
	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	/**
	 * Returns the file value.
	 * @return The file value.
	 */
	public FileEntity getFileValue() {
		return fileValue;
	}

	/**
	 * Sets the file value.
	 * @param fileValue The file value.
	 */
	public void setFileValue(FileEntity fileValue) {
		this.fileValue = fileValue;
	}

	/**
	 * Returns the content type value.
	 * @return The content type value.
	 */
	public ContentTypeEntity getContentTypeValue() {
		return contentTypeValue;
	}

	/**
	 * Sets the content type value.
	 * @param contentTypeValue The content type value.
	 */
	public void setContentTypeValue(ContentTypeEntity contentTypeValue) {
		this.contentTypeValue = contentTypeValue;
	}

	/**
	 * Returns the category value.
	 * @return The category value.
	 */
	public CategoryEntity getCategoryValue() {
		return categoryValue;
	}

	/**
	 * Sets the category value.
	 * @param categoryValue The category value.
	 */
	public void setCategoryValue(CategoryEntity categoryValue) {
		this.categoryValue = categoryValue;
	}
}
