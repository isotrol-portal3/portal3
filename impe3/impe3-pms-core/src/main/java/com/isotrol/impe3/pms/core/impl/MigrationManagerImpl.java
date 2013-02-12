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


import net.derquinse.common.log.ContextLog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.core.MigrationManager;
import com.isotrol.impe3.pms.model.Definition;
import com.isotrol.impe3.pms.model.PublishableEntity;
import com.isotrol.impe3.pms.model.PublishedEntity;


/**
 * Implementation of Migration Manager.
 * @author Andres Rodriguez.
 */
@Service
public final class MigrationManagerImpl extends AbstractService implements MigrationManager {
	private final ContextLog baseLog = ContextLog.of(Loggers.pms());

	/** Constructor. */
	public MigrationManagerImpl() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.MigrationManager#publishedFlag()
	 */
	@Transactional(rollbackFor = Throwable.class)
	public void publishedFlag() {
		ContextLog base = baseLog.to("PFM");
		base.info("Starting published flag migration.");
		publishedFlag(base.to("Content Types"), getDao().getPFMContentTypes());
		publishedFlag(base.to("Categories"), getDao().getPFMCategories());
		publishedFlag(base.to("Connectors"), getDao().getPFMConnectors());
		publishedFlag(base.to("Portals"), getDao().getPFMPortals());
		base.info("Finished published flag migration.");
	}

	private <E extends PublishableEntity<E, D, P>, D extends Definition<D, E, P>, P extends PublishedEntity> void publishedFlag(
		ContextLog log, Iterable<E> entities) {
		for (E entity : entities) {
			ContextLog elog = log.to(entity.getId().toString());
			elog.info("Detected");
			boolean ever = false;
			for (D dfn : entity.getDefinitions()) {
				elog.info("Migrating definition %s", dfn.getId());
				Boolean dfnEver = dfn.getEverPublished();
				if (dfnEver == null) {
					dfnEver = !dfn.getEditions().isEmpty();
					dfn.setEverPublished(dfnEver);
				}
				ever |= dfnEver;
			}
			elog.info("Ever published: %s", ever);
			entity.setEverPublished(ever);
		}

	}
}
