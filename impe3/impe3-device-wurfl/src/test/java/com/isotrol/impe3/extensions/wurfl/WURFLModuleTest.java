package com.isotrol.impe3.extensions.wurfl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.HttpHeaders;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;

public class WURFLModuleTest {
	private static TestEnvironment environment;
	private static ModuleTester<WURFLModule> tester;
	private static WURFLModule module;
	private static DeviceCapabilitiesProvider provider;

	@BeforeClass
	public static void setUp() {
		TestEnvironmentBuilder teb = new TestEnvironmentBuilder();
		environment = teb.get();
		tester = environment.getModule(WURFLModule.class);
		module = tester.start();
		provider = module.provider();
		assertNotNull(provider);
	}

	private HttpHeaders mockHeaders(String userAgent) {
		// User-Agent
		final HttpHeaders headers = Mockito.mock(HttpHeaders.class);
		Mockito.when(headers.getRequestHeader("User-Agent")).thenReturn(Lists.newArrayList(userAgent));

		Mockito.when(headers.getRequestHeader("UA")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("X-Skyfire-Version")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("x-device-user-agent")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("x-wap-profile")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("Profile")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("wap-profile")).thenReturn(null);
		Mockito.when(headers.getRequestHeader("Opt")).thenReturn(null);
		
		return headers;
	}
	
	@Test
	public void testIPAD() {
		final String userAgent = "Mozilla/5.0 (iPad; U; CPU iPhone OS 3_2 like Mac OS X; xx-xx) AppleWebKit/531.21.10 (KHTML, like Gecko) Mobile/7D11";
		final DeviceCapabilitiesProvider provider = module.provider();
		assertNotNull(provider);
		
		final DeviceCapabilities capabilities = provider.getDeviceCapabilities(mockHeaders(userAgent));
		assertNotNull(capabilities);
		
		assertTrue(capabilities.getWidth() == 768);
		assertFalse(capabilities.isFlashSupported());
		assertTrue(capabilities.isHTML4Supported());
		assertTrue(capabilities.isXHTMLSupported());
	}

	@Test
	public void testChrome() {
		final String userAgent = "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.83 Safari/534.13";
		final DeviceCapabilitiesProvider provider = module.provider();
		assertNotNull(provider);
		
		final DeviceCapabilities capabilities = provider.getDeviceCapabilities(mockHeaders(userAgent));
		assertNull(capabilities);
	}
}
