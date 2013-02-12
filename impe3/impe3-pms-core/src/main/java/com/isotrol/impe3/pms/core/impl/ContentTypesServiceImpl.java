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


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.isotrol.impe3.pbuf.type.ContentTypeProtos.ContentTypePB;
import com.isotrol.impe3.pbuf.type.ContentTypeProtos.ContentTypesPB;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.obj.ContentTypeObject;
import com.isotrol.impe3.pms.core.obj.ContentTypesObject;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ContentTypeDfn;
import com.isotrol.impe3.pms.model.ContentTypeEdition;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.ExportJobType;


/**
 * Implementation of ContentTypesService.
 * @author Andres Rodriguez.
 */
@Service("contentTypesService")
public final class ContentTypesServiceImpl extends
	AbstractPublishableService<ContentTypeEntity, ContentTypeDfn, ContentTypeEdition> implements ContentTypesService {

	@Autowired
	private ExportJobManager exportJobManager;
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public ContentTypesServiceImpl() {
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.CONTENT_TYPE;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_SET)
	public void delete(String id) throws PMSException {
		final ContextGlobal ctx = loadContextGlobal();
		// 1 - Load content types
		final ContentTypesObject types = loadContentTypes();
		final ContentTypeObject c = types.load(id);
		final UUID uuid = c.getId();
		// 2 - Check if in use.
		InUseProviders.CONTENT_TYPE.checkUsed(ctx.isContentTypeUsed(uuid), id);
		for (PortalObject p : ctx.getPortals().values()) {
			InUseProviders.CONTENT_TYPE.checkUsed(ctx.toPortal(p.getId()).isContentTypeUsed(uuid), id);
		}
		// 3 - Delete
		loadContentType(uuid).setDeleted(true);
		// 4 - Touch environment
		getEnvironment().touchContentTypeVersion(loadUser());
		// 5 - Done
		sync();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_GET)
	public ContentTypeDTO get(String id) throws PMSException {
		return loadContentTypes().load(id).toDTO();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#getContentTypes()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_GET)
	public List<ContentTypeSelDTO> getContentTypes() throws PMSException {
		return loadContentTypes().map2sel();
	}

	private static void fill(ContentTypeDfn dfn, ContentTypeDTO dto) {
		Mappers.dto2localizedName(dto, dfn);
		dfn.setDescription(dto.getDescription());
		dfn.setRoutable(dto.isRoutable());
		dfn.setNavigable(dto.isNavigable());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#save(com.isotrol.impe3.pms.api.type.ContentTypeDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_SET)
	public ContentTypeDTO save(ContentTypeDTO dto) throws PMSException {
		checkNotNull(dto);
		validate(dto.getDefaultName());
		validate(dto.getLocalizedNames());
		final String id = dto.getId();
		ContentTypeEntity entity;
		ContentTypeDfn dfn;
		final UUID uuid;
		if (id == null) {
			entity = new ContentTypeEntity();
			dfn = new ContentTypeDfn();
			fill(dfn, dto);
			uuid = saveNewEntity(entity, dfn);
		} else {
			entity = load(id);
			uuid = entity.getId();
			Preconditions.checkArgument(entity != null);
			dfn = entity.getCurrent();
			if (isNewDfnNeeded(dfn)) {
				dfn = new ContentTypeDfn();
				fill(dfn, dto);
				saveNewDfn(entity, dfn);
			} else {
				fill(dfn, dto);
				getDao().update(dfn);
			}
		}
		touchContentType();
		sync();
		return get(uuid.toString());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#exportAll()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_GET)
	public String exportAll() throws PMSException {
		return exportJobManager.create(ExportJobType.CONTENT_TYPE_ALL, null, null, null).toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#exportSome(java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_GET)
	public String exportSome(Set<String> ids) throws PMSException {
		final Set<UUID> set = Sets.newHashSet();
		final ContentTypesObject cts = loadContentTypes();
		for (String id : ids) {
			set.add(cts.load(id).getId());
		}
		return exportJobManager.create(ExportJobType.CONTENT_TYPE, null, null, set).toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.type.ContentTypesService#importTypes(java.lang.String, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.TYPE_SET)
	public void importTypes(String fileId, boolean overwrite) throws PMSException {
		ContentTypesPB msg = fileManager.parseImportFile(fileId, ContentTypesPB.newBuilder(), true).build();
		ContentTypesObject cts = loadContentTypes();
		int imported = 0;
		try {
			for (ContentTypePB ct : msg.getContentTypesList()) {
				final UUID id;
				try {
					id = UUID.fromString(ct.getId());
				} catch (RuntimeException e) {
					continue;
				}
				ContentTypeEntity entity;
				boolean deleted = false;
				if (cts.containsKey(id)) {
					entity = load(id);
				} else {
					entity = findById(id);
					if (entity != null && entity.isDeleted()) {
						deleted = true;
						entity.setDeleted(false);
					}
				}
				if (entity != null) {
					if (!overwrite && !deleted) {
						continue;
					}
					ContentTypeDfn dfn = entity.getCurrent();
					if (isNewDfnNeeded(dfn)) {
						dfn = new ContentTypeDfn();
						fill(dfn, ct);
						saveNewDfn(entity, dfn);
					} else {
						fill(dfn, ct);
						getDao().update(dfn);
					}
					imported++;
				} else {
					entity = new ContentTypeEntity();
					entity.setId(id);
					ContentTypeDfn dfn = new ContentTypeDfn();
					fill(dfn, ct);
					saveNewEntity(entity, dfn);
					imported++;
				}
			}
			if (imported > 0) {
				touchContentType();
			}
		}
		finally {
			purge();
		}
	}

	private void fill(ContentTypeDfn dfn, ContentTypePB pb) {
		Mappers.pb2localizedName(pb.getDefaultName(), pb.getLocalizedNamesList(), dfn);
		if (pb.hasDescription()) {
			dfn.setDescription(pb.getDescription());
		}
		dfn.setRoutable(pb.getRoutable());
		dfn.setNavigable(pb.getNavigable());
	}

}
