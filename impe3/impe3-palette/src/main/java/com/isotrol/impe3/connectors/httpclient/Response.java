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

import java.io.UnsupportedEncodingException;

/**
 * Response dto
 * @author Emilio Escobar Reyero
 */
public class Response {

	private final int code;
	private final byte[] content;
	private final String enc;
	private final String type;

	private Response(int code, byte[] content, String type, String enc) {
		this.content = content;
		this.type = type;
		this.enc = enc;
		this.code = code;
	}

	/**
	 * Factory. Create a Response with a concrete code.
	 * @param code response code
	 * @return new response object
	 */
	public static Response code(int code) {
		return new Response(code, null, null, null);
	}
	
	/**
	 * Create a new response builder
	 * @return new response builder
	 */
	public static Builder builder() {
		return new Builder(); 
	}
	
	public byte[] getContent() {
		return content;
	}
	public String getString() {
		if (content == null) {
			return null;
		}
		
		if (enc == null) {
			return new String(content);
		}
		
		try {
			return new String(content, enc);
		} catch (UnsupportedEncodingException e) {
			return new String(content);
		}
	}
	public String getEnc() {
		return enc;
	}
	public String getType() {
		return type;
	}
	public int getCode() {
		return code;
	}
	
	/**
	 * Response builder. 
	 * @author Emilio Escobar Reyero
	 */
	public static class Builder {
		private int code;
		private byte[] content;
		private String enc;
		private String type;
		
		private Builder() {
		}

		public void setCode(int code) {
			this.code = code;
		}
		public void setContent(byte[] content) {
			this.content = content;
		}
		public void setEnc(String enc) {
			this.enc = enc;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		/**
		 * Create a new Response object
		 * @return new response object
		 */
		public Response get() {
			return new Response(code, content, enc, type);
		}
	}
	
}
