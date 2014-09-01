package com.isotrol.impe3.idx.oc;


import org.apache.lucene.document.Document;


/**
 * 
 * @author Emilio Escobar Reyero
 * 
 * @param <T>
 */
public interface DocumentContentBuilder<T> {

	Document[] createDocuments(T task);

}
