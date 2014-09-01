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
