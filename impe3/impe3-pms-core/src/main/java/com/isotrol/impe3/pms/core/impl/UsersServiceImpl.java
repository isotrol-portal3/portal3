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
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newHashSet;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.isotrol.impe3.api.Identifiables;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PortalAuthority;
import com.isotrol.impe3.pms.api.user.AuthorizationDTO;
import com.isotrol.impe3.pms.api.user.DuplicateUserException;
import com.isotrol.impe3.pms.api.user.UserDTO;
import com.isotrol.impe3.pms.api.user.UserSelDTO;
import com.isotrol.impe3.pms.api.user.UsersService;
import com.isotrol.impe3.pms.core.dao.UserDAO;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.core.support.PasswordFunction;
import com.isotrol.impe3.pms.model.Lengths;
import com.isotrol.impe3.pms.model.PortalAuthorityValue;
import com.isotrol.impe3.pms.model.PortalEntity;
import com.isotrol.impe3.pms.model.UserEntity;


/**
 * Implementation of UsersService.
 * @author Andres Rodriguez.
 */
@Service("usersService")
public final class UsersServiceImpl extends AbstractContextService implements UsersService {
	private static <T extends UserSelDTO> T fill(T dto, UserEntity entity) {
		dto.setId(Identifiables.toStringId(entity.getId()));
		dto.setName(entity.getName());
		dto.setDisplayName(entity.getDisplayName());
		dto.setRoot(entity.isRoot());
		dto.setActive(entity.isActive());
		dto.setLocked(entity.isLocked());
		return dto;
	}

	private static final Function<UserEntity, UserSelDTO> map2sel = new Function<UserEntity, UserSelDTO>() {
		public UserSelDTO apply(UserEntity from) {
			return fill(new UserSelDTO(), from);
		};
	};

	/** User entity DAO. */
	private UserDAO userDAO;

	/** Default constructor. */
	public UsersServiceImpl() {
	}

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.USER;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#getUsers()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_GET)
	public List<UserSelDTO> getUsers() throws PMSException {
		final List<UserEntity> entities = userDAO.getNotDeleted();
		return Mappers.list(entities, map2sel);
	}

	/**
	 * Loads a non-deleted user.
	 * @param id User id.
	 * @return The loaded user entity.
	 * @throws PMSException If the user is not found.
	 */
	private UserEntity load(String id) throws PMSException {
		final UserEntity entity = load(UserEntity.class, id);
		NotFoundProviders.USER.checkCondition(!entity.isDeleted(), id);
		return entity;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_GET)
	public UserDTO get(String id) throws PMSException {
		final UserEntity entity = load(id);
		final UserDTO dto = fill(new UserDTO(), entity);
		dto.setRoles(newHashSet(entity.getGlobalRoles()));
		dto.setAuthorities(newHashSet(entity.getGlobalAuthorities()));
		return dto;
	}

	private <T> void copy(Set<T> entity, Set<T> dto) {
		entity.clear();
		if (dto != null) {
			entity.addAll(dto);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#save(com.isotrol.impe3.pms.api.user.UserDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_SET)
	public UserDTO save(UserDTO dto) throws PMSException {
		checkNotNull(dto);
		final String name = dto.getName();
		checkNotNull(name);
		String id = dto.getId();
		final UserEntity entity = (id != null) ? load(id) : new UserEntity();
		// Check for duplicate users.
		final UserEntity other = userDAO.getByName(name);
		if (other != null && !other.getId().equals(entity.getId())) {
			throw new DuplicateUserException(name);
		}
		// Fill user
		entity.setName(name);
		entity.setDisplayName(dto.getDisplayName());
		entity.setRoot(dto.isRoot());
		entity.setActive(dto.isActive());
		entity.setLocked(dto.isLocked());
		copy(entity.getGlobalRoles(), dto.getRoles());
		copy(entity.getGlobalAuthorities(), dto.getAuthorities());
		if (id == null) {
			saveNewEntity(entity);
			id = Identifiables.toStringId(entity.getId());
		} else {
			entity.setUpdated(loadUser());
		}
		sync();
		return get(id);
	}

	/**
	 * Gets a correct portal id.
	 * @param portalId Portal Id.
	 * @return The portal UUID.
	 * @throws PMSException If the portal is not found.
	 */
	private UUID getPortal(String portalId) throws PMSException {
		return loadContextGlobal().toPortal(portalId).getPortalId();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#getPortalAuthorities(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_GET)
	public Set<PortalAuthority> getPortalAuthorities(String id, String portalId) throws PMSException {
		final UserEntity entity = load(id);
		final UUID uuid = getPortal(portalId);
		return newHashSet(transform(filter(entity.getPortalAuthorities(), new PortalPred(uuid)), AuthValue.INSTANCE));
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#setPortalAuthorities(java.lang.String, java.lang.String,
	 * java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_SET)
	public void setPortalAuthorities(String id, String portalId, Set<PortalAuthority> granted) throws PMSException {
		checkNotNull(granted);
		final UserEntity user = load(id);
		final UUID uuid = getPortal(portalId);
		final PortalEntity portal = loadPortal(uuid);
		final Set<PortalAuthorityValue> set = newHashSet(filter(user.getPortalAuthorities(), not(new PortalPred(uuid))));
		for (PortalAuthority a : granted) {
			final PortalAuthorityValue v = new PortalAuthorityValue();
			v.setPortal(portal);
			v.setPortalAuthority(a);
			set.add(v);
		}
		copy(user.getPortalAuthorities(), set);
		sync();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_SET)
	public void delete(String id) throws PMSException {
		final UserEntity entity = load(id);
		entity.setDeleted(true);
		String name = Identifiables.toStringId(entity.getId()) + ':' + entity.getName();
		if (name.length() > Lengths.NAME) {
			name = name.substring(0, Lengths.NAME);
		}
		entity.setName(name);
		sync();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#setPassword(java.lang.String, java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_PWD)
	public void setPassword(String id, String password) throws PMSException {
		checkNotNull(password);
		checkArgument(StringUtils.hasText(password));
		final UserEntity entity = load(id);
		final String pwd = PasswordFunction.INSTANCE.apply(password);
		if (!Objects.equal(pwd, entity.getPassword())) {
			entity.setPassword(pwd);
		}
	}

	/**
	 * @see com.isotrol.impe3.pms.api.user.UsersService#getGranted(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.USER_GET)
	public AuthorizationDTO getGranted(String id) throws PMSException {
		final UserEntity entity = load(id);
		return new Authorization(loadContextGlobal().getPortals(), entity).toDTO();
	}

	private final class PortalPred implements Predicate<PortalAuthorityValue> {
		private final UUID portalId;

		PortalPred(final UUID portalId) {
			this.portalId = portalId;
		}

		public boolean apply(PortalAuthorityValue input) {
			if (input == null) {
				return false;
			}
			final PortalEntity e = input.getPortal();
			return e != null && portalId.equals(e.getId());
		}
	}

	private enum AuthValue implements Function<PortalAuthorityValue, PortalAuthority> {
		INSTANCE;

		public PortalAuthority apply(PortalAuthorityValue from) {
			return from.getPortalAuthority();
		}
	}
}
