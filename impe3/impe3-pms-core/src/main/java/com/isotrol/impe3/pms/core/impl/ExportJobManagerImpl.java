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


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.protobuf.MessageLite;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.ExportResult;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.obj.ComponentObject;
import com.isotrol.impe3.pms.core.obj.ConnectorObject;
import com.isotrol.impe3.pms.core.obj.ContentTypeObject;
import com.isotrol.impe3.pms.core.obj.SourceMappingObject;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ExportJobEntity;
import com.isotrol.impe3.pms.model.ExportJobType;


/**
 * Implementation of Export Job Manager.
 * @author Andres Rodriguez.
 */
@Service
public class ExportJobManagerImpl extends AbstractEntityService<ExportJobEntity> implements ExportJobManager {
	@Autowired
	private FileManager fileManager;

	/**
	 * @see com.isotrol.impe3.pms.core.ExportJobManager#create(com.isotrol.impe3.pms.model.ExportJobType,
	 * java.util.UUID, java.util.UUID, java.lang.Iterable)
	 */
	public UUID create(ExportJobType type, UUID mainId, UUID otherId, Iterable<UUID> objects) throws PMSException {
		final ExportJobEntity entity = new ExportJobEntity();
		entity.setType(type);
		entity.setMainId(mainId);
		entity.setOtherId(otherId);
		if (objects != null) {
			Iterables.addAll(entity.getObjects(), objects);
		}
		saveNew(entity);
		sync();
		return entity.getId();
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ExportJobManager#export(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public ExportResult export(String jobId) throws PMSException {
		return export(load(jobId));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ExportJobManager#export(java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public ExportResult export(UUID jobId) throws PMSException {
		return export(load(jobId));
	}

	private ExportResult export(ExportJobEntity job) throws PMSException {
		NotFoundProviders.DEFAULT.checkCondition(Objects.equal(getUserId(), job.getCreated().getUser().getId()),
			job.getId());
		final MessageLite msg;
		switch (job.getType()) {
			case CONTENT_TYPE:
				msg = loadContentTypes()
					.toPB(Predicates.compose(Predicates.in(job.getObjects()), ContentTypeObject.ID));
				break;
			case CONTENT_TYPE_ALL:
				msg = loadContentTypes().toPB();
				break;
			case CONNECTOR:
				msg = loadConnectors().toPB(fileManager,
					Predicates.compose(Predicates.in(job.getObjects()), ConnectorObject.ID));
				break;
			case CONNECTOR_ALL:
				msg = loadConnectors().toPB(fileManager);
				break;
			case MAPPING:
				msg = loadMappings().toPB(Predicates.compose(Predicates.in(job.getObjects()), SourceMappingObject.ID));
				break;
			case MAPPING_ALL:
				msg = loadMappings().toPB();
				break;
			case MAPPING_SETS:
				msg = loadMappings().get(job.getMainId()).toSetsPB();
				break;
			case MAPPING_CATEGORIES:
				msg = loadMappings().get(job.getMainId()).toCategoriesPB();
				break;
			case MAPPING_CONTENT_TYPES:
				msg = loadMappings().get(job.getMainId()).toContentTypesPB();
				break;
			case CATEGORY_LEVEL:
				msg = loadCategories().map2pb(job.getMainId(), false, false);
				break;
			case CATEGORY_LEVEL_ALL:
				msg = loadCategories().map2pb(job.getMainId(), false, true);
				break;
			case CATEGORY_NODE:
				msg = loadCategories().map2pb(job.getMainId(), true, false);
				break;
			case CATEGORY_NODE_ALL:
				msg = loadCategories().map2pb(job.getMainId(), true, true);
				break;
			case PORTAL_NAME:
				msg = loadContextGlobal().toPortal(job.getMainId()).toPortalNamePB();
				break;
			case PORTAL_BASE:
				msg = loadContextGlobal().toPortal(job.getMainId()).exportBases();
				break;
			case PORTAL_PROP:
				msg = loadContextGlobal().toPortal(job.getMainId()).exportProperties();
				break;
			case PORTAL_SET:
				msg = loadContextGlobal().toPortal(job.getMainId()).exportSets();
				break;
			case PORTAL_PAGE_ALL:
				msg = loadContextGlobal().toPortal(job.getMainId()).toDevice(job.getOtherId()).exportPages(fileManager);
				break;
			case COMPONENT:
				msg = loadContextGlobal().toPortal(job.getMainId()).getComponents()
					.exportOwned(fileManager, Predicates.compose(Predicates.in(job.getObjects()), ComponentObject.ID));
				break;
			case COMPONENT_ALL:
				msg = loadContextGlobal().toPortal(job.getMainId()).getComponents().exportOwned(fileManager);
				break;
			case COMPONENT_OVR:
				msg = loadContextGlobal()
					.toPortal(job.getMainId())
					.getComponents()
					.exportOverriden(fileManager,
						Predicates.compose(Predicates.in(job.getObjects()), ComponentObject.ID));
				break;
			case COMPONENT_OVR_ALL:
				msg = loadContextGlobal().toPortal(job.getMainId()).getComponents().exportOverriden(fileManager);
				break;
			default:
				throw new EntityNotFoundException();
		}
		delete(job);
		return ExportResult.create(job.getType().getFileName(), msg);
	}
}
