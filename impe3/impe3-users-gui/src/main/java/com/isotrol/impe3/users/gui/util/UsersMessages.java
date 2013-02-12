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

package com.isotrol.impe3.users.gui.util;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	/home/acojocaru/workspace-gwt/impe3/impe3-users-gui/target/classes/com/isotrol/impe3/users/gui/util/UsersMessages.properties'.
 */
public interface UsersMessages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "Servicio de Gestión de Usuarios".
   * 
   * @return translated "Servicio de Gestión de Usuarios"
   */
  @DefaultMessage("Servicio de Gestión de Usuarios")
  @Key("appName")
  String appName();

  /**
   * Translated "Gestión de Usuarios".
   * 
   * @return translated "Gestión de Usuarios"
   */
  @DefaultMessage("Gestión de Usuarios")
  @Key("headerUsersManagementMenu")
  String headerUsersManagementMenu();

  /**
   * Translated "Activo".
   * 
   * @return translated "Activo"
   */
  @DefaultMessage("Activo")
  @Key("labelActivatedField")
  String labelActivatedField();

  /**
   * Translated "Nombre".
   * 
   * @return translated "Nombre"
   */
  @DefaultMessage("Nombre")
  @Key("labelDisplayNameField")
  String labelDisplayNameField();

  /**
   * Translated "Email".
   * 
   * @return translated "Email"
   */
  @DefaultMessage("Email")
  @Key("labelEmailField")
  String labelEmailField();

  /**
   * Translated "Contraseña".
   * 
   * @return translated "Contraseña"
   */
  @DefaultMessage("Contraseña")
  @Key("labelPasswordField")
  String labelPasswordField();

  /**
   * Translated "Repetir contraseña".
   * 
   * @return translated "Repetir contraseña"
   */
  @DefaultMessage("Repetir contraseña")
  @Key("labelRepeatPasswordField")
  String labelRepeatPasswordField();

  /**
   * Translated "Nombre de Usuario".
   * 
   * @return translated "Nombre de Usuario"
   */
  @DefaultMessage("Nombre de Usuario")
  @Key("labelUserNameField")
  String labelUserNameField();

  /**
   * Translated "No se pudo cambiar la contraseña del usuario.".
   * 
   * @return translated "No se pudo cambiar la contraseña del usuario."
   */
  @DefaultMessage("No se pudo cambiar la contraseña del usuario.")
  @Key("msgErrorChangingUserPassword")
  String msgErrorChangingUserPassword();

  /**
   * Translated "Ha ocurrido un error al crear el usuario.".
   * 
   * @return translated "Ha ocurrido un error al crear el usuario."
   */
  @DefaultMessage("Ha ocurrido un error al crear el usuario.")
  @Key("msgErrorCreatingUser")
  String msgErrorCreatingUser();

  /**
   * Translated "No se pudieron obtener los datos del usuario.".
   * 
   * @return translated "No se pudieron obtener los datos del usuario."
   */
  @DefaultMessage("No se pudieron obtener los datos del usuario.")
  @Key("msgErrorGettingUserData")
  String msgErrorGettingUserData();

  /**
   * Translated "Ha ocurrido un error al actualizar el usuario.".
   * 
   * @return translated "Ha ocurrido un error al actualizar el usuario."
   */
  @DefaultMessage("Ha ocurrido un error al actualizar el usuario.")
  @Key("msgErrorUpdatingUser")
  String msgErrorUpdatingUser();

  /**
   * Translated "Contraseña cambiada correctamente.".
   * 
   * @return translated "Contraseña cambiada correctamente."
   */
  @DefaultMessage("Contraseña cambiada correctamente.")
  @Key("msgPasswordChangedSuccessfully")
  String msgPasswordChangedSuccessfully();

  /**
   * Translated "Las contraseñas no coinciden.".
   * 
   * @return translated "Las contraseñas no coinciden."
   */
  @DefaultMessage("Las contraseñas no coinciden.")
  @Key("msgPasswordsDoNotMatch")
  String msgPasswordsDoNotMatch();

  /**
   * Translated "Usuario creado correctamente.".
   * 
   * @return translated "Usuario creado correctamente."
   */
  @DefaultMessage("Usuario creado correctamente.")
  @Key("msgUserCreatedSuccessfully")
  String msgUserCreatedSuccessfully();

  /**
   * Translated "Usuario actualizado correctamente.".
   * 
   * @return translated "Usuario actualizado correctamente."
   */
  @DefaultMessage("Usuario actualizado correctamente.")
  @Key("msgUserUpdatedSuccessfully")
  String msgUserUpdatedSuccessfully();
}
