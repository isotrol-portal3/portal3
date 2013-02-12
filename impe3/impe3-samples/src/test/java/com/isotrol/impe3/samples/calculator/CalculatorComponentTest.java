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

package com.isotrol.impe3.samples.calculator;


import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;


/**
 * Tests for Calculator Component Module
 * @author Andres Rodriguez
 */
public class CalculatorComponentTest extends AbstractCalculatorTest {
	private ModuleTester<CalculatorComponentModule> module = null;
	private ComponentTester<CalculatorComponent> tester = null;

	private ComponentTester<CalculatorComponent> getTester() {
		if (tester == null) {
			module = environment.getModule(CalculatorComponentModule.class);
			module.start("service", getService());
			tester = module.getComponent(CalculatorComponent.class, "component");
		}
		return tester;
	}

	/**
	 * No arguments
	 */
	@Test
	public void empty() {
		getTester().executeAndRenderHTML();
	}

	/**
	 * With arguments
	 */
	@Test
	public void args() {
		final CalculatorComponent c = getTester().getComponent();
		c.setA(1);
		c.setB(2);
		c.setOperation(CalculatorComponent.ADD);
		getTester().executeAndRenderHTML();
		Assert.assertEquals(Integer.valueOf(3), c.getResult());
	}

}
