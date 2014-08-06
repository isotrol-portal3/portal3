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
package com.isotrol.impe3.oi.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.es.common.server.AbstractService;
import com.isotrol.impe3.oi.dao.DAO;
import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.ClassNameEntity;
import com.isotrol.impe3.oi.model.ClassSetEntity;
import com.isotrol.impe3.oi.server.ClassKey;



/**
 * Class component.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Component
public final class ClassComponent extends AbstractService<DAO> {
	/** Class set component. */
	private final ClassSetComponent classSetComponent;
	/** Class name component. */
	private final ClassNameComponent classNameComponent;

	/**
	 * Constructor.
	 * @param classSetComponent Class set component.
	 * @param classNameComponent Class name component.
	 */
	@Autowired
	public ClassComponent(ClassSetComponent classSetComponent, ClassNameComponent classNameComponent) {
		this.classSetComponent = classSetComponent;
		this.classNameComponent = classNameComponent;
	}

	/**
	 * Returns the a class entity by key, creating it if needed.
	 * @param key String key.
	 * @return The class entity.
	 */
	final ClassEntity get(ClassKey key) {
		ClassEntity entity = getDao().getClassification(key);
		if (entity == null) {
			entity = new ClassEntity();
			entity.setSet(findById(ClassSetEntity.class, key.getSet()));
			entity.setName(findById(ClassNameEntity.class, key.getName()));
			getDao().save(entity);
			flush();
		}
		return entity;
	}

	/**
	 * Returns a class set id.
	 * @param set Class set name.
	 * @return Class set id.
	 */
	long getSet(String set) {
		return classSetComponent.get(set);
	}

	/**
	 * Returns a class name id.
	 * @param name Class name.
	 * @return Class name id.
	 */
	long getName(String name) {
		return classNameComponent.get(name);
	}

	/**
	 * Returns a class key.
	 * @param set Class set.
	 * @param name Class name.
	 * @return The requested key.
	 */
	ClassKey getKey(String set, String name) {
		return new ClassKey(classSetComponent.get(set), classNameComponent.get(name));
	}

	/**
	 * Returns a class entity.
	 * @param set Class set.
	 * @param name Class name.
	 * @return The requested class entity.
	 */
	ClassEntity get(String set, String name) {
		return get(getKey(set, name));
	}
}
