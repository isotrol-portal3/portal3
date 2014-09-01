package com.isotrol.impe3.idx.oc;


import nu.xom.Node;

public abstract class IndexCommandEcho implements IndexCommand {

	/**
	 * @see com.isotrol.impe3.idx.oc.IndexCommand#execute(nu.xom.Node)
	 */
	public String execute(Node value) {
		return value.toXML();
	}
	/**
	 * @see com.isotrol.impe3.idx.oc.IndexCommand#execute(java.lang.String)
	 */
	public String execute(String value) {
		return value;
	}
	
	
	
}
