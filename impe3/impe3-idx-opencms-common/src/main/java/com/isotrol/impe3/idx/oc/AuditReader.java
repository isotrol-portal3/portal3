package com.isotrol.impe3.idx.oc;


import java.util.List;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public interface AuditReader<T, C> {

	List<T> readAuditBatch(C checkpoint);

}
