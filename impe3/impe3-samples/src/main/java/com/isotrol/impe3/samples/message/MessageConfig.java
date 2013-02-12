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

package com.isotrol.impe3.samples.message;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Configuration for simple connector service.
 * @author Andres Rodriguez
 */
public interface MessageConfig extends Configuration {
	/**
	 * Message text.
	 */
	@Name("Text Message")
	@Description("The provided text message will be part of the service output")
	String text();
	
	@Optional
	Integer number();
	
	@Optional
	ContentType contentType1();

	@Optional
	ContentType contentType2();

	@Optional
	ContentType contentType3();
	
	@Optional
	Category category1();

	@Optional
	Category category2();

	@Optional
	Category category3();

	@Optional
	Boolean boolean1();
	
	@Name("Value")
	@Optional
	ExampleEnum enum1();
	
}
