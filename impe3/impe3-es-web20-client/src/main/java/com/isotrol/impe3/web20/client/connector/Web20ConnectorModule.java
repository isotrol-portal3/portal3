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
package com.isotrol.impe3.web20.client.connector;

import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityNoticesService;
import com.isotrol.impe3.web20.api.CountersService;
import com.isotrol.impe3.web20.api.FavoritesService;
import com.isotrol.impe3.web20.api.MembersService;
import com.isotrol.impe3.web20.api.MigrationService;
import com.isotrol.impe3.web20.api.RatingsService;
import com.isotrol.impe3.web20.api.RecommendationsService;
import com.isotrol.impe3.web20.api.ResourcesService;
import com.isotrol.impe3.web20.api.TagsService;

/**
 * Web 2.0 Connector Module
 * @author Emilio Escobar Reyero
 */
@Path("module-web20-connector.xml")
@Bundle
@Name("module.name")
@Description("module.desc")
public interface Web20ConnectorModule extends Module {

	/** Members service. */
	MembersService members();
	
	/** Communities service. */
	CommunitiesService communities();

	/** Counters service. */
	CountersService counters();
	
	/** Resources service. */
	ResourcesService resources();
	
	/** Ratings service. */
	RatingsService ratings();

	/** Tags service. */
	TagsService tags();
	
	/** Comments service.*/
	CommentsService comments();

	/** Notices service. */
	CommunityNoticesService notices();
	
	/** Favorites service. */
	FavoritesService favorites();
	
	/** Recommendations service. */
	RecommendationsService recommendations();
	
	/** Migration service. */
	MigrationService migration();
	
	/** Module configuration. */
	void config(Web20ConnectorConfig config);
	
}
