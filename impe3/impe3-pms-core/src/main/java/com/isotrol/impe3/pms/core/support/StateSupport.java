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

package com.isotrol.impe3.pms.core.support;


import java.util.UUID;

import net.derquinse.common.log.ContextLog;

import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.model.Definition;
import com.isotrol.impe3.pms.model.PublishableEntity;


/**
 * State calculation support class.
 * @author Andres Rodriguez
 */
public final class StateSupport {
	private static final ContextLog LOG = ContextLog.of(Loggers.pms()).to("State");

	/** Not instantiable. */
	private StateSupport() {
		throw new AssertionError();
	}

	/**
	 * Calculates a state from a definition.
	 * @param dfn Definition.
	 */
	public static State calculate(Definition<?, ?, ?> dfn) {
		final Boolean dfnEver = dfn.getEverPublished();
		final PublishableEntity<?, ?, ?> entity = dfn.getEntity();
		final Boolean entityEver = entity.getEverPublished();
		final UUID entityId = entity.getId();
		if (dfnEver == null) {
			LOG.to(entity.getEntityName()).warn("Definition %s of entity %s has null published flag", dfn.getId(),
				entityId);
			return State.NEW;
		} else if (entityEver == null) {
			LOG.to(entity.getEntityName()).warn("Entity %s has null published flag", entityId);
			return State.NEW;
		} else {
			if (dfn.getEntity().isDeleted()) {
				return State.UNPUBLISHED;
			} else if (dfnEver) {
				return State.PUBLISHED;
			} else if (entityEver) {
				return State.MODIFIED;
			} else {
				return State.NEW;
			}
		}
	}

	/** Ensures an entity has the published flag. */
	public static void check(PublishableEntity<?, ?, ?> entity) {
		if (entity.getEverPublished() == null) {
			String msg = String.format("Entity %s/%s has null published flag", entity.getEntityName(), entity.getId());
			LOG.error(msg);
			throw new IllegalStateException(msg);
		}
	}
}
