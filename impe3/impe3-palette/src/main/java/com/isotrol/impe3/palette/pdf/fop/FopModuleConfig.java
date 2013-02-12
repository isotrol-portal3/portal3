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

package com.isotrol.impe3.palette.pdf.fop;


import com.isotrol.impe3.api.FileBundle;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.palette.binary.ModuleConfig;


/**
 * Base configuration interface for modules with FOP-based PDF renderers.
 * @author Andres Rodriguez
 */
public interface FopModuleConfig extends ModuleConfig {
	/** Bundle of referenced files. */
	@Name("Recursos Referenciados")
	@Description("Paquete con recursos referenciados desde las plantillas con URI portalStream:nombre")
	@FileBundle
	@Optional
	FileId fopResources();
}
