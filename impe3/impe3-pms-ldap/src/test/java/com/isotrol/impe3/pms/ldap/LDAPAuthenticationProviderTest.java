package com.isotrol.impe3.pms.ldap;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.ext.api.AuthenticationProvider;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.pms.ext.api.ExternalUserDataDTO;
import com.isotrol.impe3.pms.ext.api.InvalidCredentialsException;


public class LDAPAuthenticationProviderTest extends MemoryContextTest {

	private AuthenticationProvider ldap;

	@Before
	public void setUp() {
		ldap = getBean(AuthenticationProvider.class);
		assertNotNull(ldap);
	}

	/*
	 * @Test public void testLoginOk() { final ExternalUserDataDTO user = ldap.login(new
	 * Credentials.BasicCredentials().setLogin("eescobar").setPassword("xxxxx")); assertNotNull(user);
	 * 
	 * assertEquals("eescobar", user.getName()); }
	 */
	@Test(expected=InvalidCredentialsException.class)
	public void testLoginFail() throws Exception {
		final ExternalUserDataDTO user = ldap.authenticate(new Credentials.BasicCredentials().setLogin("eescobar")
			.setPassword("kaka"));

		assertNull(user);
	}

}
