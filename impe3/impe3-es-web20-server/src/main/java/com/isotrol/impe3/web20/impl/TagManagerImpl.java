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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.isotrol.impe3.web20.model.ResourceEntity;
import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.model.TagNameEntity;
import com.isotrol.impe3.web20.server.TagKey;
import com.isotrol.impe3.web20.server.TagManager;
import com.isotrol.impe3.web20.server.TagMap;


/**
 * Implementation of TagManager.
 * @author Andres Rodriguez.
 */
@Service("tagManager")
public final class TagManagerImpl extends AbstractResourceManager implements TagManager {
	@Autowired
	private TagComponent tagComponent;

	/** Constructor. */
	public TagManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#addTags(java.lang.String, java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addTag(String set, String tag, boolean valid) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(tag)) {
			return;
		}
		tagComponent.get(set, tag).setValid(valid);
	}
	
	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#updateTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void updateTag(String set, String tag, String name) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(tag)) {
			return;
		}
		checkArgument(StringUtils.hasText(name));
		final TagKey k0 = getKey(set, tag);
		TagEntity tag0 = getDao().getTag(k0);
		if (tag0 == null) {
			return;
		}
		final TagKey k1 = getKey(set, name);
		TagNameEntity tne = findById(TagNameEntity.class, k1.getName());
		TagEntity tag1 = getDao().getTag(k1);
		if (tag1 == null) {
			tag0.setName(tne);
			return;
		}
		// Tag fusion
		tag1 = new TagEntity();
		tag1.setSet(tag0.getSet());
		tag1.setName(tne);
		tag1.setValid(tag0.isValid());
		getDao().save(tag1);
		flush();
		// Tag fusion 2: the resources
		for (ResourceEntity r : tag0.getResources()) {
			Set<TagEntity> tags = r.getTags();
			tags.remove(tag0);
			tags.add(tag1);
		}
		// Deletion
		getDao().delete(tag0);
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.web20.server.TagManager#deleteTag(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteTag(String set, String tag) {
		if (!StringUtils.hasText(set) || !StringUtils.hasText(tag)) {
			return;
		}
		final TagKey key = getKey(set, tag);
		TagEntity te = getDao().getTag(key);
		if (te == null) {
			return;
		}
		for (ResourceEntity r : te.getResources()) {
			Set<TagEntity> tags = r.getTags();
			tags.remove(te);
		}
		getDao().delete(te);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#tag(java.lang.String, java.lang.String, java.util.Set, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void tag(String resource, String set, Set<String> tags, boolean valid) {
		checkNotNull(resource);
		checkNotNull(set);
		if (tags == null) {
			tags = ImmutableSet.of();
		}
		final long setId = tagComponent.getSet(set);
		final ResourceEntity entity = findById(ResourceEntity.class, getResource(resource));
		final Set<TagEntity> previous = entity.getTags();
		final Set<TagEntity> newSet = Sets.newHashSet();
		for (TagEntity tag : entity.getTags()) {
			if (setId != tag.getSet().getId()) {
				newSet.add(tag);
			}
		}
		for (String name : tags) {
			final long nameId = tagComponent.getName(name);
			final TagEntity tag = tagComponent.get(new TagKey(setId, nameId));
			if (valid) {
				tag.setValid(true);
			}
			newSet.add(tag);
		}
		previous.clear();
		previous.addAll(newSet);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#loadAll()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public TagMap loadAll() {
		final TagMap.Builder builder = TagMap.builder().addAll(getDao().loadAppliedTags());
		return builder.get();
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getSet(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public long getSet(String set) {
		return tagComponent.getSet(set);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getName(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public long getName(String name) {
		return tagComponent.getName(name);
	}

	/**
	 * @see com.isotrol.impe3.web20.server.TagManager#getKey(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public TagKey getKey(String set, String name) {
		return tagComponent.getKey(set, name);
	}

}
