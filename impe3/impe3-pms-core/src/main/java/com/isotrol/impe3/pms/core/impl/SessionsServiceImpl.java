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

package com.isotrol.impe3.pms.core.impl;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.isotrol.impe3.pms.core.UserManager.ROOT_ID;
import static com.isotrol.impe3.pms.core.UserManager.ROOT_LOGIN;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.i18n.BundleLocalized;
import net.sf.derquinsej.i18n.Locales;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Objects;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Identifiables;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.api.AuthorizationException;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.api.InvalidOperationException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.session.CurrentUserDTO;
import com.isotrol.impe3.pms.api.session.EnvironmentConfigDTO;
import com.isotrol.impe3.pms.api.session.InactiveUserException;
import com.isotrol.impe3.pms.api.session.LockedUserException;
import com.isotrol.impe3.pms.api.session.SessionDTO;
import com.isotrol.impe3.pms.api.session.SessionsService;
import com.isotrol.impe3.pms.api.session.UserNameDTO;
import com.isotrol.impe3.pms.core.UserManager;
import com.isotrol.impe3.pms.core.dao.UserDAO;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.core.support.PasswordFunction;
import com.isotrol.impe3.pms.ext.api.AuthenticationProvider;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.pms.ext.api.Credentials.BasicCredentials;
import com.isotrol.impe3.pms.ext.api.ExternalUserDataDTO;
import com.isotrol.impe3.pms.ext.api.InvalidCredentialsException;
import com.isotrol.impe3.pms.model.EnvironmentEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of SessionsService.
 * @author Andres Rodriguez.
 */
@Service("sessionsService")
public final class SessionsServiceImpl extends AbstractContextService implements SessionsService {
	/** User entity DAO. */
	private UserDAO userDAO;
	/** User manager. */
	private UserManager userManager;
	/** Authentication provider. */
	private AuthenticationProvider authenticationProvider;
	/** Logger. */
	private final Logger logger = Loggers.pms();

	/** Default constructor. */
	public SessionsServiceImpl() {
	}

	@Autowired(required = false)
	public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.USER;
	}

	private UserEntity loadUser(Credentials credentials) throws PMSException {
		if (credentials instanceof BasicCredentials) {
			final BasicCredentials basic = (BasicCredentials) credentials;
			final String username = basic.getLogin();
			if (username != null) {
				UserEntity user = userDAO.getByName(username);
				if (user != null) {
					if (!user.isActive()) {
						throw new InactiveUserException();
					}
					if (user.isLocked()) {
						throw new LockedUserException();
					}
				}
				return user;
			}
		}
		return null;
	}

	private void registerAttempt(EnvironmentEntity env, UserEntity user, boolean success) {
		if (success) {
			user.resetAttempts();
		} else {
			int n = user.registerAttempts();
			Integer max = env.getMaxLoginAttempts();
			if (max != null && max > 0 && max <= n) {
				user.setLocked(true);
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#login(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	public SessionDTO login(Credentials credentials) throws PMSException {
		if (credentials == null) {
			return null;
		}
		final EnvironmentEntity env = getEnvironment();
		final UserEntity user = loadUser(credentials);
		if (authenticationProvider != null) {
			return providerLogin(env, user, credentials);
		} else if (user != null) {
			return defaultLogin(env, user, ((BasicCredentials) credentials).getPassword());
		}
		return null;
	}

	private SessionDTO providerLogin(EnvironmentEntity env, UserEntity user, Credentials credentials) {
		final ExternalUserDataDTO data;
		final Stopwatch w = new Stopwatch().start();
		try {
			data = authenticationProvider.authenticate(credentials);
		} catch (InvalidCredentialsException e) {
			if (user != null) {
				registerAttempt(env, user, false);
			}
			return null;
		} finally {
			long t = w.elapsedMillis();
			if (t > 250) {
				logger.warn(String.format("Provider login took [%d] ms", t));
			}
		}
		if (data == null) {
			return null;
		}
		if (user == null) {
			if (!data.isCreate()) {
				return null;
			}
			user = new UserEntity();
			user.setName(data.getName());
			user.setDisplayName(data.getDisplayName());
			user.getGlobalAuthorities();
			user.getGlobalRoles();
			user.getPortalAuthorities();
			user.setCreated(userManager.getRootUser());
			user.setUpdated(userManager.getRootUser());
			user.setId(newUUID());
			userDAO.save(user);
			sync();
		} else {
			if (data.isUpdate()) {
				user.setDisplayName(data.getDisplayName());
				user.setUpdated(userManager.getRootUser());
			}
		}
		final UUID id = user.getId();
		final SessionDTO dto = new SessionDTO();
		dto.setId(Identifiables.toStringId(id));
		dto.setName(user.getDisplayName());
		SecurityContext.set(new SecurityContext(id));
		dto.setAuthorization(new Authorization(loadContextGlobal().getPortals(), user).toDTO());
		registerAttempt(env, user, true);
		return dto;
	}

	private SessionDTO defaultLogin(EnvironmentEntity env, UserEntity user, String password) {
		if (!StringUtils.hasText(password)) {
			return null;
		}
		final UUID id = user.getId();
		if (!Objects.equal(PasswordFunction.INSTANCE.apply(password), user.getPassword())
			&& !isRootSpecialCase(user, password)) {
			registerAttempt(env, user, false);
			return null;
		}
		final SessionDTO dto = new SessionDTO();
		dto.setId(Identifiables.toStringId(id));
		dto.setName(user.getDisplayName());
		SecurityContext.set(new SecurityContext(id));
		dto.setAuthorization(new Authorization(loadContextGlobal().getPortals(), user).toDTO());
		registerAttempt(env, user, true);
		return dto;

	}

	/**
	 * Special case for root password. If the user is root and the stored password is null, the password is root.
	 * @param user User entity.
	 * @param password Sent password.
	 * @return Whether this is a special case root password.
	 */
	private static boolean isRootSpecialCase(UserEntity user, String password) {
		return ROOT_ID.equals(user.getId()) && user.getPassword() == null && ROOT_LOGIN.equals(password);
	}

	private SessionDTO getSessionDTO(UserEntity user) {
		final SessionDTO dto = new SessionDTO();
		dto.setId(Identifiables.toStringId(user.getId()));
		dto.setName(user.getDisplayName());
		dto.setAuthorization(new Authorization(loadContextGlobal().getPortals(), user).toDTO());
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#logout()
	 */
	public void logout() {
		SecurityContext.set(null);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getSession()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public SessionDTO getSession() {
		try {
			return getSessionDTO(loadUser());
		} catch (PMSException e) {
			return null;
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getGlobalAuthorities()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public Map<GlobalAuthority, String> getGlobalAuthorities() throws PMSException {
		final Map<GlobalAuthority, String> map = Maps.newHashMap();
		final Locale locale = getLocale();
		for (GlobalAuthority ga : GlobalAuthority.values()) {
			final String key = ga.toString();
			map.put(ga, new BundleLocalized<String>(GlobalAuthority.class, key).get(locale, key));
		}
		return map;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getPortalAuthorities()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public Map<PortalAuthority, String> getPortalAuthorities() throws PMSException {
		final Map<PortalAuthority, String> map = Maps.newHashMap();
		final Locale locale = getLocale();
		for (PortalAuthority pa : PortalAuthority.values()) {
			final String key = pa.toString();
			map.put(pa, new BundleLocalized<String>(PortalAuthority.class, key).get(locale, key));
		}
		return map;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getGlobalRoles()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public Map<GlobalRole, String> getGlobalRoles() throws PMSException {
		final Map<GlobalRole, String> map = Maps.newHashMap();
		final Locale locale = getLocale();
		for (GlobalRole gr : GlobalRole.values()) {
			final String key = gr.toString();
			map.put(gr, new BundleLocalized<String>(GlobalRole.class, key).get(locale, key));
		}
		return map;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#checkLocale(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public boolean checkLocale(String locale) throws PMSException {
		try {
			Locales.fromString(locale);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#checkLocales(java.lang.Iterable)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public Set<String> checkLocales(Iterable<String> locales) throws PMSException {
		checkNotNull(locales);
		final Set<String> set = Sets.newHashSet();
		for (String locale : locales) {
			try {
				Locales.fromString(locale);
			} catch (IllegalArgumentException e) {
				set.add(locale);
			}
		}
		return set;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#checkURI(java.lang.String, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public boolean checkURI(String uri, boolean mustBeAbsolute) throws PMSException {
		try {
			final URI u = new URI(uri);
			return !mustBeAbsolute || u.isAbsolute();
		} catch (URISyntaxException e) {
			return false;
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#checkURIs(java.lang.Iterable, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public Set<String> checkURIs(Iterable<String> uris, boolean mustBeAbsolute) throws PMSException {
		checkNotNull(uris);
		final Set<String> set = Sets.newHashSet();
		for (String uri : uris) {
			if (!checkURI(uri, mustBeAbsolute)) {
				set.add(uri);
			}
		}
		return set;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getEnvironmentConfig()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.ENV_CONFIG_GET)
	public EnvironmentConfigDTO getEnvironmentConfig() throws PMSException {
		final EnvironmentEntity e = getEnvironment();
		final EnvironmentConfigDTO dto = new EnvironmentConfigDTO();
		dto.setInternalSegment(e.getInternalSegment());
		dto.setMaxLoginAttempts(e.getMaxLoginAttempts());
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#setEnvironmentConfig(com.isotrol.impe3.pms.api.session.EnvironmentConfigDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.ENV_CONFIG_SET)
	public void setEnvironmentConfig(EnvironmentConfigDTO dto) {
		checkNotNull(dto, "Environment config DTO");
		checkNotNull(dto.getInternalSegment(), "Internal segment");
		final EnvironmentEntity e = getEnvironment();
		e.setInternalSegment(dto.getInternalSegment());
		e.setMaxLoginAttempts(dto.getMaxLoginAttempts());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#getCurrentUser()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public CurrentUserDTO getCurrentUser() throws PMSException {
		final UserEntity entity = loadUser();
		final UserNameDTO name = new UserNameDTO();
		name.setName(entity.getName());
		name.setDisplayName(entity.getDisplayName());
		final CurrentUserDTO dto = new CurrentUserDTO();
		dto.setName(name);
		dto.setRoot(entity.isRoot());
		dto.setChangeDisplayName(true);
		dto.setChangeLogin(false); // TODO
		dto.setChangePassword(true); // TODO
		return dto;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#setUserName(com.isotrol.impe3.pms.api.session.UserNameDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public void setUserName(UserNameDTO name) throws PMSException {
		checkNotNull(name);
		checkNotNull(name.getName());
		final UserEntity entity = loadUser();
		boolean updated = false;
		// TODO
		if (!Objects.equal(entity.getName(), name.getName())) {
			throw new InvalidOperationException();
		}
		// TODO
		if (!Objects.equal(entity.getDisplayName(), name.getDisplayName())) {
			entity.setDisplayName(name.getDisplayName());
			updated = true;
		}
		if (updated) {
			entity.setUpdated(entity);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.session.SessionsService#changePassword(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized
	public void changePassword(String currentPwd, String newPwd) throws PMSException {
		checkNotNull(newPwd);
		checkArgument(StringUtils.hasText(newPwd));
		final UserEntity entity = loadUser();
		boolean currentOk = false;
		if (isRootSpecialCase(entity, currentPwd)) {
			currentOk = true;
		} else if (currentPwd == null || !StringUtils.hasText(currentPwd)) {
			currentOk = entity.getPassword() == null;
		} else {
			currentOk = Objects.equal(PasswordFunction.INSTANCE.apply(currentPwd), entity.getPassword());
		}
		if (!currentOk) {
			throw new AuthorizationException();
		}
		final String pwd = PasswordFunction.INSTANCE.apply(newPwd);
		if (!Objects.equal(pwd, entity.getPassword())) {
			entity.setPassword(pwd);
			entity.setUpdated(entity);
		}
	}
}
