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

package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleSelDTO;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.ConfigurationPanel;
import com.isotrol.impe3.pms.gui.client.widget.DependencesPanel;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Common GUI & logic for module detail panels (connectors & portal components).
 * @author Andrei Cojocaru
 * 
 */
public abstract class AModuleDetailPanel extends TypicalWindow implements IDetailPanel {

	/**
	 * main container<br/>
	 */
	private TabPanel container = null;

	/** css class for connector input tfName */
	private TextField<String> tfName = null;

	/**
	 * Description field.<br/>
	 */
	private TextArea taDesc = null;

	/** Panel with the template's dependencies */
	private DependencesPanel tiDeps = null;

	/** Panel with the template's configuration items */
	private ConfigurationPanel tiConfig = null;

	/**
	 * Change events source fields.<br/>
	 */
	private List<Field<?>> fields = null;

	/**
	 * Module name<br/>
	 */
	private TextField<String> tfModuleName = null;

	/**
	 * Module description<br/>
	 */
	private TextArea taModuleDesc = null;

	/**
	 * Listener for accept button.<br/>
	 */
	private SelectionListener<ButtonEvent> acceptListener = null;
	
	/**
	 * Listener for accept button.<br/>
	 */
	private SelectionListener<ButtonEvent> applyListener = null;

	/**
	 * Data bound to this widget.<br/>
	 */
	private ModuleInstanceTemplateDTO moduleInstanceTemplateDto = null;
	
	/**
	 * Apply button
	 */
	private Button bApply = null;
	
	/**
	 * Save button
	 */
	private Button bSave = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * Forms helper.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * PMS events listening strategy.<br/>
	 */
	private PmsListeningStrategy pmsListeningStrategy = null;

	/**
	 * String validator for <b>name</b> field.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Generic styles bundle
	 */
	private GuiCommonStyles guiCommonStyles = null;
	
	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Default constructor.
	 */
	public AModuleDetailPanel() {
		setClosable(true);
	}

	/**
	 * Inits the widget. Must be explicitly called after properties injection.<br/>
	 * @param mitDto
	 */
	public void init(ModuleInstanceTemplateDTO mitDto) {
		this.moduleInstanceTemplateDto = mitDto;

		initListeners();

		initComponent();
	}

	/**
	 * Inits the listeners without binding them to any Observable.<br/>
	 */
	private void initListeners() {
		acceptListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							applyValues();
							trySaveModuleInstance(true);
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), getModuleSaveConfirmText(), lConfirm).setModal(true);
			}
		};
		
		applyListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							applyValues();
							trySaveModuleInstance(false);
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), getModuleSaveConfirmText(), lConfirm).setModal(true);
			}
		};
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	protected void initComponent() {

		initThis();

		addFields();

		showDataValues();

		addButtonBar();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setWidth(PmsConstants.DETAIL_WINDOW_WIDTH);
		setHeading(moduleInstanceTemplateDto.getName());
		setClosable(false);
		setLayout(new FitLayout());
		setBodyBorder(false);

		container = new TabPanel();

		add(container);
	}

	/**
	 * Adds the Module instance fields: name, description, dependencies and configuration.<br/> It also displays the
	 * instantiated Module information: name and description.
	 */
	private void addFields() {

		TabItem tiBasicProperties = new TabItem(pmsMessages.basicPropertiesWidgetTitle());
		tiBasicProperties.setLayout(new FitLayout());
		tiBasicProperties.setScrollMode(Scroll.AUTOY);
		LayoutContainer lcBasicProperties = new LayoutContainer(formSupport.getStandardLayout());
		lcBasicProperties.addStyleName(guiCommonStyles.margin10px());
		lcBasicProperties.setAutoHeight(true);
		tiBasicProperties.add(lcBasicProperties);

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setValidator(nonEmptyStringValidator);
		tfName.setAutoValidate(true);
		tfName.addListener(Events.Render, validatorListener);

		lcBasicProperties.add(tfName);

		taDesc = new TextArea();
		taDesc.setFieldLabel(pmsMessages.labelDescription());
		lcBasicProperties.add(taDesc);

		fields = Arrays.asList(new Field<?>[] {tfName, taDesc});

		addModuleInfoFieldSet(lcBasicProperties);
		container.add(tiBasicProperties);

		List<DependencyTemplateDTO> deps = moduleInstanceTemplateDto.getDependencies();
		if (deps != null && deps.size() > 0) {
			tiDeps = PmsFactory.getInstance().getDependencesPanel();
			tiDeps.init(deps);
			TabItem ti = new TabItem(pmsMessages.dependencesWidgetTitle());
			ti.setScrollMode(Scroll.AUTOY);
			ti.setLayout(new FitLayout());
			ti.add(tiDeps);
			container.add(ti);
		}

		ConfigurationTemplateDTO config = moduleInstanceTemplateDto.getConfiguration();
		if (config != null && config.getItems().size() > 0) {
			tiConfig = PmsFactory.getInstance().getConfigurationPanel();
			tiConfig.init(config);
			container.add(tiConfig);
		}
	}

	/**
	 * Adds the instantiated Module information: name and description. This info is readonly.<br/>
	 */
	private void addModuleInfoFieldSet(LayoutContainer c) {
		// module info fieldset:
		FieldSet fsModuleInfo = new FieldSet();
		fsModuleInfo.setCollapsible(false);
		fsModuleInfo.setHeading(pmsMessages.headerModuleInfo());

		FormLayout fl = formSupport.getStandardLayout();
		fsModuleInfo.setLayout(fl);

		tfModuleName = new TextField<String>();
		formSupport.configReadOnly(tfModuleName);
		tfModuleName.setFieldLabel(pmsMessages.labelName());
		fsModuleInfo.add(tfModuleName);

		taModuleDesc = new TextArea();
		formSupport.configReadOnly(taModuleDesc);
		taModuleDesc.setFieldLabel(pmsMessages.labelDescription());
		fsModuleInfo.add(taModuleDesc);

		c.add(fsModuleInfo);
	}

	/**
	 * Adds "Accept" and "Cancel" controls.<br/>
	 */
	private void addButtonBar() {

		List<Component> components = new LinkedList<Component>(fields);
		if (tiConfig != null) {
			components.add(tiConfig);
		}
		if (tiDeps != null) {
			components.add(tiDeps);
		}
		
		bApply = buttonsSupport.createApplyButtonForDetailPanels(this, applyListener, components,
			pmsListeningStrategy);
		addButton(bApply);

		bSave = buttonsSupport.createSaveButtonForDetailPanels(this, acceptListener, components,
			pmsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Shows the current mapping values in components directly managed by this panel.<br/>
	 */
	protected void showDataValues() {

		String sTemplate = moduleInstanceTemplateDto.getName();
		tfName.setValue(sTemplate);
		tfName.updateOriginalValue(sTemplate);

		String description = moduleInstanceTemplateDto.getDescription();
		taDesc.setValue(description);
		taDesc.updateOriginalValue(description);

		ModuleSelDTO mDto = moduleInstanceTemplateDto.getModule();

		String mName = mDto.getName();
		tfModuleName.setValue(mName);
		tfModuleName.updateOriginalValue(mName);

		String mDesc = mDto.getDescription();
		taModuleDesc.setValue(mDesc);
		taModuleDesc.updateOriginalValue(mDesc);
	}
	
	protected void disableSaveButtons() {
		bSave.disable();
		bApply.disable();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public final boolean isValid() {
		boolean valid = tfName.isValid();
		if (valid && tiConfig != null) {
			valid = valid && tiConfig.isValid();
		}
		if (valid && tiDeps != null) {
			valid = valid && tiDeps.isValid();
		}
		return valid;
	}

	/**
	 * Component is dirty when any of its fields is dirty, or any of the configuration/dependencies panels fields is
	 * dirty.<br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Field<?>> it = fields.iterator();
		while (it.hasNext() && !dirty) {
			dirty = dirty || it.next().isDirty();
		}

		boolean tiConfigDirty = false;
		if (!dirty && tiConfig != null) {
			tiConfigDirty = tiConfig.isDirty();
		}

		boolean tiDepsDirty = false;
		if (!dirty && tiDeps != null) {
			tiDepsDirty = tiDeps.isDirty();
		}

		return dirty || tiConfigDirty || tiDepsDirty;
	}

	/**
	 * Injects values from GUI objects to bound DTOs.<br/>
	 */
	private void applyValues() {

		moduleInstanceTemplateDto.setName(tfName.getValue());
		tfName.updateOriginalValue(tfName.getValue());

		moduleInstanceTemplateDto.setDescription(taDesc.getValue());
		taDesc.updateOriginalValue(taDesc.getValue());

		// save config:
		if (tiConfig != null) {
			tiConfig.applyValues();
		}

		// save dependencies:
		if (tiDeps != null) {
			tiDeps.applyValues();
		}
	}

	/**
	 * Saves the module instance by calling the RPC service.<br/>
	 * @param closeThis if current window must be closed
	 */
	protected abstract void trySaveModuleInstance(boolean closeThis);

	/**
	 * <br/>
	 * @return the text displayed in the confirm window before saving the module
	 */
	protected abstract String getModuleSaveConfirmText();

	/**
	 * @return the messages
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the pmsMessages
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the buttons helper
	 */
	protected Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	/**
	 * @return the shared objects container.
	 */
	protected Util getUtilities() {
		return util;
	}

	/**
	 * @return the bound data instance.
	 */
	protected final ModuleInstanceTemplateDTO getBoundModule() {
		return this.moduleInstanceTemplateDto;
	}

	/*
	 * Dependencies injectors.
	 */
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
	 * Injects the buttons helper object.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the shared objects container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the forms helper
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
		this.pmsListeningStrategy = pmsListeningStrategy;
	}

	/**
	 * Injects the name validator
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
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error message processor
	 */
	protected ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @param guiCommonStyles the guiCommonStyles to set
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}
	
	/**
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @return the settings
	 */
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @param moduleInstanceTemplateDto the moduleInstanceTemplateDto to set
	 */
	public void setModuleInstanceTemplateDto(ModuleInstanceTemplateDTO moduleInstanceTemplateDto) {
		this.moduleInstanceTemplateDto = moduleInstanceTemplateDto;
	}

	/**
	 * @return the moduleInstanceTemplateDto
	 */
	public ModuleInstanceTemplateDTO getModuleInstanceTemplateDto() {
		return moduleInstanceTemplateDto;
	}
}
