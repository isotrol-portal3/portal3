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

package com.isotrol.impe3.palette.breadcrumb;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Manual breadcrumb component configuration.
 * @author Andres Rodriguez
 */
@Bundle
public interface ManualBreadcrumbConfig extends Configuration {
	/** First text. */
	@Name("text1")
	@Optional
	String text1();

	/** First URI. */
	@Name("uri1")
	@Optional
	String uri1();

	/** Second text. */
	@Name("text2")
	@Optional
	String text2();

	/** Second URI. */
	@Name("uri2")
	@Optional
	String uri2();

	/** Third text. */
	@Name("text3")
	@Optional
	String text3();

	/** Third URI. */
	@Name("uri3")
	@Optional
	String uri3();

	/** Fourth text. */
	@Name("text4")
	@Optional
	String text4();

	/** Fourth URI. */
	@Name("uri4")
	@Optional
	String uri4();

	/** Fifth text. */
	@Name("text5")
	@Optional
	String text5();

	/** Fifth URI. */
	@Name("uri5")
	@Optional
	String uri5();
}
