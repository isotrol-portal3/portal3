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

package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.isotrol.impe3.gui.common.util.databinding.ADataBoundContentPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.LocalizedNameModel;
import com.isotrol.impe3.pms.gui.client.error.ContentTypeErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.ILocalizedNamesReceiver;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.LocalesMappingWindow;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Abstract base class for Content Types edition panels. Concrete logic for "accept" buttons (create new/edit) is
 * implemented in subclasses.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class AContentTypeEdition extends TypicalWindow implements IDetailPanel, ILocalizedNamesReceiver {

	/**
	 * main container: the only element directly attached to the details window.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * "Name" field.<br/>
	 */
	private TextField<String> tfName = null;
	/**
	 * "Path" field.<br/>
	 */
	private TextField<String> tfPath = null;
	/** description field */
	private TextArea taDescription = null;
	/** routable field */
	private CheckBox cbRoutable = null;

	/**
	 * Contains the panel fields.<br/>
	 */
	private List<Component> fields = null;

	/**
	 * navigable field<br/>
	 */
	private CheckBox cbNavigable = null;

	/**
	 * "Accept" button.<br/>
	 */
	private Button bAccept = null;

	/**
	 * Data bound to current widget.<br/>
	 */
	private ContentTypeDTO contentTypeDto = null;

	/** Window that manages the localized names */
	private LocalesMappingWindow wLocales = null;

	/**
	 * Content type's localized names
	 */
	private Map<String, NameDTO> localizedNames = null;

	/*
	 * Injected deps
	 */
	/**
	 * The services errors processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * General messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * App specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Content Types proxy controller.<br/>
	 */
	private IContentTypesServiceAsync contentTypesService = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms support service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;

	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Error Message Resolver for the content types service.<br/>
	 */
	private ContentTypeErrorMessageResolver emrCt = null;

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
	 * Default constructor for any {@link ADataBoundContentPanel}<br/>
	 * 
	 * @param model
	 */
	public AContentTypeEdition() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are set.
	 * @param model
	 */
	public void init(ContentTypeDTO model) {
		this.contentTypeDto = model;

		initThis();
		initComponent();
		displayBoundDataValues();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		// help button
		getHeader().addTool(buttonsSupport.createHelpToolButton(settings.pmsContentTypesAdminPortalManualUrl()));

		addFields();
		addButtonBar();
	}

	/**
	 * Creates, configures and adds the toolbar to this container.<br/>
	 */
	private void addButtonBar() {
		fields = Arrays.asList(new Component[] {tfName, tfPath, taDescription, cbRoutable, cbNavigable, wLocales});

		bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, getAcceptButtonSelectionListener(),
			new LinkedList<Component>(fields), pmsListeningStrategy);
		addButton(bAccept);

		// "Cancel" button:
		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	/**
	 * Adds fields for name, description, path, routable and navigable.<br/>
	 */
	private void addFields() {
		wLocales = PmsFactory.getInstance().getLocalizedNamesWindow();
		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		tfName = new TextField<String>();
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		tfName.setValidator(nonEmptyStringValidator);
		tfName.addListener(Events.Render, validatorListener);
		tfName.setWidth(Constants.FIELD_WIDTH);
		lr[0].add(tfName);

		tfPath = new TextField<String>();
		tfPath.setFieldLabel(pmsMessages.labelPath());
		tfPath.setAllowBlank(false);
		tfPath.setAutoValidate(true);
		tfPath.setValidator(nonEmptyStringValidator);
		tfPath.addListener(Events.Render, validatorListener);
		lr[0].add(tfPath);

		// button to manage the locales map
		SelectionListener<IconButtonEvent> lLocales = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				showLanguagesWindow();
			}
		};
		buttonsSupport.addGenericIconButton(pmsMessages.titleLocale(), pmsStyles.iconLocale(), lr[1], lLocales);

		taDescription = new TextArea();
		taDescription.setFieldLabel(pmsMessages.labelDescription());
		container.add(taDescription);

		cbRoutable = new CheckBox();
		cbRoutable.addInputStyleName(styles.checkBoxAlignLeft());
		cbRoutable.setFieldLabel(pmsMessages.labelRoutable());
		container.add(cbRoutable);

		cbNavigable = new CheckBox();
		cbNavigable.addInputStyleName(styles.checkBoxAlignLeft());
		cbNavigable.setFieldLabel(pmsMessages.labelBrowsable());
		container.add(cbNavigable);
	}

	/**
	 * Creates the languages window, if not already created. Shows the languages window.<br/>
	 */
	private void showLanguagesWindow() {
		if (!wLocales.isInitialized()) {
			wLocales.init(this, contentTypeDto.getLocalizedNames());
		}

		wLocales.show();
	}

	/**
	 * Inits this component properties, as well as its main container.<br/>
	 */
	private void initThis() {
		setLayout(new FitLayout());
		setAutoHeight(true);
		setButtonAlign(HorizontalAlignment.LEFT);
		setWidth(Constants.FORM_WINDOW_WIDTH);
		setModal(true);
		setHeaderVisible(true);
		setClosable(false);
		setScrollMode(Scroll.NONE);

		String idSuffix = getOperationSuffix();
		setId("info_architecture_content_type" + idSuffix);

		container = new LayoutContainer(formSupport.getStandardLayout());
		container.setAutoHeight(true);
		container.addStyleName(getStyles().margin10px());
		container.setBorders(false);
		add(container);
	}

	/**
	 * default logic for Accept buttons. May be overridden in subclasses.<br/>
	 */
	protected SelectionListener<ButtonEvent> getAcceptButtonSelectionListener() {
		return new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> callback = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked.getItemId().equals(Dialog.YES)) { // confirmed
							trySaveCurrentContentType();
						}
					}
				};
				MessageBox confirm = MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages
					.msgConfirmSaveContentType(), callback);
				confirm.setModal(true);
			}
		};
	}

	/**
	 * Calls the Content Types remote service "save" operation for the current Content Type.<br/>
	 */
	protected final void trySaveCurrentContentType() {
		util.mask(pmsMessages.mskSaveContentType());

		State state = null;
		if (contentTypeDto.getId() == null) {
			state = State.NEW;
		} else {
			state = State.MODIFIED;
		}
		contentTypeDto.setState(state);

		NameDTO name = new NameDTO();
		name.setDisplayName(getTfName().getValue());
		name.setPath(getTfPath().getValue());
		contentTypeDto.setDefaultName(name);
		contentTypeDto.setDescription(getTfDescription().getValue());
		contentTypeDto.setRoutable(getCbRoutable().getValue());
		contentTypeDto.setNavigable(getCbNavigable().getValue());

		if (localizedNames == null) {
			localizedNames = new HashMap<String, NameDTO>();
		}
		contentTypeDto.setLocalizedNames(localizedNames);

		AsyncCallback<ContentTypeDTO> callback = getSaveCallback();

		contentTypesService.save(contentTypeDto, callback);
	}

	/**
	 * Returns the callback code to execute after calling {@link ContentTypesService#save(ContentTypeDTO)}<br/>
	 * Implementation is operation-specific (CREATE/EDIT). See subclasses.
	 * 
	 * @return the callback code to execute after calling {@link ContentTypesService#save(ContentTypeDTO)}<br/>
	 * Implementation is operation-specific (CREATE/EDIT). See subclasses.
	 */
	protected abstract AsyncCallback<ContentTypeDTO> getSaveCallback();

	/**
	 * text for the Panel heading<br/>
	 * 
	 * @return
	 */
	protected abstract String getHeadingText();

	/**
	 * Operation suffix will be appended to the base strings for HTML ID attributes.<br/>
	 * 
	 * @return
	 */
	protected abstract String getOperationSuffix();

	/**
	 * Refreshes the GUI with the data contained in the bound DTO.<br/>
	 */
	private void displayBoundDataValues() {

		setHeading(getHeadingText());

		String displayName = contentTypeDto.getDefaultName().getDisplayName();
		tfName.setValue(displayName);
		tfName.updateOriginalValue(displayName);

		String path = contentTypeDto.getDefaultName().getPath();
		tfPath.setValue(path);
		tfPath.updateOriginalValue(path);

		String desc = contentTypeDto.getDescription();
		taDescription.setValue(desc);
		taDescription.updateOriginalValue(desc);

		boolean routable = contentTypeDto.isRoutable();
		cbRoutable.setValue(routable);
		cbRoutable.updateOriginalValue(routable);

		boolean navigable = contentTypeDto.isNavigable();
		cbNavigable.setValue(navigable);

		localizedNames = contentTypeDto.getLocalizedNames();
	}

	public void receiveLocalizedNames(List<LocalizedNameModel> locales) {
		localizedNames = new HashMap<String, NameDTO>();

		for (LocalizedNameModel ln : locales) {
			localizedNames.put(ln.getLocale(), new NameDTO(ln.getName(), ln.getPath()));
		}
	}

	/**
	 * <br/>
	 * @return the text field "Name"
	 */
	protected TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * <br/>
	 * @return the "path" text field.
	 */
	protected TextField<String> getTfPath() {
		return tfPath;
	}

	/**
	 * Returns the "Accept" button.<br/>
	 * @return the "Accept" button.
	 */
	protected Button getBAccept() {
		return bAccept;
	}

	/**
	 * @return the taDescription
	 */
	protected TextArea getTfDescription() {
		return taDescription;
	}

	/**
	 * @return the cbRoutable
	 */
	public CheckBox getCbRoutable() {
		return cbRoutable;
	}

	/**
	 * @return the cbNavigable
	 */
	public CheckBox getCbNavigable() {
		return cbNavigable;
	}

	/**
	 * @return <code>true</code>, if at least one of the panel fields is dirty.<br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty
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
	 * @return the messages service.
	 */
	protected GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the pmsMessages service.
	 */
	protected PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the static objects container
	 */
	protected Util getUtilities() {
		return util;
	}

	/**
	 * @return the bound Content Type
	 */
	protected ContentTypeDTO getContentType() {
		return contentTypeDto;
	}

	/**
	 * @return the emrCt
	 */
	protected final ContentTypeErrorMessageResolver getErrorMessageResolver() {
		return emrCt;
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
	 * Injects the PMS specific message bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the Content Types service.
	 * @param contentTypesService
	 */
	@Inject
	public void setContentTypesService(IContentTypesServiceAsync contentTypesService) {
		this.contentTypesService = contentTypesService;
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
	 * Injects the form supprt service.
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
	 * Injects the Content Types service Error Message Resolver
	 * @param emrCt the emrCt to set
	 */
	@Inject
	public void setEmrCt(ContentTypeErrorMessageResolver emrCt) {
		this.emrCt = emrCt;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the errors processor
	 */
	protected ServiceErrorsProcessor getErrorProcessor() {
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
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
