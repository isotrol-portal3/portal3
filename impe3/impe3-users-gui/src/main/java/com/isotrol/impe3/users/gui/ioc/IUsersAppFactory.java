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

/**
 * 
 */
package com.isotrol.impe3.users.gui.ioc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.isotrol.impe3.users.gui.UsersViewport;
import com.isotrol.impe3.users.gui.users.ChangePwdWindow;
import com.isotrol.impe3.users.gui.users.UserEditorPanel;
import com.isotrol.impe3.users.gui.users.UsersManagement;

/**
 * Objects factory and injector for Users Management App
 * 
 * @author Andrei Cojocaru
 *
 */
@GinModules(UsersGinModule.class)
public interface IUsersAppFactory extends Ginjector {
	
	/**
	 * @return the Users app viewport
	 */
	UsersViewport getUsersViewport();
	
	/**
	 * @return a new Users editor window.
	 */
	UserEditorPanel getUserEditorPanel();
	
	/**
	 * @return a new Change Password window.
	 */
	ChangePwdWindow getChangePwdWindow();
	
	/**
	 * @return a new Users Management widget.
	 */
	UsersManagement getUsersManagement();
}
