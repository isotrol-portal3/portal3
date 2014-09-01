package com.isotrol.impe3.palette.oc7.loader;

import com.isotrol.impe3.api.component.Extract;
import com.isotrol.impe3.api.content.Content;
import com.isotrol.impe3.api.content.ContentCriteria;


/**
 * Abstract loader for content components.
 */
public abstract class AbstractContentComponent extends AbstractLoaderComponent {
	
	/** Content. */
	private Content content;

	/**
	 * Constructor.
	 */
	AbstractContentComponent() {
	}

	@Override
	final void load(ContentCriteria criteria) {
		this.content = loadContent(criteria);
	}

	abstract Content loadContent(ContentCriteria criteria);

	@Extract
	public Content getContent() {
		return content;
	}
}
