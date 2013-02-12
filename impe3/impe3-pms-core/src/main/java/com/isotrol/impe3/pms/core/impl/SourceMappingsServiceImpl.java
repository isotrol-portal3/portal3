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


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.isotrol.impe3.pbuf.BaseProtos.StringEntryPB;
import com.isotrol.impe3.pbuf.mappings.MappingProtos.MappingPB;
import com.isotrol.impe3.pbuf.mappings.MappingProtos.MappingsPB;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.smap.CategoryMappingDTO;
import com.isotrol.impe3.pms.api.smap.ContentTypeMappingDTO;
import com.isotrol.impe3.pms.api.smap.SetMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingSelDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingsService;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.obj.Context1;
import com.isotrol.impe3.pms.core.obj.SourceMappingsObject;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.CategoryMappingValue;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.ContentTypeMappingValue;
import com.isotrol.impe3.pms.model.ExportJobType;
import com.isotrol.impe3.pms.model.SetMappingValue;
import com.isotrol.impe3.pms.model.SourceMappingEntity;


/**
 * Implementation of SourceMappingsService.
 * @author Andres Rodriguez.
 * @author Emilio Escobar Reyero.
 */
@Service("sourceMappingsService")
public final class SourceMappingsServiceImpl extends AbstractOfEnvironmentService<SourceMappingEntity> implements
	SourceMappingsService {

	@Autowired
	private ExportJobManager exportJobManager;
	@Autowired
	private FileManager fileManager;

	public void setExportJobManager(ExportJobManager exportJobManager) {
		this.exportJobManager = exportJobManager;
	}

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	/** Default constructor. */
	public SourceMappingsServiceImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.api.smap.SourceMappingsService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public void delete(String id) throws PMSException {
		final SourceMappingEntity entity = load(id);
		entity.getEnvironment().touchMappingVersion(loadUser());
		delete(entity);
	}

	private SourceMappingTemplateDTO newTemplate(final SourceMappingDTO dto) {
		final SourceMappingTemplateDTO template = new SourceMappingTemplateDTO();
		template.setMapping(dto);
		template.setContentTypes(loadContentTypes().map2sel());
		template.setCategories(loadCategories().map2tree());
		return template;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.smap.SourceMappingsService#newTemplate()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public SourceMappingTemplateDTO newTemplate() throws PMSException {
		final SourceMappingDTO dto = new SourceMappingDTO();
		dto.setSets(new ArrayList<SetMappingDTO>(0));
		dto.setContentTypes(new ArrayList<ContentTypeMappingDTO>(0));
		dto.setCategories(new ArrayList<CategoryMappingDTO>(0));
		return newTemplate(dto);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.smap.SourceMappingsService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public SourceMappingTemplateDTO get(String id) throws PMSException {
		final Context1 ctx = loadContext1();
		final SourceMappingDTO dto = ctx.getMappings().load(id).toDTO(ctx);
		final SourceMappingTemplateDTO template = newTemplate(dto);
		return template;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.smap.SourceMappingsService#getSourceMappings()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public List<SourceMappingSelDTO> getSourceMappings() throws PMSException {
		return loadMappings().map2sel();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.smap.SourceMappingsService#save(com.isotrol.impe3.pms.api.smap.SourceMappingDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public SourceMappingTemplateDTO save(SourceMappingDTO dto) throws PMSException {
		Preconditions.checkNotNull(dto);
		Preconditions.checkNotNull(dto.getName());
		final String id = dto.getId();
		final boolean isNew;
		final SourceMappingEntity entity;
		final List<SetMappingValue> sets;
		final List<ContentTypeMappingValue> contentTypes;
		final List<CategoryMappingValue> categories;
		if (id == null) {
			isNew = true;
			entity = new SourceMappingEntity();
			sets = Lists.newArrayList();
			contentTypes = Lists.newArrayList();
			categories = Lists.newArrayList();
			entity.setCategories(categories);
			entity.setContentTypes(contentTypes);
			entity.setSets(sets);
		} else {
			isNew = false;
			entity = load(id);
			sets = entity.getSets();
			contentTypes = entity.getContentTypes();
			categories = entity.getCategories();
			sets.clear();
			contentTypes.clear();
			categories.clear();
		}
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		final List<SetMappingDTO> setsDTO = dto.getSets();
		if (setsDTO != null && !setsDTO.isEmpty()) {
			for (SetMappingDTO from : setsDTO) {
				SetMappingValue value = new SetMappingValue();
				value.setSet(from.getSet());
				value.setMapping(from.getMapping());
				sets.add(value);
			}
		}
		final List<ContentTypeMappingDTO> contentTypesDTO = dto.getContentTypes();
		if (contentTypesDTO != null && !contentTypesDTO.isEmpty()) {
			for (ContentTypeMappingDTO from : contentTypesDTO) {
				ContentTypeMappingValue value = new ContentTypeMappingValue();
				value.setMapping(from.getMapping());
				final ContentTypeEntity contentType = loadContentType(from.getContentType().getId());
				value.setContentType(contentType);
				contentTypes.add(value);
			}
		}
		final List<CategoryMappingDTO> categoriesDTO = dto.getCategories();
		if (categoriesDTO != null && !categoriesDTO.isEmpty()) {
			for (CategoryMappingDTO from : categoriesDTO) {
				CategoryMappingValue value = new CategoryMappingValue();
				value.setMapping(from.getMapping());
				final CategoryEntity category = loadCategory(from.getCategory().getId());
				value.setCategory(category);
				categories.add(value);
			}
		}
		if (isNew) {
			saveNew(entity);
		}
		entity.getEnvironment().touchMappingVersion(loadUser());
		sync();
		return get(entity.getId().toString());
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public String exportAll() throws PMSException {
		return exportJobManager.create(ExportJobType.MAPPING_ALL, null, null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public String exportSome(Set<String> ids) throws PMSException {
		final SourceMappingsObject mpps = loadContextGlobal().getMappings();
		final Set<UUID> set = Sets.newHashSet();
		for (String id : ids) {
			set.add(mpps.load(id).getId());
		}

		return exportJobManager.create(ExportJobType.MAPPING, null, null, set).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public String exportCategories(String id) throws PMSException {
		return exportJobManager.create(ExportJobType.MAPPING_CATEGORIES, NotFoundProviders.MAPPING.toUUID(id), null,
			null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public String exportContentTypes(String id) throws PMSException {
		return exportJobManager.create(ExportJobType.MAPPING_CONTENT_TYPES, NotFoundProviders.MAPPING.toUUID(id),
			null, null).toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_GET)
	public String exportSets(String id) throws PMSException {
		return exportJobManager.create(ExportJobType.MAPPING_SETS, NotFoundProviders.MAPPING.toUUID(id), null, null)
			.toString();
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public void importMappings(String fileId, boolean overwrite) throws PMSException {
		MappingsPB msg = fileManager.parseImportFile(fileId, MappingsPB.newBuilder(), true).build();
		SourceMappingsObject mps = loadMappings();
		int imported = 0;
		try {
			for (MappingPB mp : msg.getMappingsList()) {
				final UUID id;
				try {
					id = UUID.fromString(mp.getId());
				} catch (RuntimeException e) {
					continue;
				}

				SourceMappingEntity entity;
				List<SetMappingValue> sets;
				List<ContentTypeMappingValue> contentTypes;
				List<CategoryMappingValue> categories;
				boolean isNew = false;
				if (mps.containsKey(id)) {
					entity = load(id);
				} else {
					entity = null;
				}
				if (entity != null) {
					if (!overwrite) {
						continue;
					}
					sets = entity.getSets();
					contentTypes = entity.getContentTypes();
					categories = entity.getCategories();
					sets.clear();
					contentTypes.clear();
					categories.clear();
				} else {
					isNew = true;
					entity = new SourceMappingEntity();
					entity.setId(id);
					sets = Lists.newArrayList();
					contentTypes = Lists.newArrayList();
					categories = Lists.newArrayList();
					entity.setCategories(categories);
					entity.setContentTypes(contentTypes);
					entity.setSets(sets);


					imported++;
				}
				if (mp.hasName()) {
					entity.setName(mp.getName());
				}
				if (mp.hasDescription()) {
					entity.setDescription(mp.getDescription());
				}
				final List<StringEntryPB> setsPB = mp.getSetsList();
				if (setsPB != null && !setsPB.isEmpty()) {
					for (StringEntryPB from : setsPB) {
						SetMappingValue value = new SetMappingValue();
						value.setSet(from.getKey());
						value.setMapping(from.getValue());
						sets.add(value);
					}
				}
				final List<StringEntryPB> contentTypesPB = mp.getContentTypesList();
				if (contentTypesPB != null && !contentTypesPB.isEmpty()) {
					for (StringEntryPB from : contentTypesPB) {
						ContentTypeMappingValue value = new ContentTypeMappingValue();
						value.setMapping(from.getValue());
						final ContentTypeEntity contentType = loadContentType(from.getKey());
						value.setContentType(contentType);
						contentTypes.add(value);
					}
				}
				final List<StringEntryPB> categoriesPB = mp.getCategoriesList();
				if (categoriesPB != null && !categoriesPB.isEmpty()) {
					for (StringEntryPB from : categoriesPB) {
						CategoryMappingValue value = new CategoryMappingValue();
						value.setMapping(from.getValue());
						final CategoryEntity category = loadCategory(from.getKey());
						value.setCategory(category);
						categories.add(value);
					}
				}
				if (isNew) {
					saveNew(entity);
				}
				imported++;
				if (imported > 0) {
					entity.getEnvironment().touchMappingVersion(loadUser());
					sync();
				}
			}
		} finally {
			purge();
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public void importSets(String mappingId, String fileId) throws PMSException {
		final UUID id = NotFoundProviders.MAPPING.toUUID(mappingId);
		final MappingPB msg = fileManager.parseImportFile(fileId, MappingPB.newBuilder(), true).build();
		final SourceMappingEntity entity = load(id);
		if (entity != null) {
			final List<SetMappingValue> sets = entity.getSets();
			final List<StringEntryPB> setsPB = msg.getSetsList();
			if (setsPB != null && !setsPB.isEmpty()) {
				for (StringEntryPB from : setsPB) {
					SetMappingValue value = new SetMappingValue();
					value.setSet(from.getKey());
					value.setMapping(from.getValue());
					sets.add(value);
				}
			}
			entity.getEnvironment().touchMappingVersion(loadUser());
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public void importContentTypes(String mappingId, String fileId) throws PMSException {
		final UUID id = NotFoundProviders.MAPPING.toUUID(mappingId);
		final MappingPB msg = fileManager.parseImportFile(fileId, MappingPB.newBuilder(), true).build();
		final SourceMappingEntity entity = load(id);
		if (entity != null) {
			final List<ContentTypeMappingValue> contentTypes = entity.getContentTypes();
			final List<StringEntryPB> contentTypesPB = msg.getContentTypesList();
			if (contentTypesPB != null && !contentTypesPB.isEmpty()) {
				for (StringEntryPB from : contentTypesPB) {
					ContentTypeMappingValue value = new ContentTypeMappingValue();
					value.setMapping(from.getValue());
					final ContentTypeEntity contentType = loadContentType(from.getKey());
					value.setContentType(contentType);
					contentTypes.add(value);
				}
			}
			entity.getEnvironment().touchMappingVersion(loadUser());
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.SMAP_SET)
	public void importCategories(String mappingId, String fileId) throws PMSException {
		final UUID id = NotFoundProviders.MAPPING.toUUID(mappingId);
		final MappingPB msg = fileManager.parseImportFile(fileId, MappingPB.newBuilder(), true).build();
		final SourceMappingEntity entity = load(id);
		if (entity != null) {
			final List<CategoryMappingValue> categories = entity.getCategories();
			final List<StringEntryPB> categoriesPB = msg.getCategoriesList();
			if (categoriesPB != null && !categoriesPB.isEmpty()) {
				for (StringEntryPB from : categoriesPB) {
					CategoryMappingValue value = new CategoryMappingValue();
					value.setMapping(from.getValue());
					final CategoryEntity category = loadCategory(from.getKey());
					value.setCategory(category);
					categories.add(value);
				}
			}
			entity.getEnvironment().touchMappingVersion(loadUser());
		}
	}

}
