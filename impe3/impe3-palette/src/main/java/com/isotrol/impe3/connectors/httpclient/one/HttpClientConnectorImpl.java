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

package com.isotrol.impe3.connectors.httpclient.one;

import org.slf4j.Logger;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.connectors.Loggers;
import com.isotrol.impe3.connectors.httpclient.AbstractHttpClientConnector;
import com.isotrol.impe3.connectors.httpclient.Response;


/**
 * HttpClient connector implmentation 
 * @author Emilio Escobar Reyero
 */
public class HttpClientConnectorImpl extends AbstractHttpClientConnector implements HttpClientConnector {

	final Logger logger = Loggers.connectors();

	private String url;

	/**
	 * @see com.isotrol.impe3.connectors.httpclient.one.HttpClientConnector#get()
	 */
	public Response get() {
		return doGet(url, false);
	}

	/**
	 * @see com.isotrol.impe3.connectors.httpclient.one.HttpClientConnector#get(boolean)
	 */
	public Response get(boolean content) {
		return doGet(url, content);
	}

	/**
	 * @see com.isotrol.impe3.connectors.httpclient.one.HttpClientConnector#post(com.google.common.collect.Multimap)
	 */
	public Response post(Multimap<String, String> params) {
		return doPost(url, params, false);
	}

	/**
	 * @see com.isotrol.impe3.connectors.httpclient.one.HttpClientConnector#post(com.google.common.collect.Multimap, boolean)
	 */
	public Response post(Multimap<String, String> params, boolean content) {
		return doPost(url, params, content);
	}


	/**
	 * Set configuration values
	 */
	public void setConfig(HttpClientConfig config) {
		this.url = config.url();
		this.user = config.user() != null && !"".equals(config.user()) ? config.user() : null;
		this.pass = config.pass() != null && !"".equals(config.pass()) ? config.pass() : null;
		this.scopeport = config.scopeport() != null && !"".equals(config.scopeport()) ? config.scopeport() : 80;
		this.scopeserver = config.scopeserver() != null && !"".equals(config.scopeserver()) ? config.scopeserver()
			: null;
		this.timeout = config.timeout() != null && !"".equals(config.timeout()) ? config.timeout() : 30;
	}

}
