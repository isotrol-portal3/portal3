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
package com.isotrol.impe3.idx.oc;


import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.idx.oc.ResourceTypeDescriptor;


public class ResourceTypeDescriptorTest {

	@Test
	public void fieldTest() {
		ResourceTypeDescriptor.Field f1 = new ResourceTypeDescriptor.Field("name", true, true, "value", null);
		ResourceTypeDescriptor.Field f2 = new ResourceTypeDescriptor.Field("namename", true, true, "value", null);

		Assert.assertNotSame(f1, f2);
	}

}
