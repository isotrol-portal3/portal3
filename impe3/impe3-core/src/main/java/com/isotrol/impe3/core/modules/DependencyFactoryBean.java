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

package com.isotrol.impe3.core.modules;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import com.google.common.base.Preconditions;


/**
 * Spring factory bean to supply a dependency. All dependencies are singletons.
 * @author Andres Rodriguez.
 */
public class DependencyFactoryBean implements FactoryBean {
	private final Class<?> type;
	private final Object value;

	/**
	 * Returns the bean definition to supply a dependency.
	 * @param type Dependency type.
	 * @param value Dependency value.
	 * @return A bean definition.
	 */
	static BeanDefinition getDefinition(Class<?> type, Object value) {
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(value);
		final GenericBeanDefinition definition = new GenericBeanDefinition();
		definition.setBeanClass(DependencyFactoryBean.class);
		final ConstructorArgumentValues cavs = new ConstructorArgumentValues();
		cavs.addIndexedArgumentValue(0, type);
		cavs.addIndexedArgumentValue(1, value);
		definition.setConstructorArgumentValues(cavs);
		definition.validate();
		return definition;
	}

	/**
	 * Default constructor.
	 * @param type Dependency type.
	 * @param value Dependency value.
	 */
	public DependencyFactoryBean(Class<?> type, Object value) {
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(value);
		this.type = type;
		this.value = value;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public Object getObject() throws Exception {
		return value;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return type;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}
}
