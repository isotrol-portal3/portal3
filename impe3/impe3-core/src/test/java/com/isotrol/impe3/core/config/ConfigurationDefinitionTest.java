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

package com.isotrol.impe3.core.config;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.DefaultValue;

import org.junit.Test;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Tests for ConfigurationDefinition
 * @author Andres Rodriguez
 */
public class ConfigurationDefinitionTest {
	private static final String P1 = "param1";
	private static final String P2 = "param2";
	private static final String STRING = "String";
	private static final Integer INTEGER = 27;
	private static final String STRING2 = "String2";
	private static final Integer INTEGER2 = 33;

	private enum Enum1 {
		A, B;
	}

	private enum Enum2 {
		@Name(STRING)
		A, B;
	}

	/**
	 * Concrete class.
	 */
	@Test(expected = NonInterfaceConfigurationException.class)
	public void testClass() throws ConfigurationException {
		ConfigurationDefinition.of(TestClass.class);
	}

	private static class TestClass implements Configuration {
	}

	/**
	 * Empty interface.
	 */
	@Test(expected = EmptyConfigurationException.class)
	public void testEmpty() throws ConfigurationException {
		ConfigurationDefinition.of(TestEmpty.class);
	}

	private static interface TestEmpty extends Configuration {
	}

	/**
	 * Has arguments.
	 */
	@Test(expected = MethodWithArgumentsConfigurationException.class)
	public void testArgs() throws ConfigurationException {
		ConfigurationDefinition.of(TestArgs.class);
	}

	private static interface TestArgs extends Configuration {
		String param(int a);
	}

	/**
	 * Illegal return type.
	 */
	@Test(expected = IllegalTypeConfigurationException.class)
	public void testType() throws ConfigurationException {
		ConfigurationDefinition.of(TestType.class);
	}

	private static interface TestType extends Configuration {
		Runnable param();
	}

	/**
	 * Configuration interface.
	 */
	private static interface Config extends Configuration {
		String param1();

		Integer param2();
	}

	/**
	 * Definition OK.
	 */
	@Test
	public void ok() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class);
	}

	/**
	 * Builder: wrong parameter
	 */
	@Test(expected = IllegalArgumentException.class)
	public void builderBad01() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class).builder().set("param3", 0);
	}

	/**
	 * Builder: wrong type
	 */
	@Test(expected = IllegalArgumentException.class)
	public void builderBad02() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class).builder().set(P1, 0);
	}

	/**
	 * Builder: OK
	 */
	@Test
	public void builderOK() throws ConfigurationException {
		Config c = ConfigurationDefinition.of(Config.class).builder().set(P1, STRING).set(P2, INTEGER).get();
		assertNotNull(c);
		assertEquals(c.param1(), STRING);
		assertEquals(c.param2(), INTEGER);
	}

	/**
	 * Builder: Missing required
	 */
	@Test(expected = IllegalStateException.class)
	public void builderMissing1() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class).builder().get();
	}

	/**
	 * Builder: Missing required
	 */
	@Test(expected = IllegalStateException.class)
	public void builderMissing2() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class).builder().set(P2, INTEGER).get();
	}

	/**
	 * Builder: Missing required
	 */
	@Test(expected = IllegalStateException.class)
	public void builderMissing3() throws ConfigurationException {
		ConfigurationDefinition.of(Config.class).builder().set(P1, STRING).get();
	}

	/** Configuration interface with primitives. */
	public interface ConfigPrim extends Configuration {
		int param1();

		boolean param2();
	}

	/**
	 * Primitives
	 */
	@Test
	public void primitives() throws ConfigurationException {
		ConfigPrim c = ConfigurationDefinition.of(ConfigPrim.class).builder().set(P1, INTEGER).set(P2, Boolean.TRUE)
			.get();
		assertNotNull(c);
		assertEquals(c.param1(), INTEGER.intValue());
		assertEquals(c.param2(), Boolean.TRUE.booleanValue());
	}

	/** Configuration interface with primitives. */
	public interface ConfigPrim2 extends Configuration {
		@Optional
		int param1();

		@Optional
		boolean param2();
	}

	/**
	 * Primitives. Missing 1
	 */
	@Test(expected = IllegalStateException.class)
	public void primitivesMissing1() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigPrim2.class).builder().get();
	}

	/**
	 * Primitives. Missing 2
	 */
	@Test(expected = IllegalStateException.class)
	public void primitivesMissing2() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigPrim2.class).builder().set(P2, Boolean.TRUE).get();
	}

	/**
	 * Primitives. Missing 3
	 */
	@Test(expected = IllegalStateException.class)
	public void primitivesMissing3() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigPrim2.class).builder().set(P1, INTEGER).get();
	}

	/**
	 * Configuration interface with default values.
	 */
	private static interface ConfigDef extends Configuration {
		@DefaultValue(STRING2)
		String param1();

		int param2();
	}

	/**
	 * Default values 1
	 */
	@Test
	public void defaults1() throws ConfigurationException {
		ConfigDef c = ConfigurationDefinition.of(ConfigDef.class).builder().set(P2, INTEGER2).get();
		assertNotNull(c);
		assertEquals(c.param1(), STRING2);
		assertEquals(c.param2(), INTEGER2.intValue());
	}

	/**
	 * Configuration interface with default values.
	 */
	private static interface ConfigDef2 extends Configuration {
		String param1();

		@DefaultValue(STRING2)
		int param2();
	}

	/**
	 * Default values 2
	 */
	@Test(expected = IllegalValueConfigurationException.class)
	public void defaults2() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigDef2.class);
	}

	/**
	 * Invalid configuration interface with default values.
	 */
	private static interface ConfigDef3 extends Configuration {
		@DefaultValue(STRING2)
		ContentType param1();
	}

	/**
	 * Default values 3
	 */
	@Test(expected = IllegalValueConfigurationException.class)
	public void defaults3() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigDef3.class);
	}

	/**
	 * Configuration interface with enum.
	 */
	private static interface ConfigEnum1 extends Configuration {
		Enum1 param1();

		@DefaultValue("B")
		Enum2 param2();
	}

	/**
	 * Enumeration test 1
	 */
	public void enum1() throws ConfigurationException {
		ConfigurationDefinition<ConfigEnum1> cd = ConfigurationDefinition.of(ConfigEnum1.class);
		assertEquals(P1, cd.getParameters().get(P2).getChoices().get(Enum2.A).getName().get());
		ConfigEnum1 c = cd.builder().set(P1, Enum1.A).get();
		assertEquals(Enum1.A, c.param1());
		assertEquals(Enum2.B, c.param2());
	}

	/**
	 * Configuration interface with incorrect default value.
	 */
	private static interface ConfigEnum2 extends Configuration {
		@DefaultValue("kk")
		Enum2 param2();
	}

	/**
	 * Wrong enum default values.
	 */
	@Test(expected = IllegalValueConfigurationException.class)
	public void enum2() throws ConfigurationException {
		ConfigurationDefinition.of(ConfigEnum2.class);
	}

}
