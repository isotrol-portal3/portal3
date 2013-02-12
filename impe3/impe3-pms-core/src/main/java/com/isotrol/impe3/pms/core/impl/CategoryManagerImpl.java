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


import static com.google.common.collect.Iterables.transform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.CategoryManager;
import com.isotrol.impe3.pms.core.UserManager;
import com.isotrol.impe3.pms.core.obj.CategoriesObject;
import com.isotrol.impe3.pms.model.CategoryDfn;
import com.isotrol.impe3.pms.model.CategoryEdition;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.EditionEntity;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.NameValue;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of Category Manager.
 * @author Andres Rodriguez.
 */
@Component
public final class CategoryManagerImpl extends AbstractStateLoaderComponent<CategoriesObject> implements
	CategoryManager {
	private static final Function<CategoryEdition, CategoryDfn> CATEGORY = new Function<CategoryEdition, CategoryDfn>() {
		public CategoryDfn apply(CategoryEdition from) {
			return from.getPublished();
		};
	};

	private UserManager userManager;
	/** Loader. */
	private final CategoriesLoader loader;

	/** Default constructor. */
	public CategoryManagerImpl() {
		this.loader = new CategoriesLoader();
	}

	@Override
	CategoriesLoader getLoader() {
		return loader;
	}

	@Override
	int getOfflineVersion(EnvironmentEntity e) {
		return e.getCategoryVersion();
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.CategoryManager#getRoot(com.isotrol.impe3.pms.model.EnvironmentEntity)
	 */
	public CategoryEntity getRoot(EnvironmentEntity environment) throws PMSException {
		CategoryEntity root = getDao().getRootCategory(environment.getId());
		if (root == null) {
			final UserEntity rootUser = userManager.getRootUser();
			root = new CategoryEntity();
			root.setEnvironment(environment);
			root.setCreated(rootUser);
			saveNewEntity(root);
			flush();
			final CategoryDfn dfn = new CategoryDfn();
			dfn.setEntity(root);
			dfn.setUpdated(rootUser);
			dfn.setName(new NameValue(NAME, PATH));
			dfn.setOrder(0);
			dfn.setRoutable(true);
			dfn.setVisible(true);
			dfn.setParent(null);
			saveNewEntity(dfn);
			flush();
			root.setCurrent(dfn);
			flush();
		}
		return root;
	}

	private class CategoriesLoader implements Loader<CategoriesObject> {
		public CategoriesObject load(EnvironmentEntity e) {
			return CategoriesObject.current(getDao().getOfflineCategories(e.getId()));
		}

		public CategoriesObject load(EditionEntity e) {
			return CategoriesObject.definitions(transform(e.getCategories(), CATEGORY));
		}

		public String toString() {
			return "Categories";
		}
	}

}
