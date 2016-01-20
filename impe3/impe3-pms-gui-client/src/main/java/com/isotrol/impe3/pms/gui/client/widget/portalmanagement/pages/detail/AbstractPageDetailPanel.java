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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.PageSelModelData;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.design.Design;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsInPageManagement;


/**
 * Abstract superclass for all page detail panels.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class AbstractPageDetailPanel extends TypicalWindow implements IDetailPanel {

	/**
	 * North panel (fields panel) height in pixels.<br/>
	 */
	private static final int FIELDS_PANEL_HEIGHT = 136;

	/** TextField of page name */
	private TextField<String> tfName = null;
	/** TextArea of page description */
	private TextArea fDescription = null;
	/** Combo with available templates that can be associated to current page<br/> */
	private ComboBox<ModelData> cbSelectTemplate = null;

	/**
	 * Main container for this widget inner elements.<br/> Any inner elements should be added to this container.
	 */
	private LayoutContainer topContainer = null;

	/**
	 * Page components management panel.<br/>
	 */
	private ComponentsInPageManagement compsPanel = null;

	/**
	 * DTO bound to the widget.<br/>
	 */
	private PageTemplateDTO pageTemplateDto = null;

	/**
	 * If a button to open the page layout is showed
	 */
	private boolean layoutButton = false;

	/**
	 * Button to save page
	 */
	private Button bSave = null;

	/**
	 * Apply Button to save page and not close the window
	 */
	private Button bApply = null;

	private enum SaveMode {
		SAVE, DESIGN, APPLY;
	}

	/*
	 * Injected deps.
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;

	/**
	 * Generic messages service reference.<br/>
	 */
	private GuiCommonMessages guiCommonMessages = null;

	/**
	 * PMS specific messages service reference.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Generic styles bundle.
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * PMS styles bundle.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms support service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * Pages async service proxy.<br/>
	 */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Error Message resolver for Pages service.<br/>
	 */
	private PageErrorMessageResolver emrPages = null;

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
	 * Contains the center widget (components in page).<br/>
	 */
	private LayoutContainer bottomContainer = null;

	/**
	 * The templates combo store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * The only allowed constructor.<br/>
	 */
	public AbstractPageDetailPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependencies injection.
	 * 
	 * @param pageDto
	 */
	public void init(PageTemplateDTO pageDto) {
		this.pageTemplateDto = pageDto;

		// general init:
		setLayout(new BorderLayout());
		setWidth(Constants.EIGHTY_FIVE_PERCENT);
		// setHeight(Constants.EIGHTY_FIVE_PERCENT);
		setClosable(false);
		setMaximizable(true);
		setHeadingText(guiCommonMessages.headerDetailPanel());

		topContainer = new LayoutContainer(formSupport.getStandardLayout(false));
		topContainer.setAutoHeight(true);
		topContainer.addStyleName(guiCommonStyles.margin10px());
		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.NORTH);
		layoutData.setSize(FIELDS_PANEL_HEIGHT);
		add(topContainer, layoutData);

		bottomContainer = new LayoutContainer(new FitLayout());
		layoutData = new BorderLayoutData(LayoutRegion.CENTER);
		add(bottomContainer, layoutData);

		initComponents();
		populateTemplateSelectorStore();
		displayPageValues(true);
	}

	/**
	 * Inits this topContainer inner components.<br/>
	 */
	protected abstract void initComponents();

	/**
	 * Default implementation. This component is dirty if any of its inner fields is dirty: <b>name</b>,
	 * <b>description</b>, <b>template selector combo</b>, <b>components tree</b>. <br/> If the concrete implementation
	 * had more fields, this method should be overridden in order to include those fields.
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return getCompsPanel().isDirty() || this.getTfName().isDirty() || this.getFDescription().isDirty()
			|| this.getCbSelectTemplate().isDirty();
	}

	/**
	 * Default implementation. This component is valid if all its inner fields are valid: <b>name</b>,
	 * <b>description</b>, <b>template selector combo</b>, <b>components tree</b>. <br/> If the concrete implementation
	 * had more fields, this method should be overridden in order to include those fields.
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		return getTfName().isValid() && getFDescription().isValid() // always valid
			&& getCbSelectTemplate().isValid() // always valid
			&& getCompsPanel().isValid();
	}

	/**
	 * Returns the PMS specific messages service.<br/>
	 * 
	 * @return the PMS specific messages service.
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * <br/>
	 * 
	 * @return <b>name</b> text field.
	 */
	protected final TextField<String> getTfName() {
		return tfName;
	}

	/**
	 * <br/> <b>description</b> field.
	 * 
	 * @return
	 */
	protected final TextArea getFDescription() {
		return fDescription;
	}

	/**
	 * <br/>
	 * 
	 * @return <b>select template</b> field.
	 */
	protected final ComboBox<ModelData> getCbSelectTemplate() {
		return cbSelectTemplate;
	}

	/**
	 * Sets the text field <b>name</b>.<br/>
	 * 
	 * @param tfName
	 */
	protected final void setTfName(TextField<String> tfName) {
		this.tfName = tfName;
	}

	/**
	 * <br/>
	 * 
	 * @return the page components management panel.
	 */
	protected final ComponentsInPageManagement getCompsPanel() {
		return compsPanel;
	}

	/**
	 * Inits the "name" textfield.<br/>
	 */
	protected final void initTfName() {
		/*
		 * page class (MAIN, DEFAULT, ERROR, SPECIAL, TAG, CATEGORY, CONTENT, TEMPLATE, CONTENT_TYPE (content list))
		 */
		PageClass pageClass = pageTemplateDto.getPageClass();

		tfName = new TextField<String>();
		tfName.setFieldLabel(getPmsMessages().labelName());
		if (pageClass.equals(PageClass.MAIN) || pageClass.equals(PageClass.DEFAULT)) {
			formSupport.configReadOnly(tfName);
		}
		tfName.setAllowBlank(false);
		tfName.setValidator(nonEmptyStringValidator);
		tfName.addListener(Events.Render, validatorListener);
		tfName.setAutoValidate(true);
	}

	/**
	 * Inits the Page description text area.<br/>
	 */
	protected final void initFDescription() {
		fDescription = new TextArea();
		fDescription.setFieldLabel(getPmsMessages().labelDescription());
	}

	/**
	 * Inits the template selector combo.<br/>
	 */
	protected final void initCbSelectTemplate() {
		cbSelectTemplate = new ComboBox<ModelData>();
		cbSelectTemplate.setFieldLabel(getPmsMessages().labelTemplate());
		cbSelectTemplate.setDisplayField(PageSelModelData.PROPERTY_NAME);
		cbSelectTemplate.setEditable(false);
		cbSelectTemplate.setTriggerAction(TriggerAction.ALL);
		cbSelectTemplate.setStore(new ListStore<ModelData>());
		cbSelectTemplate.getStore().setStoreSorter((StoreSorter) storeSorter);
		cbSelectTemplate.getStore().setSortField(PageSelModelData.PROPERTY_NAME);
	}

	/**
	 * Creates the components tree panel for the bound Page.<br/>
	 */
	private void initPageComponentsPanel() {
		compsPanel = PmsFactory.getInstance().getPageComponentsManagement();
		compsPanel.init(pageTemplateDto);
	}

	/**
	 * Inits and adds the page components panel.<br/>
	 */
	protected final void addPageComponentsPanel() {
		initPageComponentsPanel();
		getBottomContainer().add(getCompsPanel());
	}

	/**
	 * Adds a button bar to this topContainer.<br/>
	 */
	protected final void addButtonBar() {

		SelectionListener<ButtonEvent> lSaveDesign = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Listener<MessageBoxEvent> cbConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySavePage(SaveMode.DESIGN);
						}
					}
				};
				if (isDirty()) {
					MessageBox.confirm(guiCommonMessages.headerConfirmWindow(), pmsMessages
						.msgConfirmSavePageAndLayout(), cbConfirm);
				} else {
					tryGetPageLayoutAndDisplayDesign(pageTemplateDto);
				}
			}
		};
		
		SelectionListener<ButtonEvent> lApply = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> cbConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySavePage(SaveMode.APPLY);
						}
					}
				};
				MessageBox.confirm(guiCommonMessages.headerConfirmWindow(), getPmsMessages().msgConfirmSavePage(),
					cbConfirm);
			}
		};

		SelectionListener<ButtonEvent> lSave = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> cbConfirm = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button clicked = we.getButtonClicked();
						if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
							trySavePage(SaveMode.SAVE);
						}
					}
				};
				MessageBox.confirm(guiCommonMessages.headerConfirmWindow(), getPmsMessages().msgConfirmSavePage(),
					cbConfirm);
			}
		};

		List<Component> changeSourceComponents = createChangeSourceComponentsList();

		if (layoutButton) {
			Button bSaveAndDesign = buttonsSupport.createGenericButton(pmsMessages.labelDesign(), pmsMessages
				.ttSaveAndLayoutPage(), pmsStyles.iconDesign(), lSaveDesign);
			addButton(bSaveAndDesign);
			enableDisableDesignButton(bSaveAndDesign);
		}

		bApply = buttonsSupport.createApplyButtonForDetailPanels(this, lApply, changeSourceComponents,
			pmsListeningStrategy);
		addButton(bApply);

		bSave = buttonsSupport.createSaveButtonForDetailPanels(this, lSave, changeSourceComponents,
			pmsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}

	private void enableDisableDesignButton(final Button bSaveAndDesign) {
		if(!isEdition()){
			bSaveAndDesign.setEnabled(false);
		}
		Listener<BaseEvent> lEnableDesign = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent fe) {
				if(tfName.isValid()) {
					bSaveAndDesign.setEnabled(true);
				} else {
					bSaveAndDesign.setEnabled(false);
				}
			}
		};
		tfName.addListener(Events.OnKeyUp, lEnableDesign);
		tfName.addListener(Events.Change, lEnableDesign);
	}

	/**
	 * Inits the change source components list<br/>
	 */
	private List<Component> createChangeSourceComponentsList() {
		List<Component> changeSourceComponents = new LinkedList<Component>();
		changeSourceComponents.add(tfName);
		changeSourceComponents.add(fDescription);
		changeSourceComponents.add(cbSelectTemplate);
		changeSourceComponents.add(compsPanel);

		initSpecificChangeSourceComponents(changeSourceComponents);
		return changeSourceComponents;
	}

	/**
	 * Adds specific change source components to the passed list.<br/>
	 */
	protected void initSpecificChangeSourceComponents(List<Component> changeSourceComponents) {
	}

	/**
	 * Saves the GUI values into the bound DTO and calls <b>save</b> operation on the pages service.<br/>
	 * @param mode "save", "save and close" or "save and design"
	 */
	private void trySavePage(SaveMode mode) {
		mask(pmsMessages.mskSavePage());

		PageTemplateDTO pageTemplate = pageTemplateDto;

		saveCommonFieldsValues();
		saveSpecificFieldsValues();

		// convert to PageDto
		PageDTO dto = pageTemplate.toPageDTO();

		pagesService.save(dto, new SaveCallback(mode));
	}

	/**
	 * Saves, into the bound DTO, the GUI values from the components managed in this class: name, description, template
	 * and the components tree.<br/>
	 */
	private void saveCommonFieldsValues() {

		// components
		List<ComponentInPageTemplateDTO> depth1Components = getCompsPanel().getDepsTree().computeComponentsInPageTree();
		pageTemplateDto.setComponents(depth1Components);

		// template
		ModelData templateModelData = getCbSelectTemplate().getValue();
		PageSelDTO templateDto = null;
		if (templateModelData instanceof PageSelModelData) {
			templateDto = ((PageSelModelData) templateModelData).getDTO();
		}
		pageTemplateDto.setTemplate(templateDto);

		pageTemplateDto.setName(getTfName().getValue());

		// page description
		pageTemplateDto.setDescription(getFDescription().getValue());
	}

	/**
	 * Saves, into the bound DTO, the GUI values present in the implementation specific components.<br/>
	 */
	protected abstract void saveSpecificFieldsValues();

	/**
	 * Async Callback for "save page" operation.<br/> Static inner class implementation for performance purpose.
	 * 
	 * @author Andrei Cojocaru
	 * @author Manuel Ruiz
	 * 
	 */
	private class SaveCallback implements AsyncCallback<PageTemplateDTO> {

		private SaveMode mode = SaveMode.SAVE;

		public SaveCallback(SaveMode mode) {
			super();
			this.mode = mode;
		}

		public void onFailure(Throwable arg0) {
			unmask();
			errorProcessor.processError(arg0, getEmrPages(), pmsMessages.msgErrorSavePage());
		}

		public void onSuccess(PageTemplateDTO arg0) {

			unmask();

			if (mode.equals(SaveMode.DESIGN)) {
				tryGetPageLayoutAndDisplayDesign(arg0);
				pageTemplateDto = arg0;
				displayPageValues(false);
				getCompsPanel().setDirty(false);
				bSave.disable();
				bApply.disable();
			} else if (mode.equals(SaveMode.APPLY)) {
				pageTemplateDto = arg0;
				displayPageValues(false);
				getCompsPanel().setDirty(false);
				bSave.disable();
				bApply.disable();
			} else {
				buttonsSupport.closeActiveWindow();
				util.info(pmsMessages.msgSuccessSavePage());
			}
		}
	}

	/**
	 * Requests the pages service for the layout of the passed page, and shows the Design window with the resulting
	 * layout data.<br/>
	 * 
	 */
	private void tryGetPageLayoutAndDisplayDesign(final PageTemplateDTO page) {

		mask(pmsMessages.mskLoadPageLayout());

		AsyncCallback<LayoutDTO> callback = new AsyncCallback<LayoutDTO>() {
			public void onFailure(Throwable arg0) {
				unmask();
				errorProcessor.processError(arg0, emrPages, getPmsMessages().msgErrorRetrieveLayout());
			}

			public void onSuccess(LayoutDTO layout) {
				Design design = Design.getInstance();
				design.config(page, layout);
				design.show();
				design.maximize();

				unmask();
			}
		};

		PageLoc pageLoc = new PageLoc(page.getPortalId(), page.getDeviceId(), page.getId());
		pagesService.getLayout(pageLoc, callback);
	}

	/**
	 * Displays the bound data values on the GUI.<br/>
	 * 
	 * @param firstTime if <code>true</code>, sets the <i>original value</i> on the components that need it (necessary
	 * for computing the <code>dirty</code> state).
	 */
	private void displayPageValues(boolean firstTime) {
		displayCommonFieldsValues();
		displaySpecificFieldsValues(firstTime);
	}

	/**
	 * Displays, in the GUI components managed by this class, the values stored in the bound DTO: name, description,
	 * template.<br/>
	 */
	private void displayCommonFieldsValues() {
		// name field:
		getTfName().setValue(pageTemplateDto.getName());
		getTfName().setOriginalValue(pageTemplateDto.getName());

		// description field:
		getFDescription().setValue(pageTemplateDto.getDescription());
		getFDescription().setOriginalValue(pageTemplateDto.getDescription());

		// template selector:
		displayPageTemplateValues();
	}

	/**
	 * Displays in GUI the implementation-specific values.<br/>
	 * 
	 * @param firstTime
	 */
	protected abstract void displaySpecificFieldsValues(boolean firstTime);

	/**
	 * Returns the possible templates. Transform them into modeldata
	 * 
	 * @return the possible templates
	 */
	private void populateTemplateSelectorStore() {
		ListStore<ModelData> store = getCbSelectTemplate().getStore();
		store.removeAll();

		// "--no template--" value:
		BaseModelData noTemplate = new BaseModelData();
		noTemplate.set(PageSelModelData.PROPERTY_NAME, getPmsMessages().emptyValueTemplate());

		// available templates values:
		List<Inherited<PageSelDTO>> templatesDto = pageTemplateDto.getTemplates();
		List<ModelData> lModels = new LinkedList<ModelData>();
		lModels.add(noTemplate);
		for (Inherited<PageSelDTO> template : templatesDto) {
			lModels.add(new PageSelModelData(template.getValue()));
		}
		store.add(lModels);
	}

	/**
	 * Displays the bound data values relative to the associated template.<br/>
	 */
	private void displayPageTemplateValues() {

		String propertyValue = getPmsMessages().emptyValueTemplate();
		PageSelDTO currentTemplate = pageTemplateDto.getTemplate();
		if (currentTemplate != null) {
			propertyValue = currentTemplate.getName();
		}

		ListStore<ModelData> store = getCbSelectTemplate().getStore();
		ModelData templateValue = store.findModel(PageSelModelData.PROPERTY_NAME, propertyValue);
		getCbSelectTemplate().setValue(templateValue);
		getCbSelectTemplate().setOriginalValue(templateValue);
	}

	/**
	 * @return the bound Page template DTO
	 */
	protected final PageTemplateDTO getPageTemplate() {
		return pageTemplateDto;
	}

	/**
	 * @return the buttons helper
	 */
	protected Buttons getButtonsSupport() {
		return buttonsSupport;
	}

	/**
	 * @return the forms helper.
	 */
	protected final FormSupport getFormSupport() {
		return formSupport;
	}

	/**
	 * Returns the top container for this widget.<br/> Top container contains the simple fields.
	 * 
	 * @return the top container for the page detail widget
	 */
	protected final LayoutContainer getTopContainer() {
		return topContainer;
	}

	/**
	 * Returns the bottom container for this widget.<br /> Bottom container contains the components in page management
	 * panel.
	 * @return the bottomContainer
	 */
	protected final LayoutContainer getBottomContainer() {
		return bottomContainer;
	}

	/**
	 * @return the emrPages
	 */
	protected final PageErrorMessageResolver getEmrPages() {
		return emrPages;
	}

	/**
	 * Injects the PMS specific messages bundle.
	 * 
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the buttons helper.
	 * 
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * Injects the forms support service.
	 * 
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the Pages service proxy.
	 * 
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
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
	 * 
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * Injects the Pages Error Message Resolver.
	 * 
	 * @param emrPages the emrPages to set
	 */
	@Inject
	public void setEmrPages(PageErrorMessageResolver emrPages) {
		this.emrPages = emrPages;
	}

	/**
	 * @param ep the error processor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor ep) {
		this.errorProcessor = ep;
	}

	public GuiCommonMessages getGuiCommonMessages() {
		return guiCommonMessages;
	}

	/**
	 * Injects the GuiCommon messages bundle.
	 * @param guiCommonMessages
	 */
	@Inject
	public void setGuiCommonMessages(GuiCommonMessages guiCommonMessages) {
		this.guiCommonMessages = guiCommonMessages;
	}

	/**
	 * @return gui common styles
	 */
	protected final GuiCommonStyles getGuiCommonStyles() {
		return guiCommonStyles;
	}

	/**
	 * @param guiCommonStyles
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

	/**
	 * Injects the alphabetic store sorter<br/>
	 * @param storeSorter
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}

	/**
	 * @return the pmsStyles
	 */
	public PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @param pmsStyles the pmsStyles to set
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param layoutButton the layoutButton to set
	 */
	public void setLayoutButton(boolean layoutButton) {
		this.layoutButton = layoutButton;
	}
}
