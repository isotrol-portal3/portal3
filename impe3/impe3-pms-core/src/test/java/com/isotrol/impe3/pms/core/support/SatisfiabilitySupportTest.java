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

package com.isotrol.impe3.pms.core.support;


import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pms.core.support.SatisfiabilitySupport;
import com.isotrol.impe3.samples.calculator.CalculatorComponentModule;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;


/**
 * Tests for SatisfiabilitySupport.
 * @author Andres Rodriguez
 */
public class SatisfiabilitySupportTest {
	private static ModuleDefinition<CalculatorConnectorModule> CALC_CONNECTOR = ModuleDefinition
		.of(CalculatorConnectorModule.class);
	private static ModuleDefinition<CalculatorComponentModule> CALC_COMPONENT = ModuleDefinition
		.of(CalculatorComponentModule.class);

	private static SatisfiabilitySupport with(ModuleDefinition<?>... modules) {
		return new SatisfiabilitySupport(asList(modules));
	}

	/** One instantiable module. */
	@Test
	public void simpleYes() {
		final SatisfiabilitySupport ss = with(CALC_CONNECTOR);
		assertTrue(ss.isInstantiable(CALC_CONNECTOR));
	}

	/** One non-instantiable module. */
	@Test
	public void simpleNo() {
		final SatisfiabilitySupport ss = with(CALC_COMPONENT);
		assertFalse(ss.isInstantiable(CALC_COMPONENT));
	}

	/** One simple dependency. */
	@Test
	public void oneDependency() {
		final SatisfiabilitySupport ss = with(CALC_CONNECTOR, CALC_COMPONENT);
		assertTrue(ss.isInstantiable(CALC_CONNECTOR));
		assertTrue(ss.isInstantiable(CALC_COMPONENT));
	}

}
