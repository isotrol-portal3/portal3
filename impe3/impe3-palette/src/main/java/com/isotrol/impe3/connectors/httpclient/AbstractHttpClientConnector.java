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

package com.isotrol.impe3.connectors.httpclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.connectors.Loggers;

/**
 * Base class for http client connectors. 
 * @author Emilio Escobar Reyero
 */
public abstract class AbstractHttpClientConnector {
	final Logger logger = Loggers.connectors();
	
	protected DefaultHttpClient httpclient;

	protected int timeout;
	protected int scopeport;
	protected String scopeserver;
	protected String user;
	protected String pass;


	/**
	 * Create new default http client.
	 */
	public void init() {
		httpclient = new DefaultHttpClient();

		if (isAuthenticated()) {
			httpclient.getCredentialsProvider().setCredentials(new AuthScope(scopeserver, scopeport),
				new UsernamePasswordCredentials(user, pass));
		}
	}

	/**
	 * Shutdown all http conections
	 */
	public void ends() {
		httpclient.getConnectionManager().shutdown();
	}


	private boolean isAuthenticated() {
		return user != null && pass != null;
	}

	
	protected Response doGet(String url, boolean bytes) {
		Response res;
		final HttpGet httpget = new HttpGet(url);

		try {
			final HttpResponse response = httpclient.execute(httpget);
			res = http(response, bytes);
		} catch (ClientProtocolException e) {
			res = Response.code(400);
			logger.warn("ClientProtocolException GET {}", url);
			logger.trace("Error trace, ", e);
		} catch (IOException e) {
			res = Response.code(400);
			logger.warn("IOException GET {}", url);
			logger.trace("Error trace, ", e);
		}
		finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().closeIdleConnections(timeout, TimeUnit.SECONDS);
		}

		return res;
	}

	protected Response doPost(String url, Multimap<String, String> params, boolean bytes) {
		Response res;
		final HttpPost httppost = new HttpPost(url);
		final List<NameValuePair> nvps = TOLIST.apply(params);

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			final HttpResponse response = httpclient.execute(httppost);

			res = http(response, bytes);
		} catch (UnsupportedEncodingException e) {
			res = Response.code(400);
			logger.warn("UnsupportedEncodingException POST {}", url);
			logger.trace("Error trace, ", e);
		} catch (ClientProtocolException e) {
			res = Response.code(400);
			logger.warn("ClientProtocolException POST {}", url);
			logger.trace("Error trace, ", e);
		} catch (IOException e) {
			res = Response.code(400);
			logger.warn("IOException POST {}", url);
			logger.trace("Error trace, ", e);
		}
		finally {
			httpclient.getConnectionManager().closeExpiredConnections();
			httpclient.getConnectionManager().closeIdleConnections(timeout, TimeUnit.SECONDS);
		}

		return res;

	}
	
	private Response http(HttpResponse response, boolean bytes) {
		final StatusLine status = response.getStatusLine();

		final int code = status.getStatusCode();
		final Response.Builder builder = Response.builder();
		builder.setCode(code);
		
		
		if (code >= 200 && code < 300) {
			final HttpEntity entity = response.getEntity();

			if (entity != null) {
				
				//final long length = entity.getContentLength();
				
				final Header contentType = entity.getContentType();
				if (contentType != null) {
					String value = contentType.getValue();
					if (value != null) {
						int pos = value.indexOf(";"); 
						
						if (pos > 0) {
							builder.setType(value.substring(0, pos).trim());
							String charset = value.substring(pos+1).trim();
							builder.setEnc(charset.substring(charset.indexOf("=") + 1).trim());
						} else {
							builder.setType(value.trim());
						}
					}
				}
				
				if (bytes) {

					try {
						BufferedInputStream in = new BufferedInputStream(entity.getContent());
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						
						byte buffer[] = new byte[1024];
						int leidos;
						while((leidos=in.read(buffer,0,1024))!=-1){
						   out.write(buffer,0,leidos);
						}
						
						builder.setContent(out.toByteArray());
						in.close();
						out.close();
						
					} catch (IllegalStateException e) {
						logger.warn("IllegalStateException");
						logger.trace("Error trace, ", e);
					} catch (IOException e) {
						logger.warn("IOException");
						logger.trace("Error trace, ", e);
					}
				}

				try {
					entity.consumeContent();
				} catch (IOException e) {
					logger.warn("IOException consumeContent");
					logger.trace("Error trace, ", e);
				}
			}
		}

		return builder.get();
	}

	protected static final Function<Multimap<String, String>, List<NameValuePair>> TOLIST = new Function<Multimap<String, String>, List<NameValuePair>>() {
		public List<NameValuePair> apply(Multimap<String, String> input) {
			List<NameValuePair> nvp = Lists.newArrayListWithExpectedSize(input.size());

			for (Map.Entry<String, String> entry : input.entries()) {
				nvp.add(TONVP.apply(entry));
			}

			return nvp;
		}
	};

	private static final Function<Map.Entry<String, String>, NameValuePair> TONVP = new Function<Map.Entry<String, String>, NameValuePair>() {
		public NameValuePair apply(Map.Entry<String, String> input) {
			return new BasicNameValuePair(input.getKey(), input.getValue());
		}
	};
}
