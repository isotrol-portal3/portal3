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

package com.isotrol.impe3.pms.gui.client.widget;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.PropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.inject.Inject;
import com.isotrol.impe3.api.StringItemStyle;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.util.databinding.DataBoundSupport;
import com.isotrol.impe3.gui.common.util.databinding.IDataBound;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.config.ChoiceItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemType;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateItemDTO;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ChoiceItemModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.CategoryTreeStore;
import com.isotrol.impe3.pms.gui.client.util.ConfigurationFileUploadField;


/**
 * Fieldset that manages the configuration template items for Connectors and portal components. <dl> <dt><b>Forwarded
 * Events:</b></dt>
 * 
 * <dd><b>Select</b> : Forwarded from ComboBox. </dd> <dd><b>Submit</b> : Forwarded from FileUpload. </dd>
 * <dd><b>OnKeyUp</b> : Forwarded from TextFields. </dd>
 * 
 * <dd><b>Change</b> : BaseEvent(eventType, ComponentInPageNameEditor)<br> <div> Forwarded from TextFields and other
 * fields.</div> <ul> <li>eventType : {@link Events#Change}.</li> <li>ComponentInPageNameEditor : <code>this</code></li>
 * </ul> </dd> </dl>
 * 
 * @author Manuel Ruiz
 * @author Andrei Cojocaru
 * 
 */
public class ConfigurationPanel extends TabItem implements IDetailPanel, IDataBound<ConfigurationTemplateDTO> {

	/**
	 * Main container. Directly contained in the tab item.<br/> Every inner item must be attached to this container.
	 */
	private LayoutContainer container = null;

	/**
	 * Name of the graphic resource used as information icon.<br/>
	 */
	private static final String INFO_ICON = "icon_info.gif";
	/**
	 * Name of the graphic resource used as indicator icon for a ZIP bundle.<br/>
	 */
	private static final String BUNDLE_ICON = "icon_bundle.gif";

	/**
	 * Support for data binding.<br/>
	 */
	private DataBoundSupport<ConfigurationTemplateDTO> dataBoundSupport = null;

	/**
	 * The configuration map.<br/> Keys are configuration template DTOs, and values are the GUI fields that control
	 * them.
	 */
	private Map<ConfigurationTemplateItemDTO, Field<?>> confMap = null;

	/*
	 * 'name' and 'id' fields descriptors in FileUploads results.
	 */
	/**
	 * <b>name</b> field in the JSON object that returned from a successful file upload.<br/>
	 */
	private static final String KEY_NAME = "name";
	/**
	 * <b>ID</b> field in the JSON object that returned from a successful file upload.<br/>
	 */
	private static final String KEY_ID = "id";

	/**
	 * Listener that forwards events when fired.<br/>
	 */
	private Listener<FieldEvent> forwardChangeEventListener = null;
	/**
	 * Category selector window.<br/>
	 */
	private Window wCategories = null;

	/** the category tree selection change listener */
	private SelectionChangedListener<CategorySelModelData> catTreeSelectionListener;

	/*
	 * Injected deps
	 */
	/**
	 * PMS pmsSettings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles service.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Forms helper service.<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * PMS specific styles bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Validates non null strings.<br/>
	 */
	private NonEmptyStringValidator nonNullStringsValidator = null;

	/**
	 * Field validator listener.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * The templates combo store sorter<br/>
	 */
	private AlphabeticalStoreSorter storeSorter = null;

	/**
	 * Unique allowed constructor.<br/>
	 */
	public ConfigurationPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependencies injection.
	 * @param data the bound configuration template
	 */
	public void init(ConfigurationTemplateDTO data) {
		confMap = new HashMap<ConfigurationTemplateItemDTO, Field<?>>();
		dataBoundSupport = new DataBoundSupport<ConfigurationTemplateDTO>(data);

		initListeners();
		initComponents();
	}

	/**
	 * Inits listeners declared as this class fields.<br/>
	 */
	private void initListeners() {
		forwardChangeEventListener = new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				be.setComponent(ConfigurationPanel.this);
				ConfigurationPanel.this.fireEvent(be.getType(), be);
			}
		};
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		setLayout(new FitLayout());
		setText(pmsMessages.configurationWidgetTitle());
		setScrollMode(Scroll.AUTOY);

		container = new LayoutContainer(formSupport.getStandardLayout());
		container.addStyleName(styles.margin10px());
		container.setAutoHeight(true);
		add(container);

		addFormFields();
	}

	/**
	 * Adds the corresponding field, for each of the configuration items of the bound configuration template.<br/>
	 */
	private void addFormFields() {

		ConfigurationTemplateDTO ctDto = getBoundData();

		if (ctDto != null) { // may be null
			List<ConfigurationTemplateItemDTO> confItems = ctDto.getItems();

			for (ConfigurationTemplateItemDTO item : confItems) {

				Field<?> field = null;

				ConfigurationItemType type = item.getType();

				if (ConfigurationItemType.STRING.equals(type)) {
					field = addTextField(item);
				} else if (ConfigurationItemType.INTEGER.equals(type)) {
					field = addNumericalField(item);
				} else if (ConfigurationItemType.FILE.equals(type)) {
					field = addFileUploadField(item);
				} else if (ConfigurationItemType.CONTENT_TYPE.equals(type)) {
					field = addContentTypeComboBox(item);
				} else if (ConfigurationItemType.CATEGORY.equals(type)) {
					field = addCategoryField(item);
				} else if (ConfigurationItemType.BOOLEAN.equals(type)) {
					field = addBooleanField(item);
				} else if (ConfigurationItemType.CHOICE.equals(type)) {
					field = addSelectionField(item);
				}

				confMap.put(item, field);

				configFieldListeners(field);
			}
		}
	}

	/**
	 * Associates the passed field with a listener that validates on <b>render</b>, and a listener that forwards
	 * <b>Change</b> events<br/>
	 * 
	 * @param field
	 */
	private void configFieldListeners(Field<?> field) {
		// validate field on after render:
		field.addListener(Events.Render, validatorListener);
		// forward events:
		if (field instanceof ComboBox<?>) {
			field.addListener(Events.Select, forwardChangeEventListener);
		} else if (field instanceof FileUploadField) {
			field.addListener(Events.Submit, forwardChangeEventListener);
		} else if (field instanceof TextField<?>) {
			field.addListener(Events.OnKeyUp, forwardChangeEventListener); // regular TextFields
			field.addListener(Events.Change, forwardChangeEventListener); // TextFields and FileUploads
		} else {
			field.addListener(Events.Change, forwardChangeEventListener);
		}
	}

	/**
	 * Adds a text field, for a configuration item of type {@link ConfigurationItemType#STRING}.<br/>
	 * @param item the configuration template item DTO.
	 * @return added field.
	 */
	private Field<?> addTextField(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);
		LayoutContainer left = lr[0];
		LayoutContainer right = lr[1];

		final TextField<String> field = new TextField<String>();
		field.setFieldLabel(item.getName());
		// check if type="password"
		if (item.getStringStyle().equals(StringItemStyle.PASSWORD)) {
			field.setPassword(true);
		}

		String value = item.getString();
		field.setOriginalValue(value);
		field.setValue(value);

		// validation config:
		if (item.isRequired()) {
			field.setAllowBlank(false);
			field.setValidator(nonNullStringsValidator);
			field.getMessages().setInvalidText(messages.vmRequired());
			field.getMessages().setBlankText(messages.vmRequired());
		}
		field.setAutoValidate(true);
		field.setValidateOnBlur(true);
		field.setName(item.getKey());
		left.add(field);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, right, INFO_ICON);

		return field;
	}

	/**
	 * Adds a numerical field, for a configuration item of type {@link ConfigurationItemType#INTEGER}<br/>
	 * @param item the configuration template item DTO.
	 * @return the added field.
	 */
	private Field<?> addNumericalField(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		NumberField field = new NumberField();
		field.setPropertyEditorType(Integer.class);
		field.setFieldLabel(item.getName());

		Integer value = item.getInteger();
		field.setValue(value);
		field.updateOriginalValue(value);

		field.setAllowBlank(!item.isRequired());
		field.setAutoValidate(true);
		field.setValidateOnBlur(true);
		field.setName(item.getKey());
		lr[0].add(field);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, lr[1], INFO_ICON);

		return field;
	}

	/**
	 * Adds a file upload field, for a configuration template item of type {@link ConfigurationItemType#FILE}<br/>
	 * @param item the configuration template item dto
	 * @return the added file upload field.
	 */
	private Field<?> addFileUploadField(final ConfigurationTemplateItemDTO item) {

		String url = null;
		if (GWT.isScript()) {
			url = Util.getBaseApplicationContext() + pmsSettings.fileUploadUrlWebMode();
		} else {
			url = pmsSettings.fileUploadUrlHostedMode();
		}

		final FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setBorders(false);
		formPanel.setBodyBorder(false);
		formPanel.setLayout(new FitLayout());
		formPanel.addStyleName("config-form");

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(formPanel);
		LayoutContainer pLeft = lr[0];

		final LabelField lUploadStatus = new LabelField();
		lUploadStatus.addStyleName(styles.labelInfoMessage());
		formPanel.add(lUploadStatus);

		formPanel.setAction(url);
		formPanel.setMethod(Method.POST);
		formPanel.setEncoding(FormPanel.Encoding.MULTIPART);

		final ConfigurationFileUploadField fileUpload = new ConfigurationFileUploadField();
		fileUpload.setName(pmsSettings.fileUploadFieldName());
		fileUpload.setFieldLabel(item.getName());
		fileUpload.setAllowBlank(!item.isRequired());
		pLeft.add(fileUpload);

		final UploadedFileDTO fileDto = item.getFile();
		if (fileDto != null) {
			fileUpload.setOriginalFile(fileDto);
		}

		HorizontalPanel pRight = (HorizontalPanel) lr[1];
		pRight.setVerticalAlign(VerticalAlignment.MIDDLE);

		if (item.isBundle()) {
			formSupport.addIcon(pmsMessages.labelZipPackage(), pRight, BUNDLE_ICON);
		}

		// upload file button
		Button bSubmit = new Button(pmsMessages.labelUpload(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				lUploadStatus.setText(pmsMessages.labelUpoading());
				formPanel.submit();
			}
		});
		bSubmit.addStyleName(styles.marginLeft5px());
		pRight.add(bSubmit);

		final IconButton bDownload = new IconButton(pmsStyles.iconDownloadFile());
		// delete file button
		SelectionListener<IconButtonEvent> lTrash = new SelectionListener<IconButtonEvent>() {
			public void componentSelected(IconButtonEvent ce) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent be) {
						if (be.getButtonClicked().getItemId().equals(Dialog.YES)) { // confirmed
							fileUpload.setCurrentFile(null);
							fireFileUploadChangeEvent(fileUpload, item, null);
							bDownload.disable();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteFile(), listener)
					.setModal(true);
			}
		};
		IconButton bTrash = buttonsSupport.addTrashIconButton(pRight, lTrash);
		bTrash.addStyleName(styles.marginLeft5px());

		// download button
		bDownload.setToolTip(messages.btDownload());
		bDownload.addStyleName(styles.marginLeft5px());
		bDownload.addSelectionListener(new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				if (fileDto != null) {
					tryDownloadFile(fileUpload.getCurrentFile());
				}
			}
		});
		pRight.add(bDownload);
		if (fileDto == null) {
			bDownload.disable();
		}

		container.add(formPanel);

		formPanel.addListener(Events.Submit, new Listener<FormEvent>() {
			public void handleEvent(FormEvent be) {
				JSONObject fileJson = null;
				try {
					fileJson = (JSONObject) JSONParser.parseStrict(be.getResultHtml()).isObject();
				} catch (JSONException jsone) {
					// TODO review this. Should always receive a JSON object.
					lUploadStatus.setText(pmsMessages.msgErrorUpload());
				}
				UploadedFileDTO fileDto = null;
				if (fileJson != null) {
					JSONString id = null;
					if (fileJson.containsKey(KEY_ID)) {
						id = (JSONString) fileJson.get(KEY_ID);
					}
					JSONString name = null;
					if (fileJson.containsKey(KEY_NAME)) {
						name = (JSONString) fileJson.get(KEY_NAME);
					}
					if (id != null && name != null) {
						fileDto = new UploadedFileDTO();
						fileDto.setId(id.stringValue());
						fileDto.setName(name.stringValue());
					}
				}
				String statusLabelText = null;
				if (fileDto != null) {
					onFileUploaded(fileUpload, item, fileDto);
					statusLabelText = pmsMessages.msgSuccessUpload(fileDto.getName());
					bDownload.enable();
				} else { // error
					statusLabelText = pmsMessages.msgErrorUpload();
					bDownload.disable();
				}
				lUploadStatus.setText(statusLabelText);
			}
		});

		return fileUpload;
	}

	/**
	 * Method called when a file upload is successful.<br/> In order to get {@link #isDirty()} working, fires a Change
	 * event on the FileUpload field.
	 */
	private void onFileUploaded(ConfigurationFileUploadField field, ConfigurationTemplateItemDTO item,
		UploadedFileDTO fileDto) {

		field.setCurrentFile(fileDto);

		// fire change event:
		fireFileUploadChangeEvent(field, item, fileDto);
	}

	/**
	 * Forces the passed FileUploadField to fire a Submit event.<br/> (Change event already has semantics for file
	 * upload fields.)
	 * 
	 * @param field field that will fire the event.
	 * @param item associated item.
	 * @param fileDto the uploaded file DTO.
	 */
	private void fireFileUploadChangeEvent(FileUploadField field, ConfigurationTemplateItemDTO item,
		UploadedFileDTO fileDto) {

		// current value:
		String value = null;
		if (fileDto != null) {
			value = fileDto.getName();
		}
		// old value:
		UploadedFileDTO oldFile = item.getFile();
		String oldValue = null;
		if (oldFile != null) {
			oldValue = oldFile.getName();
		}

		FieldEvent fe = new FieldEvent(field);
		fe.setOldValue(oldValue);
		fe.setValue(value);
		fe.setType(Events.Submit);
		field.fireEvent(Events.Submit, fe);
	}

	private void tryDownloadFile(UploadedFileDTO fileDto) {

		if (fileDto != null) {
			String url = Util.getBaseApplicationContext() + pmsSettings.fileDownloadUrl() + fileDto.getId();
			fireDownloadFileClickEvent(url);
		}
	}

	private native void fireDownloadFileClickEvent(String url)
	/*-{
	  $doc.location.href = url;
	}-*/;

	/**
	 * Adds a content type combo, for a configuration template item of type {@link ConfigurationItemType#CONTENT_TYPE}
	 * <br/>
	 * @param item configuration template item DTO.
	 * @return the added field.
	 */
	private Field<?> addContentTypeComboBox(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		ComboBox<ModelData> combo = new ComboBox<ModelData>();
		combo.setEditable(false);
		combo.setFieldLabel(item.getName());
		combo.setDisplayField(ContentTypeSelModelData.PROPERTY_NAME);
		combo.setTriggerAction(TriggerAction.ALL);

		boolean required = item.isRequired();
		combo.setAllowBlank(!required);
		combo.setValidateOnBlur(true);

		// store config:
		ListStore<ModelData> store = new ListStore<ModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ContentTypeSelModelData.PROPERTY_NAME);
		List<ContentTypeSelDTO> contenttypesDto = getBoundData().getContentTypes();
		List<ModelData> lModelData = new LinkedList<ModelData>();

		if (!required) {
			// no content type option
			BaseModelData noContentType = new BaseModelData();
			noContentType.set(ContentTypeSelModelData.PROPERTY_NAME, pmsMessages.emptyValueContentType());
			lModelData.add(noContentType);
			combo.setValue(noContentType);
		}

		if (contenttypesDto != null) {
			for (ContentTypeSelDTO contentTypeDto : contenttypesDto) {
				lModelData.add(new ContentTypeSelModelData(contentTypeDto));
			}
		}
		store.add(lModelData);
		combo.setStore(store);

		ContentTypeSelDTO currentCt = item.getContentType();
		if (currentCt != null) {
			combo.disableEvents(true);
			ModelData currentModelData = store.findModel(ContentTypeSelModelData.PROPERTY_NAME, currentCt.getName());
			combo.setValue(currentModelData);
			combo.enableEvents(true);
		}

		lr[0].add(combo);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, lr[1], INFO_ICON);

		return combo;
	}

	/**
	 * Adds a Category field, for a passed configuration template item of type {@link ConfigurationItemType#CATEGORY}
	 * .<br/>
	 * @param item
	 * @return
	 */
	private Field<?> addCategoryField(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);
		LayoutContainer pLeft = lr[0], pRight = lr[1];

		final ConfigurationCategoryField tfCategory = new ConfigurationCategoryField();
		formSupport.configReadOnly(tfCategory);

		tfCategory.setFieldLabel(item.getName());
		CategorySelDTO csDto = item.getCategory();
		// value binding:
		CategorySelModelData csModelData = null;
		if (csDto != null) {
			csModelData = new CategorySelModelData(item.getCategory());
		}
		tfCategory.setValue(csModelData);
		tfCategory.updateOriginalValue(csModelData);
		tfCategory.setAllowBlank(!item.isRequired());

		pLeft.add(tfCategory);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, pRight, INFO_ICON);

		SelectionListener<IconButtonEvent> lEdit = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				showCategorySelector(item);
			}
		};
		buttonsSupport.addEditionIconButton(pRight, lEdit);

		SelectionListener<IconButtonEvent> lTrash = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				CategorySelModelData oldValue = tfCategory.getValue();
				tfCategory.setValue(null);
				// setValue() does not fire events: we do it manually
				fireChangeEvent(tfCategory, null, oldValue);
			}
		};
		buttonsSupport.addTrashIconButton(pRight, lTrash);

		return tfCategory;
	}

	private void createCategoryTreeSelectionChangedListener(final ConfigurationTemplateItemDTO item) {
		catTreeSelectionListener = new SelectionChangedListener<CategorySelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<CategorySelModelData> se) {
				CategorySelModelData modelData = se.getSelectedItem();
				if (modelData != null) {
					item.setCategory(modelData.getDTO());
					Field<CategorySelModelData> tfCategory = (Field<CategorySelModelData>) confMap.get(item);
					CategorySelModelData oldValue = tfCategory.getValue();
					tfCategory.setValue(modelData);
					fireChangeEvent(tfCategory, modelData, oldValue);
					wCategories.hide();
				}
			}
		};
	}

	/**
	 * Creates a FieldEvent with the passed data and calls fireEvent on the passed Field.<br/> Used for fields that do
	 * not fire {@link Events#Change}.
	 * 
	 * @param field
	 * @param value
	 * @param oldValue
	 */
	private void fireChangeEvent(Field<?> field, Object value, Object oldValue) {
		FieldEvent fe = new FieldEvent(field);
		fe.setValue(value);
		fe.setOldValue(oldValue);
		field.fireEvent(Events.Change, fe);
	}

	/**
	 * Shows a Category selector for the passed item.<br/>
	 * @param item configuration template item DTO. Must be of type {@link ConfigurationItemType#CATEGORY}
	 */
	private void showCategorySelector(final ConfigurationTemplateItemDTO item) {

		wCategories = new Window();
		wCategories.setHeading("Categorías");
		wCategories.setScrollMode(Scroll.AUTO);
		wCategories.setSize(500, 550);
		wCategories.setClosable(true);
		wCategories.setModal(true);
		wCategories.setShadow(false);

		TreeStore<CategorySelModelData> treeStore = new CategoryTreeStore(getBoundData().getCategories());
		final TreePanel<CategorySelModelData> tree = new TreePanel<CategorySelModelData>(treeStore);
		tree.setDisplayProperty(CategorySelModelData.PROPERTY_NAME);

		tree.getStyle().setLeafIcon(IconHelper.create(pmsStyles.iconTreeFolder()));

		// the SelectionChangedListener to be added to the category tree's selection model
		createCategoryTreeSelectionChangedListener(item);
		tree.getSelectionModel().addSelectionChangedListener(catTreeSelectionListener);
		wCategories.add(tree);
		wCategories.show();

		selectTreeItem(tree, item);
	}

	/**
	 * Selects the TreeItem corresponding to passesd configuration item DTO.<br/>
	 * 
	 * @param tree
	 * @param item
	 */
	private void selectTreeItem(TreePanel<CategorySelModelData> tree, ConfigurationTemplateItemDTO item) {

		tree.expandAll();

		CategorySelDTO csDto = item.getCategory();
		if (csDto != null) {
			CategorySelModelData model = (CategorySelModelData) tree.getStore().findModel(
				CategorySelModelData.PROPERTY_ID, csDto.getId());

			tree.getSelectionModel().removeSelectionListener(catTreeSelectionListener);
			tree.getSelectionModel().select(model, false);
			tree.getSelectionModel().addSelectionChangedListener(catTreeSelectionListener);
		} else {
			tree.getSelectionModel().removeSelectionListener(catTreeSelectionListener);
			tree.getSelectionModel().deselectAll();
			tree.getSelectionModel().addSelectionChangedListener(catTreeSelectionListener);
		}
	}

	/**
	 * Creata a boolean form field (radio button YES/NO)
	 * 
	 * @param item
	 * @return the boolean form field
	 */
	private Field<?> addBooleanField(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		final RadioGroup field = new RadioGroup();
		field.setOrientation(Orientation.HORIZONTAL);
		field.setFieldLabel(item.getName());

		final Radio yesOption = new Radio();
		yesOption.setBoxLabel(pmsMessages.valueYes());
		yesOption.setItemId(Dialog.YES);
		field.add(yesOption);

		final Radio noOption = new Radio();
		noOption.setBoxLabel(pmsMessages.valueNo());
		noOption.setItemId(Dialog.NO);
		field.add(noOption);

		Radio nothingOption = null;
		if (!item.isRequired()) {
			nothingOption = new Radio();
			nothingOption.setBoxLabel(pmsMessages.valueNone());
			nothingOption.setItemId(Dialog.CANCEL);
			field.add(nothingOption);
		}

		field.setName(item.getKey());

		// set original value:
		Radio value = null;
		if (item.getBoolean() != null) {
			if (item.getBoolean()) {
				value = yesOption;
			} else {
				value = noOption;
			}
		} else if (!item.isRequired()) {
			value = nothingOption;
		} else {
			value = noOption;
		}

		field.setValue(value);
		field.updateOriginalValue(value);

		lr[0].add(field);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, lr[1], INFO_ICON);

		return field;
	}

	/**
	 * Adds a combo with the values of a enum type, for a configuration template item of type
	 * {@link ConfigurationItemType#CHOICE} <br/>
	 * @param item configuration template item DTO.
	 * @return the added field.
	 */
	private Field<?> addSelectionField(final ConfigurationTemplateItemDTO item) {

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(container);

		ComboBox<ModelData> combo = new ComboBox<ModelData>();
		combo.setEditable(false);
		combo.setFieldLabel(item.getName());
		combo.setDisplayField(ChoiceItemModelData.PROPERTY_NAME);
		combo.setTriggerAction(TriggerAction.ALL);

		boolean required = item.isRequired();
		combo.setAllowBlank(!required);
		combo.setValidateOnBlur(true);

		// store config:
		ListStore<ModelData> store = new ListStore<ModelData>();
		store.setStoreSorter((StoreSorter) storeSorter);
		store.setSortField(ChoiceItemModelData.PROPERTY_NAME);
		List<ChoiceItemDTO> choices = item.getChoices();
		List<ModelData> lModelData = new LinkedList<ModelData>();

		if (!required) {
			// no content type option
			BaseModelData noChoice = new BaseModelData();
			noChoice.set(ChoiceItemModelData.PROPERTY_NAME, pmsMessages.emptyValueChoice());
			lModelData.add(noChoice);
			combo.setValue(noChoice);
		}

		if (choices != null) {
			for (ChoiceItemDTO choiceDto : choices) {
				lModelData.add(new ChoiceItemModelData(choiceDto));
			}
		}
		store.add(lModelData);
		combo.setStore(store);

		String currentChoice = item.getChoice();
		if (currentChoice != null) {
			combo.disableEvents(true);
			ModelData currentModelData = store.findModel(ChoiceItemModelData.PROPERTY_KEY, currentChoice);
			combo.setValue(currentModelData);
			combo.enableEvents(true);
		}

		lr[0].add(combo);

		// info icon
		String itemDescription = item.getDescription();
		formSupport.addIcon(itemDescription, lr[1], INFO_ICON);

		return combo;
	}

	/**
	 * Returns the boolean value according to the selected option in the radio group.<br/>
	 * @param group
	 * @return <code>true</code>, if the selection of the passed group represents the value <b>Yes</b>;
	 * <code>false</code>, if the selected radio in the passed group represents the value <b>No</b>.
	 */
	private Boolean resolveBoolean(RadioGroup group) {
		String valueId = group.getValue().getItemId();
		Boolean res = null;
		if (valueId.equals(Dialog.YES)) {
			res = Boolean.TRUE;
		} else if (valueId.equals(Dialog.NO)) {
			res = Boolean.FALSE;
		}
		return res;
	}

	/**
	 * delegates to same method on {@link #dataBoundSupport}
	 * 
	 * @return
	 * @see com.isotrol.impe3.gui.common.util.databinding.IDataBound#getBoundData()
	 */
	public ConfigurationTemplateDTO getBoundData() {
		return dataBoundSupport.getBoundData();
	}

	/**
	 * Applies GUI values to bound DTO, if rendered, or does nothing otherwise
	 */
	public void applyValues() {
		if (isRendered()) {
			applyGuiValues();
		}
	}

	/**
	 * Inserts values from GUI objects to bound DTO.<br/>
	 * 
	 * <b>Precondition:</b> widget must be rendered.
	 */
	private void applyGuiValues() {
		assert isRendered() : getClass().getName() + "#applyGuiValues(): widget must be rendered when called!";

		for (Map.Entry<ConfigurationTemplateItemDTO, Field<?>> entry : this.confMap.entrySet()) {
			ConfigurationTemplateItemDTO iDto = entry.getKey();
			Field<?> field = entry.getValue();
			Object value = field.getValue();

			ConfigurationItemType iType = iDto.getType();

			if (iType.equals(ConfigurationItemType.CONTENT_TYPE)) {
				if (value instanceof ContentTypeSelModelData && value != null) {
					ContentTypeSelModelData ctsModelData = (ContentTypeSelModelData) value;
					iDto.setContentType(ctsModelData.getDTO());
					((ComboBox<ContentTypeSelModelData>) field).updateOriginalValue(ctsModelData);
				} else {
					iDto.setContentType(null);
					field.updateOriginalValue(null);
				}

			} else if (iType.equals(ConfigurationItemType.INTEGER)) {
				iDto.setInteger((Integer) value);
				((NumberField) field).updateOriginalValue((Integer) value);

			} else if (iType.equals(ConfigurationItemType.STRING)) {
				iDto.setString((String) value);
				((TextField<String>) field).updateOriginalValue((String) value);

			} else if (iType.equals(ConfigurationItemType.BOOLEAN)) {
				iDto.setBoolean(resolveBoolean((RadioGroup) field));
				Radio res = ((RadioGroup) field).getValue();
				((RadioGroup) field).updateOriginalValue(res);

			} else if (iType.equals(ConfigurationItemType.FILE)) {
				ConfigurationFileUploadField fileUploadField = (ConfigurationFileUploadField) field;
				iDto.setFile(fileUploadField.getCurrentFile());
				fileUploadField.setOriginalFile(fileUploadField.getCurrentFile());

			} else if (iType.equals(ConfigurationItemType.CATEGORY)) {
				CategorySelModelData catModelData = (CategorySelModelData) value;
				CategorySelDTO catDto = null;
				if (catModelData != null) {
					catDto = catModelData.getDTO();
				}
				iDto.setCategory(catDto);
				ConfigurationCategoryField catField = (ConfigurationCategoryField) field;
				catField.updateOriginalValue(catModelData);

			} else if (iType.equals(ConfigurationItemType.CHOICE)) {
				if (value instanceof ChoiceItemModelData && value != null) {
					ChoiceItemModelData modelData = (ChoiceItemModelData) value;
					iDto.setChoice((String) modelData.getDTO().getKey());
					((ComboBox<ModelData>) field).updateOriginalValue(modelData);
				} else {
					iDto.setChoice(null);
					field.updateOriginalValue(null);
				}
			}
		}
	}

	/**
	 * Validates input fields values.<br/>
	 * 
	 * @return <code>true</code>, if input fields values are valid; <code>false</code>, otherwise.
	 */
	public boolean isValid() {
		Iterator<Entry<ConfigurationTemplateItemDTO, Field<?>>> it = confMap.entrySet().iterator();
		boolean valid = true;
		while (valid && it.hasNext()) {
			Entry<ConfigurationTemplateItemDTO, Field<?>> current = it.next();
			Field<?> field = current.getValue();
			if (field.isRendered()) { // validate GUI data:
				valid = valid && field.isValid();
			} else { // validate bound itemDTO data:
				ConfigurationTemplateItemDTO itemDto = current.getKey();
				boolean required = itemDto.isRequired();
				valid = valid && (!required || required && valueOf(itemDto) != null);
			}
		}
		return valid;
	}

	/**
	 * Returns the current value of the passed item, according to its type.<br/>
	 * @param item
	 * @return
	 */
	public Object valueOf(ConfigurationTemplateItemDTO item) {
		Object val = null;
		ConfigurationItemType type = item.getType();
		if (ConfigurationItemType.STRING.equals(type)) {
			val = item.getString();
		} else if (ConfigurationItemType.INTEGER.equals(type)) {
			val = item.getInteger();
		} else if (ConfigurationItemType.FILE.equals(type)) {
			val = item.getFile();
		} else if (ConfigurationItemType.CONTENT_TYPE.equals(type)) {
			val = item.getContentType();
		} else if (ConfigurationItemType.CATEGORY.equals(type)) {
			val = item.getCategory();
		} else if (ConfigurationItemType.BOOLEAN.equals(type)) {
			val = item.getBoolean();
		}
		return val;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		// return fieldsSupport.isDirty();
		boolean dirty = false;
		Iterator<Field<?>> it = confMap.values().iterator();
		while (!dirty && it.hasNext()) {
			Field<?> current = it.next();
			dirty = dirty || current.isDirty();
		}
		return dirty;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.gui.common.util.databinding.IDataBound#setBoundData(java.lang.Object)
	 */
	public void setBoundData(ConfigurationTemplateDTO data) {
		DataBoundSupport.throwIllegalBoundDataSettingException(this.getClass());
	}

	/**
	 * Injects the generic messages bundle
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * Injects the PMS messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the generic styles bundle.
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * Injects the buttons helper.
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
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
	 * Injects the PMS specific styles bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * Injects the PMS specific pmsSettings bundle.
	 * @param pmsSettings
	 */
	@Inject
	public void setSettigns(PmsSettings settigns) {
		this.pmsSettings = settigns;
	}

	/**
	 * Injects the string validator.
	 * @param nonNullStringsValidator the nonNullStringsValidator to set
	 */
	@Inject
	public void setNonNullStringsValidator(NonEmptyStringValidator nonNullStringsValidator) {
		this.nonNullStringsValidator = nonNullStringsValidator;
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
	 * A TextField bound to CategorySelModelData type.<br/> Its constructor automatically associates it to a
	 * PropertyEditor that returns its bound value.
	 * 
	 * @author Andrei Cojocaru
	 * 
	 */
	private static class ConfigurationCategoryField extends TextField<CategorySelModelData> {

		/**
		 * Default constructor.
		 */
		public ConfigurationCategoryField() {
			setPropertyEditor(new PropertyEditor<CategorySelModelData>() {

				public String getStringValue(CategorySelModelData value) {
					if (value == null) {
						return "";
					}
					return value.getDTO().getName();
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see com.extjs.gxt.ui.client.widget.form.PropertyEditor#convertStringValue(java.lang.String)
				 */
				/**
				 * Returns the real value.<br/>
				 */
				public CategorySelModelData convertStringValue(String value) {
					return ConfigurationCategoryField.this.value;
				}
			});
		}
	}

	/**
	 * @param storeSorter the storeSorter to set
	 */
	@Inject
	public void setStoreSorter(AlphabeticalStoreSorter storeSorter) {
		this.storeSorter = storeSorter;
	}
}
