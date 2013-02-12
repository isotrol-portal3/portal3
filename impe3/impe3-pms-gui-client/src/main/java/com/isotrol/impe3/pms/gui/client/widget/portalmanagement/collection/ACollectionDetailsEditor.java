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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.SimpleModelData;
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
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Popup window that manages a SetFilter
 * 
 * @author Manuel Ruiz
 */
public abstract class ACollectionDetailsEditor extends TypicalWindow implements IDetailPanel {

	/**
	 * The set filter "name" field<br/>
	 */
	private TextField<String> tfName = null;

	/**
	 * The set filter "type" field.<br/>
	 */
	private ComboBox<SimpleModelData> cbType = null;

	/**
	 * The set filter "description" field<br/>
	 */
	private TextArea taDescription = null;

	/**
	 * The fields that can change the "dirty" and "valid" states on this component.<br/>
	 */
	private List<Field<?>> fields = null;

	/**
	 * Main layout container. Only direct child of this class panel.<br/>
	 */
	private LayoutContainer container = null;

	/**
	 * Data bound to current widget.<br/>
	 */
	private SetFilterDTO setFilterDto = null;

	/**
	 * Current portal
	 */
	private String portalId = null;
	
	private Map<String, String> mapFilterTypes = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	
	/**
	 * Error Message Resolver for Portals service.<br/>
	 */
	private PortalsServiceErrorMessageResolver emrPortals = null;

	/**
	 * Validates non empty strings.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;
	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;
	/**
	 * Proxy to Portals async service.<br/>
	 */
	private IPortalsServiceAsync portalsService = null;

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
	 * Constructor with bound model and operation type<br/>
	 */
	public ACollectionDetailsEditor() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are injected.
	 * @param setFilter bound SetFilter
	 */
	protected void init(String portalId, SetFilterDTO setFilter) {
		this.portalId = portalId;
		this.setFilterDto = setFilter;

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
		setHeading(getHeadingText());
		setClosable(false);
		setScrollMode(Scroll.NONE);

		container = new LayoutContainer(formSupport.getStandardLayout(false));
		container.addStyleName(getStyles().margin10px());
		container.setBorders(false);
		container.setAutoHeight(true);
		add(container);
		
		String mapString = pmsMessages.mapFilterTypes();
		mapFilterTypes = new HashMap<String, String>();
		for (String s : mapString.split(",")) {
			String[] entry = s.trim().split("=");
			if (!Util.emptyString(entry[0])) {
				mapFilterTypes.put(entry[0], entry[1]);
			}
		}
	}

	/**
	 * Inits the inner components of this panel.<br/>
	 */
	private void initComponents() {

		addFields();

		// must be called before addButtonBar();
		displayBoundDataValues();

		addButtonBar();
	}

	/**
	 * Creates and configures the button bar.<br/>
	 */
	private void addButtonBar() {
		fields = Arrays.asList(new Field<?>[] {tfName, cbType, taDescription});

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

		tfName = new TextField<String>();
		if(isEdition()) {
			formSupport.configReadOnly(tfName);
		}
		tfName.setFieldLabel(pmsMessages.labelName());
		tfName.setAllowBlank(false);
		tfName.setAutoValidate(true);
		tfName.setValidator(nonEmptyStringValidator);
		// perform validation on render:
		tfName.addListener(Events.Render, validatorListener);
		container.add(tfName);

		cbType = new ComboBox<SimpleModelData>();
		cbType.setFieldLabel(pmsMessages.labelType());
		cbType.setDisplayField(SimpleModelData.PROPERTY_DISPLAY_NAME);
		cbType.setTriggerAction(TriggerAction.ALL);
		cbType.setAllowBlank(false);
		cbType.setForceSelection(true);
		cbType.setEditable(false);
		cbType.addListener(Events.Render, validatorListener);
		// store population:
		ListStore<SimpleModelData> daStore = new ListStore<SimpleModelData>();
		cbType.setStore(daStore);
		for (FilterType type : FilterType.values()) {
			daStore.add(new SimpleModelData(mapFilterTypes.get(type.toString()), type.toString()));
		}
		container.add(cbType);

		taDescription = new TextArea();
		taDescription.setFieldLabel(pmsMessages.labelDescription());
		container.add(taDescription);
	}

	/**
	 * Returns the listener for "Accept" button.<br/>
	 * @return the listener for "Accept" button.
	 */
	private SelectionListener<ButtonEvent> getAcceptButtonListener() {
		return new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				trySaveSetFilter();
			}
		};
	}

	private void trySaveSetFilter() {
		util.mask(getPmsMessages().mskSaveSetFilter());

		SetFilterDTO setFilter = setFilterDto;
		setFilter.setName(getTfName().getValue());
		setFilter.setType(FilterType.valueOf((String) getCbType().getValue().getId()));
		setFilter.setDescription(getTaDescription().getValue());

		AsyncCallback<List<SetFilterDTO>> callback = new AsyncCallback<List<SetFilterDTO>>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrPortals, pmsMessages.msgErrorSaveSetFilter());
			}

			public void onSuccess(List<SetFilterDTO> arg0) {
				hide();
				util.unmask();
				util.info(getPmsMessages().msgSuccessSaveSetFilter());
			}
		};

		portalsService.putSetFilter(portalId, setFilter, callback);
	}

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
		tfName.setValue(setFilterDto.getName());
		tfName.updateOriginalValue(setFilterDto.getName());

		FilterType type = setFilterDto.getType();
		if (type != null) {
			SimpleModelData typeModel = cbType.getStore().findModel(SimpleModelData.PROPERTY_ID, type.toString());
			cbType.setValue(typeModel);
		}

		taDescription.setValue(setFilterDto.getDescription());
	}

	/**
	 * <br/>
	 * @return the "name" text field.
	 */
	protected final TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * @return <code>true</code>, if at least one of the panel fields is dirty.
	 * @see Field#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Field<?>> it = fields.iterator();
		while (it.hasNext() && !dirty) {
			dirty = dirty || it.next().isDirty();
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
		Iterator<Field<?>> it = fields.iterator();
		while (valid && it.hasNext()) {
			valid = valid && it.next().isValid();
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
	 * @return the Portals async service proxy.
	 */
	protected IPortalsServiceAsync getPortalsService() {
		return portalsService;
	}

	/**
	 * Injects the Portals service proxy.
	 * @param portalsService
	 */
	@Inject
	public void setPortalsService(IPortalsServiceAsync portalsService) {
		this.portalsService = portalsService;
	}

	/**
	 * @return the static objects container
	 */
	protected Util getUtilities() {
		return util;
	}

	/**
	 * @return the bound SetFilter.
	 */
	protected SetFilterDTO getSetFilter() {
		return setFilterDto;
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
	 * @return the taDescription
	 */
	public TextArea getTaDescription() {
		return taDescription;
	}

	/**
	 * @return the cbType
	 */
	public ComboBox<SimpleModelData> getCbType() {
		return cbType;
	}

	/**
	 * @param emrPortals the emrPortals to set
	 */
	@Inject
	public void setEmrPortals(PortalsServiceErrorMessageResolver emrPortals) {
		this.emrPortals = emrPortals;
	}
}
