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

package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories;


import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;


/**
 * Popup window that manages a categories export.
 * 
 * @author Manuel Ruiz
 */
public class CategoriesExportWindow extends TypicalWindow {

	/**
	 * Checkbox to export selected category
	 */
	private CheckBox cbSelectedCategory = null;

	/**
	 * Checkbox to export all levels
	 */
	private CheckBox cbAllLevels = null;

	/**
	 * Main layout container. Only direct child of this class panel.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * Category's id to export
	 */
	private String categoryId = null;

	/*
	 * Injected deps
	 */
	/**
	 * Proxy to Categories async service.<br/>
	 */
	private ICategoriesServiceAsync categoriesService = null;

	/**
	 * Messages app-specific service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons support service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms support service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * Helper service that contains shared objects.<br/>
	 */
	private Util util = null;
	
	/**
	 * PmsUtil support
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Generic styles bundle.<br />
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Flag to indicate if passed category is the root category
	 */
	private boolean root = false;

	/**
	 * Constructor<br/>
	 * 
	 */
	public CategoriesExportWindow() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.
	 * @param cat bound Category
	 */
	protected void init(String cat) {
		this.categoryId = cat;

		initThis();
		initComponents();
	}

	/**
	 * Inits the component properties.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setWidth(Constants.FORM_WINDOW_WIDTH);
		setAutoHeight(true);
		setModal(true);
		setHeaderVisible(true);
		setHeading(pmsMessages.headerCategoriesExport());
		setClosable(false);
		setScrollMode(Scroll.NONE);

		FormLayout formLayout = formSupport.getStandardLayout(false);
		formLayout.setLabelWidth(200);
		container = new LayoutContainer(formLayout);
		container.addStyleName(getStyles().margin10px());
		container.setBorders(false);
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {

		addFields();
		addButtonBar();
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {

		Button bAccept = buttonsSupport
			.createGenericButton(pmsMessages.labelExport(), "", getAcceptButtonListener());
		addButton(bAccept);

		final Button bCancel = buttonsSupport.createCancelButton(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				hide();
			}
		});
		addButton(bCancel);
	}

	/**
	 * Adds the fields to the panel.<br/>
	 */
	private void addFields() {

		cbSelectedCategory = new CheckBox();
		cbSelectedCategory.addInputStyleName(styles.checkBoxAlignLeft());
		cbSelectedCategory.setFieldLabel(pmsMessages.labelExportSelectedCategory());
		container.add(cbSelectedCategory);
		if(isRoot()) {
			cbSelectedCategory.disable();
			cbSelectedCategory.setValue(false);
		}

		cbAllLevels = new CheckBox();
		cbAllLevels.addInputStyleName(styles.checkBoxAlignLeft());
		cbAllLevels.setFieldLabel(pmsMessages.labelExportAllLevels());
		container.add(cbAllLevels);
	}

	/**
	 * Returns the listener for "Accept" button.<br/>
	 * 
	 */
	private SelectionListener<ButtonEvent> getAcceptButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				tryExportCategories();
			}
		};
	}

	private void tryExportCategories() {

		mask(pmsMessages.mskCategoriesExport());

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			public void onSuccess(String result) {
				unmask();
				pmsUtil.exportPmsFile(result);
			}

			public void onFailure(Throwable caught) {
				unmask();
				util.error(pmsMessages.msgErrorExportCategories());
			}
		};
		categoriesService.exportBranch(categoryId, cbSelectedCategory.getValue(), cbAllLevels.getValue(), callback);
	}

	/**
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the Categories service proxy.
	 * @param categoriesService
	 */
	@Inject
	public void setCategoriesService(ICategoriesServiceAsync categoriesService) {
		this.categoriesService = categoriesService;
	}

	/**
	 * Injects the buttons support object.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the statics container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the form support service.
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * @return the styles
	 */
	public GuiCommonStyles getStyles() {
		return styles;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	protected void setRoot(boolean root) {
		this.root  = root;
	}

	/**
	 * @return true if the passed category is the root category
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
