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

package com.isotrol.impe3.pms.gui.client.widget.design;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ColumnsFrameDTO;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * Form to create columns
 * @author Manuel Ruiz
 * 
 */
public class ColumnSelector extends ContentPanel {

	private static final String TEMPLATE_COLNUM = "<span style='margin-left:7px'>${LABEL_COLNUM}</span>";
	private static final String PATTERN_LABEL_COLNUM = "\\$\\{LABEL_COLNUM\\}";

	private static final String FORM_ID = "columnSelector-form-id";
	private static final String NUMCOLS_FIELD_ID = "numCols-id";
	private static final String NEXTBUTTON_CSS = "nextButton";
	/** style attribute 'padding' */
	private static final String PADDING_STYLE = "padding";

	private FieldSet fieldSetCols = new FieldSet();
	private int lastNumColsValue;

	private VerticalPanel palette = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS utilities service.<br/>
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Create the column selector form
	 */
	public ColumnSelector() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies injection
	 * @param panel
	 */
	public void init(VerticalPanel panel) {
		this.palette = panel;
		initThis();

		initComponents();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		HorizontalPanel numColsPanel = new HorizontalPanel();
		numColsPanel.setStyleAttribute(PADDING_STYLE, "7px 0");
		numColsPanel.setAutoWidth(true);

		String sCols = TEMPLATE_COLNUM.replaceAll(PATTERN_LABEL_COLNUM, pmsMessages.labelNumberOfColumns());
		Html hCols = new Html(sCols);
		numColsPanel.add(hCols);

		final NumberField numCols = new NumberField();
		numCols.setAllowBlank(false);
		numCols.setAutoWidth(true);
		numCols.setId(NUMCOLS_FIELD_ID);
		numColsPanel.add(numCols);

		IconButton nextButton = new IconButton(NEXTBUTTON_CSS);
		nextButton.setTitle(pmsMessages.titleNext());
		nextButton.addSelectionListener(new SelectionListener<IconButtonEvent>() {

			@Override
			public void componentSelected(IconButtonEvent ce) {

				int n = numCols.getValue().intValue();
				if (n < 1) {
					MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorNonPositiveInteger(), null)
						.setModal(true);
					return;
				}
				if (n != lastNumColsValue) {
					lastNumColsValue = n;
					addColFields(n, palette);
				}
			}

		});
		numColsPanel.add(nextButton);

		add(numColsPanel);

		Button cancelButton = new Button(messages.labelCancel());
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();

				// clear creation column form
				numCols.clear();
				removeFieldColumns();
			}
		});

		addButton(cancelButton);
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setHeaderVisible(false);
		setAutoWidth(true);
		setId(FORM_ID);
		setButtonAlign(HorizontalAlignment.LEFT);
	}

	private void removeFieldColumns() {
		if (fieldSetCols != null) {
			fieldSetCols.removeFromParent();
		}
		fieldSetCols = null;
		lastNumColsValue = 0;
	}

	protected void addColFields(final int value, final VerticalPanel panel) {

		removeFieldColumns();

		fieldSetCols = new FieldSet();
		fieldSetCols.setLayout(new FormLayout());
		fieldSetCols.setBorders(false);

		final List<NumberField> cols = new LinkedList<NumberField>();
		final List<ColumnDTO> columns = new ArrayList<ColumnDTO>();

		for (int i = 1; i <= value; i++) {
			final NumberField col = new NumberField();
			col.setFieldLabel(pmsMessages.labelColumnPercent(Integer.toString(i)));
			col.addStyleName("selector-width-column");
			cols.add(col);
			fieldSetCols.add(col);
		}

		Button createButton = new Button(pmsMessages.labelCreateFrame());
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Iterator<NumberField> it = cols.iterator();
				Integer[] widths = new Integer[value];
				int i = 0;
				while (it.hasNext()) {
					NumberField field = it.next();
					widths[i] = field.getValue().intValue();
					ColumnDTO colDto = new ColumnDTO();
					colDto.setWidth(field.getValue().intValue());
					columns.add(colDto);
					i++;
				}
				if (pmsUtil.totalWidthIs100(Arrays.asList(widths))) {
					ColumnsFrameDTO frame = new ColumnsFrameDTO();
					frame.setChildren(columns);
					FrameColumn boxColumn = PmsFactory.getInstance().getFrameColumn().init(frame);
					boxColumn.setClosable(true);
					panel.add(boxColumn);
					ColumnSelector.this.hide();
				} else {
					MessageBox
						.alert(messages.headerErrorWindow(), pmsMessages.msgErrorColumnsDontFillContainer(), null)
						.setModal(true);
				}
			}

		});

		fieldSetCols.add(createButton);

		add(fieldSetCols);
		layout();

	}

	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
