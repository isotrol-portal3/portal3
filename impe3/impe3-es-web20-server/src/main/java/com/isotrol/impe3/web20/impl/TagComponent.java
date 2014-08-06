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
package com.isotrol.impe3.web20.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.model.TagNameEntity;
import com.isotrol.impe3.web20.model.TagSetEntity;
import com.isotrol.impe3.web20.server.TagKey;


/**
 * Tag component.
 * @author Andres Rodriguez.
 */
@Component
public final class TagComponent extends AbstractWeb20Service {
	/** Tag set component. */
	private final TagSetComponent tagSetComponent;
	/** Tag name component. */
	private final TagNameComponent tagNameComponent;

	/**
	 * Constructor.
	 * @param tagSetComponent Tag set component.
	 * @param tagNameComponent Tag name component.
	 */
	@Autowired
	public TagComponent(TagSetComponent tagSetComponent, TagNameComponent tagNameComponent) {
		this.tagSetComponent = tagSetComponent;
		this.tagNameComponent = tagNameComponent;
	}

	/**
	 * Returns the a tag entity by key, creating it if needed.
	 * @param key String key.
	 * @return The tag entity.
	 */
	final TagEntity get(TagKey key) {
		TagEntity entity = getDao().getTag(key);
		if (entity == null) {
			entity = new TagEntity();
			entity.setSet(findById(TagSetEntity.class, key.getSet()));
			entity.setName(findById(TagNameEntity.class, key.getName()));
			getDao().save(entity);
			flush();
		}
		return entity;
	}

	/**
	 * Returns a tag set id.
	 * @param set Tag set name.
	 * @return Tag set id.
	 */
	long getSet(String set) {
		return tagSetComponent.get(set);
	}

	/**
	 * Returns a tag name id.
	 * @param name Tag name.
	 * @return Tag name id.
	 */
	long getName(String name) {
		return tagNameComponent.get(name);
	}

	/**
	 * Returns a tag key.
	 * @param set Tag set.
	 * @param name Tag name.
	 * @return The requested key.
	 */
	TagKey getKey(String set, String name) {
		return new TagKey(tagSetComponent.get(set), tagNameComponent.get(name));
	}

	/**
	 * Returns a tag entity.
	 * @param set Tag set.
	 * @param name Tag name.
	 * @return The requested tag entity.
	 */
	TagEntity get(String set, String name) {
		return get(getKey(set, name));
	}
}
