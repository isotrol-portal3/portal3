package com.isotrol.impe3.pms.ldap;


import org.junit.AfterClass;
import org.junit.BeforeClass;


/**
 * Abstract class for tests that share an application context with a private memory database.
 * @author Andres Rodriguez
 */
public abstract class MemoryContextTest {
	/** Aplication context. */
	private static AppContextSupport context; 
	/** String index. */
	private static int stringIndex = 0;

	protected static final String testString() {
		return "test" + (++stringIndex);
	}

	/** Default constructor. */
	public MemoryContextTest() {
	}

	/**
	 * Loads a fresh application context.
	 */
	@BeforeClass
	public static void loadContext() {
		context = AppContextSupport.memory();
	}

	@AfterClass
	public static void shutdown() {
		context.shutdown();
	}

	/**
	 * Returns the unique bean of the context with the specified type.
	 * @param type Requested type.
	 * @return The requested bean.
	 */
	protected static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}

}
