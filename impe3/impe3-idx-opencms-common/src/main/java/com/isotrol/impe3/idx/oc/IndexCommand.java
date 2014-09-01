package com.isotrol.impe3.idx.oc;

import nu.xom.Node;

/**
 * 
 * @author Antonio Castillo
 * @author Emilio Escobar Reyero
 *
 */
public interface IndexCommand {

	String execute(String value);
	String execute(Node value);
}
