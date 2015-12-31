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
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ChangeListener;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.CustomizableStoreFilter;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync;
import com.isotrol.impe3.pms.gui.client.controllers.CategoriesController;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;
import com.isotrol.impe3.pms.gui.client.error.CategoriesServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.store.CategoryTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsChangeEvent;
import com.isotrol.impe3.pms.gui.client.util.PmsContentPanel;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Shows the categories edition panel
 * 
 * @author Manuel A. Ruiz Gijon
 * 
 */
public class CategoryManagement extends PmsContentPanel {

	/**
	 * Key used in GXT for mapping models that don't implement TreeModel (CategorySelModelData) into a
	 * BaseTreeModel.<br/>
	 */
	private static final String KEY_MODEL = "model";

	/**
	 * Detail view as popup window.<br/>
	 */
	private Window wDetail = null;

	/**
	 * The Categories tree.<br/>
	 */
	private TreePanel<CategorySelModelData> tree = null;
	/**
	 * Store for {@link #tree}. It is also bound to the {@link #filter}.<br/>
	 */
	private CategoryTreeStore store = null;

	// Tool items we should care enabling/disabling
	/**
	 * "Edit" toolbar button.<br/>
	 */
	private Button bEdit = null;
	/**
	 * "Delete" toolbar button.<br/>
	 */
	private Button bDelete = null;
	/**
	 * "Export" toolbar button.<br/>
	 */
	private Button bExportBranch = null;
	/**
	 * "Import" toolbar button.<br/>
	 */
	private Button bImport = null;

	/**
	 * Fitler for the tree elements.<br/>
	 */
	private CustomizableStoreFilter<CategorySelModelData> filter = null;

	private String categoryRootId = null;

	/*
	 * Injected deps
	 */
	/**
	 * The error processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	/**
	 * Error message resolver for the operation {@link CategoriesService#delete(String)}.<br/>
	 */
	private CategoriesServiceErrorMessageResolver emrCategories = null;
	/**
	 * Categories async service proxy.<br/>
	 */
	private ICategoriesServiceAsync categoriesService = null;

	/**
	 * Generic messages service.
	 */
	private GuiCommonMessages messages = null;

	/**
	 * PMS specific messages service.
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * PMS specific styles service.
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Buttons helper service.<br/>
	 */
	private Buttons buttonsSupport = null;

	/**
	 * Static objects container<br/>
	 */
	private Util util = null;
	
	/**
	 * Settings bundle
	 */
	private Settings settings = null;

	/**
	 * Constructor
	 */
	public CategoryManagement() {
	}

	/**
	 * Inits the widget. Must be explicitly called after the dependencies are set.
	 */
	public void init() {
		configThis();
		initController();

		addToolBars();

		tryGetCategories();
	}

	/**
	 * inits the {@link #controllerListener} and adds it to the categories controller. All change events fired by the
	 * controller will refresh the {@link #tree}.<br/>
	 */
	private void initController() {
		ChangeListener controllerListener = new ChangeListener() {
			public void modelChanged(ChangeEvent e) {
				if (e instanceof PmsChangeEvent) {
					PmsChangeEvent event = (PmsChangeEvent) e;
					switch (event.getType()) {
						case PmsChangeEvent.ADD: // ask for all categories:
						case PmsChangeEvent.DELETE:
						case PmsChangeEvent.IMPORT:
							tryGetCategories();
							break;
						case PmsChangeEvent.UPDATE:
							CategoryDTO cDto = event.getEventInfo();

							List<CategorySelModelData> models = store.findModels(CategorySelModelData.PROPERTY_ID, cDto
								.getId());
							boolean found = !models.isEmpty();
							if (found) { // update writable properties on the ModelData,
								// and update view
								CategorySelModelData model = models.get(0);
								CategorySelDTO csDto = model.getDTO();
								csDto.setState(cDto.getState());
								csDto.setRoutable(cDto.isRoutable());
								csDto.setVisible(cDto.isVisible());
								csDto.setName(cDto.getDefaultName().getDisplayName());

								store.update(model);
								// item.setValue((String) csModelData.get(treeBinder.getDisplayProperty()));
							}
							break;
						default: // shouldn't happen..
							// Logger.getInstance().log(
							// "Unexpected event descriptor for a ChangeEventSource instance :" + event.type);
					}
				}
			}
		};
		CategoriesController catController = (CategoriesController) categoriesService;
		catController.addChangeListener(controllerListener);
	}

	/**
	 * Configures this component properties.<br/>
	 */
	private void configThis() {
		setLayout(new FitLayout());
		setScrollMode(Scroll.AUTO);
		setLayoutOnChange(true);
	}

	/**
	 * Creates, configures and adds the toolbar.<br/>
	 */
	private void addToolBars() {
		ToolBar toolBar = new ToolBar();
		ToolBar bottomToolBar = new ToolBar();

		buttonsSupport.addAddButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				CategorySelModelData parent = tree.getSelectionModel().getSelectedItem();
				if (parent == null) {
					parent = tree.getStore().getRootItems().get(0);
				}
				if (parent != null) {
					showCategoryCreationPanel(parent.getDTO(), tree.getStore().getChildCount(parent));
				}
			}
		}, null);

		toolBar.add(new SeparatorToolItem());

		bDelete = buttonsSupport.addDeleteButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				CategorySelModelData item = tree.getSelectionModel().getSelectedItem();
				if (item != null) {
					final CategorySelDTO csDto = item.getDTO();
					Listener<MessageBoxEvent> lConfirm = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent be) {
							if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
								tryDeleteCategory(csDto);
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmDeleteCategory(csDto
						.getName()), lConfirm);
				}
			}
		}, null);
		bDelete.disable();

		toolBar.add(new SeparatorToolItem());

		bEdit = buttonsSupport.addEditButton(toolBar, new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent tbe) {
				getSelectedAndDisplayDetails();
			}
		});
		bEdit.disable();

		toolBar.add(new FillToolItem());

		// Filter:
		filter = new CustomizableStoreFilter<CategorySelModelData>(Arrays
			.asList(new String[] {CategorySelModelData.PROPERTY_NAME}));
		filter.setHideLabel(false);
		filter.setFieldLabel(messages.labelFilter());
		toolBar.add(filter);

		// "Refresh" button:
		SelectionListener<IconButtonEvent> lRefresh = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				tryGetCategories();
			}
		};
		buttonsSupport.addRefreshButton(toolBar, lRefresh);
		
		// Export branch button
		bExportBranch = buttonsSupport.addGenericButton(pmsMessages.labelExportBranch(), pmsStyles.exportIcon(),
			bottomToolBar, new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					CategorySelModelData selectedCategory = tree.getSelectionModel().getSelectedItem();
					if (selectedCategory != null) {
						CategoriesExportWindow catWindow = PmsFactory.getInstance().getCategoriesExportWindow();
						String id = selectedCategory.getDTO().getId();
						if (id.equals(categoryRootId)) {
							catWindow.setRoot(true);
						}
						catWindow.init(id);
						catWindow.show();
					}
				}
			});
		bExportBranch.disable();

		buttonsSupport.addSeparator(bottomToolBar);

		// Import button
		bImport = buttonsSupport.addGenericButton(pmsMessages.labelImport(), pmsStyles.importIcon(), bottomToolBar,
			new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					CategorySelModelData selectedCategory = tree.getSelectionModel().getSelectedItem();
					if (selectedCategory != null) {
						CategoriesImportWindow catWindow = PmsFactory.getInstance().getCategoriesImportWindow();
						String id = selectedCategory.getDTO().getId();
						if (id.equals(categoryRootId)) {
							catWindow.setRoot(true);
						}
						catWindow.init(id);
						catWindow.show();
					}
				}
			});
		bImport.disable();
		
		bottomToolBar.add(new FillToolItem());
		
		// "Help" button:
		buttonsSupport.addHelpButton(bottomToolBar, settings.pmsCategoriesAdminPortalManualUrl());

		setTopComponent(toolBar);
		setBottomComponent(bottomToolBar);
	}

	/**
	 * Retrieves from service the selected Category details data, and displays it in a details window.<br/>
	 */
	private void getSelectedAndDisplayDetails() {
		CategorySelModelData selected = tree.getSelectionModel().getSelectedItem();
		if (selected != null && !selected.equals(store.getChild(0))) {
			tryGetCategoryDetails(selected.getDTO());
		}
	}

	/**
	 * Requests the remote service for the passed category full info.<br/> On success, opens the category edition panel
	 * with the retrieved info.
	 * @param dto
	 */
	private void tryGetCategoryDetails(final CategorySelDTO dto) {
		util.mask(pmsMessages.mskCategory());

		AsyncCallback<CategoryDTO> callback = new AsyncCallback<CategoryDTO>() {
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrCategories, pmsMessages.msgErrorRetrieveCategory());
			}

			public void onSuccess(CategoryDTO arg0) {
				showCategoryEditionPanel(arg0);
				util.unmask();
			}
		};

		categoriesService.get(dto.getId(), callback);
	}

	/**
	 * If exists, removes the category tree and recreates it from scratch.<br/>
	 * @param ctDto
	 */
	private void addTreePanel(CategoryTreeDTO ctDto) {

		if (store != null) {
			filter.unbind(store);
		}
		store = new CategoryTreeStore(ctDto);
		filter.setValue(null);
		filter.bind(store);

		tree = new TreePanel<CategorySelModelData>(store) {
			protected void onDoubleClick(TreePanelEvent tpe) {
				super.onDoubleClick(tpe);
				getSelectedAndDisplayDetails();
			}
		};
		tree.getStyle().setLeafIcon(IconHelper.createStyle(pmsStyles.iconTreeFolder()));

		tree.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<CategorySelModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<CategorySelModelData> se) {
				enableDisableButtons();
			}
		});

		tree.setDisplayProperty(CategorySelModelData.PROPERTY_NAME);

		add(tree);
	}

	private void enableDisableButtons() {
		CategorySelModelData selected = tree.getSelectionModel().getSelectedItem();

		boolean enabled = false;
		boolean enabledExportImport = false;

		if (selected != null && !selected.equals(store.getChild(0))) {
			enabled = true;
			enabledExportImport = true;
		} else if (selected != null) {
			enabledExportImport = true;
		}

		bDelete.setEnabled(enabled);
		bEdit.setEnabled(enabled);
		bExportBranch.setEnabled(enabledExportImport);
		bImport.setEnabled(enabledExportImport);
	}

	/**
	 * Closes the category details window.<br/>
	 */
	private void closeDetailWindow() {
		if (wDetail != null && wDetail.isAttached()) {
			wDetail.hide();
		}
	}

	/**
	 * If exists, closes the category edition panel and re-creates it from scratch with the passed DTO for data
	 * binding.<br/>
	 * @param model
	 */
	private void showCategoryEditionPanel(CategoryDTO model) {
		// close creation panel:
		closeDetailWindow();
		CategoryEditionPanel categoryEdition = PmsFactory.getInstance().getCategoryEdition();
		categoryEdition.init(model);
		wDetail = categoryEdition;
		wDetail.show();
	}

	/**
	 * Creates and shows a category edition panel and binds it to a new category instance.<br/>
	 * @param parent
	 * @param order
	 */
	private void showCategoryCreationPanel(CategorySelDTO parent, int order) {

		CategoryDTO cSel = new CategoryDTO();
		cSel.setState(State.NEW);
		NameDTO defaultName = new NameDTO(pmsMessages.defaultValueCategoryName(), pmsMessages
			.defaultValueCategoryPath());
		cSel.setDefaultName(defaultName);
		cSel.setRoutable(Boolean.valueOf(pmsMessages.defaultValueCategoryRoutable()));
		cSel.setVisible(Boolean.valueOf(pmsMessages.defaultValueCategoryVisible()));

		String parentId = parent.getId();

		closeDetailWindow();
		CategoryCreationPanel categoryCreation = PmsFactory.getInstance().getCategoryCreation();
		categoryCreation.init(cSel, parentId, order);
		wDetail = categoryCreation;
		wDetail.show();
	}

	/**
	 * repopulates the tree with the passed category tree, and remembers the previously expanded nodes.<br/>
	 * 
	 * @param category
	 */
	private void repopulateTree(CategoryTreeDTO category) {
		// store expanded items IDs:
		List<String> expanded = new LinkedList<String>();
		if (tree != null) {
			List<CategorySelModelData> rootModels = store.getRootItems();
			for (CategorySelModelData model : rootModels) {
				computeExpanded(model, expanded);
			}
		}
		// destroy & create the tree:
		if (tree != null && tree.isAttached()) {
			tree.removeFromParent();
		}
		addTreePanel(category);
		// restore expanded:
		List<CategorySelModelData> rootModels = store.getRootItems();
		for (CategorySelModelData model : rootModels) {
			tree.setExpanded(model, true);
			// children nodes:
			for (CategorySelModelData child : store.getChildren(model)) {
				maybeExpand(child, expanded);
			}
		}

		configDND();
	}

	/**
	 * If the passed model is expanded, adds it to the passed list and recursively computes the child expanded nodes.<br
	 * /> When current call is finished, expanded contains its old nodes AND all expanded nodes from passed model
	 * branch.
	 * 
	 * @param model
	 * @param expanded
	 */
	private void computeExpanded(CategorySelModelData model, List<String> expanded) {
		if (tree.isExpanded(model)) {
			expanded.add(model.getDTO().getId());
			List<CategorySelModelData> children = store.getChildren(model);
			for (CategorySelModelData child : children) {
				computeExpanded(child, expanded);
			}
		}
	}

	/**
	 * Expands the passed model branches whose IDs are contained in the expanded list.
	 * 
	 * @param model
	 * @param expanded
	 */
	private void maybeExpand(CategorySelModelData model, List<String> expanded) {
		if (expanded.contains(model.getDTO().getId())) {
			tree.setExpanded(model, true);
			for (CategorySelModelData child : store.getChildren(model)) {
				maybeExpand(child, expanded);
			}
		}
	}

	/**
	 * Calls the categories remote service the "move" method.<br/>
	 * @param source
	 * @param parent
	 * @param order
	 */
	private void tryMoveCategory(final CategorySelDTO source, CategorySelDTO parent, int order) {

		AsyncCallback<CategoryTreeDTO> callback = new AsyncCallback<CategoryTreeDTO>() {
			public void onFailure(Throwable arg0) {
				errorProcessor.processError(arg0, emrCategories, pmsMessages.msgErrorMoveCategory());
			}

			public void onSuccess(CategoryTreeDTO arg0) {
				util.info(pmsMessages.msgSuccessMoveCategory());
			}
		};

		categoriesService.move(source.getId(), parent.getId(), order, callback);
	}

	/**
	 * Retrieves all the categories from service and displays them in the categories tree.<br/>
	 */
	private void tryGetCategories() {

		util.mask(pmsMessages.mskCategories());

		AsyncCallback<CategoryTreeDTO> catCallback = new AsyncCallback<CategoryTreeDTO>() {

			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrCategories, pmsMessages.msgErrorRetrieveCategories());
			}

			public void onSuccess(CategoryTreeDTO category) {
				// save root id
				categoryRootId = category.getNode().getId();

				repopulateTree(category);
				enableDisableButtons();
				util.unmask();
			}
		};

		categoriesService.getCategories(catCallback);
	}

	/**
	 * Calls the "delete" operation on categories remote service, fro the passed category. <br/>
	 * @param csDto
	 */
	private void tryDeleteCategory(final CategorySelDTO csDto) {
		util.mask(pmsMessages.mskDeleteCategory());

		AsyncCallback<CategoryTreeDTO> callback = new AsyncCallback<CategoryTreeDTO>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			/**
			 * Just shows an alert.<br/>
			 */
			public void onFailure(Throwable arg0) {
				util.unmask();
				errorProcessor.processError(arg0, emrCategories, pmsMessages.msgErrorDeleteCategory(csDto.getName()));
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onSuccess(java.lang.Object)
			 */
			/**
			 * Shows an animated info popup and populates the tree with the results.<br/>
			 */
			public void onSuccess(CategoryTreeDTO arg0) {
				// repopulateTree(arg0);
				util.unmask();
				util.info(pmsMessages.msgSuccessDeleteCategory());
			}
		};

		categoriesService.delete(csDto.getId(), callback);
	}

	/**
	 * Configs the tree binder with DND capabilities. Should be called after structural changes in the Tree:
	 * move/insert/remove.<br/>
	 */
	private void configDND() {

		TreePanelDragSource dragSource = new TreePanelDragSource(tree);
		dragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent e) {
				CategorySelModelData selected = tree.getSelectionModel().getSelectedItem();
				if (selected.equals(store.getRootItems().get(0))) {
					e.setCancelled(true);
					e.getStatus().setStatus(false);
				}
				super.dragStart(e);
			}
		});

		TreePanelDropTarget dropTarget = new TreePanelDropTarget(tree);
		dropTarget.setAllowSelfAsSource(true);
		dropTarget.setAllowDropOnLeaf(true);
		dropTarget.setFeedback(Feedback.BOTH);
		dropTarget.setAutoExpand(true);

		dropTarget.setAllowSelfAsSource(true);
		dropTarget.setFeedback(Feedback.BOTH);

		dropTarget.addDNDListener(new DNDListener() {
			/**
			 * Ask for confirmation on drop.
			 * @param e drop event.
			 */
			@Override
			public void dragDrop(final DNDEvent e) {

				Listener<MessageBoxEvent> wListener = new Listener<MessageBoxEvent>() {
					@SuppressWarnings("unchecked")
					public void handleEvent(MessageBoxEvent be) {
						Button clicked = be.getButtonClicked();
						if (clicked == null) {
							return;
						}

						if (clicked.getItemId().equals(Dialog.YES)) { // confirmado
							// DND params: dragged item, new parent & order
							CategorySelModelData draggedItem = null;
							List<BaseTreeModel> draggedItems = (List<BaseTreeModel>) e.getData();
							if (!draggedItems.isEmpty()) {
								draggedItem = draggedItems.get(0).get(KEY_MODEL);
							}

							// parent:
							CategorySelModelData parent = CategoryManagement.this.store.getParent(draggedItem);

							int dndOrder = CategoryManagement.this.store.getChildren(parent).indexOf(draggedItem);

							tryMoveCategory(draggedItem.getDTO(), parent.getDTO(), dndOrder);
						} else { // no ha confirmado: devolver el Tree al estado anterior es un marron,
							// asi que volvemos a pedirlo todo:
							tryGetCategories();
						}
					}
				};

				MessageBox.confirm(messages.headerConfirmWindow(), pmsMessages.msgConfirmMoveCategory(), wListener)
					.setModal(true);
			}
		});
	}

	/**
	 * Injects the Categories async service.
	 * @param categoriesService
	 */
	@Inject
	public void setCategoriesService(ICategoriesServiceAsync categoriesService) {
		this.categoriesService = categoriesService;
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
	 * Injects the PMS specific style bundle.
	 * @param pmsStyles
	 */
	@Inject
	public void setPmsStyles(PmsStyles pmsStyles) {
		this.pmsStyles = pmsStyles;
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
	 * Injects the static objects container
	 * @param utilities
	 */
	@Inject
	public void setUtil(Util utilities) {
		this.util = utilities;
	}

	/**
	 * Injects the categories error message resolver
	 * @param emr the error message resolver to set
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
	 * @param settings the settings to set
	 */
	@Inject
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
}
