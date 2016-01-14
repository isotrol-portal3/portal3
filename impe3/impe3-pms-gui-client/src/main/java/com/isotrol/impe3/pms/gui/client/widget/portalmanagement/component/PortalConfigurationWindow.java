package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component;

import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.gui.common.widget.TypicalWindow;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.widget.ConfigurationPanel;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;

public class PortalConfigurationWindow extends TypicalWindow implements IDetailPanel{
	
	private ConfigurationPanel confPanel = null;
	private ConfigurationTemplateDTO configurationTemplate = null;
	private String bean;
	private String id;
	private SelectionListener<ButtonEvent> acceptListener = null;
	private boolean inherited;
	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;
	private Util util = null;

	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Components service proxy.<br/>
	 */
	private IPortalsServiceAsync portalService = null;
	
	
	
	private PmsListeningStrategy pmsListeningStrategy = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;


	public void init(ConfigurationTemplateDTO configurationTemplate, String id, String bean,boolean inherited) {
		this.id = id;
		this.bean=bean;
		this.configurationTemplate = configurationTemplate;
		this.inherited=inherited;

		initListeners();
		initThis();
		initComponents();
	}
	
	private void initComponents() {
		confPanel.init(configurationTemplate);
		add(confPanel);
		addButtonBar();
	}

	/**
	 * Adds "Accept" and "Cancel" controls.<br/>
	 */
	private void addButtonBar() {

		List<Component> components = new LinkedList<Component>();
		components.add(confPanel);

		Button bSave = buttonsSupport.createSaveButtonForDetailPanels(this, acceptListener, components,
			pmsListeningStrategy);
		addButton(bSave);

		Button bCancel = buttonsSupport.createCancelButtonForDetailPanels(this);
		addButton(bCancel);
	}
	
	
	private void initThis() {
		setWidth(PmsConstants.DETAIL_WINDOW_WIDTH);
		setScrollMode(Scroll.AUTO);
		setHeadingText(pmsMessages.configurationWidgetTitle());
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
							trySavePortalConfiguration();
						}
					}
				};
				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmOverrideConfiguration(),
					lConfirm).setModal(true);
			}
		};
	}

	
	
	private void trySavePortalConfiguration() {
		
		confPanel.applyValues();
		
		List<ConfigurationItemDTO> confsDto = configurationTemplate.toConfiguationItemDTO();

		AsyncCallback<ConfigurationTemplateDTO> callback = new AsyncCallback<ConfigurationTemplateDTO>() {

			public void onSuccess(ConfigurationTemplateDTO result) {
				util.info(pmsMessages.msgSuccessOverrideConfiguration());
				hide();
			}

			public void onFailure(Throwable caught) {
				util.error(pmsMessages.msgErrorOverrideConfiguration());
			}
		};
		
		portalService.savePortalConfiguration(id, bean, confsDto, callback);
	}

	@Override
	public boolean isDirty() {
		return confPanel.isDirty();
		
	}

	@Override
	public boolean isValid(){
		return confPanel.isValid();
	}

	@Override
	public boolean isEdition() {
		return true;
	}
	
	@Inject
	public void setButtonsSupport(Buttons buttonsSupport) {
		this.buttonsSupport = buttonsSupport;
	}

	/**
	 * @param messages the messages to set
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param pmsMessages the pmsMessages to set
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @param componentsService the componentsService to set
	 */
	@Inject
	public void setPortalService(IPortalsServiceAsync portalService) {
		this.portalService = portalService;
	}

	/**
	 * @param pmsListeningStrategy the pmsListeningStrategy to set
	 */
	@Inject
	public void setPmsListeningStrategy(PmsListeningStrategy pmsListeningStrategy) {
		this.pmsListeningStrategy = pmsListeningStrategy;
	}

	/**
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}

	/**
	 * @param confPanel the confPanel to set
	 */
	@Inject
	public void setConfPanel(ConfigurationPanel confPanel) {
		this.confPanel = confPanel;
	}

}
