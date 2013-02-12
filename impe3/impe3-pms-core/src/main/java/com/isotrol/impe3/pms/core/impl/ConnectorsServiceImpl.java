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


import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.isotrol.impe3.core.modules.ModuleType;
import com.isotrol.impe3.pbuf.BaseProtos.ModulePB;
import com.isotrol.impe3.pbuf.connector.ConnectorProtos.ConnectorPB;
import com.isotrol.impe3.pbuf.connector.ConnectorProtos.ConnectorsPB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.mreg.ConnectorModuleDTO;
import com.isotrol.impe3.pms.core.ExportJobManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.ModuleInstanceComponent;
import com.isotrol.impe3.pms.core.obj.ConnectorObject;
import com.isotrol.impe3.pms.core.obj.ConnectorsObject;
import com.isotrol.impe3.pms.core.obj.ContextGlobal;
import com.isotrol.impe3.pms.core.obj.ModuleObject;
import com.isotrol.impe3.pms.core.obj.PortalObject;
import com.isotrol.impe3.pms.core.support.InUseProviders;
import com.isotrol.impe3.pms.core.support.NotFoundProvider;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.ConnectorDfn;
import com.isotrol.impe3.pms.model.ConnectorEdition;
import com.isotrol.impe3.pms.model.ConnectorEntity;
import com.isotrol.impe3.pms.model.ExportJobType;


/**
 * Implementation of ConnectorsService.
 * @author Andres Rodriguez.
 */
@Service("connectorsService")
public final class ConnectorsServiceImpl extends
	AbstractPublishableService<ConnectorEntity, ConnectorDfn, ConnectorEdition> implements ConnectorsService {
	private ModuleInstanceComponent moduleComponent;

	/** Export job manager. */
	@Autowired
	private ExportJobManager exportJobManager;
	/** File Manager. */
	@Autowired
	private FileManager fileManager;

	/** Default constructor. */
	public ConnectorsServiceImpl() {
	}

	@Autowired
	public void setModuleComponent(ModuleInstanceComponent moduleComponent) {
		this.moduleComponent = moduleComponent;
	}

	@Override
	protected NotFoundProvider getDefaultNFP() {
		return NotFoundProviders.CONNECTOR;
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#delete(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_SET)
	public void delete(String id) throws PMSException {
		final ContextGlobal ctx = loadContextGlobal();
		// 1 - Load connector
		final ConnectorObject c = ctx.loadConnector(id);
		// 2 - Check if in use.
		InUseProviders.CONNECTOR.checkUsed(ctx.isConnectorUsed(c.getId()), id);
		for (PortalObject p : ctx.getPortals().values()) {
			InUseProviders.CONNECTOR.checkUsed(ctx.toPortal(p.getId()).isConnectorUsed(c.getId()), id);
		}
		// 3 - Delete
		loadConnectorEntity(id).setDeleted(true);
		// 4 - Touch environment
		getEnvironment().touchConnectorVersion(loadUser());
		// 5 - Done
		sync();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#get(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public ModuleInstanceTemplateDTO get(String id) throws PMSException {
		return loadContextGlobal().getConnectorTemplate(id);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#getConnectorModules()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public List<ConnectorModuleDTO> getConnectorModules() throws PMSException {
		return loadContextGlobal().getConnectorModules();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#getConnectors()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public List<ModuleInstanceSelDTO> getConnectors() throws PMSException {
		return loadContextGlobal().getConnectorsSel();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#getConnectorsByCorrectness(com.isotrol.impe3.pms.api.Correctness)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public List<ModuleInstanceSelDTO> getConnectorsByCorrectness(Correctness correctness) throws PMSException {
		return loadContextGlobal().getConnectorsSel(correctness);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#newTemplate(java.lang.String)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_SET)
	public ModuleInstanceTemplateDTO newTemplate(String key) throws PMSException {
		return ModuleObject.template(loadContextGlobal(), key, ModuleType.CONNECTOR);
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#save(com.isotrol.impe3.pms.api.minst.ModuleInstanceDTO)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_SET)
	public ModuleInstanceTemplateDTO save(ModuleInstanceDTO dto) throws PMSException {
		Preconditions.checkNotNull(dto);
		final boolean isNew;
		final boolean newDfn;
		final ConnectorEntity entity;
		final ConnectorDfn dfn;
		final String id = dto.getId();
		// 1 - Load or create entity and/or definition.
		if (id == null) {
			isNew = true;
			newDfn = true;
			entity = new ConnectorEntity();
			dfn = new ConnectorDfn();
		} else {
			isNew = false;
			entity = load(id);
			final ConnectorDfn current = entity.getCurrent();
			Preconditions.checkArgument(current.getModuleClass().equals(dto.getKey()));
			newDfn = isNewDfnNeeded(current);
			if (newDfn) {
				dfn = new ConnectorDfn();
				dfn.setEntity(current.getEntity());
			} else {
				dfn = current;
			}
		}
		// 3 - Fill definition.
		moduleComponent.fill(dto, dfn, isNew);
		// 4 - Save if needed.
		if (isNew) {
			saveNewEntity(entity, dfn);
		} else if (newDfn) {
			saveNewDfn(entity, dfn);
		}
		// 5 - Sync and return template.
		touchConnector();
		sync();
		return get(entity.getId().toString());
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#exportAll()
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public String exportAll() throws PMSException {
		return exportJobManager.create(ExportJobType.CONNECTOR_ALL, null, null, null).toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#exportSome(java.util.Set)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_GET)
	public String exportSome(Set<String> ids) throws PMSException {
		final ConnectorsObject cnns = loadContextGlobal().getConnectors();
		final Set<UUID> set = Sets.newHashSet();
		for (String id : ids) {
			set.add(cnns.load(id).getId());
		}

		return exportJobManager.create(ExportJobType.CONNECTOR, null, null, set).toString();
	}

	/**
	 * @see com.isotrol.impe3.pms.api.connector.ConnectorsService#importConnectors(java.lang.String, boolean)
	 */
	@Transactional(rollbackFor = Throwable.class)
	@Authorized(global = GlobalAuthority.CONNECTOR_SET)
	public void importConnectors(String fileId, boolean overwrite) throws PMSException {
		final ConnectorsPB msg = fileManager.parseImportFile(fileId, ConnectorsPB.newBuilder(), true).build();
		try {
			for (ConnectorPB cn : msg.getConnectorsList()) {
				importConnector(cn, overwrite);
			}
		}
		finally {
			purge();
		}
	}

	private void importConnector(ConnectorPB cn, boolean overwrite) throws PMSException {
		final ConnectorsObject cnns = loadContextGlobal().getConnectors();
		final ModulePB m = cn.getModule();
		final UUID id;
		try {
			id = UUID.fromString(m.getId());
		} catch (RuntimeException e) {
			return;
		}
		ConnectorEntity entity;
		boolean deleted = false;
		if (cnns.containsKey(id)) {
			entity = load(id);
		} else {
			entity = findById(id);
			if (entity != null && entity.isDeleted()) {
				deleted = true;
				entity.setDeleted(false);
				touchConnector();
				sync();
				importConnector(cn, overwrite);
				return;
			}
		}
		if (entity != null) {
			if (!overwrite && !deleted) {
				return;
			}
			ConnectorDfn dfn = entity.getCurrent();
			if (isNewDfnNeeded(dfn)) {
				dfn = new ConnectorDfn();
				dfn.setEntity(entity);
				moduleComponent.fill(cn.getModule(), dfn, false);
				saveNewDfn(entity, dfn);
			} else {
				moduleComponent.fill(cn.getModule(), dfn, false);
				getDao().update(dfn);
			}
		} else {
			entity = new ConnectorEntity();
			entity.setId(id);
			ConnectorDfn dfn = new ConnectorDfn();
			moduleComponent.fill(cn.getModule(), dfn, true);
			saveNewEntity(entity, dfn);
		}
		touchConnector();
		sync();
	}

}
