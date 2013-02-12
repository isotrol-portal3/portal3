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

package com.isotrol.impe3.palette.atom.feed.img;


import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Downloadable;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Name;


/**
 * ATOM Img Component Configuration.
 * @author Emilio Escobar Reyero
 */
@Bundle
public interface ImgConfig extends Configuration {
	/** Image file. */
	@Downloadable
	@Name("file")
	@Optional
	FileId file();

	/** Image name. */
	@Name("name")
	@Optional
	String name();

	@Name("title")
	@Optional
	String title();
	
	/** Category. */
	@Name("category")
	@Optional
	Category category();

	/** Content type. */
	@Name("contentType")
	@Optional
	ContentType contentType();
	
	/** URI. */
	@Name("link")
	@Optional
	String link();

	/** Alt text. */
	@Name("description")
	@Optional
	String description();
}
