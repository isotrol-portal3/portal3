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


import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.isotrol.impe3.oi.model.ClassEntity;
import com.isotrol.impe3.oi.model.ClassNameEntity;
import com.isotrol.impe3.oi.model.InterviewEntity;
import com.isotrol.impe3.oi.server.ClassKey;
import com.isotrol.impe3.oi.server.ClassManager;


/**
 * Implementation of ClassManager.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero
 */
@Service("oiClassManager")
public final class ClassManagerImpl extends AbstractOiService implements ClassManager {
	@Autowired
	private ClassComponent classComponent;

	/** Constructor. */
	public ClassManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#addTags(java.lang.String, java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addClass(String set, String classification) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(classification)) {
			return;
		}
		classComponent.get(set, classification);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#updateTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void updateClass(String set, String classification, String name) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(classification)) {
			return;
		}
		checkArgument(StringUtils.hasText(name));
		final ClassKey k0 = getKey(set, classification);
		ClassEntity class0 = getDao().getClassification(k0);
		if (class0 == null) {
			return;
		}
		final ClassKey k1 = getKey(set, name);
		ClassNameEntity tne = findById(ClassNameEntity.class, k1.getName());
		ClassEntity class1 = getDao().getClassification(k1);
		if (class1 == null) {
			class0.setName(tne);
			return;
		}
		// Class fusion
		class1 = new ClassEntity();
		class1.setSet(class0.getSet());
		class1.setName(tne);
		getDao().save(class1);
		flush();
		// Class fusion 2: the resources
		for (InterviewEntity r : class0.getInterviews()) {
			Set<ClassEntity> classes = r.getClasses();
			classes.remove(class0);
			classes.add(class1);
		}
		// Deletion
		getDao().delete(class0);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.web20.server.TagManager#deleteTag(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteClass(String set, String classification) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(classification)) {
			return;
		}
		final ClassKey key = getKey(set, classification);
		ClassEntity te = getDao().getClassification(key);
		if (te == null) {
			return;
		}
		for (InterviewEntity r : te.getInterviews()) {
			Set<ClassEntity> classes = r.getClasses();
			classes.remove(te);
		}
		getDao().delete(te);
	}


	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getSet(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public long getSet(String set) {
		return classComponent.getSet(set);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getName(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public long getName(String name) {
		return classComponent.getName(name);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getKey(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ClassKey getKey(String set, String name) {
		return classComponent.getKey(set, name);
	}

	public void setClassComponent(ClassComponent classComponent) {
		this.classComponent = classComponent;
	}
	
}
