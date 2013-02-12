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

import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ConnectorsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;


/**
 * Window to import a connectors impe file.
 * 
 * @author Manuel Ruiz
 * 
 */
public class ConnectorsImportWindow extends TypicalWindow implements IDetailPanel {

	/**
	 * <b>name</b> field in the JSON object that returned from a successful file upload.<br/>
	 */
	private static final String KEY_NAME = "name";
	/**
	 * <b>ID</b> field in the JSON object that returned from a successful file upload.<br/>
	 */
	private static final String KEY_ID = "id";

	/**
	 * Main container.<br/>
	 */
	private LayoutContainer container = null;
	/*
	 * form fields
	 */
	/**
	 * "Override" flag field.<br/>
	 */
	private CheckBox cbOverride = null;

	/**
	 * fields that may fire Change events.<br/>
	 */
	private List<Field<?>> fields = null;

	/**
	 * Uploaded file id
	 */
	private String fileId = null;

	/*
	 * Injected deps
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/**
	 * Error message resolver for connectors.<br/>
	 */
	private ConnectorsErrorMessageResolver errorMessageResolver = null;

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
	 * Forms helper object<br/>
	 */
	private FormSupport formSupport = null;

	/**
	 * GuiCommon styles service
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Shared objects container.<br/>
	 */
	private Util util = null;

	/**
	 * PMS events listening strategy.<br/>
	 */
	private IComponentListeningStrategy pmsListeningStrategy = null;

	/**
	 * PMS pmsSettings bundle.<br/>
	 */
	private PmsSettings pmsSettings = null;

	/**
	 * Connectors async service
	 */
	private IConnectorsServiceAsync connectorsService = null;

	/**
	 * Constructor.<br/>
	 * 
	 */
	public ConnectorsImportWindow() {
	}

	/**
	 * (non-Javadoc)
	 * @see com.extjs.gxt.ui.client.widget.Component#beforeRender()
	 */
	@Override
	protected void beforeRender() {
		initThis();
		initComponent();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponent() {

		container = new LayoutContainer(formSupport.getStandardLayout(false));
		container.setBorders(false);
		container.addStyleName(styles.margin10px());
		add(container);

		addFormFields();
		addButtonBar();
	}

	/**
	 * Inits this component properties.<br/>
	 */
	private void initThis() {
		setModal(true);
		setWidth(525);
		setAutoHeight(true);
		setHeading(pmsMessages.headerConnectorsImport());
		setClosable(false);
		setShadow(false);
	}

	private void addFormFields() {

		// file upload field
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

		LayoutContainer[] lr = formSupport.addFieldContainerSkeleton(formPanel, false, 250);
		LayoutContainer pLeft = lr[0];

		final LabelField lUploadStatus = new LabelField();
		lUploadStatus.addStyleName(styles.labelInfoMessage());
		formPanel.add(lUploadStatus);

		formPanel.setAction(url);
		formPanel.setMethod(Method.POST);
		formPanel.setEncoding(FormPanel.Encoding.MULTIPART);

		final FileUploadField fileUpload = new FileUploadField();
		fileUpload.setName(pmsSettings.fileUploadFieldName());
		fileUpload.setFieldLabel(pmsMessages.labelFile());
		fileUpload.setAllowBlank(false);
		pLeft.add(fileUpload);

		HorizontalPanel pRight = (HorizontalPanel) lr[1];
		pRight.setVerticalAlign(VerticalAlignment.MIDDLE);

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
					onFileUploaded(fileUpload, fileDto);
					statusLabelText = pmsMessages.msgSuccessUpload(fileDto.getName());
				} else { // error
					statusLabelText = pmsMessages.msgErrorUpload();
				}
				lUploadStatus.setText(statusLabelText);
			}
		});

		// CheckBox 'override content types'
		cbOverride = new CheckBox();
		cbOverride.addInputStyleName(styles.checkBoxAlignLeft());
		cbOverride.setFieldLabel(pmsMessages.labelOverrideContentTypes());
		container.add(cbOverride);

		fields = Arrays.asList(new Field<?>[] {fileUpload});
	}

	private void tryImportConnectors() {
		mask(pmsMessages.mskConnectorsImport());

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				unmask();
				hide();
			}

			public void onFailure(Throwable caught) {
				unmask();
				errorProcessor.processError(caught, errorMessageResolver, pmsMessages.msgErrorImportConnectors());
			}
		};
		connectorsService.importConnectors(fileId, cbOverride.getValue(), callback);
	}

	/**
	 * Method called when a file upload is successful.<br/> In order to get {@link #isDirty()} working, fires a Change
	 * event on the FileUpload field.
	 */
	private void onFileUploaded(FileUploadField field, UploadedFileDTO fileDto) {

		fileId = fileDto.getId();

		// fire change event:
		fireFileUploadChangeEvent(field, fileDto);
	}

	/**
	 * Forces the passed FileUploadField to fire a Submit event.<br/> (Change event already has semantics for file
	 * upload fields.)
	 * 
	 * @param field field that will fire the event.
	 * @param item associated item.
	 * @param fileDto the uploaded file DTO.
	 */
	private void fireFileUploadChangeEvent(FileUploadField field, UploadedFileDTO fileDto) {

		// current value:
		String value = null;
		if (fileDto != null) {
			value = fileDto.getName();
		}

		FieldEvent fe = new FieldEvent(field);
		// fe.setOldValue(oldValue);
		fe.setValue(value);
		fe.setType(Events.Submit);
		field.fireEvent(Events.Submit, fe);
	}

	/**
	 * Creates, configures & adds the buttons bar.<br/>
	 */
	private void addButtonBar() {

		SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent we) {
						Button button = we.getButtonClicked();
						if (button.getItemId().equals(Dialog.YES)) { // pressed
							tryImportConnectors();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), "Seguro que desea importar los conectores",
					listener);
			}
		};
		Button bAccept = buttonsSupport.createSaveButtonForDetailPanels(this, listener, new LinkedList<Component>(
			fields), pmsListeningStrategy);
		addButton(bAccept);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
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
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		boolean dirty = false;
		Iterator<Field<?>> it = fields.iterator();
		while (!dirty && it.hasNext()) {
			dirty = dirty || it.next().isDirty();
		}
		return dirty;
	}

	/**
	 * @return the {@link #messages}
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the {@link #pmsMessages}
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return shared objects container
	 */
	protected final Util getUtilities() {
		return util;
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
	 * Injects the shared objects container.
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the form helper.
	 * @param formSupport
	 */
	@Inject
	public void setFormSupport(FormSupport formSupport) {
		this.formSupport = formSupport;
	}

	/**
	 * Injects the buttons helper
	 * @param buttonsSupport
	 */
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = (IComponentListeningStrategy) pmsListeningStrategy;
	}

	/**
	 * @param errorProcessor the error processor.
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor
	 */
	protected ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @param styles the styles to set
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}

	/**
	 * @return the container
	 */
	public LayoutContainer getDataContainer() {
		return container;
	}

	public boolean isEdition() {
		return false;
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
	 * @param connService the IConnectorsServiceAsync to set
	 */
	@Inject
	public void setCtService(IConnectorsServiceAsync connService) {
		this.connectorsService = connService;
	}
	
	/**
	 * Injects the Error Message resolver.<br/>
	 * @param emrConnectors
	 */
	@Inject
	protected final void setEmrConnectors(ConnectorsErrorMessageResolver emrConnectors) {
		this.errorMessageResolver = emrConnectors;
	}
}
