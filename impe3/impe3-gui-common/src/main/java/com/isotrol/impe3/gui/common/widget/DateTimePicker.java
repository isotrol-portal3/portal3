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

package com.isotrol.impe3.gui.common.widget;

import java.util.Date;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.component.IFieldPanel;

/**
 * A Component that provides with Date and Time selection capabilities.
 * 
 * @author Andrei Cojocaru
 *
 */
public class DateTimePicker extends ContentPanel implements IFieldPanel {
	
	/**
	 * Pattern used for the TimeField format
	 */
	private static final String PATTERN_TIME = "HH:mm";
	
	/**
	 * Date and Time format
	 */
	private DateTimeFormat dtFormat = null;
	
	/**
	 * Width of the widget, in pixels.
	 */
	protected static final int WIDGET_WIDTH = 200;
	
	/**
	 * Width in pixels for the text field used as time picker.
	 */
	private static final int TIME_PICKER_FIELD_WIDTH = 55;

	/**
	 * if <code>false</code>, will not display the time picker.
	 */
	private boolean showTimePicker = true;
	
	/**
	 * The date picker.
	 */
	private DatePicker datePicker = null;
	
	/**
	 * The time selector.
	 */
	private TimeField timePicker = null;
	
	/**
	 * Th originally set value
	 */
	private Date originalValue = null;
	
	/**
	 * GuiCommon module messages bundle.
	 */
	private GuiCommonMessages messages = null;
	
	/**
	 * GuiCommon module styles.
	 */
	private GuiCommonStyles styles = null;
	
	/**
	 * Constructor. Set the DateTimeFormat to use
	 */
	public DateTimePicker() {
		dtFormat = DateTimeFormat.getFormat(PATTERN_TIME);
	}
	
	/** 
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {
		initThis();
		initComponents();
		validate();
	}

	/**
	 * Validates the widget, and fires {@link Events#Valid} or {@link Events#Invalid}
	 */
	private void validate() {
		if (isValid()) {
			fireEvent(Events.Valid);
		} else {
			fireEvent(Events.Invalid);
		}
	}

	/**
	 * Inits this component properties
	 */
	private void initThis() {
		FormLayout layout = new FormLayout();
		layout.setDefaultWidth(TIME_PICKER_FIELD_WIDTH);
		setLayout(layout);
		setWidth(WIDGET_WIDTH);
		setHeaderVisible(false);
		setBodyBorder(false);
		setBodyStyle("padding: 10px");
	}

	/**
	 * Inits this container inner components
	 */
	private void initComponents() {
		addDatePicker();
		if (showTimePicker) {
			addTimePicker();
		}
		displayValues();
	}
	
	/**
	 * Adds the date picker. Always added
	 */
	private void addDatePicker() {
		datePicker = new DatePicker();
		datePicker.setValue(new Date());
		if (showTimePicker) {	// add 4px bottom separation
			datePicker.addStyleName(styles.gxtFormItem());
		}
		add(datePicker);
	}

	/**
	 * Adds the time picker. This method may never be called,
	 * if {@link #showTimePicker} is <code>false</code>
	 */
	private void addTimePicker() {
		timePicker = new TimeField();
		timePicker.setLazyRender(false);
		timePicker.setFieldLabel(messages.flPickTime());
		timePicker.setAllowBlank(false);
		timePicker.setPropertyEditor(new ListModelPropertyEditor<Time>() {
			public String getStringValue(Time value) {
				return dtFormat.format(value.getDate());
			}
			public Time convertStringValue(String value) {
				return new Time(dtFormat.parse(value));
			}
		});
		timePicker.setValidator(new Validator() {
			public String validate(Field<?> field, String value) {
				try {
					dtFormat.parse(value);
				} catch (IllegalArgumentException e) {
					return messages.vmInvalid();
				}
				return null;
			}
		});
		timePicker.setForceSelection(false);
		timePicker.setTriggerAction(TriggerAction.ALL);
		timePicker.setFireChangeEventOnSetValue(true);
		Listener<FieldEvent> lChange = new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				validate();
			}
		};
		timePicker.addListener(Events.Select, lChange);
		timePicker.addListener(Events.Change, lChange);
		timePicker.addListener(Events.KeyPress, lChange);
		timePicker.addListener(Events.Render, lChange);
		add(timePicker);
	}

	/**
	 * Displays the bound data value
	 */
	private void displayValues() {
		if (originalValue == null) {
			return;
		}
		
		displayValue(originalValue);
	}

	/**
	 * @param value displays the passed value in GUI.
	 */
	private void displayValue(Date value) {
		if (datePicker != null) {
			datePicker.setValue(value);
		}
		if (showTimePicker && timePicker != null) {
			timePicker.setValue(new Time(value));
		}
	}
	
	/**
	 * @param value
	 */
	public void setValue(Date value) {
		if (originalValue == null) {
			setOriginalValue(value);
		}
		displayValues();
	}
	
	/**
	 * @param originalValue
	 */
	public void setOriginalValue(Date originalValue) {
		this.originalValue = originalValue;
	}
	
	/**
	 * Injects the GuiCommon messages bundle.
	 * @param messages the messages bundle.
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}
	
	/**
	 * Injects the GuiCommon styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/** 
	 * (non-Javadoc)
	 * @see com.isotrol.merlin3.gui.common.util.component.IFieldPanel#isDirty()
	 */
	public boolean isDirty() {
		Date value = getValue();
		if (originalValue == null) {
			return value != null;
		}
		return !originalValue.equals(value);
	}

	/** 
	 * (non-Javadoc)
	 * @see com.isotrol.merlin3.gui.common.util.component.IFieldPanel#isValid()
	 */
	public boolean isValid() {
		if (!isRendered()) {
			return isDtoValid();
		}
		return isGuiValid();
	}
	
	/**
	 * @return
	 */
	private boolean isDtoValid() {
		return originalValue != null;
	}

	private boolean isGuiValid() {
		if (!showTimePicker) {
			return true;
		}
		return timePicker.isValid();
	}

	/**
	 * @param showTimePicker
	 */
	public void setShowTimePicker(boolean showTimePicker) {
		this.showTimePicker = showTimePicker;
	}
	
	/**
	 * @return the date value of the component
	 */
	public Date getValue() {
		Date date = datePicker.getValue();
		if (!showTimePicker) {
			return date;
		}
		
		Time time = timePicker.getValue();
		return new DateWrapper(date).clearTime()
				.addHours(time.getHour())
				.addMinutes(time.getMinutes())
				.asDate();
	}
}
