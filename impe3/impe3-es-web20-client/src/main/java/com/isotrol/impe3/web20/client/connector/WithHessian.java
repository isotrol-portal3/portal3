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
package com.isotrol.impe3.web20.client.connector;

import java.net.MalformedURLException;

import com.caucho.hessian.client.HessianProxyFactory;
import com.google.common.collect.ForwardingObject;
import com.isotrol.impe3.web20.api.Web20Service;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public abstract class WithHessian<T extends Web20Service> extends ForwardingObject {

	private Web20ConnectorConfig config;
	private T service;
	
	
	@Override
	protected T delegate() {
		return service;
	}
	
	protected abstract Class<T> serviceClass();
	protected abstract String serviceUrl();
	
	public void init() throws MalformedURLException {
		final HessianProxyFactory factory = new HessianProxyFactory();
		factory.setHessian2Reply(true);
		factory.setHessian2Request(true);
		@SuppressWarnings("unchecked")
		final T s = (T)factory.create(serviceClass(), serviceUrl());
		this.service = s;
	}
	
	public String server() {
		return config.serverUrl();
	}
	
	
	public void setConfig(Web20ConnectorConfig config) {
		this.config = config;
	}
	
}
