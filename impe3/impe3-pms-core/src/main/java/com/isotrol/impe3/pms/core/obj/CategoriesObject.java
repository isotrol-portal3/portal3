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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;
import static com.isotrol.impe3.pms.core.support.NotFoundProviders.CATEGORY;

import java.util.List;
import java.util.UUID;

import net.sf.derquinsej.collect.Hierarchies;
import net.sf.derquinsej.collect.Hierarchy;
import net.sf.derquinsej.collect.HierarchyVisitor;
import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.api.Categories;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.core.impl.AbstractIdentifiableHierarchy;
import com.isotrol.impe3.core.impl.CategoriesFactory;
import com.isotrol.impe3.pbuf.category.CategoryProtos.CategoriesPB;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.core.support.EntityFunctions;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.CategoryDfn;
import com.isotrol.impe3.pms.model.CategoryEntity;


/**
 * Collection of content types domain object.
 * @author Andres Rodriguez
 */
public final class CategoriesObject extends AbstractIdentifiableHierarchy<CategoryObject> {
	private static final Ordering<CategoryDfn> BY_ORDER = Ordering.natural().onResultOf(
		new Function<CategoryDfn, Integer>() {
			public Integer apply(CategoryDfn from) {
				return from.getOrder();
			}
		});

	private static final Joiner JOINER = Joiner.on('/');

	private final ImmutableHierarchy<UUID, CategoryObject> hierarchy;

	/**
	 * Builds a collection from a set of definitions.
	 * @param dfns Definitions.
	 * @param function State function to apply.
	 * @return The requested collection.
	 */
	public static CategoriesObject definitions(Iterable<CategoryDfn> dfns) {
		return new CategoriesObject(dfns);
	}

	/**
	 * Builds a collection from a set of current definitions.
	 * @param entities Entities.
	 * @param function State function to apply.
	 */
	public static CategoriesObject current(Iterable<CategoryEntity> entities) {
		return definitions(transform(entities, EntityFunctions.CATEGORY2DFN));
	}

	/** API collection. */
	private volatile Categories categories = null;

	/**
	 * Constructor.
	 * @param dfns Definitions.
	 * @param function State function to apply.
	 */
	private CategoriesObject(Iterable<CategoryDfn> dfns) {
		final Iterable<CategoryDfn> ordered = BY_ORDER.sortedCopy(dfns);
		ImmutableHierarchy.Builder<UUID, CategoryObject> builder = ImmutableHierarchy.builder();
		for (CategoryDfn dfn : ordered) {
			builder.add(dfn.getEntity().getId(), new CategoryObject(dfn), dfn.getParentId());
		}
		hierarchy = builder.get();
	}

	@Override
	protected Hierarchy<UUID, CategoryObject> delegate() {
		return hierarchy;
	}

	/**
	 * Returns the root category.
	 * @return The root category or {@code null} if the hierarchy is empty.
	 */
	public CategoryObject getRoot() {
		if (isEmpty()) {
			return null;
		}
		return getFirstLevel().get(0);
	}

	public CategoryObject load(UUID id) throws EntityNotFoundException {
		return CATEGORY.checkNotNull(get(id), id);
	}

	public CategoryObject load(CategoryEntity e) {
		if (e == null) {
			return null;
		}
		return get(e.getId());
	}

	public CategoryObject load(String id) throws EntityNotFoundException {
		return load(CATEGORY.toUUID(id));
	}

	/** Returns the category path using the display name as segments. */
	public String getDisplayPath(UUID id) {
		CategoryObject c = hierarchy.get(id);
		checkArgument(c != null, "Category [%s] not found", id);
		StringBuilder b = new StringBuilder("/");
		List<String> list = Lists.newLinkedList();
		CategoryObject p = hierarchy.getParent(id);
		while (p != null) {
			list.add(0, c.getDefaultName().getDisplayName());
			c = p;
			p = hierarchy.getParent(c.getId());
		}
		JOINER.appendTo(b, list);
		return b.toString();
	}

	public CategoryTreeDTO map2tree() {
		if (isEmpty()) {
			return null;
		}
		final Function<CategoryObject, CategoryTreeDTO> tree = new Function<CategoryObject, CategoryTreeDTO>() {
			public CategoryTreeDTO apply(CategoryObject from) {
				final CategoryTreeDTO dto = new CategoryTreeDTO();
				dto.setNode(from.toSelDTO());
				dto.setChildren(Mappers.list(hierarchy.getChildren(from.getId()), this));
				return dto;
			}
		};
		return tree.apply(hierarchy.getFirstLevel().get(0));
	}

	public CategorySelDTO map2sel(CategoryEntity e) {
		if (e == null) {
			return null;
		}
		return load(e).toSelDTO();
	}

	public Categories map2api() {
		if (categories == null) {
			final ImmutableHierarchy.Builder<UUID, Category> builder = ImmutableHierarchy.builder();
			final HierarchyVisitor<UUID, CategoryObject> v = new HierarchyVisitor<UUID, CategoryObject>() {
				public void visit(UUID key, CategoryObject value, UUID parentKey, int position) {
					builder.add(key, CategoryObject.MAP2API.apply(value), hierarchy.getParentKey(key));
				}
			};
			Hierarchies.visitDepthFirst(hierarchy, v);
			categories = CategoriesFactory.of(builder.get());
		}
		return categories;
	}

	public CategoriesPB map2pb(UUID id, boolean exportRoot, boolean allLevels) throws PMSException {
		// 1 - Check the requested node exist.
		final CategoryObject root = load(id);
		// 2 - Calculate levels
		int levels;
		if (allLevels) {
			levels = Integer.MAX_VALUE;
		} else {
			levels = exportRoot ? 2 : 1;
		}
		// 3 - Create builder
		CategoriesPB.Builder b = CategoriesPB.newBuilder();
		// 4 - Populate
		if (exportRoot) {
			b.addCategories(root.toPB(this, levels));
		} else {
			b.addAllCategories(transform(getChildren(id), CategoryObject.map2pb(this, levels)));
		}
		// 5 - Done
		return b.build();
	}

}
