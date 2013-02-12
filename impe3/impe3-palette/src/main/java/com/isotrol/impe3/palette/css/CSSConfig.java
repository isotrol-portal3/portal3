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

package com.isotrol.impe3.palette.css;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Downloadable;
import com.isotrol.impe3.api.FileBundle;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Name;


/**
 * CSS Component Module Configuration.
 * @author Andres Rodriguez
 */
@Bundle
public interface CSSConfig extends Configuration {
	/** External URI. */
	@Name("uri")
	@Optional
	String uri();

	/** CSS Bundle. */
	@Name("bundle")
	@FileBundle
	@Downloadable
	@Optional
	FileId bundle();

	/** CSS File in bundle. */
	@Name("path")
	@Optional
	String path();

	/** Additional IE6 CSS File in bundle. */
	@Name("pathIE6")
	@Optional
	String pathIE6();

	/** Additional IE6 CSS File in bundle. */
	@Name("pathIE7")
	@Optional
	String pathIE7();

	/** Additional IE6 CSS File in bundle. */
	@Name("pathIE8")
	@Optional
	String pathIE8();

	/** Media attribute. */
	@Name("media")
	@Optional
	String media();

}
