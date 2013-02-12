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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages;


import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.client.data.impl.ComponentInPageTemplateModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.PaletteModelData;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;


/**
 * Manages the Components and their logical dependencies tree within a Page.<br/> Panel with the list of portal's
 * components and their dependencies tree.<br/>
 * 
 * Forwards the <b>Change</b> event fired by the tree Components in Page.
 * 
 * @author Manuel Ruiz
 * 
 */
public class ComponentsInPageManagement extends LayoutContainer implements IDetailPanel, IPaletteComponentsReceiver {

	/**
	 * <b>Dirty</b> state. The component is dirty if any of these operations has been performed: <ul> <li>add component
	 * from palette</li> <li>remove component</li> <li>rename component</li> <li>config component</li> <li>reconfigure
	 * components dependence tree</li> </ul> Once one of these operations is performed, the dirty state will not go back
	 * to <code>false</code> for this window lifecycle.
	 */
	private boolean dirty = false;

	/** remove tool item of tree's tool bar */
	private Button ttiRemove = null;
	/** configure component tool item of tree's tool bar */
	private Button ttiConfig = null;
	/** new tool item of tree's tool bar */
	private Button ttiNew = null;
	/**
	 * "Rename" component tool item in tree toolbar.<br/>
	 */
	private Button ttiRename = null;

	/** modeldata selected to add a new component as a child */
	private ComponentInPageTemplateModelData parentModel = null;

	/** portal locator */
	private PortalPagesLoc portalPagesLoc = null;
	/** page locator */
	private PageLoc pageLoc = null;

	/**
	 * Panel that contains a tree that represents the functional dependences between page components.<br/>
	 */
	private ContentPanel depsPanel = null;

	/**
	 * Page data bound to the widget.<br/>
	 */
	private PageTemplateDTO pageTemplateDto = null;

	/**
	 * The Components palette window.<br/>
	 */
	private ComponentsPaletteWindow wComponentsPalette = null;

	/*
	 * Injected deps.
	 */
	/**
	 * The service error processor.
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/** pages service */
	private IPagesServiceAsync pagesService = null;

	/**
	 * Error message resolver for Pages service.<br/>
	 */
	private PageErrorMessageResolver emrPages = null;

	/**
	 * Generic messages service.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Common styles service.<br/>
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Buttons helper object.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Components dependences widget.<br/>
	 */
	private ComponentsDependencesTree tComponentsDependences = null;

	/**
	 * Constructor
	 */
	public ComponentsInPageManagement() {
	}

	/**
	 * Inits the widget. Must be called after the dependencies are set.
	 * @param pageData
	 */
	public void init(PageTemplateDTO pageData) {
		this.pageTemplateDto = pageData;
		portalPagesLoc = new PortalPagesLoc(pageData.getPortalId(), pageData.getDeviceId());
		pageLoc = new PageLoc(portalPagesLoc, pageData.getId());

		/*
		 * on detach, destroy palette window
		 */
		addListener(Events.Detach, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent be) {
				if (wComponentsPalette != null) {
					wComponentsPalette.hide();
					wComponentsPalette = null;
				}
			}
		});

		initComponent();
	}

	/**
	 * Adds widgets to the panel
	 */
	private void initComponent() {
		setAutoWidth(true);
		setLayout(new FitLayout());

		addDependenciesTree();
	}

	/**
	 * Marks as <code>dirty</code> this component (the panel) and fires a <code>Change</code> event.<br/>
	 */
	private void markDirty() {
		dirty = true;
		Util.fireChangeEvent(this);
	}

	/**
	 * Creates the components dependences tree and adds it to the panel
	 */
	private void addDependenciesTree() {

		depsPanel = new ContentPanel();
		depsPanel.setHeading(pmsMessages.headerComponentsDependencesPanel());
		depsPanel.setBodyStyleName("components-tree-panel-body");
		depsPanel.setScrollMode(Scroll.AUTO);

		tComponentsDependences.init(pageTemplateDto.getComponents());
		tComponentsDependences.addListener(Events.Remove,
			new Listener<TreePanelEvent<ComponentInPageTemplateModelData>>() {
				public void handleEvent(TreePanelEvent<ComponentInPageTemplateModelData> be) {
					markDirty();
				};
			});
		tComponentsDependences.addDNDListener(new DNDListener() {
			@Override
			public void dragDrop(DNDEvent e) {
				markDirty();
			}
		});
		depsPanel.add(tComponentsDependences);

		// add the tool bar with the "Remove" component button
		addToolBar();

		// listener to enable the remove button
		// listener that enables/disables the toolbar buttons
		tComponentsDependences.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<ComponentInPageTemplateModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<ComponentInPageTemplateModelData> se) {
					ComponentInPageTemplateModelData selectedItem = tComponentsDependences.getSelectionModel()
						.getSelectedItem();
					List<ComponentInPageTemplateModelData> rootItems = tComponentsDependences.getStore().getRootItems();

					if (selectedItem == null || rootItems.contains(selectedItem)) {
						// parent will be root, enable only New:
						ttiNew.enable();
						ttiRemove.disable();
						ttiConfig.disable();
						ttiRename.disable();
					} else if (selectedItem.getDTO().isSpace()) {
						ttiNew.disable();
						ttiRemove.disable();
						ttiConfig.disable();
						ttiRename.disable();
					} else {
						ComponentInPageTemplateDTO dto = selectedItem.getDTO();

						ttiNew.enable();
						ttiRemove.enable();
						ttiRename.enable();

						// disable 'Config' if no config items:
						if (dto.getConfiguration() == null || dto.getConfiguration().getItems().isEmpty()) {
							ttiConfig.disable();
						} else {
							ttiConfig.enable();
						}
					}
				}
			});

		tComponentsDependences.addListener(Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				markDirty();
			}
		});

		add(depsPanel);
	}

	/**
	 * Adds the toolBar to the components dependences tree.
	 * 
	 * @return the tool bar
	 */
	private void addToolBar() {
		ToolBar toolBar = new ToolBar();

		ttiNew = buttonsSupport.addAddButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				parentModel = tComponentsDependences.getSelectionModel().getSelectedItem();
				if (parentModel == null) {
					parentModel = tComponentsDependences.getStore().getRootItems().get(0);
				}
				showPaletteWindow();
			}
		}, null);

		buttonsSupport.addSeparator(toolBar);

		ttiRemove = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ComponentInPageTemplateModelData selected = tComponentsDependences.getSelectionModel()
					.getSelectedItem();
				if (selected != null) {
					tryRemoveComponentFromTree(selected);
				}
			}
		}, null);
		ttiRemove.disable();

		buttonsSupport.addSeparator(toolBar);

		ttiConfig = new Button(pmsMessages.labelConfigure());
		ttiConfig.setIconStyle(styles.iEdit());
		ttiConfig.disable();
		ttiConfig.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ComponentInPageTemplateModelData selectedItem = tComponentsDependences.getSelectionModel()
					.getSelectedItem();
				if (selectedItem != null) {
					ConfigurationTemplateDTO configurationDto = selectedItem.getDTO().getConfiguration();
					showComponentConfigWindow(configurationDto);
				}
			}
		});
		toolBar.add(ttiConfig);

		buttonsSupport.addSeparator(toolBar);

		ttiRename = buttonsSupport.addGenericButton(pmsMessages.labelRename(), styles.iEdit(), toolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					ComponentInPageTemplateModelData tiSelected = tComponentsDependences.getSelectionModel()
						.getSelectedItem();
					showNameEditor(tiSelected);
				}
			});
		ttiRename.disable();

		depsPanel.setTopComponent(toolBar);
	}

	/**
	 * Creates the palette window, if not already created. Shows the palette window.<br/>
	 */
	private void showPaletteWindow() {
		if (wComponentsPalette == null) {
			wComponentsPalette = PmsFactory.getInstance().getComponentsPaletteWindow();
			wComponentsPalette.init(this, portalPagesLoc);
		}
		wComponentsPalette.show();
	}

	/**
	 * Creates and shows a new component configuration window for the passed configuration template DTO.<br/>
	 * 
	 * @param configDto configuration data to show in window.
	 */
	private void showComponentConfigWindow(ConfigurationTemplateDTO configDto) {

		ComponentsInPageConfigWindow wConfig = PmsFactory.getInstance().getComponentsInPageConfigWindow();
		wConfig.setConfigDto(configDto);
		wConfig.init().show();

		// fires Change events when configuration changed.
		wConfig.addListener(Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				tComponentsDependences.getStore().update(tComponentsDependences.getSelectionModel().getSelectedItem());
				// markDirty();
			}
		});
	}

	/**
	 * TODO this should go to a new class<br/>
	 * 
	 * @param model
	 */
	private void showNameEditor(final ComponentInPageTemplateModelData model) {

		final ComponentInPageNameEditor wNameEditor = PmsFactory.getInstance().getComponentInPageNameEditor();
		final ComponentInPageTemplateDTO dto = model.getDTO();
		wNameEditor.setDto(dto);
		wNameEditor.init().show();

		wNameEditor.addListener(Events.Change, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				tComponentsDependences.getStore().update(tComponentsDependences.getSelectionModel().getSelectedItem());
			};
		});
	}

	/**
	 * Called from within the Components palette window.<br/>
	 * @param components
	 */
	public void receiveComponents(List<PaletteModelData> components) {
		for (PaletteModelData paletteComponent : components) {
			addComponentToTree(paletteComponent);
		}
		// mark as dirty & notify change:
		markDirty();
	}

	/**
	 * Adds the passed component to the components-in-page tree.<br/> Operation is realized server-side and client-side.
	 * 
	 * @param componentSelected
	 */
	private void addComponentToTree(PaletteModelData componentSelected) {

		AsyncCallback<ComponentInPageTemplateDTO> callback = new AsyncCallback<ComponentInPageTemplateDTO>() {
			public void onFailure(Throwable arg0) {
				MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorCreateComponentInPage(), null)
					.setModal(true);
			}

			public void onSuccess(ComponentInPageTemplateDTO component) {
				tComponentsDependences.addNode(parentModel, new ComponentInPageTemplateModelData(component));
				tComponentsDependences.expandAll();
			}
		};

		pagesService.newComponentTemplate(portalPagesLoc, componentSelected.getDTO().getKey(), callback);
	}

	/**
	 * Checks if the component is in the current layout, if true, removing the component is not allowed
	 * 
	 * @param component the component to remove
	 */
	private void tryRemoveComponentFromTree(final ComponentInPageTemplateModelData component) {

		if (isEdition()) {
			AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
				public void onFailure(Throwable arg0) {
					errorProcessor.processError(arg0, emrPages, pmsMessages.msgErrorRetrieveComponentInPage());
				}

				public void onSuccess(Boolean arg0) {

					if (arg0) {
						MessageBox.alert(messages.headerErrorWindow(), pmsMessages.msgErrorDeleteComponentInDesign(),
							null).setModal(true);
					} else {
						tComponentsDependences.removeItem(component);
						markDirty();
					}
				}
			};

			pagesService.isCIPinLayout(pageLoc, component.getDTO().getId(), callback);
		} else {
			tComponentsDependences.removeItem(component);
		}

	}

	/**
	 * @return the tDeps
	 */
	public ComponentsDependencesTree getDepsTree() {
		return tComponentsDependences;
	}

	/**
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return pageLoc.getId() != null;
	}

	/**
	 * Valid when all tree items configurations are valid<br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	public boolean isValid() {
		return tComponentsDependences.isValid();
	}

	/**
	 * Injects the Pages async service.
	 * @param pagesService
	 */
	@Inject
	public void setPagesService(IPagesServiceAsync pagesService) {
		this.pagesService = pagesService;
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
	 * Injects the Components dependences widget.
	 * @param tComponentsDependences
	 */
	@Inject
	public void setComponentsDependencesTree(ComponentsDependencesTree componentsDependencesTree) {
		this.tComponentsDependences = componentsDependencesTree;
	}

	/**
	 * Injects the pages error message resolver.
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
}
