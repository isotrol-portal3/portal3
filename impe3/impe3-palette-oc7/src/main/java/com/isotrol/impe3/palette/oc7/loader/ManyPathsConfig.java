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
package com.isotrol.impe3.palette.oc7.loader;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Single content by path configuration.
 * @author Juan Manuel Valverde Ramirez
 */
public interface ManyPathsConfig extends Configuration {
	/** First OpenCMS content path. */
	@Name("Ruta del contenido 1 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	String path1();

	/** 2nd OpenCMS content path. */
	@Name("Ruta del contenido 2 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path2();

	/** 3rd OpenCMS content path. */
	@Name("Ruta del contenido 3 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path3();

	/** 4th OpenCMS content path. */
	@Name("Ruta del contenido 4 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path4();

	/** 5th OpenCMS content path. */
	@Name("Ruta del contenido 5 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path5();

	/** 6th OpenCMS content path. */
	@Name("Ruta del contenido 6 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path6();

	/** 7th OpenCMS content path. */
	@Name("Ruta del contenido 7 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path7();

	/** 8th OpenCMS content path. */
	@Name("Ruta del contenido 8 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path8();

	/** 9th OpenCMS content path. */
	@Name("Ruta del contenido 9 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path9();

	/** 10th OpenCMS content path. */
	@Name("Ruta del contenido 10 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path10();
}
