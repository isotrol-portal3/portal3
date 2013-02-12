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

package com.isotrol.impe3.test;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import net.sf.derquinsej.uuid.TimeBasedUUIDGenerator;
import net.sf.derquinsej.uuid.UUIDGenerator;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.core.config.ConfigurationBuilder;
import com.isotrol.impe3.core.config.ConfigurationDefinition;


/**
 * Support class for IMPE3 tests.
 * @author Andres Rodriguez
 */
public final class TestSupport {
	/** Not instantiable. */
	private TestSupport() {
		throw new AssertionError();
	}

	/** An UUID generator. */
	private static UUIDGenerator generator = new TimeBasedUUIDGenerator();

	/**
	 * Generates a new UUID.
	 * @return A new UUID.
	 */
	public static UUID uuid() {
		return generator.get();
	}
	
	public static Device device(DeviceType type, Integer width) {
		if (!type.isLayout()) {
			width = null;
		}
		final UUID id = uuid();
		return new Device(id, type, id.toString(), "", width, null, null);
	}

	public static Device htmlDevice() {
		return device(DeviceType.HTML, 980);
	}
	
	/**
	 * Returns a configuration builder.
	 * @param configClass Configuration class.
	 * @return A builder for the configuration.
	 */
	public static <T extends Configuration> ConfigurationBuilder<T> builder(Class<T> configClass) {
		return ConfigurationDefinition.of(configClass).builder();
	}

	/**
	 * Returns a configuration with one parameter.
	 * @param configClass Configuration class.
	 * @param name1 First parameter name.
	 * @param value1 First parameter value.
	 * @return The configuration.
	 */
	public static <T extends Configuration> T config(Class<T> configClass, String name1, Object value1) {
		return builder(configClass).set(name1, value1).get();
	}

	/**
	 * Returns a configuration with two parameters.
	 * @param configClass Configuration class.
	 * @param name1 First parameter name.
	 * @param value1 First parameter value.
	 * @param name2 Second parameter name.
	 * @param value2 Second parameter value.
	 * @return The configuration.
	 */
	public static <T extends Configuration> T config(Class<T> configClass, String name1, Object value1, String name2,
		Object value2) {
		return builder(configClass).set(name1, value1).set(name2, value2).get();
	}

	/**
	 * Returns a configuration with three parameters.
	 * @param configClass Configuration class.
	 * @param name1 First parameter name.
	 * @param value1 First parameter value.
	 * @param name2 Second parameter name.
	 * @param value2 Second parameter value.
	 * @param name3 Third parameter name.
	 * @param value3 Third parameter value.
	 * @return The configuration.
	 */
	public static <T extends Configuration> T config(Class<T> configClass, String name1, Object value1, String name2,
		Object value2, String name3, Object value3) {
		return builder(configClass).set(name1, value1).set(name2, value2).set(name3, value3).get();
	}

	private static final InvocationHandler UNSUPPORTED = new InvocationHandler() {
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			throw new UnsupportedOperationException();
		}
	};

	/**
	 * Creates a proxy for the provided class that throws for every method invocation.
	 * @param type Interface to be supported by the proxy.
	 * @return The requested proxy.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unsupportedProxy(Class<T> type) {
		return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type}, UNSUPPORTED);
	}
}
