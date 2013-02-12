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

package com.isotrol.impe3.users.impl;


import static com.google.common.base.Predicates.notNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.derquinsej.uuid.UUIDGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.users.api.DuplicatePortalUserException;
import com.isotrol.impe3.users.api.PortalUserDTO;
import com.isotrol.impe3.users.api.PortalUserException;
import com.isotrol.impe3.users.api.PortalUserNotFoundException;
import com.isotrol.impe3.users.api.PortalUserSelDTO;
import com.isotrol.impe3.users.api.PortalUsersService;
import com.isotrol.impe3.users.dao.PortalUserDAO;
import com.isotrol.impe3.users.model.PortalUserEntity;


/**
 * Implementation of PortalUsersService.
 * @author Andres Rodriguez.
 */
@Service("portalUsersService")
public final class PortalUsersServiceImpl implements PortalUsersService {
	private static <T extends PortalUserSelDTO> T fillDTO(PortalUserEntity entity, T dto) {
		dto.setId(entity.getId().toString());
		dto.setUsername(entity.getName());
		dto.setDisplayName(entity.getDisplayName());
		dto.setEmail(entity.getEmail());
		dto.setActive(entity.isActive());
		return dto;
	}

	private static PortalUserDTO map2dto(PortalUserEntity entity) {
		final PortalUserDTO dto = fillDTO(entity, new PortalUserDTO());
		dto.setProperties(Maps.newHashMap(entity.getProperties()));
		dto.setRoles(Sets.newHashSet(entity.getRoles()));
		return dto;
	}

	private static final Function<PortalUserEntity, PortalUserSelDTO> MAP2SEL = new Function<PortalUserEntity, PortalUserSelDTO>() {
		public PortalUserSelDTO apply(PortalUserEntity from) {
			return fillDTO(from, new PortalUserSelDTO());
		}
	};

	/** DAO. */
	private PortalUserDAO dao;
	/** UUID Generator. */
	private UUIDGenerator generator;

	/**
	 * Constructor.
	 */
	public PortalUsersServiceImpl() {
	}

	@Autowired
	public void setDao(PortalUserDAO dao) {
		this.dao = dao;
	}

	@Autowired
	public void setGenerator(UUIDGenerator generator) {
		this.generator = generator;
	}

	private PortalUserEntity fill(PortalUserEntity entity, PortalUserDTO dto) {
		entity.setName(dto.getUsername());
		entity.setDisplayName(dto.getDisplayName());
		entity.setEmail(dto.getEmail());
		entity.setActive(dto.isActive());
		final Map<String, String> properties = entity.getProperties();
		properties.clear();
		final Map<String, String> dtop = dto.getProperties();
		if (dtop != null) {
			properties.putAll(Maps.filterKeys(Maps.filterValues(dtop, notNull()), notNull()));

		}
		final Set<String> roles = entity.getRoles();
		roles.clear();
		final Set<String> dtor = dto.getRoles();
		if (dtor != null) {
			roles.addAll(Sets.filter(dtor, notNull()));
		}
		return entity;
	}

	private PortalUserEntity load(String id) throws PortalUserNotFoundException {
		if (id == null) {
			throw new PortalUserNotFoundException(id);
		}
		final UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new PortalUserNotFoundException(id);
		}
		final PortalUserEntity entity = dao.findById(uuid, false);
		if (entity == null) {
			throw new PortalUserNotFoundException(id);
		}
		return entity;
	}

	@Transactional(rollbackFor = Throwable.class)
	public List<PortalUserSelDTO> getUsers() {
		return Lists.newArrayList(Iterables.transform(dao.findAll(), MAP2SEL));
	}

	@Transactional(rollbackFor = Throwable.class)
	public void changePassword(String id, String password) throws PortalUserNotFoundException {
		load(id).setPassword(password);
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO checkPassword(String username, String password) {
		if (username == null) {
			return null;
		}
		final PortalUserEntity entity = dao.getByName(username);
		if (entity == null) {
			return null;
		}
		if (!entity.isActive()) {
			return null;
		}
		final String pwd = entity.getPassword();
		if (pwd != null && !pwd.equals(password)) {
			return null;
		}
		return map2dto(entity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO create(PortalUserDTO user, String password) throws PortalUserException {
		Preconditions.checkNotNull(user);
		final String username = user.getUsername();
		Preconditions.checkNotNull(username);
		Preconditions.checkNotNull(user.getDisplayName());
		if (dao.getByName(username) != null) {
			throw new DuplicatePortalUserException(username);
		}
		final PortalUserEntity entity = new PortalUserEntity();
		final UUID id = generator.get();
		entity.setId(id);
		entity.setPassword(password);
		fill(entity, user);
		dao.save(entity);
		dao.sync();
		return map2dto(dao.findById(id, false));
	}

	@Transactional(rollbackFor = Throwable.class)
	public void delete(String id) throws PortalUserNotFoundException {
		final PortalUserEntity entity = load(id);
		dao.delete(entity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getById(String id) throws PortalUserNotFoundException {
		return map2dto(load(id));
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getByName(String name) {
		if (name == null) {
			return null;
		}
		final PortalUserEntity entity = dao.getByName(name);
		if (entity == null) {
			return null;
		}
		return map2dto(entity);
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO update(PortalUserDTO user) throws PortalUserException {
		Preconditions.checkNotNull(user);
		final String id = user.getId();
		Preconditions.checkNotNull(id);
		Preconditions.checkNotNull(user.getUsername());
		Preconditions.checkNotNull(user.getDisplayName());
		final PortalUserEntity entity = load(id);
		if (dao.getByName(entity.getId(), user.getUsername()) != null) {
			throw new DuplicatePortalUserException(user.getId(), user.getUsername());
		}
		fill(entity, user);
		dao.sync();
		return getById(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public PortalUserDTO getByEMail(String email) {
		if (email == null) {
			return null;
		}
		final PortalUserEntity entity = dao.getByEmail(email);
		if (entity == null) {
			return null;
		}
		return map2dto(entity);
	}
}
