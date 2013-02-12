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

package com.isotrol.impe3.connectors.hessian;


import java.net.MalformedURLException;
import java.util.Map;

import net.sf.derquinse.lucis.Result;

import com.caucho.hessian.client.HessianProxyFactory;
import com.isotrol.impe3.nr.api.NodeRepositoriesService;
import com.isotrol.impe3.nr.api.NodeRepositoryRequest;


/**
 * Remote hessian NodeRepositoriesService implementation.
 * @author Andres Rodriguez
 */
public class NodeRepositoriesServiceHessian implements NodeRepositoriesService {
	/** Remote service. */
	private NodeRepositoriesService remote;
	/** Service config. */
	private NRServiceConfig config;

	/** Default constructor. */
	public NodeRepositoriesServiceHessian() {
	}

	/* Spring injection. */

	public void setConfig(NRServiceConfig config) {
		this.config = config;
	}

	/**
	 * Init method exec after properties set
	 * @throws MalformedURLException throwed if hessian url is malformed
	 */
	public void init() throws MalformedURLException {
		HessianProxyFactory factory = new HessianProxyFactory();
		factory.setHessian2Reply(true);
		factory.setHessian2Request(true);
		remote = (NodeRepositoriesService) factory.create(NodeRepositoriesService.class, config.serviceUrl());
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepositoriesService#getRepositories()
	 */
	public Map<String, String> getRepositories() {
		return remote.getRepositories();
	}

	/**
	 * @see com.isotrol.impe3.nr.api.NodeRepositoriesService#execute(java.lang.String,
	 * com.isotrol.impe3.nr.api.NodeRepositoryRequest)
	 */
	public Result execute(String repositoryKey, NodeRepositoryRequest request) {
		return remote.execute(repositoryKey, request);
	}
}
