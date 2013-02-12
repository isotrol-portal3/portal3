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

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.Message;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.support.FileLoaderSupport;
import com.isotrol.impe3.pms.api.InvalidImportFileException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.FileEntity;
import com.isotrol.impe3.pms.model.FilePurpose;


/**
 * Implementation of FileManager.
 * @author Andres Rodriguez.
 */
@Service("fileManager")
public final class FileManagerImpl extends AbstractService implements FileManager {
	/**
	 * Constructor.
	 */
	public FileManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.FileManager#upload(java.lang.String, byte[])
	 */
	@Transactional(rollbackFor = Throwable.class)
	public UploadedFileDTO upload(String name, byte[] data) throws PMSException {
		checkNotNull(name);
		checkNotNull(data);
		checkArgument(data.length > 0);
		flush();
		final FileEntity entity = new FileEntity();
		entity.setName(name);
		entity.setData(data);
		saveNewEntity(entity);
		flush();
		return Mappers.UPLOADED_FILE.apply(entity);
	}

	private FileData getFile(FileEntity entity) throws PMSException {
		final String savedName = entity.getName();
		return new FileData(entity.getId(), savedName, FileLoaderSupport.getMediaType(savedName),
			entity.isDownloadable(), entity.isBundle(), entity.getData(), false);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.FileManager#getFile(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public FileData getFile(String id) throws PMSException {
		// TODO complete
		final FileEntity entity = load(FileEntity.class, id, NotFoundProviders.FILE);
		return getFile(entity);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.FileManager#getFile(java.util.UUID)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public FileData getFile(UUID id) throws PMSException {
		// TODO complete
		final FileEntity entity = load(FileEntity.class, id, NotFoundProviders.FILE);
		return getFile(entity);
	}

	/**
	 * @see com.isotrol.impe3.pms.core.FileManager#parseImportFile(java.lang.String,
	 * com.google.protobuf.Message.Builder, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public <B extends Message.Builder> B parseImportFile(String id, B builder, boolean delete) throws PMSException {
		final FileEntity entity = load(FileEntity.class, id, NotFoundProviders.FILE);
		if (entity.getPurpose() == FilePurpose.UNKNOWN) {
			entity.setPurpose(FilePurpose.IMPORT);
		}
		NotFoundProviders.FILE.checkCondition(entity.getPurpose() == FilePurpose.IMPORT, id);
		try {
			builder.mergeFrom(entity.getData());
		} catch (IOException e) {
			throw new InvalidImportFileException();
		}
		if (delete) {
			deleteEntity(entity);
		}
		return builder;
	}
	
	/**
	 * @see com.isotrol.impe3.pms.core.FileManager#purge(int)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public int purge(int age) throws PMSException {
		Set<UUID> all = getDao().getUploadedFiles(age);
		Loggers.pms().info(String.format("Purging files: found %d candidate files in the system", all.size()));
		if (all.isEmpty()) {
			return 0;
		}
		all.removeAll(getDao().getUsedFiles());
		for (UUID id : all) {
			FileEntity f = findById(FileEntity.class, id);
			Loggers.pms().warn(String.format("Purging file [%s/%s]", id, f.getName()));
			deleteEntity(findById(FileEntity.class, id));
			// Clear first level cache
			sync();
		}
		return all.size();
	}
}
