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

package com.isotrol.impe3.palette.html.iframe;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Simple iframe component configuration.
 * @author Andres Rodriguez
 */
@Bundle
public interface IFrameConfig extends Configuration {
	/** URI. */
	@Name("uri")
	String uri();

	/** Frame width. */
	@Name("width")
	@Optional
	String width();

	/** Frame height. */
	@Name("height")
	@Optional
	String height();

	/** Frame border. */
	@Name("frameborder")
	@Optional
	Boolean frameborder();

	/** Scroll bar. */
	@Name("scrolling")
	@Optional
	Boolean scrolling();

	/** 1st query parameter. */
	@Name("qp1")
	@Optional
	String qp1();

	/** 1st query parameter. */
	@Name("qp2")
	@Optional
	String qp2();

	/** 1st query parameter. */
	@Name("qp3")
	@Optional
	String qp3();

	/** 1st query parameter. */
	@Name("qp4")
	@Optional
	String qp4();

	/** 1st query parameter. */
	@Name("qp5")
	@Optional
	String qp5();
}
