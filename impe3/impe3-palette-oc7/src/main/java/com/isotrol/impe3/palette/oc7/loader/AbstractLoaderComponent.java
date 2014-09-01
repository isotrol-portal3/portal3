package com.isotrol.impe3.palette.oc7.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;


/**
 * Abstract OpenCMS 7 loader component.
 */
public abstract class AbstractLoaderComponent implements Component {
	
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/** Content loader. */
	private ContentLoader contentLoader;
	
	/**
	 * Constructor.
	 */
	AbstractLoaderComponent() {
	}
	
	@Inject
	public void setContentLoader(ContentLoader contentLoader) {
		this.contentLoader = contentLoader;
	}

	public final ComponentResponse execute() {
		final ContentCriteria criteria = contentLoader.newCriteria();
		criteria.setBytes(true);
		load(criteria);
		
		return ComponentResponse.OK;
	}

	abstract void load(ContentCriteria criteria);
}
