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

package com.isotrol.impe3.palette.cloud;

import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.support.WithURIGenerator;
import com.isotrol.impe3.freemarker.WithFreeMarker;
import com.isotrol.impe3.support.nr.WithContentRepository;

/**
 * Cloud module provides CategoryCloudComponent and ContentTypeCloudComponent
 *  
 * @author Emilio Escobar Reyero
 */
@Bundle
@Name("name")
public interface CloudModule extends Module, WithContentRepository, WithFreeMarker, WithURIGenerator {

	@Name("categoriesCloud")
	CategoryCloudComponent categoriesCloud();
	
	@Name("contentTypesCloud")
	ContentTypeCloudComponent contentTypesCloud();
	
	void config(CloudModuleConfig config);
}
