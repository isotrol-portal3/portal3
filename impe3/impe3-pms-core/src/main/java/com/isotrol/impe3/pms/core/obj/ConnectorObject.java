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

package com.isotrol.impe3.pms.core.obj;


import com.google.common.base.Function;
import com.isotrol.impe3.pbuf.connector.ConnectorProtos.ConnectorPB;
import com.isotrol.impe3.pms.api.State;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.StateSupport;
import com.isotrol.impe3.pms.model.ConnectorDfn;


/**
 * Connector domain object.
 * @author Andres Rodriguez
 * @author Emilio Escobar Reyero
 */
public final class ConnectorObject extends ModuleObject implements PublishableObject {
	static Function<ConnectorObject, ConnectorPB> map2pb(final FileManager fileManager) {
		return new Function<ConnectorObject, ConnectorPB>() {
			public ConnectorPB apply(ConnectorObject from) {
				return from.toPB(fileManager);
			}
		};
	}

	/** Connector state. */
	private final State state;

	/**
	 * Constructor.
	 * @param dfn Definition.
	 */
	ConnectorObject(ConnectorDfn dfn) {
		super(dfn);
		this.state = StateSupport.calculate(dfn);
	}

	@Override
	public State getState() {
		return state;
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @return The PB message.
	 */
	final ConnectorPB toPB(FileManager fileManager) {
		ConnectorPB.Builder b = ConnectorPB.newBuilder();

		b.setModule(modulePB(fileManager));

		return b.build();
	}

}
