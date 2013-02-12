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

package com.isotrol.impe3.pms.model;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.isotrol.impe3.core.support.FileCompression;


/**
 * Entity that represents an uploaded file.
 * @author Andres Rodriguez
 */
@javax.persistence.Entity
@Table(name = "FILE_DATA")
@NamedQuery(name = FileEntity.FILEID_BEFORE, query = "select e.id from FileEntity e where created < ?")
public class FileEntity extends Entity {
	/** Query: Ids of files created some time ago. */
	public static final String FILEID_BEFORE = "file.before";
	/** Creation time. */
	@Type(type = "calendar")
	@Column(name = "CREATED", updatable = false)
	private Calendar created = Calendar.getInstance();
	/** File purpose. */
	@Column(name = "PURPOSE", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private FilePurpose purpose = FilePurpose.UNKNOWN;
	/** File name. */
	@Column(name = "NAME", length = Lengths.NAME)
	private String name;
	/** Whether the file is downloadable. */
	@Column(name = "DOWNLOADABLE")
	private boolean downloadable = false;
	/** Whether the file is a bundle. */
	@Column(name = "BUNDLE")
	private boolean bundle = false;
	/** Whether the file is a compressed. */
	@Column(name = "COMPRESSED", nullable = true)
	private Boolean compressed = false;
	/** File data. */
	@Lob
	@Column(name = "FILE_DATA")
	@Basic(fetch = FetchType.LAZY)
	private byte[] data;

	/** Default constructor. */
	public FileEntity() {
	}

	/**
	 * Returns the creation time.
	 * @return The creation time.
	 */
	public Calendar getCreated() {
		return created;
	}

	/**
	 * Returns the file purpose.
	 * @return The file purpose.
	 */
	public FilePurpose getPurpose() {
		// Schema evolution: NULL purposes are configs.
		return purpose != null ? purpose : FilePurpose.CONFIG;
	}

	/**
	 * Sets the file purpose.
	 * @param name The file purpose.
	 */
	public void setPurpose(FilePurpose purpose) {
		this.purpose = checkNotNull(purpose);
	}

	/**
	 * Returns the file name.
	 * @return The file name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the file name.
	 * @param name The file name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns whether the file is downloadable.
	 * @return True if the file is downloadable.
	 */
	public boolean isDownloadable() {
		return downloadable;
	}

	/**
	 * Sets whether the file is downloadable.
	 * @param downloadable True if the file is downloadable.
	 */
	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	/**
	 * Returns whether the file is a bundle.
	 * @return True if the file is a bundle.
	 */
	public boolean isBundle() {
		return bundle;
	}

	/**
	 * Sets whether the file is a bundle.
	 * @param bundle True if the file is a bundle.
	 */
	public void setBundle(boolean bundle) {
		this.bundle = bundle;
	}

	/**
	 * Returns whether the file is a compressed.
	 * @return True if the file is a compressed.
	 */
	public boolean isCompressed() {
		return Boolean.TRUE.equals(compressed);
	}

	/**
	 * Compress the data if it is not.
	 */
	public void compress() {
		if (isCompressed()) {
			final byte[] bytes = getData();
			if (bytes != null) {
				setData(FileCompression.compress(bytes));
				compressed = Boolean.TRUE;
			}
		}
	}

	/**
	 * Returns the file data.
	 * @return The file data.
	 */
	public byte[] getData() {
		if (isCompressed()) {
			return FileCompression.decompress(data);
		}
		return data;
	}

	/**
	 * Sets the file data.
	 * @param data The file data.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
}
