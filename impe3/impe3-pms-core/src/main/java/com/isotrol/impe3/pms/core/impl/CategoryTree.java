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


import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.derquinsej.collect.ImmutableHierarchy;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.pms.api.category.CategorySelDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.model.CategoryEntity;


/**
 * Internal representacion of a category tree.
 * @author Andres Rodriguez
 */
final class CategoryTree {
	private static final Ordering<CategoryEntity> BY_ORDER;

	static {
		final Ordering<Integer> oi = Ordering.natural();
		final Function<CategoryEntity, Integer> order = new Function<CategoryEntity, Integer>() {
			public Integer apply(CategoryEntity from) {
				return from.getCurrent().getOrder();
			}
		};
		BY_ORDER = oi.onResultOf(order);
	}

	private final CategoryTreeDTO root;
	private final Map<String, CategoryTreeDTO> nodes;

	/**
	 * Constructor.
	 * @param categories Loaded categories.
	 * @param mapper Transformation function.
	 */
	CategoryTree(final Iterable<CategoryEntity> categories, final Function<CategoryEntity, CategorySelDTO> mapper) {
		final List<CategoryEntity> sorted = Lists.newArrayList(categories);
		Collections.sort(sorted, BY_ORDER);
		System.out.println("---+++---");
		for (CategoryEntity c : sorted) {
			System.out
				.printf("%s %s %s\n", c.getId(), c.getCurrent().getName().getName(), c.getCurrent().getParentId());
		}
		System.out.println("---+++---");
		ImmutableHierarchy.Builder<String, CategorySelDTO> builder = ImmutableHierarchy.builder();
		for (CategoryEntity category : sorted) {
			final CategorySelDTO dto = mapper.apply(category);
			final UUID parentUUID = category.getCurrent().getParentId();
			final String parentId = (parentUUID == null) ? null : parentUUID.toString();
			builder.add(dto.getId(), dto, parentId);
		}
		final ImmutableHierarchy<String, CategorySelDTO> h = builder.get();
		this.nodes = Maps.newHashMap();
		final Function<CategorySelDTO, CategoryTreeDTO> tree = new Function<CategorySelDTO, CategoryTreeDTO>() {
			public CategoryTreeDTO apply(CategorySelDTO from) {
				final String id = from.getId();
				final CategoryTreeDTO dto = new CategoryTreeDTO();
				dto.setNode(from);
				dto.setChildren(Lists.newArrayList(Iterables.transform(h.getChildren(id), this)));
				nodes.put(id, dto);
				return dto;
			}
		};
		root = tree.apply(h.getFirstLevel().get(0));
	}

	/**
	 * Returns the root node.
	 * @return The root node.
	 */
	public CategoryTreeDTO getRoot() {
		return root;
	}

	/**
	 * Returns a node by id.
	 * @param id Id
	 * @return The root node.
	 */
	public CategoryTreeDTO getNode(UUID id) {
		return nodes.get(id);
	}
}
