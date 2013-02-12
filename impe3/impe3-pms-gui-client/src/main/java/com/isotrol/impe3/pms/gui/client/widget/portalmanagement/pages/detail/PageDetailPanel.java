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


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreePanelEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.CategorySelModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.ContentTypeSelModelData;
import com.isotrol.impe3.pms.gui.client.store.CategoryTreeStore;
import com.isotrol.impe3.pms.gui.client.util.PmsConstants;


/**
 * Detail panel for pages of any kind. The GUI components of this window are created according to the bound page class.
 * 
 * @author Manuel Ruiz
 * 
 */
public class PageDetailPanel extends AbstractPageDetailPanel {

	/**
	 * Content types combo selector.<br/>
	 */
	private ComboBox<ModelData> cbSelectedCt = null;

	/**
	 * Category selector popup window.<br/>
	 */
	private Window wCategories = null;

	/**
	 * Categories tree.<br/>
	 */
	private TreePanel<CategorySelModelData> tCategories = null;

	/**
	 * Text field that displays currently selected categories.<br/>
	 */
	private TextField<String> tfSelectedCategory = null;

	/**
	 * Button that modifies the bound category.<br/>
	 */
	private Button bModifySelectedCategory = null;

	/**
	 * Panel that contains the category management.<br/>
	 */
	private VerticalPanel categoryPanel = null;

	/**
	 * HTML "name" attribute value for category radios.<br/>
	 */
	private static final String CATEGORY_RADIO_NAME = "category-radioGroup";

	/**
	 * el tipo de selección permitida
	 */
	private enum ESelectionPolicy {
		/**
		 * se permite seleccionar una sola categoría
		 */
		ONE,
		/**
		 * se permite seleccionar una categoría y sus descendientes
		 */
		HIERARCHY,
		/**
		 * todas las categorías del portal asociadas a esta página
		 */
		ALL
	}

	/** la politica de selección elegida */
	private ESelectionPolicy selectionPolicy = ESelectionPolicy.HIERARCHY;

	/*
	 * Category policy radios:
	 */
	/**
	 * RadioGroup that contains the category binding policy<br/>
	 */
	private RadioGroup rgCategories;

	/**
	 * Radio selected if the page is associated to all categories.<br/>
	 */
	private Radio radioAll = null;

	/**
	 * Radio selected if the bound page is associated to one category <br/>
	 */
	private Radio radioOne = null;

	/**
	 * Radio selected if the bound page is associated to the whole hierarchy of a category.<br/>
	 */
	private Radio radioHierarchy = null;

	/** Category selected. */
	private CategorySelDTO categorySelected = null;

	/**
	 * Default constructor
	 * 
	 * @param template
	 */
	public PageDetailPanel() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail
	 * .AbstractPageDetailPanel#initComponents()
	 */
	@Override
	protected final void initComponents() {

		// field page name
		initTfName();
		getTopContainer().add(getTfName());

		// field page description
		initFDescription();
		getTopContainer().add(getFDescription());

		// template selector
		initCbSelectTemplate();
		getTopContainer().add(getCbSelectTemplate());

		// content type selector
		if (canHaveContentType()) {
			addContentTypeSelector();
		}

		// categories:
		if (canHaveCategory()) {
			addCategoriesWindow();
			categoryPanel = new VerticalPanel();
			addCategoryRadios();
			addCategorySelector();
			getTopContainer().add(categoryPanel);
		}

		// page components:
		addPageComponentsPanel();

		addButtonBar();
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractPageDetailPanel#initSpecificChangeSourceComponents(java.util.List)
	 */
	@Override
	protected void initSpecificChangeSourceComponents(List<Component> changeSourceComponents) {
		if (canHaveContentType()) {
			changeSourceComponents.add(cbSelectedCt);
		}
		if (canHaveCategory()) {
			changeSourceComponents.add(rgCategories);
			changeSourceComponents.add(tfSelectedCategory);
		}
	}

	/**
	 * Adds the combo that associates content types to the bound page.<br/>
	 */
	private void addContentTypeSelector() {

		ListStore<ModelData> store = new ListStore<ModelData>();
		cbSelectedCt = new ComboBox<ModelData>();
		cbSelectedCt.setFieldLabel(getPmsMessages().labelContentType());
		cbSelectedCt.setDisplayField(ContentTypeSelModelData.PROPERTY_NAME);
		cbSelectedCt.setEditable(false);
		cbSelectedCt.setStore(store);
		getTopContainer().add(cbSelectedCt);

		populateContentTypesCombo();
	}

	/**
	 * Fills the Content Types selector store with the available content types.<br/> Adds a special ModelData that
	 * represents the "no Content Type association" state.
	 */
	private void populateContentTypesCombo() {
		ListStore<ModelData> store = cbSelectedCt.getStore();
		store.removeAll();

		List<ContentTypeSelDTO> dtos = getPageTemplate().getContentTypes();
		List<ModelData> models = new LinkedList<ModelData>();

		// "--no content type--" value:
		BaseModelData noCt = new BaseModelData();
		noCt.set(ContentTypeSelModelData.PROPERTY_NAME, getPmsMessages().emptyValueContentType());
		// must have an ID because of searches
		noCt.set(ContentTypeSelModelData.PROPERTY_ID, PmsConstants.NULL_ID_VALUE);
		models.add(noCt);

		for (ContentTypeSelDTO dto : dtos) {
			models.add(new ContentTypeSelModelData(dto));
		}
		store.add(models);
	}

	/**
	 * Inits the Categories selector window. Does not set it visible.<br/>
	 */
	private void addCategoriesWindow() {
		// create categories window:
		wCategories = new Window();
		wCategories.setModal(true);
		wCategories.setHeight(400);
		wCategories.setScrollMode(Scroll.AUTO);
		wCategories.setShadow(false);

		// custom close action: there is no other way :[
		wCategories.setClosable(false);
		SelectionListener<IconButtonEvent> listener = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				displayCategoryValues(false);
			}
		};
		getButtonsSupport().addCloseButton(wCategories, true, listener);

		// the tree:
		tCategories = new TreePanel<CategorySelModelData>(new CategoryTreeStore(getPageTemplate().getCategories()));
		tCategories.setAutoHeight(true);
		tCategories.getStyle().setLeafIcon(IconHelper.create(getGuiCommonStyles().iconTreeFolder()));

		// Listener evento "categoría seleccionada"
		tCategories.getSelectionModel().addSelectionChangedListener(
			new SelectionChangedListener<CategorySelModelData>() {
				@Override
				public void selectionChanged(SelectionChangedEvent<CategorySelModelData> se) {
					CategorySelModelData item = tCategories.getSelectionModel().getSelectedItem();
					if (item != null) {
						categorySelected = item.getDTO();
						if (selectionPolicy == ESelectionPolicy.ONE) {
							tfSelectedCategory.setValue(getPmsMessages().valueSelectedOneCategory(
								categorySelected.getName()));
							getPageTemplate().setUmbrella(false);
						} else if (selectionPolicy == ESelectionPolicy.HIERARCHY) {
							tfSelectedCategory.setValue(getPmsMessages().valueSelectedCategoryHierarchy(
								categorySelected.getName()));
							getPageTemplate().setUmbrella(true);
						}
						Util.fireChangeEvent(tfSelectedCategory);
						wCategories.hide();
					}
				}
			});

		tCategories.setDisplayProperty(CategorySelModelData.PROPERTY_NAME);

		tCategories.addListener(Events.Show, new Listener<TreePanelEvent<CategorySelModelData>>() {
			/**
			 * (non-Javadoc)
			 * 
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(TreePanelEvent<CategorySelModelData> be) {
				tCategories.expandAll();
				// select currently bound category:
				CategorySelDTO catCurrent = getPageTemplate().getCategory();
				Store<CategorySelModelData> treeStore = tCategories.getStore();
				CategorySelModelData mCurrent = treeStore.findModel(CategorySelModelData.PROPERTY_ID, catCurrent
					.getId());

				tCategories.enableEvents(false);
				tCategories.getSelectionModel().setSelection(Arrays.asList(new CategorySelModelData[] {mCurrent}));
				tCategories.enableEvents(true);
			}
		});

		wCategories.add(tCategories);
	}

	/**
	 * Adds the category policy radios.<br/>
	 */
	private void addCategoryRadios() {
		// Campo Radio (todas las categorias / una o varias)
		rgCategories = new RadioGroup();
		rgCategories.setOrientation(Orientation.VERTICAL);
		rgCategories.setBorders(false);
		// group.setHideLabel(true);
		rgCategories.setId("nav_page_cat_radio_group");
		rgCategories.setFieldLabel(getPmsMessages().labelCategoriesAssociationPolicy());

		radioAll = new Radio();
		radioAll.setName(CATEGORY_RADIO_NAME);
		radioAll.setBoxLabel(getPmsMessages().labelAllCategoriesAssociationPolicy());
		radioAll.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				if (radioAll.getValue()) {
					selectionPolicy = ESelectionPolicy.ALL;
					onSelectionPolicyChanged();
				}
			}
		});
		rgCategories.add(radioAll);

		radioOne = new Radio();
		radioOne.setName(CATEGORY_RADIO_NAME);
		radioOne.setBoxLabel(getPmsMessages().labelOneCategoryAssociationPolicy());
		radioOne.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				if (radioOne.getValue()) {
					selectionPolicy = ESelectionPolicy.ONE;
					onSelectionPolicyChanged();
				}
			}
		});
		rgCategories.add(radioOne);

		radioHierarchy = new Radio();
		radioHierarchy.setName(CATEGORY_RADIO_NAME);
		radioHierarchy.setBoxLabel(getPmsMessages().labelHierarchyCategoriesAssociationPolicy());
		radioHierarchy.addListener(Events.Change, new Listener<FieldEvent>() {
			public void handleEvent(FieldEvent be) {
				if (radioHierarchy.getValue()) {
					selectionPolicy = ESelectionPolicy.HIERARCHY;
					onSelectionPolicyChanged();
				}
			}
		});
		rgCategories.add(radioHierarchy);

		categoryPanel.add(rgCategories);
	}

	/**
	 * Adds a textfield that displays the current category association, and a button that allows the user to change
	 * it.<br/>
	 */
	private void addCategorySelector() {
		HorizontalPanel pRow = new HorizontalPanel();

		// a la izquierda, TextField con eventInfo categoria(s) asociada(s)
		LayoutContainer lcSelectedCategory = new LayoutContainer(getFormSupport().getStandardLayout());
		tfSelectedCategory = new TextField<String>();
		tfSelectedCategory.setFieldLabel(getPmsMessages().labelSelectedCategoryOne());
		getFormSupport().configReadOnly(tfSelectedCategory);
		lcSelectedCategory.add(tfSelectedCategory);
		pRow.add(lcSelectedCategory);

		// a la derecha, botón Modificar:
		LayoutContainer lcModify = new LayoutContainer(getFormSupport().getStandardLayout());
		bModifySelectedCategory = new Button(getGuiCommonMessages().labelEdit(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				wCategories.show();
			}
		});
		lcModify.add(bModifySelectedCategory);
		pRow.add(lcModify);

		categoryPanel.add(pRow);
	}

	/**
	 * Sets the heading of the categories selector according to current category association policy.<br/>
	 */
	private void putCategoriesWindowHeading() {
		String heading = "";
		if (selectionPolicy == ESelectionPolicy.ONE) {
			heading = getPmsMessages().headerOneCategorySelector();
		} else if (selectionPolicy == ESelectionPolicy.HIERARCHY) {
			heading = getPmsMessages().headerRootCategorySelector();
		}
		wCategories.setHeading(heading);
	}

	/**
	 * lógica disparada cuando se ha cambiado la política de selección de Categorias
	 */
	private final void onSelectionPolicyChanged() {
		putCategoriesWindowHeading();

		if (selectionPolicy == ESelectionPolicy.ALL) {
			String value = getPmsMessages().valueSelectedAllCategories();
			tfSelectedCategory.setValue(value);
			tfSelectedCategory.setOriginalValue(value);
			categorySelected = new CategorySelDTO();
			categorySelected.setName(null);
			getPageTemplate().setUmbrella(true);
			bModifySelectedCategory.disable();
		} else {
			bModifySelectedCategory.enable();
			wCategories.show();
		}
	}

	/**
	 * Sets the current selection policy logical (see {@link #selectionPolicy}) and visual (radio and textfield)
	 * values.<br/>
	 * 
	 * @param firstTime if <code>true</code>, sets the current DTO value as the original value on the radio group. The
	 * RadioGroup component will be marked <code>dirty</code> when radio values change to something different from the
	 * value set as <code>original</code>.
	 */
	private void displayCategoryValues(boolean firstTime) {

		PageTemplateDTO pageTemplateDto = getPageTemplate();

		Radio radio = null;
		String tfValue = null;

		categorySelected = pageTemplateDto.getCategory();
		if (categorySelected == null) {
			selectionPolicy = ESelectionPolicy.ALL;
			radio = radioAll;
			tfValue = getPmsMessages().valueSelectedAllCategories();
		} else if (pageTemplateDto.isUmbrella()) {
			selectionPolicy = ESelectionPolicy.HIERARCHY;
			radio = radioHierarchy;
			tfValue = getPmsMessages().valueSelectedCategoryHierarchy(categorySelected.getName());
			bModifySelectedCategory.enable();
		} else {
			selectionPolicy = ESelectionPolicy.ONE;
			radio = radioOne;
			tfValue = getPmsMessages().valueSelectedOneCategory(categorySelected.getName());
			bModifySelectedCategory.enable();
		}

		setRadioValueWithoutFiringEvents(radio, true);
		tfSelectedCategory.setValue(tfValue);

		if (firstTime) {
			// original must be set on RadioGroup, otherwise it'll always be
			// dirty:
			rgCategories.setOriginalValue(radio);
		}
	}

	/**
	 * Sets the passed radio value to the passed boolean without firing Change events.<br/> Used in initialization and
	 * state restore.
	 * 
	 * @param radio
	 * @param value
	 */
	private void setRadioValueWithoutFiringEvents(Radio radio, boolean value) {
		radio.enableEvents(false);
		radio.setValue(value);
		radio.enableEvents(true);
	}

	/**
	 * Shows the values related with contents
	 */
	private void displayPageContentTypeValues() {

		PageTemplateDTO pageTemplateDto = getPageTemplate();
		ContentTypeSelDTO contentType = pageTemplateDto.getContentType();

		String ctId = PmsConstants.NULL_ID_VALUE;
		if (contentType != null) {
			ctId = contentType.getId();
		}
		ModelData ctsModelData = cbSelectedCt.getStore().findModel(ContentTypeSelModelData.PROPERTY_ID, ctId);
		cbSelectedCt.setValue(ctsModelData);

	}

	/**
	 * <br/>
	 * 
	 * @return <code>true</code> if the bound page class can be associated to categories, and <code>false</code>
	 * otherwise.
	 */
	private boolean canHaveCategory() {
		PageClass pageClass = getPageTemplate().getPageClass();
		return (pageClass.equals(PageClass.CONTENT) || pageClass.equals(PageClass.CATEGORY) || pageClass
			.equals(PageClass.CONTENT_TYPE));
	}

	/**
	 * <br/>
	 * 
	 * @return <code>true</code> if the bound page class can be associated to a Content Type, and <code>false</code>
	 * otherwise.
	 */
	private boolean canHaveContentType() {
		PageClass pageClass = getPageTemplate().getPageClass();
		return pageClass.equals(PageClass.CONTENT) || pageClass.equals(PageClass.TAG)
			|| pageClass.equals(PageClass.CATEGORY) || pageClass.equals(PageClass.CONTENT_TYPE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isDirty()
	 */
	public boolean isDirty() {
		return super.isDirty() || canHaveContentType() && cbSelectedCt.isDirty() || canHaveCategory()
			&& (rgCategories.isDirty() || tfSelectedCategory.isDirty());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return getPageTemplate().getId() != null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isValid()
	 */
	@Override
	public boolean isValid() {
		boolean allItemsValid = super.isValid();
		if (!allItemsValid) {
			return false;
		}

		if (canHaveContentType()) {
			allItemsValid = cbSelectedCt.isValid();
			if (!allItemsValid) {
				return false;
			}
		}

		if (canHaveCategory()) {
			allItemsValid = rgCategories.isValid() && tfSelectedCategory.isValid();
		}

		return allItemsValid;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractPageDetailPanel
	 * #saveSpecificFieldsValues()
	 */
	@Override
	protected void saveSpecificFieldsValues() {
		PageTemplateDTO pageTemplate = getPageTemplate();

		// category
		if (categorySelected != null) {
			pageTemplate.setCategory(categorySelected);
		}

		// content type
		if (cbSelectedCt != null) {
			ContentTypeSelDTO ctsDto = null;
			ModelData ctModel = cbSelectedCt.getValue();
			if (ctModel instanceof ContentTypeSelModelData) {
				ctsDto = ((ContentTypeSelModelData) ctModel).getDTO();
			}
			pageTemplate.setContentType(ctsDto);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.AbstractPageDetailPanel
	 * #displaySpecificFieldsValues(boolean)
	 */
	@Override
	protected void displaySpecificFieldsValues(boolean firstTime) {
		// category radios:
		if (canHaveCategory()) {
			displayCategoryValues(firstTime);
		}

		// content type selector:
		if (canHaveContentType()) {
			displayPageContentTypeValues();
		}
	}

}
