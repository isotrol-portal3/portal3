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


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.dnd.TreePanelDragSource;
import com.extjs.gxt.ui.client.dnd.TreePanelDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateItemDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.ComponentInPageTemplateModelData;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.store.ComponentTreeStore;


/**
 * Tree with the components dependences relations
 * 
 * @author Manuel Ruiz
 * 
 * <dl> <dt><b>Events:</b></dt> <dd><b>Change:</b> fired when one node is added, removed or updated. This event is not
 * fired after bulk operations (method {@link #populate(List)}, called during {@link #init(List)})</dd> </dl>
 * 
 */
public class ComponentsDependencesTree extends TreePanel<ComponentInPageTemplateModelData> {

	/**
	 * Component's name maximum length  
	 */
	private static final int NAME_MAX_LENGTH = 100;

	/**
	 * A tree drop target for this tree.<br/>
	 */
	private TreePanelDropTarget dropTarget = null;

	/*
	 * Injected deps
	 */
	/**
	 * PMS specific bundle.<br/>
	 */
	private PmsStyles pmsStyles = null;

	/**
	 * Styles bundle.
	 */
	private GuiCommonStyles styles = null;

	/**
	 * PMS specific messages bundle.<br/>
	 */
	private PmsMessages pmsMessages = null;

	/**
	 * Default constructor
	 * @param components
	 */
	public ComponentsDependencesTree() {
		super(new ComponentTreeStore());
		getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	/**
	 * Inits the widget. Must be called after the properties are injected.
	 * @param com
	 * @param dd
	 */
	public void init(List<ComponentInPageTemplateDTO> com) {
		populate(com);
		configThis();
	}

	/**
	 * Creates and configures a binder for the current tree, with the bound {@link #store}.<br/>
	 */
	private void configThis() {

		getStyle().setLeafIcon(IconHelper.createStyle(styles.iconTreeFolder()));

		setIconProvider(new ModelIconProvider<ComponentInPageTemplateModelData>() {
			/**
			 * Returns an icon for the passed model, according to its configuration: if config and name lenght are OK,
			 * returns a gear icon; if model has wrong configuration (contains required configuration items set to
			 * <code>null</code>) or wrong length, the icon will have a small warning symbol.
			 * @param model
			 * @return an icon for the passed model.
			 */
			public AbstractImagePrototype getIcon(ComponentInPageTemplateModelData model) {
				String style = null;
				if (isConfigOk(model.getDTO()) && isNameLengthOk(model.getDTO())) {
					style = pmsStyles.iconComponentTreeFolder();
				} else {
					style = pmsStyles.iconWrongComponentTreeFolder();
				}

				return IconHelper.createStyle(style);
			}
		});

		getStore().addStoreListener(new StoreListener<ComponentInPageTemplateModelData>() {
			@Override
			public void storeAdd(StoreEvent<ComponentInPageTemplateModelData> se) {
				fireChangeEvent();
			}

			@Override
			public void storeRemove(StoreEvent<ComponentInPageTemplateModelData> se) {
				fireChangeEvent();
			}

			@Override
			public void storeUpdate(StoreEvent<ComponentInPageTemplateModelData> se) {
				fireChangeEvent();
			}
		});

		setLabelProvider(new ModelStringProvider<ComponentInPageTemplateModelData>() {

			/**
			 * @param model
			 * @param property
			 * @return a html with the component name as title and the component in page name as visible string
			 */
			public String getStringValue(ComponentInPageTemplateModelData model, String property) {
				String title = "";
				PaletteDTO component = model.getDTO().getComponent();
				if (!isNameLengthOk(model.getDTO())) {
					title = pmsMessages.msgErrorComponentNameMaxLength(String.valueOf(NAME_MAX_LENGTH));
				} else if (!isConfigOk(model.getDTO())) {
					title = pmsMessages.msgRequirdedComponentConfiguration();
				} else if (component != null && component.getName() != null) {
					title = component.getName();
				}
				String name = model.getDTO().getName();

				return "<span title='" + title + "'>" + name + "<span>";
			}
		});

		configDND();

		// expand 1st root node:
		setExpanded(getStore().getRootItems().get(0), true);
	}

	/**
	 * Configures DragNDrop for the current tree.<br/>
	 */
	private void configDND() {
		TreePanelDragSource source = new TreePanelDragSource(this);

		source.addDNDListener(new DNDListener() {
			/**
			 * Forbids "Drag" for root item.<br/> (non-Javadoc)
			 * @see com.extjs.gxt.ui.client.event.DNDListener#dragStart(com.extjs.gxt.ui.client.event.DNDEvent)
			 */
			@Override
			public void dragStart(DNDEvent e) {
				List<ComponentInPageTemplateModelData> rootModels = getStore().getRootItems();
				ComponentInPageTemplateModelData draggedModel = findNode(e.getTarget()).getModel();
				if (rootModels.contains(draggedModel)) {
					e.setCancelled(true);
					e.getStatus().setStatus(false);
					return;
				}
			}
		});

		dropTarget = new TreePanelDropTarget(this);
		dropTarget.setAllowSelfAsSource(true);
		dropTarget.setAllowDropOnLeaf(true);
		dropTarget.setFeedback(Feedback.BOTH);
		dropTarget.setAutoExpand(true);
		dropTarget.addDNDListener(new DNDListener() {
			@Override
			public void dragMove(DNDEvent e) {
				Element target = e.getTarget();
				TreeNode targetNode = findNode(target);
				boolean cancel = false;
				if (targetNode != null && targetNode.getModel().getDTO().isSpace()) {
					cancel = true;
				}
				e.getStatus().setStatus(!cancel);
				e.setCancelled(cancel);
			}
		});
	}

	/**
	 * Add the child dto to the new parent dto. Remove the child dto from the old parent dto. Modify the store
	 * 
	 * @param componentParent
	 * @param childModelData
	 */
	public void addNode(ComponentInPageTemplateModelData parent, ComponentInPageTemplateModelData child) {

		// modifies the item's dto
		List<ComponentInPageTemplateDTO> parentChildren = parent.getDTO().getChildren();
		if (parentChildren == null) {
			parentChildren = new LinkedList<ComponentInPageTemplateDTO>();
		}
		parentChildren.add(child.getDTO());
		parent.getDTO().setChildren(parentChildren);

		// change the parent node and remove the child dto from its old parent
		ComponentInPageTemplateModelData oldParent = child.getParent();
		if (oldParent != null) {
			List<ComponentInPageTemplateDTO> listChildren = oldParent.getDTO().getChildren();
			listChildren.remove(child.getDTO());
			oldParent.getDTO().setChildren(listChildren);
		}
		child.setParent(parent);

		// modifies the store
		getStore().add(parent, child, true);
	}

	/**
	 * Initializes the store. Creates a new item as root parent for the passed Components in Page.
	 */
	private void populate(List<ComponentInPageTemplateDTO> components) {
		ComponentInPageTemplateDTO dto = createRootDTO();
		dto.setChildren(components);

		disableEvents(true);
		getStore().removeAll();
		getStore().add(dto, true);
		enableEvents(true);
	}

	/**
	 * Creates a root model for the tree.<br/>
	 * @return
	 */
	private ComponentInPageTemplateDTO createRootDTO() {
		ComponentInPageTemplateDTO rootDto = new ComponentInPageTemplateDTO();
		rootDto.setName(pmsMessages.nodeComponentsDependencesTreeRootItemLabel());
		PaletteDTO paletteComponent = new PaletteDTO();
		ComponentKey paletteComponentKey = new ComponentKey();
		paletteComponentKey.setBean(pmsMessages.nodeComponentsDependencesTreeRootItemKey());
		paletteComponent.setKey(paletteComponentKey);
		rootDto.setComponent(paletteComponent);

		return rootDto;
	}

	/**
	 * Remove a model from the tree store
	 * @param item the model data to remove
	 */
	public void removeItem(ComponentInPageTemplateModelData item) {
		ComponentInPageTemplateModelData parent = item.getParent();

		// remove the dto
		List<ComponentInPageTemplateDTO> children = parent.getDTO().getChildren();
		children.remove(item.getDTO());
		parent.getDTO().setChildren(children);

		// remove the modeldata
		getStore().remove(parent, item);
	}

	/**
	 * Adds a Drag'n Drop listener to the DropTarget.<br/>
	 * @param dndListener
	 */
	public void addDNDListener(DNDListener listener) {
		dropTarget.addDNDListener(listener);
	}

	/**
	 * Constructs equivalent tree of class ComponentInPageTemplateDTO.<br/>
	 * @return equivalent tree of class ComponentInPageTemplateDTO
	 */
	public List<ComponentInPageTemplateDTO> computeComponentsInPageTree() {

		List<ComponentInPageTemplateDTO> res = new LinkedList<ComponentInPageTemplateDTO>();

		ComponentInPageTemplateModelData rootItem = getStore().getRootItems().get(0); // root visual item ("/Reference")
		List<ComponentInPageTemplateModelData> depth1Items = getStore().getChildren(rootItem);

		for (ComponentInPageTemplateModelData item : depth1Items) {
			ComponentInPageTemplateDTO dto = computeComponentInPageTemplateDto(item);
			res.add(dto);
		}

		return res;
	}

	/**
	 * <br/>
	 * @param item
	 * @return
	 */
	private ComponentInPageTemplateDTO computeComponentInPageTemplateDto(ComponentInPageTemplateModelData item) {

		ComponentInPageTemplateDTO dto = item.getDTO();
		List<ComponentInPageTemplateDTO> childDtos = new LinkedList<ComponentInPageTemplateDTO>();

		List<ComponentInPageTemplateModelData> childModels = getStore().getChildren(item);
		for (ComponentInPageTemplateModelData childModel : childModels) {
			ComponentInPageTemplateDTO childDto = computeComponentInPageTemplateDto(childModel);
			childDtos.add(childDto);
		}
		dto.setChildren(childDtos);

		return dto;
	}

	/**
	 * fires a Change event.
	 */
	private void fireChangeEvent() {
		BaseEvent event = new BaseEvent(this);
		fireEvent(Events.Change, event);
	}

	/**
	 * @return the store
	 */
	public ComponentTreeStore getStore() {
		return (ComponentTreeStore) super.getStore();
	};

	/**
	 * <br/>
	 * @param dto
	 * @return <code>true</code>, if the current configuration of the passed DTO is correct; <code>false</code>,
	 * otherwise.
	 */
	private boolean isConfigOk(ComponentInPageTemplateDTO dto) {
		ConfigurationTemplateDTO config = dto.getConfiguration();

		if (config == null) {
			return true;
		}

		boolean ok = true;
		Iterator<ConfigurationTemplateItemDTO> it = config.getItems().iterator();
		while (ok && it.hasNext()) {
			ConfigurationTemplateItemDTO item = it.next();
			if (item.isRequired()) {
				ok = valueOf(item) != null;
			}
		}

		return ok;
	}

	/**
	 * Return true if the component's name has a right lentgh.
	 * @param dto the component
	 * @return <code>true</code>, if the component's name has a right lentgh; <code>false</code>, otherwise.
	 */
	private boolean isNameLengthOk(ComponentInPageTemplateDTO dto) {
		return dto.getName().length() <= NAME_MAX_LENGTH;
	}

	/**
	 * <br/>
	 * @param item
	 * @return
	 */
	private Object valueOf(ConfigurationTemplateItemDTO item) {
		Object value = null;
		switch (item.getType()) {
			case BOOLEAN:
				value = item.getBoolean();
				break;
			case CATEGORY:
				value = item.getCategory();
				break;
			case CONTENT_TYPE:
				value = item.getContentType();
				break;
			case FILE:
				value = item.getFile();
				break;
			case INTEGER:
				value = item.getInteger();
				break;
			case STRING:
				value = item.getString();
				break;
			case CHOICE:
				value = item.getChoice();
				break;
			default:
				// null
		}
		return value;
	}

	/**
	 * <br/>
	 * @return <code>true</code>, if all items have a correct configuration; <code>false</code> otherwise.
	 */
	public boolean isValid() {
		boolean valid = true;

		List<ComponentInPageTemplateModelData> allItems = getStore().getAllItems();
		allItems.removeAll(getStore().getRootItems());
		Iterator<ComponentInPageTemplateModelData> it = allItems.iterator();
		while (valid && it.hasNext()) {
			ComponentInPageTemplateDTO dto = it.next().getDTO();
			valid = isConfigOk(dto) && isNameLengthOk(dto);
		}

		return valid;
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
	 * Injects the PMS specific messages bundle.
	 * @param pmsMessages
	 */
	@Inject
	public void setPmsMessages(PmsMessages pmsMessages) {
		this.pmsMessages = pmsMessages;
	}

	/**
	 * Injects the styles bundle.
	 * 
	 * @param styles
	 */
	@Inject
	public void setStyles(GuiCommonStyles styles) {
		this.styles = styles;
	}
}
