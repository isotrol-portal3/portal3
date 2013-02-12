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

package com.isotrol.impe3.pms.api.config;


import java.util.List;

import com.isotrol.impe3.api.StringItemStyle;
import com.isotrol.impe3.pms.api.AbstractDescribed;
import com.isotrol.impe3.pms.api.WithId;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;


/**
 * DTO for a configuration template.
 * @author Andres Rodriguez
 */
public final class ConfigurationTemplateItemDTO extends AbstractDescribed {
	/** Serial UID. */
	private static final long serialVersionUID = 1643971261271695166L;
	/** Configuration item key. */
	private String key;
	/** Configuration item type. */
	private ConfigurationItemType type;
	/** Required. */
	private boolean required = true;
	/** Bundle. */
	private boolean bundle = false;
	/** String style. */
	private StringItemStyle stringStyle;
	/** Choice items. */
	private List<ChoiceItemDTO> choices;
	/** String Value. */
	private String stringValue;
	/** Integer Value. */
	private Integer integerValue;
	/** Boolean Value. */
	private Boolean booleanValue;
	/** Category Value. */
	private CategorySelDTO categoryValue;
	/** Content type value. */
	private ContentTypeSelDTO contentTypeValue;
	/** Uploaded file value. */
	private UploadedFileDTO fileValue;

	/** Default constructor. */
	public ConfigurationTemplateItemDTO() {
	}

	/**
	 * Returns the configuration item key.
	 * @return The configuration item key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the configuration item key.
	 * @param key The configuration item key.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the configuration item type.
	 * @return The configuration item type.
	 */
	public ConfigurationItemType getType() {
		return type;
	}

	/**
	 * Sets the configuration item type. Setting the type to a value different of the current one causes the value to be
	 * set to {@code null}.
	 * @param type The configuration item type.
	 */
	public void setType(ConfigurationItemType type) {
		this.type = type;
	}

	/**
	 * Returns wheteher the item is required.
	 * @return If the item is required.
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Sets if the item is required.
	 * @param required If the item is required.
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Returns whether the file item is a bundle.
	 * @return True if the file item is a bundle.
	 */
	public boolean isBundle() {
		return bundle;
	}

	/**
	 * Sets whether the file item is a bundle.
	 * @param bundle True if the file item is a bundle.
	 */
	public void setBundle(boolean bundle) {
		this.bundle = bundle;
	}

	/**
	 * Returns the string style.
	 * @return The string style.
	 */
	public StringItemStyle getStringStyle() {
		return stringStyle;
	}

	/**
	 * Sets the string style.
	 * @param stringStyle The string style.
	 */
	public void setStringStyle(StringItemStyle stringStyle) {
		this.stringStyle = stringStyle;
	}

	private void type(ConfigurationItemType itemType) {
		if (type != itemType) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Returns the choice items.
	 * @return The choice items.
	 */
	public List<ChoiceItemDTO> getChoices() {
		return choices;
	}

	/**
	 * Sets the choice items.
	 * @param choices The choice items.
	 */
	public void setChoices(List<ChoiceItemDTO> choices) {
		this.choices = choices;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no STRING.
	 */
	public String getString() {
		type(ConfigurationItemType.STRING);
		return stringValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no STRING.
	 */
	public void setString(String value) {
		type(ConfigurationItemType.STRING);
		this.stringValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no CHOICE.
	 */
	public String getChoice() {
		type(ConfigurationItemType.CHOICE);
		return stringValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no CHOICE.
	 */
	public void setChoice(String value) {
		type(ConfigurationItemType.CHOICE);
		this.stringValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no INTEGER.
	 */
	public Integer getInteger() {
		type(ConfigurationItemType.INTEGER);
		return integerValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no INTEGER.
	 */
	public void setInteger(Integer value) {
		type(ConfigurationItemType.INTEGER);
		this.integerValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no BOOLEAN.
	 */
	public Boolean getBoolean() {
		type(ConfigurationItemType.BOOLEAN);
		return booleanValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no BOOLEAN.
	 */
	public void setBoolean(Boolean value) {
		type(ConfigurationItemType.BOOLEAN);
		this.booleanValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no CONTENT_TYPE.
	 */
	public ContentTypeSelDTO getContentType() {
		type(ConfigurationItemType.CONTENT_TYPE);
		return contentTypeValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no CONTENT_TYPE.
	 */
	public void setContentType(ContentTypeSelDTO value) {
		type(ConfigurationItemType.CONTENT_TYPE);
		this.contentTypeValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no CATEGORY.
	 */
	public CategorySelDTO getCategory() {
		type(ConfigurationItemType.CATEGORY);
		return categoryValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no CATEGORY.
	 */
	public void setCategory(CategorySelDTO value) {
		type(ConfigurationItemType.CATEGORY);
		this.categoryValue = value;
	}

	/**
	 * Returns the current value.
	 * @return The current value.
	 * @throws IllegalStateException if the type is no FILE.
	 */
	public UploadedFileDTO getFile() {
		type(ConfigurationItemType.FILE);
		return fileValue;
	}

	/**
	 * Sets the current value.
	 * @param value The current value.
	 * @throws IllegalStateException if the type is no FILE.
	 */
	public void setFile(UploadedFileDTO value) {
		type(ConfigurationItemType.FILE);
		this.fileValue = value;
	}

	private String get(WithId id) {
		if (id == null) {
			return null;
		}
		return id.getId();
	}

	/**
	 * Returns a configuration item with the data from the current template.
	 * @return A configuration item.
	 */
	public ConfigurationItemDTO toConfigurationItemDTO() {
		final ConfigurationItemDTO dto = new ConfigurationItemDTO();
		dto.setKey(key);
		final String value;
		switch (type) {
			case CONTENT_TYPE:
				value = get(contentTypeValue);
				break;
			case CATEGORY:
				value = get(categoryValue);
				break;
			case FILE:
				value = get(fileValue);
				break;
			case STRING:
				value = stringValue;
				break;
			case CHOICE:
				value = stringValue;
				break;
			case INTEGER:
				value = (integerValue == null) ? null : integerValue.toString();
				break;
			case BOOLEAN:
				value = (booleanValue == null) ? null : booleanValue.toString();
				break;
			default:
				value = null;
		}
		dto.setCurrentValue(value);
		return dto;
	}

}
