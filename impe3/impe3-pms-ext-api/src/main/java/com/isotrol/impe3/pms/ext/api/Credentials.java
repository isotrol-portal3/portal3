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

package com.isotrol.impe3.pms.ext.api;

import java.io.Serializable;

/**
 * 
 * @author Emilio Escobar Reyero
 */
public abstract class Credentials implements Serializable {
	private static final long serialVersionUID = 7927735604589555345L;

	public abstract Object get(String key);
	
	public static final class BasicCredentials extends Credentials {
		private static final long serialVersionUID = 7122060197215889562L;

		private String login;
		private String pass;

		public BasicCredentials() {
		}
		
		public BasicCredentials setLogin(String login) {
			this.login = login;
			return this;
		}
		
		public BasicCredentials setPassword(String pass) {
			this.pass = pass;
			return this;
		}
	
		public String getLogin() {
			return login;
		}
		
		public String getPassword() {
			return pass;
		}
		
		@Override
		public Object get(String key) {
			if (key == null) {
				return null;
			} else if ("_BASIC_LOGIN".equals(key)) {
				return login;
			} else if ("_BASIC_PASSWORD".equals(key)) {
				return pass;
			} else {
				return null;
			}
		}
		
	}
	
}
