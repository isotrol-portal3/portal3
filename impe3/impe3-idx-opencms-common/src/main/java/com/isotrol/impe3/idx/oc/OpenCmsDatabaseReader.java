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
package com.isotrol.impe3.idx.oc;


import java.io.InputStream;
import java.util.List;

import com.isotrol.impe3.idx.XML;
import com.isotrol.impe3.idx.oc.OpenCmsContent.OpenCmsContentBuilder;


/**
 * Database reader test oriented interface.
 * 
 * @author Emilio Escobar Reyero
 * @modified Juan Manuel Valverde Ramírez
 */
public interface OpenCmsDatabaseReader {

	/**
	 * Create a content builder from content id
	 * @param id content original id
	 * @return the builder
	 */
	OpenCmsContentBuilder createBuilder(String id);

	/**
	 * Reads and parse xml
	 * @param id content id
	 * @return xml
	 */
	XML readContentXml(String id);

	/**
	 * Reads content bytes
	 * @param id content id
	 * @return String content.
	 */
	String readContentBytes(String id);

	/**
	 * Reads content properties
	 * @param id content id
	 * @return string arrays list
	 */
	List<String[]> readContentProperties(String id);

	/**
	 * Reads content categories
	 * @param id content id
	 * @return list of categories ids
	 */
	List<String> readContentCategories(String id);

	/**
	 * Lee los ficheros adjuntos al id pasado.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	List<String> readAttached(String id);

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	List<Attached> readAttachedIds(String id);

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	byte[] readAttachedBytes(String id);

	/**
	 * Lee los ficheros adjuntos al id pasado y devuelve el id del resource del fichero adjunto.
	 * @author Juan Manuel Valverde Ramírez
	 * @param id
	 * @return Devuelve una lista con los "id" de los ficheros adjuntos.
	 */
	InputStream readAttachedInputStream(String id);

}
