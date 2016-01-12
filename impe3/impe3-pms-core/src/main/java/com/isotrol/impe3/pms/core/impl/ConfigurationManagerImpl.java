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


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition.Item;
import com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB;
import com.isotrol.impe3.pbuf.BaseProtos.ConfigurationValuePB;
import com.isotrol.impe3.pbuf.BaseProtos.FileContentPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.PortalConfigurationManager;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.ConfigurationValue;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.FileEntity;
import com.isotrol.impe3.pms.model.FilePurpose;


/**
 * Implementation of PortalConfigurationManager.
 * @author Enrique Diaz.
 */
@Service("portalConfigurationManager")
public final class ConfigurationManagerImpl extends AbstractEntityService<ConfigurationEntity> implements
	PortalConfigurationManager {

	/** Logger. */
	private final Logger logger = Loggers.pms();
	/** File manager. */
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public ConfigurationManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#create(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * java.util.Collection)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity create(PortalConfigurationDefinition<?> cd, Collection<ConfigurationItemDTO> configuration)
		throws PMSException {
		final ConfigurationEntity entity = new ConfigurationEntity();
		setValues(cd, entity, configuration);
		saveNew(entity);
		return entity;
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#create(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity create(PortalConfigurationDefinition<?> cd, ConfigurationPB configuration) throws PMSException {
		return create(cd, pb2dto(configuration));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#update(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * com.isotrol.impe3.pms.model.ConfigurationEntity, java.util.Collection)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity update(PortalConfigurationDefinition<?> cd, ConfigurationEntity entity,
		Collection<ConfigurationItemDTO> configuration) throws PMSException {
		setValues(cd, entity, configuration);
		return entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#update(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * com.isotrol.impe3.pms.model.ConfigurationEntity, com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity update(PortalConfigurationDefinition<?> cd, ConfigurationEntity entity,
		ConfigurationPB configuration) throws PMSException {
		return update(cd, entity, pb2dto(configuration));
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#update(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * java.util.UUID, java.util.Collection)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity update(PortalConfigurationDefinition<?> cd, UUID id,
		Collection<ConfigurationItemDTO> configuration) throws PMSException {
		return update(cd, load(id), configuration);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#update(com.isotrol.impe3.core.config.ConfigurationDefinition,
	 * java.util.UUID, com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity update(PortalConfigurationDefinition<?> cd, UUID id, ConfigurationPB configuration)
		throws PMSException {
		return update(cd, load(id), configuration);
	}

	private void setValues(PortalConfigurationDefinition<?> cd, ConfigurationEntity entity,
		Collection<ConfigurationItemDTO> configuration) throws PMSException {
		checkNotNull(configuration);
		checkArgument(!configuration.isEmpty() || cd.getMBPParameters().isEmpty());
		final Map<String, ConfigurationValue> values = entity.getValues();
		values.clear();
		final ImmutableMap<String, Item> parameters = cd.getParameters();
		for (final ConfigurationItemDTO itemDTO : configuration) {
			final String name = itemDTO.getKey();
			checkNotNull(name);
			checkArgument(!values.containsKey(name));
			checkArgument(parameters.containsKey(name));
			final Item item = parameters.get(name);
			final String value = itemDTO.getCurrentValue();
			if (value == null) {
				if (item.isMVP()) {
					logger.error("Configuration [{}] type [{}]: Required item [{}].", new Object[] {
						entity.getId().toString(), cd.getType().getName(), name});
					throw new IllegalArgumentException(); // TODO
				}
				continue;
			}
			final ConfigurationValue configValue = new ConfigurationValue();
			final Class<?> type = item.getType();
			if (item.isEnum()) {
				configValue.setStringValue(((Enum<?>)item.fromString(value)).name());
			} else if (String.class == type) {
				configValue.setStringValue(value);
			} else if (Integer.class == type) {
				configValue.setIntegerValue(Integer.valueOf(value));
			} else if (FileId.class == type) {
				FileEntity file = load(FileEntity.class, value, NotFoundProviders.FILE);
				if (file.getPurpose() == FilePurpose.UNKNOWN) {
					file.setPurpose(FilePurpose.CONFIG);
				}
				NotFoundProviders.FILE.checkCondition(file.getPurpose() == FilePurpose.CONFIG, value);
				if (item.isCompressed()) {
					file.compress();
				}
				file.setBundle(item.isBundle());
				file.setDownloadable(item.isDownloadable());
				configValue.setFileValue(file);
			} else if (Category.class == type) {
				configValue.setCategoryValue(load(CategoryEntity.class, value, NotFoundProviders.CATEGORY));
			} else if (ContentType.class == type) {
				configValue.setContentTypeValue(load(ContentTypeEntity.class, value, NotFoundProviders.CONTENT_TYPE));
			} else if (Boolean.class == type) {
				configValue.setBooleanValue(Boolean.valueOf(value));
			} else {
				logger.error("Configuration [{}] type [{}] item [{}] Invalid type.", new Object[] {
					entity.getId().toString(), cd.getType().getName(), name});
				// TODO: other types.
				throw new IllegalStateException();
			}
			values.put(name, configValue);
		}
		for (final Item item : parameters.values()) {
			checkState(!item.isMVP() || values.containsKey(item.getParameter()));
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#delete(com.isotrol.impe3.pms.model.ConfigurationEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void delete(ConfigurationEntity entity) {
		getDao().delete(entity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<ConfigurationItemDTO> pb2dto(ConfigurationPB configuration) throws PMSException {
		if (configuration == null) {
			return null;
		}
		final int n = configuration.getConfigurationValuesCount();
		if (n == 0) {
			return ImmutableList.of();
		}
		final List<ConfigurationItemDTO> list = Lists.newArrayListWithCapacity(n);
		for (final ConfigurationValuePB value : configuration.getConfigurationValuesList()) {
			final ConfigurationItemDTO dto = new ConfigurationItemDTO();
			if (value.hasCtFile()) {
				final FileContentPB filePB = value.getCtFile();
				final UploadedFileDTO uf = fileManager.upload(filePB.getName(), filePB.getContent().toByteArray());
				dto.setCurrentValue(uf.getId());
			} else if (value.hasCtString()) {
				dto.setCurrentValue(value.getCtString());
			} else if (value.hasCtInteger()) {
				dto.setCurrentValue(Integer.toString(value.getCtInteger()));
			} else if (value.hasCtBoolean()) {
				dto.setCurrentValue(Boolean.toString(value.getCtBoolean()));
			} else {
				continue;
			}
			dto.setKey(value.getName());
			list.add(dto);
		}
		return list;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.ConfigurationManager#duplicate(com.isotrol.impe3.pms.model.ConfigurationEntity)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public ConfigurationEntity duplicate(ConfigurationEntity entity) throws PMSException {
		if (entity == null) {
			return null;
		}
		final ConfigurationEntity dup = new ConfigurationEntity();
		for (Entry<String, ConfigurationValue> entry : entity.getValues().entrySet()) {
			dup.getValues().put(entry.getKey(), entry.getValue().duplicate());
		}
		saveNew(dup);
		return dup;
	}

}
