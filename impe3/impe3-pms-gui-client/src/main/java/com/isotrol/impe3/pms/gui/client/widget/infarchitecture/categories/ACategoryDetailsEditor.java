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


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.LocalizedNameModel;
import com.isotrol.impe3.pms.gui.client.error.CategoriesServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.ILocalizedNamesReceiver;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.LocalesMappingWindow;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Popup window that manages a Category details.
 * 
 * @author Andrei Cojocaru
 */
public abstract class ACategoryDetailsEditor extends TypicalWindow implements IDetailPanel, ILocalizedNamesReceiver {

	/**
	 * The category "name" field<br/>
	 */
	private TextField<String> tfName = null;

	/**
	 * The category "path" field.<br/>
	 */
	private TextField<String> tfPath = null;

	/**
	 * The category "visible" field.<br/>
	 */
	private CheckBox cbVisible = null;

	/**
	 * Category "routable" field.<br/>
	 */
	private CheckBox cbRoutable = null;

	/**
	 * The fields that can change the "dirty" and "valid" states on this component.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * Main layout container. Only direct child of this class panel.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * Data bound to current widget.<br/>
	 */
	private CategoryDTO categoryDto = null;

	/** Window that manages the localized names */
	private LocalesMappingWindow wLocales = null;

	private Map<String, NameDTO> localizedNames = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Error message resolver for the Categories service.<br/>
	 */
	private CategoriesServiceErrorMessageResolver emrCategories = null;
	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;
	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;
	/**
	 * Proxy to Categories async service.<br/>
	 */
	private ICategoriesServiceAsync categoriesService = null;

	/**
	 * Messages app-specific service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Messages general service.<br/>
	 */
	private GuiCommonMessages messages = null;

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
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Generic styles bundle.<br />
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Pms styles bundle
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Constructor with bound model and operation type<br/>
	 * 
	 * @param cat
	 * @param op
	 */
	public ACategoryDetailsEditor() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.
	 * @param cat bound Category
	 * @param op operation type (edition or creation)
	 */
	protected void init(CategoryDTO cat) {
		this.categoryDto = cat;

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
		setHeadingText(getHeadingText());
		setClosable(false);
		setScrollMode(Scroll.NONE);

		container = new LayoutContainer(formSupport.getStandardLayout(false));
		container.addStyleName(getStyles().margin10px());
		container.setBorders(false);
		container.setAutoHeight(true);
		add(container);
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {

		// help button
		getHeader().addTool(buttonsSupport.createHelpToolButton(settings.pmsCategoriesAdminPortalManualUrl()));

		addFields();

		// must be called before addButtonBar();
		displayBoundDataValues();

		addButtonBar();
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {
		fields = Arrays.asList(new Component[] {tfName, tfPath, cbVisible, cbRoutable, wLocales});

		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, getAcceptButtonListener(),
			new LinkedList<Component>(fields), pmsListeningStrategy);
		addButton(bAccept);

		final Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds the fields to the panel.<br/>
	 */
	private void addFields() {
		wLocales = PmsFactory.getInstance().getLocalizedNamesWindow();
		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		tfName.setValidator(nonEmptyStringValidator);
		// perform validation on render:
		tfName.addListener(Events.Render, validatorListener);
		lr[0].add(tfName);

		tfPath = new TextField<String>();
		tfPath.setFieldLabel(pmsMessages.labelPath());
		tfPath.setAllowBlank(false);
		tfPath.setAutoValidate(true);
		tfPath.setValidator(nonEmptyStringValidator);
		lr[0].add(tfPath);

		// button to manage the locales map
		SelectionListener<IconButtonEvent> lLocales = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				showLanguagesWindow();
			}
		};
		buttonsSupport.addGenericIconButton(pmsMessages.titleLocale(), pmsStyles.iconLocale(), lr[1], lLocales);

		cbVisible = new CheckBox();
		cbVisible.addInputStyleName(styles.checkBoxAlignLeft());
		cbVisible.setFieldLabel(pmsMessages.labelVisible());
		container.add(cbVisible);

		cbRoutable = new CheckBox();
		cbRoutable.addInputStyleName(styles.checkBoxAlignLeft());
		cbRoutable.setFieldLabel(pmsMessages.labelRoutable());
		container.add(cbRoutable);
	}

	/**
	 * Creates the languages window, if not already created. Shows the languages window.<br/>
	 */
	private void showLanguagesWindow() {
		if (!wLocales.isInitialized()) {
			wLocales.init(this, categoryDto.getLocalizedNames());
		}
		wLocales.show();
	}

	/**
	 * Returns the listener for "Accept" button.<br/> Should be overridden by subclasses with correct specific
	 * logic.<br/>
	 * 
	 * @return
	 */
	protected abstract SelectionListener<ButtonEvent> getAcceptButtonListener();

	/**
	 * Should be overridden by subclasses with correct specific logic.<br/>
	 * 
	 * @return String containing the displayed panel heading text.
	 */
	protected abstract String getHeadingText();

	/**
	 * Displays on GUI the values of the bound DTO.<br/>
	 */
	private void displayBoundDataValues() {

		NameDTO cName = categoryDto.getDefaultName();

		String displayName = cName.getDisplayName();
		tfName.setValue(displayName);
		tfName.updateOriginalValue(displayName);

		String path = cName.getPath();
		tfPath.setValue(path);
		tfPath.updateOriginalValue(path);

		boolean routable = categoryDto.isRoutable();
		cbRoutable.setValue(routable);
		cbRoutable.updateOriginalValue(routable);

		boolean visible = categoryDto.isVisible();
		cbVisible.setValue(visible);
		cbVisible.updateOriginalValue(visible);

		localizedNames = categoryDto.getLocalizedNames();
	}

	public void receiveLocalizedNames(List<LocalizedNameModel> locales) {
		localizedNames = new HashMap<String, NameDTO>();

		for (LocalizedNameModel ln : locales) {
			localizedNames.put(ln.getLocale(), new NameDTO(ln.getName(), ln.getPath()));
		}
	}

	/**
	 * <br/>
	 * @return the "name" text field.
	 */
	protected final TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * @return the tfPath
	 */
	/**
	 * <br/>
	 * @return the "path" text field.
	 */
	public TextField<String> getTfPath() {
		return tfPath;
	}

	/**
	 * <br/>
	 * @return "visible" field checkbox.
	 */
	protected final CheckBox getCbVisible() {
		return cbVisible;
	}

	/**
	 * <br/>
	 * @return "routable" field checkbox.
	 */
	protected final CheckBox getCbRoutable() {
		return cbRoutable;
	}

	/**
	 * @return <code>true</code>, if at least one of the panel fields is dirty.
	 * @see Field#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Component> it = fields.iterator();
		while (it.hasNext() && !dirty) {
			Component field = it.next();
			if (field instanceof Field<?>) {
				dirty = dirty || ((Field<?>) field).isDirty();
			} else if (field instanceof Window) {
				dirty = dirty || wLocales.isDirty();
			}
		}
		return dirty;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		boolean valid = true;
		Iterator<Component> it = fields.iterator();
		while (valid && it.hasNext()) {
			Component field = it.next();
			if (field instanceof Field<?>) {
				valid = valid && ((Field<?>) field).isValid();
			}
		}
		return valid;
	}

	/**
	 * @return the pmsMessages service
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the messages service.
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the Categories async service proxy.
	 */
	protected ICategoriesServiceAsync getCategoriesService() {
		return categoriesService;
	}

	/**
	 * @return the static objects container
	 */
	protected Util getUtilities() {
		return util;
	}

	/**
	 * @return the bound Category.
	 */
	protected CategoryDTO getCategory() {
		return categoryDto;
	}

	/**
	 * @return the emrCategories
	 */
	protected final CategoriesServiceErrorMessageResolver getEmrCategories() {
		return emrCategories;
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
	 * Injects the generic message bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
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
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = (IComponentListeningStrategy) pmsListeningStrategy;
	}

	/**
	 * @param nonEmptyStringValidator the nonEmptyStringValidator to set
	 */
	@Inject
	public void setNonEmptyStringValidator(NonEmptyStringValidator nonEmptyStringValidator) {
		this.nonEmptyStringValidator = nonEmptyStringValidator;
	}

	/**
	 * Injects the field validator listener.
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
	}

	/**
	 * @param emr the emrCategories to set
	 */
	@Inject
	public void setErrorMessageResolver(CategoriesServiceErrorMessageResolver emr) {
		this.emrCategories = emr;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the error message processor
	 */
	@Inject
	public ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
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

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @return the localizedNames
	 */
	public Map<String, NameDTO> getLocalizedNames() {
		if (localizedNames == null) {
			localizedNames = new HashMap<String, NameDTO>();
		}
		return localizedNames;
	}

	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
