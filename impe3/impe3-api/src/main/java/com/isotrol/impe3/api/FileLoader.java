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

package com.isotrol.impe3.api;


import java.util.UUID;


/**
 * Internal file loader service.
 * @author Andres Rodriguez
 */
public interface FileLoader {
	/**
	 * Loads a file.
	 * @param id File Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the file is not found or is a bundle.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileData load(FileId id);

	/**
	 * Loads a file.
	 * @param id File Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the file is not found or is a bundle.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileData load(UUID id);

	/**
	 * Loads a file bundle.
	 * @param id File Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the file is not found.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileBundleData loadBundle(FileId id);

	/**
	 * Loads a file bundle.
	 * @param id File Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the file is not found.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileBundleData loadBundle(UUID id);

	/**
	 * Loads a file from a bundle.
	 * @param id File Bundle Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the provided id is not from a bundle.
	 * @throws IllegalArgumentException if the file is not found.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileData loadFromBundle(FileId id, String name);

	/**
	 * Loads a file from a bundle.
	 * @param id File Bundle Id.
	 * @return The loaded file.
	 * @throws IllegalArgumentException if the provided id is not from a bundle.
	 * @throws IllegalArgumentException if the file is not found.
	 * @throws UnableToLoadFileException if an exception is thrown during file loading.
	 */
	FileData loadFromBundle(UUID id, String name);

}
