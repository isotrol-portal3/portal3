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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master;


import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.ILazyComponent;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.PagesController;
import com.isotrol.impe3.pms.gui.client.data.impl.PageDeviceModelData;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.design.Design;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.DeletePageCallback;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.PagesImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ToolbarSupport;


/**
 * Common logic for Pages management panels.
 * 
 * @author Andrei Cojocaru
 * @author Manuel Ruiz
 * 
 */
public abstract class AbstractPagesManagement extends PmsContentPanel implements ILazyComponent {

	/** pages controller listener for change events */
	private ChangeListener controllerListener = null;

	/**
	 * The location params for the bound portal.<br/>
	 */
	private PortalPagesLoc portalPagesLoc = null;

	/**
	 * Data bound to the widget.<br/>
	 */
	private PortalNameDTO portalNameDto = null;

	/**
	 * Page device
	 */
	private PageDeviceModelData currentDevice = null;

	/*
	 * Injected deps.
	 */
	/**
	 * The service error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;

	/** pages service reference */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Pages service error message resolver.<br/>
	 */
	private PageErrorMessageResolver emrPages = null;

	/**
	 * Utilities object.<br/>
	 */
	private Util util = null;
	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;
	/**
	 * PMS specific messages service.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS specific styles service.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Provides with operations on the pages management panels toolbar.<br/>
	 */
	private ToolbarSupport toolBarSupport = null;

	/**
	 * Async callback for page deletion.<br/>
	 */
	private DeletePageCallback deleteCallback = null;
	
	/**
	 * Pms Utilities object.<br/>
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Default constructor.
	 */
	public AbstractPagesManagement() {
	}

	/**
	 * Inits this component properties that must be set before rendering.<br/>
	 * @param portal the portal name dto
	 * @param currentDevice current page device
	 */
	public void init(PortalNameDTO portal, PageDeviceModelData currentDevice) {
		this.portalNameDto = portal;
		this.currentDevice = currentDevice;

		Util.configLazyComponent(this);

		setHeaderVisible(false);
		// setBodyBorder(false);
		setLayout(new FitLayout());
		setWidth(Constants.HUNDRED_PERCENT);
		setScrollMode(Scroll.AUTO);
		setLayoutOnChange(true);

		// setTopComponent() must be called on pre-render
		addToolBar();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.ILazyComponent#lazyInit()
	 */
	public void lazyInit() {
		initComponents();
		initController();

		portalPagesLoc = new PortalPagesLoc(portalNameDto.getId(), currentDevice.getDTO().getDeviceId());
		tryGetPages();
	}

	/**
	 * Inits this container inner components.<br/>
	 */
	private void initComponents() {
		populatePageDeviceCombo();
		addSpecificComponents();
		addListeners();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the categories controller. All change events fired by the
	 * controller will refresh the {@link #tree}.<br/>
	 */
	private void initController() {
		this.controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				PmsChangeEvent event = (PmsChangeEvent) e;
				switch (event.getType()) {
					case PmsChangeEvent.UPDATE:
					case PmsChangeEvent.ADD:
					case PmsChangeEvent.DELETE:
						String portalId = "";
						Object info = event.getEventInfo();
						if (info instanceof LayoutDTO) {
							LayoutDTO layout = (LayoutDTO) event.getEventInfo();
							portalId = layout.getPortalId();
						} else if (info instanceof PageTemplateDTO) {
							PageTemplateDTO page = (PageTemplateDTO) event.getEventInfo();
							portalId = page.getPortalId();
						} else if (info instanceof PageLoc) {
							PageLoc pageLoc = (PageLoc) event.getEventInfo();
							portalId = pageLoc.getPortalId();
						}
						if (portalId.equals(portalPagesLoc.getPortalId())) {
							tryGetPages();
						}
						break;
					case PmsChangeEvent.IMPORT:
						tryGetPages();
						break;
					default: // shouldn't happen..
						GWT.log("Unexpected event descriptor for a ChangeEventSource instance :" + event.getType(),
							null);
				}
			}
		};

		final PagesController pagesController = (PagesController) pagesService;
		pagesController.addChangeListener(controllerListener);

		addListener(Events.Detach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				pagesController.removeChangeListener(controllerListener);
			}
		});
	}

	/**
	 * Fired when user removed a page.<br/>
	 * 
	 * @param pageId
	 */
	protected abstract void onPageRemove(String pageId);

	/**
	 * Fired when user added a new page.<br/>
	 * 
	 * @param ptDto
	 */
	protected abstract void onPageAdd(PageSelDTO ptDto);

	/**
	 * @param psDto
	 * @return <code>true</code>, if page is managed by this implementation; <code>false</code> otherwise.
	 */
	protected abstract boolean isPageManagedInThisWidget(PageSelDTO psDto);

	/**
	 * Creates & inits this panel toolbar.<br/>
	 */
	private void addToolBar() {
		setTopComponent(toolBarSupport.getToolBar());
		setBottomComponent(toolBarSupport.getBottomToolBar());

		addSpecificToolItems();
	}

	/**
	 * Creates and adds tool items to the top tool bar. <br/>
	 */
	protected abstract void addSpecificToolItems();

	/**
	 * Adds implementation specifc components to panel.<br/>
	 */
	protected abstract void addSpecificComponents();

	/**
	 * Adds listeners to initialized components.<br/>
	 */
	protected void addListeners() {
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetPages();
			}
		};
		toolBarSupport.getTbRefresh().addSelectionListener(lRefresh);

		// export listener
		SelectionListener<ButtonEvent> exportListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				pagesService.exportAll(portalPagesLoc, new AsyncCallback<String>() {

					public void onSuccess(String result) {
						pmsUtil.exportPmsFile(result);
					}

					public void onFailure(Throwable caught) {
						util.error(getPmsMessages().msgExportError());
					}
				});
			}
		};
		toolBarSupport.getbExport().addSelectionListener(exportListener);

		SelectionListener<ButtonEvent> importListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				PagesImportWindow w = PmsFactory.getInstance().getPagesImportWindow();
				w.setPageLoc(portalPagesLoc);
				w.show();
			}
		};
		toolBarSupport.getbImport().addSelectionListener(importListener);

		// When we change device, we get pages for new device and disable/enable design button if the pages can be
		// designed
		toolBarSupport.getCbDevices().addSelectionChangedListener(new SelectionChangedListener<PageDeviceModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<PageDeviceModelData> se) {
				PageDeviceModelData selectedDevice = se.getSelectedItem();
				if (selectedDevice != null
					&& !selectedDevice.getDTO().getDeviceId().equals(currentDevice.getDTO().getDeviceId())) {
					currentDevice = selectedDevice;
					portalPagesLoc.setDeviceId(selectedDevice.getDTO().getDeviceId());
					tryGetPages();
				}
			}
		});
	}

	/**
	 * @return the page class
	 */
	protected abstract PageClass getPageClass();

	/**
	 * implemented in subclasses. Retrieves the specific pages
	 */
	protected abstract void tryGetPages();

	/**
	 * Delete a page
	 * 
	 * @param item the modeldata to delete
	 */
	protected final void tryDeletePage(PageLoc pageLoc) {
		util.mask(pmsMessages.mskDeletePage());
		getPagesService().delete(pageLoc, deleteCallback);
	}

	/**
	 * Requests the pages service for the layout of the passed page, and shows the Design window with the resulting
	 * layout data.<br/>
	 * 
	 * @param page
	 */
	protected final void tryGetPageLayoutAndDisplayDesign(final PageTemplateDTO page) {
		AsyncCallback<LayoutDTO> callback = new AsyncCallback<LayoutDTO>() {
			public void onFailure(Throwable arg0) {
				getUtil().unmask();
				errorProcessor.processError(arg0, emrPages, getPmsMessages().msgErrorRetrieveLayout());
			}

			public void onSuccess(LayoutDTO layout) {
				Design design = Design.getInstance();
				design.config(page, layout);
				design.show();
				design.maximize();

				getUtil().unmask();
			}
		};

		PageLoc pageLoc = new PageLoc(page.getPortalId(), page.getDeviceId(), page.getId());

		getPagesService().getLayout(pageLoc, callback);
	}

	/**
	 * Requests the service for the preview URL of the passed page. On success opens the preview window.
	 * 
	 * @param pageLoc page identification data.
	 */
	protected void openPreviewWindow(PageLoc pageLoc) {
		
		pmsUtil.openPagePreview(pageLoc);
		
//		AsyncCallback<String> callback = new AsyncCallback<String>() {
//			public void onFailure(Throwable arg0) {
//				errorProcessor.processError(arg0, emrPages, getPmsMessages().msgErrorRetrievePreviewUrl());
//			}
//
//			public void onSuccess(String arg0) {
//				com.google.gwt.user.client.Window.open(arg0, getPmsMessages().headerPreviewWindow(),
//					PmsConstants.NEW_WINDOW_FEATURES);
//			}
//		};
//		getPagesService().getPreview(pageLoc, callback);
	}

	private void populatePageDeviceCombo() {
		final ListStore<PageDeviceModelData> store = toolBarSupport.getCbDevices().getStore();

		store.removeAll();

		AsyncCallback<List<PageDeviceDTO>> callback = new AsyncCallback<List<PageDeviceDTO>>() {

			public void onSuccess(List<PageDeviceDTO> result) {
				List<PageDeviceModelData> models = new ArrayList<PageDeviceModelData>();
				for (PageDeviceDTO device : result) {
					models.add(new PageDeviceModelData(device));
				}
				store.add(models);
				toolBarSupport.getCbDevices().setValue(
					store.findModel(PageDeviceModelData.PROPERTY_DEVICE_ID, currentDevice.getDTO().getDeviceId()));
			}

			public void onFailure(Throwable caught) {
				getUtil().error(getPmsMessages().msgErrorRetrieveDevices());
			}
		};
		pagesService.getPageDevices(portalNameDto.getId(), callback);
	}

	/**
	 * @return the portalPagesLoc
	 */
	protected PortalPagesLoc getPortalPagesLoc() {
		return portalPagesLoc;
	}

	/**
	 * @return the Pages service proxy
	 */
	protected final IPagesServiceAsync getPagesService() {
		return pagesService;
	}

	/**
	 * @return the messages
	 */
	protected final GuiCommonMessages getMessages() {
		return messages;
	}

	/**
	 * @return the pmsMessages
	 */
	protected final PmsMessages getPmsMessages() {
		return pmsMessages;
	}

	/**
	 * @return the PMS specific styles service.
	 */
	protected PmsStyles getPmsStyles() {
		return pmsStyles;
	}

	/**
	 * @return the toolBarSupport
	 */
	protected final ToolbarSupport getToolBarSupport() {
		return toolBarSupport;
	}

	/**
	 * @return the util
	 */
	protected final Util getUtil() {
		return util;
	}

	/**
	 * @return the emrPages
	 */
	protected final PageErrorMessageResolver getEmrPages() {
		return emrPages;
	}

	/**
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
	}

	/**
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
	}

	/**
	 * @param toolBarSupport
	 */
	@Inject
	public void setToolBarSupport(ToolbarSupport toolBarSupport) {
		this.toolBarSupport = toolBarSupport;
	}

	/**
	 * Injects the async callback for <b>delete Page</b> operation.
	 * @param deleteCallback
	 */
	@Inject
	public void setDeleteCallback(DeletePageCallback deleteCallback) {
		this.deleteCallback = deleteCallback;
	}

	/**
	 * Injects the utilities object.
	 * @param u the util to set
	 */
	@Inject
	public void setUtil(Util u) {
		this.util = u;
	}

	/**
	 * Injects the Pages error message resolver.
	 * @param emrPages the emrPages to set
	 */
	@Inject
	public void setEmrPages(PageErrorMessageResolver emrPages) {
		this.emrPages = emrPages;
	}

	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}

	/**
	 * @return the service error processor
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}

	/**
	 * @return the currentDevice
	 */
	public PageDeviceModelData getCurrentDevice() {
		return currentDevice;
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}
}
