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

package com.isotrol.impe3.palette.content.loader;


import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Query by field component configuration (multiple values).
 * @author Andres Rodriguez
 */
public interface ManyByFieldConfig extends FieldConfig {
	/** Field value. */
	@Name("Valor 2")
	@Optional
	String fieldValue2();

	/** Field value. */
	@Name("Valor 3")
	@Optional
	String fieldValue3();

	/** Field value. */
	@Name("Valor 4")
	@Optional
	String fieldValue4();

	/** Field value. */
	@Name("Valor 5")
	@Optional
	String fieldValue5();

	/** Field value. */
	@Name("Valor 6")
	@Optional
	String fieldValue6();

	/** Field value. */
	@Name("Valor 7")
	@Optional
	String fieldValue7();

	/** Field value. */
	@Name("Valor 8")
	@Optional
	String fieldValue8();

	/** Field value. */
	@Name("Valor 9")
	@Optional
	String fieldValue9();

	/** Field value. */
	@Name("Valor 10")
	@Optional
	String fieldValue10();

}
