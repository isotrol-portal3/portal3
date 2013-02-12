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

package com.isotrol.impe3.connectors.httpclient.din;


import com.google.common.collect.Multimap;
import com.isotrol.impe3.connectors.httpclient.Response;

/**
 * HttpClient connector service
 * @author Emilio Escobar Reyero
 */
public interface HttpClientConnector {

	/**
	 * Do a get call to url
	 * @param url recover url
	 * @return response object
	 */
	public Response get(String url);
	/**
	 * Do a get call to url
	 * @param url recover url
	 * @param content if true recover content bytes too
	 * @return response object
	 */
	public Response get(String url, boolean content);
	/**
	 * Do a post call to url with some params
	 * @param url action url
	 * @param params params values
	 * @return response object
	 */
	public Response post(String url, Multimap<String, String> params);
	/**
	 * Do a post call to url with some params
	 * @param url action url
	 * @param params params values
	 * @param conenct if true recover content bytes too
	 * @return response object
	 */
	public Response post(String url, Multimap<String, String> params, boolean content);
	
}
