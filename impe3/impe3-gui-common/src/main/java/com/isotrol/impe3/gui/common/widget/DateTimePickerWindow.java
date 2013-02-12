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

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;


/**
 * @author Manuel Ruiz
 * 
 */
public class DateTimePickerWindow extends HorizontalPanel {

	private static final String DATE_TIME_PICKER_TEXT = "Seleccionar fecha";
	/**
	 * ItemID for button "Add date", in the {@link #dateTimePicker}<br/>
	 */
	private static final String ID_BUTTON_ADD = "add-date";

	private GuiCommonStyles styles = null;
	private Buttons buttons = null;

	private TextField<String> fDate = null;
	private DateTimePicker dtPicker = null;
	private DateTimeFormat dateTimeFormat = null;
	private Date date = null;

	@Override
	protected void beforeRender() {
		initThis();
		initComponents();
	}

	private void initThis() {
	}

	private void initComponents() {
		dateTimeFormat = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");
		fDate = new TextField<String>();
		if (date != null) {
			fDate.setValue(dateTimeFormat.format(date));
		}
		fDate.setEmptyText("dd/mm/aaaa hh:mm");
		fDate.addStyleName(styles.marginLeft17px());
		add(fDate);
		// Calendar icon button:
		SelectionListener<IconButtonEvent> lCalendar = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				showDatePicker();
			}
		};
		final IconButton bCalendar = buttons.createGenericIconButton(DATE_TIME_PICKER_TEXT, styles.iCalendar(),
			lCalendar);
		add(bCalendar);
	}

	private void showDatePicker() {

		final TypicalWindow wCalendar = new TypicalWindow();
		wCalendar.setHeading(DATE_TIME_PICKER_TEXT);
		wCalendar.setBodyStyle(styles.padding10px());
		if (GXT.isChrome) {
			wCalendar.setWidth(DateTimePicker.WIDGET_WIDTH + 10);
		} else {
			wCalendar.setAutoWidth(true);
		}
		wCalendar.setAutoHeight(true);
		dtPicker.setShowTimePicker(true);
		wCalendar.add(dtPicker);

		final Button bAdd = buttons.createAcceptButton(null);
		bAdd.setItemId(ID_BUTTON_ADD);
		wCalendar.addButton(bAdd);
		bAdd.disable();
		Listener<BaseEvent> lValidation = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				bAdd.setEnabled(dtPicker.isValid());
			}
		};
		dtPicker.addListener(Events.Valid, lValidation);
		dtPicker.addListener(Events.Invalid, lValidation);

		String value = fDate.getValue();
		if (!Util.emptyString(value)) {
			try {
				Date d = dateTimeFormat.parse(fDate.getValue());
				dtPicker.setValue(d);
			} catch (IllegalArgumentException e) {
				// do nothing.
			}
		}
		SelectionListener<ButtonEvent> lAccept = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				wCalendar.hide();
				fDate.setValue(dateTimeFormat.format(dtPicker.getValue()));
			}
		};

		Component bAccept = wCalendar.getButtonBar().getItemByItemId(ID_BUTTON_ADD);
		bAccept.removeAllListeners();
		bAccept.addListener(Events.Select, lAccept);
		wCalendar.show();
	}

	/**
	 * Clear the date text field
	 */
	public void clear() {
		fDate.clear();
	}

	public Date getDateValue() {
		if (fDate.getValue() != null) {
			return dateTimeFormat.parse(fDate.getValue());
		} else {
			return null;
		}

	}

	/**
	 * @param date
	 */
	public void setDateValue(Date d) {
		this.date = d;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @param buttons the buttons to set
	 */
	@Inject
	public void setButtons(Buttons buttons) {
		this.buttons = buttons;
	}

	/**
	 * @param dtPicker the dtPicker to set
	 */
	@Inject
	public void setDtPicker(DateTimePicker dtPicker) {
		this.dtPicker = dtPicker;
	}

	/**
	 * Sets the label to the date field
	 * @param label the label to set
	 */
	public void setFieldLabel(String label) {
		this.fDate.setFieldLabel(label);
	}
}
