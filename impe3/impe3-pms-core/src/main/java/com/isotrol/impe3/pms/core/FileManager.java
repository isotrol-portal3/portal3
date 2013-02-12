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

package com.isotrol.impe3.pms.core;


import java.util.UUID;

import com.google.protobuf.Message;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;


/**
 * Interface for the file manager.
 * @author Andres Rodriguez.
 */
public interface FileManager {
	/**
	 * Uploads a file.
	 * @param name File name.
	 * @param data File data.
	 * @return The uploaded file id and name.
	 */
	UploadedFileDTO upload(String name, byte[] data) throws PMSException;

	/**
	 * Returns the contents of an uploaded file.
	 * @param id File id.
	 * @param name File name.
	 * @return The file data.
	 * @throws PMSException
	 */
	FileData getFile(String id) throws PMSException;

	/**
	 * Returns the contents of an uploaded file.
	 * @param id File id.
	 * @param name File name.
	 * @return The file data.
	 * @throws PMSException
	 */
	FileData getFile(UUID id) throws PMSException;

	/**
	 * Loads and parse an uploaded import file.
	 * @param id File id.
	 * @param builder Builder to use to parse the file.
	 * @param delete Whether to delete the file after a succesful parsing.
	 */
	<B extends Message.Builder> B parseImportFile(String id, B builder, boolean delete) throws PMSException;
	
	/**
	 * Purge files older than a certain age.
	 * @param age Age in seconds.
	 * @return Number of files deleted.
	 * @throws PMSException
	 */
	int purge(int age) throws PMSException;
	
	

}
