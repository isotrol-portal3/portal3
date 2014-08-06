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


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.TagDTO;
import com.isotrol.impe3.web20.api.TagsService;
import com.isotrol.impe3.web20.api.UsedTagDTO;
import com.isotrol.impe3.web20.mappers.TagDTOMapper;
import com.isotrol.impe3.web20.model.TagEntity;
import com.isotrol.impe3.web20.server.TagManager;
import com.isotrol.impe3.web20.server.TagMap;


/**
 * Implementation of TagsService.
 * @author Andres Rodriguez.
 */
@Service("tagsService")
public final class TagsServiceImpl extends AbstractWeb20Service implements TagsService, InitializingBean {
	@Autowired
	private TagManager tagManager;
	/** Scheduler. */
	@Autowired
	private SchedulerComponent scheduler;
	/** Global tag map. */
	private volatile TagMap globalMap = TagMap.builder().get();

	/** Constructor. */
	public TagsServiceImpl() {
	}

	public void afterPropertiesSet() throws Exception {
		final Runnable global = new Runnable() {
			public void run() {
				globalMap = tagManager.loadAll();
			}
		};
		scheduler.scheduleWithFixedDelay(global, 0L, 10L, TimeUnit.SECONDS);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#addTag(java.lang.String, java.lang.String, java.lang.String,
	 * boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addTag(String serviceId, String set, String tag, boolean valid) throws ServiceException {
		tagManager.addTag(set, tag, valid);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#addTags(java.lang.String, java.lang.String, java.util.Set, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void addTags(String serviceId, String set, Set<String> tags, boolean valid) throws ServiceException {
		if (tags != null) {
			for (String tag : tags) {
				tagManager.addTag(set, tag, valid);
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#updateTag(java.lang.String, java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void updateTag(String serviceId, String set, String tag, String name) throws ServiceException {
		tagManager.updateTag(set, tag, name);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#deleteTag(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteTag(String serviceId, String set, String tag) throws ServiceException {
		tagManager.deleteTag(set, tag);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#deleteTags(java.lang.String, java.lang.String, java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void deleteTags(String serviceId, String set, Set<String> tags) throws ServiceException {
		if (tags != null) {
			for (String tag : tags) {
				tagManager.deleteTag(set, tag);
			}
		}
	}

	private Iterable<TagEntity> loadSet(String set) {
		checkNotNull(set);
		return getDao().loadTagsBySetName(set);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#getTagSet(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<TagDTO> getTagSet(String serviceId, String set) throws ServiceException {
		return newArrayList(transform(loadSet(set), TagDTOMapper.INSTANCE));
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#getPendingTags(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<TagDTO> getPendingTags(String serviceId, String set) throws ServiceException {
		return newArrayList(transform(filter(loadSet(set), not(TagEntity.IS_VALID)), TagDTOMapper.INSTANCE));
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#tag(java.lang.String, java.lang.String, java.lang.String,
	 * java.util.Set, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void tag(String serviceId, String resource, String set, Set<String> tags, boolean valid)
		throws ServiceException {
		tagManager.tag(resource, set, tags, true);
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#getMostUsed(java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<UsedTagDTO> getMostUsed(String serviceId, String set, int max) throws ServiceException {
		final long setId = tagManager.getSet(set);
		return newArrayList(globalMap.get(setId, max));
	}

	/**
	 * @see com.isotrol.impe3.web20.api.TagsService#suggest(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public List<UsedTagDTO> suggest(String serviceId, String set, String prefix, int max) throws ServiceException {
		final long setId = tagManager.getSet(set);
		return globalMap.suggest(setId, prefix, max);
	}
}
