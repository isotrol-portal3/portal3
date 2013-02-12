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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.pbuf.category.CategoryProtos.CategoriesPB;
import com.isotrol.impe3.pbuf.category.CategoryProtos.CategoryPB;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.core.obj.CategoryObject;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.CategoryDfn;
import com.isotrol.impe3.pms.model.CategoryEdition;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ExportJobType;
import com.isotrol.impe3.pms.model.NameValue;


/**
 * Implementation of CategoriesService.
 * @author Andres Rodriguez.
 */
@Service("categoriesService")
public final class CategoriesServiceImpl extends
	AbstractPublishableService<CategoryEntity, CategoryDfn, CategoryEdition> implements CategoriesService {

	/** Export job manager. */
	@Autowired
	private ExportJobManager exportJobManager;
	/** File manager. */
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public CategoriesServiceImpl() {
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.CATEGORY;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#delete(java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_SET)
	public CategoryTreeDTO delete(String id) throws PMSException {
		final ContextGlobal ctx = loadContextGlobal();
		// 1 - Load categories
		final CategoriesObject tree = ctx.getCategories();
		final CategoryObject c = tree.load(id);
		final UUID uuid = c.getId();
		// 2 - Check if in use.
		InUseProviders.CATEGORY.checkUsed(tree.getRoot().getId().equals(uuid), id);
		InUseProviders.CATEGORY.checkUsed(!tree.getChildrenKeys(uuid).isEmpty(), id);
		InUseProviders.CATEGORY.checkUsed(ctx.isCategoryUsed(uuid), id);
		for (PortalObject p : ctx.getPortals().values()) {
			InUseProviders.CATEGORY.checkUsed(ctx.toPortal(p.getId()).isCategoryUsed(uuid), id);
		}
		// 3 - Delete
		loadCategory(uuid).setDeleted(true);
		// 4 - Touch environment
		getEnvironment().touchCategoryVersion(loadUser());
		// 5 - Done
		sync();
		return getCategories();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_GET)
	public CategoryDTO get(String id) throws PMSException {
		return loadCategories().load(id).toDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#getCategories()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_GET)
	public CategoryTreeDTO getCategories() throws PMSException {
		return loadCategories().map2tree();
	}

	private static void dto2dfn(CategoryDTO dto, CategoryDfn dfn) {
		Mappers.dto2localizedName(dto, dfn);
		dfn.setVisible(dto.isVisible());
		dfn.setRoutable(dto.isRoutable());
	}

	private static void validate(CategoryDTO dto) {
		Preconditions.checkNotNull(dto);
		validate(dto.getDefaultName());
		validate(dto.getLocalizedNames());
	}

	private CategoryDfn getUpdateDfn(CategoryEntity entity) throws PMSException {
		final CategoryDfn dfn = entity.getCurrent();
		if (isNewDfnNeeded(dfn)) {
			CategoryDfn dfn2 = new CategoryDfn();
			dfn2.setName(new NameValue(dfn.getName()));
			dfn2.getLocalizedNames().putAll(dfn.getLocalizedNames());
			dfn2.setVisible(dfn.isVisible());
			dfn2.setRoutable(dfn.isRoutable());
			dfn2.setOrder(dfn.getOrder());
			dfn2.setParent(dfn.getParent());
			saveNewDfn(entity, dfn2);
			return dfn2;
		}
		return dfn;
	}

	private void updatePosition(CategoryEntity entity, CategoryEntity parent, int order) throws PMSException {
		final CategoryDfn dfn = getUpdateDfn(entity);
		dfn.setOrder(order);
		if (parent != null) {
			dfn.setParent(parent);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#create(com.isotrol.impe3.pms.api.category.CategoryDTO,
	 * java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_SET)
	public CategoryDTO create(CategoryDTO dto, String parentId, int order) throws PMSException {
		validate(dto);
		final CategoryEntity parentCategory;
		if (parentId == null) {
			parentCategory = getRootCategory();
		} else {
			parentCategory = load(parentId);
		}
		final CategoryEntity entity = new CategoryEntity();
		final CategoryDfn dfn = new CategoryDfn();
		dto2dfn(dto, dfn);
		dfn.setOrder(new Tree().add(parentCategory.getId(), order));
		dfn.setParent(parentCategory);
		final UUID id = saveNewEntity(entity, dfn);
		touchCategory();
		sync();
		return get(id.toString());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#move(java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_SET)
	public CategoryTreeDTO move(String categoryId, String parentId, int order) throws PMSException {
		final Tree tree = new Tree();
		final CategoryObject category = tree.categories.load(categoryId);
		final CategoryObject parent = parentId == null ? null : tree.categories.load(parentId);
		final UUID currentParentId = tree.categories.getParentKey(category.getId());
		Preconditions.checkArgument(currentParentId != null);
		final UUID categoryUUID = category.getId();
		if (parent != null) {
			final UUID parentUUID = parent.getId();
			if (!currentParentId.equals(parentUUID)) {
				tree.remove(categoryUUID);
				updatePosition(load(categoryUUID), load(parentUUID), tree.add(parentUUID, order));
			} else {
				tree.move(categoryUUID, order);
			}
		} else {
			tree.move(categoryUUID, order);
		}
		touchCategory();
		sync();
		return getCategories();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.category.CategoriesService#update(com.isotrol.impe3.pms.api.category.CategoryDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_SET)
	public CategoryDTO update(CategoryDTO dto) throws PMSException {
		validate(dto);
		final String id = dto.getId();
		final CategoryEntity entity = load(id);
		final CategoryDfn dfn = getUpdateDfn(entity);
		dto2dfn(dto, dfn);
		touchCategory();
		sync();
		return get(id);
	}

	/**
	 * @throws PMSException
	 * @see com.isotrol.impe3.pms.core.CategoryManager#getSelectableCategories()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public CategoryTreeDTO getSelectableCategories() throws PMSException {
		return getCategories();
	}

	private class Tree {
		private final CategoriesObject categories = loadCategories();

		Tree() {
		}

		private void setOrder(UUID id, int order) throws PMSException {
			final CategoryEntity entity = load(id);
			if (entity.getCurrent().getOrder() != order) {
				updatePosition(entity, null, order);
			}
		}

		int add(UUID parentId, int order) throws PMSException {
			final int o = Math.max(0, order);
			final List<UUID> children = categories.getChildrenKeys(parentId);
			final int n = children.size();
			if (o > n) {
				return n;
			}
			for (int i = 0; i < n; i++) {
				final int newOrder = (i < o) ? i : i + 1;
				setOrder(children.get(i), newOrder);
			}
			return o;
		}

		private List<UUID> siblings(UUID id) {
			return categories.getChildrenKeys(categories.getParentKey(id));
		}

		void remove(UUID id) throws PMSException {
			final List<UUID> children = siblings(id);
			final int o = children.indexOf(id);
			final int n = children.size();
			for (int i = 0; i < n; i++) {
				if (i != o) {
					final int newOrder = (i < o) ? i : i - 1;
					setOrder(children.get(i), newOrder);
				}
			}
		}

		void move(UUID id, int order) throws PMSException {
			final List<UUID> children = siblings(id);
			final int n = children.size();
			// Normalize list
			for (int i = 0; i < n; i++) {
				setOrder(children.get(i), i);
			}
			final int current = load(id).getCurrent().getOrder();
			final int o = Math.min(Math.max(0, order), n - 1);
			if (o == current) {
				return;
			}
			setOrder(id, o);
			if (o < current) {
				for (int i = o; i < current; i++) {
					setOrder(children.get(i), i + 1);
				}
			} else {
				for (int i = current + 1; i <= o; i++) {
					setOrder(children.get(i), i - 1);
				}
			}
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_GET)
	public String exportBranch(String id, boolean exportRoot, boolean allLevels) throws PMSException {
		// 1 - Check the requeste node exist.
		final CategoryObject root = loadCategories().load(id);
		// 2 - Select job
		final ExportJobType type;
		if (exportRoot) {
			type = allLevels ? ExportJobType.CATEGORY_NODE_ALL : ExportJobType.CATEGORY_NODE;
		} else {
			type = allLevels ? ExportJobType.CATEGORY_LEVEL_ALL : ExportJobType.CATEGORY_LEVEL;
		}
		// 3 - Create job
		return exportJobManager.create(type, root.getId(), null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CATEGORY_SET)
	public void importCategories(String id, String fileId, boolean sameLevel, boolean overwrite) throws PMSException {
		// Load "root" category
		final CategoriesObject categories = loadCategories();
		final CategoryObject root = categories.load(id);
		final UUID parentId = categories.getParentKey(root.getId());
		checkArgument(!(sameLevel && parentId == null));
		// Load import file.
		final CategoriesPB msg = fileManager.parseImportFile(fileId, CategoriesPB.newBuilder(), true).build();
		// Calculate real parent
		final UUID realParentId = sameLevel ? parentId : root.getId();
		// Import categories
		try {
			for (CategoryPB pb : msg.getCategoriesList()) {
				importOne(pb, realParentId, overwrite);
			}
		}
		finally {
			purge();
		}
	}

	private void importOne(CategoryPB pb, UUID parentId, boolean overwrite) throws PMSException {
		final UUID id = NotFoundProviders.CATEGORY.toUUID(pb.getId());
		final CategoriesObject categories = loadCategories();
		final CategoryEntity parent = load(parentId);
		if (categories.getRoot().getId().equals(id)) {
			// TODO
			return;
		}
		// Import
		CategoryEntity entity;
		CategoryDfn dfn = null;
		if (categories.containsKey(id)) {
			if (overwrite) {
				entity = load(id);
				dfn = getUpdateDfn(entity);
				pb2dfn(dfn, pb);
				dfn.setOrder(categories.getChildrenKeys(parentId).size());
				dfn.setParent(parent);
			}
		} else {
			entity = findById(id);
			if (entity != null) {
				if (entity.isDeleted()) {
					entity.setDeleted(false);
					dfn = getUpdateDfn(entity);
					pb2dfn(dfn, pb);
					dfn.setOrder(categories.getChildrenKeys(parentId).size());
					dfn.setParent(parent);
				}
			} else {
				entity = new CategoryEntity();
				entity.setId(id);
				dfn = new CategoryDfn();
				pb2dfn(dfn, pb);
				dfn.setOrder(categories.getChildrenKeys(parentId).size());
				dfn.setParent(parent);
				saveNewEntity(entity, dfn);
			}
		}
		if (dfn != null) {
			touchCategory();
			sync();
			// Children
			for (CategoryPB child : pb.getChildrenList()) {
				importOne(child, id, overwrite);
			}
		}

	}

	private void pb2dfn(CategoryDfn dfn, CategoryPB pb) {
		Mappers.pb2localizedName(pb.getDefaultName(), pb.getLocalizedNamesList(), dfn);
		dfn.setRoutable(pb.getRoutable());
		dfn.setVisible(pb.getVisible());
	}

}
