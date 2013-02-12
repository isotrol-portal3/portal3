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

package com.isotrol.impe3.nr.api;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;


public class HessianTest {
	public static void hessian(Object o) throws Exception {
		if (o == null) {
			return;
		}
		// Hessian 2
		ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
		Hessian2Output h2o = new Hessian2Output(bos2);
		h2o.writeObject(o);
		h2o.close();
		Hessian2Input h2i = new Hessian2Input(new ByteArrayInputStream(bos2.toByteArray()));
		final Object o2 = h2i.readObject();
		assertNotNull(o2);
		assertEquals(o, o2);
		// Hessian 1
		ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
		HessianOutput h1o = new HessianOutput(bos1);
		h1o.writeObject(o);
		h1o.close();
		HessianInput h1i = new HessianInput(new ByteArrayInputStream(bos1.toByteArray()));
		final Object o1 = h1i.readObject();
		assertNotNull(o1);
		assertEquals(o, o1);
	}

	@Test
	public void string() throws Exception {
		hessian("string");
	}

	@Test
	public void integer() throws Exception {
		hessian(3);
	}

}
