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

package com.isotrol.impe3.pms.gui.client.util;


/**
 * Describe operaciones que realizan los paneles de edición de páginas.<br/>
 * 
 * Los paneles de edición de propiedades de páginas se dividen en dos subgrupos: los de <i>edición</i> de una página que
 * ya está en el sistema, y los de <i>creación</i> de una página nueva. Los dos tipos difieren únicamente en unas
 * etiquetas ("Creación" vs "Edición", botón "Crear" vs botón "Editar" etc) y en el comportamiento del botón de
 * guardado, que debe aplicar los cambios o guardar el objeto recién creado.<br/> Por no duplicar codigo, estos cambios
 * se implementan en los propios paneles, con ayuda de este enumerado.
 * 
 * @author Andrei Cojocaru
 * 
 * @see TemplatePageEdition
 * @see NavigationPageEdition
 * @see ContentPageEdition
 */
public enum EDataOperation {
	/**
	 * edición de un objeto del sistema
	 */
	EDIT,
	/**
	 * creación de un nuevo objeto
	 */
	CREATE
}
