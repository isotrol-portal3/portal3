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

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.model.ExportJobType;


/**
 * Export jobs manager.
 * @author Andres Rodriguez.
 */
public interface ExportJobManager {
	/**
	 * Creates a new export job.
	 * @param type Job type.
	 * @param mainId Main object id.
	 * @param otherId Secondary id.
	 * @param objects Other objects.
	 * @return The id of the created job.
	 */
	UUID create(ExportJobType type, UUID mainId, UUID otherId, Iterable<UUID> objects) throws PMSException;

	/**
	 * Executes an export job. If executed successfully, the export job is deleted.
	 * @param jobId Export job id.
	 * @return The message to export.
	 */
	ExportResult export(String jobId) throws PMSException;

	/**
	 * Executes an export job. If executed successfully, the export job is deleted.
	 * @param jobId Export job id.
	 * @return The message to export.
	 */
	ExportResult export(UUID jobId) throws PMSException;
}
