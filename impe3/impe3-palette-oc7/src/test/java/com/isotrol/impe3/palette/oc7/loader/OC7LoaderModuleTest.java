package com.isotrol.impe3.palette.oc7.loader;


import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Tests for OC7LoaderModule.
 * @author Andres Rodriguez Chamorro
 */
public class OC7LoaderModuleTest {
	private static TestEnvironment environment;
	private static ModuleTester<OC7LoaderModule> tester;

	@BeforeClass
	public static void setUp() {
		environment = new TestEnvironmentBuilder().get();
		tester = environment.getModule(OC7LoaderModule.class);
	}

	/** Start. */
	@Test
	public void start() {
		tester.fakeStart();
		assertNotNull(tester.getComponent("oneByPath"));
		assertNotNull(tester.getComponent("manyByPath"));
	}

}
